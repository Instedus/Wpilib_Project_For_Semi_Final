package frc.robot.commands;

import frc.robot.Autonomus.InitLogicAuto;
import frc.robot.casesOMS.*;

public class StatesOMS{

    public static float posLift;
    public static float posForward;
    public static float posGrab;
    public static float posGrabRotate;

    public static IState[][] statesOMS = new IState[][]{
        {
            new InfinityCycle(),
        },
        {
            new OMS(-2, 15, 110,-1),
            new InfinityCycle(),
        },
        {
            new OMS(0,-1,-1,-1),
            new InfinityCycle(),
        },
        {
            new OMS(-1, 1, 110, 80), 
            new InfinityCycle(),
        },
        {
            new OMS(0,1,210,-1),
            new InfinityCycle(),
        },
        {
            new OMS(0,1,-1,-1),
            new InfinityCycle(),
        }
    };

}