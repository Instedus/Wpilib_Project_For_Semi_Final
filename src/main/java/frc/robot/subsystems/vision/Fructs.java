package frc.robot.subsystems.vision;

import java.util.ArrayList;
import java.util.List;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.imgproc.Moments;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.RobotContainer;

public class Fructs {

    static Scalar greenLow = new Scalar(26, 69, 49);   // ДНЕВНЫЕ МАСКИ
    static Scalar greenHigh = new Scalar(77, 149, 134);

    //// static Scalar yellowLow = new Scalar(10, 125, 103);
    //// static Scalar yellowHigh = new Scalar(55, 234, 245);

    //// static Scalar redLow = new Scalar(0, 175, 115);
    //// static Scalar redHigh =new Scalar(188, 255, 255);

    
    static Scalar yellowLow = new Scalar(0, 108, 140);
    static Scalar yellowHigh = new Scalar(43, 212, 245);


    // static Scalar redLow = new Scalar(0, 118, 160);
    // static Scalar redHigh =new Scalar(191, 196, 232);

    static Scalar redLow = new Scalar(123, 40, 26);
    static Scalar redHigh =new Scalar(236, 251, 143);
 
    static Scalar purpleLow = new Scalar(123, 70, 60);
    static Scalar purpleHigh = new Scalar(137, 177, 148);



    static Scalar greenLowTree = new Scalar(26, 140, 99);    // НОЧНЫЕ МАСКИ
    static Scalar greenHighTree = new Scalar(60, 237, 219);

    static Scalar yellowLowTree = new Scalar(16, 119, 82);
    static Scalar yellowHighTree = new Scalar(41, 214, 255);

    // static Scalar yellowLow = new Scalar(7, 80, 86);
    // static Scalar yellowHigh = new Scalar(44, 197, 208);

    static Scalar redLowTree = new Scalar(0, 160, 110);
    static Scalar redHighTree =new Scalar(188, 240, 255);

    static Scalar purpleLowTree = new Scalar(138, 118, 97);
    static Scalar purpleHighTree = new Scalar(169, 170, 149);



    float cx, cy; 
    Mat fruitcontour;
    Scalar finalScalarColorLow;
    Scalar finalScalarColorHigh;
    public String fruitSize;

    private int upperFruitBig = 10000;
    private int lowerFruitBig = 1200;

    public float[] detectCentre(Mat frame) {
        Mat greenFrame = frame.clone();
        Mat redFrame = frame.clone();
        Mat yellowFrame = frame.clone();

        float centerX = (float) frame.cols() / 2;
        float centerY = (float) frame.rows() / 2 + 20;

        float offsetX = 0;
        float offsetY = 0;

        if(detectPurple(frame) > 200 ){
            if (searchForColorGreen(greenFrame, greenLow, greenHigh))
            {
                offsetX = (float) cx - centerX;
                offsetY = (float) cy - centerY - 50;
            }
            else if (searchForColorYellow(yellowFrame, yellowLow, yellowHigh))
            {
                offsetX = (float) cx - centerX;
                offsetY = (float) (cy - centerY) - 60;
            }
            else if (searchForColorRed(redFrame, redLow, redHigh))
            {
                offsetX = (float) cx - centerX;
                offsetY = (float) cy - centerY - 50;
            }
            else 
            {
                offsetX = 0;
                offsetY = 0;
            }   
        }

        else if (searchForColorYellow(yellowFrame, yellowLow, yellowHigh))
        {
            offsetX = (float) cx - centerX;
            offsetY = (float) (cy - centerY);
        }
        else if (searchForColorRed(redFrame, redLow, redHigh))
        {
            offsetX = (float) cx - centerX;
            offsetY = (float) cy - centerY;
        }
        else 
        {
            offsetX = 0;
            offsetY = 0;
        }

        return new float[] { offsetX, offsetY };
    }

