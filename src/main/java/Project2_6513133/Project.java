
package Project2_6513133;
import java.util.*;



public class Project {
    private static final Object lock = new Object();
    public static void main(String Args[]){
        String path = "src/main/java/Project2_6513133/" ;
        String []filename = {"config.txt","configs1.txt"};
        ArrayList<FactoryThread> F = new ArrayList<FactoryThread>();
        ArrayList<SupplierThread> S = new ArrayList<SupplierThread>();
        ArrayList<Material> M = new ArrayList<Material>();
      
        input cal1 = new input(path,filename[1]); //change file
      
        cal1.New_try(F,S,M);
        cal1.print_from();
        Project.print(cal1, F, S);
    
         for(int i=0; i<S.size(); i++){
          S.get(i).set_lock(lock);
        }
         for(int i=0; i<S.size(); i++){
          S.get(i).start();
        }
        for(int i=0; i<F.size(); i++){
          F.get(i).start();
        }
       for(int i=1; i<=cal1.get_days(); i++){
              
                System.out.print("-".repeat(60));
            System.out.printf("\n%s >> day %d\n",Thread.currentThread().getName(),i);
             
             synchronized (lock) {
                // Notify supplier threads that the day number is printed
               lock.notifyAll();
            }
             
              synchronized (FactoryThread.class) {
               try{
                  FactoryThread.class.wait(); 
               }catch(InterruptedException e){
                   
               }
                
                
            }

            try {
                Thread.sleep(3000); // Sleep for 1 second to simulate a day
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            
           }
    
         Collections.sort(F);
         System.out.print("-".repeat(60));
         System.out.printf("\n%s >> summary\n",Thread.currentThread().getName());
         for(int i=0; i<F.size(); i++){
             System.out.printf("%s >> %s %-8s %s %3d %s\n",Thread.currentThread().getName(),"Total",F.get(i).get_product_name(),"="
             ,F.get(i).get_current_lot(),"lots"); 
         }
    }
      public static void print(input cal1,ArrayList<FactoryThread> F,ArrayList<SupplierThread> S ){
        System.out.printf("\n%s >> simulation days = %d\n",Thread.currentThread().getName(),cal1.get_days());
        for(int i =0 ;i<S.size(); i++){
               System.out.printf("%s >> ",Thread.currentThread().getName());
               S.get(i).print();    
        }
        for(int i =0 ;i<F.size(); i++){
            System.out.printf("%s >> ",Thread.currentThread().getName());
            F.get(i).print();
        }
    }
        }
        
 
