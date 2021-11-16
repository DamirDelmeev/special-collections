package com.efimchick.ifmo.collections;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

class PairStringList<E> extends ArrayList<E> {
    E[] values;

    public PairStringList() {
        values = (E[]) new Object[0];
    }

    @Override
    public int size() {
        return values.length;
    }

    @Override
    public E get(int index) {
        return values[index];
    }

    @Override
    public E set(int index, E element) {
        if (index - 1 >= 0) {
            if (values[index] != null && values[index].equals(values[index - 1])) {
                values[index - 1] = element;
            } else {
                if (values[index] == null & values[index - 1] == null) {
                    values[index] = element;
                    return values[index - 1] = element;
                }
                values[index + 1] = element;
            }
        } else {
            values[index + 1] = element;
        }
        return values[index] = element;
    }

    @Override
    public boolean add(E e) {
        try {
            E[] temp = values;
            values = (E[]) new Object[temp.length + 2];
            System.arraycopy(temp, 0, values, 0, temp.length);
            set(values.length - 2, e);
            return true;
        } catch (ClassCastException classCastException) {
            classCastException.printStackTrace();
        }
        return false;
    }

    @Override
    public void add(int index, E element) {
        if (index % 2 != 0) {
            index++;
        }
        E[] temp = values;
        values = (E[]) new Object[temp.length + 2];
        if (index <= 1) {
            System.arraycopy(temp, 0, values, 2, temp.length);
            set(index, element);
        } else {
            if (index == values.length | index == values.length - 1) {
                System.arraycopy(temp, 0, values, 0, values.length - 2);
                set(index, element);
            }
            System.arraycopy(temp, 0, values, 0, index);
            System.arraycopy(temp, index, values, index + 2, temp.length - index);
            set(index, element);
        }
    }

    @Override
    public E remove(int index) {
        E[] temp = values;
        values = (E[]) new Object[temp.length - 2];
        if (index <= 1) {
            System.arraycopy(temp, 2, values, 0, values.length);
        } else if (index == temp.length | index == temp.length - 1) {
            System.arraycopy(temp, 0, values, 0, values.length);
        } else {
            if (index % 2 != 0) {
                index++;
            }
            System.arraycopy(temp, 0, values, 0, index);
            System.arraycopy(temp, index + 2, values, index, temp.length - index - 2);
        }
        return temp[index];
    }

    @Override
    public boolean remove(Object o) {
        int i = indexOfRange(o, values.length);
        if (i >= 0) {
            remove(i);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        Object[] a = c.toArray();
        if (a.length == 0) {
            return false;
        }
        for (Object someValueForAdd : a) {
            add((E) someValueForAdd);
        }
        return true;
    }

    @Override
    public boolean addAll(int index, Collection<? extends E> c) {
        Object[] a = c.toArray();
        if (a.length == 0) {
            return false;
        }
        for (Object someValueForAdd : a) {
            add(index, (E) someValueForAdd);
            index = index + 2;
        }
        return true;
    }

    @Override
    public Iterator<E> iterator() {
        return new IteratorForPair(values);
    }

    private int indexOfRange(Object o, int end) {
        Object[] es = values;
        if (o == null) {
            for (int i = 0; i < end; i++) {
                if (es[i] == null) {
                    return i;
                }
            }
        } else {
            for (int i = 0; i < end; i++) {
                if (o.equals(es[i])) {
                    return i;
                }
            }
        }
        return -1;
    }

    public void clear() {
        values = (E[]) new Object[0];
    }
}

class IteratorForPair<E> implements Iterator<E> {
    private final E[] values;
    private int index = 0;

    public IteratorForPair(E[] values) {
        this.values = values;
    }

    @Override
    public boolean hasNext() {
        return index < values.length;
    }

    @Override
    public E next() {
        return values[index++];
    }
}
