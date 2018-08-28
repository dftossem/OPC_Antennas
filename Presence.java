package unal.labfabex.rfid;

import javafish.clients.opc.JOpc;
import javafish.clients.opc.component.OpcGroup;
import javafish.clients.opc.component.OpcItem;
import javafish.clients.opc.exception.ComponentNotFoundException;
import javafish.clients.opc.exception.ConnectivityException;
import javafish.clients.opc.exception.SynchReadException;
import javafish.clients.opc.exception.UnableAddGroupException;
import javafish.clients.opc.exception.UnableAddItemException;

public class Presence {
	
	static int actualR_E;
	static int actualR_I;
	static int actualR_P;
	static int isTag_E;
	static int isTag_I;
	static int isTag_P;
	
	public static void main(String[] args) throws InterruptedException {
		
		JOpc.coInitialize();
		
		JOpc jopc = new JOpc("localhost", "SOPAS.OPC.Server.1", "JOPC1");
		
		OpcItem read_E = new OpcItem("RFU620F (EXPERIMENTAL).Diag.Visualisation.StatisticDisplay.GoodReads", true, "");
		OpcItem read_I = new OpcItem("RFU620F (INDUSTRIAL).Diag.Visualisation.StatisticDisplay.GoodReads", true, "");
		OpcItem read_P = new OpcItem("RFU620F (PROTOTIPADO).Diag.Visualisation.StatisticDisplay.GoodReads", true, "");
		
		OpcGroup reading = new OpcGroup("readingGroup", true, 500, 0.0f);
	    
	    reading.addItem(read_E);
	    reading.addItem(read_I);
	    reading.addItem(read_P);
	    jopc.addGroup(reading);
	    
	    try {
	        jopc.connect();
	        System.out.println("JOPC client is connected...");
	      }
	      catch (ConnectivityException e2) {
	        e2.printStackTrace();
	        System.out.println("Error connecting");
	      }
	    
	    try {
	        jopc.registerGroups();
	        System.out.println("OPCGroup are registered...");
	      }
	      catch (UnableAddGroupException e2) {
	        e2.printStackTrace();
	        System.out.println("Error registering group (1)");
	      }
	      catch (UnableAddItemException e2) {
	        e2.printStackTrace();
	        System.out.println("Error registering item (2)");
	      }
	    try {
	    	OpcItem responseE = jopc.synchReadItem(reading, read_E);
	    	OpcItem responseI = jopc.synchReadItem(reading, read_I);
	    	OpcItem responseP = jopc.synchReadItem(reading, read_P);
	    	//actualR_E = responseE.getValue().getVariantType();	// Helps when the type of the item is unknown
	    	actualR_E = responseE.getValue().getWord();
	    	actualR_I = responseI.getValue().getWord();
	    	actualR_P = responseP.getValue().getWord();
			//System.out.println(actualR_E);
			//System.out.println(responseP.getValue());
	    }
	    catch (ComponentNotFoundException e1) {
	    	e1.printStackTrace();
	    	System.out.println("Couldn't read items (1)");
	    }
	    catch (SynchReadException e) {
	    	e.printStackTrace();
	    	System.out.println("Couldn't read items (2)");
	    }
	    JOpc.coUninitialize();
    }
	
	/*
	 * Return the final 4 char of the tag's ID read in all antennas
	 */
	public int[] lastTagsID() {
		int[] presenceAntennas = new int[3];
		
		presenceAntennas[0] = actualR_E;
		presenceAntennas[1] = actualR_I;
		presenceAntennas[2] = actualR_P;
		return presenceAntennas;
	}
}