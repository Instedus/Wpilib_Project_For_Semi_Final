package frc.robot.subsystems;

//WPI imports
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.wpilibj.AnalogInput;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.DigitalOutput;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.Ultrasonic;
import edu.wpi.first.wpilibj.shuffleboard.BuiltInWidgets;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

import java.util.Map;

//Vendor imports
import com.kauailabs.navx.frc.AHRS;
import com.studica.frc.TitanQuad;
import com.studica.frc.TitanQuadEncoder;
import com.studica.frc.Cobra;
import com.studica.frc.Servo;
import com.studica.frc.ServoContinuous;

public class Training extends SubsystemBase {
   
    private TitanQuad motorRight, motorLeft, motorLift;
    private TitanQuadEncoder encRight, encLeft, encLift;

    private AHRS gyro;
    
    private ServoContinuous grabForward;
    private Servo grab, grabRotate;

    private DigitalInput startButton, emsButton, limitSwitch, limitSwitchForward;
    private DigitalOutput ledRed, ledGreen;
    
    private float lastLeft;
    private float lastRight;

    private PID motorPIDRight = new PID(0.95f, 0.0001f, 0.0001f);
    private PID motorPIDLeft = new PID(0.95f, 0.0001f, 0.0001f);
    private PID motorPIDLift = new PID(0.7f, 0.0001f, 0.000001f);

    private MeanFilter meanFilterSonicB, meanFilterSonicL, meanFilterSharpL, meanFilterSharpR;
    private MedianFilter medianFilterSonicB, medianFilterSonicL, medianFilterSharpL, medianFilterSharpR;

    private float speedRightMotor;
    private float speedLeftMotor;
    
    private float curAngle;
    private float lastAngle;

    public float grabAngle = 100, grabRotateAngle = 130;
    public float grabSpeed;

    public float positionLift;
    private float speedLift;
    public boolean initializeLift = false;
    public boolean liftPosReached = false;

    public int destination;
    public boolean initializeServoForward = false;
    public float speedServoForward;
    public boolean servoForwardPosReached = false;
    public boolean servoForwardPosReachedMove = false;

    private boolean isCheckedCobra = false;

    public int countCobra;

    private Cobra cobra;

    private Ultrasonic sonicL, sonicB;
    private AnalogInput sharpL,sharpR;
    
    public float posX, posY;

    public float getSliderR, getSliderL;
    public float getRp, getRi, getRd;
    public float getLp, getLi, getLd;
    private ShuffleboardTab tab;

    private static final float[][] speedLiftFunc = { { 0f, 8f, 20f, 100f, 200f, 350f , 450f, 550f}, { 0f, 15f, 25f, 35, 45, 60f , 75f, 85f} };

