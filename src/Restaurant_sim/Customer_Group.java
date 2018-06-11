package Restaurant_sim;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Jiayun
 */
public class Customer_Group implements Restaurant_Control{
    private CustomerThread Customer_List[];
    private int size;
    private int Gnumber;
    private boolean entered;
    private boolean eating;
    private boolean finished;
    private int rdTime_in_group; //groups members arriving with this interval 
    private ArrayList<Restaurant_Control> threadArrayList;
    private Restaurant_Monitor rm;

    public Customer_Group(int newSize, int newGnumber, int newPnumber, ArrayList<Restaurant_Control> mArrayList, Restaurant_Monitor newRm){
        Gnumber = newGnumber;
        threadArrayList = mArrayList;
        size = newSize;
        rm = newRm;
        Random rd = new Random();
        Customer_List = new CustomerThread[newSize];
        
        for(int i = 0; i < size; i++){
            rdTime_in_group = rd.nextInt(1)+500; //Group Members arrive with interval of 0.5-4 seconds
            Customer_List[i] = new CustomerThread(newPnumber, i, Gnumber, threadArrayList, newRm);
            try{
                   Thread.sleep(rdTime_in_group);
                }catch(InterruptedException ie){}
        }
            
        
        eating = false;
        finished = false;
        entered = false;
    }

    public void EAT_AND_FINISH(){
//        System.out.println("Customer Group " + Gnumber + " is Eating!");
        eating = true;
        for (int i = 0; i < size; i++){
//            System.out.println("i = " + i);
            Customer_List[i].ReadyToEat();
        }
            
        try(FileWriter fw = new FileWriter("Customer.txt", true);
            BufferedWriter bw = new BufferedWriter(fw);
            PrintWriter out = new PrintWriter(bw)){
                out.println("--Group G" + Gnumber + " of size " + size + "  is eating!--");    
             }catch (IOException e) {}
        rm.updateLog("--Group G" + Gnumber + " of size " + size + "  is eating!--" + System.lineSeparator());
        //wait unitl all the customers had finised
        while(!isFinished()){
            try {
                Thread.sleep(1000);
            } catch (InterruptedException ex) {
                Logger.getLogger(Customer_Group.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        FINISHED();
    }
    public void FINISHED(){
        for (int i = 0; i < size; i++)
            Customer_List[i].interrupt();
//        System.out.println("Group " + Gnumber + " finished eating!");
        finished = true;
        try(FileWriter fw = new FileWriter("Customer.txt", true);
            BufferedWriter bw = new BufferedWriter(fw);
            PrintWriter out = new PrintWriter(bw)){
                out.println("--Group G" + Gnumber + " of size " + size + " finished eating!--");    
             }catch (IOException e) {}
        rm.updateLog("--Group G" + Gnumber + " of size " + size + "  finished eating!--" + System.lineSeparator());
    }
    
    public void printInfo(){
        System.out.println("Group number: " + Gnumber + ", Group size: " + size);
    }
    
    public int getSize(){
        return size;
    }
    
    public int getGroupNumber(){
        return Gnumber;
    }
    
    public boolean isFinished(){
        for(int i = 0; i < size; i++)
            if(!Customer_List[i].isFinished())
                return false;
        return true;
    }
    
    public Customer_Group Enter(){
        entered = true;
        return this;
    }
    
    public boolean hasEntered(){
        return entered;
    }
    
    @Override
    public String toString(){
        return("Group: " + Gnumber + " ,size " + size);
    }
    
    public String toStringDetailed(){
        String result = null;
        for(int i = 0; i < size; i++)
            result += Customer_List[i].toString()+System.lineSeparator();
        return result;
    }

    @Override
    public void Resume() {
        for(int i = 0; i < size; i++)
            Customer_List[i].Resume();
    }
    
}
