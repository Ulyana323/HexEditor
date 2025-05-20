package ru.khav.ProjectNIC.Controllers;

import lombok.AllArgsConstructor;
import ru.khav.ProjectNIC.MainWindow;
import ru.khav.ProjectNIC.models.MeanTableModel;
import ru.khav.ProjectNIC.utill.ButNames;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

@AllArgsConstructor
public class SimpleAction extends AbstractAction {

    MainWindow mainWindow;

    public void actionPerformed(ActionEvent e) {
        JButton btn = (JButton) e.getSource();
        System.out.println("Нажатие на кнопку <" + btn.getName() + ">");
        if (btn.getName().equalsIgnoreCase(ButNames.Exit.name())) {
            CardLayout cl = (CardLayout) (mainWindow.getMainPanel().getLayout());
            cl.show(mainWindow.getMainPanel(), "first");
            mainWindow.getCurrentByteData().clear();
            mainWindow.getCurrentIntData().clear();
            mainWindow.getCurrentStrData().clear();
            mainWindow.getFileDataTableModel().setRowCount(0);
            mainWindow.getFileDataTableModel().setColumnCount(0);
            MeanTableModel m = (MeanTableModel) mainWindow.getMeanByteTable().getModel();
            m.clear();
            mainWindow.getSecondPanel().removeAll();
        }
        if (btn.getName().equalsIgnoreCase(ButNames.Open.name())) {
            CardLayout cl = (CardLayout) (mainWindow.getMainPanel().getLayout());
            mainWindow.getSecondPanel().revalidate();
            mainWindow.getSecondPanel().repaint();
            cl.show(mainWindow.getMainPanel(), "second");
        }
        if (btn.getName().equalsIgnoreCase(ButNames.File.name())) {
            mainWindow.getFileChooser().setDialogTitle("Выбор директории");
            //только каталог
            mainWindow.getFileChooser().setFileSelectionMode(JFileChooser.FILES_ONLY);
            int result = mainWindow.getFileChooser().showOpenDialog(mainWindow);
            // Если директория выбрана, покажем ее в сообщении
            if (result == JFileChooser.APPROVE_OPTION) {
                System.out.println(mainWindow.getFileChooser().getSelectedFile().getPath());

                try {
                    mainWindow.dataloadInitial(mainWindow.getFileChooser().getSelectedFile().getPath());
                } catch (Exception ex) {
                    System.out.println(ex.getMessage());
                }
            }
        }
    }
}
