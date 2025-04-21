package frc.robot.cases;

import frc.robot.RobotContainer;
import frc.robot.commands.IState;
import frc.robot.subsystems.Function;
import frc.robot.subsystems.Training;

public class Reset implements IState {

    Training train = RobotContainer.train;

    private float x = 0;
    private float y = 0;
    private float z = 0;
    private float count;
    private float waitTime = 0.5f;

    public Reset(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    @Override
    public boolean execute() {
        count += Function.countTimer();
        if(x != -1 && y != -1){
            train.resetCord(x, y);
        }
        train.resetGyro(z);
        return count > waitTime;
    }

    @Override
    public void initialize() {
    }
}