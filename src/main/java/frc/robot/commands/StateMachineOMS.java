package frc.robot.commands;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class StateMachineOMS{

    public static int currentIndexOMS;
    public static int currentArrayOMS;

    public static boolean isFirst;

    static boolean isInit = true;

    public static void Update(){

        SmartDashboard.putNumber("currentIndexOMS", currentIndexOMS);
        SmartDashboard.putNumber("currentArrayOMS", currentArrayOMS);
        SmartDashboard.putNumber("logLift", StatesOMS.posLift);
        SmartDashboard.putNumber("logForward", StatesOMS.posForward);
        SmartDashboard.putNumber("logGrab", StatesOMS.posGrab);
        SmartDashboard.putNumber("logGrabRotate", StatesOMS.posGrabRotate);

        if(StatesOMS.statesOMS[currentArrayOMS][currentIndexOMS].execute()){

            StateMachine.isAutonomusOMS = false;
            isFirst = true;
            isInit = true;
            currentIndexOMS++;
        }
    }

    public static void Initialize(){
        isFirst = true;
        StatesOMS.posLift = -1;
        StatesOMS.posForward = -1;
        StatesOMS.posGrab = -1;
        StatesOMS.posGrabRotate = -1;
    }

}