//-------------------------------------------------------------------------------------------------
// Factory.java
//
// Factory class that creates the bank and each bank customer
// Usage:  java Factory 10 5 7

import java.io.*;
import java.util.*;

public class Factory {
    public static void main(String[] args) {
        String filename = "infile.txt";
        int nResources = args.length;
        int[] resources = new int[nResources];
        for (int i = 0; i < nResources; i++) { resources[i] = Integer.parseInt(args[i].trim()); }

        Bank theBank = new BankImpl(resources);
        int[] maxDemand = new int[nResources];
        int[] allocated = new int[nResources];
        Thread[] workers = new Thread[Customer.COUNT];      // the customers

        try{
		// read in values and initialize the matrices
		// to do
		// ...

        BufferedReader reader = null;
        reader = new BufferedReader(new FileReader(filename));

        String temp = "";
        String[] temp_arr;

        for(int threadNum = 0; threadNum < workers.length; threadNum++){
            temp = reader.readLine();
            temp_arr = temp.split(",");

            for(int i = 0; i < nResources; i++){
                allocated[i] = Integer.parseInt(temp_arr[i]);
                maxDemand[i] = Integer.parseInt(temp_arr[i + 3]);
            }
                System.out.println("adding customer " + threadNum + "...");
                workers[threadNum] = new Thread(new Customer(threadNum, maxDemand, theBank));
                theBank.addCustomer(threadNum, allocated, maxDemand);

                //++threadNum;        //theBank.getCustomer(threadNum);
                //resourceNum = 0;
        }
            
        } catch (FileNotFoundException fnfe) { throw new Error("Unable to find file \"" + filename + "\"");
        } catch (IOException ioe) { throw new Error("Error processing \"" + filename + "\""); }

        System.out.println("FACTORY: created threads");     // start the customers

        System.out.println("FACTORY: started threads");
        for (int i = 0; i < Customer.COUNT; i++) { workers[i].start(); }
        

    }
}
