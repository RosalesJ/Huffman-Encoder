/**
 * EECS 132
 * Harold Connamacher
 * Programming Project 5
 * Jacob Rosales Chase
 */

import java.util.Iterator;
import java.util.ListIterator;
import java.util.NoSuchElementException;

/**
 * A doubly linked linked list.
 * @since Java 8
 * @author Jacob Rosales Chase
 * @param <T> any object
 */
public class DoubleLinkedList<T> implements Iterable<T> {

    /**
     * a reference to the first node of the double linked list
     */
    private DLNode<T> front;
    /**
     * a reference to the last node of a double linked list
     */
    private DLNode<T> back;

    /**
     * Create an empty double linked list.
     */
    public DoubleLinkedList() {
        front = back = null;
    }

    public DoubleLinkedList(DoubleLinkedList<T> inList) {
        for (T item : inList) {
            this.addToBack(item);
        }
    }

    /**
     * Returns the reference to the first node of the linked list.
     *
     * @return the first node of the linked list
     */
    private DLNode<T> getFrontNode() {
        return front;
    }

    /**
     * Sets the first node of the linked list.
     *
     * @param node the node that will be the head of the linked list.
     */
    private void setFrontNode(DLNode<T> node) {
        front = node;
    }

    /**
     * Returns the reference to the last node of the linked list.
     *
     * @return the last node of the linked list
     */
    private DLNode<T> getBackNode() {
        return back;
    }

    /**
     * Sets the last node of the linked list.
     *
     * @param node the node that will be the last node of the linked list
     */
    private void setBackNode(DLNode<T> node) {
        back = node;
    }

    /**
     * Add an element to the head of the linked list.
     *
     * @param element the element to add to the front of the linked list
     */
    public final void addToFront(T element) {
        if (isEmpty()) {
            setFrontNode(new DLNode<>(element, null, null));
            setBackNode(getFrontNode());
        } else {
            setFrontNode(new DLNode<>(element, null, getFrontNode()));
        }
    }

    /**
     * Add an element to the tail of the linked list.
     *
     * @param element the element to add to the tail of the linked list
     */
    public final void addToBack(T element) {
        if (isEmpty()) {
            setFrontNode(new DLNode<>(element, null, null));
            setBackNode(getFrontNode());
        } else {
            setBackNode(new DLNode<>(element, getBackNode(), null));
        }
    }

    /**
     * gets the first element of the list
     *
     * @return the first element of the list
     */
    public T getFront() {
        if (this.getFrontNode() == null) {
            return null;
        }
        return this.getFrontNode().getElement();
    }

    /**
     * gets the last element of the list
     *
     * @return the last element of the list
     */
    public T getBack() {
        if (this.getBackNode() == null) {
            return null;
        }
        return this.getBackNode().getElement();
    }

    /**
     * Remove and return the element at the front of the linked list.
     *
     * @return the element that was at the front of the linked list
     * @throws NoSuchElementException if attempting to remove from an empty list
     */
    public T removeFromFront() {
        if (isEmpty()) {
            throw new NoSuchElementException();
        }

        T element = getFrontNode().getElement();

        if (getFrontNode() == getBackNode()) {
            setFrontNode(null);
            setBackNode(null);
            return element;
        }

        setFrontNode(getFrontNode().getNext());
        getFrontNode().getPrevious().setNext(null);
        getFrontNode().setPrevious(null);
        return element;
    }

    /**
     * Remove and return the element at the back of the linked list.
     *
     * @return the element that was at the back of the linked list
     * @throws NoSuchElementException if attempting to remove from an empty list
     */
    public T removeFromBack() {
        if (isEmpty()) {
            throw new NoSuchElementException();
        }

        T element = getBackNode().getElement();

        if (getFrontNode() == getBack()) {
            setFrontNode(null);
            setBackNode(null);
            return element;
        }

        setBackNode(getBackNode().getPrevious());
        getBackNode().getNext().setPrevious(null);
        getBackNode().setNext(null);

        return element;
    }

    /**
     * get the length of the list
     *
     * @return length the length of the list
     */
    public int length() {
        int length = 0;
        for (Iterator<T> iterator = this.iterator(); iterator.hasNext(); length++) {
            iterator.next();
        }
        return length;
    }

