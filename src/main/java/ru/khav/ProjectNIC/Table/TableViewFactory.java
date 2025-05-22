package ru.khav.ProjectNIC.Table;

import ru.khav.ProjectNIC.models.MeanTableModel;
import ru.khav.ProjectNIC.views.AddressTable;
import ru.khav.ProjectNIC.views.MeanByteTable;
import ru.khav.ProjectNIC.views.TableData;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

public class TableViewFactory {

    public JTable createMeanByteTable(AbstractTableModel model) {
        return new MeanByteTable((MeanTableModel) model);
    }

    public AddressTable createAddressTable(DefaultTableModel model) {
        return new AddressTable(model);
    }

    public TableData createDataTable(DefaultTableModel model) {
        return new TableData(model);
    }
}
