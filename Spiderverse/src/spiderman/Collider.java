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
 * ColliderOutputFile name is passed in through the command line as args[2]
 * Output to ColliderOutputFile with the format:
 * 1. e lines, each with a different dimension number, then listing
 *       all of the dimension numbers connected to that dimension (space separated)
 * 
 * @author Seth Kelley
 */

public class Collider {

    private Dim_Node[] Adj_List;
    private List<Person> People_at_hub;

    public Collider(){
        Adj_List=null;
        People_at_hub=null;
    }

    public Dim_Node[] getAdj_List(){
        return Adj_List;
    }
    public List<Person> getPeople_at_hub(){
        return People_at_hub;
    }
    public void setAdj_List(Dim_Node[] Adj_List){
        this.Adj_List=Adj_List;
    }
    public void setPeople_at_hub(List<Person> People_at_hub){
        this.People_at_hub=People_at_hub;
    }

    public void Make_The_Goddamn_Hash_Table(String args0){
        StdIn.setFile(args0);
        int[] arr=StdIn.readAllInts();
        Clusters cl= new Clusters();
        cl.setClusters(new Dim_Node[arr[1]]);
        cl.setTableSize(0);
        for(int i=3;i<((arr[0]+1)*3);i+=3){
            Dim_Node nodeImmaAdd= new Dim_Node(arr[i], arr[i+1], arr[i+2], null);
            cl.insert(nodeImmaAdd, arr);
        }
        cl.wrapItAround();
        Adj_List=cl.getClusters();
    }

    public void Make_The_Dumbass_Adjacency_List(){
        for(int i=0; i<Adj_List.length;i++){
            Dim_Node Gimme_Head= Adj_List[i], node=Adj_List[i].getNext_Dim_Node();
            Dim_Node[] Arr= new Dim_Node[node.Size(node)+2];
            int next_Idx=0;
            int length=Adj_List.length;
            while (node.getNext_Dim_Node().getNext_Dim_Node()!=null) {
                Arr[next_Idx]=node;
                Dim_Node[] This_Arr= {Gimme_Head};
                node.setAdj_Arr(This_Arr);
                node=node.getNext_Dim_Node();
                next_Idx++;
            }
            Arr[next_Idx]=Adj_List[(((i-1)%length)+length)%length];
            Arr[next_Idx+1]=Adj_List[(((i-2)%length)+length)%length];
            Arr[next_Idx+2]=Adj_List[(((i+1)%length)+length)%length];
            Arr[next_Idx+3]=Adj_List[(((i+2)%length)+length)%length];
            Gimme_Head.setAdj_Arr(Arr);
        }
    }

    public void Add_The_People_Into_The_Stoobid_Spiderverse(String args1){
        StdIn.setFile(args1);
        int d = StdIn.readInt();
        for(int i=0; i<d;i++){
            int Dim_No=StdIn.readInt();
            Dim_Node node=Adj_List[Dim_No%Adj_List.length];
            while(node!=null){
                if(node.getDimNumber()==Dim_No){
                    Person person= new Person(Dim_No, StdIn.readString(), StdIn.readInt(), node.getPeopleInDim());
                    node.setPeopleInDim(person);
                    break;
                }
                node=node.getNext_Dim_Node();
            }
        }
    }

    public List<Integer> Where_are_you_lil_spotty_boy(String file){
        StdIn.setFile(file);
        List<Integer> Spot_Was_Spotted_Here_BadumTss=new ArrayList<>();
        int a=StdIn.readInt();
        Dim_Node node=Adj_List[a%Adj_List.length];
        while(node!=null){
            if(node.getDimNumber()==a){
                break;
            }
            node=node.getNext_Dim_Node();
        }
        int Goal= StdIn.readInt();
        recursive_DFS_for_spotty_boy(node,Goal, Spot_Was_Spotted_Here_BadumTss);
        return Spot_Was_Spotted_Here_BadumTss;
    }

