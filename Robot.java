// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonFX;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
/**
 * The VM is configured to automatically run this class, and to call the functions corresponding to
 * each mode, as described in the TimedRobot documentation. If you change the name of this class or
 * the package after creating this project, you must also update the build.gradle file in the
 * project.
 */
public class Robot extends TimedRobot {
  private static final String kDefaultAuto = "Default";
  private static final String kCustomAuto = "My Auto";
  private String m_autoSelected;
  private final SendableChooser<String> m_chooser = new SendableChooser<>();
// drive encoders
  public static CANSparkMax sparkFrontLeft;
  public static CANSparkMax sparkFrontRight;
  public static CANSparkMax sparkBottomLeft;
  public static CANSparkMax sparkBottomRight;
  public static WPI_TalonFX talon;

  private Joystick driverController;
  private XboxController operatorController;

  /**
   * This function is run when the robot is first started up and should be used for any
   * initialization code.
   * 
   * How does it work? Voodoo magic!
   */
  @Override
  public void robotInit() {
    // motor initializations
    sparkFrontLeft = new CANSparkMax(4, MotorType.kBrushed);
    sparkFrontRight = new CANSparkMax(1, MotorType.kBrushed);
    sparkBottomLeft = new CANSparkMax(3, MotorType.kBrushed);
    sparkBottomRight = new CANSparkMax(2, MotorType.kBrushed);

    // back follows front
    sparkBottomLeft.follow(sparkFrontLeft);
    sparkBottomRight.follow(sparkFrontRight);

    sparkFrontLeft.setInverted(true);
    sparkFrontRight.setInverted(false); // check to see whether this is right or left
    
    talon = new WPI_TalonFX(15);

    driverController = new Joystick(0);

    operatorController = new XboxController(1);

    m_chooser.setDefaultOption("Default Auto", kDefaultAuto);
    m_chooser.addOption("My Auto", kCustomAuto);
    SmartDashboard.putData("Auto choices", m_chooser);
  }

  /**
   * This function is called every 20 ms, no matter the mode. Use this for items like diagnostics
   * that you want ran during disabled, autonomous, teleoperated and test.
   *
   * <p>This runs after the mode specific periodic functions, but before LiveWindow and
   * SmartDashboard integrated updating.
   */
  @Override
  public void robotPeriodic() {}

  Timer autoTimer = new Timer();
  /**
   * This autonomous (along with the chooser code above) shows how to select between different
   * autonomous modes using the dashboard. The sendable chooser code works with the Java
   * SmartDashboard. If you prefer the LabVIEW Dashboard, remove all of the chooser code and
   * uncomment the getString line to get the auto name from the text box below the Gyro
   *
   * <p>You can add additional auto modes by adding additional comparisons to the switch structure
   * below with additional strings. If using the SendableChooser make sure to add them to the
   * chooser code above as well.
   */
  @Override
  public void autonomousInit() {
    m_autoSelected = m_chooser.getSelected();
    // m_autoSelected = SmartDashboard.getString("Auto Selector", kDefaultAuto);
    System.out.println("Auto selected: " + m_autoSelected);

    autoTimer.reset();
    autoTimer.start();
  }

  /** This function is called periodically during autonomous. */
  @Override
  public void autonomousPeriodic() {
    switch (m_autoSelected) {
      case kCustomAuto:
        // Put custom auto code here
        break;
      case kDefaultAuto:
      default:
        if (autoTimer.get() < 2.0) {
          sparkFrontLeft.set(0.35);
          sparkFrontRight.set(0.35);
        } else {
          sparkFrontLeft.stopMotor();
          sparkFrontRight.stopMotor();
        }
        // Put default auto code here
        break;
    }
  }

  /** This function is called once when teleop is enabled. */
  @Override
  public void teleopInit() {}

  /** This function is called periodically during operator control. */
  @Override
  public void teleopPeriodic() {
    double forward = -driverController.getRawAxis(1);

    double turn = driverController.getRawAxis(2);

    sparkFrontLeft.set((forward+turn));
    sparkFrontRight.set(forward-turn);
    System.out.println(forward);

    if(operatorController.getAButton()) {
     
    }
  }

  /** This function is called once when the robot is disabled. */
  @Override
  public void disabledInit() {}

  /** This function is called periodically when disabled. */
  @Override
  public void disabledPeriodic() {}

  /** This function is called once when test mode is enabled. */
  @Override
  public void testInit() {}

  /** This function is called periodically during test mode. */
  @Override
  public void testPeriodic() {}

  /** This function is called once when the robot is first started up. */
  @Override
  public void simulationInit() {}

  /** This function is called periodically whilst in simulation. */
  @Override
  public void simulationPeriodic() {}
}
