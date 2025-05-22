package ru.khav.ProjectNIC.Controllers;

import ru.khav.ProjectNIC.Enums.ButtonNames;
import ru.khav.ProjectNIC.Services.PageNavigateService;
import ru.khav.ProjectNIC.Services.TableScaleService;
import ru.khav.ProjectNIC.UI_Components.PanelFactory;
import ru.khav.ProjectNIC.UI_Components.TableFactory;
import ru.khav.ProjectNIC.models.DataManager;
import ru.khav.ProjectNIC.utill.Globals;
import ru.khav.ProjectNIC.views.MainWindow;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.text.ParseException;


public class ChangeTableScale extends AbstractAction {
    MainWindow mainWindow;
    TableFactory tableFactory;
    DataManager dataManager;
    PanelFactory panelFactory;


    public ChangeTableScale(MainWindow mainWindow) {
        this.mainWindow = mainWindow;
        panelFactory = mainWindow.getPanelFactory();
        dataManager = mainWindow.getDataManager();
        tableFactory = mainWindow.getTableFactory();

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        JButton btn = (JButton) e.getSource();
        System.out.println("Нажатие на кнопку <" + btn.getName() + ">");

        if (btn.getName().equalsIgnoreCase(ButtonNames.AddRow.name())) {
            ((DefaultTableModel) tableFactory.getDataFromFileTable().getModel()).addRow(new Object[]{Globals.address++});
            TableScaleService.changeScaleDataTable(dataManager.getCurrentStrData(), Globals.countByte, Globals.address, tableFactory);
        }
        if (btn.getName().equalsIgnoreCase(ButtonNames.AddColumn.name())) {
            ((DefaultTableModel) tableFactory.getDataFromFileTable().getModel()).addColumn(Globals.countByte++);
            TableScaleService.changeScaleDataTable(dataManager.getCurrentStrData(), Globals.countByte, Globals.address, tableFactory);
        }
        if (btn.getName().equalsIgnoreCase(ButtonNames.DelRow.name())) {
            if ((tableFactory.getDataFromFileTable().getModel()).getRowCount() > 0) {
                ((DefaultTableModel) tableFactory.getDataFromFileTable().getModel()).removeRow(Globals.address - 2);
                Globals.address--;
                TableScaleService.changeScaleDataTable(dataManager.getCurrentStrData(), Globals.countByte, Globals.address, tableFactory);
            }
        }
        if (btn.getName().equalsIgnoreCase(ButtonNames.DelColumn.name())) {
            if ((tableFactory.getDataFromFileTable().getModel()).getColumnCount() > 1) {
                ((DefaultTableModel) tableFactory.getDataFromFileTable().getModel()).setColumnCount(Globals.countByte - 1);
                Globals.countByte--;
                TableScaleService.changeScaleDataTable(dataManager.getCurrentStrData(), Globals.countByte, Globals.address, tableFactory);
            }
        }
        if (btn.getName().equalsIgnoreCase(ButtonNames.UpPage.name())) {
            try {
                PageNavigateService.dataloadWhenChangePageUp(mainWindow, dataManager, tableFactory);
            } catch (IOException | ParseException ex) {
                throw new RuntimeException(ex);
            }
        }
        if (btn.getName().equalsIgnoreCase(ButtonNames.DownPage.name())) {
            try {
                PageNavigateService.dataloadWhenChangePageDown(mainWindow, dataManager, tableFactory);
            } catch (IOException | ParseException ex) {
                throw new RuntimeException(ex);
            }

        }
    }
}
