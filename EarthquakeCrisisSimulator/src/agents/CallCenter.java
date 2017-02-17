package agents;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;
import java.util.StringTokenizer;
import tools.Coordonnee;
import tools.Map;
import tools.Messages;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.OneShotBehaviour;
import jade.core.behaviours.ThreadedBehaviourFactory;
import jade.lang.acl.ACLMessage;

public class CallCenter extends Agent {

	// An ArrayList which contains the names of all the firefighters.
	ArrayList<String> firefighters;
	HashMap<String, Boolean> availableFirefighters;
	//HashMap<String, Coordonnee> coordonneeFirefighters;
	ArrayList<Coordonnee> rocksVisited;
	Map world;
	
	int numberOfSavedVictims;
	/**
	 * Constructor
	 */
	public CallCenter() {
		firefighters = new ArrayList<String>();
		firefighters.add("ff1");
		firefighters.add("ff2");
		availableFirefighters = new HashMap<String, Boolean>();
		availableFirefighters.put("ff1", true);
		availableFirefighters.put("ff2", true);
		
		rocksVisited = new ArrayList<Coordonnee>();
		
//		coordonneeFirefighters = new HashMap<String, Coordonnee>();
//		coordonneeFirefighters.put("ff1", null);
		world = new Map();
		numberOfSavedVictims = 0;
	}
	
	/**
	 * Prints the list of all the firefighters.
	 */
	public void showFirefighters() {
		System.out.println("List of all the firefighters:");
		for (int i=0; i<firefighters.size(); i++) {
			System.out.println(firefighters.get(i));
		}
	}
	
	/**
	 * @return 
	 * 		the Coordonnee of the first Rock it finds as it checks the map.
	 */
	public Coordonnee findNewRock() {
		boolean b;
		for (int i=0; i<5; i++) {
			for (int j=0; j<5; j++) {
				if (world.map[i][j] instanceof tools.Rock ) {
					b = true;
					for (int k=0; k<rocksVisited.size(); k++) {
						if (rocksVisited.get(k).getPositionX() == i 
								&& rocksVisited.get(k).getPositionY() == j) {
							//System.out.println("**** " + i + " " + j);
							b = false;
						}
					}
					if (b) {
						return new Coordonnee(i, j);
					}
				}
			}
		}
		return null;
	}
	
	/**
	 * @return 
	 * 		the Coordonnee of a free adjacent square to a given square.
	 */
	public Coordonnee findAdjacent(Coordonnee c) {
		if (c == null) {
			return null;
		}
		else {
			for (int i=0; i<5; i++) {
				for (int j=0; j<5; j++) {
					if ( (distance(i, j, c) == 1) && (world.map[i][j] == null) ) {
						//System.out.println("**** " + i + " " + j);
						return new Coordonnee(i, j);
					}
				}
			}
		}
		return null;
	}
	
	/**
	 * @return 
	 * 		the distance between 2 squares.
	 */
	public int distance(int x, int y, Coordonnee c) {
		int distX = Math.abs(x - c.getPositionX());
		int distY = Math.abs(y - c.getPositionY());
		return distX + distY;
	}
	
	/**
	 * Add a specific behaviour to the CallCenter.
	 */
	protected void setup() {
		System.out.println("A CallCenter has been created!");
		this.showFirefighters();
		//this.findNewTargetPlace().getPositionX();
		//this.findNewTargetPlace().getPositionY();
		System.out.println();
		
		// Create a thread behaviour factory
		ThreadedBehaviourFactory tbf = new ThreadedBehaviourFactory();
		// Creates a behaviour
		Behaviour b = new CCFirstActions(this);
		// Adds the behaviour to the agent
		this.addBehaviour(tbf.wrap(b));
	}
	
	private class CCReceivingBehaviour extends CyclicBehaviour {
		
		public CCReceivingBehaviour(CallCenter cc){
			super(cc);
		}
		