    public int detectNumber(Mat frame) {
        boolean find = false;

        Mat mainImage = frame.clone();

        String one = "";
        String two = "";
        String three = "";
        String four = "";
        String five = "";
        String six = "";
        String seven = "";
        String eight = "";
        String nine = "";

        String numbers[] = { one, two, three, four, five, six, seven, eight, nine };

        for (int i = 0; i < numbers.length; i++) {
            Mat imageOfNumbers = Imgcodecs.imread(numbers[i]);

            Mat processedImages[] = processing(mainImage, imageOfNumbers);

            List<MatOfPoint> contoursMain = new ArrayList<>();

            List<MatOfPoint> contoursNumbers = new ArrayList<>();

            Mat hierarchyMain = new Mat();
            Mat hierarchyNumbers = new Mat();

            Imgproc.findContours(processedImages[0], contoursMain, hierarchyMain, Imgproc.RETR_TREE,
                    Imgproc.CHAIN_APPROX_SIMPLE);
            Imgproc.findContours(processedImages[1], contoursNumbers, hierarchyNumbers, Imgproc.RETR_TREE,
                    Imgproc.CHAIN_APPROX_SIMPLE);

            Mat cnt1 = contoursMain.get(1);
            Mat cnt2 = contoursNumbers.get(1);

            double num = Imgproc.matchShapes(cnt1, cnt2, Imgproc.CONTOURS_MATCH_I2, 0.0);

            if (i != 4 && num < 0.1) {
                int a = i;
                a++;
                System.out.println(a);
                find = true;
                break;
            } else if (i == 4 && num < 0.3) {
                int a = i;
                a++;
                System.out.println(a);
                find = true;
                break;
            }
            return (int) num;
        }
        return (int) 0;
    }

    private static Mat[] processing(Mat src, Mat src2) {
        Mat gray = new Mat(src.rows(), src.cols(), src.type());
        Mat one_gray = new Mat(src2.rows(), src2.cols(), src2.type());

        Imgproc.cvtColor(src, gray, Imgproc.COLOR_BGR2GRAY);
        Imgproc.cvtColor(src2, one_gray, Imgproc.COLOR_BGR2GRAY);

        Mat binary = new Mat(src.rows(), src.cols(), src.type(), new Scalar(0));
        Mat binary2 = new Mat(src2.rows(), src2.cols(), src2.type(), new Scalar(0));

        Imgproc.threshold(gray, binary, 160, 255, Imgproc.THRESH_BINARY);
        Imgproc.threshold(one_gray, binary2, 160, 255, Imgproc.THRESH_BINARY);

        return new Mat[] { binary, binary2 };
    }

    public int detectFruit(Mat frame) { // 1-greenBigApple | 2-greenAppleSmall | 3-greenPear | 4 - yellowPear | 5 -
        // redAppleBig | 6 - redAppleSmall | 7 - purpleTrash
        // Mat greenFrame = frame.clone();
        Mat redFrame = frame.clone();
        Mat yellowFrame = frame.clone();
        Mat purpleFrame = frame.clone();

        int purple = detectPurple(purpleFrame);
        // boolean green = searchForColorGreen(greenFrame, greenLow, greenHigh);
        boolean red = searchForColorRed(redFrame, redLow, redHigh);
        boolean yellow = searchForColorYellow(yellowFrame, yellowLow, yellowHigh);

        // if (green) {
        //     if (fruitSize.equals("big")) {
        //         boolean stem = detectStem(frame, finalScalarColorLow, finalScalarColorHigh, cx, cy);
        //         if (stem) {
        //             return 3;
        //         } else {
        //             return 1;
        //         }
        //     } else {
        //         return 2;
        //     }
        // }

        if (purple == 1) {
            return 7;
        }
        else if (yellow) {
            if(fruitSize.equals("big")){
                return 4;
            }
            else{
                return 6;
            }
        }
        else if (red) {
            // if (fruitSize.equals("big")) {
                return 5;
            // } else {
            //     return 6;
            // }
        }else {
            return 0;
        } 
    }

    public int detectFruitTree(Mat frame) { // 1-greenBigApple | 2-greenAppleSmall | 3-greenPear | 4 - yellowPear | 5 -
        // redAppleBig | 6 - redAppleSmall | 7 - purpleTrash
        Mat greenFrame = frame.clone();
        Mat redFrame = frame.clone();
        Mat yellowFrame = frame.clone();
        Mat purpleFrame = frame.clone();

        int purple = detectPurple(purpleFrame);
        boolean green = searchForColorGreen(greenFrame, greenLowTree, greenHighTree);
        boolean red = searchForColorRed(redFrame, redLowTree, redHighTree);
        boolean yellow = searchForColorYellow(yellowFrame, yellowLowTree, yellowHighTree);

        if (green) {
            if (fruitSize.equals("big")) {
                boolean stem = detectStem(frame, finalScalarColorLow, finalScalarColorHigh, cx, cy);
                if (stem) {
                    return 3;
                } else {
                    return 1;
                }
            } else {
                return 2;
            }
        } else if (red) {
            if (fruitSize.equals("big")) {
                return 5;
            } else {
                return 6;
            }
        } else if (yellow) {
            return 4;
        } else if (purple == 1) {
            return 7;
        } else {
            return 0;
        }

    }

