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
import java.util.Iterator;
import java.util.concurrent.ArrayBlockingQueue;

/**
 *
 * @author Jiayun
 */
public class Restaurant_Table{
    private final int num_tb_two = 5;
    private final int num_tb_four = 9;
    private final int num_tb_six = 5;
    private final int num_tb_eight = 3;
    private final int table_TOTAL;
    private static Table[] tb_two;
    private static Table[] tb_four;
    private static Table[] tb_six;
    private static Table[] tb_eight;
    private ArrayBlockingQueue<Table> freeTables;
    private ArrayBlockingQueue<Table> requestTables;
    private ArrayBlockingQueue<Table> servedTables;
    private ArrayBlockingQueue<Table> occupied; 
    
    public Restaurant_Table(ArrayBlockingQueue<Table> newF, ArrayBlockingQueue<Table> newR, ArrayBlockingQueue<Table> newS, ArrayBlockingQueue<Table> newO){
        int id = 0;
        tb_two = new Table[num_tb_two];
        tb_four = new Table[num_tb_four];
        tb_six = new Table[num_tb_six];
        tb_eight = new Table[num_tb_eight];
        
        for(int i = 0; i< num_tb_two; i++)
            tb_two[i] = new Table(2,id++);
        for(int i = 0; i< num_tb_four; i++)
            tb_four[i] = new Table(4,id++);
        for(int i = 0; i< num_tb_six; i++)
            tb_six[i] = new Table(6,id++);
        for(int i = 0; i< num_tb_eight; i++)
            tb_eight[i] = new Table(8,id++);
        
        table_TOTAL = num_tb_two + num_tb_four + num_tb_eight + num_tb_six;
        freeTables = newF;
        requestTables = newR;
        servedTables = newS;
        occupied = newO;
    }
    
    public void update_availablity(){
        freeTables.clear();
        for(int i = 0; i< num_tb_two; i++)
            if(tb_two[i].isfree() && !tb_two[i].isClosed())
                freeTables.add(tb_two[i]);
        for(int i = 0; i< num_tb_four; i++)
            if(tb_four[i].isfree() && !tb_four[i].isClosed())
                freeTables.add(tb_four[i]);
        for(int i = 0; i< num_tb_six; i++)
            if(tb_six[i].isfree() && !tb_six[i].isClosed())
                freeTables.add(tb_six[i]);
        for(int i = 0; i< num_tb_eight; i++)
            if(tb_eight[i].isfree() && !tb_eight[i].isClosed())
                freeTables.add(tb_eight[i]);
    }
    
    public void update_request(){
        requestTables.clear();
        for(int i = 0; i< num_tb_two; i++)
            if(tb_two[i].isRequest())
                requestTables.add(tb_two[i]);
        for(int i = 0; i< num_tb_four; i++)
            if(tb_four[i].isRequest())
                requestTables.add(tb_four[i]);
        for(int i = 0; i< num_tb_six; i++)
            if(tb_six[i].isRequest())
                requestTables.add(tb_six[i]);
        for(int i = 0; i< num_tb_eight; i++)
            if(tb_eight[i].isRequest())
                requestTables.add(tb_eight[i]);
    }
    
    public void update_served(){
        servedTables.clear();
        for(int i = 0; i < num_tb_two; i++)
            if(tb_two[i].isServed() == true)
                servedTables.add(tb_two[i]);
        for(int i = 0; i < num_tb_four; i++)
            if(tb_four[i].isServed() == true)
                servedTables.add(tb_four[i]);
        for(int i = 0; i < num_tb_two; i++)
            if(tb_six[i].isServed() == true)
                servedTables.add(tb_six[i]);
        for(int i = 0; i < num_tb_eight; i++)
            if(tb_eight[i].isServed() == true)
                servedTables.add(tb_eight[i]);
    }
    
