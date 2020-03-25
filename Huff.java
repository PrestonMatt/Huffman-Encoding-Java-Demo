//import java.util.*;
import java.io.*;
import java.util.Map;
import java.util.Hashtable;
import java.util.PriorityQueue;
//import java.util.Iterator;
import java.util.List;
import java.util.ArrayList;
//import java.util.Arrays;

public class Huff
{
    private Map<String, Integer> freqTable = new Hashtable<String, Integer>();
    private Map<String, String> codeTable = new Hashtable<String, String>(); //The binary code should be represented as a string
    private PriorityQueue<Node> aQ = new PriorityQueue<Node>();
    private ArrayList<String> allChars = new ArrayList<String>();
    private static Node root;
    List<List<String>> allPathsToLeaves = new ArrayList<List<String>>();
    private static String textFileName = "medTale.txt";
    private static String binaryOutFileName = "huffCodingResults.txt";
    //private String[] alphabet = {"a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z", " "};

    public class Node implements Comparable
    {
        String letter; // the letter for this node;
        Integer freq;  // the letter's frequence
        Node left, right;
        
        //Costructor1
        public Node(String letter, Integer freq)
        {
            this.letter=letter;
            this.freq=freq;
        }
        
        //Constructor2
        public Node(String letter, Integer freq, Node left, Node right)
        {
            this.letter=letter;
            this.freq=freq;
            this.left=left;
            this.right=right;
        }
    
        // YOUR CODES HERE FOR IMPLEMENTING THE COMPARETO() METHOD, SO THAT NODES ARE COMPARABLE BY THEIR FREQUENCY.
        public int compareTo(Object o)
        {
            //Node o1 = (Node)o; Another way
            if(this.freq > ((Node)o).freq){ return 1; }
            else if(this.freq < ((Node)o).freq){ return -1; }
            //They are equal:    
            return 0; // FOR COMPILATION, RETURN 0. PLEASE MODIFY IT.
        }
    }
    
    //Constructor for Huff class
    public Huff(){}
    
    public static void main(String[] args) throws Exception
    {
        Huff aHuff = new Huff();
        aHuff.buildFreqTable(textFileName); // build the frequency table based upon input text file.
        aHuff.buildHuffTree(); // construct the Huffman tree.
        aHuff.getCodes(); // encode letters based on the Huffman tree constructed. 
        aHuff.outputBinaryFile(textFileName, binaryOutFileName); // output the encoding bitstream to an external file.
        
        DecodeHuffmanFile dHF = new DecodeHuffmanFile();
        dHF.decodeHuffmanFile(aHuff, root, "huffCodingResults.txt");
        System.out.println("Finished writing decodedOutputStream");
    }

    public void buildFreqTable(String fileName) throws Exception {
        /** READ THROUGH THE INPUT FILE, GET THE FREQUENCY OF EACH APPEARING LETTER, AND KEEP IT IN A HASHTABLE FREQTABLE. */
        FileReader fr;
        BufferedReader bf;
        try
        {
            fr = new FileReader(fileName);
            bf = new BufferedReader(fr);
            int value =0;
            while ((value = bf.read())!=-1)
            {
                char c = (char)value; // read letter one by one from a text file.
        
                // YOUR CODES HERE TO MAINTAIN THE FREQUENCY TABLE.
                
                //Change it into a string:
                String cStr = Character.toString(c);
                
                //If it's not in the table, just put it in as 1 freq:
                if(freqTable.get(cStr) == null)
                { freqTable.put(cStr, 1); 
                    allChars.add(cStr);
                  }
                
                //Else override value with a freq+=1:
                else
                {
                    int freq1 = freqTable.get(cStr);
                    freq1 += 1;
                    freqTable.put(cStr, freq1);
                }
            }
            bf.close();
        }
        catch (FileNotFoundException e1) 
        { System.out.println("The file does not exist!"); }
        catch (IOException e2)
        { System.out.println("Cannot open that file!"); }
    }
    
    public void buildHuffTree()
    {//YOUR CODES HERE TO CONSTRUCT A HUFFMAN TREE.
        //"create"  a priority queue: she did that as 'aQ'
        /*for(int x = 0; x < freqTable.size(); x++)
        {
            //System.out.println("Adding leaf node of: " + alphabet[x] + " with a freq of: " + freqTable.get(alphabet[x]));
            String thisChar = alphabet[x]; //add each character as a leaf node with it's freq
            aQ.add(new Node(thisChar, freqTable.get(thisChar), null, null)); //null null makes it a leaf node
            //System.out.println("Added!");
        }
     
        int nodeValInt = 0;
        while(aQ.size()>1)
        {
            nodeValInt++;
            String fakeLetter = "N" + Integer.toString(nodeValInt);
            Node t1 = aQ.remove();
            //System.out.print("Itereation " + nodeValInt + " t1: " + t1.letter + " " + t1.freq);
            Node t2 = aQ.remove();
            //System.out.println(", t2: " + t2.letter + " " + t2.freq);
            //This ^ is getting the smallest two nodes
            int fakeFreq = t1.freq + t2.freq;
            Node t = new Node(fakeLetter, fakeFreq, t1, t2);
            aQ.add(t);
        }
        root = aQ.remove();  */
        
//        Iterator<String> keys = freqTable.keySet().iterator();
//        while(keys.hasNext())
//        {
//            String key = keys.next();
//            this.aQ.add(new Node(key, freqTable.get(key)));
//        }
        
        //This maintians order (unlike iterator), which is key for the decoding
        for(int i = 0; i < allChars.size(); i++)
        { this.aQ.add(new Node(allChars.get(i), freqTable.get(allChars.get(i)))); }
        
        int nodeValInt = 0;
        while(aQ.size()>1)
        {
            nodeValInt++;
            String fakeLetter = "N" + Integer.toString(nodeValInt);
            Node t1 = aQ.remove();
            //System.out.print("Itereation " + nodeValInt + " t1: " + t1.letter + " " + t1.freq);
            Node t2 = aQ.remove();
            //System.out.println(", t2: " + t2.letter + " " + t2.freq);
            //This ^ is getting the smallest two nodes
            int fakeFreq = t1.freq + t2.freq;
            Node t = new Node(fakeLetter, fakeFreq, t1, t2);
            aQ.add(t);
        }
        root = aQ.remove();
        
    }
    
