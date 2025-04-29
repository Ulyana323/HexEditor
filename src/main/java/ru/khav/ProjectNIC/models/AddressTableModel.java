package ru.khav.ProjectNIC.models;

import javax.swing.table.AbstractTableModel;

public class AddressTableModel extends AbstractTableModel {
    @Override
    public int getRowCount() {
        return 1;
    }

    @Override
    public int getColumnCount() {
        return 1000;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        return null;
    }

    @Override
    public boolean isCellEditable(int row, int column) {
        // запрет на редактирование ячейки адреса
        return column != 0;
    }
}
