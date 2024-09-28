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
 * Read from the HubInputFile with the format:
 * One integer
 *      i.    The dimensional number of the starting hub (int)
 * 
 * Step 4:
 * CollectedOutputFile name is passed in through the command line as args[3]
 * Output to CollectedOutputFile with the format:
 * 1. e Lines, listing the Name of the anomaly collected with the Spider who
 *    is at the same Dimension (if one exists, space separated) followed by 
 *    the Dimension number for each Dimension in the route (space separated)
 * 
 * @author Seth Kelley
 */

public class CollectAnomalies {

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

        if ( args.length < 4 ) {
            StdOut.println(
                "Execute: java -cp bin spiderman.CollectAnomalies <dimension INput file> <spiderverse INput file> <hub INput file> <collected OUTput file>");
                return;
        }
        Collider Col=new Collider();
        Col.Make_The_Goddamn_Hash_Table(args[0]);
        Col.Make_The_Dumbass_Adjacency_List();
        Col.Add_The_People_Into_The_Stoobid_Spiderverse(args[1]);
        StdOut.setFile(args[3]);
        StdIn.setFile(args[2]);
        List<Person> persons_at_hub=new ArrayList<Person>();
        int hub = StdIn.readInt();
        int idx=hub%Col.getAdj_List().length;
        Dim_Node hubNode=Col.getAdj_List()[idx];
        while (hubNode.getDimNumber()!=hub){
            hubNode=hubNode.getNext_Dim_Node();           
        }
        for(int i=0; i<Col.getAdj_List().length; i++){
            Dim_Node ptr=Col.getAdj_List()[i];
            while(ptr.getNext_Dim_Node().getNext_Dim_Node()!=null){
                if(ptr.getPeopleInDim()!=null&&ptr.getDimNumber()!=hub){
                    Person ptr2=ptr.getPeopleInDim();
                    while(ptr2!=null){
                        if(ptr2.getDim_origin()!=ptr2.getDim_Signature()){
                        List<Dim_Node> arr = Col.shortestPathBFS(hubNode, ptr);

                        StdOut.print(ptr2.getName()+" ");
                            if(!SpideExists(ptr)){
                                persons_at_hub.add(ptr2);
                                for(Dim_Node w:arr){
                                    StdOut.print(w.getDimNumber()+" ");
                                }
                                for(int a=arr.size()-2; a>=0;a--){
                                    StdOut.print(arr.get(a).getDimNumber()+" ");
                                }
                            }
                            else{
                                StdOut.print(Node_Spide_that_Exists_for_Some_Reason(ptr).getName()+" ");
                                persons_at_hub.add(Node_Spide_that_Exists_for_Some_Reason(ptr));
                                persons_at_hub.add(ptr2);
                                for(int a=arr.size()-1; a>=0;a--){
                                    StdOut.print(arr.get(a).getDimNumber()+" ");
                                }
                            }
                            StdOut.println();
                        }
                        ptr2=ptr2.getNext_Person();
                    }
                }
                ptr=ptr.getNext_Dim_Node();
            }
        }
        Col.setPeople_at_hub(persons_at_hub);
    }
}
