package spiderman;

/**
 * Steps to implement this class main method:
 * 
 * Step 1:
 * DimensionInputFile name is passed through the command line as args[0]
 * Read from the DimensionsInputFile with the format:
 * 1. The first line with three numbers:
 *      i.    a (int): number of dimensions in the graph
 *      ii.   b (int): the initial size of the cluster table prior to rehashing
 *      iii.  c (double): the capacity(threshold) used to rehash the cluster table 
 * 
 * Step 2:
 * ClusterOutputFile name is passed in through the command line as args[1]
 * Output to ClusterOutputFile with the format:
 * 1. n lines, listing all of the dimension numbers connected to 
 *    that dimension in order (space separated)
 *    n is the size of the cluster table.
 * 
 * @author Seth Kelley
 */

public class Clusters {
    private int tableSize;
    private Dim_Node[] Clusters;

    public Clusters(){
        Clusters=null;
        tableSize=0;
    }

    public Dim_Node[] getClusters() {return Clusters;}
    public int getTableSize(){return tableSize;}

    public void setClusters(Dim_Node[] Clusters){this.Clusters=Clusters;}
    public void setTableSize(int tableSize){this.tableSize=tableSize;}
    
    public void insert(Dim_Node dimension, int[] arr) {
        int index = dimension.getDimNumber() % Clusters.length;
        if(Clusters[index]==null) Clusters[index]=dimension;
        else {
            dimension.setNext_Dim_Node(Clusters[index]);
            Clusters[index] = dimension;
        }
        tableSize++;
        int currentLoadFactor = tableSize/ Clusters.length;
        if (currentLoadFactor >= arr[2]) rehash();
    }

    public void rehash() {
        int newSize = Clusters.length * 2;
        Dim_Node[] newClusters = new Dim_Node[newSize];

        for (int j=0;j<Clusters.length;j++ ) {
            Dim_Node node= Clusters[j];
            while (node != null) {
                Dim_Node array=new Dim_Node(node);
                int newIndex = array.getDimNumber() % newSize;
                array.setNext_Dim_Node(newClusters[newIndex]);
                newClusters[newIndex] = array;
                node = node.getNext_Dim_Node();
            }
        }
        Clusters = newClusters;
    }
    public void wrapItAround(){
        Dim_Node Ptr= Clusters[1];
        while (Ptr.getNext_Dim_Node()!=null) {
            Ptr = Ptr.getNext_Dim_Node();
        }
        Dim_Node node1 = new Dim_Node(Clusters[0].getDimNumber(), Clusters[0].getCanonEvents(), Clusters[0].getDimWeight(), null);
        Ptr.setNext_Dim_Node(node1);
        Ptr=Ptr.getNext_Dim_Node();
        Dim_Node node2= new Dim_Node(Clusters[Clusters.length-1].getDimNumber(), Clusters[Clusters.length-1].getCanonEvents(), Clusters[Clusters.length-1].getDimWeight(), null);
        Ptr.setNext_Dim_Node(node2);

        Dim_Node Ptr2= Clusters[0];
        while (Ptr2.getNext_Dim_Node()!=null) {
            Ptr2 = Ptr2.getNext_Dim_Node();
        }
        Dim_Node node3 = new Dim_Node(Clusters[Clusters.length-1].getDimNumber(), Clusters[Clusters.length-1].getCanonEvents(), Clusters[Clusters.length-1].getDimWeight(), null);
        Ptr2.setNext_Dim_Node(node3);
        Ptr2=Ptr2.getNext_Dim_Node();
        Dim_Node node4= new Dim_Node(Clusters[Clusters.length-2].getDimNumber(), Clusters[Clusters.length-2].getCanonEvents(), Clusters[Clusters.length-2].getDimWeight(), null);
        Ptr2.setNext_Dim_Node(node4);

        for(int i=2; i<Clusters.length;i++){
            Ptr=Clusters[i];
            while (Ptr.getNext_Dim_Node()!=null) {
                Ptr = Ptr.getNext_Dim_Node();
            }
            Dim_Node node5 = new Dim_Node(Clusters[i-1].getDimNumber(), Clusters[i-1].getCanonEvents(), Clusters[i-1].getDimWeight(), null);
            Ptr.setNext_Dim_Node(node5);
            Ptr=Ptr.getNext_Dim_Node();
            Dim_Node node6 = new Dim_Node(Clusters[i-2].getDimNumber(), Clusters[i-2].getCanonEvents(), Clusters[i-2].getDimWeight(), null);
            Ptr.setNext_Dim_Node(node6);
        }
    }
    public static void main(String[] args) {

        if ( args.length < 2 ) {
            StdOut.println(
                "Execute: java -cp bin spiderman.Clusters <dimension INput file> <collider OUTput file>");
                return;
        }
        StdIn.setFile(args[0]);
        StdOut.setFile(args[1]);
        int[] arr=StdIn.readAllInts();
        Clusters cl= new Clusters();
        cl.Clusters = new Dim_Node[arr[1]];
        cl.tableSize= 0;
        for(int i=3;i<((arr[0]+1)*3);i+=3){
            Dim_Node nodeImmaAdd= new Dim_Node(arr[i], arr[i+1], arr[i+2], null);
            cl.insert(nodeImmaAdd, arr);
        }
        cl.wrapItAround();
        for(int i=0; i<cl.Clusters.length; i++){
            Dim_Node ptr=cl.Clusters[i];
            while(ptr!=null){
                StdOut.print(ptr.getDimNumber() +" ");
                ptr=ptr.getNext_Dim_Node();
            }
            StdOut.println();
        }

    }
}
