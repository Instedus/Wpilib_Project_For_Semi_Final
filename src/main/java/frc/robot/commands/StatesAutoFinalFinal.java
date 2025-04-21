package frc.robot.commands;

import java.util.Set;

import edu.wpi.first.wpilibj.Timer;
import frc.robot.Autonomus.InitializeLogicAuto;
import frc.robot.Autonomus.TransitionAuto;
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

public class StatesAutoFinalFinal{

    public final static String abs = "abs";
    public final static String rel = "rel";
    public final static String ikl = "ikl";
    public final static String ikr = "ikr";
    public final static String sonicb = "snonicb";

    public static IState[][] states = new IState[][] { 
        {
            new Start(1f),

            new SensorsTest(sonicb,25,0,0),
            new Odometry(0, 0, -90),
            new SensorsTest(ikr, 12, 0, 1),
            new Reset(0,0,0),

            new Odometry(abs, -455, 0, 90, true),
            new Odometry(abs, -455, 870, 90, true), 

            new Reset(-455, 870, 90),

            new Odometry(240, 0, 0),
             
            new OMS(-500, 15, 110,-1),
            new TimerCount(0.5f),
            new Camera("centre"),
            new Camera("main_detect"),  
            new WhereElementLogic(true,90),
            new InitializeLogicAuto(),
            
            new TransitionAuto(),
        }, 
        {// from checkpoint to zone 1 // 1
            
            new Odometry(abs, -455, 0, 90, true),
            new Odometry(abs, -455, 870, 90, true), 

            new Reset(-455, 870, 90),

            new TransitionAuto(),   
        },
        { // from checkpoint to zone 2 // 2
            new Odometry(abs, -2010, 110, 0, true),

            new Reset(-2010, 110, 0),

            // new Reset(-1080, 2150, 90), 
          
            new TransitionAuto(),  
        },
        {
            new Odometry(abs, -3227, 576, -90, true),

            new Odometry(abs, -3335, 95, -90, true),

            
            // new SensorsTest(ikr, 15, 0, 1),

            // new Reset(-1, -1, -90),

            // new Odometry(-1000, 0, 0),

            // new Odometry(0, -250, 0),

            // new Odometry(650,0,0),  

            new Reset(-3335,95,-90), 

            new TransitionAuto(),
        },
        {// to zone drive // 4
            new Odometry(rel, 0, 0, 0,  false), 
            new TransitionAuto(),
        },   
        {// to tree drive // 5 
            new Odometry(rel, 0, 0, 0, true), 
            new TransitionAuto(),
        },
        { // exit zone drive // 6                                                                                                                                                          
            new Odometry(rel, 0, 0, 0, true),
            new ResetWhereElementLogic(), 
            new TransitionAuto(),
        }, 
        { //exit zone tree // 7
            new Odometry(rel, 0, 0, 0, true),
            new ResetWhereElementLogic(), 
            new TransitionAuto(),
        },
        {// frist tree to checpoint // 8
            new SetOMS(5),
            

            // new Odometry(-400, 0, 0),

            // new Odometry(abs, -1860, 400, 0, true),

            new Odometry(abs, -470, 0, 90, true),

            new SensorsTest(sonicb,25,0,0),
            new Odometry(0, 0, -90),
            new SensorsTest(ikr, 12, 0, 1),
            new Reset(0,0,0),

            new TransitionAuto(),
        },
        {// second tree to checpoint // 9

            new SetOMS(5), 

            // new Odometry(-400, 0, 0),

            // new Odometry(abs, -1860, 400, 0, true),

            new Odometry(abs, -470, 0, 90, true),

            new SensorsTest(sonicb,25,0,0),
            new Odometry(0, 0, -90),
            new SensorsTest(ikr, 12, 0, 1),
            new Reset(0,0,0),

            new TransitionAuto(),
        },
        {// third tree to checpoint // 10

            new SetOMS(5), 
            new Odometry(-700, 0, 0),

            new Odometry(abs, -470, 0, 90, true),

            new SensorsTest(sonicb,25,0,0),
            new Odometry(0, 0, -90),
            new SensorsTest(ikr, 12, 0, 1), 
            new Reset(0,0,0),

            new TransitionAuto(),
        },
        { // 11 CheckPointToAppleTrash
            new Odometry(abs, -3227, 576, 90, true),
            new SensorsTest(ikr, 15, 0, 1),
            new Odometry(0, 0, 90),
            new SensorsTest(ikr, 12, 0, 1),
            // new Reset(-1, -1, 0),
            new Odometry(0, 0, 90),

            new OMS(-1, 21, 225, 80),
            new OMS(-1, -1, 110, 80),
            new TimerCount(0.5f), 
            // new OMS(-1, 1, 110, 80), 
            new SetOMS(3), 

            new TransitionAuto(), 
        },
        { // 12 CheckPointToPearTrash 

            
            new Odometry(abs, -3227, 576, -90, true),
            
            new SensorsTest(ikr, 15, 0, 1),

            new Reset(-1, -1, -90),

            new Odometry(0, 0, 90),

            new Odometry(400, 0, 0),

            new OMS(-1, 21, 225, 80),
            new OMS(-1, -1, 110, 80),
            new TimerCount(0.5f), 
            // new OMS(-1, 1, 110, 80),
            
            new SetOMS(3),

            new TransitionAuto(),
        },

        { // 13 CheckPointToUnripTrash

            // new Odometry(abs, -730, 2400, -90, true),
            // new Odometry(abs, -730, 720, -90, true),
            // new Odometry(abs, -730, 0, -90, true),

           
            new Odometry(abs, -1860, 400, 0, true),

            new Odometry(0, 0, 90),

            new TimerDrive(85, -85, 3.5f),

            new SensorsTest(ikr, 12, 0, 1),

            new Odometry(0, 0, 90),

            new OMS(-1, 21, 225, 80),
            new OMS(-1, -1, 110, 80),
            new TimerCount(0.5f), 
            // new OMS(-1, 1, 110, 80),
            new SetOMS(3),

            new TransitionAuto(), 
        },
        { // 14 CheckPointToSmallAppleTrash
   
            // new Odometry(abs, -800, 720, 90, true),
            
            new Odometry(abs, -470, 0, 0, true),

            new Odometry(-400, 0, 0),

            new Odometry(0, 0, -90),

            new SensorsTest(ikr, 12, 0, 1),

            new Odometry(0, 0, 90),

            new Odometry(-350, 0, 0),

            new OMS(-1, 21, 225, 80),
            new OMS(-1, -1, 110, 80),
            new TimerCount(0.5f), 
            
            // new OMS(-1, 1, 110, 80),
            new SetOMS(3),

            new TransitionAuto(),   
        },
        { // 15 AppleTrashToCheckPoint
            
            new Odometry(abs, -3227, 576, 0, true),

            // new Odometry(abs, -470, 0, 0, true),

            // new SensorsTest(ikr, 12, 0, 1),

            // new Reset(-1, -1, 0),

            new Odometry(abs, -470, 0, 90, true),    

            new SensorsTest(sonicb,25,0,0),
            new Odometry(0, 0, -90),
            new SensorsTest(ikr, 12, 0, 1),
            new Reset(0,0,0),

            new TransitionAuto(), 
        },
        { // 16 PearTrashToCheckPoint

            
            new Odometry(abs, -3227, 576, 0, true),

            // new Odometry(abs, -470, 0, 0, true),

            // new SensorsTest(ikr, 12, 0, 1),

            // new Reset(-1, -1, 0),

            new Odometry(abs, -470, 0, 90, true),
            new SensorsTest(sonicb,25,0,0),
            new Odometry(0, 0, -90),
            new SensorsTest(ikr, 12, 0, 1),
            new Reset(0,0,0),

            new TransitionAuto(), 
        },                                                                                                   
        { // 17 unrip trash to checkpoint 
           
            new Odometry(0, 0, -90),

            new TimerDrive(-75, 75, 3f),

            // new Odometry(abs, -470, 0, 0, true),

            // new SensorsTest(ikr, 12, 0, 1),

            // new Reset(-1, -1, 0),

            new Odometry(abs, -470, 0, 90, true),

            new SensorsTest(sonicb,25,0,0),
            new Odometry(0, 0, -90),
            new SensorsTest(ikr, 12, 0, 1),
            new Reset(0,0,0),
 
            new TransitionAuto(), 
        },
        { // 18 checkpoint to small apple trash

            new Odometry(abs, -470, 0, 90, true),

            new SensorsTest(sonicb,25,0,0),
            new Odometry(0, 0, -90),
            new SensorsTest(ikr, 12, 0, 1), 

            new Reset(0,0,0),

            new TransitionAuto(), 
        },
        { // 19 checkpoint to end
            new Odometry(-850, 0, 0),
            new Odometry(0, 0, -90),
            new Odometry(-800, 0, 0),
            // new Odometry(rel, 0,0,90, true),
            // new SensorsTest(sonicb, 15,0,0),
            // end
            // new SensorsTest(ikr, 15,0,0),
            new TransitionAuto(), 
        },
        { // 20
            // oms fruit in  
            new OMS(-1,-1,110,-1),  
            // new Camera("centre"),
            new OMS(-1,-1,110,-1),
            new TimerCount(1f),
            new OMS(-1,-1,222,-1),
            new TimerCount(0.5f),
            // new SetOMS(0,1,97,-1,0),
            new OMS(0,1,222,-1),
            // new SetOMS(4),
            new TransitionAuto(),  
        }, 
        { // 21
            // new OMS(-500,-1,210,-1),
            // oms fruit in small  
            new OMS(-1,-1,110,-1),
            // new Camera("centre"),
            new OMS(-1,-1,110,-1),
            new TimerCount(1f),
            new OMS(-1,-1,225,-1),
            new TimerCount(0.5f),
            // new SetOMS(0,1,97,-1,0),
            new OMS(0,1,225,-1),
            // new SetOMS(4),
            new TransitionAuto(),
        },
        { // 22
            new End(), 
        },
        { //23 scan first zone
            new OMS(-2, 15, 110,-1),
            new TimerCount(0.5f),
            new Camera("centre"),
            new Camera("main_detect"), 
            new WhereElementLogic(true,90),
            new InitializeLogicAuto(),
            new OMS(0,-1,-1,-1),
            // new SetOMS(2),
            new TransitionAuto(),
        },
        { //24 scan second zone

            new OMS(-2, 15, 110,-1),
            new TimerCount(0.5f),
            new Camera("centre"),
            new Camera("main_detect"), 
            new WhereElementLogic(true,0),
            new InitializeLogicAuto(),
            new OMS(0,-1,-1,-1),
            // new SetOMS(2),
            new TransitionAuto(),
        },
        { //25 scan third zone

            new OMS(-2, 15, 110,-1),
            new TimerCount(0.5f),
            new Camera("centre"),
            new Camera("main_detect"), 
            new WhereElementLogic(true,-90),
            new InitializeLogicAuto(),
            new OMS(0,-1,-1,-1),
            // new SetOMS(2),
            new TransitionAuto(),
        },
        { // 26 scan first tree
            new OMS(-2, 15, 110,-1),
            new TimerCount(0.5f),
            new Camera("centre"),
            new Camera("main_detect_tree"),
            new WhereElementLogic(false,90), 
            new InitializeLogicAuto(),
            // new OMS(-1,18,-1,-1),
            new OMS(-1, -2, -1, -1),
            new OMS(0,-1,-1,-1),
            new TransitionAuto(),
        },
        { // 27 scan second tree
            new OMS(-2, 15, 110,-1),
            new TimerCount(0.5f), 
            new Camera("centre"),
            new Camera("main_detect_tree"), 
            new WhereElementLogic(false,0),
            new InitializeLogicAuto(),
            // new OMS(-1,18,-1,-1), 
            new OMS(-1, -2, -1, -1),
            new OMS(0,-1,-1,-1),
            new TransitionAuto(),
        },
        { // 28 scan third tree
            new OMS(-2, 15, 110,-1),
            new TimerCount(0.5f),
            new Camera("centre"),
            new Camera("main_detect_tree"),
            new WhereElementLogic(false,-90),
            new InitializeLogicAuto(), 
            // new OMS(-1,18,-1,-1),
            new OMS(-1, -2, -1, -1),
            new OMS(0,-1,-1,-1),
            new TransitionAuto(),
        },
    };
}