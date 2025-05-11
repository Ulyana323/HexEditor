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

    @Override
    public TableModel getModel() {
        return super.getModel();
    }

    public void scrollTable(int direction) {
        int currentViewableColumns = getVisibleRect().width / getColumnModel().getColumn(0).getWidth();

        if (direction == 1) { //r
            int newColumnIndex = Math.min(getSelectedColumn() + currentViewableColumns, getColumnCount() - 1);
            scrollRectToVisible(getCellRect(0, newColumnIndex, true));
            changeSelection(0, newColumnIndex, false, false);
        } else { //l
            int newColumnIndex = Math.max(getSelectedColumn() - currentViewableColumns, 0);
            scrollRectToVisible(getCellRect(0, newColumnIndex, true));
            changeSelection(0, newColumnIndex, false, false);
        }
    }

}
