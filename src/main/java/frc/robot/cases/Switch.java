package frc.robot.cases;

import frc.robot.RobotContainer;
import frc.robot.commands.IState;
import frc.robot.subsystems.Training;

public class Switch implements IState{

    Training train = RobotContainer.train;

    @Override
    public void initialize() {
    }

    @Override
    public boolean execute() {
        return train.getStart();
    }

}