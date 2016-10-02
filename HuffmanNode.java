/**
 * EECS 233
 * Richard Kolacinski
 * Programming Project 2
 * Jacob Rosales Chase
 */

/**
 * A generic representation of a single node of a Huffman tree.
 * 
 * @author Jacob Rosales Chase
 * @since Java 8
 * @param <T> the object type being stored in the node
 */
public class HuffmanNode<T> implements Comparable<HuffmanNode<T>> {
    private T inChar;                    // the character stored in the node
    private String encoding;            // the encoding of the character
    private int frequency;             // the frequency of occurence of the character
    private HuffmanNode<T> right;     // the right child of this node
    private HuffmanNode<T> left;     // the left child of this node

    /**
     * Instantiates a new HuffmanNode with a given data object
     * @param data        the piece of data to be stored in the node
     * @param frequency   the frequency of occurrence of the piece of the data
     */
    public HuffmanNode(T data, int frequency){
        this.inChar = data;
        this.frequency = frequency;
    }
    
    /**
     * This constructor would be used while constructing the actual Huffman tree
     * hence the decision to not implement the option of selecting the character
     * since the inner nodes don't need them
     * @param right           the node's right node
     * @param left            the node's left node
     * @param frequency       the frequency of the node's element
     */
    public HuffmanNode(HuffmanNode<T> right, HuffmanNode<T> left, int frequency){
       this.right = right;
       this.left  = left;
       this.frequency = frequency;
    }
    
    /**
     * @return the inChar
     */
    public T getInChar() {
        return inChar;
    }

    /**
     * @param inChar the inChar to set
     */
    public void setInChar(T inChar) {
        this.inChar = inChar;
    }

    /**
     * @return the frequency
     */
    public int getFrequency() {
        return frequency;
    }

    /**
     * @param frequency the frequency to set
     */
    public void setFrequency(int frequency) {
        this.frequency = frequency;
    }

    /**
     * @return the right
     */
    public HuffmanNode getRight() {
        return right;
    }

    /**
     * @param right the right to set
     */
    public void setRight(HuffmanNode right) {
        this.right = right;
    }

    /**
     * @return the left
     */
    public HuffmanNode getLeft() {
        return left;
    }

    /**
     * @param left the left to set
     */
    public void setLeft(HuffmanNode left) {
        this.left = left;
    }
    /**
     * Override the compareTo Method
     * @return a negative integer if o's frequency is greater than this's frequency
     *         a positive integer if this's frequecy is greater than o's frequency
     *         0 if the frequency of this and o are equal
     */
    @Override
    public int compareTo(HuffmanNode<T> o) {
        return this.getFrequency() - o.getFrequency();
    }
    
    /**
     * Represent the node as a string
     * @return   the string representation of the node
     */
    @Override
    public String toString(){
        return (getInChar() + ":" + getFrequency() + ":" + getEncoding());
    }

    /**
     * @return the encoding
     */
    public String getEncoding() {
        return encoding;
    }

    /**
     * @param encoding the encoding to set
     */
    public void setEncoding(String encoding) {
        this.encoding = encoding;
    }
}
