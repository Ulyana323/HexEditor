package ru.khav.ProjectNIC.views;

import javax.swing.*;
import javax.swing.table.TableModel;

public class MeanByteTable extends JTable {

    public MeanByteTable(TableModel tm) {
        super(tm);
        getTableHeader().setResizingAllowed(false);
        revalidate();
        repaint();

    }

}
