//-------------------------------------------------------------------------------------------------
// Factory.java
//
// Factory class that creates the bank and each bank customer
// Usage:  java Factory 10 5 7

import java.io.*;
import java.util.*;

public class Factory {
    public static void main(String[] args) throws IOException {
        String filename = "infile.txt";
        int nResources = args.length;
        int[] resources = new int[nResources];
        for (int i = 0; i < nResources; i++) { resources[i] = Integer.parseInt(args[i].trim()); }

        Bank theBank = new BankImpl(resources);
        int[] maxDemand = new int[nResources];
        int[] allocated = new int[nResources];
        Thread[] workers = new Thread[Customer.COUNT];      // the customers
        char choice = 'a';
        String ch;
        boolean loop = true;
        boolean havent_ran_this = true;


        Scanner scanner = new Scanner(System.in);  // Create a Scanner object

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
        do{
          havent_ran_this = false;
          System.out.println("Would you like to run Interactive mode(i), or Automatic Mode(a)?");
          choice =  scanner.nextLine().charAt(0);
          System.out.println("char is " + (char)choice);

        if(choice == 'a'){
          havent_ran_this = false;
          System.out.println("FACTORY: created threads");     // start the customers

          System.out.println("FACTORY: started threads");
          for (int i = 0; i < Customer.COUNT; i++) { workers[i].start(); }
      } else if(choice == 'i'){

          System.out.print("Enter * to get the system state, OR enter <RQ | RL> <customer number> <resource #0> <#1> <#2>");
          choice = scanner.nextLine().charAt(0);
          if(choice == '*'){
            theBank.getState();
          }
        }
      }while(!theBank.isDone());
    }
}
