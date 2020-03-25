People who helped me: 
	-T.A.: Bijwadia (during T.A. hours)
	-Prof.: Yang (during office hours)
	-Roger Preston (Father & retired Software Engineer; explained some of the concepts for me)

There were a few challenges I found.  The first is that of how the text file downloaded, and this is something that I am afraid of in grading honestly.  My text file downloaded strangely, and and not as the way that she has it in canvas.  I had to fix it a little bit to be like her's.  The text file my code works with will be down below (I will also provide a screenshot), because the new paragraphs mess everyting up.  I have put failsafes in to help allieviate that (in the outputBinaryFile method) if it downloads onto the TA's computer messed up as well.  Here's my code to help negate and not crash with unknown characters (it starts at the "YOUR CODES HERE" comment): 

while ((value = bfIn.read())!= -1)
	{
		char c = (char)value;
            	// YOUR CODES HERE. RE-READ THE TEXT FILE, OUTPUT THE ENCODING RESULTS FOR EACH LETTER.   
		//System.out.print(c + " ");

		//Change it into a string:
		String cStr = Character.toString(c);
		//Gets the 'string', really the bi-num to print back onto the file:
		String toPrint = codeTable.get(cStr);

		//System.out.print("is: \"" + toPrint + "\"" + " ");
		if(toPrint == null)
		{	
			System.out.println("ERROR");
			bfOut.write(" _!!! THE CHARACTER DOES NOT EXIST !!!_ "); 
		} //This should never occur, yet it does?
		//The bug was a translation from downloading the text file itself.  Remove all instances of new paragraphs and other 'unknown' characters and it goes away
		
		else { bfOut.write(toPrint); /*this should write it*/ }
        }

So the part that is asking if toPrint is null, is asking, does this character exist? I only used the alphabet and spaces beacuse that's the only characters in medText.txt, but sometimes in downloading, extra characters get added because of decoding issues in the computer itself (not my extra credit).  That actually happened to me.  This was the first big problem for me, was dealing with these dumb characters that shouldn't even be there.  But it's not a code/program crashing bug (hopefully).

The next challenge for me was the recursive algorythm for finding the huffman binary tree bitstream for each character.  I know that it's trying to find a specific path to a leaf node, but I am not quite sure yet how to get to it.  Initially I used a random bitstream that I just typed in ("0110101") for every character just to test the rest of my code.  This was my first attempt at the algorythm:

public String biCode(Node root1, String letter1, int freq1)
	{	//figure this out here how to map each value to the character: involes souting table paths
		//REMEMBER: GOING LEFT IS 0, AND RIGHT IS 1
		if(root1.left == null && root1.right == null)
		{
			//if(letter1.equals(root1.letter))
			//{
				return "";
			//}
			//else { return "ERROR"; }
		}
		else
		{
			if(root1.left != null && root1.right != null) //has 2 childs
			{
				if(freq1 < root1.left.freq) //go right
				{ return "1" + biCode(root1.right, letter1, freq1); }
				else// if(freq1 > root1.left.freq) //go left b/c if it's equal still go left
				{ return "0" + biCode(root1.left, letter1, freq1); }
			}
			else if(root1.left != null && root1.right == null) //has left child
			{ return "0" + biCode(root1.left, letter1, freq1); }
			else //has right child
			{ return "1" + biCode(root1.right, letter1, freq1); }
		}
		//return "";
	}

So far it only makes every letter encode to "1" which means that the first "if" statement is always being accepted as true?

1:32 PM 5/6/2017
Updated: I have finally made it encode, althought it encodes to completely wrong codes.  I think it's a problem with my method that finds the paths from root to leaf nodes.  What I do is just find the paths, and compress/store them temporarily in an arraylist of the strings that lead to the leaf node.  For example, a path to "a" could be N26, N24, ... N1, "a" (it probably isn't this in reality).  Then, once I have the path I go down it, if the right node is the next 'letter' then I make a 1, and vice versa to create the code. This doesn't work yet. Here's the codes:

public void getCodes()
{
	/** YOUR CODES HERE. BASED ON THE HUFFMAN TREE, TRAVERSE EACH PATH AND GET THE SEQUENCE OF BITS FOR EACH LETTER. 
          * HINT: YOU CAN FOLLOW THE SIMILAR IDEA TO GET PATHSUM IN PS6.
          * A HELPER METHOD MAY BE NEEDED. KEEP THE LETTER AND ITS ASSOCIATED BIT SEQUENCE IN A HASHTABLE CODETABLE.
          * This is like finding all the paths whose "sum" or in this case "code" is equal ot the given key; I will need: codeTable
          */
        for(int x = 0; x < freqTable.size(); x++) //I could have used a foreach element in freqTable but w/e
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
        //printThePaths(allPathsToLeaves);
}

