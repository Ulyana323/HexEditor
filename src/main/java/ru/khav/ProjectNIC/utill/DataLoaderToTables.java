package ru.khav.ProjectNIC.utill;

import ru.khav.ProjectNIC.Controllers.LoadDataFromFile;
import ru.khav.ProjectNIC.UI_Components.TableFactory;
import ru.khav.ProjectNIC.models.DataFromFile;
import ru.khav.ProjectNIC.views.MainWindow;

import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.util.logging.Logger;

import static javax.swing.JOptionPane.INFORMATION_MESSAGE;
import static ru.khav.ProjectNIC.utill.Globals.address;
import static ru.khav.ProjectNIC.utill.Globals.countByte;

public class DataLoaderToTables {
    private static Logger logger = Logger.getLogger(DataLoaderToTables.class.getName());
    private final DataManager dataManager;
    private final TableFactory tableFactory;


    public DataLoaderToTables(DataManager dataManager, TableFactory tableFactory) {
        this.dataManager = dataManager;
        this.tableFactory = tableFactory;

    }

    public void dataloadInitial(String path, MainWindow mainWindow) throws IOException, ParseException {
        logger.info("dataloadInitial() :DataloaderTables");
        ((LoadDataFromFile) dataManager.getReadDataFromFile()).setFile(new File(path));
        DataFromFile curData = dataManager.getReadDataFromFile().getDataByteFromFile();
        dataManager.setCurrentByteData(curData.getBytes());
        dataManager.setCurrentIntData(curData.getBytes10());
        dataManager.setCurrentStrData(curData.getHexFormatOfData());

        while (dataManager.getCurrentByteData().size() < countByte * address) {//чтоб начальное количество ячеек не превышало количество считанного
            countByte--;
            address--;
        }
        tableFactory.createDynamicTable(dataManager.getCurrentStrData(), countByte, address, mainWindow);

        mainWindow.revalidate();
        mainWindow.repaint();

    }

    public void dataloadWhenChangePageUp(MainWindow mainWindow) throws IOException, ParseException {
        logger.info("dataloadWhenChangePage() :DataloaderTables");
        DataFromFile curData = dataManager.getReadDataFromFile().getNextDataFromFile();
        dataManager.setCurrentByteData(curData.getBytes());
        dataManager.setCurrentIntData(curData.getBytes10());
        dataManager.setCurrentStrData(curData.getHexFormatOfData());
        while (dataManager.getCurrentByteData().size() < countByte * address) {
            countByte--;
            address--;
        }
        TableScaleService.changeScaleDataTable(dataManager.getCurrentStrData(), countByte, address, tableFactory);
        if (dataManager.getReadDataFromFile().isLastPage()) {
            JOptionPane.showMessageDialog(mainWindow, "это последняя страница",
                    "Все!", INFORMATION_MESSAGE);
        }
        mainWindow.revalidate();
        mainWindow.repaint();
    }

    public void dataloadWhenChangePageDown(MainWindow mainWindow) throws IOException, ParseException {
        logger.info("dataloadWhenChangePageDown() :DataloaderTables");
        DataFromFile curData = dataManager.getReadDataFromFile().getPreviousDataFromFile();
        dataManager.setCurrentByteData(curData.getBytes());
        dataManager.setCurrentIntData(curData.getBytes10());
        dataManager.setCurrentStrData(curData.getHexFormatOfData());
        while (dataManager.getCurrentByteData().size() < countByte * address) {
            countByte--;
            address--;
        }
        TableScaleService.changeScaleDataTable(dataManager.getCurrentStrData(), countByte, address, tableFactory);
        mainWindow.revalidate();
        mainWindow.repaint();
    }

}