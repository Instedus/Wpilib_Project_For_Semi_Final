package frc.robot.cases;


import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.RobotContainer;
import frc.robot.commands.IState;
import frc.robot.commands.StateMachine;
import frc.robot.commands.StatesRuslan;
import frc.robot.subsystems.Function;
import frc.robot.subsystems.Training;

public class SensorsTest implements IState {

    float[][] speedXY = { { 0, 1, 3, 5, 8, 14, 18, 22 }, { 0f, 17f, 23f, 35f, 40f, 60f, 80f, 95f } };
    float[][] speedXYW = { { 0, 1 }, { 0f, 25 } };
    float[][] speedAngle = { { 0, 0.1f, 1, 3, 5, 8, 14, 18, 22 }, { 0f, 5f, 10f, 25f, 40f, 45f, 50, 70, 95 } };
    float[][] speedAngleCorrection = {{0f,0.1f,0.3f,0.6f, 1.2f, 2, 3f, 6f, 12f, 26, 32, 50}, {0f,10f,15f,25f,30f, 35f, 45f, 60f, 65f, 70f, 70f, 70f}};
    private float[][] timeSpeed = { { 0f, 0.15f, 0.3f, 0.5f, 0.8f, 1f }, 
    { 0f, 0.1f, 0.2f, 0.6f, 0.8f, 1 } };

    private Training train = RobotContainer.train;
    private String name;
    private float x, y, z;
    private float newX = 0, newZ = 0;
    float zChanged = 0;
    private float countTime = 0;

    boolean stopX = false;
    boolean stopY = false;
    boolean stopZ = false;

    int curIndexZ;

    public SensorsTest(String name, float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.name = name;
    }

    @Override
    public void initialize() {
        StateMachine.startTime = (float)Timer.getFPGATimestamp();
        zChanged = train.getAngle();
    }

    @Override
    public boolean execute() {
        return moveBySensors(this.name, this.x, this.y, this.z);
    }

    public boolean moveBySensors(String name, float x, float y, float z) {
        
        

        newX = calculateX();
        newZ = generateDistZ();

        SmartDashboard.putNumber("newZSensors", newZ);
        SmartDashboard.putNumber("newXSensors", newX);

        float acc = Function.TransF(timeSpeed, (float) (Timer.getFPGATimestamp() - StateMachine.startTime));

        stopX = Function.inRangeBool(newX, -1.2f, 1.2f);
        stopZ = Function.inRangeBool(newZ, -0.5f, 0.5f);

        float speedX = returnSpeed(speedXY, newX) * acc; 

        float speedZ = curIndexZ == 1 ? returnSpeed(speedAngleCorrection, newZ) * acc : returnSpeed(speedAngle, newZ);

        if(stopX && stopZ){
            train.setAxisSpeed(0,0);
        }
        else{
            train.setAxisSpeed(speedX, speedZ);
        }

        boolean end = false;

        if(stopX && stopZ){
            countTime += Function.countTimer();
            end = countTime > 1f;
        }
        else{
            countTime = 0;
        }

        return end;
    }

    public float calculateX() {
        if (this.x == 0) {
            return 0;
        }
        if (this.name.equals(StatesRuslan.ikl)) {
            return train.getSharpL() - Math.abs(this.x);
        } else if (this.name.equals(StatesRuslan.ikr)) {
            return train.getSharpR() - Math.abs(this.x);
        } else if (this.name.equals(StatesRuslan.sonicb)) {
            return Math.abs(this.x) - train.getSonicB();
        } else {
            return 0;
        }
    }

    private float returnSpeed(float[][] array, float cur){
        return Function.TransF(array, cur);
    }

    public float generateDistZ(){

        float speed = 0;

        if((train.getSharpL() + train.getSharpR() / 2) < 45 && this.z % 10 == 1){
            curIndexZ = 1;
        }else{
            curIndexZ = 0;
        }

        switch(this.curIndexZ){
            case 1: 
                speed = train.getSharpL() - train.getSharpR();
            break;

            case 2:
                speed = (this.z % 10 == 1 ? this.z - 1 : this.z) - zChanged;
            break;

            case 0:
                speed = zChanged - train.getAngle();
            break;
        }

        return speed;
    }


}