    private void recursive_DFS_for_spotty_boy(Dim_Node vertex, int goal, List<Integer> Spot_Was_Spotted_Here_BadumTss) {
        if (Spot_Was_Spotted_Here_BadumTss.size() > 65) return;
        
        for(int i=0; i<Spot_Was_Spotted_Here_BadumTss.size();i++){
            if(Spot_Was_Spotted_Here_BadumTss.get(i)==vertex.getDimNumber()) return;
        }
    
        if(Spot_Was_Spotted_Here_BadumTss.size()>1){
            if(Spot_Was_Spotted_Here_BadumTss.get(Spot_Was_Spotted_Here_BadumTss.size()-1)==goal)return;
        }
        Spot_Was_Spotted_Here_BadumTss.add(vertex.getDimNumber());


        for (int i = 0; i < vertex.getAdj_Arr().length; i++) {
            Dim_Node node = vertex.getAdj_Arr()[i];
            recursive_DFS_for_spotty_boy(node, goal, Spot_Was_Spotted_Here_BadumTss);
            if(Spot_Was_Spotted_Here_BadumTss.size()>2){
                if(Spot_Was_Spotted_Here_BadumTss.get(Spot_Was_Spotted_Here_BadumTss.size()-2)==goal)return;
            }
        }
    }

    public List<Dim_Node> shortestPathBFS(Dim_Node startNode, Dim_Node endNode) {
        Queue<Dim_Node> queue = new LinkedList<>();
        Map<Dim_Node, Dim_Node> prev = new HashMap<>();
        Set<Dim_Node> visited = new HashSet<>();

        queue.add(startNode);
        visited.add(startNode);
        prev.put(startNode, null);

        while (!queue.isEmpty()) {
            Dim_Node currentNode = queue.poll();
            if (currentNode.equals(endNode)) {
                break;
            }

            for (Dim_Node neighbor : currentNode.getAdj_Arr()) {
                if (!visited.contains(neighbor)) {
                    queue.add(neighbor);
                    visited.add(neighbor);
                    prev.put(neighbor, currentNode);
                }
            }
        }

        List<Dim_Node> path = new ArrayList<>();
        for (Dim_Node at = endNode; at != null; at = prev.get(at)) {
            path.add(at);
        }
        Collections.reverse(path);

        if (!path.isEmpty() && path.get(0).equals(startNode)) {
            return path;
        }

        return null;
    }

    public List<Dim_Node> djikstra(Dim_Node startNode, Dim_Node endNode) {
        Set<Dim_Node> visited = new HashSet<>();
        Map<Dim_Node, Dim_Node> prev = new HashMap<>();
        Map<Dim_Node,Integer> dist = new HashMap<>();
        PriorityQueue<Dim_Node> queue = new PriorityQueue<>((a, b) -> dist.get(a) - dist.get(b));

        dist.put(startNode,0);
        for(int i=0; i<Adj_List.length; i++){
            Dim_Node node=Adj_List[i];
            while(node!=null){
                if(node.getDimNumber()!=startNode.getDimNumber()){
                    dist.put(node, Integer.MAX_VALUE);
                }
                node=node.getNext_Dim_Node();
            }
        }
        queue.add(startNode);
        visited.add(startNode);
        prev.put(startNode, null);

        while (!queue.isEmpty()) {
            Dim_Node currentNode = queue.poll();
            visited.add(currentNode);

            
            for (Dim_Node neighbor : currentNode.getAdj_Arr()) {
                if (!visited.contains(neighbor)) {
                    if (dist.containsKey(currentNode) && dist.containsKey(neighbor)) {
                        int currentDist = dist.get(currentNode);
                        int neighborDist = dist.get(neighbor);
                        int weight = currentDist + neighbor.getDimWeight();
                        if (weight < neighborDist) {
                            dist.put(neighbor, weight);
                            prev.put(neighbor, currentNode);
                            queue.add(neighbor);
                        }
                    }

                }
            }
        }
    
        List<Dim_Node> path = new ArrayList<>();
        for (Dim_Node at = endNode; at!= null; at = prev.get(at)) {
            path.add(at);
        }
        Collections.reverse(path);
    
        return path;    
    }

    public static void main(String[] args) {

        if ( args.length < 3 ) {
            StdOut.println(
                "Execute: java -cp bin spiderman.Collider <dimension INput file> <spiderverse INput file> <collider OUTput file>");
                return;
        }
        Collider Col=new Collider();
        Col.Make_The_Goddamn_Hash_Table(args[0]);
        Col.Make_The_Dumbass_Adjacency_List();
        Col.Add_The_People_Into_The_Stoobid_Spiderverse(args[1]);
        StdOut.setFile(args[2]);
        for(int i=0; i<Col.Adj_List.length; i++){
            Dim_Node ptr=Col.Adj_List[i];
            while(ptr.getNext_Dim_Node().getNext_Dim_Node()!=null){
                StdOut.print(ptr.getDimNumber()+ " ");
                for(Dim_Node node: ptr.getAdj_Arr()) StdOut.print(node.getDimNumber()+" ");
                StdOut.println();
                ptr=ptr.getNext_Dim_Node();
            }
         } 
    }
}