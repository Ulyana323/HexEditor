package ru.khav.ProjectNIC.models;

import ru.khav.ProjectNIC.Enums.TypesOfMeanData;

import javax.swing.table.AbstractTableModel;

public class MeanTableModel extends AbstractTableModel {

    final Object[][] meanings = new Object[8][4];

    public void clear() {
        for (int i = 0; i < meanings.length; i++) {
            for (int j = 0; j < meanings[i].length; j++) {
                meanings[i][j] = null;
            }
        }
        fireTableDataChanged();
    }

    @Override
    public String getColumnName(int column) {
        switch (column) {
            case 0:
                return TypesOfMeanData.LocalAddress.name();
            case 1:
                return TypesOfMeanData.Byte.name();
            case 2:
                return TypesOfMeanData.Signed.name();
            case 3:
                return TypesOfMeanData.Unsigned.name();
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
        meanings[rowIndex][columnIndex] = aValue;
        fireTableCellUpdated(rowIndex, columnIndex); // перерисовка ячейки
    }

    @Override
    public int getColumnCount() {
        return 4;
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
