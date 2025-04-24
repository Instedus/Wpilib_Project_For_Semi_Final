package frc.robot.Autonomus;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import frc.robot.commands.StateMachine;

public class InitLogicAuto {

    // Константы и структуры данных
    private final String[] ZONES = {"First", "Second", "Third"};
    private final int[] TRANSITION_SEQUENCE = {0, 1, 3, 2, 5};

    // Структуры данных для хранения состояний
    public int[][] EARTH = { // Матрица зон (3 зоны x 6 областей)
            {
                    0, 0, 0,
                    0, -1, 0,
            },
            {
                    0, 0, 0,
                    0, -1, 0,
            },
            {
                    0, 0, 0,
                    0, -1, 0,
            },
    };

    private final int[][] TREE = {  // Матрица деревьев (3 зоны x 3 ветки)
            {0, 0, 0},
            {0, 0, 0},
            {0, 0, 0},
    };


    private final float[][][] toZone = {
        {
            {350, 0, 0}, {-30, 0, 0}, {-350, 0, 0},
            {465, -300, 0}, {-1,-1,-1}, {-545, -427, 0}, 
        },
        {
            {350, 0, 0}, {-30, 0, 0}, {-350, 0, 0},
            {465, -300, 0}, {-1,-1,-1}, {-545, -427, 0}, 
        },
        {
            {350, 0, 0}, {-30, 0, 0}, {-350, 0, 0},
            {485, -300}, {-1,-1,-1}, {-480, -375},
        },
    };
    private final float[][][] fromZone = {
        {
            {-350, 0, 0},{30,0,0},{350, 0, 0},
            {-570, 0,0},{-1,-1,-1},{692, 0, 0},
        },
        {
            {-350,0,0},{30,0,0},{350, 0, 0},
            {-570, 0,0},{-1,-1,-1},{692, 0,0},
        },
        {
            {-350, 0, 0},{30, 0, 0},{350, 0, 0},
            {-510, 0 , 0},{-1,-1,-1},{610, 0,0},
        },
    };

    private final float[][][] toTree = {
            // {
            //     {-10, 0, 0}, {-130, -20, 0}, {172,-30, 0}
            // },
            // {
            //     {20,0, 0}, {-130, -40, 0}, {172,-40, 0}
            // },
            // {
            //     {20,0, 0}, {-130, -40, 0}, {180,-15, 0} 
            // },
            {
                {-150, -130,0}, {-10, 0,0}, {100,-150,0}
            },
            {
                {-150, -130,0}, {-10, 0,0}, {100,-150,0}
            },
            {
                {-150, -130,0}, {-10, 0,0}, {100,-150,0} 
            },
    };

    private final float[][][] fromTree = {
        {
            {150, 130,0}, {10, 0,0}, {100,150,0}
        },
        {
            {150, 130,0}, {10, 0,0}, {100,150,0}
        },
        {
            {150, 130,0}, {10, 0,0}, {100,150,0}
        },
    };


    // Переменные состояния
    int countZonaAutonomous = 0;  // Текущая зона (0-2)
    int countAreaAutonomous = 0; // Индекс в TRANSITION_SEQUENCE (0-4)
    int countBranchAutonomous = 0; // Текущая ветка (0-2)
    int targetArea = 0;
    int targetBranch = 0;
    boolean autoEarth = true;  // Режим работы с землей
    boolean autoTree = false; // Режим работы с деревьями
    boolean isFinished = false; // Флаг завершения

    // Коллекции для хранения логики
    private final ArrayList<DriveElementsAuto> arrayWithLogic = new ArrayList<>();
    public ArrayList<int[]> indexMas = new ArrayList<>();

    // Настройки
    private final SettingForLogicAuto settings = new SettingForLogicAuto(TREE, EARTH);

    // Методы для инициализации и логики
    public void initLogic() {
        if (isFinished) return; // Если уже завершено, не выполняем повторную инициализацию

        // Генерация элементов (земля и деревья)
        ArrayList<ArrayList<String>> allocationElements = generationElements(settings.getCommands());

        // Выбираем действие в зависимости от текущей зоны и режима
        if (countAreaAutonomous >= 0 && countAreaAutonomous <= 5 && autoEarth) {
            setPathRetEarth(allocationElements.get(0)); // Работа с землей
        } else if (targetBranch < 4 && autoTree) {
            setPathRetTree(allocationElements.get(allocationElements.size() - 1)); // Работа с деревьями
        }
        // Генерация логики действий
        generateActionLogic();
    }

