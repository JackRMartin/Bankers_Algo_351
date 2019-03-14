//-------------------------------------------------------------------------------------------------
// BankImpl.java
//
// implementation of the Bank
//
import java.io.*;
import java.util.*;

public class BankImpl implements Bank {
    private int n;			// the number of threads in the system
    private int m;			// the number of resources

    private int[] available; 	// the amount available of each resource
    private int[][] maximum; 	// the maximum demand of each thread
    private int[][] allocation;	// the amount currently allocated to each thread
 	   private int[][] need;	// the remaining needs of each thread

    private void showAllMatrices(int[][] alloc, int[][] max, int[][] need, String msg) { 
		// todo
        System.out.println(msg);
        for(int i = 0; i < alloc.length; i++){
            System.out.print("[");
            for(int j = 0; j < alloc[i].length; j++){
                System.out.print(" " + alloc[i][j]);
            }
            System.out.print(" ]");

            System.out.print("    [");
            for(int j = 0; j < alloc[i].length; j++){
                System.out.print(" " + max[i][j]);
            }
            System.out.print(" ]");

            System.out.print("       [");
            for(int j = 0; j < alloc[i].length; j++){
                System.out.print(" " + need[i][j]);
            }
            System.out.println(" ]");
        }
	}
 
    private void showMatrix(int[][] matrix, String title, String rowTitle) {
		// todo
        System.out.println(title);

        for(int i = 0; i < matrix.length; i++){
            System.out.println(rowTitle + " ");
            for(int j = 0; j < matrix[i].length; j++){
                System.out.println(matrix[i][j] + " ");
            }
        }
    }

    private void showVector(int[] vect, String msg) {
		// todo
        System.out.println(msg);
        for(int i = 0; i < vect.length; i++){
            System.out.println(vect[i] + " ");
        }
    }

    public BankImpl(int[] resources) {      // create a new bank (with resources)
		// todo

        n = Customer.COUNT;
        m = resources.length;

        available = new int[resources.length];
        maximum = new int[n][m];       
        allocation = new int[n][m];
        need = new int[n][m]; 

        for(int i = 0; i < resources.length; i++){
            available[i] = resources[i];
        }

    }
                               // invoked by a thread when it enters the system;  also records max demand
    public void addCustomer(int threadNum, int[] allocated, int[] maxDemand) {
        for(int i = 0; i < m; i++){
            allocation[threadNum][i] =  allocated[i];
            maximum[threadNum][i] = maxDemand[i];
            need[threadNum][i] = maxDemand[i] - allocated[i];
        }
    }

    public void getState() {        // output state for each thread
		// todo
        showAllMatrices(allocation, maximum, need, "ALLOCATED   MAXIMUM            NEED");
    }

    private boolean isSafeState (int threadNum, int[] request) {
		// todo -- actual banker's algorithm
        return true;
    }
                                // make request for resources. will block until request is satisfied safely

    public synchronized boolean requestResources(int threadNum, int[] request) throws InterruptedException {
 		// todo

        System.out.print("#P" + threadNum + " RQ:[");

        System.out.print(request[0]);
        for(int i = 1; i < request.length; i++){
            System.out.print(" " + request[i]);
        }
        System.out.print("]");

        System.out.print(", needs:[" + need[threadNum][0]);
        for(int i = 1; i < need[threadNum].length; i++){
            System.out.print(" " + need[threadNum][i]);
        }
        System.out.print("]");

        System.out.print(", available:[" + available[0]);
        for(int i = 1; i < available.length; i++){
            System.out.print(" " + available[i]);
        }
        System.out.print("]  ");

         for(int i = 0; i < request.length; i++){
            if(request[i] > available[i] || request[i] > need[threadNum][i]){
                System.out.println("   DENIED!");
                return false;
            }
        }

        System.out.print("   ---> APPROVED, #P" + threadNum + " now at:");

        for(int i = 0; i < request.length; i++){
            allocation[threadNum][i] += request[i];
            available[i] -= request[i];
            need[threadNum][i] = need[threadNum][i] - request[i] <= 0 ? 0 : need[threadNum][i] - request[i];
        }


        System.out.print("[" + allocation[threadNum][0]);
        for(int i = 1; i < allocation[threadNum].length; i++){
            System.out.print(" " + allocation[threadNum][i]);
        }
        System.out.println("]");

        showAllMatrices(allocation, maximum, need, "ALLOCATED    MAXIMUM           NEED");


        boolean found = false;
        for(int i = 0; i < need[threadNum].length && !found; i++){
            if(need[threadNum][i] != 0){
                found = true;
            }
        }

        if(!found){
            System.out.println("---------------->#P" + threadNum + " has all its resources! RELEASING ALL and SHUTTING DOWN...");
            System.out.print("----------------Customer #" + threadNum + " releasing:" + Arrays.toString(allocation[threadNum]));
            this.releaseResources(threadNum, allocation[threadNum]);
            System.out.println(" allocated =" + Arrays.toString(allocation[threadNum]));

            System.out.print(", available:[" + available[0]);
        for(int i = 1; i < available.length; i++){
            System.out.print(" " + available[i]);
        }

            throw new InterruptedException();
        }

        return true;
     }

    public synchronized void releaseResources(int threadNum, int[] release)  {
        for(int i = 0; i < release.length; i++){
            allocation[threadNum][i] -= release[i];
            available[i] += release[i];
        }
    }
}
