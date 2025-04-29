package ru.khav.ProjectNIC;

import ru.khav.ProjectNIC.models.DataFromFile;
import ru.khav.ProjectNIC.models.MeanTableModel;
import ru.khav.ProjectNIC.services.DownloadDataFromFile;
import ru.khav.ProjectNIC.utill.LoadDataFromFile;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.JTextComponent;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Vector;

public class MainWindow extends JFrame {

    private static int countByte = 10;
    private static int address = 10;
    DefaultTableModel tableModel;
    DefaultTableModel tableAddressModel;
    private JFileChooser fileChooser = null;
    DownloadDataFromFile downloadDataFromFile = new LoadDataFromFile();
    JTable table;
    JTable addressTable;
    JTable meanByteTable;
    List<String> array = new LinkedList<>();

    private List<String> currentData;
    Container container = getContentPane();
    CardLayout cardLayout = new CardLayout();
    JPanel mainPanel = new JPanel(cardLayout);
    JPanel firstPanel = new JPanel(new BorderLayout());

    JPanel secondPanel = new JPanel(new BorderLayout());

    public MainWindow() {
        setSize(2000, 1000);
        setResizable(false);
        setTitle("trying...");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setPreferredSize(new Dimension(500, 600));
        setLocation(200, 100); // Устанавливаем позицию окна
        configContainer();

        startWindow();

        pack();
        setVisible(true);
    }

    public void startWindow() {
        // Создание экземпляра JFileChooser
        fileChooser = new JFileChooser();
        firstPanel.setBackground(Color.CYAN);

        JButton jButton = new JButton("File");
        JButton openBut = new JButton("Open it");
        openBut.setName("openBut");
        jButton.setName("File");
        firstPanel.add(openBut);
        firstPanel.add(jButton);
        jButton.addActionListener(new SimpleAction());


        mainPanel.add(firstPanel, "first");
        revalidate();
        repaint();
    }


    public void configContainer() {
        container.setLayout(new BorderLayout());
        container.add(mainPanel, BorderLayout.CENTER);
        container.setBackground(Color.black);
    }


