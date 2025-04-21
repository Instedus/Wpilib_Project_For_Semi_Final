package frc.robot.cases;

import edu.wpi.first.wpilibj.Timer;
import frc.robot.RobotContainer;
import frc.robot.commands.IState;
import frc.robot.commands.StateMachine;
import frc.robot.subsystems.Training;

public class TimerDrive implements IState {

    private Training train = RobotContainer.train;

    private float speedRight, speedLeft;
    private float time;
    private boolean end = false;
    
    public TimerDrive(float right, float left, float time) {
        this.speedRight = right;
        this.speedLeft = left;
        this.time = time;
    }
    @Override
    public void initialize() {

    }

    @Override
    public boolean execute() {

        this.end = Timer.getFPGATimestamp() - StateMachine.startTime > time;
        if (this.end) {
            train.setMotorsSpeed(0, 0);
        } else {
            train.setMotorsSpeed(this.speedRight, this.speedLeft);
        }
        return this.end;
    }
    
}