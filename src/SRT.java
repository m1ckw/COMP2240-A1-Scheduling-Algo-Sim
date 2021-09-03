
//*********************************************************************************************//
//  Class:  SRT - Shortest Remaining time First                                                //
//  Author: Mick Wiedermann                                                                    //
//  Course: COMP2240 | Assignment 1                                                            //
//  Date  : 2021-09-02                                                                         //
//  Description: Class to extend the scheduler specifically simulating a SRT algorithm.        //
//***********************************************************************************************

public class SRT extends Scheduler {

    private int clock;                  // Tracks each time increment.
    private int shortestRT;             // Tracks the process with the Shortest Remaining processing Time. 
    private int pIndex;                 // Tracks the index of the current running process.
    private Process runningP;           // Tracks the current running process.

    // Constructor 
    public SRT(ProcessList pList) {
        super(pList);
        this.clock = 0;
        this.shortestRT = 0;
        this.pIndex = 0;
        this.runningP = new Process();
    }
    // Simulates the SRT algorithm. 
    public void runSRT() {
        this.clock = 0;
        setSrt(totExecTime());
        while (super.checkRemainingTime()) {
            int nextProcess;
            if (super.getRQ().getSize() > 1) {
                nextProcess = assessSRT();
            } else {
                nextProcess = 0;
            }
            if (super.getRQ().getProcess(nextProcess).getId().equals(this.runningP.getId())) {
                runProcess(nextProcess);
                setSrt(totExecTime());
            } else {
                runningP.setId(super.getRQ().getProcess(nextProcess).getId());
                runDispatcher();
                runProcess(nextProcess);
                setSrt(totExecTime());
            }  
        }
    } 

    // Checks each process that has arrived and returns the process with SRT
    public int assessSRT() {
        for (int j=0; j<super.getRQ().getSize(); j++) {
            if (super.getRQ().getProcess(j).getRemainRunTime() < getSrt() 
            && (super.getRQ().getProcess(j).getArrive() <= this.clock)) {
                setSrt(super.getRQ().getProcess(j).getRemainRunTime());
                this.pIndex = j;
            }
        } // If the SRT is the current running process, pIndex runs again.
        return this.pIndex;
    }

    // "Runs Process" for 1 increment of time. 
    public void runProcess(int i) {
        if (super.getRQ().getProcess(i).getProcessedTime() == 0 
        && super.getRQ().getProcess(i).getCompletionTime() == 0) {
            super.getRQ().getProcess(i).setProcessStart(readClock());
            super.getRQ().getProcess(i).incrementPT();
            incrementClock();
            if (super.getRQ().getProcess(i).getRemainRunTime() == 0) {
                super.getRQ().getProcess(i).setCompletionTime(this.clock);
                super.add2CompletedQ(super.getRQ().getProcess(i));
                super.removeProcess(super.getRQ(), i);
            }
        } else if (super.getRQ().getProcess(i).getRemainRunTime() == 0 
        && super.getRQ().getProcess(i).getCompletionTime() == 0) {
            super.getRQ().getProcess(i).setCompletionTime(this.clock);
            super.add2CompletedQ(super.getRQ().getProcess(i));
            super.removeProcess(super.getRQ(), i);
        } else {
            super.getRQ().getProcess(i).incrementPT();
            incrementClock();
        }
    }

    // Adds a process to the completed queue based on certain qualifing criteria.
    public void add2CompletedQ() {
        for (int i=0; i<super.getRQ().getSize(); i++) {
            if ( (super.getRQ().getProcess(i).getRemainRunTime() == 0) 
            && (super.getRQ().getProcess(i).getCompletionTime() == 0)) {
                    super.getRQ().getProcess(i).setCompletionTime(this.clock);
                    super.add2CompletedQ(super.getRQ().getProcess(i));
            }
        }
    } 
    
    // Calculates total processing time to ensure assessSRT performs.
    public int totExecTime() {
        int total = 0;
        for (int i=0; i<super.getRQ().getSize(); i++) {
            total = total + super.getRQ().getProcess(i).getExSize();
        }
        return total;
    }

    // Reads in the cuurent time on the clock.
    public int readClock() {
        return this.clock;
    }

    // Increments the timing clock byt the value passed. 
    public void incrementClock() {
        this.clock++;
    }

    // "Runs Dispatcher" adding the dispatch time to the relavant process. 
    // also adds that process to the running queue. 
    public void runDispatcher() {
        this.clock = this.clock + getDispTime();
        Process process = new Process(runningP.getId(), this.clock, 0, 0);
        add2RunQ(process);
    } 

    public int getSrt() {
        return this.shortestRT;
    }

    public void setSrt(int srt) {
        this.shortestRT = srt;
    }

}