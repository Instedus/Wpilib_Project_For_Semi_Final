package frc.robot.commands;

import frc.robot.cases.End;
import frc.robot.cases.JOY;
import frc.robot.cases.Odometry;
import frc.robot.cases.SensorsTest;
import frc.robot.cases.Start;
import frc.robot.cases.SwitchJOY;
import frc.robot.casesOMS.OMS;

public class    StatesJOY{
    public final static String abs = "abs";
    public final static String rel = "rel";
    public final static String ikl = "ikl";
    public final static String ikr = "ikr";
    public final static String sonicb = "snonicb";

    public static IState[][] states = new IState[][]{
        {
            new Start(2f), 
            new JOY(),
            new SensorsTest(sonicb, 25, 0, 0),
            new Odometry(0, 0, -90),
            new SensorsTest(ikr, 15, 0, 1),
            new SwitchJOY(),
            new End(),
        },
    };
}                                                                               