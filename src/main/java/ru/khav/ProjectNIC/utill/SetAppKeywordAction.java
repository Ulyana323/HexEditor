package ru.khav.ProjectNIC.utill;

import ru.khav.ProjectNIC.Interfaces.ReadDataFromFile;
import ru.khav.ProjectNIC.UI_Components.TableFactory;
import ru.khav.ProjectNIC.models.DataFromFile;
import ru.khav.ProjectNIC.views.TableData;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;


public class SetAppKeywordAction {
    private static Logger logger = Logger.getLogger(SetAppKeywordAction.class.getName());
    private static List<Object> buffer = new ArrayList<>();

    public static void setupDeleteAction(TableData tableData, DataManager dataManager,
                                         int countByte) {
        //удаление одиночное и блочно
        KeyStroke deleteKey = KeyStroke.getKeyStroke(KeyEvent.VK_DELETE, 0);
        tableData.getInputMap(JComponent.WHEN_FOCUSED).put(deleteKey, "deleteCells");
        tableData.getActionMap().put("deleteCells", new AbstractAction() {
            @Override//удаляем блоки клавишей delete
            public void actionPerformed(ActionEvent e) {
                int selectedRow = tableData.getSelectedRow();
                int[] selectedCols = tableData.getSelectedColumns();
                for (int col : selectedCols) {
                    tableData.setValueAt("0", selectedRow, col);
                    int curPos = selectedRow * countByte + col;
                    if (curPos < dataManager.getCurrentStrData().size() && curPos < dataManager.getCurrentByteData().size()) {
                        dataManager.updateCurData("0", curPos);
                    } else {
                        dataManager.wideCurData("0", curPos);
                    }
                }
                try {
                    DataFromFile data = new DataFromFile(dataManager.getCurrentByteData(), dataManager.getCurrentIntData());
                    dataManager.getReadDataFromFile().updateDataInFile(data);
                } catch (IOException ex) {
                        logger.severe("Ошибка при сохранении после удаления: " + ex.getMessage());
                }
            }
        });
    }
    public static void  setupInsertWithoutChаngeAction(TableData tableData, DataManager dataManager,
                                                       int countByte, int address,TableFactory tableFactory) {
        List<Byte>  currentByteData=dataManager.getCurrentByteData();
        List<String> currentStrData=dataManager.getCurrentStrData();
        List<Integer> currentIntData=dataManager.getCurrentIntData();
        ReadDataFromFile readDataFromFile=dataManager.getReadDataFromFile();
//вставка без замены
        KeyStroke ctrlX = KeyStroke.getKeyStroke(KeyEvent.VK_X, InputEvent.CTRL_DOWN_MASK);
        tableData.getInputMap(JComponent.WHEN_FOCUSED).put(ctrlX, "addXFromBuff");
        tableData.getActionMap().put("addXFromBuff", new AbstractAction() {
            @Override//вставка блока элементов с помощью ctrlX
            public void actionPerformed(ActionEvent e) {
                TableFactory tablefactory= tableFactory;
                int selectedRow = tableData.getSelectedRow();
                int[] selectedCols = tableData.getSelectedColumns();
                int posFromCopy = selectedRow * countByte + selectedCols[0];
                int posToInsert = selectedRow * countByte + selectedCols[selectedCols.length - 1] + 1;
                List<Byte> toInsByte = new ArrayList<>(currentByteData.subList(posFromCopy, currentByteData.size() - 1));
                List<Integer> toInsInt = new ArrayList<>(currentIntData.subList(posFromCopy, currentIntData.size() - 1));
                List<String> toInsStr = new ArrayList<>(currentStrData.subList(posFromCopy, currentStrData.size() - 1));
                int it = 0;
                String value;
                dataManager.wideCurDataWithoutChange(selectedCols.length);
                for (int col : selectedCols) {
                    if (it >= buffer.size()) break;
                    value = (String) buffer.get(it);
                    tableData.getModel().setValueAt(value, selectedRow, col);
                    int curPos = selectedRow * countByte + col;
                    it++;
                    if (curPos >= currentByteData.size()) {
                        if (readDataFromFile.isLastPage()) {
                            dataManager.wideCurData(value, curPos);
                        }
                    } else {
                        dataManager.updateCurData(value, curPos);
                    }
                }
                dataManager.updateBalanceCurDataWider(toInsByte, toInsInt, toInsStr, posToInsert,countByte,address,tablefactory);
                TableScaleService.changeScaleDataTable(currentStrData, countByte, address,tableFactory);

                try {
                    DataFromFile data = new DataFromFile(currentByteData, currentIntData);
                    readDataFromFile.updateDataInFile(data);
                } catch (IOException ex) {
                    logger.severe("Ошибка при сохранении после вставки без замены: " + ex.getMessage());
                }
            }
        });
    }

