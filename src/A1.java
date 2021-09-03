
//*********************************************************************************************//
//  Class:  A1 - Main                                                                          //
//  Author: Mick Wiedermann                                                                    //
//  Course: COMP2240 | Assignment 1                                                            //
//  Date  : 2021-09-02                                                                         //
//  Description: Main class to import the data files and run the algorithm simulations.        //
//***********************************************************************************************

import java.io.File;
import java.io.FileNotFoundException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class A1 {

    private static List<Summary> averages;             // Stores the average results for each algo.
    private static DecimalFormat df;

    public static void main(String[] args) {
      ProcessList processList = new ProcessList();
      List<Integer> lotteryArray = new ArrayList<>();
      averages = new ArrayList<>();
      df = new DecimalFormat();
      df.setMinimumFractionDigits(2);
      df.setMaximumFractionDigits(2);
      Scanner userInput = new Scanner(System.in);

      System.out.print("\nPlease enter the file name you wish to upload in the following format, \"name.txt\" :");
      String fileName = userInput.next();
      System.out.println("");
      userInput.close();

      try {
        File file = new File(fileName);
        Scanner scanner = new Scanner(file);
        String rubish = "";
        scanner.skip(rubish).next();
        scanner.skip(rubish).next();
        Scheduler.setDispTime(scanner.nextInt()); 
        scanner.skip(rubish).next();
        for (int i=0; i<5; i++) {
          scanner.skip(rubish).next();
          String id = scanner.next();
          scanner.skip(rubish).next();
          int arrive = scanner.nextInt();
          scanner.skip(rubish).next();
          int execSize = scanner.nextInt();
          scanner.skip(rubish).next();
          int tickets = scanner.nextInt();
          scanner.skip(rubish).next();
          Process newProcess = Process.createProcess(id, arrive, execSize, tickets);
          processList.addProcess(newProcess);
        }
        scanner.skip(rubish).next();
        scanner.skip(rubish).next();
        int lotteryNum;
        for (int i=0; i<18; i++) {
          lotteryNum = scanner.nextInt();
          lotteryArray.add(lotteryNum);
        }
        scanner.close();
      } catch (FileNotFoundException e) {
          System.out.println(" ~ Hmmmm, an error occurred. This did work when I tested it... ¯\\_(ツ)_/¯ ~ ");
          e.printStackTrace();
        }
        
      FCFS fcfs = new FCFS(processList);
      fcfs.prepReadyQueue();
      fcfs.runFCFS();
      add2SumList(fcfs.getCompletedQ(), "FCFS");
      fcfs.printResults("FCFS");
      fcfs.printTable();
        
      SRT srt = new SRT(processList);
      srt.prepReadyQueue();
      srt.runSRT();
      add2SumList(srt.getCompletedQ(), "SRT");
      srt.printResults("SRT");
      srt.printTable();

      LTR ltr = new LTR(processList, lotteryArray);
      ltr.prepReadyQueue();
      ltr.runLTR();
      add2SumList(ltr.getCompletedQ(), "LTR");
      ltr.printResults("LTR");
      ltr.printTable();

      printSummary();

    }

    // Prints out the summary table containg the average times for each algo. 
    public static void printSummary() {
        System.out.println("Summary:");
        System.out.println("Algorithm    Average Turnaround Time    Waiting Time");
        for (int i=0; i<averages.size(); i++) {
            System.out.println(averages.get(i).getName() + "\t     "
            + (df.format(averages.get(i).getAvgTaroundTime())) + "     \t\t\t"
            + (df.format(averages.get(i).getAvgWaitTime())));
        }
        System.out.println("");
    }

    // Calculates the averages of an algos performance and adds them to the summary list.
    public static void add2SumList(ProcessList pList, String algo) {
        int totalTaroundTime = 0;
        int totalWaitTime = 0;
        for (int i=0; i<pList.getSize(); i++) {
            totalTaroundTime += pList.getProcess(i).getTurnAroundTime();
            totalWaitTime += pList.getProcess(i).getWaitTime();
        }
        Summary summary = new Summary(algo);
        summary.setAvgTaroundTime((double)totalTaroundTime/pList.getSize());
        summary.setAvgWaitTime((double)totalWaitTime/pList.getSize());
        averages.add(summary);
    }
}