    /**
     * Duplicates the list
     *
     * @return a new linked list containing all the items of this list
     */
    public DoubleLinkedList<T> duplicate() {
        return new DoubleLinkedList<>(this);
    }

    /**
     * Reverses the list O(N)
     */
    public void reverse() {
        DLNode nodeptr = this.front;

        this.front = this.back;     // flip the pointers for front and back
        this.back = nodeptr;

        DLNode temp;
        while (nodeptr != null) {
            temp = nodeptr.getPrevious();
            nodeptr.setPrevious(nodeptr.getNext());
            nodeptr.setNext(temp);

            nodeptr = nodeptr.getPrevious();
        }
    }

    /**
     * Returns true if the list is empty.
     *
     * @return true if the list has no nodes
     */
    public boolean isEmpty() {
        return getFrontNode() == null;
    }

    /**
     * override the iterator method
     *
     * @return a new iterator for this list
     */
    @Override
    public Iterator<T> iterator() {
        return new LinkedListIterator<>(this);
    }

    /**
     * Insert the element in order into a list. Pre-requisite: the list is
     * already in sorted order
     *
     * @param <T>
     * @param list
     * @param element
     */
    public static <T extends Comparable<? super T>> void insertInOrder(DoubleLinkedList<T> list, T element) {
        /**
         * if the list is empty or element is smaller than first entry the
         * element goes in the front
         */
        if (list.isEmpty() || element.compareTo(list.getFront()) < 0) {
            list.addToFront(element);
        } else if (element.compareTo(list.getBack()) >= 0) {
            list.addToBack(element);
        } else {
            /* advances an iterator until in position to insert */
            ListIterator<T> it = (ListIterator) list.iterator();
            T ptr = ptr = it.next();
            for (; it.hasNext() && element.compareTo(ptr) > 0; ptr = it.next()); // empty loop
            it.previous();

            it.add(element); //inserts the element
        }
    }

    /**
     * gets the value in the node at a given index
     *
     * @param i the position to get
     * @return the element in the i position
     */
    public T get(int i) {
        if (i >= this.length()) {
            throw new IndexOutOfBoundsException();
        }
        for (T element : this) {
            if (i == 0) {
                return element;
            }
            i--;
        }
        return this.getBack();
    }

    /**
     * EECS 132 Lab 13
     *
     * @author Jacob Rosales Chase
     */
    /**
     * The node of a double linked list.
     *
     * @param <T>
     */
    private class DLNode<T> {

        /**
         * The element stored in the node.
         */
        private final T element;

        /**
         * A pointer to the next node in the list.
         */
        private DLNode<T> next;

        /**
         * A pointer to the previous node of the list.
         */
        private DLNode<T> previous;

        /**
         * The constructor. Creates a node and puts it into place in the double
         * linked list.
         *
         * @param element the element to be stored in the node
         * @param previous the node that will be before this node in the list,
         * or null if no node is before this one
         * @param next the node that will be after this node in the list, or
         * null of no node will be after this node
         */
        public DLNode(T element, DLNode<T> previous, DLNode<T> next) {
            this.element = element;
            this.next = next;
            this.previous = previous;
            if (next != null) {
                next.setPrevious(this);
            }
            if (previous != null) {
                previous.setNext(this);
            }
        }

        /**
         * Get the element stored in this node.
         *
         * @return the element stored in the node
         */
        public T getElement() {
            return element;
        }

        /**
         * Gets the node that is before this node in the list.
         *
         * @return a reference to the node that comes before this node in the
         * list
         */
        public DLNode<T> getPrevious() {
            return previous;
        }

        /**
         * Gets the node that is after this node in the list.
         *
         * @return a reference to the node that comes after this node in the
         * list
         */
        public DLNode<T> getNext() {
            return next;
        }

        /**
         * Sets the reference to the node that will be after this node in the
         * list.
         *
         * @param node a reference to the node that will be after this node in
         * the list
         */
        public void setNext(DLNode<T> node) {
            next = node;
        }

        /**
         * Sets the reference to the node that will be before this node in the
         * list.
         *
         * @param node a reference to the node that will be before this node in
         * the list
         */
        public void setPrevious(DLNode<T> node) {
            previous = node;
        }
    }

