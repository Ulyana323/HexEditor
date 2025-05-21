package ru.khav.ProjectNIC.utill;

import lombok.Data;
import ru.khav.ProjectNIC.Controllers.LoadDataFromFile;
import ru.khav.ProjectNIC.UI_Components.TableFactory;

import java.util.List;
import java.util.logging.Logger;

@Data
public class DataManager {


    ReadDataFromFile readDataFromFile = new LoadDataFromFile();
    private List<Integer> currentIntData;
    private List<Byte> currentByteData;
    private List<String> currentStrData;

    public void updateCurData(String hexString, int curPos) {
        hexString = (hexString == null) ? "0" : hexString;
        int intValue = Integer.parseInt(hexString.trim(), 16); //парсим как hex
        byte byteValue = (byte) intValue;
        currentStrData.set(curPos, hexString);
        currentByteData.set(curPos, byteValue);
        currentIntData.set(curPos, intValue);
    }

    public void wideCurData(String hexString, int curPos) {
        for (int i = currentByteData.size(); i <= curPos; i++) {
            if (i == curPos) {
                currentStrData.set(i, "0");
                currentByteData.add((byte) 0);
                currentIntData.add(0);
                updateCurData(hexString, curPos);
                return;
            }
            currentStrData.add("");
            currentByteData.add((byte) 0);
            currentIntData.add(0);

        }
    }

    public void wideCurDataWithoutChange(int size) {
        for (int i = 0; i <= size; i++) {
            currentByteData.add((byte) 0);
            currentIntData.add(0);
        }
    }

    public void updateBalanceCurDataWider(List<Byte> tmp, List<Integer> tm,
                                          List<String> t, int indexFrom,
                                          int countByte, int address,TableFactory tableFactory) {
        currentByteData.addAll(indexFrom, tmp);
        currentIntData.addAll(indexFrom, tm);
        currentStrData.addAll(indexFrom, t);
        TableScaleService.changeScaleDataTable(currentStrData, countByte, address,tableFactory);
    }

}
