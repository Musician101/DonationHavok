package io.musician101.donationhavok.gui.model.table;

import java.util.ArrayList;
import java.util.List;
import javax.swing.table.AbstractTableModel;

public abstract class ListTableModel<E> extends AbstractTableModel {

    final List<E> elements;
    private final String[] columns;

    ListTableModel(List<E> elements, String... columns) {
        this.elements = new ArrayList<>(elements);
        this.columns = columns;
    }

    public void add(E element) {
        elements.add(element);
        fireTableDataChanged();
    }

    @Override
    public int getColumnCount() {
        return columns.length;
    }

    @Override
    public String getColumnName(int column) {
        return columns[column];
    }

    public List<E> getElements() {
        return elements;
    }

    public E getObjectAt(int row) {
        return elements.get(row);
    }

    @Override
    public int getRowCount() {
        return elements.size();
    }

    public void remove(int row) {
        elements.remove(row);
        fireTableDataChanged();
    }

    public void replace(int row, E element) {
        elements.set(row, element);
        fireTableDataChanged();
    }
}
