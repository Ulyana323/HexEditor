package ru.khav.ProjectNIC.views;

import javax.swing.*;
import javax.swing.table.TableModel;

public class AddressTable extends JTable {

    public AddressTable(TableModel tm) {
        super(tm);
        setCellSelectionEnabled(false);
        getColumnModel().setColumnSelectionAllowed(false);

    }
    @Override
    public TableModel getModel() {
        return super.getModel();
    }
}
