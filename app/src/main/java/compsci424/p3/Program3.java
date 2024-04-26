/* COMPSCI 424 Program 3
 * Name: Lucas Sass
 * 

I apologize, I don't want to make excuses for not being able to complete the assinment, I will 
take whatever grade is coming to me. I already know it does not run because there are parts that I couldn't figure
out how to do. I have dumped too much of my time into another class because I have a huge project in it and that has caused me to run out of time for this one.

 
 */

package compsci424.p3;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;//for safestate check
import java.util.Scanner;//for manual

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
    private static int[][] n;

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
        if (args.length < 2 ||(!args[0].equals("manual") || !args[0].equals("auto"))) {//if args[0] doesnt equal "manual" or "auto" have an error appear
            System.err.println("Not enough command-line arguments provided, exiting.");
            return;
        }//end if

        if(args[0].equals("manual")){//runs manual if args0 says so
            manual();
        }//end if
        else if(args[0].equals("auto")){//runs auto if args0 says so
            auto();
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
        int numResources;
        int numProcesses;

        // For simplicity's sake, we'll use one try block to handle
        // possible exceptions for all code that reads the setup file.
        try {
            // Get number of resources
            currentLine = setupFileReader.readLine();
            if (currentLine == null) {
                System.err.println("Cannot find number of resources, exiting.");
                setupFileReader.close();
                return;
            }
            else {
                numResources = Integer.parseInt(currentLine.split(" ")[0]);
                System.out.println(numResources + " resources");
            }
 
            // Get number of processes
            currentLine = setupFileReader.readLine();
            if (currentLine == null) {
                System.err.println("Cannot find number of processes, exiting.");
                setupFileReader.close();
                return;
            }
            else {
                numProcesses = Integer.parseInt(currentLine.split(" ")[0]);
                System.out.println(numProcesses + " processes");
            }

            // Create the Banker's Algorithm data structures, in any
            // way you like as long as they have the correct size
            available = new int[numResources];
            max = new int[numProcesses][numResources];
            allocation = new int[numProcesses][numResources];
            n = new int[numProcesses][numResources];

            // 3. Use the rest of the setup file to initialize the
            // data structures

            setupFileReader.close(); // done reading the file, so close it

            if (!checkInitialConditions()) {//checks initial conditions
                System.err.println("Check Initial Conditions failed");
                return;
            }
        }
        catch (IOException e) {
            System.err.println("Something went wrong while reading setup file "
            + args[1] + ". Stack trace follows. Exiting.");
            e.printStackTrace(System.err);
            System.err.println("Exiting.");
            return;
        }

        // 4. Check initial conditions to ensure that the system is 
        // beginning in a safe state: see "Check initial conditions"
        // in the Program 3 instructions
    

        // 5. Go into either manual or automatic mode, depending on
        // the value of args[0]; you could implement these two modes
        // as separate methods within this class, as separate classes
        // with their own main methods, or as additional code within
        // this main method.


    }//end main
        private static boolean checkInitialConditions() {
            //condition 1
            for(int i=0;i<allocation.length;i++){
                for(int j=0;j<allocation[i].length;j++){
                    if(allocation[i][j]>max[i][j]){
                    return false;//if it doesnt meet the condition
                    }
                }
            }//end c1 for1

            //condition 2
            int[] total= new int[available.length];
            for(int i=0;i<allocation.length;i++){
                for(j=0;j<allocation[i].length;j++){
                    total[j]+=allocation[i][j];//total= available+allocated
                }
            }//end c2 for1
            for(int j=0;j<available.length;j++){
            if(available[j]+total[j]<0){
            return false;//if it doesnt meet the condition
            }
            }//end for for j

            //condition3
            if (!safeState()){
            return false;
            }

          return true;//if passes all conditions  
        }//end check

    //condition 3 (cont.)
    private static boolean safeState(){
        int numProcesses = allocation.length;
        int numResources = available.length;
        int[] available2= Arrays.copyOf(available,numResources);//available r that can be allocated to p
        boolean[] done= new boolean[numProcesses];//to keep track of if check is done or not
        int[] safe=new int[numProcesses];
        int safeCount=0;//to count how many processes are safe

    //I'm so confused on this part, I don't remember anything about safe state and I'm trying to do research but I cant find anything that helps
        
    }//end safeState
private static boolean request(int p, int[] request){
    for(int i=0;i<request.length;i++){
        if(request[i]> n[p][i] || request[i]>available[i]){
        return false;}
        if(safeState()){
        for (int i = 0; i < request.length; i++) {
            available[i] += request[i];//adds the request to the available resources  
            allocation[p][i] -= request[i];  
            n[p][i] += request[i];      
            return false;//deny if not safe state
            }//end for
        }//end if
    }//end for1
    return true;
}//end request
    private static void release(int p, int[] release){
    for (int i = 0; i < release.length; i++) {//check if "the number of units to be released is valid"
        if (release[i] < 0 || release[i] > allocation[p][i]) {
            return;
        }//end if
    }//end for
        for(int i=0; i<release.length;i++){
         available[i]+=release[i];//release resources back to being available
          
        }//end for
        System.out.println("Resources have been released");
    }//end release

private static void manual(){
    Scanner sc= new Scanner(System.in);
    boolean started=true;//to show that its running manually
    while(started==true){//while manual mode is called
        System.out.println("Use request, release, or end");
        String input=scanner.nextLine().trim();//gets user input and splits it into parts
        input=input.toLowerCase();//makes it so user can enter Uppercase if they want and it wont matter
        if(input.startsWith("request")){
        String[] parts= input.split(" ");//splits input into parts separated by spaces

            //I apologize, I can't think and the deadline is coming up 
            
        }//end if
        else if (input.startsWith("release")) {
            String[] parts = input.split(" ");

         //I apologize, I can't think and the deadline is coming up 

            
        }//end else if
        else if (input.equals("end")) {
            running = false;//will stop the program
        }//end else if2

        
    }//end while
}//end manual

private static void auto(){

}//end auto



    

}//end prog3
