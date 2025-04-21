package frc.robot.casesOMS;

import edu.wpi.first.wpilibj.Timer;
import frc.robot.RobotContainer;
import frc.robot.commands.IState;
import frc.robot.commands.StateMachine;
import frc.robot.commands.StateMachineOMS;
import frc.robot.subsystems.Function;
import frc.robot.subsystems.Training;

public class OMS implements IState {
    float x;
    float y;
    float servo;
    float servoAngle;

    Training train = RobotContainer.train;

    private float count;
    private float waitTime = 1f;

    boolean endServo = false;

    private float[][] timeSpeedForAngle = { {0,0.3f,0.7f,1,1.5f,2f,2.5f,3f,3.5f,4,4.5f},
            {0,0.1f,0.2f,0.3f,0.4f,0.5f,0.6f,0.7f,0.8f,0.9f,1} };

    public OMS(float x, float y, float servo, float servoAngle) {
        this.x = x;
        this.y = y;
        this.servo = servo;
        this.servoAngle = servoAngle;
    }

    @Override
    public boolean execute() {

        if(!StateMachine.isAutonomusOMS){
            train.setAxisSpeed(0, 0);

            if (StateMachine.isFirst) {
                count = 0;
                train.initializeLift = false;
                train.servoForwardPosReachedMove = false;
                StateMachine.startTime = (float) Timer.getFPGATimestamp();
                StateMachine.isFirst = false;
                endServo = false;
            }
        }

        else{
            if (StateMachineOMS.isFirst) {
                count = 0;
                train.initializeLift = false;
                train.servoForwardPosReachedMove = false;
                StateMachine.startTime = (float) Timer.getFPGATimestamp();
                StateMachineOMS.isFirst = false;
                endServo = false;
            }
        }

        if(y < -1){
            y = Math.abs(y) + train.countCobra;
        }

        return moveLift(this.x, this.y, this.servo, this.servoAngle);
    }

    private int[] arrayOMSposition = { -915, -990, -680, -330 };
    private int[] beforeArrayOMSposition = { -500, -990, -680, -330 };
    private boolean isOMSTree = false;

    private boolean moveLift(float x, float y, float servo, float servoAngle) {

        isOMSTree = StateMachine.positionOMSLogic >= 1;
 
        float acc = Function.TransF(timeSpeedForAngle, (float) (Timer.getFPGATimestamp() - StateMachine.startTime));

        count += Function.countTimer();

        if (x != -1 && x != -2) {
            train.positionLift = x;
        } else {
            if (StateMachine.positionOMSLogic != -1 && x == -1) {
                train.positionLift = arrayOMSposition[StateMachine.positionOMSLogic];
            } else if (StateMachine.positionOMSLogic != -1 && x == -2) {
                train.positionLift = beforeArrayOMSposition[StateMachine.positionOMSLogic];
            }
        }

        if (y == -1) {
            train.destination = train.countCobra;
        }
        // else if(StateMachine.positionOMSLogic != -1 && x == -2 && beforeArrayOMSposition[StateMachine.positionOMSLogic] != beforeArrayOMSposition[0]){
        //     train.destination = 14;
        // }
        else {
            train.destination = (int) this.y;
        }

        if (servo != -1 && servo != train.grabAngle) {
            // if(train.getGrabAngle() == train.grabAngle){
            // train.grabAngle = train.grabAngle;
            // }
            // else{
            // train.grabAngle = servo * acc;
            // }

            float calculatedPos = (servo - train.grabAngle) * acc;
            
            // if(calculatedPos > 0){
                train.grabAngle += calculatedPos;
            // }
            // else{
            //     train.grabAngle = -train.grabAngle - calculatedPos;
            // }

            
            
            if(train.grabAngle == servo){
                endServo = true;
            }
            else{
                endServo = false;
            }

            // train.grabAngle = servo;
        } else {
            endServo = true;
            train.grabAngle = train.grabAngle;
        }

        if (servoAngle != -1) {
            train.grabRotateAngle = servoAngle;
        } else {
            if (isOMSTree) {
                train.grabRotateAngle = 60;
            } else {
                train.grabRotateAngle = 137;
                // train.grabRotateAngle = train.grabRotateAngle;
            }
        }

        return count > waitTime && train.liftPosReached && train.servoForwardPosReachedMove && endServo;
    }

    @Override
    public void initialize() {
        // if(StatesOMS.posLift!=-1){
        // this.x = StatesOMS.posLift;
        // }
        // if(StatesOMS.posForward!=-1){
        // this.y = StatesOMS.posForward;
        // }
        // if(StatesOMS.posGrab != -1){
        // this.servo = StatesOMS.posGrab;
        // }
        // if(StatesOMS.posGrabRotate != -1){
        // this.servoAngle = StatesOMS.posGrabRotate;
        // }
        isOMSTree = false;

    }

}