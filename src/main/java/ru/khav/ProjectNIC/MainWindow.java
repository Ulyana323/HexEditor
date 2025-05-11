package ru.khav.ProjectNIC;

import lombok.Data;
import ru.khav.ProjectNIC.Controllers.ChangeTableScale;
import ru.khav.ProjectNIC.Controllers.SearchSeq;
import ru.khav.ProjectNIC.models.DataFromFile;
import ru.khav.ProjectNIC.models.MeanTableModel;
import ru.khav.ProjectNIC.utill.ButNames;
import ru.khav.ProjectNIC.utill.DownloadDataFromFile;
import ru.khav.ProjectNIC.Controllers.SimpleAction;
import ru.khav.ProjectNIC.Controllers.LoadDataFromFile;
import ru.khav.ProjectNIC.views.AddressTable;
import ru.khav.ProjectNIC.views.MeanByteTable;
import ru.khav.ProjectNIC.views.TableData;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.MaskFormatter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Vector;
import java.util.logging.Logger;

@Data
public class MainWindow extends JFrame {
    private static final Logger logger = Logger.getLogger(MainWindow.class.getName());
    private static int countByte = 10;
    private static int address = 10;
    private List<List<Integer>> highlightRanges = new ArrayList<>();

    public static int getCountBytee() {
        return countByte;
    }

    public static int getAddresss() {
        return address;
    }

    public int upCountByte() {
        return countByte++;
    }

    public int upAddress() {
        return address++;
    }

    public int downCountByte() {
        return countByte--;
    }

    public int downAddress() {
        return address--;
    }

    DefaultTableModel fileDataTableModel;
    DefaultTableModel tableAddressModel;
    MeanTableModel meanTableModel = new MeanTableModel();//иначе не прорисуется
    private JFileChooser fileChooser = null;
    DownloadDataFromFile downloadDataFromFile = new LoadDataFromFile();

    TableData tableData;
    AddressTable addressTable;
    JFormattedTextField searchSeq;

    JTable meanByteTable;
    List<Object> buffer = new ArrayList<>();

    private List<Integer> currentIntData;
    private List<Byte> currentByteData;
    private List<String> currentStrData;

    Container container = getContentPane();
    CardLayout cardLayout = new CardLayout();
    JPanel mainPanel = new JPanel(cardLayout);
    JPanel firstPanel = new JPanel(new BorderLayout());
    JPanel secondPanel = new JPanel(new BorderLayout());

    public MainWindow() {
        logger.info("Mainwinow()");
        setSize(4000, 2000);
        setResizable(false);
        setTitle("Ulyana's HexEditor");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setPreferredSize(new Dimension(500, 600));
        setLocation(200, 100); // Устанавливаем позицию окна
        configContainer();

        startWindow();

        pack();
        setVisible(true);
    }

    public void startWindow() {
        logger.info("startWindow");
        // Создание экземпляра JFileChooser
        fileChooser = new JFileChooser();
        firstPanel.setBackground(Color.CYAN);
        JButton jButton = new JButton("File");
        JButton openBut = new JButton("Open it");
        openBut.setName(ButNames.Open.name());
        jButton.setName(ButNames.File.name());
        firstPanel.add(openBut);
        firstPanel.add(jButton);
        jButton.addActionListener(new SimpleAction(this));
        mainPanel.add(firstPanel, "first");
        revalidate();
        repaint();
    }

    public void configContainer() {
        logger.info("configContainer()");
        container.setLayout(new BorderLayout());
        container.add(mainPanel, BorderLayout.CENTER);
        container.setBackground(Color.black);
    }

    public void dataload(String path) throws IOException, ParseException {
        logger.info("dataload()");
        ((LoadDataFromFile) downloadDataFromFile).setFile(new File(path));
        DataFromFile curData = downloadDataFromFile.getDataByteFromFile();
        currentByteData = curData.getBytes();
        currentIntData = curData.getBytes10();
        currentStrData = curData.getHexFormatOfData();

        while (currentByteData.size() < countByte * address) {//чтоб начальное количество ячеек не превышало количество считанного
            countByte--;
            address--;
        }
        createDynamicTable(currentStrData, countByte, address);
        //createMenu();
        revalidate();
        repaint();
    }

