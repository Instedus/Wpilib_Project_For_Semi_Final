package frc.robot.Autonomus;

import frc.robot.RobotContainer;
import frc.robot.commands.IState;
import frc.robot.commands.StateMachine;
import frc.robot.subsystems.Training;


/**
 * Данный класс предназначен для перехода по массивам комманд
 */
public class TransitionAuto implements IState
{

    private InitLogicAuto log = RobotContainer.initLogicAuto;
    private Training train = RobotContainer.train;
    
    @Override
    public boolean execute() 
    {
        train.setAxisSpeed(0, 0);
        StateMachine.currentArray = log.indexMas.get(StateMachine.indexElementLogic)[0];
        StateMachine.positionOMSLogic = log.indexMas.get(StateMachine.indexElementLogic)[1];
        StateMachine.logCordX = log.indexMas.get(StateMachine.indexElementLogic)[2];
        StateMachine.logCordY = log.indexMas.get(StateMachine.indexElementLogic)[3];

        if (StateMachine.isFirst) 
        {
            StateMachine.currentIndex = -1;
            StateMachine.indexElementLogic++;
            StateMachine.isFirst = false;
        }
        return true;
    }

    @Override
    public void initialize() {
    }
}
