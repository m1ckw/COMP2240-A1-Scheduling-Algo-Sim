
//*********************************************************************************************//
//  Class:  Summary                                                                            //
//  Author: Mick Wiedermann                                                                    //
//  Course: COMP2240 | Assignment 1                                                            //
//  Date  : 2021-09-02                                                                         //
//  Description: Simple object to store the average times for each algo.                       //
//***********************************************************************************************

public class Summary {
    
    private String name;
    private double avgTaroundTime;
    private double avgWaitTime;

    public Summary(String name) {
        this.name = name;
        this.avgTaroundTime = 0.0;
        this.avgWaitTime = 0.0;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getAvgTaroundTime() {
        return this.avgTaroundTime;
    }

    public void setAvgTaroundTime(double avgTaroundTime) {
        this.avgTaroundTime = avgTaroundTime;
    }

    public double getAvgWaitTime() {
        return this.avgWaitTime;
    }
    
    public void setAvgWaitTime(double avgWaitTime) {
        this.avgWaitTime = avgWaitTime;
    }
    
}
