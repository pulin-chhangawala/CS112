package forensic;

import org.w3c.dom.Node;

/**
 * This class represents a forensic analysis system that manages DNA data using
 * BSTs.
 * Contains methods to create, read, update, delete, and flag profiles.
 * 
 * @author Kal Pandit
 */
public class ForensicAnalysis {

    private TreeNode treeRoot;            // BST's root
    private String firstUnknownSequence;
    private String secondUnknownSequence;

    public ForensicAnalysis () {
        treeRoot = null;
        firstUnknownSequence = null;
        secondUnknownSequence = null;
    }

    /**
     * Builds a simplified forensic analysis database as a BST and populates unknown sequences.
     * The input file is formatted as follows:
     * 1. one line containing the number of people in the database, say p
     * 2. one line containing first unknown sequence
     * 3. one line containing second unknown sequence
     * 2. for each person (p), this method:
     * - reads the person's name
     * - calls buildSingleProfile to return a single profile.
     * - calls insertPerson on the profile built to insert into BST.
     *      Use the BST insertion algorithm from class to insert.
     * 
     * DO NOT EDIT this method, IMPLEMENT buildSingleProfile and insertPerson.
     * 
     * @param filename the name of the file to read from
     */
    public void buildTree(String filename) {
        // DO NOT EDIT THIS CODE
        StdIn.setFile(filename); // DO NOT remove this line

        // Reads unknown sequences
        String sequence1 = StdIn.readLine();
        firstUnknownSequence = sequence1;
        String sequence2 = StdIn.readLine();
        secondUnknownSequence = sequence2;
        
        int numberOfPeople = Integer.parseInt(StdIn.readLine()); 

        for (int i = 0; i < numberOfPeople; i++) {
            // Reads name, count of STRs
            String fname = StdIn.readString();
            String lname = StdIn.readString();
            String fullName = lname + ", " + fname;
            // Calls buildSingleProfile to create
            Profile profileToAdd = createSingleProfile();
            // Calls insertPerson on that profile: inserts a key-value pair (name, profile)
            insertPerson(fullName, profileToAdd);
        }
    }

    /** 
     * Reads ONE profile from input file and returns a new Profile.
     * Do not add a StdIn.setFile statement, that is done for you in buildTree.
    */
    public Profile createSingleProfile() {
        int S=StdIn.readInt();
        STR[] strArr= new STR[S];
        for(int i=0; i<S; i++){
            STR str= new STR(StdIn.readString(),StdIn.readInt());
            strArr[i]= str;
        }
        return new Profile(strArr);
    }

    /**
     * Inserts a node with a new (key, value) pair into
     * the binary search tree rooted at treeRoot.
     * 
     * Names are the keys, Profiles are the values.
     * USE the compareTo method on keys.
     * 
     * @param newProfile the profile to be inserted
     */
    public void insertPerson(String name, Profile newProfile) {
        TreeNode person =new TreeNode(name, newProfile, null, null);
        if(treeRoot==null){
            treeRoot=person;
            return;
        }
        TreeNode prev= treeRoot;
        TreeNode i=treeRoot;
        while (i!=null) {
            prev=i;
            if(i.getName().compareTo(name)<0) i=i.getRight();
            else i=i.getLeft();
        }
        if(prev.getName().compareTo(name)<0) prev.setRight(person);
        else prev.setLeft(person);

    }


    private int getMatchingProfileCountHelper(TreeNode node, boolean isOfInterest) {
        if (node == null) {
            return 0;
        }
    
        int leftCount = getMatchingProfileCountHelper(node.getLeft(), isOfInterest);
        int rightCount = getMatchingProfileCountHelper(node.getRight(), isOfInterest);
    
        Profile profile = node.getProfile();
        if (profile.getMarkedStatus() == isOfInterest) {
            return 1 + leftCount + rightCount;
        } else {
            return leftCount + rightCount;
        }
    }
    /**
     * Finds the number of profiles in the BST whose interest status matches
     * isOfInterest.
     *
     * @param isOfInterest the search mode: whether we are searching for unmarked or
     *                     marked profiles. true if yes, false otherwise
     * @return the number of profiles according to the search mode marked
     */
    public int getMatchingProfileCount(boolean isOfInterest) {
        return getMatchingProfileCountHelper(treeRoot, isOfInterest);
    }

    /**
     * Helper method that counts the # of STR occurrences in a sequence.
     * Provided method - DO NOT UPDATE.
     * 
     * @param sequence the sequence to search
     * @param STR      the STR to count occurrences of
     * @return the number of times STR appears in sequence
     */
    private int numberOfOccurrences(String sequence, String STR) {
        
        // DO NOT EDIT THIS CODE
        
        int repeats = 0;
        // STRs can't be greater than a sequence
        if (STR.length() > sequence.length())
            return 0;
        
            // indexOf returns the first index of STR in sequence, -1 if not found
        int lastOccurrence = sequence.indexOf(STR);
        
        while (lastOccurrence != -1) {
            repeats++;
            // Move start index beyond the last found occurrence
            lastOccurrence = sequence.indexOf(STR, lastOccurrence + STR.length());
        }
        return repeats;
    }


