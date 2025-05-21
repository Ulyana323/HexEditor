package ru.khav.ProjectNIC.views;

import lombok.Data;
import ru.khav.ProjectNIC.UI_Components.PanelFactory;
import ru.khav.ProjectNIC.UI_Components.TableFactory;
import ru.khav.ProjectNIC.utill.DataLoaderToTables;
import ru.khav.ProjectNIC.utill.DataManager;

import javax.swing.*;
import java.awt.*;
import java.util.logging.Logger;

@Data
public class MainWindow extends JFrame {
    private final DataManager dataManager;
    private final PanelFactory panelFactory;
    private final TableFactory tableFactory;
    private final DataLoaderToTables dataLoaderToTables;
    Container container = getContentPane();
    CardLayout cardLayout = new CardLayout();
    private Logger logger = Logger.getLogger(MainWindow.class.getName());

    public MainWindow(DataManager dataManager, PanelFactory panelFactory, TableFactory tableFactory, DataLoaderToTables dataLoaderToTables) {
        this.dataManager = dataManager;
        this.panelFactory = panelFactory;
        this.tableFactory = tableFactory;
        this.dataLoaderToTables = dataLoaderToTables;
        logger.info("Mainwinow()");
        setSize(4000, 2000);
        setResizable(false);
        setTitle("Ulyana's HexEditor");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setPreferredSize(new Dimension(500, 600));
        setLocation(200, 100); // Устанавливаем позицию окна
        configContainer();
        panelFactory.startWindow(this);
        revalidate();
        repaint();
        pack();
        setVisible(true);
    }

    public void configContainer() {
        logger.info("configContainer()");
        container.setLayout(new BorderLayout());
        container.add(panelFactory.getMainPanel(), BorderLayout.CENTER);
        container.setBackground(Color.black);
    }


}