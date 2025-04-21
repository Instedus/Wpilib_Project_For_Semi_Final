package frc.robot.commands;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class WhereElementLogic implements IState{

    boolean onGround;
    float absoluteZ;

    @Override
    public void initialize() {

    }

    public WhereElementLogic(boolean onGround, float absoluteZ) {
        this.onGround = onGround;
        this.absoluteZ = absoluteZ;
    }

    @Override
    public boolean execute() {
        StateMachine.onGround = this.onGround;
        StateMachine.absoluteZ = this.absoluteZ;

        SmartDashboard.putNumber("WhereElementLogic", StateMachine.absoluteZ);

        return true;
    }

}