public void pathsToLeafs(Node root, List<String> path, String searchLetter)
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
}

I am not sure why none of this works.  Am I constructing my tree and frequency correctly?
Turns out I wasn't.  I fixed it to work now.

//------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------

medTale:

it was the best of times it was the worst of times it was the age of wisdom it was the age of foolishness it was the epoch of belief it was the epoch of incredulity it was the season of light it was the season of darkness it was the spring of hope it was the winter of despair we had everything before us we had nothing before us we were all going direct to heaven we were all going direct the other wayin short the period was so far like the present period that some of its noisiest authorities insisted on its being received for good or for evil in the superlative degree of comparison only there were a king with a large jaw and a queen with a plain face on the throne of england there were a king with a large jaw and a queen with a fair face on the throne of france  in both countries it was clearer than crystal to the lords of the state preserves of loaves and fishes that things in general were settled for ever it was the year of our lord one thousand seven hundred and seventyfive  spiritual revelations were conceded to england at that favoured period as at this  mrs southcott had recently attained her fiveandtwentieth blessed birthday of whom a prophetic private in the life guards had heralded the sublime appearance by announcing that arrangements were made for the swallowing up of london and westminster  even the cocklane ghost had been laid only a round dozen of years after rapping out its messages as the spirits of this very year last past supernaturally deficient in originality rapped out theirs mere messages in the earthly order of events had lately come to the english crown and people from a congress of british subjects in america  which strange to relate have proved more important to the human race than any communications yet received through any of the chickens of the cocklane brood france less favoured on the whole as to matters spiritual than her sister of the shield and trident rolled with exceeding smoothness down hill making paper money and spending it under the guidance of her christian pastors she entertained herself besides with such humane achievements as sentencing a youth to have his hands cut off his tongue torn out with pincers and his body burned alive because he had not kneeled down in the rain to do honour to a dirty procession of monks which passed within his view at a distance of some fifty or sixty yards  it is likely enough that rooted in the woods of france and norway there were growing trees when that sufferer was put to death already marked by the woodman fate to come down and be sawn into boards to make a certain movable framework with a sack and a knife in it terrible in history  it is likely enough that in the rough outhouses of some tillers of the heavy lands adjacent to paris there were sheltered from the weather that very day rude carts bespattered with rustic mire snuffed about by pigs and roosted in by poultry which the farmer death had already set apart to be his tumbrils of the revolution but that woodman and that farmer though they work unceasingly work silently and no one heard them as they went about with muffled tread  the rather forasmuch as to entertain any suspicion that they were awake was to be atheistical and traitorous in england there was scarcely an amount of order and protection to justify much national boasting  daring burglaries by armed men and highway robberies took place in the capital itself every night families were publicly cautioned not to go out of town without removing their furniture to upholsterers warehouses for security the highwayman in the dark was a city tradesman in the light and being recognised and challenged by his fellow tradesman whom he stopped in his character of the captain gallantly shot him through the head and rode away the mail was waylaid by seven robbers and the guard shot three dead and then got shot dead himself by the other four in consequence of the failure of his ammunition after which the mail was robbed in peace that magnificent potentate the lord mayor of london was made to stand and deliver on turnham green by one highwayman who despoiled the illustrious creature in sight of all his retinue prisoners in london gaols fought battles with their turnkeys and the majesty of the law fired blunderbusses in among them loaded with rounds of shot and ball thieves snipped off diamond crosses from the necks of noble lords at court drawingrooms musketeers went into st giless to search for contraband goods and the mob fired on the musketeers and the musketeers fired on the mob and nobody thought any of these occurrences much out of the common way  in the midst of them the hangman ever busy and ever worse than useless was in constant requisition now stringing up long rows of miscellaneous criminals now hanging a housebreaker on saturday who had been taken on tuesday now burning people in the hand at newgate by the dozen and now burning pamphlets at the door of westminster hall today taking the life of an atrocious murderer and tomorrow of a wretched pilferer who had robbed a farmers boy of sixpence all these things and a thousand like them came to pass in and close upon the dear old year one thousand seven hundred and seventyfive  environed by them while the woodman and the farmer worked unheeded those two of the large jaws and those other two of the plain and the fair faces trod with stir enough and carried their divine rights with a high hand  thus did the year one thousand seven hundred and seventyfive conduct their greatnesses and myriads of small creaturesthe creatures of this chronicle among the restalong the roads that lay before them