package ru.khav.ProjectNIC.UI_Components;


import lombok.Data;
import ru.khav.ProjectNIC.TableConfig.TableEditorConfigurer;
import ru.khav.ProjectNIC.TableConfig.TableHighlighter;
import ru.khav.ProjectNIC.TableConfig.TableModelFactory;
import ru.khav.ProjectNIC.TableConfig.TableViewFactory;
import ru.khav.ProjectNIC.models.MeanTableModel;
import ru.khav.ProjectNIC.models.DataManager;
import ru.khav.ProjectNIC.views.AddressTable;
import ru.khav.ProjectNIC.views.MainWindow;
import ru.khav.ProjectNIC.views.DataFromFileTable;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.IOException;
import java.text.ParseException;
import java.util.List;
import java.util.Vector;
import java.util.logging.Logger;

@Data
public class TableFactory {
    private final TableModelFactory modelFactory;
    private final TableViewFactory viewFactory;
    private final TableEditorConfigurer tableEditorConfigurer;
    private final TableHighlighter highlighter;

    private Logger logger = Logger.getLogger(TableFactory.class.getName());
    private DataFromFileTable dataFromFileTable;
    private JTable meanByteTable;
    private AddressTable addressTable;

    public TableFactory(TableModelFactory modelFactory, TableViewFactory viewFactory,
                        TableEditorConfigurer tableEditorConfigurer, TableHighlighter highlighter) {
        this.modelFactory = modelFactory;
        this.viewFactory = viewFactory;
        this.tableEditorConfigurer = tableEditorConfigurer;
        this.highlighter = highlighter;

    }

    public void createDynamicTable(List<String> dataToView, int colls, int rows, MainWindow mainWindow, PanelFactory panelFactory, DataManager dataManager) throws IOException, ParseException {
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

        DefaultTableModel fileDataTableModel = modelFactory.createFileDataTableModel(myData, columnNames);
        DefaultTableModel tableAddressModel = modelFactory.createAddressTableModel(addresses);

        dataFromFileTable = viewFactory.createDataTable(fileDataTableModel);
        dataFromFileTable.setDefaultRenderer(Object.class, highlighter.getCustomRenderer());
        //тут адреса
        addressTable = viewFactory.createAddressTable(tableAddressModel);
        //тут значения в другой интерпретации
        meanByteTable = viewFactory.createMeanByteTable(new MeanTableModel());

        tableEditorConfigurer.EditDataInMeanTable(dataFromFileTable, dataManager, meanByteTable);
        tableEditorConfigurer.editDataInMainTable(dataFromFileTable, dataManager, panelFactory, this);
        // Добавляем на главный контейнер панели
        JPanel editor = new JPanel(new BorderLayout());
        editor.add(panelFactory.getNorthPanelBuilder().build(mainWindow), BorderLayout.NORTH);
        editor.add(panelFactory.getCenterPanelBuilder().build(mainWindow), BorderLayout.CENTER);
        editor.add(panelFactory.getSouthPanelBuilder().build(mainWindow), BorderLayout.SOUTH);
        panelFactory.secondPanel.add(editor);
        panelFactory.mainPanel.add(panelFactory.secondPanel, "second");
        panelFactory.cardLayout.show(panelFactory.mainPanel, "second");

        mainWindow.revalidate();
        mainWindow.repaint();
    }

    public void addHighlightRange(int sr, int sc, int er, int ec) {
        highlighter.addHighlightRange(sr, sc, er, ec);
    }

    public void clearHighlightRanges() {
        highlighter.clearHighlightRanges();
        dataFromFileTable.repaint();
    }

}