    boolean detectStem(Mat frame, Scalar low, Scalar high, double cx, double cy) {
        int size = 5;
        int halfSize = size / 2;
        boolean allWhite = true;

        Mat hsv = new Mat();
        hsv = Viscad.preprocessImage(frame);
        frame.release();

        Mat mask = new Mat();
        Core.inRange(hsv, low, high, mask);
        hsv.release();

        for (int i = -halfSize; i <= halfSize; i++) {
            for (int j = -halfSize; j <= halfSize; j++) {
                int x = (int) cx + i;
                int y = (int) cy + j;
                if (x >= 0 && x < mask.cols() && y >= 0 && y < mask.rows()) {
                    double[] pixel = mask.get(y, x);
                    if (pixel[0] != 255) {
                        allWhite = false;
                        break;
                    }
                }
            }
            if (!allWhite)
                break;
        }
        return allWhite;
    }

    boolean searchForColorGreen(Mat frame, Scalar low, Scalar high) {
        Mat hsv = new Mat();
        Mat source = frame.clone();

        hsv = Viscad.preprocessImage(frame);
        frame.release();

        Mat mask = new Mat();
        Core.inRange(hsv, low, high, mask);
        hsv.release();

        Mat kernel = Imgproc.getStructuringElement(Imgproc.MORPH_ELLIPSE, new Size(15, 15));

        Imgproc.morphologyEx(mask, mask, Imgproc.MORPH_CLOSE, kernel);
        Imgproc.morphologyEx(mask, mask, Imgproc.MORPH_OPEN, kernel);
        RobotContainer.server.greenFruct.putFrame(mask);

        List<MatOfPoint> contours = new ArrayList<>();
        Mat hierarchy = new Mat();
        Imgproc.findContours(mask, contours, hierarchy, Imgproc.RETR_EXTERNAL, Imgproc.CHAIN_APPROX_SIMPLE);
        mask.release();
        SmartDashboard.putNumber("contoursSizeGreen", contours.size());

        if (!contours.isEmpty()) {
            float maxArea = 0;
            MatOfPoint largestContour = null;

            for (MatOfPoint contour : contours) {
                float area = (float) Imgproc.contourArea(contour);
                Rect boundingBox = Imgproc.boundingRect(contour);
                float aspectRatio = (float) boundingBox.width / boundingBox.height;

                if (area > maxArea && area > upperFruitBig && aspectRatio > 0.8 && aspectRatio < 1.2) {
                    fruitSize = "big";
                    maxArea = area;
                    SmartDashboard.putNumber("areaGreen", area);
                    largestContour = contour;
                    break;
                } else if (area > maxArea && area < upperFruitBig && area > lowerFruitBig && aspectRatio > 0.8
                        && aspectRatio < 1.2) {
                    fruitSize = "small";
                    maxArea = area;
                    SmartDashboard.putNumber("areaGreen", area);
                    largestContour = contour;
                    break;
                }
                break;
            }

            if (largestContour != null) {
                Moments moments = Imgproc.moments(largestContour);

                cx = (float) (moments.m10 / moments.m00);
                cy = (float) (moments.m01 / moments.m00);

                fruitcontour = largestContour;
                finalScalarColorLow = low;
                finalScalarColorHigh = high;
                return true;
            } else {
                return false;
            }
        }

        return false;
    }

