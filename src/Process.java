
//*********************************************************************************************//
//  Class:  Process                                                                            //
//  Author: Mick Wiedermann                                                                    //
//  Course: COMP2240 | Assignment 1                                                            //
//  Date  : 2021-09-02                                                                         //
//  Description: Base class to support Process Attributes.                                     //
//***********************************************************************************************

public class Process {
    
    private String id;              // Process Id
    private int arrive;             // Arrival time
    private int execSize;           // Processing Time
    private int tickets;            // Number of tickets

    private int processStart;       // The time when the process first reaches the CPU.
    private int processedTime;      // Time on processer so far.
    private int completionTime;     // The time when the process completes.
    private int ogI;                // The original index.

    // Constructors 
    public Process(String id, int arrive, int execSize, int tickets) {
        this.id = id;
        this.arrive = arrive;
        this.execSize = execSize;
        this.tickets = tickets;
        this.completionTime = 0;
        this.processStart = 0;
        this.processedTime = 0;
        this.ogI = 0;
    }

    public Process() {
        this("p", 0, 0, 0);
    }

    // Static method to call the constructor creating a new process. 
    public static Process createProcess(String id, int arrive, int execSize, int tickets) {
        return new Process(id, arrive, execSize, tickets);
    }

    // Calculates remaining run time
    public int getRemainRunTime() {
        return this.execSize - this.processedTime;
    }

    // Calulates wait time. 
    public int getWaitTime() {
        return (this.completionTime - this.arrive - this.execSize);
    }

    // Calculates turn around time. 
    public int getTurnAroundTime() {
        return (this.completionTime - this.arrive);
    }

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getArrive() {
        return this.arrive;
    }

    public void setArrive(int arrive) {
        this.arrive = arrive;
    }

    public int getExSize() {
        return this.execSize;
    }

    public void setExecSize(int execSize) {
        this.execSize = execSize;
    }

    public int getTickets() {
        return this.tickets;
    }

    public int getProcessStart() {
        return this.processStart;
    }

    public void setProcessStart(int processStart) {
        this.processStart = processStart;
    }

    public int getProcessedTime() {
        return this.processedTime;
    }

    public void setProcessedTime(int time) {
        this.processedTime = time;
    }

    public void incrementPT() {
        this.processedTime++;
    }

    public int getCompletionTime() {
        return this.completionTime;
    }

    public void setCompletionTime(int completionTime) {
        this.completionTime = completionTime;
    }

    public int getOgI() {
        return this.ogI;
    }

    public void setOgI(int ogI) {
        this.ogI = ogI;
    }

}