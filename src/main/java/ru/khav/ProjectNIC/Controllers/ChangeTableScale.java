package ru.khav.ProjectNIC.Controllers;

import lombok.AllArgsConstructor;
import ru.khav.ProjectNIC.MainWindow;
import ru.khav.ProjectNIC.utill.ButtonNames;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.text.ParseException;

@AllArgsConstructor
public class ChangeTableScale extends AbstractAction {
    MainWindow mainWindow;

    @Override
    public void actionPerformed(ActionEvent e) {
        JButton btn = (JButton) e.getSource();
        System.out.println("Нажатие на кнопку <" + btn.getName() + ">");

        if (btn.getName().equalsIgnoreCase(ButtonNames.AddRow.name())) {
            mainWindow.getFileDataTableModel().addRow(new Object[]{mainWindow.upAddress()});
            mainWindow.changeScaleDataTable(mainWindow.getCurrentStrData(), MainWindow.getCountBytee(), MainWindow.getAddresss());
        }
        if (btn.getName().equalsIgnoreCase(ButtonNames.AddColumn.name())) {
            mainWindow.getFileDataTableModel().addColumn(mainWindow.upCountByte());
            mainWindow.changeScaleDataTable(mainWindow.getCurrentStrData(), MainWindow.getCountBytee(), MainWindow.getAddresss());
        }
        if (btn.getName().equalsIgnoreCase(ButtonNames.DelRow.name())) {
            if (mainWindow.getFileDataTableModel().getRowCount() > 0) {
                mainWindow.getFileDataTableModel().removeRow(MainWindow.getAddresss() - 2);
                mainWindow.downAddress();
                mainWindow.changeScaleDataTable(mainWindow.getCurrentStrData(), MainWindow.getCountBytee(), MainWindow.getAddresss());
            }
        }
        if (btn.getName().equalsIgnoreCase(ButtonNames.DelColumn.name())) {
            if (mainWindow.getFileDataTableModel().getColumnCount() > 1) {
                mainWindow.getFileDataTableModel().setColumnCount(MainWindow.getCountBytee() - 1);
                mainWindow.downCountByte();
                mainWindow.changeScaleDataTable(mainWindow.getCurrentStrData(), MainWindow.getCountBytee(), MainWindow.getAddresss());
            }
        }
        if (btn.getName().equalsIgnoreCase(ButtonNames.UpPage.name())) {
            try {
                mainWindow.dataloadWhenChangePageUp();
            } catch (IOException | ParseException ex) {
                throw new RuntimeException(ex);
            }
        }
        if (btn.getName().equalsIgnoreCase(ButtonNames.DownPage.name())) {
            try {
                mainWindow.dataloadWhenChangePageDown();
            } catch (IOException | ParseException ex) {
                throw new RuntimeException(ex);
            }

        }
    }
}