    public void dataload(String path) throws IOException {
        ((LoadDataFromFile) downloadDataFromFile).setFile(new File(path));
        DataFromFile curData = downloadDataFromFile.getDataFromFile();
        currentData = curData.getHexFormatOfData();

        createDynamicTable(currentData, countByte, address);
        //createMenu();
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

    public void createDynamicTable(List<String> dataToView, int colls, int rows) {
        Vector<Vector<String>> myData = new Vector<>();
        Vector<Vector<String>> addresses = new Vector<>();
        currentData = dataToView;
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

        tableModel = new DefaultTableModel(myData, columnNames);
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
        editor.add(configCenterPanel(), BorderLayout.CENTER);
        editor.add(configSouthPanel(), BorderLayout.SOUTH);
        secondPanel.add(editor);
        mainPanel.add(secondPanel, "second");
        cardLayout.show(mainPanel, "second");

        revalidate();
        repaint();
    }

    public void changeScaleDataTable(List<String> originalData, int colls, int rows) {
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

        tableModel.setDataVector(myData, columnNames);
        tableAddressModel.setDataVector(addresses, columnAddrNames);

        table.revalidate();
        table.repaint();
    }

    private void scrollTable(int direction) {
        int currentViewableColumns = table.getVisibleRect().width / table.getColumnModel().getColumn(0).getWidth();

        if (direction == 1) { //r
            int newColumnIndex = Math.min(table.getSelectedColumn() + currentViewableColumns, table.getColumnCount() - 1);
            table.scrollRectToVisible(table.getCellRect(0, newColumnIndex, true));
            table.changeSelection(0, newColumnIndex, false, false);
        } else { //l
            int newColumnIndex = Math.max(table.getSelectedColumn() - currentViewableColumns, 0);
            table.scrollRectToVisible(table.getCellRect(0, newColumnIndex, true));
            table.changeSelection(0, newColumnIndex, false, false);
        }
    }

    class SimpleAction extends AbstractAction {
        private static final long serialVersionUID = 1L;

        // Обработка события нажатия на кнопку
        public void actionPerformed(ActionEvent e) {
            JButton btn = (JButton) e.getSource();
            System.out.println("Нажатие на кнопку <" + btn.getName() + ">");

            if (btn.getName().equalsIgnoreCase("addrow")) {
                tableModel.addRow(new Object[]{address++});
                changeScaleDataTable(currentData, countByte, address);
            }
            if (btn.getName().equalsIgnoreCase("addcol")) {
                tableModel.addColumn(countByte++);
                changeScaleDataTable(currentData, countByte, address);
            }
            if (btn.getName().equalsIgnoreCase("delrow")) {
                if (tableModel.getRowCount() > 0) {
                    tableModel.removeRow(address - 2);
                    address--;
                    changeScaleDataTable(currentData, countByte, address);
                }
            }
            if (btn.getName().equalsIgnoreCase("delcol")) {
                if (tableModel.getColumnCount() > 1) {
                    tableModel.setColumnCount(countByte - 1);
                    countByte--;
                    changeScaleDataTable(currentData, countByte, address);
                }
            }
            if (btn.getName().equalsIgnoreCase("exitBut")) {
                CardLayout cl = (CardLayout) (mainPanel.getLayout());
                cl.show(mainPanel, "first");
            }
            if (btn.getName().equalsIgnoreCase("openBut")) {
                CardLayout cl = (CardLayout) (mainPanel.getLayout());

                cl.show(mainPanel, "second");
            }
            if (btn.getName().equalsIgnoreCase("File")) {
                fileChooser.setDialogTitle("Выбор директории");
                //только каталог
                fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
                int result = fileChooser.showOpenDialog(MainWindow.this);
                // Если директория выбрана, покажем ее в сообщении
                if (result == JFileChooser.APPROVE_OPTION) {
                    System.out.println(fileChooser.getSelectedFile().getPath());
                    //createDynamicTable(array, countByte, address);}

                    try {
                        dataload(fileChooser.getSelectedFile().getPath());
                    } catch (Exception ex) {
                        System.out.println(ex.getMessage());
                    }

                }

            }
        }
    }

    public JTextComponent configDecViev(String cellValue) {
        JTextField jTextField = new JTextField(30);
        return jTextField;
    }

    public JPanel configCenterPanel() {
        for (int i = 0; i < table.getColumnCount(); i++) {
            table.getColumnModel().getColumn(i).setPreferredWidth(80);
        }
        // Прокручиваемая панель с таблицей
        JScrollPane scrollPane = new JScrollPane(table,
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
        JPanel tableButtonPanel = new JPanel();

        JButton addRowButton = new JButton("add row");
        addRowButton.setName("addrow");
        addRowButton.setToolTipText("Добавить строку");
        addRowButton.addActionListener(new SimpleAction());

        JButton addColumnButton = new JButton("add column");
        addColumnButton.setName("addcol");
        addColumnButton.setToolTipText("Добавить столбец");
        addColumnButton.addActionListener(new SimpleAction());

        JButton delRowButton = new JButton("del row");
        delRowButton.setName("delrow");
        delRowButton.setToolTipText("Удалить строку");
        delRowButton.addActionListener(new SimpleAction());

        JButton delColumnButton = new JButton("del column");
        delColumnButton.setName("delcol");
        delColumnButton.setToolTipText("Удалить столбец");
        delColumnButton.addActionListener(new SimpleAction());

        JButton exitBut = new JButton("Exit");
        exitBut.setName("exitBut");
        exitBut.addActionListener(new SimpleAction());

        tableButtonPanel.add(addRowButton);
        tableButtonPanel.add(addColumnButton);
        tableButtonPanel.add(delColumnButton);
        tableButtonPanel.add(delRowButton);
        //tableButtonPanel.add(exitBut);

        JButton scrollLeftButton = new JButton("<");
        scrollLeftButton.addActionListener(e -> scrollTable(-1));
        JButton scrollRightButton = new JButton(">");
        scrollRightButton.addActionListener(e -> scrollTable(1));

        tableButtonPanel.add(scrollLeftButton);
        tableButtonPanel.add(scrollRightButton);

        // панель с десят знач
        JPanel decNote = new JPanel();
        decNote.setBackground(Color.GREEN);
        //без скролпэйн не отобразятся заголовки
        JScrollPane jScrollMean = new JScrollPane(meanByteTable);
        jScrollMean.setPreferredSize(new Dimension(450, 200));
        decNote.add(jScrollMean, BorderLayout.CENTER);
        //decNote.setPreferredSize(new Dimension(500, 100));
        decNote.setBorder(BorderFactory.createLineBorder(Color.BLACK));

        //нижняяя панель с десятичными значениями и управлением таблицы  с кнопками
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
        table.getColumnModel().getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int selectedRow = table.getSelectedRow();
                int[] selectedColumns = table.getSelectedColumns();
                int mean = 2;
                int addr = 0;
                int byteNum = 1;
                int indexRow = 0;


                for (int col : selectedColumns) {
                    if (table.isCellSelected(selectedRow, col)) {
                        Object value = table.getValueAt(selectedRow, col);
                        if (indexRow <= 7) {
                            meanByteTable.getModel().setValueAt(value, indexRow,mean);
                            //todo сделать десятичный вывод значения со знаком и без
                            meanByteTable.getModel().setValueAt(String.format("%8s", Integer.toBinaryString(selectedRow * countByte+col)).replace(' ', '0'), indexRow, addr);
                            meanByteTable.getModel().setValueAt(col, indexRow, byteNum);
                            indexRow++;
                        }
                        System.out.println("Выбрана ячейка [" + selectedRow + ", " + col + "]: " + value);
                    }
                }
             for (int i = indexRow; i < 8; i++) {
                meanByteTable.getModel().setValueAt("", i, mean);
                meanByteTable.getModel().setValueAt("", i, addr);
                meanByteTable.getModel().setValueAt("", i, byteNum);
            }
        }
        });

    }

    public void configTables()
    {
        //тут данные с файла
        table = new JTable(tableModel);
        table.getTableHeader().setResizingAllowed(false);
        table.getTableHeader().setReorderingAllowed(false);
        table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        table.setCellSelectionEnabled(true);
        table.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setSelectionBackground(Color.YELLOW);

        //тут адреса
        addressTable = new JTable(tableAddressModel);
        addressTable.setCellSelectionEnabled(false);
        addressTable.getColumnModel().setColumnSelectionAllowed(false);

        //тут значения в другой интерпретации
        meanByteTable = new JTable(new MeanTableModel());
        meanByteTable.getTableHeader().setResizingAllowed(false);



    }

    public static void main(String[] args) {


        SwingUtilities.invokeLater(() -> new MainWindow());

    }
}