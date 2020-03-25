//import java.util.*;
//import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.FileNotFoundException;
import java.io.IOException;

public class DecodeHuffmanFile 
{
  
    public void decodeHuffmanFile(Huff bt, Huff.Node rootnode, String inputFileName)
    {
        FileReader encodedInputStream = null;
        FileWriter decodedOutputStream = null;

      try 
      {
          encodedInputStream = new FileReader(inputFileName);
          decodedOutputStream = new FileWriter("medTaleDecoded.txt");

          while (true) //outer while
          {
              Huff.Node t = rootnode;
              
              int c;
              c = encodedInputStream.read();
              if (c == -1)
              {
                // Yay.  We're done
                // close files...return
                  try
                  {
                      encodedInputStream.close();
                  }
                  catch (IOException e)
                  {
                      System.out.println("IOException error in 'DecodeHuffmanFile.java'");
                  }  
                  try
                  {
                      decodedOutputStream.close();
                  }
                  catch (IOException e)
                  {
                      System.out.println("IOException error in 'DecodeHuffmanFile.java'");
                  }  
              }
              else
              {
                  while (true)                                    // inner while
                  {
                      char ch = (char) c;
                      String inputS = Character.toString(ch);
                      int result_1 = inputS.compareTo("0");
                      int result_2 = inputS.compareTo("1");

                      if (result_1 == 0)
                      {
                          t = t.left;
                      }
                      else if (result_2 == 0)
                      {
                          t = t.right;
                      }
                      else
                      {
                          System.out.println("Error: character in encoded input file is neither");
                          System.out.println("a zero nor a one.  Probable corrupt encoded input file.");
                          return;            // avoid infinite loop
                      }
                      
                      if (t.left == null & t.right == null)    // leaf node?
                      {
                        decodedOutputStream.write(t.letter);
                        break;                                    // inner while
                      }
                      else
                      {
                          c = encodedInputStream.read();
                          if (c == -1)
                          {
                              System.out.println("Error: unexpected end of encoded input file");
                              System.out.println("We are not at end-leafnode, but no more characters");
                              System.out.println("in encoded input file.  Probable corrupt encoded");
                              System.out.println("input file");
                              return;
                          }
                      }
                  }    // inner while
              }
          }       // outer while           
      }    // try
      catch (FileNotFoundException e)
      {
          System.out.println("File not found error in 'DecodeHuffmanFile.java'");
      }
      catch (IOException e)
      {
          System.out.println("IOException error in 'DecodeHuffmanFile.java'");
      }
      finally 
      {
          if (encodedInputStream != null) 
          {
              try
              {
                  encodedInputStream.close();
              }
              catch (IOException e)
              {
                 System.out.println("IOException error in 'DecodeHuffmanFile.java'");
              }  
          }
          if (decodedOutputStream != null)
          {
              try
              {
                  decodedOutputStream.close();
              }
              catch (IOException e)
              {
                  System.out.println("IOException error in 'DecodeHuffmanFile.java'");
              }  
          }
      }
        
    }
}