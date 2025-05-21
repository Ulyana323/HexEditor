package ru.khav.ProjectNIC;

import ru.khav.ProjectNIC.UI_Components.PanelFactory;
import ru.khav.ProjectNIC.UI_Components.TableFactory;
import ru.khav.ProjectNIC.utill.DataLoaderToTables;
import ru.khav.ProjectNIC.utill.DataManager;
import ru.khav.ProjectNIC.views.MainWindow;

import javax.swing.*;

public class HexEditorApp {
    public static void main(String[] args) {
        final PanelFactory panelFactory = new PanelFactory();
        final DataManager dataManager = new DataManager();
        final TableFactory tableFactory = new TableFactory(dataManager, panelFactory);
        final DataLoaderToTables dataLoaderToTables =
                new DataLoaderToTables(dataManager,
                      tableFactory );
        SwingUtilities.invokeLater(() ->
                new MainWindow(dataManager, panelFactory, tableFactory, dataLoaderToTables));
    }
}
