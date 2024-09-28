package spiderman;

public class Person {
    private int Dim_Origin;
    private String Name;
    private int Dim_Signature;
    private Person next;

    public Person(int Dimensional_origin, String Name, int dimensional_signature, Person next){
        this.Dim_Origin=Dimensional_origin;
        this.Name=Name;
        this.Dim_Signature=dimensional_signature;
        this.next=next;
    }

    public int Size(){
        int i=0;
        while(next!=null){
            i++;
            next=next.getNext_Person();
        }
        return i;
    }

    public int getDim_origin() {return Dim_Origin;}
    public String getName() {return Name;}
    public int getDim_Signature() {return Dim_Signature;}
    public Person getNext_Person(){return next;}

    public void setDim_Origin(int Dim_Origin) {this.Dim_Origin = Dim_Origin;}
    public void setPerson_Name(String Name) {this.Name = Name;}
    public void setDim_Signature(int Dim_Signature) {this.Dim_Signature = Dim_Signature;}
    public void setNext_Person(Person Person){this.next=Person;}
}
