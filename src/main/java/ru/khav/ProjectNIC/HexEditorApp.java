package ru.khav.ProjectNIC;

import ru.khav.ProjectNIC.PanelConfig.CenterPanelBuilder;
import ru.khav.ProjectNIC.PanelConfig.NorthPanelBuilder;
import ru.khav.ProjectNIC.PanelConfig.SouthPanelBuilder;
import ru.khav.ProjectNIC.TableConfig.TableEditorConfigurer;
import ru.khav.ProjectNIC.TableConfig.TableHighlighter;
import ru.khav.ProjectNIC.TableConfig.TableModelFactory;
import ru.khav.ProjectNIC.TableConfig.TableViewFactory;
import ru.khav.ProjectNIC.UI_Components.PanelFactory;
import ru.khav.ProjectNIC.UI_Components.TableFactory;
import ru.khav.ProjectNIC.models.DataManager;
import ru.khav.ProjectNIC.views.MainWindow;

import javax.swing.*;

public class HexEditorApp {
    public static void main(String[] args) {

        final TableModelFactory modelFactory = new TableModelFactory();
        final TableViewFactory viewFactory = new TableViewFactory();
        final TableEditorConfigurer tableEditorConfigurer = new TableEditorConfigurer();
        final TableHighlighter highlighter = new TableHighlighter();
        final TableFactory tableFactory = new TableFactory(modelFactory, viewFactory,
                tableEditorConfigurer, highlighter);
        final NorthPanelBuilder northPanelBuilder = new NorthPanelBuilder();
        final CenterPanelBuilder centerPanelBuilder = new CenterPanelBuilder(tableFactory);
        final SouthPanelBuilder southPanelBuilder = new SouthPanelBuilder(tableFactory);
        final PanelFactory panelFactory = new PanelFactory(northPanelBuilder, southPanelBuilder,
                centerPanelBuilder);

        SwingUtilities.invokeLater(() ->
                new MainWindow(new DataManager(), panelFactory, tableFactory));
    }
}
