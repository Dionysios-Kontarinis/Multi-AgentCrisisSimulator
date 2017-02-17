package agents;

import jade.core.AID;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.ThreadedBehaviourFactory;
import jade.domain.FIPAAgentManagement.AMSAgentDescription;
import jade.domain.JADEAgentManagement.KillAgent;
import jade.lang.acl.ACLMessage;
import tools.Coordonnee;
import tools.Messages;

public class VictimOld extends GeneralAgent{
	
	int life ;
	
	/**
	 * Constructor
	 */
	public VictimOld() {
		super(null);
		if (victimCount < 7){
			this.life = 10 ;		
		}
		this.setCoordonnee(generateInitPosition());
	}
	
	int victimCount = 1;
	
	/**
	 * Generate initial position of the victim
	 * @return initial position
	 */
	public Coordonnee generateInitPosition(){
		switch (victimCount) {
		case 1 :
			victimCount++;
			return new Coordonnee(1,3);
		case 2 :
			victimCount++;
			return new Coordonnee(2,1);
		case 3 :
			victimCount++;
			return new Coordonnee(2,5);
		case 4 :
			victimCount++;
			return new Coordonnee(3,3);
		case 5 :
			victimCount++;
			return new Coordonnee(4,1);
		case 6 :
			victimCount++;
			return new Coordonnee(5,4);
		default:
			System.out.println("BUG : allready six agents");
			return null;
		}
	}
	
	private Coordonnee lookForFireFighter(){
		Coordonnee myC = this.getCoordonnee();
		if (tools.Map.getCell(new Coordonnee(myC.getPositionX()+1,myC.getPositionY())).equals("FireFighter")){
			return new Coordonnee(myC.getPositionX()+1,myC.getPositionY());
		}
		else if (tools.Map.getCell(new Coordonnee(myC.getPositionX()-1,myC.getPositionY())).equals("FireFighter")){
			return new Coordonnee(myC.getPositionX()-1,myC.getPositionY());
		}
		else if (tools.Map.getCell(new Coordonnee(myC.getPositionX(),myC.getPositionY()+1)).equals("FireFighter")){
			return new Coordonnee(myC.getPositionX(),myC.getPositionY()+1);
		}
		else if (tools.Map.getCell(new Coordonnee(myC.getPositionX(),myC.getPositionY()-1)).equals("FireFighter")){
			return new Coordonnee(myC.getPositionX(),myC.getPositionY()-1);
		}
		else{
			return null;			
		}
	}
	
	/**
	 * Associate a specific behaviour to the Victim Agent
	 */
	protected void setup() {
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
		 * @param vic
		 *            a Victim agent
		 */
		public VictimBehaviour(VictimOld vic) {
			super(vic);
		}
		
		public void action(){
			VictimOld v = (VictimOld) super.myAgent;
			
			if (v.life > 0){
				//behavior selection
				// *** Calm behavior
				if (Math.random()>0.5){
					ACLMessage msg = blockingReceive(3000);
					if (msg != null){
						ACLMessage reply = msg.createReply();
						reply.setPerformative(ACLMessage.INFORM);
						reply.setContent(Messages.SAVE_ME);
						send(reply);
						ACLMessage end = blockingReceive(2000);
						if (end.getContent().equals(Messages.SAVED)){
							v.doDelete();
						}
					}
					life--;
				}
				// *** Action behavior
				else {
					Coordonnee ffC = lookForFireFighter();
					if (ffC != null){
						ACLMessage acl = new ACLMessage(ACLMessage.INFORM);
						acl.addReceiver(new AID(tools.Map.getCell(ffC),AID.ISLOCALNAME));
						acl.setContent(Messages.SAVE_ME);
						send(acl);
						ACLMessage end = blockingReceive(2000);
						if (end.getContent().equals(Messages.SAVED)){
							v.doDelete();
						}
					}
					life -= 2;
				}
			}
			//victim is dead
			else{
				ACLMessage aclMessage = new ACLMessage(ACLMessage.INFORM);
				aclMessage.addReceiver(new AID("callCenter", AID.ISLOCALNAME));
				aclMessage.setContent(Messages.DEAD);
				send(aclMessage);
				v.doDelete();
			}
		}
	}
}
