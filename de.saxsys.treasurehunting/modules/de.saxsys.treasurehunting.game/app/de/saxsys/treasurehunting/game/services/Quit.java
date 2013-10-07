package de.saxsys.treasurehunting.game.services;


/**
 * Represents a {@link Quit} to the game hall.
 * 
 * @author stefan.illgen
 * 
 */
public class Quit {
    
	/**
	 * The game's ID.
	 */
	public final Long gameID;
    
    /**
	 * The counter's ID.
	 */
	public final Long counterID;
    
    /**
     * Default constructor.
     * 
     * @param gameID The user.
     */
    public Quit(Long gameID, Long counterID) {
        this.gameID = gameID;
        this.counterID = counterID;
    }
    
}
