package ru.khav.ProjectNIC.Panel;

import ru.khav.ProjectNIC.Controllers.ChangeTableScale;
import ru.khav.ProjectNIC.Controllers.SimpleAction;
import ru.khav.ProjectNIC.Enums.ButtonNames;
import ru.khav.ProjectNIC.Interfaces.PanelBuilder;
import ru.khav.ProjectNIC.UI_Components.TableFactory;
import ru.khav.ProjectNIC.views.MainWindow;

import javax.swing.*;
import java.awt.*;

public class SouthPanelBuilder implements PanelBuilder {
    private final TableFactory tableFactory;

    public SouthPanelBuilder(TableFactory tableFactory) {
        this.tableFactory = tableFactory;
    }

    @Override
    public JPanel build(MainWindow mainWindow) {
        //logger.info("configSouthPanel() :PanelFactory");
        JPanel tableButtonPanel = new JPanel(new GridLayout(2, 4, 5, 5));

        JButton addRowButton = new JButton("add row");
        addRowButton.setName(ButtonNames.AddRow.name());
        addRowButton.setToolTipText("Добавить строку");
        addRowButton.addActionListener(new ChangeTableScale(mainWindow));

        JButton addColumnButton = new JButton("add column");
        addColumnButton.setName(ButtonNames.AddColumn.name());
        addColumnButton.setToolTipText("Добавить столбец");
        addColumnButton.addActionListener(new ChangeTableScale(mainWindow));

        JButton delRowButton = new JButton("del row");
        delRowButton.setName(ButtonNames.DelRow.name());
        delRowButton.setToolTipText("Удалить строку");
        delRowButton.addActionListener(new ChangeTableScale(mainWindow));

        JButton delColumnButton = new JButton("del column");
        delColumnButton.setName(ButtonNames.DelColumn.name());
        delColumnButton.setToolTipText("Удалить столбец");
        delColumnButton.addActionListener(new ChangeTableScale(mainWindow));

        JButton exitBut = new JButton("Exit");
        exitBut.setName(ButtonNames.Exit.name());
        exitBut.addActionListener(new SimpleAction(mainWindow));

        JButton up = new JButton("upPage");
        up.setName(ButtonNames.UpPage.name());
        up.addActionListener(new ChangeTableScale(mainWindow));

        JButton down = new JButton("downPage");
        down.setName(ButtonNames.DownPage.name());
        down.addActionListener(new ChangeTableScale(mainWindow));


        tableButtonPanel.add(addRowButton);
        tableButtonPanel.add(addColumnButton);
        tableButtonPanel.add(delColumnButton);
        tableButtonPanel.add(delRowButton);
        tableButtonPanel.add(up);
        tableButtonPanel.add(down);


        // панель с десят знач
        JPanel decNote = new JPanel(new BorderLayout());
        decNote.setBackground(Color.GREEN);
        //без скролпэйн не отобразятся заголовки
        JScrollPane jScrollMean = new JScrollPane(tableFactory.getMeanByteTable());


        decNote.add(jScrollMean, BorderLayout.CENTER);
        decNote.setPreferredSize(new Dimension(500, 153));
        decNote.setBorder(BorderFactory.createLineBorder(Color.BLACK));

        //нижняяя панель с десятичными значениями и управлением таблицы с кнопками
        JPanel southPanel = new JPanel(new BorderLayout());
        southPanel.add(tableButtonPanel, BorderLayout.NORTH);
        // в decNote значения в десятичном виде
        southPanel.add(decNote, BorderLayout.CENTER);
        JPanel south0 = new JPanel(new BorderLayout());
        JPanel south1 = new JPanel(new BorderLayout());
        JPanel south2 = new JPanel(new BorderLayout());
        southPanel.add(south0, BorderLayout.SOUTH);
        south0.setBackground(Color.BLACK);
        south2.add(exitBut);
        south0.add(south2, BorderLayout.NORTH);
        south0.add(south1, BorderLayout.SOUTH);

        return southPanel;

    }
}
