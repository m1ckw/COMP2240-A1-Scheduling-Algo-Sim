
//*********************************************************************************************//
//  Class:  Scheduler                                                                          //
//  Author: Mick Wiedermann                                                                    //
//  Course: COMP2240 | Assignment 1                                                            //
//  Date  : 2021-09-02                                                                         //
//  Description: Class to support the scheduling and running of each of the process algorithms.//
//***********************************************************************************************

public class Scheduler {
    
    private ProcessList newPList;               // Holds the imported processes as a process list.
    private ProcessList readyQ;                 // Holds the processes once ready for processing.
    private ProcessList runQ;                   // Holds processes once they are running, tracks start times.
    private ProcessList completedQ;             // Holds the processes once they have completed executing.
    private static int dispatchTime;            // Holds the value imported from the file upload.

    // Constructor 
    public Scheduler(ProcessList pList) {
        this.newPList = pList;
        this.readyQ = new ProcessList();
        this.runQ = new ProcessList();
        this.completedQ = new ProcessList();
    }

    public Scheduler() {
    }  
    
    // Copies accross the new process list & reorders the ready Queue based on arrival times, 
    // if there is no difference, then the order remains the same. 
    public ProcessList prepReadyQueue() {
        for (int i=0; i<newPList.getSize(); i++) {
            add2ReadyQ(newPList.getProcess(i));
            getRQ().getProcess(i).setProcessStart(0);
            getRQ().getProcess(i).setCompletionTime(0);
            getRQ().getProcess(i).setProcessedTime(0);
            getRQ().getProcess(i).setOgI(i);
        } 
        reOrderAT(getRQ());
        return getRQ();
    }

    // Prints out the results once the algo has run its course.  
    public void printResults(String algoName) {
        System.out.println(algoName + ":");
        ProcessList tempList = new ProcessList();
        for (int i=0; i<runQ.getSize(); i++) {
            tempList.appendP(runQ.getProcess(i));
        } 
        reOrderAT(tempList);
        for (int i=0; i<tempList.getSize(); i++) {
            System.out.println("T" + tempList.getProcess(i).getArrive() + ": " + tempList.getProcess(i).getId());
        }
        System.out.println("");
    }

    // Prints the summary table once an algorithm has finished running its course.
    public void printTable() {
        System.out.println("Process    Turnaround Time    Waiting Time");
        reOrder(getCompletedQ());
        for (int i=0; i<this.completedQ.getSize(); i++) {
            System.out.println(this.completedQ.getProcess(i).getId() + "\t   " 
            + this.completedQ.getProcess(i).getTurnAroundTime() + "\t\t      " 
            + this.completedQ.getProcess(i).getWaitTime());
        }
        System.out.println("");
    }

    // Little helper to reorder a queue based on process number.
    public void reOrder(ProcessList pList) {
        boolean flag = true;
        Process temp; 
        while (flag) {
            flag = false; 
            for (int i=0; i<pList.getSize()-1; i++) {
                if (pList.getProcess(i).getOgI() > pList.getProcess(i+1).getOgI() ) {
                    temp = pList.getProcess(i);
                    replaceProcess(pList, i, pList.getProcess(i+1));
                    replaceProcess(pList, (i+1), temp);
                    flag = true;
                }
            }
        }
    }

    // Little helper to reorder a queue based on arrival time.
    public void reOrderAT(ProcessList pList) {
        boolean flag = true;
        Process temp; 
        while (flag) {
            flag = false; 
            for (int i=0; i<pList.getSize()-1; i++) {
                if (pList.getProcess(i).getArrive() > pList.getProcess(i+1).getArrive() ) {
                    temp = pList.getProcess(i);
                    replaceProcess(pList, i, pList.getProcess(i+1));
                    replaceProcess(pList, (i+1), temp);
                    flag = true;
                }
            }
        }
    }

    // Checks if there is any processing time remaining for any process. 
    public boolean checkRemainingTime() {
        int timeRemaining = 0;
        boolean flag = true;
        if (getRQ().getSize() < 1) {
            flag = false;
        } else {
            for (int i=0; i<getRQ().getSize(); i++) {
                timeRemaining = timeRemaining + (getRQ().getProcess(i).getRemainRunTime());
                if (timeRemaining == -1) {
                    flag = false;
                } else {
                    flag = true;
                }
            }
        }
        return flag;
    }

    // Just a little helper to replace processes in the ArrayList.
    public void replaceProcess(ProcessList pList, int position, Process process) {
        pList.removeP(position);
        pList.addP(position, process);
    }
    
    public ProcessList getNewPList() {
        return this.newPList;
    }

    public ProcessList getRQ() {
        return this.readyQ;
    }

    public void add2ReadyQ(Process process) {
        this.readyQ.addProcess(process);
    }

    public ProcessList getRunningQ() {
        return this.runQ;
    }

    public void add2RunQ(Process process) {
        this.runQ.addProcess(process);
    }

    public ProcessList getCompletedQ() {
        return this.completedQ;
    }

    public void add2CompletedQ(Process process) {
        this.completedQ.addProcess(process);
    }

    public static int getDispTime() {
        return dispatchTime;
    }

    public static void setDispTime(int dispTime) {
        dispatchTime = dispTime;
    }

    public void removeProcess(ProcessList pList, int i) {
        pList.removeP(i);
    }
}