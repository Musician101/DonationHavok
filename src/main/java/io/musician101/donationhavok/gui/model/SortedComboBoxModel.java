package io.musician101.donationhavok.gui.model;

import java.util.Comparator;
import java.util.List;
import javax.swing.AbstractListModel;
import javax.swing.MutableComboBoxModel;

public class SortedComboBoxModel<E> extends AbstractListModel<E> implements MutableComboBoxModel<E> {

    private final Comparator<E> comparator;
    private E selectedObject;
    private final List<E> elements;

    public SortedComboBoxModel(List<E> elements, Comparator<E> comparator) {
        this.elements = elements;
        this.comparator = comparator;
        sort();
    }

    public List<E> getElements() {
        return elements;
    }

    @Override
    public int getSize() {
        return elements.size();
    }

    @Override
    public E getElementAt(int index) {
        return elements.get(index);
    }

    @Override
    public void removeElementAt(int index) {
        E element = elements.remove(index);
        fireIntervalRemoved(element, index, index);
        sort();
    }

    @Override
    public void addElement(E element) {
        elements.add(element);
        int index = elements.size() - 1;
        fireIntervalAdded(element, index, index);
        sort();
    }

    @Override
    public void removeElement(Object obj) {
        int index = elements.indexOf(obj);
        if (index != -1) {
            removeElementAt(index);
            sort();
        }
    }

    @Override
    public void insertElementAt(E item, int index) {
        elements.add(index, item);
        fireIntervalAdded(this, index, index);
        sort();
    }

    @Override
    public void setSelectedItem(Object anItem) {
        if ((selectedObject != null && !selectedObject.equals(anItem)) || (selectedObject == null && anItem != null)) {
            selectedObject = (E) anItem;
            fireContentsChanged(this, -1, -1);
            sort();
        }
    }

    @Override
    public Object getSelectedItem() {
        return selectedObject;
    }

    public void sort() {
        elements.sort(comparator);
    }
}
