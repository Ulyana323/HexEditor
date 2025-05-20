package ru.khav.ProjectNIC.Controllers;

import lombok.AllArgsConstructor;
import ru.khav.ProjectNIC.MainWindow;
import ru.khav.ProjectNIC.models.MeanTableModel;
import ru.khav.ProjectNIC.utill.ButtonNames;


import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.AbstractAction;
import java.awt.CardLayout;
import java.awt.event.ActionEvent;
import java.util.logging.Logger;

@AllArgsConstructor
public class SimpleAction extends AbstractAction {

    MainWindow mainWindow;
    public SimpleAction(MainWindow mainWindow)
    {
        this.mainWindow=mainWindow;
    }
    private Logger logger = Logger.getLogger(SimpleAction.class.getName());

    public void actionPerformed(ActionEvent e) {
        JButton btn = (JButton) e.getSource();
       logger.info("Ты нажал <" + btn.getName() + ">");
        if (btn.getName().equalsIgnoreCase(ButtonNames.Exit.name())) {
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
        if (btn.getName().equalsIgnoreCase(ButtonNames.Open.name())) {
            CardLayout cl = (CardLayout) (mainWindow.getMainPanel().getLayout());
            mainWindow.getSecondPanel().revalidate();
            mainWindow.getSecondPanel().repaint();
            cl.show(mainWindow.getMainPanel(), "second");
        }
        if (btn.getName().equalsIgnoreCase(ButtonNames.File.name())) {
            mainWindow.getFileChooser().setDialogTitle("Выбор директории");
            //только каталог
            mainWindow.getFileChooser().setFileSelectionMode(JFileChooser.FILES_ONLY);
            int result = mainWindow.getFileChooser().showOpenDialog(mainWindow);
            // Если директория выбрана, покажем ее в сообщении
            if (result == JFileChooser.APPROVE_OPTION) {
                logger.info("Opened: " +mainWindow.getFileChooser().getSelectedFile().getPath());

                try {
                    mainWindow.dataloadInitial(mainWindow.getFileChooser().getSelectedFile().getPath());
                } catch (Exception ex) {

                    logger.severe(ex.getMessage());
                }
            }
        }
    }
}
