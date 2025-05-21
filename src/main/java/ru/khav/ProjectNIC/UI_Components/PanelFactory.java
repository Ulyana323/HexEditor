
package ru.khav.ProjectNIC.UI_Components;

import lombok.Data;
import ru.khav.ProjectNIC.Controllers.ChangeTableScale;
import ru.khav.ProjectNIC.Controllers.SearchSeq;
import ru.khav.ProjectNIC.Controllers.SimpleAction;
import ru.khav.ProjectNIC.utill.ButtonNames;
import ru.khav.ProjectNIC.views.MainWindow;


import javax.swing.*;
import javax.swing.text.MaskFormatter;
import java.awt.*;
import java.text.ParseException;
import java.util.logging.Logger;

@Data
public class PanelFactory  {

    Logger logger= Logger.getLogger(PanelFactory.class.getName());
    CardLayout cardLayout = new CardLayout();
    JPanel mainPanel = new JPanel(cardLayout);
    JPanel firstPanel = new JPanel(new BorderLayout());
    JPanel secondPanel = new JPanel(new BorderLayout());
    JFormattedTextField searchSeq;
    private JFileChooser fileChooser = null;

    public JPanel configNorthPanel(MainWindow mainWindow) throws ParseException {
        logger.info("configNorthPanel(): PanelFactory");
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
            logger.severe(e.getMessage());
        }
        return northPanel;
    }
    public JPanel configCenterPanel(TableFactory tableFactory) {
        logger.info("configCenterPanel(): PanelFactory");
        for (int i = 0; i < tableFactory.getTableData().getColumnCount(); i++) {
            tableFactory.getTableData().getColumnModel().getColumn(i).setPreferredWidth(80);
        }
        // Прокручиваемая панель с таблицей
        JScrollPane scrollPane = new JScrollPane(tableFactory.getTableData(),
                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        JScrollPane scrollPane1 = new JScrollPane(tableFactory.getAddressTable(),
                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

        // Привязываем вертикальный скроллбар
        JScrollBar verticalScrollBar = scrollPane.getVerticalScrollBar();
        scrollPane1.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);

        // слушаем прокрутку основной таблицы
        verticalScrollBar.addAdjustmentListener(e -> {
            scrollPane1.getVerticalScrollBar().setValue(e.getValue());
        });

        scrollPane1.setPreferredSize(new Dimension(100, scrollPane.getPreferredSize().height));

        JPanel panelWithTables = new JPanel();
        panelWithTables.setLayout(new BoxLayout(panelWithTables, BoxLayout.X_AXIS));
        panelWithTables.add(scrollPane1); //сначала адреса
        panelWithTables.add(scrollPane);  //потом данные

        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.add(panelWithTables, BorderLayout.CENTER);
        return centerPanel;
    }
    public JPanel configSouthPanel(MainWindow mainWindow,TableFactory tableFactory) {
        logger.info("configSouthPanel() :PanelFactory");
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


