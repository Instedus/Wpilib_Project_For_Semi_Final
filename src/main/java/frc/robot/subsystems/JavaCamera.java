package frc.robot.subsystems;

import org.opencv.core.Core;
import org.opencv.core.Mat;

import edu.wpi.cscore.CvSink;
import edu.wpi.cscore.CvSource;
import edu.wpi.cscore.UsbCamera;
import edu.wpi.first.cameraserver.CameraServer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.subsystems.vision.Fructs;

public class JavaCamera {

    public static Fructs fructs = new Fructs();

    private UsbCamera camera;
    private CvSink cvSink;
    public CvSource redFruct;
    public CvSource yellowFruct;
    public CvSource greenFruct;
    public CvSource purpleFruct;

    public CvSource hsvFruit;
    public CvSource maskColor;
    public CvSource contourFruct;

    public int nowTask = 0;
    public int nowResult = 0; // 1 - green | 2 - yellow | 3 - red | 10 - big apple | 11 - small apple | 12 - pear
    public int nowIteration = 0;

    public float[] newResult = {0 , 0};

    static {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
    }

    public JavaCamera() {
        camera = CameraServer.getInstance().startAutomaticCapture();
        camera.setResolution(640, 480);
        camera.setFPS(60);
        camera.setBrightness(25);

        cvSink = CameraServer.getInstance().getVideo();
        redFruct = CameraServer.getInstance().putVideo("redFruct", 640, 480);
        yellowFruct = CameraServer.getInstance().putVideo("yellowFruct", 640, 480);
        greenFruct = CameraServer.getInstance().putVideo("greenFruct", 640, 480);
        purpleFruct = CameraServer.getInstance().putVideo("purpleFruct", 640, 480);

        hsvFruit = CameraServer.getInstance().putVideo("hsvFruit", 640, 480);
        maskColor = CameraServer.getInstance().putVideo("maskColor", 640, 480);
        contourFruct = CameraServer.getInstance().putVideo("contourFruct", 640, 480);
    }

    public void start() {
        new Thread(() -> {
            Mat source = new Mat();
            while (true) {
                SmartDashboard.putNumber("ResultCamera", nowResult);
                SmartDashboard.putNumber("nowTask", nowTask);     
                try {
                    if (cvSink.grabFrame(source) == 0) {
                        continue;
                    }

                    if (source.cols() < 640 || source.rows() < 480) {
                        continue;
                    }

                    else if(nowTask == 0){
                        nowResult = 0;
                        continue;
                    }
                    
                    else if(nowTask == 1){
                        nowResult = fructs.detectFruit(source);
                    }                   
                    else if(nowTask == 2){
                        nowResult = fructs.countFruits(source);
                    }
                    else if(nowTask == 3){
                        nowResult = fructs.detectPurple(source);
                    }
                    else if(nowTask == 4){
                        newResult = fructs.detectCentre(source);
                    }
                    else if(nowTask == 5){
                        nowResult = fructs.checkDistance(source);
                    }
                    else if(nowTask == 6){
                        nowResult = fructs.detectNumber(source);
                    }
                    else if(nowTask == 7){
                        nowResult = fructs.detectFruitTree(source);
                    }

                    Thread.sleep(5);
                } catch (Exception e) {
                    SmartDashboard.putBoolean("Error_Camera", true);
                }
            }
        }).start();
    }
}