package ru.khav.ProjectNIC.models;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MeanTableModel extends AbstractTableModel {

    Object[][] meanings= new Object[8][3];

    public void clear() {
        for (int i = 0; i < meanings.length; i++) {
            for (int j = 0; j < meanings[i].length; j++) {
                meanings[i][j] = null;
            }
        }
        fireTableDataChanged();
    }
    @Override
    public void fireTableDataChanged() {
        super.fireTableDataChanged();

    }

    @Override
    public String getColumnName(int column) {
        switch (column) {
            case 0:
                return "local address";
            case 1:
                return "Byte";
            case 2:
                return "Dec Mean";
            default:
                return null;
        }
    }


    @Override
    public int getRowCount() {
        return 8;
    }

    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        meanings[rowIndex][columnIndex]=aValue;
        fireTableCellUpdated(rowIndex, columnIndex); // перерисовка ячейки
    }

    @Override
    public int getColumnCount() {
        return 3;
    }


    @Override
    public boolean isCellEditable(int row, int column) {
        // запрет на редактирование ячейки адреса
        return false;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        return meanings[rowIndex][columnIndex];
    }
}
