package frc.robot.Autonomus;

/**
 * Класс предназначен для реализации комманд для логики
 */
public class DriveElementsAuto {
    private final String action;
    private final int positionLift;

    private final int coordinateX;
    private final int coordinateY;
    private final int coordinateZ;

    public DriveElementsAuto(String action, int positionLift, int coordinateX, int coordinateY, int coordinateZ) {
        this.action = action;
        this.positionLift = positionLift;
        this.coordinateX = coordinateX;
        this.coordinateY = coordinateY;
        this.coordinateZ = coordinateZ;
    }

    public DriveElementsAuto(String action) {
        this(action, -1, 0, 0, 0);
    }
    public DriveElementsAuto(String action, int positionLift) {
        this(action, positionLift, 0, 0, 0);
    }
    public DriveElementsAuto(String action, int coordinateX, int coordinateY, int coordinateZ) {
        this(action, -1, coordinateX, coordinateY, coordinateZ);
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

    public int getCoordinateZ() {
        return coordinateZ;
    }
}
