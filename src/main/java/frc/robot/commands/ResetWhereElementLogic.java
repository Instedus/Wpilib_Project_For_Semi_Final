package frc.robot.commands;

public class ResetWhereElementLogic implements IState{

    @Override
    public void initialize() {
    }

    @Override
    public boolean execute() {
        StateMachine.absoluteZ = -1;
        return true;
    }

}