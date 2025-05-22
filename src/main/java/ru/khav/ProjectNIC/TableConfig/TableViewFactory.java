package ru.khav.ProjectNIC.TableConfig;

import ru.khav.ProjectNIC.models.MeanTableModel;
import ru.khav.ProjectNIC.views.AddressTable;
import ru.khav.ProjectNIC.views.MeanByteTable;
import ru.khav.ProjectNIC.views.DataFromFileTable;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableModel;

public class TableViewFactory {

    public JTable createMeanByteTable(AbstractTableModel model) {
        return new MeanByteTable((MeanTableModel) model);
    }

    public AddressTable createAddressTable(DefaultTableModel model) {
        return new AddressTable(model);
    }

    public DataFromFileTable createDataTable(DefaultTableModel model) {
        return new DataFromFileTable(model);
    }
}
