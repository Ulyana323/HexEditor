package ru.khav.ProjectNIC.Services;

import ru.khav.ProjectNIC.PanelConfig.SouthPanelBuilder;
import ru.khav.ProjectNIC.UI_Components.PanelFactory;
import ru.khav.ProjectNIC.UI_Components.TableFactory;
import ru.khav.ProjectNIC.models.DataFromFile;
import ru.khav.ProjectNIC.models.DataManager;
import ru.khav.ProjectNIC.views.MainWindow;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.util.logging.Logger;

import static ru.khav.ProjectNIC.utill.Globals.address;
import static ru.khav.ProjectNIC.utill.Globals.countByte;

public class DataLoaderService {
    static Logger logger= Logger.getLogger(DataLoaderService.class.getName());

    public static void dataloadInitial(String path, MainWindow mainWindow, DataManager dataManager, TableFactory tableFactory, PanelFactory panelFactory) throws IOException, ParseException {
        logger.info("dataloadInitial() :DataLoaderService");
        (dataManager.getReadDataFromFile()).setFile(new File(path));
        DataFromFile curData = dataManager.getReadDataFromFile().getDataByteFromFile();
        dataManager.setCurrentByteData(curData.getBytes());
        dataManager.setCurrentIntData(curData.getBytes10());
        dataManager.setCurrentStrData(curData.getHexFormatOfData());

        while (dataManager.getCurrentByteData().size() < countByte * address) {//чтоб начальное количество ячеек не превышало количество считанного
            countByte--;
            address--;
        }
        tableFactory.createDynamicTable(dataManager.getCurrentStrData(), countByte, address, mainWindow,panelFactory,dataManager);

        mainWindow.revalidate();
        mainWindow.repaint();

    }
}
