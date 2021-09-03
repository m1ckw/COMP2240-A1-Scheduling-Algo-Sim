
//*********************************************************************************************//
//  Class:  LTR - Process Lottery                                                              //
//  Author: Mick Wiedermann                                                                    //
//  Course: COMP2240 | Assignment 1                                                            //
//  Date  : 2021-09-02                                                                         //
//  Description: Class to extend the scheduler specifically simulating a LTR algorithm.        //
//***********************************************************************************************

import java.util.ArrayList;
import java.util.List;

public class LTR extends Scheduler {

    private int clock;                          // Tracks each time increment.
    private List<Integer> tickets;              // A list of all the imported random numbers.
    private List<String> allocatedTickets;      // Tracks the processes the tickets have been allocated to.
    private Process runningP;                   // Tracks the current running process.
    private int nextTicket;                     // Tracks the next ticket from the imported list of Random numbers.

    // Constructor 
    public LTR(ProcessList pList, List<Integer> tickets) {
        super(pList);
        this.clock = 0;
        this.tickets = tickets;
        this.allocatedTickets = new ArrayList<>();
        this.runningP = new Process();
        this.nextTicket = 0;
    }

    // Simulates the SRT algorithm. 
    public void runLTR() {
        this.clock = 0;
        allocateTickets(this.tickets);
        while (checkRemainingTime()) {
            int nextProcess = getNextWinner();
            this.runningP = super.getRQ().getProcess(nextProcess);
            if (super.getRQ().getProcess(nextProcess).getRemainRunTime() > 0  
            && super.getRQ().getProcess(nextProcess).getArrive() <= this.clock) {
                runDispatcher(); // Ensures only relavant ptocesses are "dispacthed".
            }
            for (int i=0; i<4; i++) { // Runs the winning process for a count of 4/ms.
                if (super.getRQ().getProcess(nextProcess).getRemainRunTime() > 0) {
                    runProcess(nextProcess);
                } else if (super.getRQ().getProcess(nextProcess).getRemainRunTime() == 0 
                && super.getRQ().getProcess(nextProcess).getCompletionTime() == 0) {
                    super.getRQ().getProcess(nextProcess).setCompletionTime(this.clock);
                    super.add2CompletedQ(super.getRQ().getProcess(nextProcess));
                } // ^^ Completes the process once excution time is zero but bfore 4/ms is up. 
            }
            if (super.getRQ().getProcess(nextProcess).getRemainRunTime() == 0
            && super.getRQ().getProcess(nextProcess).getCompletionTime() == 0) {
                super.getRQ().getProcess(nextProcess).setCompletionTime(this.clock);
                super.add2CompletedQ(super.getRQ().getProcess(nextProcess));
            } // ^^ Completes the process if the excution time is zero after the 4/ms is up. 
        }
    } 

    // Allocates the random ticket numbers (provided in upload) to processes based on # of tickets held by each process. 
    public void allocateTickets(List<Integer> tickets) {
        setUpAllocation();
        int tAllocated = 0; // Total Allocated tickets - Required to calculate the percentage of tickets a process should have.
        for (int i=0; i<super.getRQ().getSize(); i++) {
            tAllocated = tAllocated + super.getRQ().getProcess(i).getTickets();
        }   // ^^ Finds the total number of tickets held by all processes.
        for (int i=0; i<super.getRQ().getSize(); i++) {
            double tShare = (super.getRQ().getProcess(i).getTickets()/(double)tAllocated)*tickets.size();
            int ticShare = (int)Math.round(tShare); // Sets the number of ticket (random numbers) a process p(i) should reveive.
            while (ticShare > 0) { // Unitll the required portion of tickets has been allocated. 
                for (int j=0; j<tickets.size(); j++) {
                    if (ticShare > 0) { 
                        int pos = (tickets.get(j)%ticShare)*2+1; // Takes the modulus of the randam number imported & the required ticket share,
                        if (pos > tickets.size()) {              // multiplies it by 2 and adds 1 each time a ticket is allocated. myRandom().
                            boolean flag = true;
                            int k = 0;
                            while (flag && k<this.allocatedTickets.size()) {
                                if (this.allocatedTickets.get(k).equals("null")) {
                                    replacePos(this.allocatedTickets, super.getRQ().getProcess(i).getId(), k);
                                    flag = false;
                                }
                                k++;
                            }
                        } else if (this.allocatedTickets.get(pos).equals("null")) {
                            replacePos(this.allocatedTickets, super.getRQ().getProcess(i).getId(), pos);
                        } else {
                            boolean flag = true;
                            int k = this.allocatedTickets.size()-1;
                            while (flag && k>-1) {
                                if (this.allocatedTickets.get(k).equals("null")) {
                                    replacePos(this.allocatedTickets, super.getRQ().getProcess(i).getId(), k);
                                    flag = false;
                                }
                                k--;
                            }
                        }
                    }
                    ticShare--;
                }
            }
        }
    }

