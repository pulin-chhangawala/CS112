package spiderman;
import java.util.*;

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
 * 2. a lines, each with:
 *      i.    The dimension number (int)
 *      ii.   The number of canon events for the dimension (int)
 *      iii.  The dimension weight (int)
 * 
 * Step 2:
 * SpiderverseInputFile name is passed through the command line as args[1]
 * Read from the SpiderverseInputFile with the format:
 * 1. d (int): number of people in the file
 * 2. d lines, each with:
 *      i.    The dimension they are currently at (int)
 *      ii.   The name of the person (String)
 *      iii.  The dimensional signature of the person (int)
 * 
 * Step 3:
 * HubInputFile name is passed through the command line as args[2]
 * Read from the SpotInputFile with the format:
 * One integer
 *      i.    The dimensional number of the starting hub (int)
 * 
 * Step 4:
 * AnomaliesInputFile name is passed through the command line as args[3]
 * Read from the AnomaliesInputFile with the format:
 * 1. e (int): number of anomalies in the file
 * 2. e lines, each with:
 *      i.   The Name of the anomaly which will go from the hub dimension to their home dimension (String)
 *      ii.  The time allotted to return the anomaly home before a canon event is missed (int)
 * 
 * Step 5:
 * ReportOutputFile name is passed in through the command line as args[4]
 * Output to ReportOutputFile with the format:
 * 1. e Lines (one for each anomaly), listing on the same line:
 *      i.   The number of canon events at that anomalies home dimensionafter being returned
 *      ii.  Name of the anomaly being sent home
 *      iii. SUCCESS or FAILED in relation to whether that anomaly made it back in time
 *      iv.  The route the anomaly took to get home
 * 
 * @author Seth Kelley
 */

public class GoHomeMachine {

    public static boolean SpideExists(Dim_Node vertex){
        Person ptr=vertex.getPeopleInDim();
        while (ptr!=null) {
            if(ptr.getDim_Signature()==ptr.getDim_origin()) return true;
            ptr=ptr.getNext_Person();     
        }
        return false;
    }

    public static Person Node_Spide_that_Exists_for_Some_Reason(Dim_Node vertex){
        Person ptr=vertex.getPeopleInDim();
        while (ptr!=null) {
            if(ptr.getDim_Signature()==ptr.getDim_origin()) return ptr;
            ptr=ptr.getNext_Person();     
        }
        return null;
    }
    
    public static void main(String[] args) {

        if ( args.length < 5 ) {
            StdOut.println(
                "Execute: java -cp bin spiderman.GoHomeMachine <dimension INput file> <spiderverse INput file> <hub INput file> <anomalies INput file> <report OUTput file>");
                return;
        }
        Collider Col=new Collider();
        Col.Make_The_Goddamn_Hash_Table(args[0]);
        Col.Make_The_Dumbass_Adjacency_List();
        Col.Add_The_People_Into_The_Stoobid_Spiderverse(args[1]);
        StdIn.setFile(args[2]);
        List<Person> persons_at_hub=new ArrayList<Person>();
        int hub = StdIn.readInt();
        int idx=hub%Col.getAdj_List().length;
        Dim_Node hubNode=Col.getAdj_List()[idx];
        while (hubNode.getDimNumber()!=hub){
            hubNode=hubNode.getNext_Dim_Node();           
        }
        StdIn.setFile(args[3]);
        StdOut.setFile(args[4]);
        int anomalies=StdIn.readInt();
        for(int i=0; i<anomalies; i++){
            String name=StdIn.readString();
            int time=StdIn.readInt();
            for(int j=0; j<Col.getAdj_List().length; j++){
                Dim_Node ptr=Col.getAdj_List()[j];
                while (ptr!=null) {
                    Person ptr2=ptr.getPeopleInDim();
                    while (ptr2!=null) {
                        if(ptr2.getName().equals(name)){
                            int a=ptr2.getDim_Signature();
                            Dim_Node w=Col.getAdj_List()[1];

                            for(int k=0; k<Col.getAdj_List().length; k++){
                                Dim_Node ww=Col.getAdj_List()[k];
                                while (ww!=null) {
                                    if(a==ww.getDimNumber()){
                                        w=ww;
                                    }
                                    ww=ww.getNext_Dim_Node();
                                }
                            }
                            List<Dim_Node> arr = Col.djikstra(hubNode, w);
                            StdOut.print(ptr2.getName()+" ");
                            if(arr!=null){
                                arr.add(w);
                            for(int b=0; b<arr.size()-1;b++){
                                StdOut.print(arr.get(b).getDimNumber()+" ");
                            }}
                            StdOut.println();
                        }
                            ptr2=ptr2.getNext_Person();
                    }
                    ptr=ptr.getNext_Dim_Node();
                }
            }
        }
        
    }
}