    public void dataload2() throws IOException, ParseException{
        logger.info("dataload2()");
        DataFromFile curData = downloadDataFromFile.getNextDataFromFile();
        currentByteData = curData.getBytes();
        currentIntData = curData.getBytes10();
        currentStrData = curData.getHexFormatOfData();
        while (currentByteData.size() < countByte * address) {
            countByte--;
            address--;
        }
        changeScaleDataTable(currentStrData,countByte,address);
        revalidate();
        repaint();
    }
    public void dataload3() throws IOException, ParseException{
        logger.info("dataload2()");
        DataFromFile curData = downloadDataFromFile.getPreviousDataFromFile();
        currentByteData = curData.getBytes();
        currentIntData = curData.getBytes10();
        currentStrData = curData.getHexFormatOfData();
        while (currentByteData.size() < countByte * address) {
            countByte--;
            address--;
        }
     //   createDynamicTable(currentStrData, countByte, address);
        changeScaleDataTable(currentStrData,countByte,address);
        revalidate();
        repaint();
    }


    public void createMenu() {
        JMenuBar jMenuBar = new JMenuBar();
        JMenu jMenu = new JMenu("file");
        jMenu.add(new JMenuItem("open"));
        jMenu.add(new JMenuItem("close"));
        JMenu jMenu1 = new JMenu("view");

        jMenu1.add(new JMenuItem("theme1"));
        jMenu1.add(new JMenuItem("theme2"));
        JMenu jMenu2 = new JMenu("history");
        JMenu jMenu3 = new JMenu("configure");

        jMenuBar.add(jMenu);
        jMenuBar.add(jMenu1);
        jMenuBar.add(jMenu2);
        jMenuBar.add(jMenu3);
        jMenuBar.setSize(this.getWidth(), 50);
        setJMenuBar(jMenuBar);
        container.add(jMenuBar, BorderLayout.NORTH);


    }

    public void createDynamicTable(List<String> dataToView, int colls, int rows) throws IOException, ParseException {
        logger.info("createDynamicTable()");
        Vector<Vector<String>> myData = new Vector<>();
        Vector<Vector<String>> addresses = new Vector<>();
        currentStrData = dataToView;
        int indexData = 0;


        for (int i = 0; i < rows; i++) {
            Vector<String> row = new Vector<>();
            for (int j = 0; j < 1; j++) {
                String addressNum = String.format("%8s", Integer.toBinaryString(i * colls)).replace(' ', '0');
                row.add(addressNum);
            }
            addresses.add(row);
        }

        for (int i = 0; i < rows; i++) {
            Vector<String> row = new Vector<>();
            for (int j = 0; j < colls; j++) {
                String value = " ";
                if (indexData < dataToView.size()) {
                    value = dataToView.get(indexData++);
                }
                row.add(value);
            }
            myData.add(row);
        }
        // Заголовки столбцов
        Vector<String> columnNames = new Vector<>();
        for (int i = 0; i < colls; i++) {
            columnNames.add(String.valueOf(i));
        }
        Vector<String> columnAddressNames = new Vector<>();
        columnAddressNames.add("adress / byte");

        fileDataTableModel = new DefaultTableModel(myData, columnNames);
        tableAddressModel = new DefaultTableModel(addresses, columnAddressNames) {
            @Override
            public boolean isCellEditable(int row, int column) {
                // запрет на редактирование ячейки адреса
                return column != 0;
            }
        };
        configTables();
        showDecMeanConfig();
        // Добавляем на главный контейнер панели
        JPanel editor = new JPanel(new BorderLayout());
        editor.add(configNorthPanel(),BorderLayout.NORTH);
        editor.add(configCenterPanel(), BorderLayout.CENTER);
        editor.add(configSouthPanel(), BorderLayout.SOUTH);
        secondPanel.add(editor);
        mainPanel.add(secondPanel, "second");
        cardLayout.show(mainPanel, "second");

        revalidate();
        repaint();
    }

