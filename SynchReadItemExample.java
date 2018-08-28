package javafish.clients.opc;

import javafish.clients.opc.component.OpcGroup;
import javafish.clients.opc.component.OpcItem;
import javafish.clients.opc.exception.ComponentNotFoundException;
import javafish.clients.opc.exception.ConnectivityException;
import javafish.clients.opc.exception.SynchReadException;
import javafish.clients.opc.exception.UnableAddGroupException;
import javafish.clients.opc.exception.UnableAddItemException;

public class SynchReadItemExample {
  public static void main(String[] args) throws InterruptedException {
    SynchReadItemExample test = new SynchReadItemExample();
    
    JOpc.coInitialize();
    
    JOpc jopc = new JOpc("localhost", "SOPAS.OPC.Server.1", "JOPC1");

    OpcItem item1 = new OpcItem("RFU620F (EXPERIMENTAL).Diag.Visualisation.BarcodeResult.szContent", true, "");
    //OpcItem item1 = new OpcItem("Random.Real8", true, "");
    //OpcItem item1 = new OpcItem("Random.String", true, "");
    /*
    triggerGate_E = new OpcItem("RFU620F (EXPERIMENTAL).Diag.Visualisation.StatisticDisplay.TriggerGates", true, "");
    goodRead_E = new OpcItem("RFU620F (EXPERIMENTAL).Diag.Visualisation.StatisticDisplay.GoodReads", true, "");
	tagID_E = new OpcItem("RFU620F (EXPERIMENTAL).Diag.Visualisation.BarcodeResult.szContent", true, "");
	apcCounter_E = new OpcItem("RFU620F (EXPERIMENTAL).Diag.OpData.OpHours", true, "");
	// Attributes initialization
	triggerGate_I = new OpcItem("RFU620F (INDUSTRIAL).Diag.Visualisation.StatisticDisplay.TriggerGates", true, "");
	goodRead_I = new OpcItem("RFU620F (INDUSTRIAL).Diag.Visualisation.StatisticDisplay.GoodReads", true, "");
	tagID_I = new OpcItem("RFU620F (INDUSTRIAL).Diag.Visualisation.BarcodeResult.szContent", true, "");
	apcCounter_I = new OpcItem("RFU620F (INDUSTRIAL).Diag.OpData.OpHours", true, "");
    */
    OpcGroup group = new OpcGroup("group1", true, 500, 0.0f);
    
    group.addItem(item1);
    jopc.addGroup(group);
    
    try {
      jopc.connect();
      System.out.println("JOPC client is connected...");
    }
    catch (ConnectivityException e2) {
      e2.printStackTrace();
    }
    
    try {
      jopc.registerGroups();
      System.out.println("OPCGroup are registered...");
    }
    catch (UnableAddGroupException e2) {
      e2.printStackTrace();
    }
    catch (UnableAddItemException e2) {
      e2.printStackTrace();
    }
    
    synchronized(test) {
      test.wait(50);
    }
    
    // Synchronous reading of item
    int cycles = 3;
    int acycle = 0;
    while (acycle++ < cycles) {
      synchronized(test) {
        test.wait(1000);
      }
      
      try {
        OpcItem responseItem = jopc.synchReadItem(group, item1);
        //System.out.println(responseItem);
        //System.out.println(Variant.getVariantName(responseItem.getDataType()) + ": " + responseItem.getValue());
        System.out.println(responseItem.getValue());
      }
      catch (ComponentNotFoundException e1) {
        e1.printStackTrace();
      }
      catch (SynchReadException e) {
        e.printStackTrace();
      }
    }
    
    JOpc.coUninitialize();
  }
}
