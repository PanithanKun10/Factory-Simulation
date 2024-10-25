//1.Panithan Kunsuntrontham 6513133 2. Thunyaphat Permsup 6513167 3.Mattana Olarikded 6513173 4.Suphanai chalood 6513176 
package Project2_6513133;


class Material{
    private int balance=0;//balance 
    private String name;//material name
  
    public Material(String n){
        name = n;
    }
    public void add(int n)//adding balance(each SuppplierThread )
    {
 
       this.balance+=n;
    }  
      
    public void get(int n){
        if(this.balance>=n){
          this.balance-=n;
        
        }else if(this.balance<n){
            this.balance=0;
         
        }
     
    }
   
    public String get_name(){
        return this.name;
    }
    
    public int get_balance(){
        return this.balance;
    }
    public void retrieve(int n){
        this.balance+=n;
    }
     
     
     
}