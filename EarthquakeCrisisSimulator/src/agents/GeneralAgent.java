package agents;

import jade.core.Agent;
import tools.Coordonnee;

public class GeneralAgent extends Agent {

//	/**
//	 * 
//	 */
//	private static final long serialVersionUID = 6656226487075792899L;

	/**
	 * The position for the Agent in the map
	 */
	private Coordonnee coordonnee;

	/**
	 * Constructor
	 * 
	 * @param position the value given to the agent position.
	 */
	public GeneralAgent(final Coordonnee position) {
		this.coordonnee = position;
	}

	/**
	 * Return the agent position in the map.
	 * 
	 * @return the value of coordonee
	 */
	public final Coordonnee getCoordonnee() {
		return coordonnee;
	}

	/**
	 * Changes the agent's position
	 * @param coordonnee the new value of coordonee
	 */
	public final void setCoordonnee(Coordonnee coordonnee) {
		this.coordonnee = coordonnee;
	}

}