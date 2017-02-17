package main;

import java.util.ArrayList;
import agents.CallCenter;
import tools.Map;
import jade.core.Profile;
import jade.core.ProfileImpl;
import jade.core.Runtime;
import jade.wrapper.AgentContainer;
import jade.wrapper.AgentController;
import jade.wrapper.ControllerException;
import jade.wrapper.StaleProxyException;

/**
 * This class implements the starting point of the Simulation.
 * @author Kontarinis Dionysios, Marcais Julien
 */
public class SeismeMain extends Thread {

    /**
     * The agent container that contains the agents of the simulation.
     */
    private AgentContainer container;

    /**
     * Create a new simulation.
     *
     * @throws ControllerException is an error occurs while starting the main agent container.
     */
    public SeismeMain() throws ControllerException {
    	 	
    	// Create an instance of a jade runtime in the JVM
    	Runtime runtime = Runtime.instance();
    	
    	// Create a configuration for the main agent container of the platform 
    	Profile config = new ProfileImpl(true);

    	// Create the main agent container of the platform
    	this.container = runtime.createMainContainer(config);
    	
    	// Add a the controller of the simulation
    	// container.addPlatformListener(new Controller());
    	// Launch the main agent container
    	
    	container.start();
    }
    
    /**
     * The main loop of the simulation.
     */
    public void run() {
    	
    	// Just for example create and launch ........
    	try {
    		//
//    		ArrayList<String> firefighters = new ArrayList<String>();
//    		firefighters.add("ff1");
//    		firefighters.add("ff2");
    		Map.init();
    		
    		// In order to "sniff" the inter-agent messages.
    		AgentController sniffer = container.createNewAgent("sniffer", "jade.tools.sniffer.Sniffer", new String[]{""});
    		sniffer.start();
    		
    		AgentController center = container.createNewAgent(
    				"CallCenter",
    				"agents.CallCenter", null);
    		
    		AgentController firefighter1 = container.createNewAgent(
    				"ff1",
    				"agents.FireFighter", null
    		);
    		
    		AgentController firefighter2 = container.createNewAgent(
    				"ff2",
    				"agents.FireFighter", null
			);
    		
    		AgentController victim1 = container.createNewAgent(
    				"v1",
    				"agents.Victim", null
    		);
    		
    		AgentController victim2 = container.createNewAgent(
    				"v2",
    				"agents.Victim", null
    		);
    		
    		AgentController doctor = container.createNewAgent(
    				"Doctor",
    				"agents.Doctor", null
    		);
    		
    		try{
    			sleep(13000);
    		}
    		catch (InterruptedException e) {
    			// TODO: handle exception
    			System.out.println("InterruptedException took place");
    		}
    		
    		victim1.start();
    		victim2.start();
    		firefighter1.start();
    		firefighter2.start();
    		center.start();
    		doctor.start();

    	} catch (StaleProxyException e) {
    		e.printStackTrace();
    	}
    }

   /**
    * The main method of the simulation.
    * 
    * @param args the arguments of the simulation.
    */
   public static void main(String[] args) {
       try {
    	   SeismeMain simulation = new SeismeMain();
    	   simulation.start();
       } catch (ControllerException e) {
	    System.err.println("An error occours while starting the main agent container");
	    e.printStackTrace();
	}
   }
}


