package ru.khav.ProjectNIC.models;

import lombok.Data;
import ru.khav.ProjectNIC.Controllers.LoadDataFromFile;
import ru.khav.ProjectNIC.Interfaces.ReadDataFromFile;

import java.util.List;

@Data
public class DataManager {
    ReadDataFromFile readDataFromFile = new LoadDataFromFile();
    private List<Integer> currentIntData;
    private List<Byte> currentByteData;
    private List<String> currentStrData;
}
