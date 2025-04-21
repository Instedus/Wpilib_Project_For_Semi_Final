package frc.robot.logic;

import java.util.ArrayList;

public class SettingForLogic {
    private final String[][] tree;
    private final String[][] earth;

    private final String[] zona = { "First", "Second", "Third" };

    public SettingForLogic(String[][] tree, String[][] earth) {
        this.tree = tree;
        this.earth = earth;
    }

    public ArrayList<String> getCommands() {
        ArrayList<String> commands = new ArrayList<>();

        for (int i = 0; i < earth.length; i++) {
            for (int j = 0; j < earth[i].length; j++) {
                String fruit = earth[i][j];
                if (!fruit.equalsIgnoreCase("none") && !fruit.isEmpty()) {
                    commands.add("EARTH" + " " + zona[i] + " " + fruit + " " + i + "," + j);
                }
            }
        }

        for (int i = 0; i < tree.length; i++) {

            for (int j = 0; j < tree[i].length; j++) {
                String fruit = tree[i][j];
                if (!fruit.equalsIgnoreCase("none") && !fruit.isEmpty()) {
                    commands.add("TREE" + " " + zona[i] + " " + fruit + " " + zona[j]);
                }
            }
        }

        return commands;
    }
}
