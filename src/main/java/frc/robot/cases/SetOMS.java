package frc.robot.cases;

import frc.robot.commands.IState;
import frc.robot.commands.StateMachine;
import frc.robot.commands.StateMachineOMS;

public class SetOMS implements IState {

    float posLift;
    float posGrab;
    float posForward;
    float posGrabRotate;
    float array;

    float count;

    public SetOMS(float posLift, float posForward, float posGrab, float posGrabRotate, float array) {
        this.posGrabRotate = posGrabRotate;
        this.posGrab = posGrab;
        this.posForward = posForward;
        this.posLift = posLift;
        this.array = array;
    }

    public SetOMS(int array) {
        this.array = array;
    }

    @Override
    public boolean execute() {
        StateMachine.isAutonomusOMS = true;
        StateMachineOMS.currentIndexOMS = 0;
        StateMachineOMS.currentArrayOMS = (int)this.array;
        return true;
    }

    @Override
    public void initialize() {

    }

}