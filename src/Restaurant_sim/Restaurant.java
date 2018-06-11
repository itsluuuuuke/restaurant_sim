/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Restaurant_sim;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Jiayun
 */
public class Restaurant extends Thread implements Restaurant_Control{
    private static Restaurant_Table rt;
    private static Customer cs;
    private static Head_Waiter hw;
    private static Waiter w;
    private static int capacity; //number of diners that the restaurant can hold 
    private static int table_TOTAL;
    private static int customer_TOTAL;
    private ArrayBlockingQueue<Table> freeTables;
    private ArrayBlockingQueue<Table> requestTables;
    private ArrayBlockingQueue<Table> servedTables;
    private ArrayBlockingQueue<Table> occupiedTables;
    private ArrayBlockingQueue<Customer_Group> waiting;
    private ArrayBlockingQueue<Customer_Group> entered;
    private static ArrayList<Restaurant_Control> threadArrayList;
    private Restaurant_Monitor rc;
    private final String username = "HE JIAYUN";
    private final String password = "restaurant2017";
    
    public Restaurant(int customer_TOTAL, int waiter_TOTAL, int newTable_TOTAL, int newCapacity, boolean mRunning){
        capacity = newCapacity;
        table_TOTAL = newTable_TOTAL;
        this.customer_TOTAL = customer_TOTAL;
        rc = new Restaurant_Monitor();
        threadArrayList = new ArrayList<Restaurant_Control>();
        waiting = new ArrayBlockingQueue<>(customer_TOTAL);
        entered = new ArrayBlockingQueue<>(customer_TOTAL);
        freeTables = new ArrayBlockingQueue<>(table_TOTAL);
        requestTables = new ArrayBlockingQueue<>(table_TOTAL);
        servedTables = new ArrayBlockingQueue<>(table_TOTAL);
        occupiedTables = new ArrayBlockingQueue<>(table_TOTAL);
        rt = new Restaurant_Table(freeTables,requestTables,servedTables,occupiedTables);
        cs = new Customer(customer_TOTAL,capacity,waiting,entered,threadArrayList,rc);
        hw = new Head_Waiter(waiting,entered,rt,threadArrayList,rc);
        w = new Waiter(waiter_TOTAL, rt,threadArrayList,rc);
        start();
    }
    
    public void run(){
        while(rc.checkRunning()){
            //update the occupied table list
            rt.update_occupied();
            //check if any group finished eating
            if(!occupiedTables.isEmpty()){
                Iterator<Table> oi = occupiedTables.iterator();
                while(oi.hasNext()){
                    Table tmpTable = oi.next();
                    int Gnumber = tmpTable.getCurrentGnumber();
                    int tableID = tmpTable.getID();
//                    System.out.println("table id = " + tableID);
//                    System.out.println("--Checking group finished eating--");
                    Customer_Group tmpcg = cs.LocateEnteredGroupByGnumber(Gnumber);
                    if(tmpcg != null){
                        if(tmpcg.isFinished()){
                            occupiedTables.remove();
                            rt.freeTable(tableID);
                        }
                    }
                }
               
            }
            //check if any table that is served, and let the customer begins eating
            rt.update_served();
//            cs.printEntered();
//           rt.printServedTables();
            //set the diners to be eating if the table is served
            if(!servedTables.isEmpty() && !entered.isEmpty()){
//                printEntered();
//                printServedTables();
                int Gnumber = -1;
                Table tmpTable = servedTables.poll();
                tmpTable.resetServed();
                Gnumber = tmpTable.getCurrentGnumber();
//                System.out.println("--Checking Group Served-- ");
//                System.out.println("Group number served: " + Gnumber);
//                System.out.println("Table ID: " + tmpTable.getID());
                Customer_Group tmpGroup = cs.LocateEnteredGroupByGnumber(Gnumber);
                if(tmpGroup == null)
                    continue;
                tmpGroup.EAT_AND_FINISH();//set the group eating
            }
            Random rd = new Random();
            try {
                Thread.sleep(rd.nextInt(1000)+100);
            } catch (InterruptedException ex) {
                return;
            }
        }
    }
    
    public void printEntered(){
        Iterator<Customer_Group> it = entered.iterator();
//        System.out.println("- List of Entered Customer Groups -");
        while(it.hasNext())
            System.out.print(it.next().getGroupNumber() + " ");
        System.out.println();
    }
      
    public void printServedTables(){
        Iterator<Table> i = servedTables.iterator();
//        System.out.println("List of served tables: ");
        while(i.hasNext()){
            System.out.print(i.next().getID() + " ");
        }
        System.out.println();
    }
    
    public String waitingToString(){
        Iterator<Customer_Group> it = waiting.iterator();
        String result = null;
        if(waiting.isEmpty())
            return "";
        while(it.hasNext())
            result += (it.next().toString() + System.lineSeparator());
        return result;
    }
    
    public String enteredToString(){
        Iterator<Customer_Group> it = entered.iterator();
        String result = null;
        if(entered.isEmpty())
            return "";
        while(it.hasNext())
            result += (it.next().toString() + System.lineSeparator());
        return result;
    }
    
    public String[] tableToString(){
        String[] result = new String[4];
        int[][] Gnumber  = rt.getGnumber();
        String[][] temp = rt.RtableToString();
        //identify table size
        for(int i = 0; i < 4; i++){
            //identify group 
            for(int j = 0; j < 10; j++){
                //if the table is free
                if(Gnumber[i][j] == -1)
                    temp[i][j] += System.lineSeparator();
                Customer_Group tempGroup = cs.LocateEnteredGroupByGnumber(Gnumber[i][j]);
                if(tempGroup != null){
                    temp[i][j] += tempGroup.toStringDetailed()+System.lineSeparator();
                    result[i] += temp[i][j];
                }
            }
        }
        return result;
    }
    
    public String headWaiterToString(){
        return hw.headWaiterToString();
    }
    
    public String[] waiterToString(){
        return w.waiterToString();
    }
    
    public synchronized void suspendAll(){
        rc.Pause();
    }
    public synchronized void resumeAll(){
        rc.Resume();
        for(int i = 0; i < threadArrayList.size(); i++)
            threadArrayList.get(i).Resume();
    }
    public String logToString(){
        return rc.toString();
    }
    
    public synchronized void stopAll(){
        rc.Stop();
    }
    @Override
    public void Resume() {
        
    }
    
    public boolean verify(String username, String password){
        if(this.username.equals(username) && this.password.equals(password))
            return true;
        return false;
    }
    
    public boolean closeTable(int tableID){
        return rt.closeTable(tableID);
    }
    
    public boolean openTable(int tableID){
        return rt.openTable(tableID);
    }
}
