package ru.khav.ProjectNIC.Table;

import ru.khav.ProjectNIC.UI_Components.PanelFactory;
import ru.khav.ProjectNIC.UI_Components.TableFactory;
import ru.khav.ProjectNIC.models.DataFromFile;
import ru.khav.ProjectNIC.utill.DataManager;
import ru.khav.ProjectNIC.utill.SetAppKeywordAction;
import ru.khav.ProjectNIC.views.TableData;

import javax.swing.*;

import java.io.IOException;

import static ru.khav.ProjectNIC.utill.Globals.address;
import static ru.khav.ProjectNIC.utill.Globals.countByte;

public class TableEditorConfigurer {
    public void EditDataInMeanTable(TableData tableData, DataManager dataManager, JTable meanByteTable) {
      //  logger.info("showDecMeanConfig() :TableFactory");
        tableData.getColumnModel().getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int selectedRow = tableData.getSelectedRow();
                int[] selectedColumns = tableData.getSelectedColumns();

                int meanA = 3;
                int mean = 2;
                int addr = 0;
                int byteNum = 1;
                int indexRow = 0;


                for (int col : selectedColumns) {
                    int curPos = selectedRow * countByte + col;
                    if (tableData.isCellSelected(selectedRow, col) && curPos < dataManager.getCurrentByteData().size()) {
                        String value = dataManager.getCurrentStrData().get(curPos);
                        byte byteValue = dataManager.getCurrentByteData().get(curPos);
                        int unsignValue = dataManager.getCurrentIntData().get(curPos);
                        if (indexRow <= 7) {
                            meanByteTable.getModel().setValueAt(Integer.toString(byteValue), indexRow, mean);
                            meanByteTable.getModel().setValueAt(String.format("%8s", Integer.toBinaryString(selectedRow * countByte + col)).replace(' ', '0'), indexRow, addr);
                            meanByteTable.getModel().setValueAt(col, indexRow, byteNum);
                            meanByteTable.getModel().setValueAt(Integer.toString(unsignValue), indexRow, meanA);

                            indexRow++;
                        }
                    }
                }
                for (int i = indexRow; i < 8; i++) {
                    meanByteTable.getModel().setValueAt("", i, mean);
                    meanByteTable.getModel().setValueAt("", i, addr);
                    meanByteTable.getModel().setValueAt("", i, byteNum);
                    meanByteTable.getModel().setValueAt("", i, meanA);
                }
            }
        });

    }
    public void editDataInMainTableConfig(TableData tableData,
                                          DataManager dataManager,
                                          PanelFactory panelFactory,
                                          TableFactory tableFactory) {
        //logger.info("editDataConfig() :TableFactory");
        //одиночное изменение
        tableData.getModel().addTableModelListener(l -> {
            int row = l.getFirstRow();
            int col = l.getColumn();
            if (row < 0 || col < 0) return;
            int curPos = row * countByte + col;
            String hexString = (String) tableData.getModel().getValueAt(row, col);
            if (curPos >= dataManager.getCurrentByteData().size()) {
                if (dataManager.getReadDataFromFile().isLastPage()) {
                    dataManager.wideCurData(hexString, curPos);
                }
            } else {
                dataManager.updateCurData(hexString, curPos);
            }
            try {
                DataFromFile data = new DataFromFile(dataManager.getCurrentByteData(),
                        dataManager.getCurrentIntData());
                dataManager.getReadDataFromFile().updateDataInFile(data);
            } catch (IOException e) {
                //logger.severe(e.getMessage());
                throw new RuntimeException(e);
            }
        });
        SetAppKeywordAction.setupDeleteAction(tableData, dataManager, countByte);
        SetAppKeywordAction.setupCopyActionWithoutCut(tableData);
        SetAppKeywordAction.setupCutAction(tableData, dataManager,
                countByte, panelFactory.getSecondPanel());
        SetAppKeywordAction.setupInsertWithChangeAction(tableData, dataManager,
                countByte);
        SetAppKeywordAction.setupInsertWithoutChаngeAction(tableData, dataManager,
                countByte, address, tableFactory);//todo tablefactory?
    }
}
