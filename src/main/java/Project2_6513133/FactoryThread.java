//1.Panithan Kunsuntrontham 6513133 2. Thunyaphat Permsup 6513167 3.Mattana Olarikded 6513173 4.Suphanai chalood 6513176 
package Project2_6513133;

import java.util.ArrayList;
import java.util.concurrent.*;


class FactoryThread extends Thread implements Comparable<FactoryThread>{
  private CyclicBarrier Barrier;//Cyclicbarrier
    private int lot_size;// Amount of lots require
    private int current_lot=0;
    private int days;//days
    private String product_name; // product name
    private ArrayList<Material> mymaterial; // ArrayList of Material(Same as SupplierThread have)
    private ArrayList<Integer> require;//Require
    private ArrayList<Integer> Holding = new ArrayList<Integer>();
    public void Setsync(CyclicBarrier b){
            Barrier = b;        
    }
    public int compareTo(FactoryThread other) {
    if (this.current_lot < other.current_lot)       return 1;	
	else if (this.current_lot> other.current_lot)  return -1;	
	else                        return 0;
}
    public FactoryThread(String n,String p,int l,ArrayList<Material> m,ArrayList<Integer> r){
        super(n);
        product_name = p;
        lot_size =l;
        Set_Material(m);
        this.set_require(r);
        this.Set_Holding();
    }
    public void Set_Holding(){
        for(int i=0; i<this.mymaterial.size(); i++){
            Holding.add(0);
        }
    }
 
    public String get_product_name(){
        return this.product_name;
    }
    public int get_current_lot(){
        return this.current_lot;
    }
    public void Add_Holding(int i,int n){
        int temp = this.Holding.get(i)+n;
        this.Holding.set(i, temp);
    }
    public void Use_holding(int i, int n){
        int temp = this.Holding.get(i)-n;
        this.Holding.set(i,temp);
    }
    public synchronized void Holding(){
        for(int i=0; i<this.mymaterial.size(); i++){
             if(i==0){
                 System.out.printf("%s  >> holding ",Thread.currentThread().getName()); 
             }
             System.out.printf("%3d %s ",this.Holding.get(i),this.mymaterial.get(i).get_name());
        }
         System.out.printf("\n");
    }
    public void set_days(int n){
        days = n;
    }
    public void Set_Material(ArrayList<Material> m){
        this.mymaterial = new ArrayList<Material>();
        this.mymaterial = m;
     
    }    
    
  public void set_require(ArrayList<Integer> r){
      this.require= new ArrayList<Integer>();
       for(int i=0; i<r.size(); i++){
           this.require.add(r.get(i)*this.lot_size);
       }
   }
    public void print(){
        System.out.printf("%-1s  %s %s %8s %s ",this.getName(),"daliy"," use", "rates","=");
        for(int i=0; i<this.mymaterial.size(); i++){
             System.out.printf("%3d %s ",this.require.get(i),this.mymaterial.get(i).get_name());
        }
        System.out.printf("%s %3d %s ","producing",this.lot_size,this.product_name);
         System.out.print("\n");
    }
    public synchronized void Do_lot(){
        boolean Can_product = true;
        for(int i=0; i<this.mymaterial.size(); i++){
            if(this.Holding.get(i)<this.require.get(i)){
                Can_product = false;
            }
        }
        if(Can_product){
            for(int i=0; i<this.mymaterial.size(); i++){
             this.Use_holding(i, this.require.get(i));
            } 
            this.current_lot++;
        System.out.printf("%-1s  >> %-8s %s %s %3d\n",Thread.currentThread().getName(),this.product_name,"production succeeds,","lot",this.current_lot);
        }else{
            System.out.printf("%-1s  >> %-8s %s \n",Thread.currentThread().getName(),this.product_name,"production fails"); 
             for(int i=0; i<this.mymaterial.size(); i++){
            if(this.Holding.get(i)<this.require.get(i)&&this.Holding.get(i)>0){
                int temp = this.Holding.get(i);
                this.mymaterial.get(i).retrieve(temp);
                this.Use_holding(i, temp);
                System.out.printf("%-1s  >> %-9s %3d %-12s %12s %3d %s\n",Thread.currentThread().getName(),"put"
              ,temp,this.mymaterial.get(i).get_name(),"balance = ",this.mymaterial.get(i).get_balance(),this.mymaterial.get(i).get_name()); 
                
            }
        }
            
        }
    }
    public synchronized void get_material(){
           for(int i=0; i<this.mymaterial.size(); i++){
                   if(this.mymaterial.get(i).get_balance()>=this.require.get(i)){
                      this.mymaterial.get(i).get(this.require.get(i)); 
                      this.Add_Holding(i,this.require.get(i));
                       System.out.printf("%-1s  >> %-9s %3d %-12s %12s %3d %s\n",Thread.currentThread().getName(),"get",
                   this.require.get(i),this.mymaterial.get(i).get_name(),"balance = ",this.mymaterial.get(i).get_balance(),this.mymaterial.get(i).get_name());
                   }else if(this.mymaterial.get(i).get_balance()<this.require.get(i)){
                       int temp = this.mymaterial.get(i).get_balance();
                       this.Add_Holding(i,temp);
                       this.mymaterial.get(i).get(this.require.get(i));
                       System.out.printf("%-1s  >> %-9s %3d %-12s %12s %3d %s\n",Thread.currentThread().getName(),"get",
                   temp,this.mymaterial.get(i).get_name(),"balance = ",this.mymaterial.get(i).get_balance(),this.mymaterial.get(i).get_name());
                   }
                 
                 
            }
    }
    public void run(){
       int x = -1;
     
             try{x = this.Barrier.await();} catch(Exception e){}
              
          
         for(int i=1; i<=this.days; i++){
                    synchronized(SupplierThread.class){
                 try{
                     SupplierThread.class.wait();
                 }catch(InterruptedException e){
                     
                 }
             }
            
              try {
                Thread.sleep(1000); // Sleep for 1 second to simulate a day
              } catch (InterruptedException e) {
                e.printStackTrace();
              }
              try{x = this.Barrier.await();} catch(Exception e){}
                this.Holding();
                 try {
                Thread.sleep(1000); // Sleep for 1 second to simulate a day
              } catch (InterruptedException e) {
                e.printStackTrace();
              }
                try{x = this.Barrier.await();} catch(Exception e){}
                this.get_material();
                 try {
                Thread.sleep(1000); // Sleep for 1 second to simulate a day
              } catch (InterruptedException e) {
                e.printStackTrace();
              }
                try{x = this.Barrier.await();} catch(Exception e){}
                this.Do_lot();
                try {
                Thread.sleep(1000); // Sleep for 1 second to simulate a day
              } catch (InterruptedException e) {
                e.printStackTrace();
              }
                 synchronized(FactoryThread.class){
                     FactoryThread.class.notifyAll();
                 }
          }
       
    }
}
 