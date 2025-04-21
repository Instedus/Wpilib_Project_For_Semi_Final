/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018-2019 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import frc.robot.subsystems.JavaCamera;
import frc.robot.subsystems.Training;
import frc.robot.Autonomus.InitLogicAuto;
import frc.robot.commands.StateMachine;
import frc.robot.logic.InitLogic;


public class RobotContainer {

  /**
   * Create the subsystems and gamepad objects
   */
  public static Training train;
  public static JavaCamera server;
  public static InitLogic initLogic;
  public static InitLogicAuto initLogicAuto;

  public RobotContainer()
  {
      //Create new instances
      initLogic = new InitLogic();
      initLogicAuto = new InitLogicAuto();
      train = new Training();
      server = new JavaCamera();     
      server.start();
      
      //Set the default command for the training subsytem
      train.setDefaultCommand(new StateMachine());
  }
}
