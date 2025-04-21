package frc.robot.subsystems;

public class PID {
    private float p, i, d;
    private float minSpeed = -100, maxSpeed = 100;

    private float kp, ki, kd;
    public float setPoint;

    private boolean reset = false;
    private float coff = 0.1080f;
    private float error = 0;

    private float output = 0;

    public PID(float kp, float ki, float kd) {
        this.kp = kp;
        this.ki = ki;
        this.kd = kd;
    }

    public float calculate(float input) {
        this.error = setPoint - (-input * this.coff);
        if (this.reset) {
            this.p = 0;
            this.i = 0;
            this.d = 0;

            this.reset = false;
        } else {
            this.p = error * kp;
            this.i += this.p * ki;
            this.d += -this.i * kd;
        }

        this.output = Function.inRange(this.p + this.i + this.d, this.minSpeed, this.maxSpeed);

        return this.output;
    }

    public void resetPID() {
        this.reset = true;

        this.p = 0;
        this.i = 0;
        this.d = 0;
    }

}