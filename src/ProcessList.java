
//*********************************************************************************************//
//  Class:  ProcessList                                                                        //
//  Author: Mick Wiedermann                                                                    //
//  Course: COMP2240 | Assignment 1                                                            //
//  Date  : 2021-09-02                                                                         //
//  Description: Base class that builds an Array List of Processes.                            //
//***********************************************************************************************

import java.util.ArrayList;

public class ProcessList {
    
    private ArrayList<Process> pList;

    // Constructor 
    public ProcessList() {
        pList = new ArrayList<>();
    }

    // Apends new process to the Array List
    public boolean addProcess(Process newProcess) {    
        this.pList.add(newProcess);
        return true;
    }

    // Returns the list
    public ArrayList<Process> getPList() {
        return this.pList;
    }

    public int getSize() {
        return pList.size();
    }

    public Process getProcess(int i) {
        return pList.get(i);
    }

    public void removeP(int i) {
        pList.remove(i);
    }

    public void addP(int i, Process p) {
        pList.add(i, p);
    }

    public void appendP(Process p) {
        pList.add(p);
    }
    
}