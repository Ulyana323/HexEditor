package ru.khav.ProjectNIC.views;

import javax.swing.*;
import javax.swing.table.TableModel;
import java.awt.*;

public class TableData extends JTable {

    public TableData(TableModel tableModel) {
        super(tableModel);
        getTableHeader().setResizingAllowed(false);
        getTableHeader().setReorderingAllowed(false);
        setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        setCellSelectionEnabled(true);
        getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        setSelectionBackground(Color.YELLOW);
    }

}
