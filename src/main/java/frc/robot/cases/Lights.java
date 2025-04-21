package frc.robot.cases;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.RobotContainer;
import frc.robot.commands.IState;
import frc.robot.subsystems.Function;
import frc.robot.subsystems.Training;

public class Lights implements IState {
    
    private Training train = RobotContainer.train;

    private boolean active = false;
    private boolean redActive = false;
    private boolean mode;
    private float count = 0;

    public Lights(boolean mode) {
        this.mode = mode;
    }

    @Override
    public boolean execute() {

        count += Function.countTimer();

        if(mode){
            if(train.getSonicB() < 20 || train.getSonicL() < 20){
                active = true;
            }
            else{
                active = false;
            }
        }
        else{
            if(train.getSharpR() < 20 || train.getSharpL() < 20){
               active = true;
            }
            else{
                active = false;
            }
        }

    // SmartDashboard.putBoolean("SONICs", redActive);
    // SmartDashboard.putBoolean("IKS", active);

        train.setGreen(active);
        // train.setRed(redActive);

        return count > 5f && train.getStart();
    }

    @Override
    public void initialize() {
    }

}