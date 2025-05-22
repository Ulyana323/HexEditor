package ru.khav.ProjectNIC.TableConfig;

import javax.swing.table.DefaultTableModel;
import java.util.Vector;

public class TableModelFactory {

    public DefaultTableModel createFileDataTableModel(Vector<Vector<String>> data, Vector<String> columnNames) {
        return new DefaultTableModel(data, columnNames);
    }

    public DefaultTableModel createAddressTableModel(Vector<Vector<String>> addresses) {
        Vector<String> columnAddressNames = new Vector<>();
        columnAddressNames.add("adress / byte");
        return new DefaultTableModel(addresses, columnAddressNames) {
            @Override
            public boolean isCellEditable(int row, int column) {
                // запрет на редактирование ячейки адреса
                return column != 0;
            }
        };
    }
}
