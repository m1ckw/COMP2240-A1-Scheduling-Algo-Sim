
//*********************************************************************************************//
//  Class:  FCFS - First Come First Serve                                                      //
//  Author: Mick Wiedermann                                                                    //
//  Course: COMP2240 | Assignment 1                                                            //
//  Date  : 2021-09-02                                                                         //
//  Description: Class to extend the scheduler specifically simulating a FCFS algorithm.       //
//***********************************************************************************************

public class FCFS extends Scheduler {

    private int clock;                  // Tracks each time increment.
    private Process runningP;           // Tracks the current running process.

    // Constructor 
    public FCFS(ProcessList pList) {
        super(pList);
        this.clock = 0;
        this.runningP = new Process();
    }
    // Simulates the FCFS algorithm. 
    public void runFCFS() {
        this.clock = 0;
        for (int i=0; i< super.getRQ().getSize(); i++) {
            runningP.setId(getRQ().getProcess(i).getId());                  // Updates the ID of the running Process.
            runDispatcher();                                                // Dispatching process.
            super.getRQ().getProcess(i).setProcessStart(readClock());       // Setting the process Start time &
            incrementClock(super.getRQ().getProcess(i).getExSize());        // Processing the relavant process in full.
            super.getRQ().getProcess(i).setCompletionTime(readClock());     // Setting the completion time. 
            super.add2CompletedQ(super.getRQ().getProcess(i));              // adding process to completed queue.
        }
    }
    // Reads in the cuurent time on the clock.
    public int readClock() {
        return this.clock;
    }
    // Increments the timing clock byt the value passed. 
    public void incrementClock(int value) {
        this.clock = this.clock + value;
    }

    // "Runs Dispatcher" adding the dispatch time to the relavant process. 
    public void runDispatcher() {
        this.clock = this.clock + getDispTime();
        Process process = new Process(runningP.getId(), this.clock, 0, 0);
        add2RunQ(process);
    }
    
}