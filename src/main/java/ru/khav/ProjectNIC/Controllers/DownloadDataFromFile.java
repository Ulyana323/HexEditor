package ru.khav.ProjectNIC.Controllers;

import ru.khav.ProjectNIC.models.DataFromFile;

import java.io.IOException;

public interface DownloadDataFromFile {
    DataFromFile getDataByteFromFile() throws IOException;
    void updateDataInFile(DataFromFile data) throws IOException;
   boolean isLastPage();
}
