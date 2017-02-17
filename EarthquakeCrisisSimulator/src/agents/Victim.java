package agents;

import jade.core.behaviours.Behaviour;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.ThreadedBehaviourFactory;
import jade.lang.acl.ACLMessage;

public class Victim extends GeneralAgent {

	int lifeLeft;
	
	public Victim() {
		super(null);
		lifeLeft = 15;
	}
	
	/**
	 * Associate a specific behaviour to the Victim Agent
	 */
	protected void setup() {
		System.out.println("New victim under a rock!!");
		//System.out.println("My AID is: " + this.getAID());
		// Create a thread behaviour factory
		ThreadedBehaviourFactory tbf = new ThreadedBehaviourFactory();
		// Creates a behaviour
		Behaviour b = new VictimBehaviour(this);
		// Adds the behaviour to the agent
		this.addBehaviour(tbf.wrap(b));
	}
	
	private class VictimBehaviour extends CyclicBehaviour{
		
		/**
		 * Constructor
		 * 
		 * @param v
		 *            a FireFighter agent
		 */
		public VictimBehaviour(Victim v) {
			super(v);
		}
		
		public void action() {
			Victim victim = (Victim) super.myAgent;
			ACLMessage msg = blockingReceive();
			String content = msg.getContent();
			if (content.equals("Need doctor?")) {
				if (victim.lifeLeft < 10) {
					System.out.println("V: A ff asked me: " + content);
					ACLMessage reply = msg.createReply();
					reply.setPerformative(ACLMessage.CONFIRM);
					send(reply);
					//victim.doDelete();
				}
				else {
					System.out.println("V: A ff asked me: " + content);
					ACLMessage reply = msg.createReply();
					reply.setPerformative(ACLMessage.DISCONFIRM);
					send(reply);
					//victim.doDelete();
				}
			}
			else if (msg.getSender().getLocalName().equals("Doctor")){
				ACLMessage okMsg = msg.createReply();
				okMsg.setPerformative(ACLMessage.CONFIRM);
				okMsg.setContent("Thank you");
				send(okMsg);
			}
		}
	}
}
