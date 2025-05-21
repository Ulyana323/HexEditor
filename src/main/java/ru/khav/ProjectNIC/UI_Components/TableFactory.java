
package ru.khav.ProjectNIC.UI_Components;


import lombok.Data;
import ru.khav.ProjectNIC.models.DataFromFile;
import ru.khav.ProjectNIC.models.MeanTableModel;
import ru.khav.ProjectNIC.utill.DataManager;
import ru.khav.ProjectNIC.utill.SetAppKeywordAction;
import ru.khav.ProjectNIC.utill.TableScaleService;
import ru.khav.ProjectNIC.views.AddressTable;
import ru.khav.ProjectNIC.views.MainWindow;
import ru.khav.ProjectNIC.views.MeanByteTable;
import ru.khav.ProjectNIC.views.TableData;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Vector;
import java.util.logging.Logger;

import static ru.khav.ProjectNIC.utill.Globals.address;
import static ru.khav.ProjectNIC.utill.Globals.countByte;

@Data
public class TableFactory {
    private Logger logger = Logger.getLogger(TableFactory.class.getName());
    AddressTable addressTable;
    DefaultTableModel tableAddressModel;
    JTable meanByteTable;
    MeanTableModel meanTableModel = new MeanTableModel();//иначе не прорисуется
    DefaultTableModel fileDataTableModel;
    private final DataManager dataManager;
    private final PanelFactory panelFactory;

    private List<List<Integer>> highlightRanges = new ArrayList<>();
    TableData tableData;

    public TableFactory(DataManager dataManager, PanelFactory panelFactory)
    {
        this.dataManager = dataManager;
        this.panelFactory = panelFactory;
        meanByteTable = new MeanByteTable(meanTableModel);
        addressTable = new AddressTable(tableAddressModel);
    }
/*    public AddressTable createAdressTable()
    {
        addressTable = new AddressTable(tableAddressModel);
        return addressTable;
    }
    public JTable createMeanByteTable()
    {
        meanByteTable = new MeanByteTable(meanTableModel);
        return meanByteTable;
    }*/
    public void createDynamicTable(List<String> dataToView, int colls, int rows, MainWindow mainWindow) throws IOException, ParseException {
        logger.info("createDynamicTable() :TableFactory");
        Vector<Vector<String>> myData = new Vector<>();
        Vector<Vector<String>> addresses = new Vector<>();
        dataManager.setCurrentStrData(dataToView);
        int indexData = 0;


        for (int i = 0; i < rows; i++) {
            Vector<String> row = new Vector<>();
            for (int j = 0; j < 1; j++) {
                String addressNum = String.format("%8s", Integer.toBinaryString(i * colls)).replace(' ', '0');
                row.add(addressNum);
            }
            addresses.add(row);
        }

        for (int i = 0; i < rows; i++) {
            Vector<String> row = new Vector<>();
            for (int j = 0; j < colls; j++) {
                String value = " ";
                if (indexData < dataToView.size()) {
                    value = dataToView.get(indexData++);
                }
                row.add(value);
            }
            myData.add(row);
        }
        // Заголовки столбцов
        Vector<String> columnNames = new Vector<>();
        for (int i = 0; i < colls; i++) {
            columnNames.add(String.valueOf(i));
        }
        Vector<String> columnAddressNames = new Vector<>();
        columnAddressNames.add("adress / byte");

        fileDataTableModel = new DefaultTableModel(myData, columnNames);
        tableAddressModel = new DefaultTableModel(addresses, columnAddressNames) {
            @Override
            public boolean isCellEditable(int row, int column) {
                // запрет на редактирование ячейки адреса
                return column != 0;
            }
        };
        configTables();
        showDecMeanConfig();
        // Добавляем на главный контейнер панели
        JPanel editor = new JPanel(new BorderLayout());
        editor.add(panelFactory.configNorthPanel(mainWindow),BorderLayout.NORTH);
        editor.add(panelFactory.configCenterPanel(this), BorderLayout.CENTER);
        editor.add(panelFactory.configSouthPanel(mainWindow,this), BorderLayout.SOUTH);
        panelFactory.secondPanel.add(editor);
        panelFactory.mainPanel.add(panelFactory.secondPanel, "second");
        panelFactory.cardLayout.show(panelFactory.mainPanel, "second");

        mainWindow.revalidate();
        mainWindow.repaint();
    }
    public void configTables() throws IOException {
        //logger.info("configTables()");
        //тут данные с файла
        tableData = new TableData(fileDataTableModel);
        tableData.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                                                           boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

                boolean highlightFound = false;
                if (!highlightRanges.isEmpty()) {
                    for (List<Integer> range : highlightRanges) {
                        int sr = range.get(0), sc = range.get(1), er = range.get(2), ec = range.get(3);

                        //попадает ли текущая ячейка в диапазон
                        if (row >= sr && row <= er && column >= sc && column <= ec) {
                            c.setBackground(Color.CYAN);
                            c.setForeground(Color.BLACK);
                            highlightFound = true;
                            break;
                        }
                    }
                }

                // Если нет выделения, восстанавливаем обычные цвета
                if (!highlightFound) {
                    c.setBackground(isSelected ? table.getSelectionBackground() : Color.WHITE);
                    c.setForeground(isSelected ? table.getSelectionForeground() : Color.BLACK);
                }

                return c;
            }
        });
        editDataConfig();
        //тут адреса
        addressTable = new AddressTable(tableAddressModel);
        //тут значения в другой интерпретации
        meanByteTable = new MeanByteTable(meanTableModel);
    }
    public void showDecMeanConfig() {
        logger.info("showDecMeanConfig() :TableFactory");
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
    public void editDataConfig() {
       logger.info("editDataConfig() :TableFactory");
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
        SetAppKeywordAction.setupDeleteAction(tableData,dataManager,countByte);
        SetAppKeywordAction.setupCopyActionWithoutCut(tableData);
        SetAppKeywordAction.setupCutAction(tableData,  dataManager,
         countByte,panelFactory.secondPanel);
        SetAppKeywordAction.setupInsertWithChangeAction( tableData,  dataManager,
         countByte);
        SetAppKeywordAction.setupInsertWithoutChаngeAction( tableData,  dataManager,
         countByte, address,this);
    }
    public void addHighlightRange(int sr, int sc, int er, int ec) {
        highlightRanges.add(Arrays.asList(sr, sc, er, ec));
    }

    public void clearHighlightRanges() {
        highlightRanges.clear();
        tableData.repaint();
    }

}