    /**
     * EECS 132 Lab 13
     *
     * @author Jacob Rosales Chase
     */
    /**
     * The ListIterator Class
     *
     * @param <T> any object
     */
    private class LinkedListIterator<T> implements ListIterator<T> {

        private int index;
        private final DoubleLinkedList<T> list;
        private DLNode<T> nodeptr;
        private DLNode<T> lastptr;

        /**
         * Constructor
         *
         * @param list the list to be iterated through
         */
        public LinkedListIterator(DoubleLinkedList<T> list) {
            index = 0;
            this.list = list;
            nodeptr = (DLNode<T>) list.getFrontNode();
        }

        /**
         * Returns the next element in the list and advances the cursor position
         *
         * @return the next element in the list
         */
        @Override
        public T next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            T element = nodeptr.getElement();
            lastptr = nodeptr;
            nodeptr = nodeptr.getNext();
            index++;
            return element;
        }

        /**
         * Returns the previous element in the list and moves the cursor
         * position backwards by one
         *
         * @return the previous element in the list
         */
        @Override
        public T previous() {
            if (!hasPrevious()) {
                throw new NoSuchElementException();
            }

            if (nodeptr == null && index > 0) {
                nodeptr = (DLNode<T>) list.getBackNode();
            } else {
                nodeptr = nodeptr.getPrevious();
            }
            lastptr = nodeptr;
            index--;
            return nodeptr.getElement();
        }

        /**
         * returns whether there is a next element when traversing the list
         * forwards
         *
         * @return true if there is a next element else false
         */
        @Override
        public boolean hasNext() {
            return nodeptr != null;
        }

        /**
         * returns whether there is a previous element when traversing the list
         * backwards
         *
         * @return true if there is a previous element else false
         */
        @Override
        public boolean hasPrevious() {
            return previousIndex() >= 0;
        }

        /**
         * Returns the index of the element that would be returned by a
         * subsequent call to next() returns -1 if the index doesn't exist
         *
         * @return index the index of the next element that would be returned by
         * next()
         */
        @Override
        public int nextIndex() {
            if (!hasNext()) {
                return -1;
            }
            return index;
        }

        /**
         * Returns the index of the element that would be returned by a
         * subsequent call to previous() if index doesn't exist returns -1
         *
         * @return index the index of the element that would be returned by
         * previous()
         */
        @Override
        public int previousIndex() {
            return index - 1;
        }

        /**
         * Adds an element directly after the last element returned by next
         *
         * @param element the element to be added
         */
        @Override
        public void add(T element) {
            if (!hasPrevious()) {
                list.addToFront(element);
            } else if (!hasNext()) {
                list.addToBack(element);
            } else {
                DLNode<T> node = new DLNode<>(element, nodeptr.getPrevious(), nodeptr);
                nodeptr.setPrevious(node);
                node.getPrevious().setNext(node);
            }
            index++;
        }

        /**
         * Removes from the list the last element that was returned by next() or
         * previous()
         */
        @Override
        public void remove() {
            if (lastptr == null) {
                throw new IllegalStateException();
            }

            if (lastptr == list.getBackNode()) {
                list.removeFromBack();
                nodeptr = null;
            } else if (lastptr == list.getFrontNode()) {
                list.removeFromFront();
            } else {
                lastptr.getNext().setPrevious(lastptr.getPrevious());
                lastptr.getPrevious().setNext(lastptr.getNext());
                nodeptr = lastptr.getNext();
            }
            index--;

            lastptr = null;
        }

        /**
         * Replaces the last element returned by next() or previous() with the
         * specified element
         *
         * @param element the element to be set
         */
        @Override
        public void set(T element) {
            if (lastptr == null) {
                throw new IllegalStateException();
            }

            if (lastptr.getPrevious() == null) {
                list.removeFromFront();
                list.addToFront(element);
            } else if (lastptr.getNext() == null) {
                list.removeFromBack();
                list.addToBack(element);
            } else {
                DLNode<T> node = new DLNode<>(element, lastptr.getPrevious(), lastptr.getNext());
                lastptr.getPrevious().setNext(node);
                lastptr.getNext().setPrevious(node);
            }
            lastptr = null;
        }
    }
}