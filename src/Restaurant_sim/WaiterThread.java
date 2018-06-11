/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Restaurant_sim;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Jiayun
 */
public class WaiterThread extends Thread implements Restaurant_Control{
    private int Wnumber; //to identify the waiter
    private Restaurant_Table rt;
    private ArrayList<Restaurant_Control> threadArrayList;
    private Restaurant_Monitor rc;
    private String status;
    
    public WaiterThread(int myWnumber, Restaurant_Table myRt, ArrayList<Restaurant_Control> threadArrayList,Restaurant_Monitor newRc){
        Wnumber = myWnumber;
        rc = newRc;
        rt = myRt;
        this.threadArrayList = threadArrayList;
        status = "free";
    }
    @Override
    public void run(){
        threadArrayList.add(this);
//        System.out.println("Waiter C" + Wnumber + "Start");
        //The waiter update and retrieve information from the restaurant_table
        while(rc.checkRunning()){
            if(rc.checkPaused()){
                synchronized(this){
                    try {
                        wait();
                    } catch (InterruptedException ex) {} 
                }
            }
            rt.update_request();
            if(!rt.getRequestTables().isEmpty()){
                int tblID = rt.getRequestTables().peek().getID(); //get the table id
                rt.resetRequest(tblID); //Answer the request and reset it
                serve(tblID);
                status = "free";
            }
            else{
                Random rd = new Random();
                try{
                    Thread.sleep(rd.nextInt(500)+500); //if no customer is requesting, take a 0.5-1s rest
                }catch(InterruptedException ie){}
            }
        }

        
    }
    private void serve(int tblID){
        Random rd = new Random();
        int rdTime = rd.nextInt(6000)+4000; //serve with 4-10 seconds
        status = ("begins to serve table with ID " + tblID);
        try(FileWriter fw = new FileWriter("Customer.txt", true);
            BufferedWriter bw = new BufferedWriter(fw);
            PrintWriter out = new PrintWriter(bw)){
                out.println("<<<<Waiter " + Wnumber + " begins to serve table with ID " + tblID + ">>>>");    
             }catch (IOException e) {}
        status = ("serving table with ID " + tblID);
        try {
            Thread.sleep(rdTime);
        } catch (InterruptedException ex) {
            Logger.getLogger(WaiterThread.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        rt.tableServed(tblID);
        
        try(FileWriter fw = new FileWriter("Customer.txt", true);
            BufferedWriter bw = new BufferedWriter(fw);
            PrintWriter out = new PrintWriter(bw)){
                out.println("<<<<Waiter " + Wnumber + " finishes to serve table with ID " + tblID + ">>>>");    
             }catch (IOException e) {}
        rc.updateLog("<<<<Waiter " + Wnumber + " finishes to serve table with ID " + tblID + ">>>>" + System.lineSeparator());
        status = ("Finished serving table with ID " + tblID);
    }
        
    @Override
    public synchronized void Resume(){
        notify();
    }
    
    @Override
    public String toString(){
        return status;
    }
}
