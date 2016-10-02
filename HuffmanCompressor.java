/**
 * EECS 233
 * Richard Kolacinski
 * Programming Project 2
 * Jacob Rosales Chase
 */

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ListIterator;
/**
 * Compresses a text file using Huffman encoding
 * Note this is not full huffman compresson
 * Writes to the output file 0s and 1s as Strings and not straight binary
 *
 *  *** Currently does not include a decompressor ***
 * 
 * @author Jacob Rosales Chase
 * @since Java 8
 */
public class HuffmanCompressor {

    /**
     * Compresses an input file using Huffman encoding
     * 
     * expects:
     * java HuffmanCompressor inputFile outputFile
     * 
     * @param args the command line arguments
     */
    public static void main(String[] args){
        if(args.length != 2){
            System.out.println("Bad Arguments - expected:");
            System.out.println("java HuffmanCompressor inputFile outputFile");
            return;
        }
    String inputFile = args[0], outputFile =  args[1];
    String finalstate = huffmanCoder(inputFile, outputFile);
    
    System.out.println(finalstate);
    }
    
    
    /**
     * Takes the path to a text file and compresses the file using a Huffman encoding process
     * Prints the encoding table, total characters, uncompressed length, 
     * compressed length, saved space, and percent original size
     * 
     * @param inputFileName     the file to be compressed
     * @param outputFileName    the file in which to store the compression 
     *                          (if the file doesn't exist one will be created)
     * @return                  the status of the finalized compression 
     */
    public static String huffmanCoder(String inputFileName, String outputFileName){
        int uncompressedLength, compressedLength, percentCompressed, charcount = 0;
        DoubleLinkedList<HuffmanNode<Character>> encodingTable;   // the huffman encoding table
        
        /* encoding the file */
        try{ 
           encodingTable = buildEncodingTable(inputFileName);
           compressedLength = encodeFile(inputFileName, outputFileName, encodingTable);
        } catch(FileNotFoundException ex){
            return inputFileName + " not found";
        } catch (IOException ex) {
            return "Input file error";
        }
        
        /* calculating space saved */
        for(HuffmanNode h : encodingTable){
            System.out.println(h);
            charcount += h.getFrequency();
        }
        uncompressedLength = charcount * 8;
        percentCompressed  = (int)(((double)compressedLength / (double)uncompressedLength) * 100);
        
        /* compiling information to return */
        
        StringBuilder sb = new StringBuilder();
        
        sb.append("Total characters: " + charcount + '\n');
        sb.append("------------------" + '\n');
        sb.append("Uncompressed length: " + uncompressedLength + " bits" + '\n');
        sb.append("Compressed length: " + compressedLength + " bits" + '\n');
        sb.append("Saved space: " + (uncompressedLength - compressedLength) + " bits" + '\n');
        sb.append("Percent original size: " + percentCompressed + "%" + '\n');
        sb.append("OK" + '\n');
        
        return sb.toString();
    }
    

