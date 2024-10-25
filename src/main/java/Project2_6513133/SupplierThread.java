//1.Panithan Kunsuntrontham 6513133 2. Thunyaphat Permsup 6513167 3.Mattana Olarikded 6513173 4.Suphanai chalood 6513176 
package Project2_6513133;

import java.util.ArrayList;
import java.util.concurrent.*;


class SupplierThread extends Thread{
    private CyclicBarrier Barrier;//CyclicBarrier
    private ArrayList<Material> mymaterial; //Material as same as FactoryTheread have
    private ArrayList<Integer> rate;
    private int days;//days
    private Object lock;
    
   
    public void Setsync(CyclicBarrier b){
            Barrier = b;        
    }
    public void set_lock(Object l){
        this.lock = l;
    }
    public SupplierThread(String n,ArrayList<Material> m,ArrayList<Integer> R){
        super(n);
        Set_Material(m);
        this.set_rate(R);
    }
    public int get_Material_size(){
        return this.mymaterial.size();
    }
    public void set_days(int n){
        days = n;
    }
    public void set_rate(ArrayList<Integer> r){
        rate = new ArrayList<Integer>();
        for(int i=0; i<r.size(); i++){
            rate.add(r.get(i));
        }
        
    }
     public void Set_Material(ArrayList<Material> m)//Shallow copy for being able tp acess the same Materials
     {
        mymaterial = new ArrayList<Material>();
        mymaterial = m;
    }
   
   public synchronized void add_material()//Adding Material
   {
           
            for(int i=0; i<this.mymaterial.size(); i++){
                 this.mymaterial.get(i).add(this.rate.get(i));
                     System.out.printf("%-1s >> %-9s %3d %-12s %12s %3d %s\n",Thread.currentThread().getName(),"put"
                             ,this.rate.get(i),this.mymaterial.get(i).get_name(),"balance = ",this.mymaterial.get(i).get_balance(),this.mymaterial.get(i).get_name());   
            }
             
        }
        
       
 
    public void print()//Print The Info from input file
    {
        System.out.printf("%-1s %s %7s %s %s ",this.getName(),"daliy", "supply", "rates","=");
        for(int i=0; i<this.mymaterial.size(); i++){
           
        System.out.printf("%3d %s ", this.rate.get(i),this.mymaterial.get(i).get_name());
    
        }
        System.out.printf("\n");
    }
    @Override
    public void run()
    {
        int x = -1;
           for(int i=1; i<=this.days; i++){
             
                 synchronized (lock) {
                  try {
                
                    lock.wait();// wait until main thread will have printed the days
                
              } catch (InterruptedException e) {
                e.printStackTrace();
               }
                
                  } 
              
               
           try{x = this.Barrier.await();} catch(Exception e){}
            this.add_material();  
               synchronized(SupplierThread.class){
                  
                    SupplierThread.class.notifyAll();// Notify FactoryThread
                 
               }
               try{x = this.Barrier.await();} catch(Exception e){} 
               try {
                Thread.sleep(2000); // Sleep for 1 second to simulate a day
              } catch (InterruptedException e) {
                e.printStackTrace();
              }
           
              }
              }
          
           
          
          
    
      }

