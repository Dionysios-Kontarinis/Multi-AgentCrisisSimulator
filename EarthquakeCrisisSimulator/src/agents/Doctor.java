package agents;

import tools.Map;
import tools.Rock;
import jade.core.AID;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.ThreadedBehaviourFactory;
import jade.lang.acl.ACLMessage;

public class Doctor extends GeneralAgent{

	/**
	 * 
	 */
	private static final long serialVersionUID = -3656875278001304566L;

	public Doctor(){
		super(null);
	}
	
	/**
	 * Associate a specific behaviour to the Doctor Agent
	 */
	protected void setup() {
		// Create a thread behaviour factory
		ThreadedBehaviourFactory tbf = new ThreadedBehaviourFactory();
		// Creates a behaviour
		Behaviour b = new DoctorBehaviour(this);
		// Adds the behaviour to the agent
		this.addBehaviour(tbf.wrap(b));
	}
	
	private class DoctorBehaviour extends CyclicBehaviour{
		
		/**
		 * Constructor
		 * 
		 * @param d
		 *            a FireFighter agent
		 */
		public DoctorBehaviour(Doctor d) {
			super(d);
		}
		
		public void action(){
			Doctor doctor = (Doctor) super.myAgent;
			ACLMessage msg = blockingReceive();
			if (msg.getSender().getLocalName().equals("CallCenter")){
				String receivedMsg = msg.getContent();
				System.out.println("Doctor : I travel to " +
						"(" + receivedMsg.charAt(0) + "," + receivedMsg.charAt(1) + ")");
				try {
					// The doctor spends time in order to travel to his destination.
					Thread.sleep(2000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				System.out.println("Doctor : I'm at " +
						"(" + receivedMsg.charAt(0) + "," + receivedMsg.charAt(1) + ")");
				
				try {
					// The doctor spends time in order to save the victim
					Thread.sleep(2000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				int a = Integer.valueOf(receivedMsg.substring(3,4));
				int b = Integer.valueOf(receivedMsg.substring(4,5));
				Rock r = (Rock) Map.map[a][b];
				String hisName = r.getVictimName();
				ACLMessage healMsg = new ACLMessage(ACLMessage.INFORM);
				healMsg.addReceiver(new AID(hisName, AID.ISLOCALNAME));
				healMsg.setContent("You are out of danger !");
				send(healMsg);
				
				ACLMessage victimMsg = blockingReceive();
				if (victimMsg.getContent().equals("Thank you")){
					ACLMessage reply = msg.createReply();
					reply.setPerformative(ACLMessage.CONFIRM);
					send(reply);					
				}
				else{
					System.out.println("##### FATAL ERROR #####");
				}
				
			}
		}
		
	}
	
}