    boolean searchForColorRed(Mat frame, Scalar low, Scalar high) {
        Mat hsv = new Mat();
        Mat source = frame.clone();

        Mat mask = new Mat();

        hsv = Viscad.preprocessImage(frame);

        RobotContainer.server.hsvFruit.putFrame(hsv);

        frame.release();

        Core.inRange(hsv, low, high, mask);

        RobotContainer.server.maskColor.putFrame(mask);

        hsv.release();

        Mat kernel = Imgproc.getStructuringElement(Imgproc.MORPH_ELLIPSE, new Size(15, 15));

        Imgproc.morphologyEx(mask, mask, Imgproc.MORPH_CLOSE, kernel);
        Imgproc.morphologyEx(mask, mask, Imgproc.MORPH_OPEN, kernel);


        List<MatOfPoint> contours = new ArrayList<>();
        Mat hierarchy = new Mat();
        Imgproc.findContours(mask, contours, hierarchy, Imgproc.RETR_EXTERNAL, Imgproc.CHAIN_APPROX_SIMPLE);
        mask.release();
        SmartDashboard.putNumber("contoursSizeRed", contours.size());

        Mat contor = new Mat();

        if (!contours.isEmpty()) {
            float maxArea = 0;
            MatOfPoint largestContour = null;

            for (MatOfPoint contour : contours) {
                float area = (float) Imgproc.contourArea(contour);
                Rect boundingBox = Imgproc.boundingRect(contour);
                float aspectRatio = (float) boundingBox.width / boundingBox.height;

                if (area > maxArea && area > upperFruitBig && aspectRatio > 0.8 && aspectRatio < 1.2) {
                    fruitSize = "big";
                    maxArea = area;
                    SmartDashboard.putNumber("areaRed", area);
                    largestContour = contour;
                    break;
                } else if (area > maxArea && area < upperFruitBig && area > lowerFruitBig && aspectRatio > 0.8
                        && aspectRatio < 1.2) {
                    fruitSize = "small";
                    maxArea = area;
                    SmartDashboard.putNumber("areaRed", area);
                    largestContour = contour;
                    break;
                }
                break;
            }

            if (largestContour != null) {

                // Imgproc.drawContours(contor, ,-1, new Scalar(0,0,255));

                // RobotContainer.server.redFruct.putFrame(contor);

                Moments moments = Imgproc.moments(largestContour);

                cx = (float) (moments.m10 / moments.m00);
                cy = (float) (moments.m01 / moments.m00);

                fruitcontour = largestContour;
                finalScalarColorLow = low;
                finalScalarColorHigh = high;
                return true;
            } else {
                return false;
            }
        }

        return false;
    }

    boolean searchForColorYellow(Mat frame, Scalar low, Scalar high) {
        Mat hsv = new Mat();
        Mat source = frame.clone();

        Mat mask = new Mat();

        hsv = Viscad.preprocessImage(frame);
        frame.release();

        Core.inRange(hsv, low, high, mask);
        hsv.release();

        Mat kernel = Imgproc.getStructuringElement(Imgproc.MORPH_ELLIPSE, new Size(15, 15));

        Imgproc.morphologyEx(mask, mask, Imgproc.MORPH_CLOSE, kernel);
        Imgproc.morphologyEx(mask, mask, Imgproc.MORPH_OPEN, kernel);
        RobotContainer.server.yellowFruct.putFrame(mask);

        List<MatOfPoint> contours = new ArrayList<>();
        Mat hierarchy = new Mat();
        Imgproc.findContours(mask, contours, hierarchy, Imgproc.RETR_EXTERNAL, Imgproc.CHAIN_APPROX_SIMPLE);
        mask.release();
        SmartDashboard.putNumber("contoursSizeYellow", contours.size());

        if (!contours.isEmpty()) {
            float maxArea = 0;
            MatOfPoint largestContour = null;

            for (MatOfPoint contour : contours) {
                float area = (float) Imgproc.contourArea(contour);
                Rect boundingBox = Imgproc.boundingRect(contour);
                float aspectRatio = (float) boundingBox.width / boundingBox.height;

                if (area > maxArea && area > upperFruitBig && aspectRatio > 0.8 && aspectRatio < 1.2) {
                    fruitSize = "big";
                    maxArea = area;
                    SmartDashboard.putNumber("areaYellow", area);
                    largestContour = contour;
                    break;
                } else if (area > maxArea && area < upperFruitBig && area > lowerFruitBig && aspectRatio > 0.8
                        && aspectRatio < 1.2) {
                    fruitSize = "small";
                    maxArea = area;
                    SmartDashboard.putNumber("areaYellow", area);
                    largestContour = contour;
                    break;
                }
                break;
            }

            if (largestContour != null) {
                Moments moments = Imgproc.moments(largestContour);

                cx = (float) (moments.m10 / moments.m00);
                cy = (float) (moments.m01 / moments.m00);

                fruitcontour = largestContour;
                finalScalarColorLow = low;
                finalScalarColorHigh = high;
                return true;
            } else {
                return false;
            }
        }

        return false;
    }

