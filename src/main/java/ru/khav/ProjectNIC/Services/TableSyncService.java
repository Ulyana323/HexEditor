package ru.khav.ProjectNIC.Services;

import ru.khav.ProjectNIC.UI_Components.TableFactory;

import java.util.List;

public class TableSyncService {


    public static void updateBalanceCurDataWider(List<Byte> tmp, List<Integer> tm,
                                                 List<String> t, int indexFrom,
                                                 int countByte, int address, List<Byte> currentByteData,
                                                 List<Integer> currentIntData, List<String> currentStrData,
                                                 TableFactory tableFactory) {
        currentByteData.addAll(indexFrom, tmp);
        currentIntData.addAll(indexFrom, tm);
        currentStrData.addAll(indexFrom, t);
        TableScaleService.changeScaleDataTable(currentStrData, countByte, address, tableFactory);
    }
}
