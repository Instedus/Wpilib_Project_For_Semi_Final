package frc.robot.cases;

import frc.robot.RobotContainer;
import frc.robot.commands.IState;
import frc.robot.subsystems.Training;

public class End implements IState{

    Training train = RobotContainer.train;

    @Override
    public boolean execute() {
        train.setAxisSpeed(0, 0);
        train.setGreen(false);
        train.setRed(false);
        return train.getStart();
    }

    @Override
    public void initialize() {
    }

}