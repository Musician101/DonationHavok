package io.musician101.donationhavok.gui.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import javax.swing.AbstractListModel;

public class SortedListModel<E> extends AbstractListModel<E> {

    private final Comparator<E> comparator;
    private final List<E> elements;

    public SortedListModel(List<E> elements, Comparator<E> comparator) {
        this.elements = new ArrayList<>(elements);
        this.comparator = comparator;
        sort();
    }

    public void addAll(Collection<? extends E> collection) {
        collection.forEach(this::addElement);
    }

    public void addElement(E element) {
        elements.add(element);
        int index = elements.size();
        fireIntervalAdded(element, index, index);
        sort();
    }

    public void clear() {
        for (int x = 0; x < elements.size(); x++) {
            remove(x);
        }
    }

    @Override
    public E getElementAt(int index) {
        return elements.get(index);
    }

    public List<E> getElements() {
        return elements;
    }

    @Override
    public int getSize() {
        return elements.size();
    }

    public void remove(int index) {
        E element = elements.remove(index);
        fireIntervalRemoved(element, index, index);
        sort();
    }

    public void sort() {
        elements.sort(comparator);
    }
}
