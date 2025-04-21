package frc.robot.logic;

/**
 * Класс предназначен для реализации комманд для логики
 */
public class DriveElements {
    private final String action;
    private final int positionLift;

    private final int coordinateX;
    private final int coordinateY;

    public DriveElements(String action, int positionLift, int coordinateX, int coordinateY) {
        this.action = action;
        this.positionLift = positionLift;
        this.coordinateX = coordinateX;
        this.coordinateY = coordinateY;
    }

    public DriveElements(String action) {
        this(action, -1, 0, 0);
    }

    public DriveElements(String action, int positionLift) {
        this(action, positionLift, 0, 0);
    }

    public DriveElements(String action, int coordinateX, int coordinateY) {
        this(action, -1, coordinateX, coordinateY);
    }

    public String getAction() {
        return action;
    }

    public int getPositionLift() {
        return positionLift;
    }

    public int getCoordinateX() {
        return coordinateX;
    }

    public int getCoordinateY() {
        return coordinateY;
    }
}
