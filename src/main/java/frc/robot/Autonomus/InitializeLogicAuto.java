package frc.robot.Autonomus;

import frc.robot.RobotContainer;
import frc.robot.commands.IState;
import frc.robot.commands.StateMachine;

public class InitializeLogicAuto implements IState {

    private InitLogicAuto init = RobotContainer.initLogicAuto;

    @Override
    public void initialize() {

    }

    @Override
    public boolean execute() {
        init.resetLogic();
        StateMachine.indexElementLogic = 0;
        if (StateMachine.onGround) {
            init.setOrder(StateMachine.countOnGround, 0);
        } else {
            init.setOrder(0, StateMachine.countOnGround);
        }
        init.initLogic();
        return true;
    }

}