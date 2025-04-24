package frc.robot.commands;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.RobotContainer;
import frc.robot.subsystems.Training;

public class StateMachine extends CommandBase{

    Training train = RobotContainer.train;
    public static float startTime = 0;
    private boolean isInitialize = true;
    public static boolean checkElement = false;
    
    public static int currentIndex = 0;
    public static int currentArray = 0;
    public static boolean isEnd, isFirst = false;

    public static int indexElementLogic = 0;
    public static int positionOMSLogic = -1;

    public static boolean checkCenter = false;

    public static float logCordX = 0, logCordY = 0;

    public static int countOnGround = 0;

    public static boolean onGround;

    public static float absoluteZ = -1;

    public static boolean isAutonomusOMS = false;

    IState localStates[][];

    public static String TRASH = "none";

    public StateMachine() 
    {
        localStates = StatesAutoFinal.states;
        addRequirements(train);
    }

    @Override                                                    
    public void execute()
    {
        if (this.isInitialize) 
        {
            localStates[currentArray][currentIndex].initialize();
            this.isInitialize = false;
        }

        if (checkElement) 
        {
            startTime = (float) Timer.getFPGATimestamp();
            checkElement = false;
        }
        
        if(localStates[currentArray][currentIndex].execute()){
            startTime = (float) Timer.getFPGATimestamp();
            StateMachineOMS.isFirst = true;
            this.isInitialize = true;
            checkElement = false;
            isFirst = true;
            isEnd = false;
            currentIndex++;
        }
        SmartDashboard.putNumber("curArray", currentArray);
        SmartDashboard.putNumber("curIndex", currentIndex);
        SmartDashboard.putNumber("logCordX", logCordX);
        SmartDashboard.putNumber("logCordY", logCordY);
        SmartDashboard.putString("RECOGNISED_TRASH", TRASH);

        StateMachineOMS.Update();
    }

    @Override
    public void initialize(){
        train.resetEnc();
        train.resetGyro(0);
        train.resetCord(0, 0);
        startTime = (float) Timer.getFPGATimestamp();
        StateMachineOMS.Initialize();
    }
}