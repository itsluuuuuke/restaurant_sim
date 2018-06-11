package Restaurant_sim;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.ArrayBlockingQueue;
/**
 *
 * @author Jiayun
 */
//This thread generates customer groups 
public class Customer_Generator extends Thread implements Restaurant_Control{
    private Random rd;
    private int rdTime_group; //groups arriving with this interval 
    private int rdSize;//Generate Group with random size
    private int groupNumber;
    private int currentNumber; //current number of person generated
    private final int TOTAL;
    private ArrayBlockingQueue<Customer_Group> cqueue;
    private ArrayList<Restaurant_Control> threadArrayList;
    private Restaurant_Monitor rc;
    
    public Customer_Generator(int newTotal, ArrayBlockingQueue<Customer_Group> newCqueue, ArrayList mArrayList, Restaurant_Monitor newRc){
        TOTAL = newTotal;
        cqueue = newCqueue;
        threadArrayList = mArrayList;
        rc = newRc;
    }
    
    @Override
    public void run(){      
        threadArrayList.add(this);
        groupNumber = 1; //starts with 1, which corresponds to the Gnumber in CustomerThread       
        Customer_Group cGroup = null;
        //Create Groups
        while(currentNumber < (TOTAL) && rc.checkRunning()){
            if(rc.checkPaused()){
                synchronized(this){
                    try {
                        wait();
                    } catch (InterruptedException ex) {}
                }
            }
            rd = new Random();
            rdSize = rd.nextInt(8)+1;
            rdTime_group = rd.nextInt(500)+500; // Groups arrive with interval of 0.5-1 seconds
            //To make sure not to exceed the limit
            if(rdSize+currentNumber>TOTAL)
                rdSize = TOTAL - currentNumber;
//            System.out.println("Group Size =  " + rdSize);
                cGroup = new Customer_Group(rdSize,groupNumber,currentNumber,threadArrayList, rc);
                currentNumber+=rdSize;
                
            cqueue.add(cGroup);
            try{
            Thread.sleep(rdTime_group);
            }catch(InterruptedException ie){
                return;
            }
             try(FileWriter fw = new FileWriter("Customer.txt", true);
             BufferedWriter bw = new BufferedWriter(fw);
             PrintWriter out = new PrintWriter(bw)){
                out.println("Group G" + groupNumber + " has all arrived");    
             } catch (IOException e) {}
             groupNumber++;  
        }
        
    }
    
    public ArrayBlockingQueue<Customer_Group> getCustomerWaitingList(){
        return cqueue;
    }
    
    //return the customer groups that have entered the restaurant
    public ArrayBlockingQueue<Customer_Group> getEntered(){
        ArrayBlockingQueue<Customer_Group> entered = new ArrayBlockingQueue<>(TOTAL);
        while(!cqueue.isEmpty()){
            Customer_Group tempcg = cqueue.peek();
            tempcg.printInfo();
            if(tempcg.hasEntered()){
                entered.add(tempcg);
                cqueue.remove();
                System.out.println("Customer Group "+ entered.peek().getGroupNumber() + " added to entered list");
            }
            break;      
        }
        
        return entered;
    }

    @Override
    public synchronized void Resume() {
        notify();
    }
}
