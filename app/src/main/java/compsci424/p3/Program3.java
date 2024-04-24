/* COMPSCI 424 Program 3
 * Name: Lucas Sass 
 * 
 * This is a template. Program3.java *must* contain the main class
 * for this program. 
 * 
 * You will need to add other classes to complete the program, but
 * there's more than one way to do this. Create a class structure
 * that works for you. Add any classes, methods, and data structures
 * that you need to solve the problem and display your solution in the
 * correct format.
 */

package compsci424.p3;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;

/**
 * Main class for this program. To help you get started, the major
 * steps for the main program are shown as comments in the main
 * method. Feel free to add more comments to help you understand
 * your code, or for any reason. Also feel free to edit this
 * comment to be more helpful.
 */
public class Program3 {
    // Declare any class/instance variables that you need here.

    private static int[] available;
    private static int[][] max;
    private static int[][] allocation;
    private static int[][] need;
    
    /**
     * @param args Command-line arguments. 
     * 
     * args[0] should be a string, either "manual" or "auto". 
     * 
     * args[1] should be another string: the path to the setup file
     * that will be used to initialize your program's data structures. 
     * To avoid having to use full paths, put your setup files in the
     * top-level directory of this repository.
     * - For Test Case 1, use "424-p3-test1.txt".
     * - For Test Case 2, use "424-p3-test2.txt".
     */
    public static void main(String[] args) {
        // Code to test command-line argument processing.
        // You can keep, modify, or remove this. It's not required.
        if (args.length < 2) {
            System.err.println("Not enough command-line arguments provided, exiting.");
            return;
        }
        System.out.println("Selected mode: " + args[0]);
        System.out.println("Setup file location: " + args[1]);

        // 1. Open the setup file using the path in args[1]
        String currentLine;
        BufferedReader setupFileReader;
        try {
            setupFileReader = new BufferedReader(new FileReader(args[1]));
        } catch (FileNotFoundException e) {
            System.err.println("Cannot find setup file at " + args[1] + ", exiting.");
            return;
       }

        // 2. Get the number of resources and processes from the setup
        // file, and use this info to create the Banker's Algorithm
        // data structures
        
       // int numResources;
       // int numProcesses;

        // For simplicity's sake, we'll use one try block to handle
        // possible exceptions for all code that reads the setup file.
      try {
            // Read number of resources and processes
            int numResources = Integer.parseInt(setupFileReader.readLine().split(" ")[0]);
            int numProcesses = Integer.parseInt(setupFileReader.readLine().split(" ")[0]);
            
            // Initialize data structures
            available = new int[numResources];
            max = new int[numProcesses][numResources];
            allocation = new int[numProcesses][numResources];
            need = new int[numProcesses][numResources];

            // Read available resources
            String[] availableLine = setupFileReader.readLine().split(" ");
            for (int i = 0; i < numResources; i++) {
                available[i] = Integer.parseInt(availableLine[i]);
            }

            // Read max resources
            readMatrix(setupFileReader, max, numProcesses, numResources);

            // Read allocation
            readMatrix(setupFileReader, allocation, numProcesses, numResources);

            // Calculate need matrix
            for (int i = 0; i < numProcesses; i++) {
                for (int j = 0; j < numResources; j++) {
                    need[i][j] = max[i][j] - allocation[i][j];
                }
            }

            setupFileReader.close();
        } catch (IOException e) {
            System.err.println("Something went wrong while reading setup file " + args[1] + ". Exiting.");
            e.printStackTrace(System.err);
            System.err.println("Exiting.");
            return;
        }
        System.out.println("Available:");
        System.out.println(Arrays.toString(available));
        System.out.println("Max:");
        printMatrix(max);
        System.out.println("Allocation:");
        printMatrix(allocation);
        System.out.println("Need:");
        printMatrix(need);

        // 4. Check initial conditions to ensure that the system is 
        // beginning in a safe state: see "Check initial conditions"
        // in the Program 3 instructions
    if (!checkInitialConditions()) {
        System.err.println("Error: Initial conditions not met. Exiting.");
        return;
    }

    // Check if system is in a safe state
    if (isSafeState()) {
        System.out.println("System is in a safe state.");
    }
    else {
        System.out.println("System is NOT in a safe state.");
    }

        

        // 5. Go into either manual or automatic mode, depending on
        // the value of args[0]; you could implement these two modes
        // as separate methods within this class, as separate classes
        // with their own main methods, or as additional code within
        // this main method.


    }
    private static void readMatrix(BufferedReader reader, int[][] matrix, int numRows, int numCols) throws IOException {
        for (int i = 0; i < numRows; i++) {
            String[] line = reader.readLine().split(" ");
            for (int j = 0; j < numCols; j++) {
                matrix[i][j] = Integer.parseInt(line[j]);
            }
        }
    }//end readMatrix
    private static void printMatrix(int[][] matrix) {
        for (int[] row : matrix) {
            System.out.println(Arrays.toString(row));
        }
    }//end printMatrix

    private static boolean isSafeState() {
    // Initialize work array to available resources
    int[] work = Arrays.copyOf(available, available.length);

    // Initialize finish array
    boolean[] finish = new boolean[max.length];
    Arrays.fill(finish, false);

    // Initialize safe sequence
    int[] safeSequence = new int[max.length];
    int safeCount = 0;

    // Step 4: Iterate through processes
    for (int i = 0; i < max.length; i++) {
        // If process i can finish and it hasn't finished yet
        if (!finish[i] && canFinish(i, work)) {
            // Pretend to allocate resources to process i
            for (int j = 0; j < available.length; j++) {
                work[j] += allocation[i][j];
            }
            // Mark process i as finished and add it to safe sequence
            finish[i] = true;
            safeSequence[safeCount++] = i;
            // Reset loop to check from beginning
            i = -1;
        }
    }

    // Step 5: Check if all processes can finish
    return safeCount == max.length;
}//end isSafeState

// Helper method to check if process can finish with available resources
private static boolean canFinish(int processId, int[] work) {
    for (int i = 0; i < available.length; i++) {
        if (need[processId][i] > work[i]) {
            return false;
        }
    }
    return true;
}//end canFinish

private static boolean checkInitialConditions() {
    // Check condition 1: Availability of resources
    for (int i = 0; i < available.length; i++) {
        if (available[i] < 0) {
            System.err.println("Error: Available resources cannot be negative.");
            return false;
        }
    }

    // Check condition 2: Max demand less than or equal to total resources
    for (int i = 0; i < max.length; i++) {
        for (int j = 0; j < available.length; j++) {
            if (max[i][j] > available[j]) {
                System.err.println("Error: Maximum resource demand of process " + i + " exceeds available resources.");
                return false;
            }
        }
    }

    // Check condition 3: Allocation less than or equal to max demand
    for (int i = 0; i < allocation.length; i++) {
        for (int j = 0; j < available.length; j++) {
            if (allocation[i][j] > max[i][j]) {
                System.err.println("Error: Allocation for process " + i + " exceeds maximum demand.");
                return false;
            }
        }
    }

    // Check condition 4: Availability plus allocation equals max resources
    for (int i = 0; i < available.length; i++) {
        int total = available[i];
        for (int j = 0; j < allocation.length; j++) {
            total += allocation[j][i];
        }
        if (total != max[0][i]) {
            System.err.println("Error: Total of available resources and allocated resources does not match maximum resources.");
            return false;
        }
    }

    // All conditions are met
    return true;
}//end checkInitialConditions


}
