package tools;

import java.io.Serializable;

/**
 * Represents the coordinates of the map.
 * 
 * @author Kontarinis Dionysios, Marcais Julien
 * 
 */
public class Coordonnee implements Serializable {

//	/**
//	 * Long identifier of the class
//	 */
//	private static final long serialVersionUID = 9161298725940073057L;

	/**
	 * The line in the zone of the AgentMap where the agent is.
	 */
	private int positionX;

	/**
	 * The column in the zone of the AgentMap where the agent is.
	 */
	private int positionY;

	/**
	 * Constructor
	 * 
	 * @param ligne
	 *            the value given to positionX
	 * @param colonne
	 *            the value given to positionY
	 * @param nom
	 *            the name of agent
	 */

	public Coordonnee(final int ligne, final int colonne) {
		this.positionX = ligne;
		this.positionY = colonne;
	}

	/**
	 * @return the current line where the agent is situated.
	 */
	public final int getPositionX() {
		return positionX;
	}

	/**
	 * Change the number of line where the agent is situated.
	 * 
	 * @param value
	 *            the value given to positionX
	 */
	public final void setPositionX(int value) {
		this.positionX = value;
	}

	/**
	 * Return the number of column where the agent is situated.
	 * 
	 * @return the current column where the agent is situated.
	 */
	public final int getPositionY() {
		return positionY;
	}

	/**
	 * Change the number of column where the agent is situated.
	 * 
	 * @param value
	 *            the value given to positionY
	 */
	public final void setPositionY(int value) {
		this.positionY = value;
	}
	
	public final String toString(){
		return "!"+this.getPositionX()+"!"+this.getPositionY();
	}

}