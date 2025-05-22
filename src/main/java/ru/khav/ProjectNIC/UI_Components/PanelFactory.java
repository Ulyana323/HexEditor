
package ru.khav.ProjectNIC.UI_Components;

import lombok.Data;
import ru.khav.ProjectNIC.Controllers.SimpleAction;
import ru.khav.ProjectNIC.Enums.ButtonNames;
import ru.khav.ProjectNIC.Panel.CenterPanelBuilder;
import ru.khav.ProjectNIC.Panel.NorthPanelBuilder;
import ru.khav.ProjectNIC.Panel.SouthPanelBuilder;
import ru.khav.ProjectNIC.views.MainWindow;


import javax.swing.*;
import java.awt.*;
import java.util.logging.Logger;

@Data
public class PanelFactory  {
    Logger logger= Logger.getLogger(PanelFactory.class.getName());
    CardLayout cardLayout = new CardLayout();
    JPanel mainPanel = new JPanel(cardLayout);
    JPanel firstPanel = new JPanel(new BorderLayout());
    JPanel secondPanel = new JPanel(new BorderLayout());
    private JFileChooser fileChooser = null;

    private final NorthPanelBuilder northPanelBuilder;
    private final SouthPanelBuilder southPanelBuilder;
    private final CenterPanelBuilder centerPanelBuilder;
    public void startWindow(MainWindow mainWindow) {
        logger.info("startWindow :PanelFactory");
        // Создание экземпляра JFileChooser
        fileChooser = new JFileChooser();
        firstPanel.setBackground(Color.CYAN);
        JButton jButton = new JButton("File");
        JButton openBut = new JButton("Open it");
        openBut.setName(ButtonNames.Open.name());
        jButton.setName(ButtonNames.File.name());
        firstPanel.add(openBut);
        firstPanel.add(jButton);
        jButton.addActionListener(new SimpleAction(mainWindow));
        mainPanel.add(firstPanel, "first");

        mainWindow.revalidate();
        mainWindow.repaint();
    }
}