    public void changeScaleDataTable(List<String> originalData, int colls, int rows) {
        logger.info("changeScaleDataTable()");
        List<String> flatData = originalData;

        //подгонка размера под новые colls * rows
        while (flatData.size() < (rows - 1) * colls) {
            flatData.add(" ");
        }

        int indexData = 0;
        Vector<Vector<String>> myData = new Vector<>();
        Vector<Vector<String>> addresses = new Vector<>();

        for (int i = 0; i < rows; i++) {
            Vector<String> row = new Vector<>();
            Vector<String> addr = new Vector<>();
            String address = String.format("%8s", Integer.toBinaryString(i * colls)).replace(' ', '0');
            addr.add(address);
            for (int j = 0; j < colls; j++) {
                String value = " ";
                if (indexData < flatData.size()) {
                    value = flatData.get(indexData++);
                }
                row.add(value);
            }
            addresses.add(addr);
            myData.add(row);
        }

        // Заголовки
        Vector<String> columnNames = new Vector<>();
        Vector<String> columnAddrNames = new Vector<>();
        columnAddrNames.add("adress / byte");
        for (int i = 0; i < colls; i++) {
            columnNames.add(String.valueOf(i));
        }

        fileDataTableModel.setDataVector(myData, columnNames);
        tableAddressModel.setDataVector(addresses, columnAddrNames);

        tableData.revalidate();
        tableData.repaint();
    }

