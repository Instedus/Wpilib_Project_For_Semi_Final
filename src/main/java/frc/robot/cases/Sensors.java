package frc.robot.cases;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.RobotContainer;
import frc.robot.commands.IState;
import frc.robot.commands.StateMachine;
import frc.robot.commands.StatesRuslan;
import frc.robot.subsystems.Function;
import frc.robot.subsystems.Training;

public class Sensors implements IState {

    private Training train = RobotContainer.train;
    private String name;
    private float x, y, z;
    private float newX, newY, newZ;
    private float firstAngle;

    private int indexElement = 0;

    private float[][] speedXArray = { { 0f, 5f, 15f, 40f, 70f, 150f, 165f, 250f },
            { 0f, 15f, 25f, 30f, 35f, 60f, 75f, 95f } };
    private float[][] speedZArray = { 
            { 0f, 0.5f, 3f, 6f, 12f, 26, 45, 120 },
            { 0f, 25f, 35f, 45f, 55f, 65f, 75f, 90f }
        }; 
    private float[][] speedZArrayMove = { { 0f, 0.5f, 3f, 6f, 12f, 26, 32, 50 },
            { 0f, 10f, 12f, 20f, 35f, 50f, 60f, 70f } };
    private float[][] timeSpeed = { { 0f, 0.15f, 0.3f, 0.5f, 0.8f, 1f }, { 0f, 0.1f, 0.3f, 0.5f, 0.8f, 1 } };

    private boolean end = false;

    public Sensors(String name, float x, float y, float z) {
        this.name = name;
        this.x = x;
        this.y = y;
        this.z = z;
    }

    @Override
    public void initialize() {

        float nowX = calculateX(this.x);
        float nowY = calculateY(this.y);
        float r = (float) Math.sqrt(nowX * nowX + nowY * nowY);
        float theta = (float) Math.atan2(nowY, nowX);

        SmartDashboard.putNumber("r", r);
        SmartDashboard.putNumber("theta", theta);

        this.newX = (float) (r * Math.cos(theta + Math.toRadians(train.getAngle())) + train.posX);
        this.newY = (float) (r * Math.sin(theta + Math.toRadians(train.getAngle())) + train.posY);
        this.newZ = (float) (z + train.getAngle());

        this.firstAngle = train.getAngle();

    }

    @Override
    public boolean execute() {
        return this.driveSensors();
    }

    private float speedX, speedZ = 0;
    private boolean endX = false;

