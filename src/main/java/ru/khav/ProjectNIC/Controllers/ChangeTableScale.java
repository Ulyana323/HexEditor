package ru.khav.ProjectNIC.Controllers;

import ru.khav.ProjectNIC.Enums.ButtonNames;
import ru.khav.ProjectNIC.UI_Components.PanelFactory;
import ru.khav.ProjectNIC.UI_Components.TableFactory;
import ru.khav.ProjectNIC.utill.*;
import ru.khav.ProjectNIC.views.MainWindow;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.text.ParseException;


public class ChangeTableScale extends AbstractAction {
    MainWindow mainWindow;
    TableFactory tableFactory;
    DataManager dataManager;
    PanelFactory panelFactory;
    DataLoaderToTables dataLoaderToTables;

    public ChangeTableScale(MainWindow mainWindow) {
        this.mainWindow = mainWindow;
        panelFactory = mainWindow.getPanelFactory();
        dataManager = mainWindow.getDataManager();
        tableFactory = mainWindow.getTableFactory();
        dataLoaderToTables=mainWindow.getDataLoaderToTables();

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        JButton btn = (JButton) e.getSource();
        System.out.println("Нажатие на кнопку <" + btn.getName() + ">");

        if (btn.getName().equalsIgnoreCase(ButtonNames.AddRow.name())) {
            tableFactory.getFileDataTableModel().addRow(new Object[]{Globals.address++});
            TableScaleService.changeScaleDataTable(dataManager.getCurrentStrData(), Globals.countByte, Globals.address,tableFactory);
        }
        if (btn.getName().equalsIgnoreCase(ButtonNames.AddColumn.name())) {
            tableFactory.getFileDataTableModel().addColumn(Globals.countByte++);
            TableScaleService.changeScaleDataTable(dataManager.getCurrentStrData(), Globals.countByte, Globals.address,tableFactory);
        }
        if (btn.getName().equalsIgnoreCase(ButtonNames.DelRow.name())) {
            if (tableFactory.getFileDataTableModel().getRowCount() > 0) {
                tableFactory.getFileDataTableModel().removeRow(Globals.address - 2);
                Globals.address--;
                TableScaleService.changeScaleDataTable(dataManager.getCurrentStrData(), Globals.countByte, Globals.address,tableFactory);
            }
        }
        if (btn.getName().equalsIgnoreCase(ButtonNames.DelColumn.name())) {
            if (tableFactory.getFileDataTableModel().getColumnCount() > 1) {
                tableFactory.getFileDataTableModel().setColumnCount(Globals.countByte - 1);
                Globals.countByte--;
                TableScaleService.changeScaleDataTable(dataManager.getCurrentStrData(), Globals.countByte, Globals.address,tableFactory);
            }
        }
        if (btn.getName().equalsIgnoreCase(ButtonNames.UpPage.name())) {
            try {
                dataLoaderToTables.dataloadWhenChangePageUp(mainWindow);
            } catch (IOException | ParseException ex) {
                throw new RuntimeException(ex);
            }
        }
        if (btn.getName().equalsIgnoreCase(ButtonNames.DownPage.name())) {
            try {
                dataLoaderToTables.dataloadWhenChangePageDown(mainWindow);
            } catch (IOException | ParseException ex) {
                throw new RuntimeException(ex);
            }

        }
    }
}
