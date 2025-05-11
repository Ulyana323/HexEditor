package ru.khav.ProjectNIC.Controllers;

import lombok.AllArgsConstructor;
import ru.khav.ProjectNIC.MainWindow;
import ru.khav.ProjectNIC.utill.ButNames;

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

        if (btn.getName().equalsIgnoreCase(ButNames.AddRow.name())) {
            mainWindow.getFileDataTableModel().addRow(new Object[]{mainWindow.upAddress()});
            mainWindow.changeScaleDataTable(mainWindow.getCurrentStrData(), MainWindow.getCountBytee(), MainWindow.getAddresss());
        }
        if (btn.getName().equalsIgnoreCase(ButNames.AddColumn.name())) {
            mainWindow.getFileDataTableModel().addColumn(mainWindow.upCountByte());
            mainWindow.changeScaleDataTable(mainWindow.getCurrentStrData(), MainWindow.getCountBytee(), MainWindow.getAddresss());
        }
        if (btn.getName().equalsIgnoreCase(ButNames.DelRow.name())) {
            if (mainWindow.getFileDataTableModel().getRowCount() > 0) {
                mainWindow.getFileDataTableModel().removeRow(MainWindow.getAddresss() - 2);
                mainWindow.downAddress();
                mainWindow.changeScaleDataTable(mainWindow.getCurrentStrData(), MainWindow.getCountBytee(), MainWindow.getAddresss());
            }
        }
        if (btn.getName().equalsIgnoreCase(ButNames.DelColumn.name())) {
            if (mainWindow.getFileDataTableModel().getColumnCount() > 1) {
                mainWindow.getFileDataTableModel().setColumnCount(MainWindow.getCountBytee() - 1);
                mainWindow.downCountByte();
                mainWindow.changeScaleDataTable(mainWindow.getCurrentStrData(), MainWindow.getCountBytee(), MainWindow.getAddresss());
            }
        }
        if (btn.getName().equalsIgnoreCase(ButNames.UpPage.name())) {
            try {
                mainWindow.dataload2();
            } catch (IOException | ParseException ex) {
                throw new RuntimeException(ex);
            }
        }
        if (btn.getName().equalsIgnoreCase(ButNames.DownPage.name())) {
            try {
                mainWindow.dataload3();
            } catch (IOException | ParseException ex) {
                throw new RuntimeException(ex);
            }

        }
    }
}