    /**
     * Разделяет команды на категории (земля и деревья).
     */
    private ArrayList<ArrayList<String>> generationElements(ArrayList<String> commands) {
        // Разделяем команды на элементы для земли и деревьев
        ArrayList<String> elementsEarth = commands.stream()
                .filter(s -> s.startsWith("EARTH "))
                .collect(Collectors.toCollection(ArrayList::new));

        ArrayList<String> elementsTree = commands.stream()
                .filter(s -> s.startsWith("TREE "))
                .collect(Collectors.toCollection(ArrayList::new));

        return new ArrayList<>(List.of(elementsEarth, elementsTree));  // Возвращаем как пару списков
    }

    /**
     * Генерация логики действий на основе текущего состояния.
     */
    public void generateActionLogic() {
        ActionIndexAuto mapper = new ActionIndexAuto(); // Создаем маппер для действий

        // Вывод логики действий
        for (DriveElementsAuto element : arrayWithLogic) {
            System.out.println(element.getAction() + " " + element.getPositionLift() + " " + element.getCoordinateX() + " " + element.getCoordinateY() + " " + element.getCoordinateZ());
        }
// Обработка каждого элемента логики
        for (DriveElementsAuto s : arrayWithLogic) {
            if (s == null) continue; // Пропускаем пустые элементы

            // Получаем индекс действия для маппера
            int ActionIndex = mapper.getIndexForActionMap(s.getAction());
            if (ActionIndex == -1) {
                System.err.println("Неизвестное действие: " + s.getAction());
                continue;  // Если действие неизвестно, пропускаем
            }

            // Добавляем сгенерированные индексы в коллекцию
            int[] indexArray = new int[]{ActionIndex, s.getPositionLift(), s.getCoordinateX(), s.getCoordinateY(), s.getCoordinateZ()};
            indexMas.add(indexArray);
        }

        // Вывод сгенерированных индексов
        for (int[] index : indexMas) {
            System.out.println(Arrays.toString(index));
        }
    }

