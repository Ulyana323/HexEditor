package ru.khav.ProjectNIC.services;

import ru.khav.ProjectNIC.models.DataFromFile;

import java.io.IOException;

public interface DownloadDataFromFile {
     DataFromFile getDataTextFromFile() throws IOException;
    DataFromFile getDataBinFromFile() throws IOException;
    void updateDataInFile(DataFromFile data) throws IOException;;
}
