package spiderman;

public class Dim_Node {
    private int dimensionNumber;
    private int canonEvents;
    private int dimensionWeight;
    private Dim_Node next;
    private Dim_Node[] AdjArr;
    private Person PeopleInDim;

    public Dim_Node(Dim_Node dimension){
        this.dimensionNumber=dimension.getDimNumber();
        this.canonEvents=dimension.getCanonEvents();
        this.dimensionWeight=dimension.dimensionWeight;
        this.next=null;
        this.AdjArr=null;
        this.PeopleInDim=null;
    }

    public Dim_Node(int dimensionNumber, int canonEvents,int dimensionWeight,Dim_Node dimension) {
        this.dimensionNumber = dimensionNumber;
        this.canonEvents = canonEvents;
        this.dimensionWeight = dimensionWeight;
        this.next = dimension;
        this.AdjArr=null;
        this.PeopleInDim=null;
    }

    public int Size(Dim_Node node){
        int i=0;
        while(node!=null){
            i++;
            node=node.getNext_Dim_Node();
            if(i>1000) return -1;
        }
        return i;
    }
    
    public int getDimNumber() {return dimensionNumber;}
    public int getCanonEvents() {return canonEvents;}
    public int getDimWeight() {return dimensionWeight;}
    public Dim_Node getNext_Dim_Node() {return next;}
    public Dim_Node[] getAdj_Arr(){return AdjArr;}
    public Person getPeopleInDim(){return PeopleInDim;}

    public void setDimNumber(int dimensionNumber) {this.dimensionNumber = dimensionNumber;}
    public void setCanonEvents(int canonEvents) {this.canonEvents = canonEvents;}
    public void setDimWeight(int dimensionWeight) {this.dimensionWeight = dimensionWeight;}
    public void setNext_Dim_Node(Dim_Node next) {this.next = next;}
    public void setAdj_Arr(Dim_Node[] Arr) {this.AdjArr=Arr;}
    public void setPeopleInDim(Person Node){this.PeopleInDim=Node;}
}
