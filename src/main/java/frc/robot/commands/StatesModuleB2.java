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
import frc.robot.cases.StartCloseOMS;
import frc.robot.cases.StartTransition;
import frc.robot.cases.Switch;
import frc.robot.cases.TimerCount;
import frc.robot.cases.TimerDrive;
import frc.robot.cases.Transition;
import frc.robot.casesOMS.OMS;

public class StatesModuleB2{

    public final static String abs = "abs";
    public final static String rel = "rel";
    public final static String ikl = "ikl";
    public final static String ikr = "ikr";
    public final static String sonicb = "snonicb";

    public static IState[][] states = new IState[][] { 
        {
            // //в2
            new Start(2f),
            // // // // //new змейка
            new Odometry(abs, 1000, 0, 0, true),
            new Odometry(abs, 1000,-1000,0,true), 
            new Odometry(abs, 1700, -1000, 0, true),
            new Odometry(abs, 1700, -200, 0, true),
            new Odometry(abs, 2300, -200, 0, true), 
            new Odometry(abs, 2300, -1000, 0, true),
            new Odometry(abs, 3300, -1000, 0, true),
            new Odometry(abs, 3300, 0, 0, true), 
            new End(),
 
            // // // //захват с земли стоя нет бля сидя или лежа
            new StartTransition(2f),
            new OMS(-1, 15, 110, 137),
            new OMS(-900, 15, -1, -1),
            new OMS(-1,-1,220,-1),
            new OMS(-1,-1,210,-1), 
            new TimerCount(1f),
            new OMS(0,2,220,-1), 
            new TimerCount(8f),
            new End(),

            new StartTransition(2f),
            // //зона дерева нижняя ветка
            new OMS(-1, 15, 110, 137),
            new OMS(-500, 15, 110, 137),
            new OMS(-1,-1,220,137),
            new OMS(-1,-1,220,137),
            new TimerCount(1f),
            new OMS(0,1,-1,-1),
            new TimerCount(8f),
            new End(),


            // // // //от старта до зоны
            new StartTransition(1f),
            new SensorsTest(sonicb,25,0,0),
            new Odometry(0, 0, -90),
            new SensorsTest(ikr, 12, 0, 1),
            new Reset(0,0,0),

            new Odometry(abs, -455, 0, 90, true),
            new Odometry(abs, -455, 870, 90, true), 

            new OMS(-500, 15, 110, -1),
            new TimerCount(0.5f),
            new Camera("centre"),
            new OMS(-900,-1,110,-1),
            new OMS(-1,-1,-1,-1),
            new TimerCount(1f), 
            new OMS(-1,-1,220,-1),
            new TimerCount(1f),
            new OMS(0,1,220,-1),
            new TimerCount(8f),
            new End(),
            
            //от старта до мусорки
            new StartCloseOMS(2f),
             
            new SensorsTest(sonicb,25,0,0),
            new Odometry(0, 0, -90),
            new SensorsTest(ikr, 12, 0, 1),
            new Reset(0,0,0),

            new Odometry(abs, -470, 0, 0, true),

            new Odometry(-400, 0, 0),

            new Odometry(0, 0, -90),

            new SensorsTest(ikr, 12, 0, 1),

            new Odometry(0, 0, 90), 

            new Odometry(-400, 0, 0),

            new OMS(-1, 17, 210, 80),
            new OMS(-1, -1, 110, 80),
            new TimerCount(0.5f), 
            new End(),
        }
    };
}