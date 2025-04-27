package ru.khav.ProjectNIC.services;

import ru.khav.ProjectNIC.models.DataFromFile;

import java.io.IOException;

public interface DownloadDataFromFile {
     DataFromFile getDataFromFile() throws IOException;
     int updateDataInFile(DataFromFile data);
}
