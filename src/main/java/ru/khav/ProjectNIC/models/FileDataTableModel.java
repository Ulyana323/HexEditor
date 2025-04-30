package ru.khav.ProjectNIC.models;

import lombok.Data;

import javax.swing.table.AbstractTableModel;
import java.util.List;

@Data
public class FileDataTableModel extends AbstractTableModel {

    private int rowCount;
    private int colCount;

    List<List<String>> data;

    @Override
    public int getRowCount() {
        return rowCount;
    }

    @Override
    public int getColumnCount() {
        return colCount;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        return data.get(rowIndex*columnIndex+columnIndex);
    }
}
