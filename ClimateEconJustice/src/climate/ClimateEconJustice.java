package climate;

import java.util.ArrayList;

/**
 * This class contains methods which perform various operations on a layered 
 * linked list structure that contains USA communitie's Climate and Economic information.
 * 
 * @author Navya Sharma
 */

public class ClimateEconJustice {

    private StateNode firstState;
    
    /*
    * Constructor
    * 
    * **** DO NOT EDIT *****
    */
    public ClimateEconJustice() {
        firstState = null;
    }

    /*
    * Get method to retrieve instance variable firstState
    * 
    * @return firstState
    * 
    * **** DO NOT EDIT *****
    */ 
    public StateNode getFirstState () {
        // DO NOT EDIT THIS CODE
        return firstState;
    }

    /**
     * Creates 3-layered linked structure consisting of state, county, 
     * and community objects by reading in CSV file provided.
     * 
     * @param inputFile, the file read from the Driver to be used for
     * @return void
     * 
     * **** DO NOT EDIT *****
     */
    public void createLinkedStructure ( String inputFile ) {
        
        // DO NOT EDIT THIS CODE
        StdIn.setFile(inputFile);
        StdIn.readLine();
        
        // Reads the file one line at a time
        while ( StdIn.hasNextLine() ) {
            // Reads a single line from input file
            String line = StdIn.readLine();
            // IMPLEMENT these methods
            addToStateLevel(line);
            addToCountyLevel(line);
            addToCommunityLevel(line);
        }
    }

    /*
    * Adds a state to the first level of the linked structure.
    * Do nothing if the state is already present in the structure.
    * 
    * @param inputLine a line from the input file
    */
    public void addToStateLevel ( String inputLine ) {

        String[] Arr = inputLine.split(",");
        StateNode new_node = new StateNode(Arr[2], null, null);
        if (firstState == null) {
            firstState = new_node;
        } else {
            StateNode ptr = firstState;
            StateNode previousPtr = ptr;
            while(ptr != null) {
                previousPtr = ptr;
                if (ptr.name.equals(Arr[2])){
                    return;
                }
                ptr = ptr.next;
            }
            previousPtr.next = new_node;
        }
    }

    /*
    * Adds a county to a state's list of counties.
    * 
    * Access the state's list of counties' using the down pointer from the State class.
    * Do nothing if the county is already present in the structure.
    * 
    * @param inputFile a line from the input file
    */
    public void addToCountyLevel ( String inputLine ) {

        String[] Arr = inputLine.split(",");
        CountyNode countyNode = new CountyNode(Arr[1], null, null);        
        for (StateNode SPtr= firstState; SPtr!=null; SPtr=SPtr.next){
            if(SPtr.name.equals(Arr[2])){
                if(SPtr.down==null){
                    SPtr.down=countyNode;
                    return;
                }
                else{
                    CountyNode CouPrev=null;
                    for(CountyNode CouPtr=SPtr.down; CouPtr!=null; CouPtr=CouPtr.next){
                        CouPrev=CouPtr;
                        if(CouPtr.name.equals(Arr[1])){
                            return;
                        }
                    }
                    CouPrev.next=countyNode;
                }
            }
        }


    }

