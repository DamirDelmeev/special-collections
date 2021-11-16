package com.efimchick.ifmo.collections;

import java.util.AbstractSet;
import java.util.Collection;
import java.util.Iterator;

class SortedByAbsoluteValueIntegerSet<E> extends AbstractSet<E> {
    Node<E> root;
    int size = 0;

    public SortedByAbsoluteValueIntegerSet() {
        this.root = new Node<>(null);
    }

    public int size() {
        return this.size;
    }

    public boolean contains(Object e) {
        Node<E> findNode = new Node<>((E) e);
        Node<E> resultNode = search(root, findNode);
        return resultNode != null && (findNode.compareTo(resultNode) == 0);
    }

    private Node<E> search(Node<E> oldNode, Node<E> findNode) {
        int compare = oldNode.compareTo(findNode);
        if (compare < 0 && oldNode.right != null) {
            return search(oldNode.right, findNode);
        }
        if (compare > 0 && oldNode.left != null) {
            return search(oldNode.left, findNode);
        }
        if (compare == 0) {
            return oldNode;
        }
        return null;
    }

    @Override
    public boolean add(E e) {
        if (size == 0) {
            root = new Node<>(e);
            size++;
            //    sort();
            return true;
        }
        Node<E> newNode = new Node<>(e);
        Node<E> lastNode = findLastNode(root, newNode);
        if (lastNode == null) {
            //       sort();
            return false;
        }
        size++;
        newNode.parent = lastNode;
        if ((lastNode.compareTo(newNode)) < 0) {
            lastNode.right = newNode;
        } else {
            lastNode.left = newNode;
        }
        return true;
    }

    private Node<E> findLastNode(Node<E> oldNode, Node<E> newNode) {
        Node<E> lastNode = oldNode;
        int compare = oldNode.compareTo(newNode);
        if (compare < 0 && oldNode.right != null) {
            lastNode = findLastNode(oldNode.right, newNode);
            return lastNode;
        }
        if (compare > 0 && oldNode.left != null) {
            lastNode = findLastNode(oldNode.left, newNode);
            return lastNode;
        }
        if (compare == 0) {
            return null;

        }
        return lastNode;
    }

    @Override
    public boolean remove(Object o) {
        Node<E> eNode = new Node<>((E) o);
        Node<E> nodeForRemove = search(root, eNode);
        if (nodeForRemove == null) {
            return false;
        }
        if (nodeForRemove.left == null & nodeForRemove.right == null) {
            if ((nodeForRemove.parent.left.compareTo(nodeForRemove)) == 0) {
                nodeForRemove.parent.left = null;
            } else {
                nodeForRemove.parent.right = null;
            }
        } else {
            if (nodeForRemove.parent.left != null && ((nodeForRemove.parent.left.compareTo(nodeForRemove)) == 0)) {
                nodeForRemove.parent.left = nodeForRemove.right;
                if (nodeForRemove.right.left != null) {
                    nodeForRemove.right.left.left = nodeForRemove.left;
                    nodeForRemove.left.parent = nodeForRemove.right.left.left;
                } else {
                    nodeForRemove.right.left = nodeForRemove.left;
                    nodeForRemove.left.parent = nodeForRemove.right.left;
                }
            } else {
                nodeForRemove.parent.right = nodeForRemove.right;
                if (nodeForRemove.right.left != null) {
                    nodeForRemove.right.left.left = nodeForRemove.left;
                    if (nodeForRemove.left != null) {
                        nodeForRemove.left.parent = nodeForRemove.right.left.left;
                    }
                } else {
                    nodeForRemove.right.left = nodeForRemove.left;
                    nodeForRemove.left.parent = nodeForRemove.right.left;
                }
            }
        }
        size--;
        return true;
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        try {
            for (E someValue : c) {
                add(someValue);
            }
            return true;
        } catch (ClassCastException classCastException) {
            classCastException.printStackTrace();
            return false;
        }
    }

    @Override
    public Iterator<E> iterator() {
        return new Iterator<>() {
            final Iterator<Node<E>> iterator = new TreeIterator<>(root);
            int count;
            @Override
            public boolean hasNext() {
                return iterator.hasNext();
            }

            @Override
            public E next() {
                count++;
                return iterator.next().element;
            }
        };
    }
}

class Node<E> implements Comparable<E> {
    Node<E> parent;
    Node<E> right;
    Node<E> left;
    E element;

    public Node(E element) {
        this.element = element;
    }

    @Override
    public int compareTo(Object o) {
        Node<E> node = (Node<E>) o;
        return Integer.compare(Math.abs((Integer) this.element), Math.abs((Integer) node.element));
    }
}

class TreeIterator<E> implements Iterator<Node<E>> {
    Node<E> next;
    TreeIterator(Node<E> root) {
        this.next = root;
        findLeftNode();
    }

    @Override
    public boolean hasNext() {
        return next != null && next.element != null;
    }

    @Override
    public Node<E> next() {
        Node<E> temp = next;
        if (next.right != null) {
            return goToRight(temp);
        }
        return goToParent(temp);
    }

    private void findLeftNode() {
        while (next.left != null) {
            next = next.left;
        }
    }

    private Node<E> goToRight(Node<E> temp) {
        next = next.right;
        while (next.left != null) {
            next = next.left;
        }
        return temp;
    }

    private Node<E> goToParent(Node<E> temp) {
        while (true) {
            if (next.parent == null) {
                next = null;
                return temp;
            }
            if (next.parent.left == next) {
                next = next.parent;
                return temp;
            }
            next = next.parent;
        }
    }

}
