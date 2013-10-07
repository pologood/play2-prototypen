/**
 * 
 */
package models.admin;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import models.Playground;
import models.admin.GameHall.Join;
import models.admin.GameHall.Quit;
import models.common.User;

import org.codehaus.jackson.JsonFactory;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;

import play.libs.Akka;
import play.libs.F.Callback;
import play.libs.F.Callback0;
import play.mvc.WebSocket;
import scala.concurrent.Await;
import scala.concurrent.duration.Duration;
import services.common.PlaygroundServiceImpl;
import akka.actor.ActorRef;
import akka.actor.Props;
import akka.actor.UntypedActor;

import static akka.pattern.Patterns.ask;
import static java.util.concurrent.TimeUnit.*;

/**
 * Eine Websocket-basierte Spielhalle als Akka-Aktor, die genau ein Spielfeld
 * verwaltet.
 * 
 * Es werden die folgenden Nachrichtentypen akzeptiert: 
 * 
 * <ul>
 * 	<li>Join: Beitritt zur Spielhalle</li>
 *  <li>Playground: Spielfelddaten</li>
 * </ul>
 * 
 * Nicht behandelt wird:
 * 
 * <ul>
 *  <li>Wirkliches Persistieren des Spielstands.</li>
 * </ul>
 * 
 * @author stefan.illgen
 * 
 */
@SuppressWarnings("deprecation")
public class GameHall extends UntypedActor {

	// Standard Spielhalle
	static ActorRef defaultHall = Akka.system().actorOf(
			new Props(GameHall.class));
	
	public static Playground playground = null;

	// Mitglieder der Spielhalle und der zugehörige Websocket-Out-Kanal.
	Map<User, WebSocket.Out<JsonNode>> members = new HashMap<User, WebSocket.Out<JsonNode>>();

	/**
	 * Tritt der Spielhalle bei.
	 */
	public static void join(final User user, WebSocket.In<JsonNode> in,
			WebSocket.Out<JsonNode> out) throws Exception {

		// Sende eine Join-Nachricht an die Spielhalle
		String result = (String) Await.result(
				ask(defaultHall, new Join(user, out), 1000),
				Duration.create(10, SECONDS));

		// Wenn der Zutritt genehmigt wurde, ...
		if ("OK".equals(result)) {

			// Für jede empfangene Nachricht (Spielfelddaten) ...
			in.onMessage(new Callback<JsonNode>() {
				public void invoke(JsonNode event) throws Throwable {

					ObjectMapper mapper = new ObjectMapper();
					Playground aPlayground = mapper.readValue(event,
							new TypeReference<Playground>() {
							});

					// Sende die Spielfelddaten an die Spielhalle.
					defaultHall.tell(aPlayground);
				}
			});
			
			// Wenn der Websocket geschlossen wird, ...
            in.onClose(new Callback0() {
               public void invoke() {
                   
                   // Sende eine Quit-Nachricht zum Entfernen des Nutzers aus der Map.
                   defaultHall.tell(new Quit(user));
                   
               }
            });
		}
	}

	@Override
	public void onReceive(Object message) throws Exception {
		
		// Bei einem Beitrittsgesuch ...
		if(message instanceof Join) {
            
            Join join = (Join)message;
            if(members.containsKey(join.user)) {
                getSender().tell("This user is already used");
            } else {
                // .. füge den Anwender dem Spielfeld hinzu ..
            	members.put(join.user, join.channel);
                // .. und pushe die aktuellen Spielfelddaten auf den Client ..
                pushPlayground(join.channel, playground);
                // sende dem Sender-Aktor ein Okay
                getSender().tell("OK");
            }
            
        } else 
        // Bei aktualisierten Spielfelddaten ..	
        if(message instanceof Playground)  {            
        	Playground aPlayground = (Playground) message;
        	// "Pseudo-Persistieren"
        	playground = aPlayground;        	
        	// .. benachrichtige Mitspieler  durch pushen der Spielfelddaten zum Client.
        	notifyAll(aPlayground);           
        } else
        // Beim Schließen des Websockets (z.B. infolge eines Routenwechsels) ..
        if(message instanceof Quit) {
        	Quit quit = (Quit)message;
        	// .. entferne den Websocket aus der Map
            members.remove(quit.user);
        } else {
        	unhandled(message);
        }
	}

	/**
	 * Für alle Teilnehmer, push die Spielfelddaten zum Client.
	 * 
	 * @param aPlayground
	 */
	public void notifyAll(Playground aPlayground) {
		for (WebSocket.Out<JsonNode> channel : members.values()) {
			pushPlayground(channel, aPlayground);
		}
	}

	/*
	 * Pusht die Spielfelddaten zum CLient.
	 */
	private void pushPlayground(WebSocket.Out<JsonNode> channel,
			Playground playground) {
		try {
			ObjectMapper mapper = new ObjectMapper();
			String sResult = mapper.writeValueAsString(playground);
			JsonFactory factory = new JsonFactory();
			JsonParser jp = factory.createJsonParser(sResult);
			JsonNode actualObj = mapper.readTree(jp);
			channel.write(actualObj);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Zutritt zur Spielhalle.
	 * 
	 * @author stefan.illgen
	 * 
	 */
	public static class Join {

		final User user;
		final WebSocket.Out<JsonNode> channel;

		public Join(User user, WebSocket.Out<JsonNode> channel) {
			this.user = user;
			this.channel = channel;
		}
	}
	
	/**
	 * Austritt aus der Spielhalle.
	 * 
	 * @author stefan.illgen
	 *
	 */
	public static class Quit {
        
        final User user;
        
        public Quit(User user) {
            this.user = user;
        }
        
    }

}