  public static void setupInsertWithChangeAction(TableData tableData, DataManager dataManager,
                                                 int countByte) {
        List<Byte>  currentByteData=dataManager.getCurrentByteData();
        List<Integer> currentIntData=dataManager.getCurrentIntData();
        ReadDataFromFile readDataFromFile=dataManager.getReadDataFromFile();
//вставка с заменой
        KeyStroke ctrlV = KeyStroke.getKeyStroke(KeyEvent.VK_V, InputEvent.CTRL_DOWN_MASK);
        tableData.getInputMap(JComponent.WHEN_FOCUSED).put(ctrlV, "addFromBuff");
        tableData.getActionMap().put("addFromBuff", new AbstractAction() {
            @Override//вставка блока элементов с помощью ctrlv
            public void actionPerformed(ActionEvent e) {
                int selectedRow = tableData.getSelectedRow();
                int[] selectedCols = tableData.getSelectedColumns();
                int it = 0;
                String value;
                for (int col : selectedCols) {
                    if (it >= buffer.size()) break;
                    value = (String) buffer.get(it);
                    int curPos = selectedRow * countByte + col;
                    it++;
                    if (curPos >= currentByteData.size()) {
                        if (!readDataFromFile.isLastPage()){
                            break;}
                        dataManager.wideCurData(value, curPos);}
                    else {
                        tableData.getModel().setValueAt(value, selectedRow, col);
                        dataManager.updateCurData(value, curPos);
                    }
                }
                try {
                    DataFromFile data = new DataFromFile(currentByteData, currentIntData);
                    readDataFromFile.updateDataInFile(data);
                } catch (IOException ex) {
                   // logger.severe("Ошибка при сохранении после вставки c заменой: " + ex.getMessage());
                }
            }
        });
    }

    public static void setupCutAction(TableData tableData, DataManager dataManager,
                                      int countByte, JPanel secondPanel) {
        List<Byte>  currentByteData=dataManager.getCurrentByteData();

        List<Integer> currentIntData=dataManager.getCurrentIntData();
        ReadDataFromFile readDataFromFile=dataManager.getReadDataFromFile();

        //вырезка
        KeyStroke ctrlB = KeyStroke.getKeyStroke(KeyEvent.VK_B, InputEvent.CTRL_DOWN_MASK);
        tableData.getInputMap(JComponent.WHEN_FOCUSED).put(ctrlB, "addAllToBuff");
        tableData.getActionMap().put("addAllToBuff", new AbstractAction() {
            @Override//вырезка в буфер блока элементов с помощью ctrlb
            public void actionPerformed(ActionEvent e) {
                int selectedRow = tableData.getSelectedRow();
                int[] selectedCols = tableData.getSelectedColumns();
                buffer.clear();
                for (int col : selectedCols) {
                    int curPos = selectedRow * countByte + col;
                    if (tableData.getModel().getValueAt(selectedRow, col) == null) {
                        buffer.add("0");
                    } else {
                        buffer.add(tableData.getModel().getValueAt(selectedRow, col));
                    }
                    if (curPos < currentByteData.size()) {
                        tableData.getModel().setValueAt("0", selectedRow, col);
                        dataManager.updateCurData("0", curPos);
                    }
                }
                try {
                    DataFromFile data = new DataFromFile(currentByteData, currentIntData);
                    readDataFromFile.updateDataInFile(data);
                } catch (IOException ex) {
                    logger.severe("Ошибка при сохранении после вырезки: " + ex.getMessage());
                }
                secondPanel.revalidate();
            }
        });
    }

    public static void setupCopyActionWithoutCut(TableData tableData) {
        //копирование без вырезания
        KeyStroke ctrlC = KeyStroke.getKeyStroke(KeyEvent.VK_C, InputEvent.CTRL_DOWN_MASK);
        tableData.getInputMap(JComponent.WHEN_FOCUSED).put(ctrlC, "addToBuff");
        tableData.getActionMap().put("addToBuff", new AbstractAction() {
            @Override//копирование в буфер блока элементов с помощью ctrlс
            public void actionPerformed(ActionEvent e) {
                int selectedRow = tableData.getSelectedRow();
                int[] selectedCols = tableData.getSelectedColumns();
                buffer.clear();
                for (int col : selectedCols) {
                    buffer.add(tableData.getModel().getValueAt(selectedRow, col));
                }
            }
        });
    }
}

