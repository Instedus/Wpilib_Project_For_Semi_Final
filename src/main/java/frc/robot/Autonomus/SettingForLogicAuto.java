package frc.robot.Autonomus;

import java.util.ArrayList;

public class SettingForLogicAuto {
    private final int[][] tree;
    private final int[][] earth;

    private final String[] zona = {"First", "Second", "Third"};

    public SettingForLogicAuto(int[][] tree, int[][] earth) {
        this.tree = tree;
        this.earth = earth;
    }

    public ArrayList<String> getCommands() {
        ArrayList<String> commands = new ArrayList<>();

        for (int i = 0; i < earth.length; i++) {
            for (int j = 0; j < earth[i].length; j++) {
                int count = earth[i][j];
                if (count != -1 && count != 0) {
                    for (int k = 0; k < count; k++) {
                        commands.add("EARTH" + " " + zona[i] + " " + i + "," + j);
                    }
                }
            }
        }

        for (int i = 0; i < tree.length; i++) {

            for (int j = 0; j < tree[i].length; j++) {
                if (tree[i][j] != -1 && tree[i][j] != 0) {
                    commands.add("TREE" + " " + zona[i] + " " + zona[j]);
                }
            }
        }

        return commands;
    }
}