    // Методы управления маршрутами
    private void setPathRetEarth(ArrayList<String> commands) {
        // Логика для работы с Землей
        if (commands.isEmpty()) {
            int[] currentDriveCoords = getCoordinate(new String[]{String.valueOf(countZonaAutonomous), String.valueOf(targetArea)}, fromZone);
            arrayWithLogic.add(new DriveElementsAuto("ExitFromZona", currentDriveCoords[0], currentDriveCoords[1], currentDriveCoords[2]));
            // Переход к следующей зоне, если текущая зона завершена
            if (countAreaAutonomous == 5) {
                arrayWithLogic.add(new DriveElementsAuto(ZONES[countZonaAutonomous] + "TreeToCheckpoint"));
                int nextZona = countZonaAutonomous + 1;
                if (nextZona != 3) {
                    System.out.println("nextZona " + nextZona);
                    arrayWithLogic.add(new DriveElementsAuto("CheckPointTo" + ZONES[nextZona] + "Tree"));
                    int[] nextCoords = getCoordinate(new String[]{String.valueOf(countZonaAutonomous), String.valueOf(0)}, toZone);
                    arrayWithLogic.add(new DriveElementsAuto("DriveToZona", nextCoords[0], nextCoords[1], nextCoords[2]));
                    arrayWithLogic.add(new DriveElementsAuto("Scan" + ZONES[nextZona], 0));
                } else {
                    // Если зона завершена, переходим к дереву
                    arrayWithLogic.add(new DriveElementsAuto("CheckPointTo" + ZONES[0] + "Tree"));
                    int[] nextCoords = getCoordinate(new String[]{String.valueOf(0), String.valueOf(0)}, toTree);
                    arrayWithLogic.add(new DriveElementsAuto("DriveToTree", nextCoords[0], nextCoords[1], nextCoords[2]));
                    arrayWithLogic.add(new DriveElementsAuto("Scan" + ZONES[0] + "Tree", (countBranchAutonomous + 1)));
                }
            } else {
                int nextArea = findNextElement(TRANSITION_SEQUENCE, targetArea);
                int[] nextCoords = getCoordinate(new String[]{String.valueOf(countZonaAutonomous), String.valueOf(nextArea)}, toZone);
                arrayWithLogic.add(new DriveElementsAuto("DriveToZona", nextCoords[0], nextCoords[1], nextCoords[2]));
                arrayWithLogic.add(new DriveElementsAuto("Scan" + ZONES[countZonaAutonomous], 0));
            }
        } else {
            for (int i = 0; i < Objects.requireNonNull(commands).size(); i++) {
                String command = commands.get(i);
                System.out.println(command);

                String[] parts = command.split(" ");
                String zone = parts[1];
                String[] coords = parts[2].split(",");
                int[] driveCoords = getCoordinate(coords, toZone);
                int[] exitCoords = getCoordinate(coords, fromZone);

                //включить если нужен отъезд от зоны и убрать if != 0 в else

                // if (i == 0) {
                //     arrayWithLogic.add(new DriveElements("ExitFromZona", exitCoords[0], exitCoords[1], exitCoords[2]));
                // } else {
                if (i != 0) {
                    arrayWithLogic.add(new DriveElementsAuto("CheckPointTo" + zone + "Tree"));
                }
                // }

                // arrayWithLogic.add(new DriveElements("DriveToZona", driveCoords[0], driveCoords[1], driveCoords[2]));
                arrayWithLogic.add(new DriveElementsAuto("FruitIn", 0));
                arrayWithLogic.add(new DriveElementsAuto("ExitFromZona", exitCoords[0], exitCoords[1], exitCoords[2]));
                arrayWithLogic.add(new DriveElementsAuto(zone + "TreeToCheckpoint"));
                addTrashLogic();

                if (i == commands.size() - 1) {
                    if (countAreaAutonomous == 5 && countZonaAutonomous < 2) {
                        int nextZona = countZonaAutonomous + 1;
                        arrayWithLogic.add(new DriveElementsAuto("CheckPointTo" + ZONES[nextZona] + "Tree"));
                        int[] nextCoords = getCoordinate(new String[]{String.valueOf(countZonaAutonomous), String.valueOf(0)}, toZone);
                        arrayWithLogic.add(new DriveElementsAuto("DriveToZona", nextCoords[0], nextCoords[1], nextCoords[2]));
                        arrayWithLogic.add(new DriveElementsAuto("Scan" + ZONES[nextZona], 0));
                    } else if (countAreaAutonomous == 5 && countZonaAutonomous == 2) {
                        arrayWithLogic.add(new DriveElementsAuto("CheckPointTo" + ZONES[0] + "Tree"));
                        int[] nextCoords = getCoordinate(new String[]{String.valueOf(0), String.valueOf(0)}, toTree);
                        arrayWithLogic.add(new DriveElementsAuto("DriveToTree", nextCoords[0], nextCoords[1], nextCoords[2]));
                        arrayWithLogic.add(new DriveElementsAuto("Scan" + ZONES[0] + "Tree", (countBranchAutonomous + 1)));
                    } else {
                        arrayWithLogic.add(new DriveElementsAuto("CheckPointTo" + zone + "Tree"));
                        int nextArea = findNextElement(TRANSITION_SEQUENCE, targetArea);
                        int[] nextCoords = getCoordinate(new String[]{String.valueOf(countZonaAutonomous), String.valueOf(nextArea)}, toZone);
                        arrayWithLogic.add(new DriveElementsAuto("DriveToZona", nextCoords[0], nextCoords[1], nextCoords[2]));
                        arrayWithLogic.add(new DriveElementsAuto("Scan" + ZONES[countZonaAutonomous], 0));
                    }
                }
            }
        }
    }

