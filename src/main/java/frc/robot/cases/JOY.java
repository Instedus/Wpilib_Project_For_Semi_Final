package frc.robot.cases;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.RobotContainer;
import frc.robot.commands.IState;
import frc.robot.commands.StateMachine;
import frc.robot.subsystems.Function;
import frc.robot.subsystems.Training;

public class JOY implements IState {
    private final Training train = RobotContainer.train;
    private static final Joystick joystick = new Joystick(0);

    private boolean buttonAcceleration;
    private boolean buttonLeds;
    private boolean buttonGrab;
    private boolean buttonFrontServo, buttonFirstServo, buttonSecondServo, buttonThirdServo;

    private float buttonTurnRight;
    private float buttonTurnLeft;

    private float buttonMoveX;
    private float buttonMoveY;

    private float buttonElevator;
    private int buttonAutoElevator;

    private int countLift = 0;
    private boolean changeModeJoysticLift = false;
    private boolean changeSpeedLiftJOY = true;

    private int countCommandsAutoElevator = 0;

    boolean finalEnd;

    boolean isTurning;

    boolean isTurned;

    boolean canExit = true;

    float wannaAngle;

    private float[][] speedZArray = { { 0f, 0.5f, 3f, 6f, 12f, 26, 45, 120 },
            { 0f, 25f, 35f, 45f, 55f, 65f, 75f, 90f } };

    @Override
    public boolean execute() {
        try {
            updateValues();
            printValues();

            resetCords();
            skipIndex();
            lift();

            if (buttonTurnRight > 0.5f || buttonTurnLeft > 0.5f || !canExit) {
                rotate90();
            } else {
                move();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return finalEnd;
    }

    boolean autoElevatorPerehod = false;

    private void updateValues() {
        buttonAcceleration = joystick.getRawButton(8);
        buttonLeds = joystick.getRawButton(6);
        buttonGrab = joystick.getRawButton(2);
        buttonFrontServo = joystick.getRawButton(1);
        buttonFirstServo = joystick.getRawButton(4);
        buttonSecondServo = joystick.getRawButton(5);
        buttonThirdServo = joystick.getRawButton(3);

        buttonAutoElevator = joystick.getPOV();

        if (buttonAutoElevator == 180 && !autoElevatorPerehod) {
            if (countCommandsAutoElevator <= 1) {
                countCommandsAutoElevator++;
            }
        } else if (buttonAutoElevator == 0 && !autoElevatorPerehod) {
            if (countCommandsAutoElevator <= 3 && countCommandsAutoElevator > 0) {
                countCommandsAutoElevator--;
            }
        }
        autoElevatorPerehod = buttonAutoElevator != -1;

        buttonTurnRight = (float) joystick.getRawAxis(3);
        buttonTurnLeft = (float) joystick.getRawAxis(2);

        buttonMoveX = -(float) joystick.getRawAxis(1);
        buttonMoveY = (float) joystick.getRawAxis(0);

        buttonElevator = (float) joystick.getRawAxis(5);
    }

    private void printValues() {
        SmartDashboard.putBoolean("buttonAcceleration", buttonAcceleration);
        SmartDashboard.putBoolean("buttonLeds", buttonLeds);
        SmartDashboard.putBoolean("buttonGrab", buttonGrab);
        SmartDashboard.putBoolean("buttonFrontServo", buttonFrontServo);
        SmartDashboard.putBoolean("buttonFirstServo", buttonFirstServo);
        SmartDashboard.putBoolean("buttonSecondServo", buttonSecondServo);
        SmartDashboard.putBoolean("buttonThirdServo", buttonThirdServo);

        SmartDashboard.putNumber("buttonAutoElevator", buttonAutoElevator);
        SmartDashboard.putNumber("buttonTurnRight", buttonTurnRight);
        SmartDashboard.putNumber("buttonTurnLeft", buttonTurnLeft);
        SmartDashboard.putNumber("buttonMoveX", buttonMoveX);
        SmartDashboard.putNumber("buttonMoveY", buttonMoveY);
        SmartDashboard.putNumber("buttonElevator", buttonElevator);
    }

    private void move() {
        float newX = buttonMoveX > 0.5f || buttonMoveX < 0.5f ? buttonMoveX : 0;
        float newZ = buttonMoveY > 0.5f || buttonMoveY < 0.5f ? buttonMoveY : 0;

        train.setAxisSpeed(newX * 50, newZ * 50);
    }

    private void resetCords() {
        if (selectPressedButton() == PressedButton.buttonX) {
            train.resetCord(0, 0);
            train.resetEnc();
            train.resetGyro(0);
        }
    }

    private void skipIndex() {
        if (selectPressedButton() == PressedButton.buttonY) {
            StateMachine.currentIndex = 2;
            finalEnd = false;
        }
    }

    private void lift() {

        if(!buttonGrab && changeSpeedLiftJOY){
            RobotContainer.train.initializeLift = false;
            RobotContainer.train.positionLift = train.getEncLift() - buttonElevator*500;
            countLift = -1;
            changeSpeedLiftJOY = !buttonGrab;
        }else{
            RobotContainer.train.initializeLift = false;
            if (buttonGrab && !changeModeJoysticLift) {
                countLift++;
                if (countLift >= 4) {
                    countLift = 0;
                }
            }
            changeModeJoysticLift = buttonGrab;
            SmartDashboard.putNumber("countLift", countLift);
            switch (countLift) {
            case 0:
                RobotContainer.train.initializeLift = true;
                RobotContainer.train.positionLift = 0;
                break;
            case 1:
                RobotContainer.train.positionLift = -385;
                break;
            case 2:
                RobotContainer.train.positionLift = -710;
                break;
            case 3:
                RobotContainer.train.positionLift = -920;
                break;
            case 4:
                RobotContainer.train.positionLift = -1084;
                break;
            }
            changeSpeedLiftJOY = !Function.inRangeBool(buttonElevator, -0.1f, 0.1f);
        }
    }

    private void rotate90() {

        SmartDashboard.putNumber("wannaZJoy", wannaAngle);
        SmartDashboard.putNumber("movingZWannaJoy", wannaAngle - train.getAngle());

        if (!isTurned) {
            wannaAngle = train.getAngle();
        }

        if (buttonTurnLeft > 0.5f && !isTurning) {
            wannaAngle -= 90;
            isTurned = true;
            canExit = false;
            isTurning = true;
        } else if (buttonTurnRight > 0.5f && !isTurning) {
            wannaAngle += 90;
            isTurned = true;
            canExit = false;
            isTurning = true;
        }

        boolean end = Function.inRangeBool(wannaAngle - train.getAngle(), -1, 1);

        float speedZ = Function.TransF(speedZArray, wannaAngle - train.getAngle());

        if (end) {
            isTurned = false;
            isTurning = false;
            canExit = true;
            train.setAxisSpeed(0, 0);
        } else {
            train.setAxisSpeed(0, speedZ);
        }
    }

    private PressedButton selectPressedButton() {
        if (buttonFirstServo) {
            return PressedButton.buttonX;
        } else if (buttonSecondServo) {
            return PressedButton.buttonY;
        } else if (buttonThirdServo) {
            return PressedButton.buttonThirdServo;
        } else if (buttonFrontServo) {
            return PressedButton.buttonFrontServo;
        } else {
            return PressedButton.buttonDefault;
        }
    }

    @Override
    public void initialize() {
    }
}

enum PressedButton {
    buttonX, buttonY, buttonThirdServo, buttonFrontServo, buttonDefault
}
