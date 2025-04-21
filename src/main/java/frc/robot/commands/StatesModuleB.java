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
import frc.robot.cases.Switch;
import frc.robot.cases.TimerCount;
import frc.robot.cases.TimerDrive;
import frc.robot.cases.Transition;
import frc.robot.casesOMS.OMS;

public class StatesModuleB{

    public final static String abs = "abs";
    public final static String rel = "rel";
    public final static String ikl = "ikl";
    public final static String ikr = "ikr";
    public final static String sonicb = "snonicb";

    public static IState[][] states = new IState[][] { 
        {
            //модуль б
            new Start(1f),

            new Odometry(2000,0,-90),
            new Odometry(690, 0, 0),
            new TimerCount(1f),
            new SensorsTest(ikr, 15, 0, 1),
            new SensorsTest(ikr, 22, 0, 0),
            new Reset(-1, -1, -90),
            new Odometry(0, 0, -90),
            new Odometry(650, 0, 0),
            new TimerCount(1f),
            new SensorsTest(ikr, 15, 0, 1),
            new Reset(-1, -1, -180),
            new TimerCount(1f),
            new Odometry(-750, 0, -90),
            new Odometry(690, 0, 0),
            new TimerCount(1f),
            new SensorsTest(ikr, 15, 0, 1),
            new SensorsTest(ikr, 25, 0, 0),
            new Odometry(0, 0, -90),
            new SensorsTest(ikr, 15, 0, 1),
            new Odometry(-950, 0, 0),
            new TimerCount(1f),
            new Odometry(730, 0, -90),
            new Odometry(710,0, 90),
            new TimerCount(1f),
 
            new End(),
        }
    };
}