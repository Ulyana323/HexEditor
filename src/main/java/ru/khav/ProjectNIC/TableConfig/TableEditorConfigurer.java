package ru.khav.ProjectNIC.TableConfig;

import ru.khav.ProjectNIC.Services.DataEditorService;
import ru.khav.ProjectNIC.UI_Components.PanelFactory;
import ru.khav.ProjectNIC.UI_Components.TableFactory;
import ru.khav.ProjectNIC.models.DataFromFile;
import ru.khav.ProjectNIC.models.DataManager;
import ru.khav.ProjectNIC.Services.SetAppKeywordAction;
import ru.khav.ProjectNIC.views.DataFromFileTable;

import javax.swing.*;

import java.io.IOException;
import java.util.logging.Logger;

import static ru.khav.ProjectNIC.utill.Globals.address;
import static ru.khav.ProjectNIC.utill.Globals.countByte;

public class TableEditorConfigurer {
    private Logger logger = Logger.getLogger(TableEditorConfigurer.class.getName());

    public void EditDataInMeanTable(DataFromFileTable dataFromFileTable, DataManager dataManager, JTable meanByteTable) {
        logger.info("EditDataInMeanTable() :TableEditorConfigurer");
        dataFromFileTable.getColumnModel().getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int selectedRow = dataFromFileTable.getSelectedRow();
                int[] selectedColumns = dataFromFileTable.getSelectedColumns();

                int meanA = 3;
                int mean = 2;
                int addr = 0;
                int byteNum = 1;
                int indexRow = 0;


                for (int col : selectedColumns) {
                    int curPos = selectedRow * countByte + col;
                    if (dataFromFileTable.isCellSelected(selectedRow, col) && curPos < dataManager.getCurrentByteData().size()) {
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

    public void editDataInMainTable(DataFromFileTable dataFromFileTable,
                                    DataManager dataManager,
                                    PanelFactory panelFactory,
                                    TableFactory tableFactory) {
        logger.info("editDataInMainTable() :TableEditorConfigurer");
        //одиночное изменение
        dataFromFileTable.getModel().addTableModelListener(l -> {
            int row = l.getFirstRow();
            int col = l.getColumn();
            if (row < 0 || col < 0) return;
            int curPos = row * countByte + col;
            String hexString = (String) dataFromFileTable.getModel().getValueAt(row, col);
            if (curPos >= dataManager.getCurrentByteData().size()) {
                if (dataManager.getReadDataFromFile().isLastPage()) {
                    DataEditorService.wideCurData(hexString, curPos, dataManager.getCurrentByteData()
                            , dataManager.getCurrentIntData(),
                            dataManager.getCurrentStrData());
                }
            } else {
                DataEditorService.updateCurData(hexString, curPos, dataManager.getCurrentByteData()
                        , dataManager.getCurrentIntData(),
                        dataManager.getCurrentStrData());
            }
            try {
                DataFromFile data = new DataFromFile(dataManager.getCurrentByteData(),
                        dataManager.getCurrentIntData());
                dataManager.getReadDataFromFile().updateDataInFile(data);
            } catch (IOException e) {
                logger.severe(e.getMessage());
                throw new RuntimeException(e);
            }
        });
        SetAppKeywordAction.setupDeleteAction(dataFromFileTable, dataManager, countByte);
        SetAppKeywordAction.setupCopyActionWithoutCut(dataFromFileTable);
        SetAppKeywordAction.setupCutAction(dataFromFileTable, dataManager,
                countByte, panelFactory.getSecondPanel());
        SetAppKeywordAction.setupInsertWithChangeAction(dataFromFileTable, dataManager,
                countByte);
        SetAppKeywordAction.setupInsertWithoutChangeAction(dataFromFileTable, dataManager,
                countByte, address, tableFactory);
    }
}
