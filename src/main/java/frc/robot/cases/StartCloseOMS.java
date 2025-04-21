package frc.robot.cases;

import frc.robot.RobotContainer;
import frc.robot.commands.IState;
import frc.robot.subsystems.Function;
import frc.robot.subsystems.Training;

public class StartCloseOMS implements IState {
    float waitTime = 2f;
    float count;
    Training train = RobotContainer.train;
    boolean isServoForwardReached = false;

    public StartCloseOMS(float waitTime) {
        this.waitTime = waitTime;
    }

    @Override
    public boolean execute() {

        count += Function.countTimer();

        train.grabAngle = 220;

        train.destination = 1;

        train.positionLift = 0;

        train.resetEnc();
        train.resetGyro(0);
        train.resetCord(0, 0);

        boolean initOms = train.getLimitSwitch();
        if (train.getStart() && (count > waitTime) && initOms) {
            train.setRed(true);
            train.setGreen(true);
            return true;
        } else if (count > waitTime) {
            train.setRed(true);
            train.setGreen(false);
            return false;
        } else {
            train.setRed(false);
            train.setGreen(false);
            return false;
        }
    }

    @Override
    public void initialize() {
        isServoForwardReached = false;
    }

}