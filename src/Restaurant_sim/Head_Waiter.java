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
import java.util.Comparator;
import java.util.Iterator;
import java.util.Random;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Jiayun
 */
public class Head_Waiter extends Thread implements Restaurant_Control{
    private ArrayBlockingQueue<Customer_Group> cqueue;
    private ArrayBlockingQueue<Customer_Group> cqueueEntered;
    private Restaurant_Table rt;
    private int firstGsize;
    private int firstGnum;
    private ArrayList<Restaurant_Control> threadArrayList;
    private Restaurant_Monitor rc;
    private String status;

    public Head_Waiter(ArrayBlockingQueue<Customer_Group> newCqueue, ArrayBlockingQueue<Customer_Group> newCqueueEntered, Restaurant_Table newRt, ArrayList<Restaurant_Control> mArrayList,Restaurant_Monitor newRc){
        status = "free";
        cqueue = newCqueue;
        cqueueEntered = newCqueueEntered;
        rc = newRc;
        rt = newRt;
        threadArrayList = mArrayList;
        start();
    }
    @Override
    public void run(){
        threadArrayList.add(this);
        while(rc.checkRunning()){
            if(rc.checkPaused()){
                synchronized(this){
                    try {
                        wait();
                    } catch (InterruptedException ex) {} 
                }
            }
            if(cqueue.isEmpty())
                continue;
            firstGsize = checkFirstSize();
            firstGnum = checkFirstGnum();
            if(firstGsize == -1 || firstGnum == -1)
                continue;
            //Head waiter update and retrieve information from Restaurant_Table
            rt.update_availablity();
            int tableID = match(rt.getFreeTables(),firstGsize,firstGnum); 
            if(tableID != -1){
                WALK_TO_TABLE(tableID);
                rt.request(tableID); //ask waiter to handle the sitted table
                BACK_TO_ENTRANCE();
            }
        }
    }
    
    //This function is for matching the first group in line to a suitable available table and return the id of the table
    //And occupy the table, returns the table's ID
    private int match(ArrayBlockingQueue<Table> fTables, int Gsize, int Gnum){
        Comparator<Table> byTableSize = (t1,t2) -> Integer.compare(t1.getSize(), t2.getSize());//define a way to sort the freetable list
        fTables.stream().sorted(byTableSize);
        Iterator<Table> it = fTables.iterator();
        while(it.hasNext()){
            Table tmp = rt.checkAndOccupy(it.next().getID(), Gnum, Gsize);
            if(tmp != null){
                it.remove();
                return tmp.getID();
            }
        }
        return -1;
    }
    
    //Return the size of the first group in queue
    private int checkFirstSize(){
        if(!cqueue.isEmpty()){
            Customer_Group temp = cqueue.peek();
            return temp.getSize();
        }
        return -1; //if the queue is empty, return -1
    }
    private int checkFirstGnum() {
        if(!cqueue.isEmpty()){
            Customer_Group temp = cqueue.peek();
            return temp.getGroupNumber(); 
        }
        return -1; //if the queue is empty, return -1   
    }  
    private void WALK_TO_TABLE(int tblID){
        Random rd = new Random();
        int rdTime = rd.nextInt(1000)+1500; //The walking takes 1.5 to 2.5 seconds
        //poll the customer group out of the waiting queue to the entered queue
        Customer_Group tmpGroup = cqueue.peek().Enter();
        
        try {
            cqueueEntered.put(cqueue.take());
        } catch (InterruptedException ex) {
            Logger.getLogger(Head_Waiter.class.getName()).log(Level.SEVERE, null, ex);
        }
        int tmpGnumber = tmpGroup.getGroupNumber();
        status = ("Walking customer group " + tmpGnumber + " to table ID " + tblID);
        try(FileWriter fw = new FileWriter("Customer.txt", true);
             BufferedWriter bw = new BufferedWriter(fw);
             PrintWriter out = new PrintWriter(bw)){
                out.println("<<<Head Waiter walked customer group " + tmpGnumber + " to table ID "+ tblID + " >>>");    
             } catch (IOException e) {}
        rc.updateLog("<<<Head Waiter walked customer group " + tmpGnumber + " to table ID "+ tblID + " >>>"+System.lineSeparator());
        try {
            Thread.sleep(rdTime);
        } catch (InterruptedException ex) {
            Logger.getLogger(Head_Waiter.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        
        
    }
    private void BACK_TO_ENTRANCE(){
        Random rd = new Random();
        status = "Walking back to entrance";
        int rdTime = rd.nextInt(500)+1000; //The walking takes 0.5 to 1.5 seconds
        try {
            Thread.sleep(rdTime);
        } catch (InterruptedException ex) {
            Logger.getLogger(Head_Waiter.class.getName()).log(Level.SEVERE, null, ex);
        }
        status = "free";
    }
    
    @Override
    public synchronized void Resume(){
        notify();
    }
    
    public String headWaiterToString(){
        return status;
    }

}