    public Training() {
        
        motorRight = new TitanQuad(42, 0);
        motorLeft = new TitanQuad(42, 2);
        motorLift = new TitanQuad(42,3);

        encRight = new TitanQuadEncoder(motorRight, 0, 0.2399827721492203);
        encLeft = new TitanQuadEncoder(motorLeft, 2, 0.2399827721492203);
        encLift = new TitanQuadEncoder(motorLift, 3, 0.2399827721492203);

        gyro = new AHRS();

        grab = new Servo(5);
        grabRotate = new Servo(3);
        grabForward = new ServoContinuous(4);

        startButton = new DigitalInput(7);
        emsButton = new DigitalInput(6);

        cobra = new Cobra();

        ledGreen = new DigitalOutput(20);
        ledRed = new DigitalOutput(21);

        limitSwitch = new DigitalInput(0);
        limitSwitchForward = new DigitalInput(1);

        sharpL = new AnalogInput(1);
        sharpR = new AnalogInput(0);

        sonicL = new Ultrasonic(10,11);
        sonicB = new Ultrasonic(8,9);

        meanFilterSharpL = new MeanFilter(3);
        meanFilterSharpR = new MeanFilter(3);
        meanFilterSonicB = new MeanFilter(3);
        meanFilterSonicL = new MeanFilter(3);

        medianFilterSharpL = new MedianFilter(6);
        medianFilterSharpR = new MedianFilter(6);
        medianFilterSonicB = new MedianFilter(6);
        medianFilterSonicL = new MedianFilter(6);

        new Thread(() -> {
            while (!Thread.interrupted()) {
                try {
                    
                    this.calcCoordinates();

                    this.setGrabRotate(this.grabRotateAngle);
                    this.setGrab(this.grabAngle);
                    this.setSpeedGrab(this.speedServoForward);

                    setMotor(this.speedRightMotor, motorRight, motorPIDRight, encRight);
                    setMotor(this.speedLeftMotor, motorLeft, motorPIDLeft, encLeft);

                    setElevator();
                    setServoForward();

                    Thread.sleep(5);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    } 

    private float getCobraLeft2() {
        return cobra.getRawValue(0);
    }

    private float getCobraLeft() {
        return cobra.getRawValue(1);
    }

    private boolean getCobraRight() {
        return cobra.getRawValue(2) > 1600;
    }

    private float getCobraRight2() {
        return cobra.getRawValue(3);
    }

    public void setMotorsSpeed(float right, float left) {
        float max = Math.max(Math.abs(right), Math.abs(left));
        
        float coff = 1;

        if(max > 95){
            coff = 95 / max;
        }

        this.speedRightMotor = right * coff;
        this.speedLeftMotor = left * coff;
    }

    public void setAxisSpeed(float x, float z){
        float right = x - z;
        float left = - x - z;

        float max = Math.max(Math.abs(right), Math.abs(left));

        float coff = 1;

        if(max > 95){
            coff = 95 / max;
        }

        this.speedRightMotor = right * coff;
        this.speedLeftMotor = left * coff;
    }

    private void setElevator() {

        if (this.initializeLift || this.positionLift == 0) {
            this.speedLift = -75;
            this.liftPosReached = getLimitSwitch();
        } else {
            this.speedLift = Function.TransF(speedLiftFunc, getEncLift() - this.positionLift);
            this.liftPosReached = Function.inRangeBool(this.positionLift - getEncLift(), -10, 10);
        }

        if ((this.liftPosReached && !getLimitSwitch()) || getEms()) {
            motorPIDLift.resetPID();
            this.speedLift = 0;
        } else if (this.speedLift < 0 && getLimitSwitch()) {
            motorPIDLift.resetPID();
            this.speedLift = 0;
            encLift.reset();
        } else if (this.speedLift > 0 && getEncLift() < -991) {
            motorPIDLift.resetPID();
            this.speedLift = 0;
        } else {
            motorPIDLift.setPoint = this.speedLift;
            float newSpeed = motorPIDLift.calculate((float) encLift.getSpeed()) / 100;
            this.speedLift = newSpeed;
        }
        motorLift.set(this.speedLift);
    }

    public float getAngle(){
        curAngle += (float) (gyro.getAngle() - this.lastAngle);
        lastAngle = (float) (gyro.getAngle());
        return curAngle;
    }

    public void resetGyro(float z){
        lastAngle = 0;
        curAngle = z;
        gyro.reset();
    }

    public void zeroYaw(){
        lastAngle = 0;
        curAngle = 0;
        gyro.zeroYaw();
    }

    public void resetEnc(){
        lastLeft = 0;
        lastRight = 0;
        speedRightMotor = 0;
        speedLeftMotor = 0;
        encLeft.reset();
        encRight.reset();
    }

    public boolean getLimitSwitch(){
        return limitSwitch.get();
    }

    public boolean getLimitSwitchForward(){
        return limitSwitchForward.get();
    }

    public void setGreen(boolean mode) {
        ledGreen.set(!mode);
    }

    public void setRed(boolean mode) {
        ledRed.set(!mode);
    }

    public boolean getEms() {
        return emsButton.get();
    }

    public boolean getStart() {
        return startButton.get();
    }

    public void resetEncoderLift() {
        speedLift = 0;
        encLift.reset();
    }

    private void calcCoordinates(){
        float nowRight = getEncRight() - lastRight;
        float nowLeft = getEncLeft() - lastLeft;

        float nowX = ((nowRight - nowLeft) / 2) /1.1627906976744f;

        float r = (float) Math.sqrt(nowX * nowX);
        float theta = (float) (Math.atan2(0, nowX));

        posX += r * Math.cos(theta + Math.toRadians(getAngle()));
        posY += r * Math.sin(theta + Math.toRadians(getAngle()));

        lastRight = getEncRight();
        lastLeft = getEncLeft();
    }

    private float getEncRight(){
        return (float) encRight.getEncoderDistance();
    }

    private float getEncLeft(){
        return (float) encLeft.getEncoderDistance();
    }

    public float getEncLift(){
        return (float) encLift.getEncoderDistance();
    }
    
	public void resetCord(float x, float z) {
        this.posX = x;
        this.posY = z;
	}

    private void setMotor(float speed, TitanQuad motor, PID motorPid, TitanQuadEncoder enc) {
        if (speed == 0 || getEms()) {
            motorPid.resetPID();
            motor.set(0);
            motorPid.setPoint = 0;
        } else {
            motorPid.setPoint = speed;
            float newSpeed = motorPid.calculate((float) enc.getSpeed()) / 100;
            motor.set(newSpeed);
        }
    }


    public void setSpeedGrab(float sp) {
        if (getEms() || (getLimitSwitchForward() && sp < 0)) {
            grabForward.setDisabled();
        } else {
            grabForward.setSpeed(sp);
        }
    }
    
    public void setGrab(float angle) {
        if (getEms()) {
            grab.setDisabled();
        } else {
            grab.setAngle(angle);
        }
    }

    public void setGrabRotate(float angle) {
        if (getEms()) {
            grabRotate.setDisabled();
        } else {
            grabRotate.setAngle(angle);
        }
    }

    public float getGrabAngle() {
        return (float) grab.getAngle();
    }

    public float getGrabRotateAngle() {
        return (float) grabRotate.getAngle();
    }

    private void setServoForward() {
        int wannaDist = 0;
        if(getEncLift() > -300){
            wannaDist = destination - countCobraFunction();
        }
        else{
            if(destination < 13){
                wannaDist = 12 - countCobraFunction();
            }
            else if(destination >= 25){
                wannaDist = 20 - countCobraFunction();
            }
            else{
                wannaDist = destination - countCobraFunction();
            }
        }

        if (servoForwardPosReached) {
            this.initializeServoForward = false;
            this.speedServoForward = 0;
            countCobra = 0;
            servoForwardPosReached = false;
        } 
        else if (initializeServoForward) {
            this.speedServoForward = -0.5f;
            this.servoForwardPosReached = getLimitSwitchForward();
        }
        else {
            if (!getEms()) {
                if (wannaDist > 0) {
                    this.speedServoForward = 0.7f;
                } else if (wannaDist < 0) {
                    if (getLimitSwitchForward()) {
                        this.speedServoForward = 0;
                    } else {
                        this.speedServoForward = -0.7f;
                    }
                } else {    
                    servoForwardPosReachedMove = true;
                    this.speedServoForward = 0;
                }

            } else {
                this.speedServoForward = 0;
            }
        }

       
        if(getLimitSwitchForward() && this.speedServoForward < 0){
            grabForward.setDisabled();
        }
        else{
            grabForward.setSpeed(speedServoForward);
        }

        SmartDashboard.putBoolean("endLine", this.servoForwardPosReachedMove);
    }

    private int countCobraFunction() {
        if (!getLimitSwitchForward() && !initializeServoForward) {
            if (!getCobraRight() && !isCheckedCobra) {
                if(getSpeedServoForward() > 0) {
                    countCobra++;
                    isCheckedCobra = true;
                } else if (getSpeedServoForward() < 0) {
                    countCobra--;
                    isCheckedCobra = true;
                }
            }
            if (getCobraRight() && isCheckedCobra) {
                isCheckedCobra = false;
            }
        }
        return countCobra;
    }

    private float getSpeedServoForward() {
        return (float) grabForward.getSpeed();
    }

    public float getSonicB() {
        try {
            this.sonicB.ping();
            Timer.delay(0.005);
            return (float) (meanFilterSonicB
                    .Filter((medianFilterSonicB.Filter((float) (sonicB.getRangeMM() / 10)))));
        } catch (Exception e) {
            return 10;
        }
    }

    public float getSonicL() {
        try {
            this.sonicL.ping();
            Timer.delay(0.005);
            return (float) (meanFilterSonicL
                    .Filter((medianFilterSonicL.Filter((float) (sonicL.getRangeMM() / 10)))));
        } catch (Exception e) {
            return 10;
        }
    }

    public float getSharpL() {
        return (float) (meanFilterSharpL.Filter(
                (medianFilterSharpL.Filter((float) (Math.pow(sharpL.getAverageVoltage(), -1.2045) * 27.726)))));
    }

    public float getSharpR() {
        return (float) (meanFilterSharpR.Filter(
                (medianFilterSharpR.Filter((float) (Math.pow(sharpR.getAverageVoltage(), -1.2045) * 27.726)))));
    }
    

    @Override
    public void periodic() {

        SmartDashboard.putNumber("encRight", getEncRight());
        SmartDashboard. putNumber("encLeft", getEncLeft());

        SmartDashboard.putNumber("encRightSpeed", encRight.getSpeed());
        SmartDashboard.putNumber("encLeftSpeed", encLeft.getSpeed());

        SmartDashboard.putNumber("posX", this.posX);
        SmartDashboard.putNumber("posY", this.posY);
        SmartDashboard.putNumber("posZ", this.getAngle());

        SmartDashboard.putBoolean("getEMS", getEms());
        SmartDashboard.putBoolean("getStart", getStart());

        SmartDashboard.putNumber("ikr", getSharpR());
        SmartDashboard.putNumber("ikl", getSharpL());

        SmartDashboard.putNumber("sonicL", getSonicL());
        SmartDashboard.putNumber("sonicB", getSonicB());

        SmartDashboard.putBoolean("limitSwitch", getLimitSwitch());
        SmartDashboard.putBoolean("limitSwitchForward", getLimitSwitchForward());

        SmartDashboard.putNumber("grab", grabAngle);
        SmartDashboard.putNumber("grabRotate", grabRotateAngle);

        SmartDashboard.putNumber("encLift", getEncLift());
        SmartDashboard.putNumber("positionLift", positionLift);
        SmartDashboard.putBoolean("initLift", initializeLift);

        SmartDashboard.putNumber("countCobra", this.countCobra);
    }
}