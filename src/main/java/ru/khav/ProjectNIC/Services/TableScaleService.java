package ru.khav.ProjectNIC.Services;

import ru.khav.ProjectNIC.UI_Components.TableFactory;

import javax.swing.table.DefaultTableModel;
import java.util.List;
import java.util.Vector;
import java.util.logging.Logger;

public class TableScaleService {
    private static Logger logger = Logger.getLogger(TableScaleService.class.getName());

    public static void changeScaleDataTable(List<String> originalData, int colls, int rows,
                                            TableFactory tableFactory) {
        logger.info("changeScaleDataTable(): TableScaleService");
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

        ((DefaultTableModel) tableFactory.getDataFromFileTable().getModel()).setDataVector(myData, columnNames);
        ((DefaultTableModel) tableFactory.getAddressTable().getModel()).setDataVector(addresses, columnAddrNames);

        tableFactory.getDataFromFileTable().revalidate();
        tableFactory.getDataFromFileTable().repaint();
    }
}
