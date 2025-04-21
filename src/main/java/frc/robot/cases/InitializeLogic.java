package frc.robot.cases;

import frc.robot.RobotContainer;
import frc.robot.commands.IState;
import frc.robot.logic.InitLogic;

/**
 * Данный класс предназначен для запуска инциализации логики
 * Класс необходимо вызывать в самом первом массиве
 */
public class InitializeLogic implements IState 
{

    private InitLogic log = RobotContainer.initLogic;

    @Override
    public boolean execute() 
    {
        log.initLogic();
        RobotContainer.train.setAxisSpeed(0, 0);

        return true;
    }

    @Override
    public void initialize() {
    }

}