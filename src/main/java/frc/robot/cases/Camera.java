package frc.robot.cases;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.RobotContainer;
import frc.robot.commands.IState;
import frc.robot.commands.StateMachine;
import frc.robot.subsystems.Function;
import frc.robot.subsystems.JavaCamera;
import frc.robot.subsystems.Training;

public class Camera implements IState {

    private String name;
    private boolean end = false;

    private JavaCamera server = RobotContainer.server;
    private Training train = RobotContainer.train;

    private float count = 0;
    private float countForward = 0;
    private float countX = 0;

    public static String FINALANS;
    public static String greenBigApple = "greenBigApple";
    public static String greenSmallApple = "greenSmallApple";
    public static String greenPear = "greenPear";
    public static String redBigApple = "redBigApple";
    public static String redSmallApple = "redSmallApple";
    public static String yellowPear = "yellowPear";
    public static String purple = "purple";

    public float CENTREX = 0, CENTREY = 0;
    public int distanceToObject;

    private boolean isReachedPos = false, endMethod = false;

    boolean isForwardDone = false; 

    float endTime = 0;

    float waitTimeExecute = 0;

    public static int NUM;

    boolean isEndDistance = false;
    boolean isStartDistance = true;

    public Camera(String name) {
        this.name = name;
    }

    @Override
    public boolean execute() {
        RobotContainer.train.setAxisSpeed(0, 0);

        waitTimeExecute += Function.countTimer();
            if (!StateMachine.isFirst) {
                switch (this.name) {
                case ("main_detect"):
                    end = checkFruit(false);
                    break;
                case ("main_detect_tree"):
                    end = checkFruitTree(false);
                    break;
                case ("main"): {
                    end = checkFruitMain();
                    break;
                }
                case ("count"):
                    end = countOfFruit();
                    break;
                case ("purple"):
                    end = checkPurple();
                    break;
                case ("centre"):
                    end = detectCentre();
                    break;
                case ("distance"):
                    end = checkDistance();
                    break;
                case("number"):
                    end = numberDetect();
                default:
                    end = false;
                    break;
                }
            } else {
                StateMachine.isFirst = false;
            }
            return end;
        }

    private boolean numberDetect(){

        count += Function.countTimer();

        server.nowTask = 6;

        int num = server.nowResult;

        if(count > 2){
            NUM = num;
            return true;
        }

        return false;
    }

    private boolean checkDistance() {
        count += Function.countTimer();

        server.nowTask = 5;

        distanceToObject = server.nowResult;    

        if(isStartDistance){
            train.destination = 9;
            isStartDistance = false;
        }

        if(count > 5f && !isEndDistance){
            if(distanceToObject <= 10){
                train.destination = train.countCobra += 9; 
                isEndDistance = true;
            }
            else{
                train.destination += 1;
            }
        }      
        
        if(isEndDistance){
            server.nowTask = 0;
        }

        return isEndDistance;
    }

    private boolean checkPurple() {
        count += Function.countTimer();
        server.nowTask = 3;
        if (server.nowResult == 1) {
            SmartDashboard.putBoolean("purple",true);
            train.setGreen(true);
        } else {
            SmartDashboard.putBoolean("purple",false);
            train.setGreen(false);
        }

        boolean end = train.getStart();

        if(end){
            server.nowTask = 0;
        }

        return end;
    }

    public boolean detectCentre() {
        
        waitTimeExecute += Function.countTimer();
        
        float spX = 0; 
        server.nowTask = 4;

        CENTREX = -server.newResult[0];
        CENTREY = -server.newResult[1]; 

        SmartDashboard.putNumber("CENTREX", CENTREX);
        SmartDashboard.putNumber("CENTREY", CENTREY); 


        if(waitTimeExecute > 5f){

            count += Function.countTimer();

            if (!isReachedPos && !isForwardDone) {

                train.destination = train.countCobra + (int) Function.inRange(CENTREY/10, -1, 1);            
    
                isReachedPos = Function.inRangeBool(CENTREY, -15, 15);

                if(isReachedPos){
                    isForwardDone = true;
                }
            }
            if (isForwardDone)
            {
                train.destination = train.countCobra;
                spX = -Function.inRange(CENTREX, -20, 20);
            }
    
            SmartDashboard.putBoolean("endCamera", isReachedPos);
            SmartDashboard.putNumber("distXCamera", spX);
    
            endMethod = Function.inRangeBool(CENTREX, -20,20) && isReachedPos; 
            
            if(endMethod){
                train.setAxisSpeed(0, 0);
            }else{
                train.setAxisSpeed(spX, 0);
            }
    
            if ((CENTREX == 0 || CENTREY == 0) && count - endTime > 1.5f)
            {
                endMethod = true;
            }
            else
            {
                endTime = count;
            }
        }
        
        boolean end = endMethod && count > 1f;

        if(count > 15f){
            end = true;
        }   

        if(end){
            server.nowTask = 0;
        }

        return end;
    }

