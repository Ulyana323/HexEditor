package ru.khav.ProjectNIC.utill;

import ru.khav.ProjectNIC.models.DataFromFile;

import java.io.IOException;

public interface ReadDataFromFile {
    DataFromFile getDataByteFromFile() throws IOException;

    void updateDataInFile(DataFromFile data) throws IOException;

    boolean isLastPage();

    DataFromFile getNextDataFromFile() throws IOException;

    DataFromFile getPreviousDataFromFile() throws IOException;
}