    /*
    * Adds a community to a county's list of communities.
    * 
    * Access the county through its state
    *      - search for the state first, 
    *      - then search for the county.
    * Use the state name and the county name from the inputLine to search.
    * 
    * Access the state's list of counties using the down pointer from the StateNode class.
    * Access the county's list of communities using the down pointer from the CountyNode class.
    * Do nothing if the community is already present in the structure.
    * 
    * @param inputFile a line from the input file
    */
    public void addToCommunityLevel ( String inputLine ) {

        String[] Arr = inputLine.split(",");
        Data data= new Data(Double.parseDouble(Arr[3]),Double.parseDouble(Arr[4]),Double.parseDouble(Arr[5]),
        Double.parseDouble(Arr[8]),Double.parseDouble(Arr[9]),Arr[19],Double.parseDouble(Arr[49]),
        Double.parseDouble(Arr[37]),Double.parseDouble(Arr[121]));
        CommunityNode communityNode= new CommunityNode(Arr[0], null, data);
        for (StateNode SPtr= firstState; SPtr!=null; SPtr=SPtr.next){
            if(SPtr.name.equals(Arr[2])){
                for (CountyNode CouPtr= SPtr.down; CouPtr!=null; CouPtr=CouPtr.next){
                    if(CouPtr.name.equals(Arr[1])){
                        if(CouPtr.down==null){
                            CouPtr.down=communityNode;
                            return;
                        }
                        else{
                            CommunityNode ComPrev=null;
                            for(CommunityNode ComPtr=CouPtr.down; ComPtr!=null; ComPtr=ComPtr.next){
                                ComPrev=ComPtr;
                                if(ComPtr.name.equals(Arr[0])){
                                    return;
                                }
                            }
                            ComPrev.next=communityNode;
                        }
         
                    }
                }
            }
        }

    }

    /**
     * Given a certain percentage and racial group inputted by user, returns
     * the number of communities that have that said percentage  or more of racial group  
     * and are identified as disadvantaged
     * 
     * Percentages should be passed in as integers for this method.
     * 
     * @param userPrcntage the percentage which will be compared with the racial groups
     * @param race the race which will be returned
     * @return the amount of communities that contain the same or higher percentage of the given race
     */
    public int disadvantagedCommunities ( double userPrcntage, String race ) {
        int cntr=0;
        for (StateNode SPtr= firstState; SPtr!=null; SPtr=SPtr.next){
            for (CountyNode CouPtr= SPtr.down; CouPtr!=null; CouPtr=CouPtr.next){
                for (CommunityNode ComPtr=CouPtr.down; ComPtr!=null; ComPtr=ComPtr.next){
                    double prcnt=0;
                    if(race.equalsIgnoreCase("African American")){
                        prcnt=100*(ComPtr.info.getPrcntAfricanAmerican());
                    }
                    if(race.equalsIgnoreCase("Native American")){
                        prcnt=100*(ComPtr.info.getPrcntNative());
                    }
                    if(race.equalsIgnoreCase("Asian American")){
                        prcnt=100*(ComPtr.info.getPrcntAsian());
                    }
                    if(race.equalsIgnoreCase("White American")){
                        prcnt=100*(ComPtr.info.getPrcntWhite());
                    }
                    if(race.equalsIgnoreCase("Hispanic American")){
                        prcnt=100*(ComPtr.info.getPrcntHispanic());
                    }
                    if(prcnt>= userPrcntage && ComPtr.info.getAdvantageStatus().equalsIgnoreCase("True")){
                        cntr++;
                    }
                }
            }
        }
        return cntr;
    }

    /**
     * Given a certain percentage and racial group inputted by user, returns
     * the number of communities that have that said percentage or more of racial group  
     * and are identified as non disadvantaged
     * 
     * Percentages should be passed in as integers for this method.
     * 
     * @param userPrcntage the percentage which will be compared with the racial groups
     * @param race the race which will be returned
     * @return the amount of communities that contain the same or higher percentage of the given race
     */
    public int nonDisadvantagedCommunities ( double userPrcntage, String race ) {
        int cntr=0;
        for (StateNode SPtr= firstState; SPtr!=null; SPtr=SPtr.next){
            for (CountyNode CouPtr= SPtr.down; CouPtr!=null; CouPtr=CouPtr.next){
                for (CommunityNode ComPtr=CouPtr.down; ComPtr!=null; ComPtr=ComPtr.next){
                    double prcnt=0;
                    if(race.equalsIgnoreCase("African American")){
                        prcnt=100*(ComPtr.info.getPrcntAfricanAmerican());
                    }
                    if(race.equalsIgnoreCase("Native American")){
                        prcnt=100*(ComPtr.info.getPrcntNative());
                    }
                    if(race.equalsIgnoreCase("Asian American")){
                        prcnt=100*(ComPtr.info.getPrcntAsian());
                    }
                    if(race.equalsIgnoreCase("White American")){
                        prcnt=100*(ComPtr.info.getPrcntWhite());
                    }
                    if(race.equalsIgnoreCase("Hispanic American")){
                        prcnt=100*(ComPtr.info.getPrcntHispanic());
                    }
                    if(prcnt>= userPrcntage && ComPtr.info.getAdvantageStatus().equalsIgnoreCase("False")){
                        cntr++;
                    }
                }
            }
        }
        return cntr;
    }
    