    private boolean detectCentreTree(){
         
        waitTimeExecute += Function.countTimer();
        
        float spX = 0; 
        server.nowTask = 4;

        CENTREX = -server.newResult[0];
        CENTREY = -server.newResult[1]; 

        SmartDashboard.putNumber("CENTREX", CENTREX);
        SmartDashboard.putNumber("CENTREY", CENTREY); 


        if(waitTimeExecute > 5f){

            count += Function.countTimer();

            if (!isReachedPos && !isForwardDone) {

                train.destination = train.countCobra + (int) Function.inRange(CENTREY/10, -1, 1);            
    
                isReachedPos = Function.inRangeBool(CENTREY, -25, 25);
     
                if(isReachedPos){
                    isForwardDone = true;
                }
            }
            if (isForwardDone)
            {
                train.destination = train.countCobra;
                spX = Function.inRange(CENTREX, -20, 20);
            }
    
            SmartDashboard.putBoolean("endCamera", isReachedPos);
            SmartDashboard.putNumber("distXCamera", spX);
    
            endMethod = Function.inRangeBool(CENTREX, -20,20) && isReachedPos; 
            
            if(endMethod){
                train.setAxisSpeed(0, 0);
            }else{
                train.setAxisSpeed(spX, 0);
            }
    
            if ((CENTREX == 0 || CENTREY == 0) && count - endTime > 3)
            {
                endMethod = true;
            }
            else
            {
                endTime = count;
            }
        }
        
        boolean end = endMethod && count > 1f;

        if(count > 9f){
            end = true;
        }   

        if(end){
            server.nowTask = 0;
        }

        return end;
    }

    

    private boolean countOfFruit() {
        count += Function.countTimer();
        server.nowTask = 2;
        SmartDashboard.putNumber("COUNT_FRUCTS", server.nowResult);

        boolean end = train.getStart();

        if(end){
            server.nowTask = 0;
        }

        return end;
    }

    private boolean checkFruit(boolean mode) {
        server.nowTask = 1;

        count += Function.countTimer();

        SmartDashboard.putNumber("count", count);

        switch (server.nowResult) {
        case (1):
            FINALANS = greenBigApple;
            StateMachine.TRASH = "AppleTrash";
            StateMachine.countOnGround = 1;
            break;

        case (2):
            FINALANS = greenSmallApple;
            StateMachine.TRASH = "SmallAppleTrash";
            StateMachine.countOnGround = 1;
            break;

        case (3):
            FINALANS = greenPear;
            StateMachine.TRASH = "PearTrash";
            StateMachine.countOnGround = 1;
            break;

        case (4):
            FINALANS = yellowPear;
            StateMachine.TRASH = "PearTrash";
            StateMachine.countOnGround = 1;
            break;

        case (5): 
            FINALANS = redBigApple;
            StateMachine.TRASH = "AppleTrash";
            StateMachine.countOnGround = 1;
            break;

        case (6):
            FINALANS = redSmallApple;
            StateMachine.TRASH = "SmallAppleTrash"; 
            StateMachine.countOnGround = 1;
            break;

        case (7):
            FINALANS = purple;
            StateMachine.TRASH = "UnripTrash";
            StateMachine.countOnGround = 1;
            break;

        default:
            FINALANS = "nothing";
            StateMachine.TRASH = "UnripTrash";
            StateMachine.countOnGround = 0;
            break;
        }
        
        SmartDashboard.putString("FINALANS", FINALANS);

        boolean end = false;
        
        if(!mode){
            end = count > 2f;
        }
        else{
            end = false || train.getStart();

            if (FINALANS.equals(redBigApple)) {
                train.setGreen(true);
            } else {
                train.setGreen(false);  
            }
        }

        if(end){
            server.nowTask = 0;
        }

        return end;
    }

