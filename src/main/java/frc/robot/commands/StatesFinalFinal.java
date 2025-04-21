
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
import frc.robot.cases.Switch;
import frc.robot.cases.TimerCount;
import frc.robot.cases.TimerDrive;
import frc.robot.cases.Transition;
import frc.robot.casesOMS.OMS;

public class StatesFinalFinal{

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

            new Transition(),   
        },
        {// from checkpoint to zone 1 // 1
            
            new Odometry(abs, -1755, 910, 180, true),

            new Odometry(abs, -990, 910, 180, true),
            // new Odometry(abs, -455, 870, 90, true), 

            new Reset(-990, 900, 180),
 
            new Transition(),  
        },
        { // from checkpoint to zone 2 // 2 
            new Odometry(abs, -1755, 900, 180, true),

            new Odometry(abs, -2406, 900,180,true),

            new Reset(-2406, 900, 180),

        // new Reset(-1080, 2150, 90), 
              
            new Transition(),  
        },
        { // from checkpoint to zone 3 // 3

            new Odometry(abs, -1897, 220, 0, true),

            new Reset(-1897, 220, 0),
 
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
            new Odometry(rel, 0, 0, 0, true), 
            new Transition(),
        },
        { //exit zone tree // 7
            new Odometry(rel, 0, 0, 0, true),
            new Transition(),
        },
        {// frist tree to checpoint // 8
            new SetOMS(5),
            
            // new Odometry(500, 0, 0),

            new Odometry(abs, -470, 0, 90, true),

            new SensorsTest(sonicb,25,0,0),
            new Odometry(0, 0, -90),
            new SensorsTest(ikr, 12, 0, 1),
            new Reset(0,0,0),

            new Transition(),
        },
        {// second tree to checpoint // 9
           new SetOMS(5), 

        //    new Odometry(-500, 0, 0),

            new Odometry(abs, -470, 0, 90, true),
 
            new SensorsTest(sonicb,25,0,0),
            new Odometry(0, 0, -90),
            new SensorsTest(ikr, 12, 0, 1),
            new Reset(0,0,0),

            new Transition(),
        },
        {// third tree to checpoint // 10
            new SetOMS(5), 

            new Odometry(abs, -470, 0, 90, true),

            new SensorsTest(sonicb,25,0,0),
            new Odometry(0, 0, -90),
            new SensorsTest(ikr, 12, 0, 1), 
            new Reset(0,0,0),

            new Transition(),
        },
        { // 11 CheckPointToAppleTrash
            
            new Odometry(abs, -3227, 576, 180, true),
            new SensorsTest(ikr, 12, 0, 1),
            new Reset(-1, -1, 180),
            new Odometry(0, 0, 90),
            new OMS(-1, 21, 225, 80),
            new OMS(-1, -1, 110, 80),
            new TimerCount(0.5f), 
            // new OMS(-1, 1, 110, 80), 
            new SetOMS(3),

            new Transition(), 
        },
        { // 12 CheckPointToPearTrash 

            new Odometry(abs, -3227, 576, -90, true),

            new Odometry(abs, -2900, 576, -90, true),
            
            new SensorsTest(ikr, 12, 0, 1),

            new Reset(-1, -1, -90),

            new Odometry(0, 0, 90),

            // new Odometry(400, 0, 0),

            new OMS(-1, 21, 225, 80),
            new OMS(-1, -1, 110, 80),
            new TimerCount(0.5f), 
            // new OMS(-1, 1, 110, 80),
            new SetOMS(3),

            new Transition(), 
        },

        { // 13 CheckPointToUnripTrash
            new Odometry(abs, -250, 0, 90, true),
            new Odometry(abs, -250, 870, 0, true), 
            new SensorsTest(ikr, 12, 0, 1),

            new Odometry(0,0, 90),

            new OMS(-1, 17, 225, 80),
            new OMS(-1, -1, 110, 80),
            new TimerCount(0.5f), 
            // new OMS(-1, 1, 110, 80),
            new SetOMS(3),

            new Transition(), 
        },
        { // 14 CheckPointToSmallAppleTrash

            new Odometry(abs, -550, 0, 0, true),

            new Odometry(0, 0, -90),

            new SensorsTest(ikr, 12, 0, 1),

            new Odometry(0, 0, 90),
            // new Odometry(-350, 0, 0),
            new OMS(-1, 21, 225, 80),
            new OMS(-1, -1, 110, 80),
            new TimerCount(0.5f), 
            // new OMS(-1, 1, 110, 80),
            new SetOMS(3),

            new Transition(),   
        },
        { // 15 AppleTrashToCheckPoint

            new Odometry(abs, -3227, 576, 0, true),

            new Odometry(abs, -470, 576, 0, true),

            new Odometry(abs, -470, 0, 90, true),

            new SensorsTest(sonicb,25,0,0),
            new Odometry(0, 0, -90),
            new SensorsTest(ikr, 12, 0, 1),
            new Reset(0,0,0),

            new Transition(), 
        },
        { // 16 PearTrashToCheckPoint

            new Odometry(abs, -3227, 576, 0, true),

            new Odometry(abs, -470, 576, 0, true),

            new Odometry(abs, -470, 0, 90, true),

            new SensorsTest(sonicb,25,0,0),
            new Odometry(0, 0, -90),
            new SensorsTest(ikr, 12, 0, 1),
            new Reset(0,0,0),

            new Transition(), 
        },                                                                                                   
        { // 17 unrip trash to checkpoint 

            new Odometry(abs, -470, 0, 90, true),

            new SensorsTest(sonicb,25,0,0),
            new Odometry(0, 0, -90),
            new SensorsTest(ikr, 12, 0, 1),
            new Reset(0,0,0),

            new Transition(), 
        },
        { // SmallAppleTrashToCheckPoint 18 

            new Odometry(abs, -470, 0, 90, true),

            new SensorsTest(sonicb,25,0,0),
            new Odometry(0, 0, -90),
            new SensorsTest(ikr, 12, 0, 1),
            new Reset(0,0,0),
            new Reset(-1,-1,0),
 
            new Transition(), 
        },
        { // 19 checkpoint to end
            new Odometry(0, 0, 90),
            new SensorsTest(sonicb, 20, 0, 0),
            new End(),
            new Transition(), 
        },
        { // 20
            // oms fruit in 
            new OMS(-2, 17, 110,-1),
            new TimerCount(0.5f),
            new Camera("centre"),
            // new Camera("main_detect"), 
            new OMS(-1,-1,110,-1),
            new OMS(-1,-1,110,-1),
            new TimerCount(1f),
            new OMS(-1,-1,222,-1),
            new TimerCount(0.5f),
            // new SetOMS(0,1,97,-1,0),
            new OMS(0,1,222,-1),
            new Transition(),  
        }, 
        { // 21
            // oms fruit in small  
            new OMS(-2, 17, 110,-1),
            new TimerCount(0.5f),
            new Camera("centre"),
            // new Camera("main_detect"),
            new OMS(-1,-1,110,-1),
            new OMS(-1,-1,110,-1),
            new TimerCount(1f),
            new OMS(-1,-1,225,-1),
            new TimerCount(0.5f),
            // new SetOMS(0,1,97,-1,0),
            new OMS(0,1,225,-1),
            new Transition(),
        },
        { // 22
            new End(), 
        },      
    };

}