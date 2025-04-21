package frc.robot.cases;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.RobotContainer;
import frc.robot.commands.IState;
import frc.robot.subsystems.Function;
import frc.robot.subsystems.Training;

public class TimerCount implements IState {

    Training train = RobotContainer.train;

    private float waitTime = 0;

    private float count = 0;

    public TimerCount(float sec) {
        this.waitTime = sec;
    }

    @Override
    public boolean execute() {
        return isTimer(waitTime);
    }

    private boolean isTimer(float count) {
        this.count += Function.countTimer();

        SmartDashboard.putNumber("count", this.count);

        if (this.count > waitTime) {
            return true;
        } else {
            train.setAxisSpeed(0, 0);
            return false;
        }
    }

    @Override
    public void initialize() {
        count = 0;
    }
}