    private void setPathRetTree(ArrayList<String> commands) {
        System.out.println("Дерево" + commands.size() + " " + countZonaAutonomous + " " + countBranchAutonomous);
        if (commands.isEmpty()) {
            int[] currentDriveCoords = getCoordinate(new String[]{String.valueOf(countZonaAutonomous), String.valueOf(targetBranch)}, fromTree);
            arrayWithLogic.add(new DriveElementsAuto("ExitFromTree", currentDriveCoords[0], currentDriveCoords[1], currentDriveCoords[2]));
            if (countBranchAutonomous == 3) {
                arrayWithLogic.add(new DriveElementsAuto(ZONES[countZonaAutonomous] + "TreeToCheckpoint"));
                if (countZonaAutonomous != 2) {
                    int nextZona = countZonaAutonomous + 1;
                    arrayWithLogic.add(new DriveElementsAuto("CheckPointTo" + ZONES[nextZona] + "Tree"));
                    int[] nextCoords = getCoordinate(new String[]{String.valueOf(nextZona), String.valueOf(0)}, toTree);
                    arrayWithLogic.add(new DriveElementsAuto("DriveToTree", nextCoords[0], nextCoords[1], nextCoords[2]));
                    arrayWithLogic.add(new DriveElementsAuto("Scan" + ZONES[nextZona] + "Tree", 0));
                } else {
                    arrayWithLogic.add(new DriveElementsAuto("CheckPointToEnd"));
                }
            } else {
                int nextBranch = targetBranch + 1;
                int[] nextCoords = getCoordinate(new String[]{String.valueOf(countZonaAutonomous), String.valueOf(nextBranch)}, toTree);
                arrayWithLogic.add(new DriveElementsAuto("DriveToTree", nextCoords[0], nextCoords[1], nextCoords[2]));
                arrayWithLogic.add(new DriveElementsAuto("Scan" + ZONES[countZonaAutonomous] + "Tree", (nextBranch + 1)));
            }
        } else {
            for (int i = 0; i < Objects.requireNonNull(commands).size(); i++) {
                String command = commands.get(i);
                String[] parts = command.split(" ");
                String zone = parts[1];
                String branch = parts[2];
                String[] coords = {String.valueOf(getIndex(zone)), String.valueOf(getIndex(branch))};
                int[] driveCoords = getCoordinate(coords, toTree);
                int[] exitCoords = getCoordinate(coords, fromTree);

                if (i != 0) {
                    arrayWithLogic.add(new DriveElementsAuto("CheckPointTo" + zone + "Tree"));
                    arrayWithLogic.add(new DriveElementsAuto("DriveToTree", driveCoords[0], driveCoords[1], driveCoords[2]));
                }

                arrayWithLogic.add(new DriveElementsAuto("FruitIn", getIndex(branch) + 1));
                arrayWithLogic.add(new DriveElementsAuto("ExitFromTree", exitCoords[0], exitCoords[1], exitCoords[2]));
                arrayWithLogic.add(new DriveElementsAuto(zone + "TreeToCheckpoint"));
                addTrashLogic();

                if (i == commands.size() - 1) {
                    if (countBranchAutonomous == 3) {
                        if (countZonaAutonomous != 2) {
                            int nextZona = countZonaAutonomous + 1;
                            arrayWithLogic.add(new DriveElementsAuto("CheckPointTo" + ZONES[nextZona] + "Tree"));
                            int[] nextCoords = getCoordinate(new String[]{String.valueOf(nextZona), String.valueOf(0)}, toTree);
                            arrayWithLogic.add(new DriveElementsAuto("DriveToTree", nextCoords[0], nextCoords[1], nextCoords[2]));
                            arrayWithLogic.add(new DriveElementsAuto("Scan" + ZONES[nextZona] + "Tree", 0));
                        } else {
                            arrayWithLogic.add(new DriveElementsAuto("CheckPointToEnd"));
                        }
                    } else {
                        arrayWithLogic.add(new DriveElementsAuto("CheckPointTo" + zone + "Tree"));
                        int nextBranch = targetBranch + 1;
                        int[] nextCoords = getCoordinate(new String[]{String.valueOf(countZonaAutonomous), String.valueOf(nextBranch)}, toTree);
                        arrayWithLogic.add(new DriveElementsAuto("DriveToTree", nextCoords[0], nextCoords[1], nextCoords[2]));
                        arrayWithLogic.add(new DriveElementsAuto("Scan" + ZONES[countZonaAutonomous] + "Tree", (nextBranch + 1)));
                    }
                }
            }
        }
    }

