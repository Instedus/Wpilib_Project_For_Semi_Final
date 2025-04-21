package frc.robot.cases;


import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.RobotContainer;
import frc.robot.commands.IState;
import frc.robot.commands.StateMachine;
import frc.robot.commands.StatesAutoFinal;
import frc.robot.commands.StatesRuslan;
import frc.robot.subsystems.Function;
import frc.robot.subsystems.Training;

public class Odometry implements IState {

    private Training train = RobotContainer.train;
    private String name;
    private float x, y, z;
    public float newX, newY, newZ;
    private boolean checkZ = false;
    public int localIndex = 0;

    private float[][] speedXArray = { { 0f, 5f, 15f, 40f, 70f, 150f, 165f, 250f },
            { 0f, 15f, 25f, 30f, 35f, 60f, 75f, 95f } };
    private float[][] speedZArray = { 
            { 0f, 0.5f, 3f, 6f, 12f, 26, 45, 120 },
            { 0f, 25f, 35f, 45f, 55f, 65f, 75f, 90f }
        }; 
    private float[][] speedZArrayMove = { { 0f, 0.5f, 3f, 6f, 12f, 26, 32, 50 },
            { 0f, 5f, 7f, 17f, 25f, 35f, 50f, 70f } };
    private float[][] timeSpeed = { { 0f, 0.15f, 0.3f, 0.5f, 0.8f, 1f }, { 0f, 0.1f, 0.3f, 0.5f, 0.8f, 1 } };
    
    private boolean smoothRideZ = false;

    public Odometry(boolean smoothRideZ, String name, float x, float y, float z, boolean checkZ) {
        this.name = name;
        this.x = x;
        this.y = y;
        this.z = z;
        this.checkZ = checkZ;
        this.smoothRideZ = smoothRideZ;
    }

    public Odometry(String name, float x, float y, float z, boolean checkZ) {
        this.name = name;
        this.x = x;
        this.y = y;
        this.z = z;
        this.checkZ = checkZ;
        this.smoothRideZ = false;
    }

    public Odometry(boolean smoothRideZ, float x, float y, float z, boolean checkZ) {
        this.name = StatesRuslan.rel;
        this.x = x;
        this.y = y;
        this.z = z;
        this.checkZ = checkZ;
        this.smoothRideZ = smoothRideZ;
    }

    public Odometry(boolean smoothRideZ, float x, float y, float z) {
        this.name = StatesRuslan.rel;
        this.x = x;
        this.y = y;
        this.z = z;
        this.checkZ = true;
        this.smoothRideZ = smoothRideZ;
    }

    public Odometry(float x, float y, float z) {
        this.name = StatesRuslan.rel;
        this.x = x;
        this.y = y;
        this.z = z;
        this.checkZ = true;
        this.smoothRideZ = false;
    }

    @Override
    public void initialize() {

        this.localIndex = 0;
        
        StateMachine.startTime = (float)Timer.getFPGATimestamp();

        if(StateMachine.logCordX != 0 || StateMachine.logCordY != 0){
            this.x = StateMachine.logCordX;
            this.y = StateMachine.logCordY;
            // if(StateMachine.absoluteZ != -1) {
            //     this.z = StateMachine.absoluteZ;
            // }
        }
        float r = (float) Math.sqrt(x * x + y * y);
        float theta = (float) Math.atan2(y, x);

        if(smoothRideZ){
            this.localIndex++;
        }
        
        if (name.equals(StatesAutoFinal.abs)) {
            this.newX = x;
            this.newY = y;
            this.newZ = z;
        } else {
            this.newX = (float) (r * Math.cos(theta + Math.toRadians(train.getAngle())) + train.posX);
            this.newY = (float) (r * Math.sin(theta + Math.toRadians(train.getAngle())) + train.posY);

            if(StateMachine.absoluteZ != -1){
                this.newZ = StateMachine.absoluteZ;
            }
            else{
                this.newZ = (float) (z + train.getAngle());
            }
        }

        
    }

    @Override
    public boolean execute() {
        
        return structureOdometry();
    }

    float speedX = 0, speedZ = 0; 
    boolean endZ = false, endX = false;

    private boolean structureOdometry() {
        float nowX = this.newX - train.posX;
        float nowY = this.newY - train.posY;
        float nowZ = this.newZ - train.getAngle();

        SmartDashboard.putNumber("nowX", nowX);
        SmartDashboard.putNumber("nowY", nowY);

        float r = (float) Math.sqrt(nowX * nowX + nowY * nowY);
        float theta = (float) Math.atan2(nowY, nowX);

        float acc = Function.TransF(timeSpeed, (float) (Timer.getFPGATimestamp() - StateMachine.startTime));

        float newZ = (Function.normalizeAngle((float) (theta - Math.toRadians(train.getAngle()))));

        switch (this.localIndex) {
        case 0: {
            float dist = generateSpeedZ(newZ);
            speedZ = Function.TransF(speedZArray, dist) * acc;
            endX = false;
            speedX = 0;
            if (Function.inRangeBool(dist, -1f, 1f)) {
                speedZ = 0;
                StateMachine.checkElement = true;
                this.localIndex++;
            }
        }
        break;

        case 1: {
            float dist = generateSpeedZ(newZ);
            speedZ = smoothRideZ ? generateSpeedZ(newZ)*acc : Function.TransF(speedZArrayMove, dist) * acc;
            speedX = Function.TransF(speedXArray, (float) (r * Math.cos(theta - Math.toRadians(train.getAngle()))))
                    * acc;

            float max = smoothRideZ ? 25 : 9;
            if (Function.inRangeBool(r, -max, max)) {
                speedX = 0;
                speedZ = 0;
                StateMachine.checkElement = true;
                this.localIndex++;
            }
        }
        break;

        case 2: {
            if (checkZ) {
                speedZ = Function.TransF(speedZArray, nowZ) * acc;
                speedX = 0;
                if (Function.inRangeBool(nowZ, -1, 1)) {
                    speedZ = 0;
                    StateMachine.checkElement = true;
                    this.localIndex++;
                }

            } else {
                // float dist = generateSpeedZ(newZ);
                // speedZ = Function.TransF(speedZArray, dist) * acc;
                speedX = 0;
                // if (Function.inRangeBool(dist, -1.2f, 1.2f)) {
                    speedZ = 0;
                    StateMachine.checkElement = true;
                    this.localIndex++;
                // }
                
                SmartDashboard.putNumber("newZ", newZ);
            }
        }
        break;

        case 3: {
            StateMachine.checkElement = true;
            speedZ = 0;
            speedX = 0;
        }
        }

        SmartDashboard.putNumber("localIndex", this.localIndex);

        train.setAxisSpeed(speedX, speedZ);
        return this.localIndex == 3;
    }

    private float generateSpeedZ(float newZ) {
        float speed = 0;

        if (Math.abs(newZ) > Math.PI / 2) {
            speed = (float) (Function.normalizeAngle((float) (newZ - Math.PI)) / Math.PI * 180);
        } else {
            speed = (float) (newZ / Math.PI * 180);
        }
        return speed;
    }

}