package Restaurant_sim;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Jiayun
 */
public class CustomerThread extends Thread implements Restaurant_Control{
    private final int Pnumber; //to identify  an individual
    private final int Nnumber; // to identify the position of this individual in the group
    private final int Gnumber; // to identify the group which the individual is in 
    private boolean isReadyToEat; //to indicate whether the customer is eating
    private boolean finishedEating; //to indicate whether the customer has finished eating
    private ArrayList<Restaurant_Control> threadArrayList;
    private Restaurant_Monitor rc;
    
    public CustomerThread(int myPnumber, int myNnumber, int myGnumber, ArrayList mArrayList, Restaurant_Monitor newRc){
        Pnumber = myPnumber;
        Nnumber = myNnumber;
        Gnumber = myGnumber;
        rc = newRc;
        isReadyToEat = false;
        finishedEating = false;
        threadArrayList = mArrayList;
        start();
    }
    
 
    @Override
    public void run() {
        threadArrayList.add(this);
        if(rc.checkPaused()){
            synchronized(this){
                try {
                    wait();
                } catch (InterruptedException ex) {}
            }
        }
        try(FileWriter fw = new FileWriter("Customer.txt", true);
        BufferedWriter bw = new BufferedWriter(fw);
        PrintWriter out = new PrintWriter(bw))
        {
            out.println("<customer P"+Pnumber+" N"+Nnumber+" G"+Gnumber+" arrived>");    
        } catch (IOException e) {}
        rc.updateLog("<customer P"+Pnumber+" N"+Nnumber+" G"+Gnumber+" arrived>"+System.lineSeparator());
        while(rc.checkRunning()){
            if(rc.checkPaused()){
                synchronized(this){
                    try{
                        wait();
                    } catch (InterruptedException ex) {}
                }
            }   
             //Check if the customer is eating
            if(isReadyToEat && !finishedEating){
                Eat();
                Finished();
            }
            try {
                sleep(2000); //check every 2 seconds
            } catch (InterruptedException ex) {
                //deal with the situation that the thread is interrupted during sleep
//              Logger.getLogger(CustomerThread.class.getName()).log(Level.SEVERE, null, ex);
                Thread.currentThread().interrupt();
            }    
        }
      
        
    }
    private void Eat(){
        Random rd = new Random();
        int rdTime_Eating = rd.nextInt(1000)+1000; //eating takes 20-30seconds
        try {
            Thread.sleep(rdTime_Eating);
        } catch (InterruptedException ex) {
            Logger.getLogger(CustomerThread.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void ReadyToEat(){
        isReadyToEat = true;
    }
    
    private void Finished(){
        finishedEating = true;
//        System.out.println("Customer P" + Pnumber + " finishied eating! ");
        try {
            Thread.sleep(2000);
        } catch (InterruptedException ex) {
            //deal with the situation that the thread is interrupted during sleep
//            Logger.getLogger(CustomerThread.class.getName()).log(Level.SEVERE, null, ex);
            Thread.currentThread().interrupt();
        }
    }
    
    public boolean isFinished(){
        return finishedEating;
    }
    
    @Override
    public String toString(){
        return ("P"+Pnumber+"-"+"G"+Gnumber+"-"+"N"+Nnumber);
    }
    
    @Override
    public synchronized void Resume(){
        notify();
    }
}
