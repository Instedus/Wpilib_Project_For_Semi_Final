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

public class StatesModuleB1{

    public final static String abs = "abs";
    public final static String rel = "rel";
    public final static String ikl = "ikl";
    public final static String ikr = "ikr";
    public final static String sonicb = "snonicb";

    public static IState[][] states = new IState[][] { 
        {
            
            new Start(2f),
            // // // new Reset(0,0,0),
            // // // //квадрат

            // new Odometry(abs, 1150, 0, 90, true),
            // new Odometry(abs, 1150, 1150, 180, true),
            // new Odometry(abs, 0, 1150, 270, true),
            // new Odometry(abs, 0, 0, 360, true),

            // new End(),
            // new StartTransition(2f),
            // // // new Reset(0,0,0),
 
            // // // // // //проезд на 1 метр вперед
            
            // new Odometry(abs, 1400, 0, 0, true),

            // new End(),
            // new StartTransition(2f),
            // // // // // поворот
            // new Odometry(0, 0, 90),
            // new Odometry(0, 0, 90),
            // new Odometry(0, 0, 90),
            // new Odometry(0, 0, 90),
            // new Odometry(0, 0, 90),

            // new End(),
            // new StartTransition(2f),
            // // // // // датчики
            new Lights(true),

            new End(),
            new StartTransition(2f),

            new Lights(false),
            new End(),
        }
    };
}