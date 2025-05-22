package ru.khav.ProjectNIC.Services;

import java.util.List;

public class DataEditorService {
    public static void updateCurData(String hexString, int curPos, List<Byte> currentByteData,
                              List<Integer> currentIntData, List<String> currentStrData) {
        hexString = (hexString == null) ? "0" : hexString;
        int intValue = Integer.parseInt(hexString.trim(), 16); //парсим как hex
        byte byteValue = (byte) intValue;
        currentStrData.set(curPos, hexString);
        currentByteData.set(curPos, byteValue);
        currentIntData.set(curPos, intValue);
    }

    public static void wideCurData(String hexString, int curPos,List<Byte> currentByteData,
                            List<Integer> currentIntData,List<String> currentStrData) {
        for (int i = currentByteData.size(); i <= curPos; i++) {
            if (i == curPos) {
                currentStrData.set(i, "0");
                currentByteData.add((byte) 0);
                currentIntData.add(0);
                updateCurData(hexString, curPos,currentByteData,currentIntData,currentStrData);
                return;
            }
            currentStrData.add("");
            currentByteData.add((byte) 0);
            currentIntData.add(0);

        }
    }

    public static void wideCurDataWithoutChange(int size,List<Byte> currentByteData,
                                         List<Integer> currentIntData,List<String> currentStrData) {
        for (int i = 0; i <= size; i++) {
            currentByteData.add((byte) 0);
            currentIntData.add(0);
        }
    }
}