    public JPanel configNorthPanel() throws ParseException {
        JPanel northPanel = new JPanel(new BorderLayout());
        try {
            MaskFormatter m = new MaskFormatter("HH-HH-HH");
            m.setPlaceholderCharacter('0');

            searchSeq = new JFormattedTextField(m);
            searchSeq.setColumns(16);

            JButton toSearch = new JButton("search");
            toSearch.setName(ButNames.Search.name());
            toSearch.addActionListener(new SearchSeq(this));

            JButton toDelColor = new JButton("delete hightlights");
            toDelColor.setName(ButNames.DelHighlights.name());
            toDelColor.addActionListener(new SearchSeq(this));


            JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
            searchPanel.add(searchSeq);
            searchPanel.add(toSearch);
            searchPanel.add(toDelColor);

            northPanel.add(searchPanel, BorderLayout.WEST);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return northPanel;
    }
    public JPanel configCenterPanel() {
        logger.info("configCenterPanel()");
        for (int i = 0; i < tableData.getColumnCount(); i++) {
            tableData.getColumnModel().getColumn(i).setPreferredWidth(80);
        }
        // Прокручиваемая панель с таблицей
        JScrollPane scrollPane = new JScrollPane(tableData,
                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        JScrollPane scrollPane1 = new JScrollPane(addressTable,
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

    public JPanel configSouthPanel() {
        logger.info("configSouthPanel()");
        JPanel tableButtonPanel = new JPanel(new GridLayout(2, 4, 5, 5));

        JButton addRowButton = new JButton("add row");
        addRowButton.setName(ButNames.AddRow.name());
        addRowButton.setToolTipText("Добавить строку");
        addRowButton.addActionListener(new ChangeTableScale(this));

        JButton addColumnButton = new JButton("add column");
        addColumnButton.setName(ButNames.AddColumn.name());
        addColumnButton.setToolTipText("Добавить столбец");
        addColumnButton.addActionListener(new ChangeTableScale(this));

        JButton delRowButton = new JButton("del row");
        delRowButton.setName(ButNames.DelRow.name());
        delRowButton.setToolTipText("Удалить строку");
        delRowButton.addActionListener(new ChangeTableScale(this));

        JButton delColumnButton = new JButton("del column");
        delColumnButton.setName(ButNames.DelColumn.name());
        delColumnButton.setToolTipText("Удалить столбец");
        delColumnButton.addActionListener(new ChangeTableScale(this));

        JButton exitBut = new JButton("Exit");
        exitBut.setName(ButNames.Exit.name());
        exitBut.addActionListener(new SimpleAction(this));

        JButton up = new JButton("upPage");
        up.setName(ButNames.UpPage.name());
        up.addActionListener(new ChangeTableScale(this));

        JButton down = new JButton("downPage");
        down.setName(ButNames.DownPage.name());
        down.addActionListener(new ChangeTableScale(this));


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
        JScrollPane jScrollMean = new JScrollPane(meanByteTable);
        // jScrollMean.setPreferredSize(new Dimension(500, 151));


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

    public void showDecMeanConfig() {
        logger.info("showDecMeanConfig()");
        tableData.getColumnModel().getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int selectedRow = tableData.getSelectedRow();
                int[] selectedColumns = tableData.getSelectedColumns();

                int meanA = 3;
                int mean = 2;
                int addr = 0;
                int byteNum = 1;
                int indexRow = 0;


                for (int col : selectedColumns) {
                    int curPos = selectedRow * countByte + col;
                    if (tableData.isCellSelected(selectedRow, col) && curPos < currentByteData.size()) {
                        String value = currentStrData.get(curPos);
                        byte byteValue = currentByteData.get(curPos);
                        int unsignValue = currentIntData.get(curPos);
                        if (indexRow <= 7) {
                            meanByteTable.getModel().setValueAt(Integer.toString(byteValue), indexRow, mean);
                            meanByteTable.getModel().setValueAt(String.format("%8s", Integer.toBinaryString(selectedRow * countByte + col)).replace(' ', '0'), indexRow, addr);
                            meanByteTable.getModel().setValueAt(col, indexRow, byteNum);
                            meanByteTable.getModel().setValueAt(Integer.toString(unsignValue), indexRow, meanA);

                            indexRow++;
                        }
                    }
                }
                for (int i = indexRow; i < 8; i++) {
                    meanByteTable.getModel().setValueAt("", i, mean);
                    meanByteTable.getModel().setValueAt("", i, addr);
                    meanByteTable.getModel().setValueAt("", i, byteNum);
                    meanByteTable.getModel().setValueAt("", i, meanA);
                }
            }
        });

    }


    public void editDataConfig() {
        logger.info("editDataConfig()");
        //одиночное изменение
        tableData.getModel().addTableModelListener(l -> {
            int row = l.getFirstRow();
            int col = l.getColumn();
            if (row < 0 || col < 0) return;
            int curPos = row * countByte + col;
            String hexString = (String) tableData.getModel().getValueAt(row, col);
            if (curPos >= currentByteData.size()) {//todo проверить
                if (downloadDataFromFile.isLastPage()) {
                    wideCurData(hexString, curPos);
                }
            } else {
                updateCurData(hexString, curPos);
            }
            try {
                DataFromFile data = new DataFromFile(currentByteData, currentIntData);
                downloadDataFromFile.updateDataInFile(data);
            } catch (IOException e) {
                System.out.println(e.getMessage());
                throw new RuntimeException(e);
            }
        });

        //удаление одиночное и блочно
        KeyStroke deleteKey = KeyStroke.getKeyStroke(KeyEvent.VK_DELETE, 0);
        tableData.getInputMap(JComponent.WHEN_FOCUSED).put(deleteKey, "deleteCells");
        tableData.getActionMap().put("deleteCells", new AbstractAction() {
            @Override//удаляем блоки клавишей delete
            public void actionPerformed(ActionEvent e) {
                int selectedRow = tableData.getSelectedRow();
                int[] selectedCols = tableData.getSelectedColumns();
                for (int col : selectedCols) {
                    tableData.setValueAt("0", selectedRow, col);
                    int curPos = selectedRow * countByte + col;
                    if (curPos < currentStrData.size() && curPos < currentByteData.size()) {
                        updateCurData("0", curPos);
                    }/* else {
                        wideCurData("0", curPos);
                    }*/
                }
                try {
                    DataFromFile data = new DataFromFile(currentByteData, currentIntData);
                    downloadDataFromFile.updateDataInFile(data);
                } catch (IOException ex) {
                    logger.severe("Ошибка при сохранении после удаления: " + ex.getMessage());
                }
            }
        });
        //копирование без вырезания
        KeyStroke ctrlC = KeyStroke.getKeyStroke(KeyEvent.VK_C, InputEvent.CTRL_DOWN_MASK);
        tableData.getInputMap(JComponent.WHEN_FOCUSED).put(ctrlC, "addToBuff");
        tableData.getActionMap().put("addToBuff", new AbstractAction() {
            @Override//копирование в буфер блока элементов с помощью ctrlс
            public void actionPerformed(ActionEvent e) {
                int selectedRow = tableData.getSelectedRow();
                int[] selectedCols = tableData.getSelectedColumns();
                buffer.clear();
                for (int col : selectedCols) {
                    buffer.add(tableData.getModel().getValueAt(selectedRow, col));
                }
            }
        });
        //вырезка
        KeyStroke ctrlB = KeyStroke.getKeyStroke(KeyEvent.VK_B, InputEvent.CTRL_DOWN_MASK);
        tableData.getInputMap(JComponent.WHEN_FOCUSED).put(ctrlB, "addAllToBuff");
        tableData.getActionMap().put("addAllToBuff", new AbstractAction() {
            @Override//вырезка в буфер блока элементов с помощью ctrlb
            public void actionPerformed(ActionEvent e) {
                int selectedRow = tableData.getSelectedRow();
                int[] selectedCols = tableData.getSelectedColumns();
                buffer.clear();
                for (int col : selectedCols) {
                    int curPos = selectedRow * countByte + col;
                    if (tableData.getModel().getValueAt(selectedRow, col) == null) {
                        buffer.add("0");
                    } else {
                        buffer.add(tableData.getModel().getValueAt(selectedRow, col));
                    }
                    if (curPos < currentByteData.size()) {
                        tableData.getModel().setValueAt("0", selectedRow, col);
                        updateCurData("0", curPos);
                    }
                }
                try {
                    DataFromFile data = new DataFromFile(currentByteData, currentIntData);
                    downloadDataFromFile.updateDataInFile(data);
                } catch (IOException ex) {
                    logger.severe("Ошибка при сохранении после вырезки: " + ex.getMessage());
                }
                secondPanel.revalidate();
            }
        });
        KeyStroke ctrlV = KeyStroke.getKeyStroke(KeyEvent.VK_V, InputEvent.CTRL_DOWN_MASK);
        tableData.getInputMap(JComponent.WHEN_FOCUSED).put(ctrlV, "addFromBuff");
        tableData.getActionMap().put("addFromBuff", new AbstractAction() {
            @Override//вставка блока элементов с помощью ctrlv
            public void actionPerformed(ActionEvent e) {
                int selectedRow = tableData.getSelectedRow();
                int[] selectedCols = tableData.getSelectedColumns();
                int it = 0;
                String value;
                for (int col : selectedCols) {
                    if (it >= buffer.size()) break;
                    value = (String) buffer.get(it);
                    int curPos = selectedRow * countByte + col;
                    it++;
                    if (curPos >= currentByteData.size()) {
                        if (!downloadDataFromFile.isLastPage()){
                            break;}
                        wideCurData(value, curPos);}
                    else {
                        tableData.getModel().setValueAt(value, selectedRow, col);
                        updateCurData(value, curPos);
                    }
                }
                try {
                    DataFromFile data = new DataFromFile(currentByteData, currentIntData);
                    downloadDataFromFile.updateDataInFile(data);
                } catch (IOException ex) {
                    logger.severe("Ошибка при сохранении после вставки c заменой: " + ex.getMessage());
                }
            }
        });
        KeyStroke ctrlX = KeyStroke.getKeyStroke(KeyEvent.VK_X, InputEvent.CTRL_DOWN_MASK);
        tableData.getInputMap(JComponent.WHEN_FOCUSED).put(ctrlX, "addXFromBuff");
        tableData.getActionMap().put("addXFromBuff", new AbstractAction() {
            @Override//вставка блока элементов с помощью ctrlX
            public void actionPerformed(ActionEvent e) {
                int selectedRow = tableData.getSelectedRow();
                int[] selectedCols = tableData.getSelectedColumns();
                int posFromCopy = selectedRow * countByte + selectedCols[0];
                int posToInsert = selectedRow * countByte + selectedCols[selectedCols.length - 1] + 1;
                List<Byte> toInsByte = new ArrayList<>(currentByteData.subList(posFromCopy, currentByteData.size() - 1));
                List<Integer> toInsInt = new ArrayList<>(currentIntData.subList(posFromCopy, currentIntData.size() - 1));
                List<String> toInsStr = new ArrayList<>(currentStrData.subList(posFromCopy, currentStrData.size() - 1));
                int it = 0;
                String value;
                wideCurDataWithoutChange(selectedCols.length);
                for (int col : selectedCols) {
                    if (it >= buffer.size()) break;
                    value = (String) buffer.get(it);
                    tableData.getModel().setValueAt(value, selectedRow, col);
                    int curPos = selectedRow * countByte + col;
                    it++;
                    if (curPos >= currentByteData.size()) {
                        if (downloadDataFromFile.isLastPage()) {//todo check!
                            wideCurData(value, curPos);
                        }
                    } else {
                        updateCurData(value, curPos);
                    }
                }
                updateBalanceCurDataWider(toInsByte, toInsInt, toInsStr, posToInsert);

                try {
                    DataFromFile data = new DataFromFile(currentByteData, currentIntData);
                    downloadDataFromFile.updateDataInFile(data);
                } catch (IOException ex) {
                    logger.severe("Ошибка при сохранении после вставки без замены: " + ex.getMessage());
                }
            }
        });
    }

    public void configTables() throws IOException {
        logger.info("configTables()");
        //тут данные с файла
        tableData = new TableData(fileDataTableModel);
        tableData.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                                                           boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

                boolean highlightFound = false;
                if (!highlightRanges.isEmpty()) {
                    for (List<Integer> range : highlightRanges) {
                        int sr = range.get(0), sc = range.get(1), er = range.get(2), ec = range.get(3);

                        //попадает ли текущая ячейка в диапазон
                        if (row >= sr && row <= er && column >= sc && column <= ec) {
                            c.setBackground(Color.CYAN);
                            c.setForeground(Color.BLACK);
                            highlightFound = true;
                            break;
                        }
                    }
                }

                // Если нет выделения, восстанавливаем обычные цвета
                if (!highlightFound) {
                    c.setBackground(isSelected ? table.getSelectionBackground() : Color.WHITE);
                    c.setForeground(isSelected ? table.getSelectionForeground() : Color.BLACK);
                }

                return c;
            }
        });
        editDataConfig();
        //тут адреса
        addressTable = new AddressTable(tableAddressModel);
        //тут значения в другой интерпретации
        meanByteTable = new MeanByteTable(meanTableModel);
    }

    public void updateCurData(String hexString, int curPos) {
        hexString = (hexString == null) ? "0" : hexString;
        int intValue = Integer.parseInt(hexString.trim(), 16); //парсим как hex
        byte byteValue = (byte) intValue;
        currentStrData.set(curPos, hexString);
        currentByteData.set(curPos, byteValue);
        currentIntData.set(curPos, intValue);
    }

    public void wideCurData(String hexString, int curPos) {
        for (int i = currentByteData.size(); i <= curPos; i++) {
            if (i == curPos) {
                currentStrData.set(i, "0");
                currentByteData.add((byte) 0);
                currentIntData.add(0);
                updateCurData(hexString, curPos);
                return;
            }
            currentStrData.add("");
            currentByteData.add((byte) 0);
            currentIntData.add(0);

        }
    }

    public void wideCurDataWithoutChange(int size) {
        for (int i = 0; i <= size; i++) {
            currentByteData.add((byte) 0);
            currentIntData.add(0);
        }
    }

    public void updateBalanceCurDataWider(List<Byte> tmp, List<Integer> tm, List<String> t, int indexFrom) {
        currentByteData.addAll(indexFrom, tmp);
        currentIntData.addAll(indexFrom, tm);
        currentStrData.addAll(indexFrom, t);
        changeScaleDataTable(currentStrData, countByte, address);
    }

    public void addHighlightRange(int sr, int sc, int er, int ec) {
        highlightRanges.add(Arrays.asList(sr, sc, er, ec));
    }

    public void clearHighlightRanges() {
        highlightRanges.clear();
        tableData.repaint();
    }

    public List<List<Integer>> getHighlightRanges() {
        return highlightRanges;
    }

    public static void main(String[] args) {


        SwingUtilities.invokeLater(() -> new MainWindow());

    }
}