    // Retreives the process that holds the next winning ticket in order.
    public int getNextWinner() {
        int index = 0;
        if (this.nextTicket == 0 || this.nextTicket>this.tickets.size()-1) {
            this.nextTicket = 0;
        } 
        for (int i=0; i<super.getRQ().getSize(); i++) {
            if (super.getRQ().getProcess(i).getId().equals(this.allocatedTickets.get(this.nextTicket)) 
                && super.getRQ().getProcess(i).getRemainRunTime() >= 0) {
                index = i;
            } 
        }
        this.nextTicket++;
        return index;
    }

    // "Runs Process" for 1 increment of time - these are slightly different for each algo. 
    public void runProcess(int i) {
        if (super.getRQ().getProcess(i).getArrive() <= this.clock) { 
            if (super.getRQ().getProcess(i).getProcessedTime() == 0 
            && super.getRQ().getProcess(i).getCompletionTime() == 0) {
                super.getRQ().getProcess(i).setProcessStart(readClock());
                super.getRQ().getProcess(i).incrementPT();
                incrementClock();
                if (super.getRQ().getProcess(i).getRemainRunTime() == 0) {
                    super.getRQ().getProcess(i).setCompletionTime(this.clock);
                    super.add2CompletedQ(super.getRQ().getProcess(i));
                }
            } else if (super.getRQ().getProcess(i).getRemainRunTime() == 0 
            && super.getRQ().getProcess(i).getCompletionTime() == 0) {
                super.getRQ().getProcess(i).setCompletionTime(this.clock);
                super.add2CompletedQ(super.getRQ().getProcess(i));
            } else {
                super.getRQ().getProcess(i).incrementPT();
                incrementClock();
            }
        }
    }

    // Checks if there is any processing time remaining for any process. 
    @Override
    public boolean checkRemainingTime() {
        int timeRemaining = 0;
        boolean flag = true;
        if (super.getRQ().getSize() < 1) {
            flag = false;
        } else {
            for (int i=0; i<super.getRQ().getSize(); i++) {
                timeRemaining = timeRemaining + (super.getRQ().getProcess(i).getRemainRunTime());
                if (timeRemaining == 0) {
                    flag = false;
                } else {
                    flag = true;
                }
            }
        }
        return flag;
    }

    // "Runs Dispatcher" adding the dispatch time to the relavant process, 
    // also updates the current running process.
    public void runDispatcher() {
        this.clock = this.clock + getDispTime();
        Process process = new Process(this.runningP.getId(), this.clock, 0, 0);
        super.add2RunQ(process);
    } 

    // Reads in the cuurent time on the clock.
    public int readClock() {
        return this.clock;
    }

    // Increments the timing clock byt the value passed. 
    public void incrementClock() {
        this.clock++;
    }

    // creates a list the same size as the random number upload to track 
    // which process holds which ticket once allocated. 
    public void setUpAllocation() {
        for (int i=0; i<this.tickets.size(); i++) {
            this.allocatedTickets.add("null");
        }
    }

    // Just a little helper to replace an element in the ArrayList.
    public void replacePos(List<String> list, String pId, int position) {
        list.remove(position);
        list.add(position, pId);
    }

}