    /**
     * Encodes a given input file onto an output file given an encoding table
     * 
     * @param inputPath                      the path of the input file
     * @param outputPath                     the path of the output file
     * @param encodingTable                  the encoding table
     * @return                               the size of the compressed file
     * @throws FileNotFoundException         if the input file isn't found
     * @throws IOException                   if at any point it runs into IO error
     */
    private static int encodeFile(String inputPath, String outputPath, DoubleLinkedList<HuffmanNode<Character>> encodingTable) throws FileNotFoundException, IOException{
        FileReader inFile = new FileReader(inputPath);     // the input file
        
        File out = new File(outputPath);                   // delete contents of outfile (if any)
        out.delete();
        
        FileWriter outFile = new FileWriter(out);          // the output file
        
        int compressedLength = 0, charptr = inFile.read();
        String charEncoding;
        
        /* write each character of the input file to the output file */
        while(charptr != -1){
            charEncoding = lookup((char)charptr, encodingTable);
            outFile.write(charEncoding);
            compressedLength += charEncoding.length();
            
            charptr = inFile.read();
        }
        
        inFile.close();
        outFile.close();
        
        return compressedLength;
    }
    
    
    /**
     * Generates a Huffman encoding table given a text file
     * I chose a Double linked list implementation because 
     * I wrote the linked list, and it has duplicate an remove methods
     * also I'm more familiar with linked lists
     * 
     * @param inputFile      the path to the input file
     * @return               the encoding table in the form of a DoubleLinkedList of HuffmanNodes
     * @throws IOException   if at any point it encounters an IO error
     */
    private static DoubleLinkedList<HuffmanNode<Character>> buildEncodingTable(String inputFile) throws IOException{
        DoubleLinkedList<HuffmanNode<Character>> freqTable     = buildFrequencyTable(inputFile);
        /* 
        BuildHuffmanTree dismantles the linked list in building the tree 
        The list is duplicated so we have some linked list still containing
        reverences to all of the individual nodes in order
        */
        DoubleLinkedList<HuffmanNode<Character>> encodingTable = freqTable.duplicate();
        
        buildHuffmanTree(freqTable);
        assignEncoding(freqTable.getFront(), new StringBuilder());
        
        encodingTable.reverse();        // the list is in decreasing order so we reverse it
        return encodingTable;
    }
    
    
    /**
     * Takes the path of a file and returns an linked list of characters stored 
     * in Huffman nodes ordered from least to most frequent
     * 
     * @param fileName                      the path of the file to be read
     * @return                              the list of characters in Huffman nodes
     * @throws FileNotFoundException        if the file isn't found
     * @throws IOException                  if if runs into an IO error
     */
    private static DoubleLinkedList<HuffmanNode<Character>> buildFrequencyTable(String fileName) throws FileNotFoundException, IOException{
        DoubleLinkedList<HuffmanNode<Character>> huffList = new DoubleLinkedList<>();
        
        FileReader file = new FileReader(fileName);
        int charptr = file.read();
        HuffmanNode<Character> nodeptr;

        /** iterate through every character in the file **/
        while(charptr != -1){
            /** 
             * search for the character in the list
             * if you find it increment its count and update its position in huffList
             * else add it to the back of the list
             */
            for (ListIterator<HuffmanNode<Character>> it = (ListIterator)huffList.iterator(); it.hasNext() && charptr != -1;) {
                HuffmanNode<Character> huff = it.next();

                /** if you find the character in the list */
                if(huff.getInChar() == charptr){
                    huff.setFrequency(huff.getFrequency() + 1);      // increase its count

                    /** update its position **/
                    it.remove();
                    if(it.hasPrevious()){
                        nodeptr =  it.previous();
                        while(it.hasPrevious() && huff.compareTo(nodeptr) > 0)
                            nodeptr = it.previous();

                        if(huff.compareTo(nodeptr) <= 0)
                            it.next();
                    }
                    it.add(huff);

                    charptr = -1; // a cheap break mechanism
                }
            }
            /** if you don't find the character in the list */
            if(charptr != -1)
                huffList.addToBack(new HuffmanNode((char)charptr, 1));   // add it to the back

            charptr = file.read();                                     // in either case advance the read pointer
        }
        huffList.reverse();
        return huffList;
    }
    
    
    /**
     * Builds a Huffman encoding tree given an ordered DoubleLinkedList of HuffmanNodes
     * dismantles the linked list in the process
     * 
     * @param list                   a linked list of Huffman nodes containing characters in order from least to most frequent
     * @return                       the top Huffman node of the tree
     */
    private static HuffmanNode<Character> buildHuffmanTree(DoubleLinkedList<HuffmanNode<Character>> list){
        if(list.isEmpty())
            return new HuffmanNode<>(null, 0);
        
        HuffmanNode<Character> left;
        HuffmanNode<Character> right;
        
        while(list.length() != 1){
            left = list.removeFromFront();
            right = list.removeFromFront();
            
            DoubleLinkedList.insertInOrder(list, new HuffmanNode<>(left, right, (right.getFrequency() + left.getFrequency()) ));
        }
        
        return list.getFront();
    }
    
    
    /**
     * Assigns encoding for each leaf of a HuffmanTree
     * 
     * @param root         the root you're adding the encoding to
     * @param encoding     a StringBuilder containing the encoding so far
     */
    private static void assignEncoding(HuffmanNode<Character> root, StringBuilder encoding){
        root.setEncoding(encoding.toString());
        
        StringBuilder left = new StringBuilder(encoding).append(0);
        StringBuilder right = new StringBuilder(encoding).append(1);
        
        if(root.getLeft() != null) assignEncoding(root.getLeft(), left);
        if(root.getRight() != null) assignEncoding(root.getRight(), right);
    }
    
    
    /**
     * A quick helper method that searches a given encoding table for
     * a given char value and returns its encoding value if it's found
     * 
     * @param c                  the character being searched in the table
     * @param encodingTable      the table to be searched
     * @return                   the character's encoding if found else null
     */
    private static String lookup(char c, DoubleLinkedList<HuffmanNode<Character>> encodingTable){
        for(HuffmanNode n : encodingTable)
            if(n.getInChar().equals(c))
                return n.getEncoding();
        
        return null;
    }
}
