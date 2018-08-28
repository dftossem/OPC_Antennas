package unal.labfabex.rfid;

import javafish.clients.opc.JOpc;
import javafish.clients.opc.component.OpcGroup;
import javafish.clients.opc.component.OpcItem;
import javafish.clients.opc.exception.ComponentNotFoundException;
import javafish.clients.opc.exception.ConnectivityException;
import javafish.clients.opc.exception.SynchReadException;
import javafish.clients.opc.exception.UnableAddGroupException;
import javafish.clients.opc.exception.UnableAddItemException;

public class TagID {
	
	static String readID_E;
	static String readID_I;
	static String readID_P;
	
	public static void main(String[] args) throws InterruptedException {
		
		JOpc.coInitialize();
		
		JOpc jopc = new JOpc("localhost", "SOPAS.OPC.Server.1", "JOPC1");
		
		OpcItem tagID_E = new OpcItem("RFU620F (EXPERIMENTAL).Diag.Visualisation.BarcodeResult.szContent", true, "");
		OpcItem tagID_I = new OpcItem("RFU620F (INDUSTRIAL).Diag.Visualisation.BarcodeResult.szContent", true, "");
		OpcItem tagID_P = new OpcItem("RFU620F (PROTOTIPADO).Diag.Visualisation.BarcodeResult.szContent", true, "");
		
		OpcGroup reading = new OpcGroup("readingGroup", true, 500, 0.0f);
	    
	    reading.addItem(tagID_E);
	    reading.addItem(tagID_I);
	    reading.addItem(tagID_P);
	    jopc.addGroup(reading);
	    
	    try {
	        jopc.connect();
	        System.out.println("JOPC client is connected.");
	      }
	      catch (ConnectivityException e2) {
	        e2.printStackTrace();
	        System.out.println("Error connecting");
	      }
	    
	    try {
	        jopc.registerGroups();
	        System.out.println("OPCGroup are registered.");
	      }
	      catch (UnableAddGroupException e2) {
	        e2.printStackTrace();
	        System.out.println("Error registering group (1)");
	      }
	      catch (UnableAddItemException e2) {
	        e2.printStackTrace();
	        System.out.println("Error registering group (2)");
	      }
	    
	    try {
	      OpcItem responseE = jopc.synchReadItem(reading, tagID_E);
	      OpcItem responseI = jopc.synchReadItem(reading, tagID_I);
	      OpcItem responseP = jopc.synchReadItem(reading, tagID_P);
	      readID_E = responseE.getValue().getString();
	      readID_I = responseI.getValue().getString();
	      readID_P = responseP.getValue().getString();
	      String[] parts_E = readID_E.split("00000000000000");
	      String[] parts_I = readID_I.split("00000000000000");
	      String[] parts_P = readID_P.split("00000000000000");
	      readID_E = parts_E[1];
	      readID_I = parts_I[1];
	      readID_P = parts_P[1];
	      /*System.out.print("Antenna Experimental: ");
	      System.out.print(readID_E);
	      System.out.print(", ");
	      System.out.print("Antenna Industrial: ");
	      System.out.print(readID_I);
	      System.out.print(", ");
	      System.out.print("Antenna Prototipado: ");
	      System.out.print(readID_P);
	      System.out.println();*/
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
	 * Return the final 4 char of the tag's ID readed in all antennas
	 */
	public String[] lastTagsID() {
		String[] antennasRead = new String[3];
		antennasRead[0] = readID_E;
		antennasRead[1] = readID_I;
		antennasRead[2] = readID_P;
		return antennasRead;
	}
}
