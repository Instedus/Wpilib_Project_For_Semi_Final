package frc.robot.logic;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import frc.robot.subsystems.vision.Fructs;



public class InitLogic {
    private ArrayList<DriveElements> arrayWithLogic = new ArrayList<>();
    public ArrayList<int[]> indexMas = new ArrayList<>();

    private final String[] FRUIT = { "purpleFruit", "bigApple", "bigPear", "smallApple" };

    //g1
    // final String[][] EARTH = {
    //     { 
    //         "none",FRUIT[0],"none",
    //         FRUIT[0],       "none", 
    //      },
    //      {
    //         "none",FRUIT[0],"none", 
    //         "none",      "none",  
    //      },
    //      {
    //         "none",FRUIT[0],"none",
    //         "none",       "none", 
    //      }, 
    // };

    //g2
    final String[][] EARTH = {
        { 
            "none","none","none", 
            "none",       "none", 
        },
        {
            "none","none","none", 
            "none",       "none",  
        },
        {
            "none","none","none",
            "none",       FRUIT[0], 
        },// для тестов
        // {
        //     FRUIT[0],FRUIT[0],FRUIT[0],
        //     FRUIT[0],       FRUIT[0], 
        // },  
    };

    private final String[][] TREE = {
        { "none","none","none", },
        { "none","none","none", },
        {FRUIT[0],FRUIT[0],FRUIT[0], }, 
    };   

    // private final String[][] TREE = {
    //     { "none","none","none",},
    //     { "none","none","none",},
    //     { "none","none","none",}, 
    // };   
    
  
     
    private final float[][][] toZone = {
        {
            {350, 0},{-30, 0},{-350, 0},
            {465, -326},       {-480, -375}, 
        },
        { 
            {350, 0},{-30, 0},{-350, 0},
            {465, -326},       {-545, -427},                                     
        },
        { 
            {350, 0},{-30, 0},{-350, 0},
            {485, -300},       {-480, -375}, 
        },
    };

    private final float[][][] toTree = { 
        {
            {-150, -130}, {-10, 0}, {100,-150}
        },
        {
            {-150, -130}, {-10, 0}, {90,-175}
        },
        {
            {-150, -130}, {-10, 0}, {150,-175} 
        },
    };

    private final float[][][] fromTree = {
        {
            {150, 130}, {10, 0}, {100,150}
        },
        {
            {172, 121}, {10, 0}, {90,175}
        },
        {
            {172, 121}, {10, 0}, {90,175} 
        },
    };

    private final float[][][] fromZone = {
        {
            {-350,0},{30,0},{350,0},
            {-591, 0},       {692, 0},
        },
        {
            {-350,0},{30,0},{350,0},
            {-591, 0},       {692, 0},
        },
        {
            {-350,0},{30,0},{350,0},
            {-591, 0},       {692, 0},
        },
    };

    private SettingForLogic settings = new SettingForLogic(TREE, EARTH);

    public void initLogic() {
        ArrayList<ArrayList<String>> allocationElements = generationElements(settings.getCommands());

        setPathRetEarth(allocationElements.get(0));
        setPathRetTree(allocationElements.get(1));

        if(arrayWithLogic.size() < 1){
            arrayWithLogic.add(new DriveElements("End"));
        }else{
            arrayWithLogic.add(new DriveElements("CheckPointToEnd"));
        }
        
        generationLogic();
    }

    private void setPathRetEarth(ArrayList<String> commands) {
        if (commands == null || commands.isEmpty())
            return;

        for (String command : commands) {
            String[] parts = command.split(" ");
            String zone = parts[1];
            String fruit = parts[2];
            String[] coords = parts[3].split(",");
            int[] driveCoords = getCoordinate(coords, toZone);
            int[] exitCoords = getCoordinate(coords, fromZone);

            arrayWithLogic.add(new DriveElements("CheckPointTo" + zone + "Tree"));
            arrayWithLogic.add(new DriveElements("DriveToZona", driveCoords[0], driveCoords[1]));
            arrayWithLogic.add(new DriveElements(fruit.equalsIgnoreCase(FRUIT[3]) ? "FruitInSmall" : "FruitIn", 0));
            arrayWithLogic.add(new DriveElements("ExitFromZona", exitCoords[0], exitCoords[1]));
            arrayWithLogic.add(new DriveElements(zone + "TreeToCheckpoint"));
            addTrashLogic(fruit);
        }
    }

    private void setPathRetTree(ArrayList<String> commands) {
        if (commands == null || commands.isEmpty())
            return;

        for (String command : commands) {
            String[] parts = command.split(" ");
            String zone = parts[1];
            String fruit = parts[2];
            String branch = parts[3];
            String[] coords = { String.valueOf(getIndex(zone)), String.valueOf(getIndex(branch)) };
            int[] driveCoords = getCoordinate(coords, toTree);
            int[] exitCoords = getCoordinate(coords, fromTree);

            arrayWithLogic.add(new DriveElements("CheckPointTo" + zone + "Tree"));
            arrayWithLogic.add(new DriveElements("DriveToTree", driveCoords[0], driveCoords[1]));
            arrayWithLogic.add(new DriveElements(fruit.equalsIgnoreCase(FRUIT[3]) ? "FruitInSmall" : "FruitIn",
                    getIndex(branch) + 1));
            arrayWithLogic.add(new DriveElements("ExitFromTree", exitCoords[0], exitCoords[1]));
            arrayWithLogic.add(new DriveElements(zone + "TreeToCheckpoint"));
            addTrashLogic(fruit);
        }
    }

    private int[] getCoordinate(String[] coordinate, float[][][] array) {
        int x = Integer.parseInt(coordinate[0]);
        int y = Integer.parseInt(coordinate[1]);

        return new int[] { Math.round(array[x][y][0]), Math.round(array[x][y][1]) };
    }

    private ArrayList<ArrayList<String>> generationElements(ArrayList<String> commands) {
        ArrayList<String> elementsEarth = commands.stream().filter(s -> s.startsWith("EARTH "))
                .collect(Collectors.toCollection(ArrayList::new));

        ArrayList<String> elementsTree = commands.stream().filter(s -> s.startsWith("TREE "))
                .collect(Collectors.toCollection(ArrayList::new));

        return new ArrayList<>(List.of(elementsEarth, elementsTree));
    }

    private void generationLogic() {
        ActionIndex mapper = new ActionIndex();

        for (DriveElements element : arrayWithLogic) {
            System.out.println(element.getAction() + " " + element.getPositionLift() + " " + element.getCoordinateX()
                    + " " + element.getCoordinateY());
        }

        for (DriveElements s : arrayWithLogic) {
            int[] indexArray = new int[] { mapper.getIndexForActionMap(s.getAction()), s.getPositionLift(),
                    s.getCoordinateX(), s.getCoordinateY() };
            indexMas.add(indexArray);
        }

        for (int[] index : indexMas) {
            System.out.println(Arrays.toString(index));
        }
    }

    private void addTrashLogic(String fruit) {
        String trashType = "trash";
        switch (fruit.toLowerCase()) {
        case "purplefruit":
            trashType = "UnripTrash";
            break;
        case "bigapple":
            trashType = "AppleTrash";
            break;
        case "bigpear":
            trashType = "PearTrash";
            break;
        default:
            trashType = "SmallAppleTrash";
            break;
        }
        arrayWithLogic.add(new DriveElements("CheckPointTo" + trashType));
        arrayWithLogic.add(new DriveElements(trashType + "ToCheckPoint"));
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
}