    public int countFruits(Mat source) {
        Mat hsv = Viscad.preprocessImage(source);

        Mat maskRed = new Mat();
        Mat maskGreen = new Mat();
        Mat maskYellow = new Mat();

        Core.inRange(hsv, redLow, redHigh, maskRed);
        Core.inRange(hsv, greenLow, greenHigh, maskGreen);
        Core.inRange(hsv, yellowLow, yellowHigh, maskYellow);

        Mat combine = new Mat();

        Core.bitwise_or(maskRed, maskGreen, combine);
        Core.bitwise_or(combine, maskYellow, combine);

        Mat kernel = Imgproc.getStructuringElement(Imgproc.MORPH_ELLIPSE, new Size(15, 15));

        Imgproc.morphologyEx(combine, combine, Imgproc.MORPH_CLOSE, kernel);
        Imgproc.morphologyEx(combine, combine, Imgproc.MORPH_OPEN, kernel);

        List<MatOfPoint> contours = new ArrayList<>();
        Mat hierarchy = new Mat();
        Imgproc.findContours(combine, contours, hierarchy, Imgproc.RETR_EXTERNAL, Imgproc.CHAIN_APPROX_SIMPLE);

        int fruitContour = 0;

        for (MatOfPoint contour : contours) {
            double area = Imgproc.contourArea(contour);
            if (area > 1700) {
                fruitContour++;
            }
        }
        return fruitContour;
    }

    public int detectPurple(Mat source) {
        Mat hsv = new Mat();

        Imgproc.cvtColor(source, hsv, Imgproc.COLOR_BGR2HSV);
        source.release();

        Mat mask = new Mat();
        Core.inRange(hsv, purpleLow, purpleHigh, mask);
        hsv.release();

        int count = Viscad.ImageTrueArea(mask);
        mask.release();
        SmartDashboard.putNumber("Core.countNonZero", count);

        return (count > 250) ? 1 : 0;
    }

    public int checkDistance(Mat frame) {
        Mat hsv = Viscad.preprocessImage(frame);

        float knownWidth = 10.0f; // см 

        float focalLength = 430f; // пиксели

        int finalAns = 0;

        Mat maskRed = new Mat();
        Mat maskGreen = new Mat();
        Mat maskYellow = new Mat();

        Core.inRange(hsv, redLow, redHigh, maskRed);
        Core.inRange(hsv, greenLow, greenHigh, maskGreen);
        Core.inRange(hsv, yellowLow, yellowHigh, maskYellow);

        Mat combine = new Mat();

        Core.bitwise_or(maskRed, maskGreen, combine);
        Core.bitwise_or(combine, maskYellow, combine);

        Mat kernel = Imgproc.getStructuringElement(Imgproc.MORPH_ELLIPSE, new Size(15, 15));
        Imgproc.morphologyEx(combine, combine, Imgproc.MORPH_CLOSE, kernel);
        Imgproc.morphologyEx(combine, combine, Imgproc.MORPH_OPEN, kernel);

        List<MatOfPoint> contours = new ArrayList<>();
        Mat hierarchy = new Mat();
        Imgproc.findContours(combine, contours, hierarchy, Imgproc.RETR_EXTERNAL, Imgproc.CHAIN_APPROX_SIMPLE);

        if (!contours.isEmpty()) {
            // Находим самый большой контур
            MatOfPoint largestContour = null;
            float largestArea = 0;

            float maxArea = 0;

            for (MatOfPoint contour : contours) {
                float area = (float) Imgproc.contourArea(contour);
                SmartDashboard.putNumber("area", area);
                Rect boundingBox = Imgproc.boundingRect(contour);
                float aspectRatio = (float) boundingBox.width / boundingBox.height;

                if (area > maxArea && area > lowerFruitBig && aspectRatio > 0.8
                        && aspectRatio < 1.2) {                
                    maxArea = area;
                    largestContour = contour;
                    break;
                }

                break;
            }

            try {
                // Рисуем контур вокруг самого большого объекта
                // Imgproc.drawContours(frame, contours, contours.indexOf(largestContour), new
                // Scalar(0, 255, 0), 2);

                // Определяем ограничивающий прямоугольник
                Rect boundingRect = Imgproc.boundingRect(largestContour);
                Imgproc.rectangle(frame, boundingRect.tl(), boundingRect.br(), new Scalar(255, 0, 0), 2);

                // Расчет расстояния
                float perceivedWidth = boundingRect.width; // Ширина объекта в пикселях
                float distance = (knownWidth * focalLength) / perceivedWidth;
                SmartDashboard.putNumber("distanceToObject", distance - 1);
                finalAns = (int) distance;

            } catch (Exception e) {
                SmartDashboard.putNumber("distanceToObject", 1);
                finalAns = 0;
            }
        }
        return finalAns;
    }
}