    private void flagProfilesOfInterestHelper(TreeNode node, String combinedSequences) {
        if (node == null) {
            return;
        }

        Profile profile = node.getProfile();
        int halfStrs = (profile.getStrs().length+1)/2;
        int totMatches=0;
        boolean isOfInterest = false;
        for (int i = 0; i < profile.getStrs().length; i++) {
            STR[] str = profile.getStrs();
            int occurrencesInProfile = str[i].getOccurrences();
            int occurrencesInCombinedSequences = numberOfOccurrences(combinedSequences, str[i].getStrString());
    
            if (occurrencesInProfile == occurrencesInCombinedSequences) {
                totMatches++;
            }
        }
        if(totMatches>=halfStrs){
            isOfInterest=true;
        }
        profile.setInterestStatus(isOfInterest);

        flagProfilesOfInterestHelper(node.getLeft(), combinedSequences);
        flagProfilesOfInterestHelper(node.getRight(), combinedSequences);
    
    }
    /**
     * Traverses the BST at treeRoot to mark profiles if:
     * - For each STR in profile STRs: at least half of STR occurrences match (round
     * UP)
     * - If occurrences THROUGHOUT DNA (first + second sequence combined) matches
     * occurrences, add a match
     */
    public void flagProfilesOfInterest() {
        flagProfilesOfInterestHelper(treeRoot, firstUnknownSequence+secondUnknownSequence);
    }


    /**
     * Uses a level-order traversal to populate an array of unmarked Strings representing unmarked people's names.
     * - USE the getMatchingProfileCount method to get the resulting array length.
     * - USE the provided Queue class to investigate a node and enqueue its
     * neighbors.
     * 
     * @return the array of unmarked people
     */
    public String[] getUnmarkedPeople() {
        int numUnmarkedProfiles = getMatchingProfileCount(false);

        String[] unmarkedPeople = new String[numUnmarkedProfiles];
        Queue<TreeNode> queue = new Queue<>();
    
        if (treeRoot != null) {
            queue.enqueue(treeRoot);
            int index = 0;
    
            while (!queue.isEmpty()) {
                TreeNode currentNode = queue.dequeue();
    
                if (!currentNode.getProfile().getMarkedStatus()) {
                    unmarkedPeople[index] = currentNode.getName();
                    index++;
                }
    
                // Enqueue the children of the current node
                if (currentNode.getLeft() != null) {
                    queue.enqueue(currentNode.getLeft());
                }
                if (currentNode.getRight() != null) {
                    queue.enqueue(currentNode.getRight());
                }
            }
        }
    
        return unmarkedPeople;
    }

    private TreeNode findMin(TreeNode node){
        while (node.getLeft()!=null) {
            node=node.getLeft();
        }
        return node;
    }

    
    private TreeNode deleteMin(TreeNode x){
        if (x.getLeft() == null) return x.getRight();
        x.setLeft(deleteMin(x.getLeft()));
        return x;
    }

        private TreeNode delete(TreeNode x, String key) {
            if (x == null) return null;
            int cmp = key.compareTo(x.getName());
            if (cmp < 0) x.setLeft(delete(x.getLeft(), key));
            else if (cmp > 0) x.setRight(delete(x.getRight(), key));
            else {
            if (x.getRight() == null) return x.getLeft();
            if (x.getLeft() == null) return x.getRight();
            TreeNode t = x;
            x = findMin(t.getRight());
            x.setRight(deleteMin(t.getRight()));
            x.setLeft(t.getLeft());
            }
            return x;
            }
    
    /**
     * Removes a SINGLE node from the BST rooted at treeRoot, given a full name (Last, First)
     * This is similar to the BST delete we have seen in class.
     * 
     * If a profile containing fullName doesn't exist, do nothing.
     * You may assume that all names are distinct.
     * 
     * @param fullName the full name of the person to delete
     */
    public void removePerson(String fullName) {
        treeRoot=delete(treeRoot,fullName);
    }


    /**
     * Clean up the tree by using previously written methods to remove unmarked
     * profiles.
     * Requires the use of getUnmarkedPeople and removePerson.
     */
    public void cleanupTree() {
        String[] N=getUnmarkedPeople();
        for(int i=0; i<N.length; i++) removePerson(N[i]);
    }

    /**
     * Gets the root of the binary search tree.
     *
     * @return The root of the binary search tree.
     */
    public TreeNode getTreeRoot() {
        return treeRoot;
    }

    /**
     * Sets the root of the binary search tree.
     *
     * @param newRoot The new root of the binary search tree.
     */
    public void setTreeRoot(TreeNode newRoot) {
        treeRoot = newRoot;
    }

    /**
     * Gets the first unknown sequence.
     * 
     * @return the first unknown sequence.
     */
    public String getFirstUnknownSequence() {
        return firstUnknownSequence;
    }

    /**
     * Sets the first unknown sequence.
     * 
     * @param newFirst the value to set.
     */
    public void setFirstUnknownSequence(String newFirst) {
        firstUnknownSequence = newFirst;
    }

    /**
     * Gets the second unknown sequence.
     * 
     * @return the second unknown sequence.
     */
    public String getSecondUnknownSequence() {
        return secondUnknownSequence;
    }

    /**
     * Sets the second unknown sequence.
     * 
     * @param newSecond the value to set.
     */
    public void setSecondUnknownSequence(String newSecond) {
        secondUnknownSequence = newSecond;
    }

}
