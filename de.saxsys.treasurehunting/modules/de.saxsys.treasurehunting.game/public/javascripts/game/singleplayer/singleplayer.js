/**
 * Initiates the web socket connection for single player mode.
 * 
 * @param url
 *            The url to connect the websocket with.
 */
function initializeSinglePlayerGame(url) {

	console.log("Initializing websocket with URL: "+url);
	
	var WS = window['MozWebSocket'] ? MozWebSocket : WebSocket;
	var socket = new WS(url);
	
	var receiveEvent = function(event) {
		
		if(event.error) {
			socket.close();
			return
		}
		
		var responseAction = JSON.parse(event.data);
		
		// TODO: switch over initializers
		initializeScores(responseAction);
	}
	
	// set pause action
	$("#a-pause").click = function() {
//		socket.send($.toJSON(playground));
		pauseGame();
	};
	
	// hide resume game button
	$("#a-resume").hide();
	
	socket.onmessage = receiveEvent;
}

/**
 * Initializes the scores for the game.
 * 
 * @param responseAction
 */
function initializeScores(responseAction) {
	
	console.log(responseAction);
	
	for(var i = 0; i < responseAction.data[1].length; i++) {
		var counter = responseAction.data[1][i];
		createInfoRow(counter);
	}
	
}

/**
 * Inserts a info row into the table identified by #table-info concerning the
 * game scores.
 * 
 * @param counter
 */
function createInfoRow(counter) {
	
	console.log("Creating scores for counter with id "+counter.id);
	
	var hexColor = hexToRgb(counter.color);
	
	var infoRow =  
		'<tr id="#counter-'+counter.id+'-color"" class="active">'
		+'<td class="color" >'
		+'<svg width="16" height="16" xmlns="http://www.w3.org/2000/svg" xmlns:xlink="http://www.w3.org/1999/xlink">'
		+'<circle id="counter-'+counter.id+'-color" cx="8" cy="8" r="8"  fill="rgb('+hexColor+')" stroke="black" stroke-width=".0" onload="setColor()"/>'
        +'</svg></td>'
        +'<td class="name" id="#counter-'+counter.id+'-username">'+counter.user.name+'</td>'
        +'<td class="cards" id="#counter-'+counter.id+'-cards">'+counter.cards+'</td></tr>';
	
	$("#table-info").append(infoRow);
	
}

/**
 * Pasers a decimal color value into decimal rgb format.
 * 
 * @param dec The decimal color value.
 * @returns Returns a decimal rgb string.
 */
function hexToRgb(dec) {

    var r = (dec >> 16) & 255;
    var g = (dec >> 8) & 255;
    var b = dec & 255;
 
    return [r, g, b].join();
}

/**
 * Sets a info row for the scores of the given counter.
 * 
 * @param counter
 *            The counter to set the scores for.
 */
function setInfoRow(counter) {
	
	console.log("Setting scores for counter with id "+counter.id);
	
	$("#counter-"+counter.id+"-color").attr( 'fill',"#"+Number(counter.color).toString(16));
	$("#counter-"+counter.id+"-cards").text(counter.cards);
	$("#counter-"+counter.id+"-username").text(counter.user.name);
}

/**
 * Cancels the game.
 */
function cancelGame() {
	
}

/**
 * Restarts the game.
 */
function restartGame() {
	
}

/**
 * Pauses the game.
 */
function pauseGame() {
	
	// on receive action resonse for pause hide pause button and show resume
	// button
	$("#a-pause").hide();
	$("#a-resume").show();
}

/**
 * Resume a paused game.
 */
function resumeGame() {
	
	// hide pause on pressing resume
	$("#a-pause").show();
	$("#a-resume").hide();
}