    public void setOrder(int countFruitsEarth, int countFruitsBranch) {
        if (isFinished) {
            return;
        }

        targetBranch = countBranchAutonomous;

        if (autoEarth) {
            // Проверяем, нужно ли переключиться на autoTree
            if (countZonaAutonomous > 2 && countAreaAutonomous >= TRANSITION_SEQUENCE.length) {
                autoEarth = false;
                autoTree = true;
                countZonaAutonomous = 0; // Сброс для начала работы с деревьями
                countAreaAutonomous = 0;
                System.out.println("Переключение на вывоз деревьев.");
            } else {
                if (countAreaAutonomous < TRANSITION_SEQUENCE.length) {
                    targetArea = TRANSITION_SEQUENCE[countAreaAutonomous];
                    EARTH[countZonaAutonomous][targetArea] = countFruitsEarth;
                    countAreaAutonomous++;
                } else {
                    // Переход к следующей зоне, если области текущей зоны обработаны
                    countZonaAutonomous++;
                    countAreaAutonomous = 0;
                    // Проверяем, не вышли ли за пределы зон
                    if (countZonaAutonomous < 3) {
                        targetArea = TRANSITION_SEQUENCE[countAreaAutonomous];
                        EARTH[countZonaAutonomous][targetArea] = countFruitsEarth;
                        countAreaAutonomous++;
                    }
                }
            }
        }

        if (autoTree) {
            if (countZonaAutonomous >= 3) {
                System.out.println("Все зоны деревьев завершены.");
                isFinished = true;
                return;
            }

            if (targetBranch < 3) {
                if (TREE[countZonaAutonomous][targetBranch] != 0) {
                    System.err.println("Ошибка: Ветка уже заполнена! i=" + countZonaAutonomous + ", ветка=" + targetBranch);
                } else {
                    TREE[countZonaAutonomous][targetBranch] = countFruitsBranch;
                    countBranchAutonomous++;
                }
            } else {
                // Переход к следующей зоне для деревьев
                countZonaAutonomous++;
                countBranchAutonomous = 0;
                // Проверяем, не вышли ли за пределы зон
                if (countZonaAutonomous >= 3) {
                    isFinished = true;
                    System.out.println("Все зоны деревьев завершены.");
                }
            }
        }
    }

    // Методы для работы с координатами
    private int[] getCoordinate(String[] coordinateMas, float[][][] coordinates) {
        // Преобразуем координаты в массив для работы с движением
        int[] coords = new int[3];
        int zoneIndex = Integer.parseInt(coordinateMas[0]);
        int areaIndex = Integer.parseInt(coordinateMas[1]);

        coords[0] = (int) coordinates[zoneIndex][areaIndex][0];
        coords[1] = (int) coordinates[zoneIndex][areaIndex][1];
        coords[2] = (int) coordinates[zoneIndex][areaIndex][2];

        return coords;
    }

    private int findNextElement(int[] arr, int target) {
        for (int i = 0; i < arr.length - 1; i++) {
            if (arr[i] == target) {
                return arr[i + 1];
            }
        }
        return -1;
    }

    private int getIndex(String name) {
        switch (name.toLowerCase()) {
            case "first":
                return 0;
            case "second":
                return 1;
            case "third":
                return 2;
            default:
                return -1;
        }
    }

    // Методы управления логикой и сброса состояний
    private void resetAreaAndBranch() {
        if (autoEarth && countZonaAutonomous < 3) {
            countAreaAutonomous = 0;
        }
        countBranchAutonomous = 0;
        targetArea = 0;
        targetBranch = 0;
        for (int[] row : TREE) {
            Arrays.fill(row, 0);
        }

        for (int k = 0; k < EARTH.length; k++) {
            for (int value : TRANSITION_SEQUENCE) {
                if (value != 4) {
                    EARTH[k][value] = 0;
                }
            }
        }

        System.out.println("Сброс значений для следующей зоны.");
    }

    public void resetLogic() {
        if (countAreaAutonomous == 5) {
            countAreaAutonomous++;
        }
        while ((countAreaAutonomous == 6 && autoEarth && countZonaAutonomous < 3) || (countBranchAutonomous == 3 && autoTree)) {
            countZonaAutonomous++;
            if (countZonaAutonomous >= 3 && countBranchAutonomous == 3) {
                System.out.println("Все зоны завершены.");
                isFinished = true;
                return;
            }
            resetAreaAndBranch();
        }

        arrayWithLogic.clear();
        indexMas.clear();


        for (int[] row : TREE) {
            Arrays.fill(row, 0);
        }

        for (int k = 0; k < EARTH.length; k++) {
            for (int value : TRANSITION_SEQUENCE) {
                if (value != 4 && value != -1) {
                    EARTH[k][value] = 0;
                }
            }
        }
    }

    private void addTrashLogic() {
        // switch (Main.trash.toLowerCase()) {
        //     case "purplefruit":
        //         trashType = "UnripTrash";
        //         break;
        //     case "bigapple":
        //         trashType = "AppleTrash";
        //         break;
        //     case "bigpear":
        //         trashType = "PearTrash";
        //         break;
        //     default:
        //         trashType = "SmallAppleTrash";
        //         break;
        // }

       arrayWithLogic.add(new DriveElementsAuto("CheckPointTo" + StateMachine.TRASH));
       arrayWithLogic.add(new DriveElementsAuto(StateMachine.TRASH + "ToCheckPoint"));
    }

}
