package agents;

import tools.Coordonnee;
import tools.Map;
import tools.Rock;
import jade.core.AID;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.ThreadedBehaviourFactory;
import jade.lang.acl.ACLMessage;

public class FireFighter extends GeneralAgent {

	/**
	 * Constructor
	 */
	public FireFighter() {
		super(null);
	}
	
	/**
	 * Associate a specific behaviour to the FireFighter Agent
	 */
	protected void setup() {
		System.out.println("New firefighter on duty!!");
		//System.out.println("My AID is: " + this.getAID());
		// Create a thread behaviour factory
		ThreadedBehaviourFactory tbf = new ThreadedBehaviourFactory();
		// Creates a behaviour
		Behaviour b = new FireFighterBehaviour(this);
		// Adds the behaviour to the agent
		this.addBehaviour(tbf.wrap(b));
	}
	
	private class FireFighterBehaviour extends CyclicBehaviour {
		
		/**
		 * Constructor
		 * 
		 * @param ff
		 *            a FireFighter agent
		 */
		public FireFighterBehaviour(FireFighter ff) {
			super(ff);
		}

		public void action(){
			FireFighter ff = (FireFighter) super.myAgent;
			ACLMessage msg = blockingReceive();
			// If the message that the ff received has been sent by the CC...
			if (msg.getPerformative() == ACLMessage.INFORM) {
				String receivedMsg = msg.getContent();

				System.out.println("Firefighter " + ff.getLocalName() + " : " +" I travel to " +
						"(" + receivedMsg.charAt(0) + "," + receivedMsg.charAt(1) + ")");
				
				try {
					// The firefighter spends time in order to travel to his destination.
					Thread.sleep(2000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				System.out.println("Firefighter " + ff.getLocalName() + " : " + "I'm at " +
						"(" + receivedMsg.charAt(0) + "," + receivedMsg.charAt(1) + ")");
				//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
				// The ff updates his position...
				ff.setCoordonnee(new Coordonnee(Integer.parseInt(receivedMsg.charAt(0)+""), Integer.parseInt(receivedMsg.charAt(1)+"")));
				//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
				try {
					// The firefighter spends time in order to save the victim.
					Thread.sleep(2000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				int a = Integer.valueOf(receivedMsg.substring(3,4));
				int b = Integer.valueOf(receivedMsg.substring(4,5));
				Rock r = (Rock) Map.map[a][b];
				// If he finds a victim there...
				if (r.isSomeoneThere) {
					String victimName = r.getVictimName();
					ACLMessage msgToVictim = new ACLMessage(ACLMessage.REQUEST);
					String content = "Need doctor?";
					msgToVictim.setContent(content);
					System.out.println("FF: I will try to send the following msg to a victim" +
							": " + content);
					msgToVictim.addReceiver(new AID( victimName , AID.ISLOCALNAME) );
					///////////////
					send(msgToVictim);
					///////////////
					// Wait for a reply from the victim...
					ACLMessage victimReply = blockingReceive();
					if (victimReply.getPerformative() == ACLMessage.CONFIRM) {
						// Tells the CC (INFORM) that he found a victim who needs a doctor...
						ACLMessage replyToCC = new ACLMessage(ACLMessage.INFORM);
						replyToCC.addReceiver(new AID( "CallCenter" , AID.ISLOCALNAME) );
						replyToCC.setContent("success!" + a + "" + b + "!" + ff.getCoordonnee().getPositionX() +
													"" + ff.getCoordonnee().getPositionY() + "!doctor");
						////////////////
						send(replyToCC);
						////////////////
					}
					else {
						// Tells the CC (INFORM) that he found a victim who doesn't need a doctor...
						if (victimReply.getPerformative() == ACLMessage.DISCONFIRM) {
							ACLMessage replyToCC = new ACLMessage(ACLMessage.INFORM);
							replyToCC.addReceiver(new AID( "CallCenter" , AID.ISLOCALNAME) );
							replyToCC.setContent("success!" + a + "" + b + "!" + ff.getCoordonnee().getPositionX() +
														"" + ff.getCoordonnee().getPositionY() + "!nodoctor");
							////////////////
							send(replyToCC);
							////////////////
						}
					}	
				}
				else {
					// The ff doesn't find a victim under that specific rock (FAILURE)...
					System.out.println("Firefighter " + ff.getLocalName() + " : " +"Nobody here!!");
					ACLMessage replyToCC = msg.createReply();
					replyToCC.setPerformative(ACLMessage.FAILURE);
					replyToCC.setContent("failure!" + a + "" + b + "!" + ff.getCoordonnee().getPositionX() +
							"" + ff.getCoordonnee().getPositionY());
					////////////////
					send(replyToCC);
					////////////////
				}
			}
			else {
				// TO BE DELETED
				//...
			}
		}
	}
	
}
