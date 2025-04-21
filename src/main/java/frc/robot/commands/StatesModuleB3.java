package frc.robot.commands;

import edu.wpi.first.wpilibj.Timer;
import frc.robot.cases.Camera;
import frc.robot.cases.End;
import frc.robot.cases.InitializeLogic;
import frc.robot.cases.Lights;
import frc.robot.cases.Odometry;
import frc.robot.cases.Reset;
import frc.robot.cases.SensorsTest;
import frc.robot.cases.Start;
import frc.robot.cases.StartTransition;
import frc.robot.cases.Switch;
import frc.robot.cases.TimerCount;
import frc.robot.cases.TimerDrive;
import frc.robot.cases.Transition;
import frc.robot.casesOMS.OMS;

public class StatesModuleB3{

    public final static String abs = "abs";
    public final static String rel = "rel";
    public final static String ikl = "ikl";
    public final static String ikr = "ikr";
    public final static String sonicb = "snonicb";

    public static IState[][] states = new IState[][] { 
        {
            new Start(2f),
            new OMS(-1,15,110,-1),
            new OMS(-500, -1,110,-1), 
            new Camera("main"),
            new End(),
            new StartTransition(2f),
            new OMS(-1,15,110,-1),
            new OMS(-500, -1,110,-1),
            new Camera("purple"), 
            new End(),
            new StartTransition(2f),
            new OMS(-1,15,110,-1),
            new OMS(-500, -1,110,-1),
            new Camera("count"), 
            new End(),
        }
    };
}