    /** 
     * Returns a list of states that have a PM (particulate matter) level
     * equal to or higher than value inputted by user.
     * 
     * @param PMlevel the level of particulate matter
     * @return the States which have or exceed that level
     */ 
    public ArrayList<StateNode> statesPMLevels ( double PMlevel ) {
        ArrayList<StateNode> State = new ArrayList<StateNode>();
        for (StateNode SPtr= firstState; SPtr!=null; SPtr=SPtr.next){
            boolean repeatChecker = false;
            for (CountyNode CouPtr= SPtr.down; CouPtr!=null; CouPtr=CouPtr.next){
                for (CommunityNode ComPtr=CouPtr.down; ComPtr!=null; ComPtr=ComPtr.next){
                    if(ComPtr.info.getPMlevel() >= PMlevel){
                        repeatChecker=true;
                        break;
                    }
                }
                if(repeatChecker){
                State.add(SPtr);
                break;
                }
    
            }
        }

        return State;
    }

    /**
     * Given a percentage inputted by user, returns the number of communities 
     * that have a chance equal to or higher than said percentage of
     * experiencing a flood in the next 30 years.
     * 
     * @param userPercntage the percentage of interest/comparison
     * @return the amount of communities at risk of flooding
     */
    public int chanceOfFlood ( double userPercntage ) {
        int comCount=0;
        for (StateNode SPtr= firstState; SPtr!=null; SPtr=SPtr.next){
            for (CountyNode CouPtr= SPtr.down; CouPtr!=null; CouPtr=CouPtr.next){
                for (CommunityNode ComPtr=CouPtr.down; ComPtr!=null; ComPtr=ComPtr.next){
                    if(ComPtr.info.getChanceOfFlood()>=userPercntage){
                        comCount++;
                    }
                }
            }
        }

        return comCount;
    }

    /** 
     * Given a state inputted by user, returns the communities with 
     * the 10 lowest incomes within said state.
     * 
     *  @param stateName the State to be analyzed
     *  @return the top 10 lowest income communities in the State, with no particular order
    */
    public ArrayList<CommunityNode> lowestIncomeCommunities ( String stateName ) {
        ArrayList<CommunityNode> list= new ArrayList<CommunityNode>();
        for (StateNode SPtr= firstState; SPtr!=null; SPtr=SPtr.next){
            if(SPtr.name.equalsIgnoreCase( stateName)){
                for (CountyNode CouPtr= SPtr.down; CouPtr!=null; CouPtr=CouPtr.next){
                    for (CommunityNode ComPtr=CouPtr.down; ComPtr!=null; ComPtr=ComPtr.next){
                        if(list.size()<10){
                            list.add(ComPtr);
                        }
                        else if (list.size()==10){
                            int community=0;
                            double min= list.get(0).getInfo().getPercentPovertyLine();
                            for(int i=1; i<list.size(); i++){
                                double current=list.get(i).getInfo().getPercentPovertyLine();
                                if(current<min){
                                    min= current;
                                    community=i;
                                }
                            }
                                if(ComPtr.getInfo().getPercentPovertyLine()>min) list.set(community, ComPtr);
                        }
                    }
                }
            }
        }


        return list;
    }
}
