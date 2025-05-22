package ru.khav.ProjectNIC.Services;

import ru.khav.ProjectNIC.UI_Components.TableFactory;
import ru.khav.ProjectNIC.models.DataFromFile;
import ru.khav.ProjectNIC.models.DataManager;
import ru.khav.ProjectNIC.views.MainWindow;

import javax.swing.*;
import java.io.IOException;
import java.text.ParseException;
import java.util.logging.Logger;

import static javax.swing.JOptionPane.INFORMATION_MESSAGE;
import static ru.khav.ProjectNIC.utill.Globals.address;
import static ru.khav.ProjectNIC.utill.Globals.countByte;

public class PageNavigateService {
    static Logger logger = Logger.getLogger(PageNavigateService.class.getName());

    public static void dataloadWhenChangePageUp(MainWindow mainWindow, DataManager dataManager, TableFactory tableFactory) throws IOException, ParseException {
        logger.info("dataloadWhenChangePage() :PageNavigateService");
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

    public static void dataloadWhenChangePageDown(MainWindow mainWindow, DataManager dataManager, TableFactory tableFactory) throws IOException, ParseException {
        logger.info("dataloadWhenChangePageDown() :PageNavigateService");
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
