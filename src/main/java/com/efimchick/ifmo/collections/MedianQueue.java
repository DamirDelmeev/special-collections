package com.efimchick.ifmo.collections;

import java.util.*;

class MedianQueue<E> extends LinkedList<E> implements Iterable<E>, Queue<E> {
    NodeForQueue<E> firstNode;
    NodeForQueue<E> lastNode;
    int size = 0;

    public MedianQueue() {
        this.firstNode = new NodeForQueue<>(null, null, null);
        this.lastNode = new NodeForQueue<>(firstNode, null, null);
        this.firstNode = new NodeForQueue<>(null, null, lastNode);
    }

    private E getElementByIndex(int counter) {
        NodeForQueue<E> target = firstNode.getNext();
        for (int i = 0; i < counter; i++) {
            target = getNextElement(target);
        }
        return target.getItem();
    }

    public NodeForQueue<E> getNextElement(NodeForQueue<E> current) {
        return current.getNext();
    }

    private E findMedian() {
        ArrayList<Integer> list = new ArrayList<>();
        for (E i : this) {
            list.add((Integer) i);
        }
        Collections.sort(list);
        if (list.size() % 2 != 0) {
            return (E) list.get(list.size() / 2);
        } else {
            Integer element = list.get(list.size() / 2);
            Integer elementForCompare = list.get((list.size() / 2) - 1);
            if (Integer.compare(element, elementForCompare) < 0) {
                return (E) element;
            } else {
                return (E) elementForCompare;
            }
        }
    }

    private int findIndex(E e) {
        int index = 0;
        for (E i : this) {
            if (e.equals(i)) {
                return index;
            } else index++;
        }
        return 0;
    }

    @Override
    public boolean offer(E e) {
        try {
            NodeForQueue<E> newLastNode = lastNode;
            newLastNode.setItem(e);
            if (newLastNode.getPrev().getItem() == null) {
                firstNode.setNext(newLastNode);
                newLastNode.setPrev(firstNode);
            }
            lastNode = new NodeForQueue<>(newLastNode, null, null);
            newLastNode.setNext(lastNode);
            size++;
            return true;
        } catch (IllegalStateException someException) {
            someException.printStackTrace();
            return false;
        }
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public E peek() {
        E medianElement = findMedian();
        int index = findIndex(medianElement);
        NodeForQueue<E> target = firstNode.getNext();
        for (int i = 0; i < index; i++) {
            target = getNextElement(target);
        }
        return target.getItem();
    }

    @Override
    public E poll() {
        if (firstNode.getNext().getNext().getItem() == null) {
            E result = firstNode.getNext().item;
            firstNode.setNext(lastNode);
            lastNode.setPrev(firstNode);
            size--;
            return result;
        }
        E medianElement = findMedian();
        int index = findIndex(medianElement);
        NodeForQueue<E> target = firstNode.getNext();
        for (int i = 0; i < index; i++) {
            target = getNextElement(target);
        }
        E elementByIndex = getElementByIndex(index);
        target.getPrev().setNext(target.getNext());
        target.getNext().setPrev(target.getPrev());
        if (target.getPrev().equals(firstNode)) {
            firstNode = target.getPrev();
        }
        if (target.getNext().equals(lastNode)) {
            lastNode = target.getNext();
        }

        if (size == 1) {
            firstNode = target.getPrev();
        }
        size--;
        return elementByIndex;
    }

    @Override
    public Iterator<E> iterator() {
        return new Iterator<>() {
            int counter = 0;

            @Override
            public boolean hasNext() {
                return counter < size;
            }

            @Override
            public E next() {
                return getElementByIndex(counter++);
            }
        };
    }
}

class NodeForQueue<E> {
    E item;
    NodeForQueue<E> next;
    NodeForQueue<E> prev;

    NodeForQueue(NodeForQueue<E> prev, E element, NodeForQueue<E> next) {
        this.item = element;
        this.next = next;
        this.prev = prev;
    }

    public E getItem() {
        return item;
    }

    public void setItem(E item) {
        this.item = item;
    }

    public NodeForQueue<E> getNext() {
        return next;
    }

    public void setNext(NodeForQueue<E> next) {
        this.next = next;
    }

    public NodeForQueue<E> getPrev() {
        return prev;
    }

    public void setPrev(NodeForQueue<E> prev) {
        this.prev = prev;
    }


}

