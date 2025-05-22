package ru.khav.ProjectNIC.Panel;

import ru.khav.ProjectNIC.Controllers.SearchSeq;
import ru.khav.ProjectNIC.Enums.ButtonNames;
import ru.khav.ProjectNIC.Interfaces.PanelBuilder;
import ru.khav.ProjectNIC.views.MainWindow;

import javax.swing.*;
import javax.swing.text.MaskFormatter;
import java.awt.*;

public class NorthPanelBuilder implements PanelBuilder {
    JFormattedTextField searchSeq;
    @Override
    public JPanel build(MainWindow mainWindow) {
      //  logger.info("configNorthPanel(): PanelFactory");
        JPanel northPanel = new JPanel(new BorderLayout());
        try {
            MaskFormatter m = new MaskFormatter("HH-HH-HH");
            m.setPlaceholderCharacter('0');

            searchSeq = new JFormattedTextField(m);
            searchSeq.setColumns(16);

            JButton toSearch = new JButton("search");
            toSearch.setName(ButtonNames.Search.name());
            toSearch.addActionListener(new SearchSeq(mainWindow));

            JButton info = new JButton("I");
            info.setName(ButtonNames.Info.name());
            info.addActionListener(new SearchSeq(mainWindow));

            JButton toDelColor = new JButton("delete hightlights");
            toDelColor.setName(ButtonNames.DelHighlights.name());
            toDelColor.addActionListener(new SearchSeq(mainWindow));


            JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
            searchPanel.add(searchSeq);
            searchPanel.add(toSearch);
            searchPanel.add(toDelColor);
            searchPanel.add(info);

            northPanel.add(searchPanel, BorderLayout.WEST);
        } catch (Exception e) {
           // logger.severe(e.getMessage());
        }
        return northPanel;
    }
}
