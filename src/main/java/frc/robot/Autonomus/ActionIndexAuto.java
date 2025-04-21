package frc.robot.Autonomus;

import java.util.HashMap;
import java.util.Map;

/**
 * Класс предназначен для инциалилазации всего списка команд и вывода по ключу нужного массива для логики
 * Данный класс содержит методы для работы cо структурой данных(HashMap)
 */
public class ActionIndexAuto {
    private final Map<String, Integer> actionKeysMap = new HashMap<>(){
        {
            put("StartToCheckPoint", 0);

            put("CheckPointToFirstTree", 1);
            put("CheckPointToSecondTree", 2);
            put("CheckPointToThirdTree", 3);

            put("DriveToZona", 4);
            put("DriveToTree", 5);
            put("ExitFromZona", 6);
            put("ExitFromTree", 7);

            put("FirstTreeToCheckpoint", 8);
            put("SecondTreeToCheckpoint", 9);
            put("ThirdTreeToCheckpoint", 10);

            put("CheckPointToAppleTrash", 11);
            put("CheckPointToPearTrash", 12);
            put("CheckPointToUnripTrash", 13);
            put("CheckPointToSmallAppleTrash",14);

            put("AppleTrashToCheckPoint", 15);
            put("PearTrashToCheckPoint", 16);
            put("UnripTrashToCheckPoint", 17);
            put("SmallAppleTrashToCheckPoint",18);

            put("CheckPointToEnd", 19);

            put("FruitIn", 20);
            put("FruitInSmall", 21);
            put("End", 22);

            put("ScanFirst", 23);
            put("ScanSecond", 24);
            put("ScanThird", 25);

            put("ScanFirstTree", 26);
            put("ScanSecondTree", 27);
            put("ScanThirdTree", 28);
        }
    };
    /**
     * Метод предназначен для получения номера массива, в котором содержиться текущие действие для логики
     */
    public int getIndexForActionMap(String action)
    {
        return actionKeysMap.getOrDefault(action, 22);
    }
}
