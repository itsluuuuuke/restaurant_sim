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

/**
 *
 * @author Jiayun
 */
public class Table {
    private int size;
    private int id;
    private boolean occupied;
    private boolean request;
    private boolean served;
    private int currentGroupNumber;
    private int currentGroupSize;
    private String status;
    private boolean closed;
    
    public Table(int mySize, int newID){
        size = mySize;
        status = "free";
        occupied = false;
        request = false;
        served = false;
        closed = false;
        currentGroupNumber = -1;
        currentGroupSize = -1;
        id = newID;
    }
    
    public Table occupy(int newGroupNumber, int newGroupSize){
        occupied = true;
        currentGroupNumber = newGroupNumber;
        currentGroupSize = newGroupSize;
        try(FileWriter fw = new FileWriter("Customer.txt", true);
             BufferedWriter bw = new BufferedWriter(fw);
             PrintWriter out = new PrintWriter(bw)){
                out.println("<<<Table of size " + size + " allocated to Group " + currentGroupNumber + " size " + currentGroupSize + " >>>");    
             } catch (IOException e) {} 
        return this;
    }
    
    public boolean isfree(){
        return !occupied;
    }
    public int getSize(){
        return size;
    }
    public int getID(){
        return id;
    }
    public int getCurrentGnumber(){
        return currentGroupNumber;
    }
    public int getCurrentGroupSize(){
        return currentGroupSize;
    }
    public boolean isRequest(){
        return request;
    }
    public void request(){
        request = true;
        status = "waiting for service";
    }
    public void resetRequest(){
        request = false;
    }
    public Table checkAndOccupy(int Gnum, int Gsize){
        if(Gsize == size || Gsize == size-1){
            occupy(Gnum, Gsize);
            return this;
        }
        return null;
    }
    
    public void freeTable(){
        occupied = false;
        status = "free";
        currentGroupNumber = -1;
    }
    
    public Table served(){
        served = true;
        status = "served";
        return this;
    }
    
    public void resetServed(){
        served = false;
        status = "eating";
    }
    
    public boolean isServed(){
        return served;
    }
    
    //return the Gnumber to inform restaurant that this group is eating
    public int dining(){
        return currentGroupNumber; 
    }
    
    public String tableToString(){
        return ("table id: " + id + " status: " + status + System.lineSeparator());
    }
    
    public boolean open(){
        closed = false;
        return true;
    }
    public boolean close(){
        if(occupied){
            System.out.println("Table is occupied! Failed to close");
            return false;
        }
        closed = true;
        return true;
    }
    public boolean isClosed(){
        return closed;
    }
}
