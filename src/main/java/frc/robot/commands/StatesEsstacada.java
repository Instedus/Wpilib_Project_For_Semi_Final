package frc.robot.commands;

import edu.wpi.first.wpilibj.Timer;
import frc.robot.cases.Camera;
import frc.robot.cases.End;
import frc.robot.cases.InitializeLogic;
import frc.robot.cases.Lights;
import frc.robot.cases.Odometry;
import frc.robot.cases.Reset;
import frc.robot.cases.SensorsTest;
import frc.robot.cases.SetOMS;
import frc.robot.cases.Start;
import frc.robot.cases.TimerCount;
import frc.robot.cases.TimerDrive;
import frc.robot.cases.Transition;
import frc.robot.casesOMS.OMS;

public class StatesEsstacada{

    public final static String abs = "abs";
    public final static String rel = "rel";
    public final static String ikl = "ikl";
    public final static String ikr = "ikr";
    public final static String sonicb = "snonicb";

    public static IState[][] states = new IState[][] { 
        { // start //0
            new Start(1f),
            
            new InitializeLogic(),

            new SensorsTest(sonicb,25,0,0),
            new Odometry(0, 0, -90),
            new SensorsTest(ikr, 12, 0, 1),
            new Reset(0,0,0),
            new Odometry(0, 0, 90),
            new Odometry(abs, 0, 720, 0, true),
            new SensorsTest(ikr, 12, 0, 1),
            new Reset(-1,-1,0),

            new Transition(), 
        },
        {// from checkpoint to zone 1 // 1
            
            new Odometry(abs, -345, 720, -90, true),
            new Odometry(abs, -345, 1660, -90, true),
 
            new Reset(-345, 1660, -90),
 
            new Transition(),  
        },
        { // from checkpoint to zone 2 // 2

            new Odometry(abs, 0, 720, 180, true),
            new SensorsTest(ikr, 12, 0, 1),
            new Reset(-1485,720,180),
            
            new Odometry(abs, -1100, 720, 90, true),
            new Odometry(abs, -1100, 2280 , 90, true),

            new Reset(-1190, 2250, 90), 
             
            new Transition(), 
        },
        { // from checkpoint to zone 3 // 3
            new Odometry(abs, -800, 720, 90, true),
            new Odometry(abs, -800, 3000, 90, true),
            new SensorsTest(ikr, 12, 0, 1),
            new Reset(-1,-1,90 ),
            new Odometry(-435, 0, -90),
            new SensorsTest(ikr, 15, 0, 1),
            new Reset(-1,-1,0),
            new Odometry(-370, 0, 0),

            new Reset(-370, 2930, 0),
 
            new Transition(),
        }, 
        {// to zone drive // 4
            new Odometry(rel, 0, 0, 0,  false), 
            new Transition(),
        }, 
        {// to tree drive // 5 
            new Odometry(rel, 0, 0, 0, true),
            new Transition(),
        },
        { // exit zone drive // 6                                                                                                                                                          
            new Odometry(rel, 0, 0, 0, false), 
            new Transition(),
        },
        { //exit zone tree // 7
            new Odometry(rel, 0, 0, 0, false),
            new Transition(),
        },
        {// frist tree to checpoint // 8
            new Odometry(abs, -750, 2400, -90, false),
            new Transition(),
        },
        {// second tree to checpoint // 9
            new Odometry(abs, -950, 1800, 90, false),
            new Odometry(abs, -750, 2400, -90, false),
            new Transition(),
        },
        {// third tree to checpoint // 10
            new Odometry(abs, -750, 2400, -90, false),
            new Transition(),
        },
        { // 11 CheckPointToAppleTrash
            
            new Odometry(abs, 750, 2400, 90,true),
            new SensorsTest(ikr, 15, 0, 1),
            new Reset(-1, -1, 0),
            new Odometry(rel, 0,0,-90,true),
            new OMS(-1, 18, 109, 10),
            new OMS(-1, -1, 40, 10),
            new TimerCount(0.5f), 
            new OMS(-1, 1, 40, 10), 

            new Transition(), 
        },
        { // 12 CheckPointToPearTrash 

            new Odometry(abs, -750,840, 0, true),
            new SensorsTest(ikr,15,0,0),
            new SensorsTest(ikr,15,0,1),
            new Reset(-1, -1, 0),
            new Odometry(rel, 0, 0, -90, true),
            new OMS(-1, 18, 109, 10), 
            new OMS(-1, -1, 40, 10),
            new TimerCount(0.5f), 
            new OMS(-1, 1, 40, 10),

            new Transition(), 
        },

        { // 13 CheckPointToUnripTrash

            new Odometry(abs, -730, 2400, -90, true),
            new Odometry(abs, -730, 720, -90, true),
            new Odometry(abs, -730, 0, -90, true),

            new SensorsTest(ikr,15,0,1),
            new Reset(-1, -1, -90),
            new Odometry(rel, 0, 0, -90, true), 
            new OMS(-1, 21, 109, 10),
            new OMS(-1, -1, 40, 10),
            new TimerCount(0.5f), 
            new OMS(-1, 1, 40, 10),

            new Transition(), 
        },
        { // 14 CheckPointToSmallAppleTrash
   
            new Odometry(abs, -750, 1200, -180, true),
            new SensorsTest(ikr,15,0,0),
            new SensorsTest(ikr,15,0,1),
            new Reset(-1, -1, -180),
            new Odometry(rel, 0, 0, -90, true),
            new Odometry(-310, 0, 0),
            new OMS(-1, 19, 109, 10),
            new OMS(-1, -1, 40, 10),
            new TimerCount(0.5f), 
            new OMS(-1, 1, 40, 10),

            new Transition(),  
        },
        { // 15 AppleTrashToCheckPoint
            
            new Odometry(abs, -750, 2400, -90, true),
            new Odometry(abs, -750, 720, 0, true),
            new SensorsTest(ikr, 12, 0, 1),
            new Reset(-1,-1,0),
            new Odometry(0,0,90),
            
            new Odometry(abs, 0, 0, 90, true),
            new SensorsTest(sonicb,25,0,0),
            new Odometry(0, 0, -90),
            new SensorsTest(ikr, 12, 0, 1),
            new Reset(0,0,0),
            new Odometry(0, 0, 90),
            new Odometry(abs, 0, 720, 0, true),
            new SensorsTest(ikr, 12, 0, 1),
            new Reset(-1,-1,0),

            new Transition(), 
        },
        { // 16 PearTrashToCheckPoint

            new SensorsTest(ikl,25,0,0),
            new Odometry(0, 0, 90),
            new SensorsTest(ikr, 12, 0, 1),
            new Reset(0,0,0),
            new Odometry(0, 0, 90),
            new Odometry(abs, 0, 720, 0, true),
            new SensorsTest(ikr, 12, 0, 1),
            new Reset(-1,-1,0),

            new Transition(), 
        },                                                                                                   
        { // 17 unrip trash to checkpoint 
            
            new Odometry(rel, 0, 0, 180, true),
            new TimerDrive(-35, 35, 3),
            new SensorsTest(ikr, 15, 0, 1),
            // new Odometry(-1, -1, 0),
            new Odometry(0, 0, 90),

            new SensorsTest(sonicb,25,0,0),
            new Odometry(0, 0, -90),
            new SensorsTest(ikr, 12, 0, 1),
            new TimerCount(1f),
            new Reset(0,0,0),
            new Odometry(0, 0, 90),
            new Odometry(abs, 0, 720, 0, true),
            new SensorsTest(ikr, 12, 0, 1),
            new TimerCount(1f),
            new Reset(-1,-1,0),

            new Transition(), 
        },
        { // SmallAppleTrashToCheckPoint 18
            
            new Odometry(abs, -750, 720, 0, true),
            new SensorsTest(ikr, 12, 0, 1),
            new Reset(-1,-1,0),

            new Odometry(abs, 0, 0, 90, true),
            new SensorsTest(sonicb,25,0,0),
            new Odometry(0, 0, -90),
            new SensorsTest(ikr, 12, 0, 1),
            new Reset(0,0,0),
            new Odometry(0, 0, 90),
            new Odometry(abs, 0, 720, 0, true),
            new SensorsTest(ikr, 12, 0, 1),
            new Reset(-1,-1,0),

            new Transition(), 
        },
        { // 19 checkpoint to end
            new Odometry(rel, 0,0,90, true),
            new SensorsTest(sonicb, 15,0,0),
            // end
            // new SensorsTest(ikr, 15,0,0),
            new Transition(), 
        },
        { // 20
            // oms fruit in 
            new OMS(-2, 15, 40, -1),
            new TimerCount(0.5f),
            new Camera("centre"),
            new Camera("main_detect"),
            new OMS(-1,-1,55,-1),
            new OMS(-1,-1,55,-1),
            new TimerCount(0.5f),
            new OMS(-1,-1,109,-1),
            new TimerCount(0.5f),
            // new SetOMS(0,1,97,-1,0),
            new OMS(0,1,109,-1),
            new Transition(), 
        }, 
        { // 21
            // oms fruit in small  
            new OMS(-2, 15, 40, -1),
            new TimerCount(0.5f),
            new Camera("centre"),
            new Camera("main_detect"),
            new OMS(-1,-1,55,-1),
            new OMS(-1,-1,55,-1),
            new TimerCount(1f),
            new OMS(-1,-1,109,-1),
            new TimerCount(0.5f),
            // new SetOMS(0,1,97,-1,0),
            new OMS(0,1,109,-1),
            new Transition(),  
        },
        { // 22
            new End(), 
        },      
    };

}