		public void action() {
			CallCenter callCenter = (CallCenter) super.myAgent;
			ACLMessage msg = blockingReceive();
			if (msg.getSender().getLocalName().equals("Doctor")){
				// If the CC got a msg from the doctor...
				// ...
			}
			else { // The message received has been sent by a firefighter.
				String receivedMsg = msg.getContent();
				StringTokenizer st = new StringTokenizer(receivedMsg, "!");
				String tempST = st.nextToken() ;
				System.out.println("CC: A ff told me --> " + tempST);
				if (tempST.equals("success")) {
					// The CC understands that the ff has found someone...
					System.out.println("SUCCESS");
					// Increase the numberOfSavedVictims counter...
					callCenter.numberOfSavedVictims++;
					System.out.println("rescued : "+callCenter.numberOfSavedVictims);
					// Update the availableFirefighters list...
					callCenter.availableFirefighters.put(msg.getSender().getLocalName(), true);
					
					// Update of the rocksVisited list is not needed here (we have already done it)...
					//tempST = rock position
					String rock = st.nextToken();
					//////rocksVisited.add(new Coordonnee(Integer.parseInt(tempST.charAt(0)+""),Integer.parseInt(tempST.charAt(1)+"")));
					
					// Update the map (the ff has left the place where he was operating)...
					//tempST = agentPosition
					String target = st.nextToken();
					Map.map[Integer.parseInt(target.charAt(0)+"")][Integer.parseInt(target.charAt(1)+"")] = null;
					
					if (st.nextToken().equals("doctor")){
						// A doctor's help is needed...
						String content = Integer.parseInt(target.charAt(0)+"") + "" + Integer.parseInt(target.charAt(1)+"") + "!" + 
						Integer.parseInt(rock.charAt(0)+"") + "" + Integer.parseInt(rock.charAt(1)+"");
						ACLMessage command = new ACLMessage(ACLMessage.INFORM);
						command.setContent(content);
						System.out.println("CC: I will try to send the following msg to Agent Doctor: " + content);
						command.addReceiver(new AID( "Doctor" , AID.ISLOCALNAME) );
						///////////////
						send(command);
						///////////////
					}
					else{
						// A doctor is not needed, so:
						// do nothing
					}
					
					// The ff is now ready to receive new orders...
					Coordonnee newRock = callCenter.findNewRock();
					// if (newRock==null) { System.out.println("PROBLEM");}
					Coordonnee newTarget = callCenter.findAdjacent(newRock);
					
					// If we have found a place to go...
					if (newTarget != null) {
						String content = newTarget.getPositionX() + "" + newTarget.getPositionY() + "!" + 
											newRock.getPositionX() + "" + newRock.getPositionY();
						ACLMessage command = new ACLMessage(ACLMessage.INFORM);
						command.setContent(content);
						System.out.println("CC: I will try to send the following msg to Agent " + msg.getSender().getLocalName() +
												": " + content);
						command.addReceiver(new AID( msg.getSender().getLocalName() , AID.ISLOCALNAME) );
						///////////////
						send(command);
						///////////////
						System.out.println("CC: I sent the following msg to Agent " + msg.getSender().getLocalName() +
												": " + content);
						// The CallCenter has sent a msg to a firefighter, so we have to update  the following:
						// The available firefighters info...
						callCenter.availableFirefighters.put(msg.getSender().getLocalName(), false);
						// Also, the value of the map into the place where the ff was sent...
						Map.map[Integer.valueOf(content.substring(0,1))][Integer.valueOf(content.substring(1,2))] = new String("F");
						// The rocks we have already visited...
						callCenter.rocksVisited.add(newRock);
					}
				}
				else { // The ff reports a failure
					// Update the availableFirefighters list...
					callCenter.availableFirefighters.put(msg.getSender().getLocalName(), true);
					////////////////// begin copy paste
					Coordonnee newRock = callCenter.findNewRock();
					Coordonnee newTarget = callCenter.findAdjacent(newRock);

					// If we have found a place to go...
					if (newTarget != null) {
						String content = newTarget.getPositionX() + "" + newTarget.getPositionY() + "!" + 
											newRock.getPositionX() + "" + newRock.getPositionY();
						ACLMessage command = new ACLMessage(ACLMessage.INFORM);
						command.setContent(content);
						System.out.println("CC: I will try to send the following msg to Agent " + msg.getSender().getLocalName() +
												": " + content);
						command.addReceiver(new AID( msg.getSender().getLocalName() , AID.ISLOCALNAME) );
						///////////////
						send(command);
						///////////////
						System.out.println("CC: I sent the following msg to Agent " + msg.getSender().getLocalName() +
												": " + content);
						// The CallCenter has sent a msg to a firefighter, so we have to update  the following:
						// The available firefighters info...
						callCenter.availableFirefighters.put(msg.getSender().getLocalName(), false);
						// Also, the value of the map into the place where the ff was sent...
						Map.map[Integer.valueOf(content.substring(0,1))][Integer.valueOf(content.substring(1,2))] = new String("F");
						// The rocks we have already visited...
						callCenter.rocksVisited.add(newRock);
					}
				}
			}
		}
	}

	private class CCFirstActions extends OneShotBehaviour {

		public CCFirstActions(CallCenter cc){
			super(cc);
		}

		public void action() {

			CallCenter callCenter = (CallCenter) super.myAgent;
			// The first thing to do is, as long as there are Rocks, keep sending firefighters to those places.
			for (int i=0; i<callCenter.firefighters.size(); i++) {
				
				Coordonnee newRock = callCenter.findNewRock();
				Coordonnee newTarget = callCenter.findAdjacent(newRock);
				
				// If we have found a place to go...
				if (newTarget != null) {
					String content = newTarget.getPositionX() + "" + newTarget.getPositionY() + "!" + 
										newRock.getPositionX() + "" + newRock.getPositionY();
					// Preparing to send an inform message to a firefighter.
					ACLMessage command = new ACLMessage(ACLMessage.INFORM);
					command.setContent(content);
					System.out.println("CC: I will try to send the following msg to Agent " + callCenter.firefighters.get(i) +
											": " + content);
					command.addReceiver(new AID( callCenter.firefighters.get(i) , AID.ISLOCALNAME) );
					///////////////
					send(command);
					///////////////
					System.out.println("CC: I sent the following msg to Agent " + callCenter.firefighters.get(i) +
											": " + content);
					// The CallCenter has sent a msg to a firefighter, so we have to update  the following:
					// The available firefighters info...
					callCenter.availableFirefighters.put(firefighters.get(i), false);
					///////System.out.println(Integer.valueOf(content.substring(0,1)));
					// Also, the value of the map into the place where the ff was sent...
					Map.map[Integer.valueOf(content.substring(0,1))][Integer.valueOf(content.substring(1,2))] = new String("F");
					// The rocks we have already visited...
					callCenter.rocksVisited.add(newRock);
				}
			}
			// Add the CyclicBehaviour to the callCenter...
			callCenter.addBehaviour(new CCReceivingBehaviour(callCenter));
		}
	}	

}
