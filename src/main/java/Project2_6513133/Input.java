//1.Panithan Kunsuntrontham 6513133 2. Thunyaphat Permsup 6513167 3.Mattana Olarikded 6513173 4.Suphanai chalood 6513176 
package Project2_6513133;

import java.io.*;
import java.util.*;
import java.util.concurrent.*;



class input{
    private String path; //path Src/main/java/project
    private String filename; // filename
    private Scanner scan;// scan for new input filename
    private int days;// days 
    public input(String p, String f){
        path = p;
        filename = f;
        scan = new Scanner(System.in);
    }
    public void New_try(ArrayList<FactoryThread> f,ArrayList<SupplierThread> s,ArrayList<Material> m){
        boolean sus = false;//check openable file?
        while(!sus){
            try(Scanner filescan = new Scanner(new File(path+filename))){
              sus = true;// if penable set true for break while loop
              
              while(filescan.hasNext()){
                  P_line(filescan.nextLine(),f,s,m); //input from config file
              }
              //suppilerThread's Cylicbarrier
              int S_parties = 0;
              S_parties += s.size();
              CyclicBarrier S_B = new CyclicBarrier(S_parties);
              //factoryThread's Cylicbarrier
              int F_parties = 0;
               F_parties += f.size();
              CyclicBarrier F_B = new CyclicBarrier(F_parties);
            for(int i=0; i<s.size(); i++){
              s.get(i).Setsync(S_B);//set Cyclicbarrrier
              s.get(i).set_days(this.days);//set days
            }
             for(int i=0; i<f.size(); i++){
              f.get(i).Setsync(F_B);//set Cyclicbarrrier
              f.get(i).set_days(this.days);//set days
            }
            }
            catch(FileNotFoundException e){
            System.out.println(e);
            System.out.printf("Thread %s >> Enter file for simulation =  \n",Thread.currentThread().getName());
            filename = scan.next();
            }
        }
    }
    public void set_days(int n)//set days
            
    {
        days = n;
    }
    public int get_days()//get days
    {
        return this.days;
    }
    public void print_from(){
        System.out.printf("%s >> read configs from %s\n",Thread.currentThread().getName(),path+filename);
    }
   public void P_line(String Line,ArrayList<FactoryThread> f,ArrayList<SupplierThread> s,ArrayList<Material> m){
       String [] col = Line.split(","); //spilt the columns from comma
      if(col[0].trim().compareToIgnoreCase("d")==0)//days of simulation
      {
          int days = Integer.parseInt(col[1].trim());
          this.set_days(days);//set days
      }else if(col[0].trim().compareToIgnoreCase("m")==0){//material
          for(int i=1; i<col.length; i++){
              Material M = new Material(col[i].trim());//Create Material with name argument
              m.add(M);//add to Arraylist
          }
      }else if((col[0].compareToIgnoreCase("s")==0))//SuppilerThread
      {
            String name = col[1].trim();//Thread name
            ArrayList<Integer> rate = new ArrayList<Integer>();//Array of rate
            for(int i=2; i<col.length; i++){
                int r = Integer.parseInt(col[i].trim());//rate
             rate.add(r);//add to ArrayList
            }
             SupplierThread S = new SupplierThread(name,m,rate);//create SupplierThread 
             s.add(S);//Add to ArrayList
      
              
      }else if((col[0].compareToIgnoreCase("f")==0))//FactoryThread
      {
           String name = col[1].trim();//Thread name
           String product = col[2].trim();//Product name
           int lot = Integer.parseInt(col[3].trim());
           ArrayList<Integer> require = new ArrayList<Integer>();//Array OF require
           for(int i=4; i<col.length; i++){
               int r = Integer.parseInt(col[i].trim());//Require
            require.add(r);//Add to ArrayList
                   }
           FactoryThread F = new FactoryThread(name,product,lot,m,require);//Create FactoryThraed
           f.add(F);//Add to ArrayList
           
      }
       
   }
}
