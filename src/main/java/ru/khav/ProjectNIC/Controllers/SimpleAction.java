package ru.khav.ProjectNIC.Controllers;

import lombok.AllArgsConstructor;
import ru.khav.ProjectNIC.UI_Components.PanelFactory;
import ru.khav.ProjectNIC.UI_Components.TableFactory;
import ru.khav.ProjectNIC.utill.DataLoaderToTables;
import ru.khav.ProjectNIC.utill.DataManager;
import ru.khav.ProjectNIC.views.MainWindow;
import ru.khav.ProjectNIC.models.MeanTableModel;
import ru.khav.ProjectNIC.utill.ButtonNames;


import javax.swing.*;
import java.awt.CardLayout;
import java.awt.event.ActionEvent;
import java.util.logging.Logger;

import static javax.swing.JOptionPane.ERROR_MESSAGE;
import static javax.swing.JOptionPane.INFORMATION_MESSAGE;

@AllArgsConstructor
public class SimpleAction extends AbstractAction {

    MainWindow mainWindow;
    TableFactory tableFactory;
    DataManager dataManager;
    PanelFactory panelFactory;
    DataLoaderToTables dataLoaderToTables;

    public SimpleAction(MainWindow mainWindow)
    {
        this.mainWindow=mainWindow;
        panelFactory=mainWindow.getPanelFactory();
        dataManager=mainWindow.getDataManager();
        tableFactory=mainWindow.getTableFactory();
        dataLoaderToTables=mainWindow.getDataLoaderToTables();
    }

    private Logger logger = Logger.getLogger(SimpleAction.class.getName());

    public void actionPerformed(ActionEvent e) {


        JButton btn = (JButton) e.getSource();
       logger.info("Ты нажал <" + btn.getName() + ">");
        if (btn.getName().equalsIgnoreCase(ButtonNames.Exit.name())) {
            CardLayout cl = (CardLayout) (mainWindow.getPanelFactory().getMainPanel().getLayout());
            cl.show(mainWindow.getPanelFactory().getMainPanel(), "first");
            dataManager.getCurrentByteData().clear();
            dataManager.getCurrentIntData().clear();
            dataManager.getCurrentStrData().clear();
            tableFactory.getFileDataTableModel().setRowCount(0);
            tableFactory.getFileDataTableModel().setColumnCount(0);
            MeanTableModel m = (MeanTableModel) tableFactory.getMeanByteTable().getModel();
            m.clear();
            panelFactory.getSecondPanel().removeAll();
        }
        if (btn.getName().equalsIgnoreCase(ButtonNames.Open.name())) {
            CardLayout cl = (CardLayout) (panelFactory.getMainPanel().getLayout());
            panelFactory.getSecondPanel().revalidate();
            panelFactory.getSecondPanel().repaint();
            cl.show(panelFactory.getMainPanel(), "second");
        }
        if (btn.getName().equalsIgnoreCase(ButtonNames.File.name())) {
            panelFactory.getFileChooser().setDialogTitle("Выбор директории");
            //только каталог
            panelFactory.getFileChooser().setFileSelectionMode(JFileChooser.FILES_ONLY);
            int result = panelFactory.getFileChooser().showOpenDialog(mainWindow);
            // Если директория выбрана, покажем ее в сообщении
            if (result == JFileChooser.APPROVE_OPTION) {
                logger.info("Opened: " +panelFactory.getFileChooser().getSelectedFile().getPath());

                try {
                    dataLoaderToTables.dataloadInitial(panelFactory.getFileChooser().getSelectedFile().getPath(),mainWindow);
                    JOptionPane.showMessageDialog(mainWindow,"Файл открылся корректно",
                            "Приветствую, все хорошо!", INFORMATION_MESSAGE);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(mainWindow,"Ошибка открытия файла",
                    "Ой",ERROR_MESSAGE);
                    logger.severe(ex.getMessage());
                }
            }
        }
    }
}