    public void update_occupied(){
        occupied.clear();
        for(int i = 0; i < num_tb_two; i++)
            if(!tb_two[i].isfree())
                occupied.add(tb_two[i]);
        for(int i = 0; i < num_tb_four; i++)
            if(!tb_four[i].isfree())
                occupied.add(tb_four[i]);
        for(int i = 0; i < num_tb_six; i++)
            if(!tb_six[i].isfree())
                occupied.add(tb_six[i]);
        for(int i = 0; i < num_tb_eight; i++)
            if(!tb_eight[i].isfree())
                occupied.add(tb_eight[i]);
    }
    public Table LocateTableByID(int tblID){
        for(int i = 0; i < num_tb_two; i++)
            if(tb_two[i].getID() == tblID)
                return tb_two[i];
        for(int i = 0; i < num_tb_four; i++)
            if(tb_four[i].getID() == tblID)
                return tb_four[i];
        for(int i = 0; i < num_tb_six; i++)
            if(tb_six[i].getID() == tblID)
                return tb_six[i];
        for(int i = 0; i < num_tb_eight; i++)
            if(tb_eight[i].getID() == tblID)
                return tb_eight[i];
        return null;
    }
    
    //used by the head waiter
    public void request(int TableID){
        LocateTableByID(TableID).request(); 
    }
    
    //used by waiters
    public int resetRequest(int TableID){
        LocateTableByID(TableID).resetRequest();
        return TableID;
    }
    public ArrayBlockingQueue<Table> getFreeTables(){
        return freeTables;
    }
    
    public ArrayBlockingQueue<Table> getRequestTables(){
        return requestTables;
    }
   
    public ArrayBlockingQueue<Table> getServedTables(){
        return servedTables;
    }
    
    public ArrayBlockingQueue<Table> getOccupiedTables(){
        return occupied;
    }
    
    public Table checkAndOccupy(int tblID, int Gnumber, int Gsize){
        return LocateTableByID(tblID).checkAndOccupy(Gnumber, Gsize);
    }
    
    public void freeTable(int tblID){
        LocateTableByID(tblID).freeTable();
        try(FileWriter fw = new FileWriter("Customer.txt", true);
            BufferedWriter bw = new BufferedWriter(fw);
            PrintWriter out = new PrintWriter(bw)){
                out.println("-Table ID " + tblID + " is free now!-");    
             }catch (IOException e) {}
    }
    
    //return the Gnumber of the dining group for later use
    public int dining(int tblID){
        return LocateTableByID(tblID).dining();
    }
    
    public int getTableTOTAL(){
        return table_TOTAL;
    }
    
    public void tableServed(int tblID){
        LocateTableByID(tblID).served();
        servedTables.add(LocateTableByID(tblID).served());
//        System.out.println("-In tableServed-  tableID:" + servedTables.peek().getID());
//        printServedTables();
    }

    public String[][] RtableToString(){
        int count = 0;
        String[][] result = new String[4][10]; 
        
        //The Gnumber[][] is to help identify the group and print the customer there
        for(int i = 0; i < num_tb_two; i++)
            result[0][i] += tb_two[i].tableToString();
        for(int i = 0; i < num_tb_four; i++)
            result[1][i] += tb_four[i].tableToString();
        for(int i = 0; i < num_tb_six; i++)
            result[2][i] += tb_six[i].tableToString();
        for(int i = 0; i < num_tb_eight; i++)
            result[3][i] += tb_eight[i].tableToString();
        
        return result;
    }
    
    public int [][] getGnumber(){
        int[][] result = new int[4][10];
        for(int i = 0; i < num_tb_two; i++)
            result[0][i] = tb_two[i].getCurrentGnumber();
        for(int i = 0; i < num_tb_four; i++)
            result[1][i] = tb_four[i].getCurrentGnumber();
        for(int i = 0; i < num_tb_six; i++)
            result[2][i] = tb_six[i].getCurrentGnumber();
        for(int i = 0; i < num_tb_eight; i++)
            result[3][i] = tb_eight[i].getCurrentGnumber();
        return result;
    }
    
    public boolean closeTable(int tableID){
        return LocateTableByID(tableID).close();
    }
    public boolean openTable(int tableID){
        return LocateTableByID(tableID).open();
    }

}