    private boolean driveSensors() {
        float nowX = this.newX - train.posX;
        float nowY = this.newY - train.posY;
        float nowZ = this.newZ - train.getAngle();

        SmartDashboard.putNumber("nowX", nowX);
        SmartDashboard.putNumber("nowY", nowY);

        float r = (float) Math.sqrt(nowX * nowX + nowY * nowY);
        float theta = (float) Math.atan2(nowY, nowX);

        float acc = Function.TransF(timeSpeed, (float) (Timer.getFPGATimestamp() - StateMachine.startTime));

        float newZ = (Function.normalizeAngle((float) (theta - Math.toRadians(train.getAngle()))));

        switch (this.indexElement) {
        case 0: {
            float dist = generateDistanceZ(newZ);
            speedZ = Function.TransF(speedZArray, dist) * acc;
            endX = false;
            speedX = 0;
            if (Function.inRangeBool(dist, -1, 1f)) {
                speedZ = 0;
                StateMachine.checkElement = true;
                this.indexElement++;
            }
        }
        break;

        case 1: {
            float dist = generateDistanceZ(newZ);
            speedZ = Function.TransF(speedZArrayMove, dist) * acc;
            speedX = Function.TransF(speedXArray, (float) (r * Math.cos(theta - Math.toRadians(train.getAngle()))))
                    * acc;
            if (Function.inRangeBool(r, -5, 5)) {
                speedX = 0;
                StateMachine.checkElement = true;
                this.indexElement++;
            }
        }
        break;

        case 2: {
            nowZ = this.firstAngle - train.getAngle();
            speedZ = Function.TransF(speedZArray, nowZ) * acc;
            speedX = 0;
            if (Function.inRangeBool(nowZ, -0.7f, 0.7f)) {
                StateMachine.checkElement = true;
                this.indexElement++;
            }
        }
        break;

        case 3: {
            this.newX = this.x;
            this.newY = this.y;
            this.newZ = this.z;

            this.firstAngle = train.getAngle();
            StateMachine.checkElement = true;
            this.indexElement++;
        }
        break;

        case 4: {

            nowX = calculateX(x) / 10;
            nowY = calculateY(y) / 10;

            r = (float) (Math.sqrt(nowX * nowX + nowY * nowY));
            theta = (float) (Math.atan2(nowY, nowX));
    
            newZ = (Function.normalizeAngle((float) (theta - Math.toRadians(train.getAngle()))));
            float distance = generateDistanceZ(newZ);
    
            this.speedZ = Function.TransF(speedZArray, distance) * acc;
    
            this.speedX = Function.TransF(this.speedXArray,
                    (float) (r * Math.cos(theta - Math.toRadians(train.getAngle())))) * acc;
    
            this.endX = Function.inRangeBool(r, -0.7f, 0.7f);

            if (this.endX) {
                StateMachine.checkElement = true;
                this.indexElement++;
            }
        }
        break;

        case 5: {
            nowZ = calculateZ();
            speedZ = Function.TransF(speedZArray, nowZ) * acc;
            speedX = 0;
            if (Function.inRangeBool(nowZ, -0.7f, 0.7f)) {
                StateMachine.checkElement = true;
                this.indexElement++;
            }
        }
        break;
        

        case 6: {
            this.speedX = 0;
            this.speedZ = 0;
            end = true;
        }
        break;
    }
        SmartDashboard.putNumber("indexElement", this.indexElement);
        train.setAxisSpeed(this.speedX, this.speedZ);
        return end;
    }



    private float calculateX(float x) {
        float residualX;
        if (name.equals(StatesRuslan.ikl)) {
            residualX = train.getSharpL() - x;
        } else if (name.equals(StatesRuslan.ikr)) {
            residualX = train.getSharpR() - x;
        } else if (name.equals(StatesRuslan.sonicb)) {
            residualX = x - train.getSonicB();
        } else {
            residualX = 0;
        }
        return residualX * 10;
    }

    private float calculateY(float y) {
        float residualY;
        if (y == 0) {
            residualY = 0;
        } else {
            residualY = y - train.getSonicL();
        }
        return residualY * 10;
    }

    private float calculateZ() {
        float nowZ;
        if (this.z == 1) {
            if (train.getSharpR() < 25 || train.getSharpL() < 25) {
                nowZ = train.getSharpL() - train.getSharpR();
                if (Function.inRangeBool(train.getSharpL() - train.getSharpR(), -0.5f, 0.5f)) {
                    nowZ = 0;
                }
                SmartDashboard.putNumber("zzzzzzzz", nowZ);
            } else {
                if (Function.inRangeBool(this.firstAngle - train.getAngle(), -0.8f, 0.8f)) {
                    nowZ = 0;
                } else {
                    nowZ = this.firstAngle - train.getAngle();
                }
            }
        } else {
            if (Function.inRangeBool(this.firstAngle - train.getAngle(), -1f, 1f)) {
                nowZ = 0;
            } else {
                nowZ = this.firstAngle - train.getAngle();
            }
        }

        return nowZ;
    }

    private float generateDistanceZ(float newZ) {
        float speed;

        if (Math.abs(newZ) > Math.PI / 2) {
            speed = (float) (Function.normalizeAngle((float) (newZ - Math.PI)) / Math.PI * 180);
        } else {
            speed = (float) (newZ / Math.PI * 180);
        }
        return speed;
    }

}