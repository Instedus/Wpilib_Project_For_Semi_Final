package frc.robot.subsystems;

import edu.wpi.first.wpilibj.Timer;

/**
 * Данный класс содержит методы для работы со значениями, диапазонами(массивы
 * данных)
 */
public class Function {

    private static float[] smoothArrayAxis = new float[2];

    /**
     * Метод предназначен для вычисления значений, находящихся в заданными
     * диапазонами входных данных и соответствующими выходными значениями
     */
    public static float TransF(float[][] values, float value) {
        float result = 0;
        float max_o;
        float max_i;
        float min_o;
        float min_i;

        boolean minus = false;

        if (value < 0) {
            minus = true;
            value = Math.abs(value);
        }

        if (value >= values[0][values[0].length - 1]) {
            result = values[1][values[1].length - 1];
        } else {
            for (int i = 0; i < values[0].length; i++) {
                if (value >= values[0][i] && value <= values[0][i + 1]) {
                    min_i = values[0][i + 1];
                    max_i = values[0][i];
                    min_o = values[1][i + 1];
                    max_o = values[1][i];

                    if (value == values[0][i]) {
                        result = values[1][i];
                        break;
                    } else {
                        result = min_o
                                + (((max_o - min_o) * ((Math.abs(value) - min_i) * 100 / (max_i - min_i))) / 100);

                    }
                }
            }
        }

        if (minus) {
            result *= -1;
        }
        return result;
    }

    /**
     * Метод позволяет выполнить преобразование комплексного числа из декартовой
     * системы координат в полярную,предоставляя его радиус и угол.
     */

    public static float[] DecartToPolar(float x, float y) {
        float[] arr = new float[2];
        arr[0] = (float) Math.sqrt((x * x + y * y)); // вычисление радиуса комплексного числа
        arr[1] = (float) Math.atan2(y, x); // угол полярных координат комплексного числа
        return arr;
    }

    public static float[] PolarToDecart(float r, float theta) {
        float[] arr = new float[2];

        arr[0] = r * (float) Math.cos(theta); // x
        arr[1] = r * (float) Math.sin(theta); // y

        return arr;
    }

    /**
     * Метод позволяет выполнить ограничения входных данных. Вывод ограниченных
     * данных
     */

    public static float inRange(float in, float min, float max) {
        return in < min ? min : in > max ? max : in;
    }

    /**
     * Метод позволяет выполнить ограничения входных данных. Вывод проверки
     * нахождения входных данных
     */

    public static boolean inRangeBool(float in, float min, float max) {
        return in >= min && in <= max;
    }

    public static int axis(float x, float y, float curX, float curY)
    { 
        float xDist = 0; 
        if (Math.abs(x - curX) != 0)
        { 
            xDist = Math.abs(x - curX); 
        } 
        float yDist = Math.abs(y - curY); 
 
        if (Function.inRangeBool(yDist / xDist, 0.75f, 1.38f) || (xDist < 150 && yDist < 150))
        { 
            return 0; 
        }
        else
        { 
            if (yDist / xDist > 1)
            { 
                return 1; 
            }
            else
            { 
                return 2; 
            }

        }
    }

    // метод, который ограничивает угол в odometry
    public static float normalizeAngle(float angle) {
        while (angle > Math.PI)
            angle -= 2 * Math.PI;
        while (angle < -Math.PI)
            angle += 2 * Math.PI;
        return angle;
    }

    public static float[] changeAxises(float x, float z) {
        smoothArrayAxis[0] += inRange(x - smoothArrayAxis[0], -2.4f, 2.4f);
        smoothArrayAxis[1] += inRange(z - smoothArrayAxis[1], -2.4f, 2.4f);
        return smoothArrayAxis;
    }

    public static float countTimer() {
        return (float) (Timer.getFPGATimestamp() / Timer.getFPGATimestamp()) / 50;
    }
}