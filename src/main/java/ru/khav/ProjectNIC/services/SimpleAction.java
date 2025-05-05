package ru.khav.ProjectNIC.services;

import ru.khav.ProjectNIC.MainWindow;
import ru.khav.ProjectNIC.models.MeanTableModel;
import ru.khav.ProjectNIC.utill.LoadDataFromFile;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.IOException;

public class SimpleAction extends AbstractAction {

    MainWindow mainWindow;
    public SimpleAction(MainWindow mainWindow)
    {
        this.mainWindow=mainWindow;
    }
    private static final long serialVersionUID = 1L;

    // Обработка события нажатия на кнопку
    public void actionPerformed(ActionEvent e) {
        JButton btn = (JButton) e.getSource();
        System.out.println("Нажатие на кнопку <" + btn.getName() + ">");

        if (btn.getName().equalsIgnoreCase("addrow")) {
            mainWindow.getFileDataTableModel().addRow(new Object[]{mainWindow.upAddress()});
            mainWindow.changeScaleDataTable(mainWindow.getCurrentStrData(), mainWindow.getCountBytee(), mainWindow.getAddresss());
        }
        if (btn.getName().equalsIgnoreCase("addcol")) {
            mainWindow.getFileDataTableModel().addColumn(mainWindow.upCountByte());
            mainWindow.changeScaleDataTable(mainWindow.getCurrentStrData(), mainWindow.getCountBytee(), mainWindow.getAddresss());
        }
        if (btn.getName().equalsIgnoreCase("delrow")) {
            if (mainWindow.getFileDataTableModel().getRowCount() > 0) {
                mainWindow.getFileDataTableModel().removeRow(mainWindow.getAddresss() - 2);
                mainWindow.downAddress();
                mainWindow.changeScaleDataTable(mainWindow.getCurrentStrData(),mainWindow.getCountBytee(), mainWindow.getAddresss());
            }
        }
        if (btn.getName().equalsIgnoreCase("delcol")) {
            if( mainWindow.getFileDataTableModel().getColumnCount() > 1) {
                mainWindow.getFileDataTableModel().setColumnCount(mainWindow.getCountBytee() - 1);
                mainWindow.downCountByte();
                mainWindow.changeScaleDataTable(mainWindow.getCurrentStrData(), mainWindow.getCountBytee(), mainWindow.getAddresss());
            }
        }
        if (btn.getName().equalsIgnoreCase("exitBut")) {
            CardLayout cl = (CardLayout) (mainWindow.getMainPanel().getLayout());
            cl.show(mainWindow.getMainPanel(), "first");
           mainWindow.clear();
        }
        if (btn.getName().equalsIgnoreCase("openBut")) {
            CardLayout cl = (CardLayout) (mainWindow.getMainPanel().getLayout());
            mainWindow.getSecondPanel().revalidate();
            mainWindow.getSecondPanel().repaint();
            cl.show(mainWindow.getMainPanel(), "second");
        }
        if (btn.getName().equalsIgnoreCase("File")) {
            mainWindow.getFileChooser().setDialogTitle("Выбор директории");
            //только каталог
            mainWindow.getFileChooser().setFileSelectionMode(JFileChooser.FILES_ONLY);
            int result = mainWindow.getFileChooser().showOpenDialog(mainWindow);
            // Если директория выбрана, покажем ее в сообщении
            if (result == JFileChooser.APPROVE_OPTION) {
                System.out.println(mainWindow.getFileChooser().getSelectedFile().getPath());
                //createDynamicTable(array, countByte, address);}

                try {
                    mainWindow.dataload(mainWindow.getFileChooser().getSelectedFile().getPath());
                } catch (Exception ex) {
                    System.out.println(ex.getMessage());
                }

            }

        }
        if (btn.getName().equalsIgnoreCase("upPage")){
            try {
                mainWindow.dataloadNext();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        }
        if (btn.getName().equalsIgnoreCase("downPage")){
            try {
                mainWindow.dataloadPrev();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        }

    }
}
