package frc.robot.cases;

import frc.robot.RobotContainer;
import frc.robot.commands.IState;
import frc.robot.commands.StateMachine;
import frc.robot.subsystems.Training;

public class SwitchJOY implements IState{

    Training train = RobotContainer.train;

    @Override
    public boolean execute() {
        StateMachine.currentIndex = 0;
        return true;
    }

    @Override
    public void initialize() {
    }

}