    public void getCodes()
    {
        /** YOUR CODES HERE. BASED ON THE HUFFMAN TREE, TRAVERSE EACH PATH AND GET THE SEQUENCE OF BITS FOR EACH LETTER. 
          * HINT: YOU CAN FOLLOW THE SIMILAR IDEA TO GET PATHSUM IN PS6.
          * A HELPER METHOD MAY BE NEEDED. KEEP THE LETTER AND ITS ASSOCIATED BIT SEQUENCE IN A HASHTABLE CODETABLE.
          * This is like finding all the paths whose "sum" or in this case "code" is equal ot the given key; I will need: codeTable
          */
        /*for(int x = 0; x < freqTable.size(); x++) //I could have used a foreach element in freqTable but w/e
        {
            String thisChar = alphabet[x];
            
            List<String> aPath = new ArrayList<String>();
            pathsToLeafs(root, aPath, thisChar);
            //OK so this^ gets all the paths to each character
            //printThePaths(allPathsToLeaves);
            
            //Since there's only one path it will be this one:
            List<String> pathX = allPathsToLeaves.get(x);
            
            String bitstreamForChar = translatePathToBiCode(root, pathX, thisChar);
            
            System.out.println(bitstreamForChar + " is the bitstream for character: " + "\"" + thisChar + "\"");
            
            //put the key, which is the characer, and the value is the bi string
            codeTable.put(thisChar, bitstreamForChar);
        }
        //printThePaths(allPathsToLeaves);*/
        getCodeHelper(root, "");
    }
    
    public void getCodeHelper(Node n, String s)
    {
        if(n.right == null && n.left == null) //leaf node
        { codeTable.put(n.letter, s); }
        else
        {
            if(n.left != null)
            { getCodeHelper(n.left, s + "0"); }
            if(n.right != null)
            { getCodeHelper(n.right, s + "1"); }
        }
    }
    
    /*public void pathsToLeafs(Node root, List<String> path, String searchLetter)
    {
        if(root == null){ return; }
        
        path.add(root.letter);
        
        if(root.left == null && root.right == null && root.letter.equals(searchLetter))
        { 
            List<String> newList = new ArrayList<String>();
            newList.addAll(path);
            allPathsToLeaves.add(newList); 
        }
        else
        {
            if(root.left != null)
            { pathsToLeafs(root.left, path, searchLetter); }
            if(root.right != null)
            { pathsToLeafs(root.right, path, searchLetter);}
        }
        path.remove(path.get(path.size()-1));
    }   
    
    public void printThePaths(List<List<String>> aListsToPrint)
    {
        //int times = 0;
        for(List<String> aLists: aListsToPrint)
        {
            //times++;
            //System.out.print("Instance: " + times +": ");
            for(String s: aLists)
            {
                System.out.print(s + ", ");
            }
            System.out.println();
        }
    }
    
    public String translatePathToBiCode(Node root, List<String> path, String findMe)
    {
        String bits = "";
        for(int x = 0; x < path.size(); x++)
        {
            path.remove(0); //b/c is starts at n26, the root node
            //System.out.println(path.get(0));
            
            if(root.left.letter.equals(path.get(0)))
            {
                bits += "0";
                root = root.left;
            }
            else if(root.right.letter.equals(path.get(0)))
            {
                bits += "1";
                root = root.right;
            }
        }
        return bits;
    }*/
    
    public void outputBinaryFile(String inFileName, String outFileName) throws Exception 
    {
        FileReader frIn = new FileReader(inFileName);
        BufferedReader bfIn = new BufferedReader(frIn);
        FileWriter frOut = new FileWriter(outFileName);
        BufferedWriter bfOut=new BufferedWriter(frOut);
        int value =0;
        while ((value = bfIn.read())!= -1)
        {
            char c = (char)value;
            // YOUR CODES HERE. RE-READ THE TEXT FILE, OUTPUT THE ENCODING RESULTS FOR EACH LETTER.
            
            //Change it into a string:
            String cStr = Character.toString(c);
            //Gets the 'string', really the bi-num to print back onto the file:
            String toPrint = codeTable.get(cStr);
            
            if(toPrint == null)
            { bfOut.write(" _!!! THE CHARACTER DOES NOT EXIST !!!_ "); /*This is for dealing with 'nonexistant' chars*/ }
            else { bfOut.write(toPrint); /*this should write it*/ }
            
        }
        bfIn.close();
        bfOut.flush();
        bfOut.close();
    }
}