    private boolean checkFruitMain() {
        server.nowTask = 1;

        count += Function.countTimer();

        SmartDashboard.putNumber("count", count);

        switch (server.nowResult) {
        case (1):
            FINALANS = greenBigApple;
            StateMachine.TRASH = "AppleTrash";
            StateMachine.countOnGround = 1;
            break;

        case (2):
            FINALANS = greenSmallApple;
            StateMachine.TRASH = "SmallAppleTrash";
            StateMachine.countOnGround = 1;
            break;

        case (3):
            FINALANS = greenPear;
            StateMachine.TRASH = "PearTrash";
            StateMachine.countOnGround = 1;
            break;

        case (4):
            FINALANS = yellowPear;
            StateMachine.TRASH = "PearTrash";
            StateMachine.countOnGround = 1;
            break;

        case (5): 
            FINALANS = redBigApple;
            StateMachine.TRASH = "AppleTrash";
            StateMachine.countOnGround = 1;
            break;

        case (6):
            FINALANS = redSmallApple;
            StateMachine.TRASH = "SmallAppleTrash"; 
            StateMachine.countOnGround = 1;
            break;

        case (7):
            FINALANS = purple;
            StateMachine.TRASH = "UnripTrash";
            StateMachine.countOnGround = 1;
            break;

        default:
            FINALANS = "nothing";
            StateMachine.TRASH = "UnripTrash";
            StateMachine.countOnGround = 0;
            break;
        }
        
        SmartDashboard.putString("FINALANS", FINALANS);

        boolean end = false;
        
        
            end = false || train.getStart();

            if (FINALANS.equals(redBigApple)) {
                train.setGreen(true);
            } else {
                train.setGreen(false);  
            }
        // }

        if(end){
            server.nowTask = 0;
        }

        return end;
    }

    private boolean checkFruitTree(boolean mode) {
        server.nowTask = 7;

        count += Function.countTimer();

        SmartDashboard.putNumber("count", count);

        switch (server.nowResult) {
        case (1):
            FINALANS = greenBigApple;
            StateMachine.TRASH = "AppleTrash";
            StateMachine.countOnGround = 1;
            break;

        case (2):
            FINALANS = greenSmallApple;
            StateMachine.TRASH = "SmallAppleTrash";
            StateMachine.countOnGround = 1;
            break;

        case (3):
            FINALANS = greenPear;
            StateMachine.TRASH = "PearTrash";
            StateMachine.countOnGround = 1;
            break;

        case (4):
            FINALANS = yellowPear;
            StateMachine.TRASH = "PearTrash";
            StateMachine.countOnGround = 1;
            break;

        case (5):
            FINALANS = redBigApple;
            StateMachine.TRASH = "AppleTrash";
            StateMachine.countOnGround = 1;
            break;

        case (6):
            FINALANS = redSmallApple;
            StateMachine.TRASH = "SmallAppleTrash"; 
            StateMachine.countOnGround = 1;
            break;

        case (7):
            FINALANS = purple;
            StateMachine.TRASH = "UnripTrash";
            StateMachine.countOnGround = 1;
            break;

        default:
            FINALANS = "nothing";
            StateMachine.TRASH = "UnripTrash";
            StateMachine.countOnGround = 0;
            break;
        }
        
        SmartDashboard.putString("FINALANS", FINALANS);

        boolean end = false;
        
        if(!mode){
            end = count > 3f;
        }
        else{
            end = false || train.getStart();

            if (FINALANS.equals(redBigApple)) {
                train.setGreen(true);
            } else {
                train.setGreen(false);  
            }
        }

        if(end){
            server.nowTask = 0;
        }

        return end;
    }

    @Override
    public void initialize() {
        isForwardDone = false;
        isReachedPos = false;
        waitTimeExecute = 0;
        isForwardDone = false;
        count = 0;
        countForward = 0;
        countX = 0;
        end = false;
        endTime = 0;
    }
}