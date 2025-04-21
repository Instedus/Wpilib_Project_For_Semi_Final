package frc.robot.cases;

import frc.robot.RobotContainer;
import frc.robot.commands.IState;
import frc.robot.subsystems.Function;
import frc.robot.subsystems.Training;

public class Start implements IState{
    float waitTime = 2f;
    float count;
    Training train = RobotContainer.train;
    boolean isServoForwardReached = false;

    public Start(float waitTime){
        this.waitTime = waitTime;
    }

    @Override
    public boolean execute() {

        count += Function.countTimer();

        if (train.liftPosReached) {
            train.initializeLift = false;
            train.grabRotateAngle = 137;
            train.grabAngle = 110;

            // train.grabAngle = 220;
        } else {
            train.initializeLift = true;
        }

        if(!train.servoForwardPosReached && !train.getEms()){
            train.initializeServoForward = true;
        }
        else{
            train.initializeServoForward = false;
        }

        train.resetEnc(); 
        train.resetGyro(0);   
        train.resetCord(0, 0);
        train.resetEncoderLift();
        boolean initOms = train.getLimitSwitch() && train.getLimitSwitchForward();
        if (train.getStart() && (count > waitTime) && initOms) {
            train.setRed(true);
            train.setGreen(true);
            return true;
        } else if (count > waitTime){
            train.setRed(true);
            train.setGreen(false);
            return false;
        } else { 
            train.setRed(false);  
            train.setGreen(false);
            return false; 
        }
    }

    @Override
    public void initialize() {
        isServoForwardReached = false;
    }

}