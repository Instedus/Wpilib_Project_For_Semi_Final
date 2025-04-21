package frc.robot.subsystems;


public class MeanFilter 
{
    private float[] arrayForFilter;
    private int filterPowerInit = 10;

    public MeanFilter()
    {
        this.arrayForFilter = new float[this.filterPowerInit];
    }

    public MeanFilter(int filterPower)
    {
        this.filterPowerInit = filterPower;
        this.arrayForFilter = new float[this.filterPowerInit];
    }

    public float Filter(float val)
    {
        for (int i = filterPowerInit - 1; i > 0; i--)
        {
            this.arrayForFilter[i] = this.arrayForFilter[i - 1];
        }
        this.arrayForFilter[0] = val;

        float sum = 0;
        for (int i = 0; i < this.arrayForFilter.length; ++i)
        {
            sum += this.arrayForFilter[i];
        }
        return sum / this.arrayForFilter.length;
    }

}