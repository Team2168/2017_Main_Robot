package org.team2168.subsystems;


import org.team2168.Robot;
import org.team2168.RobotMap;
import org.team2168.PID.controllers.PIDSpeed;
import org.team2168.PID.sensors.AverageEncoder;
import org.team2168.commands.shooter.DriveShooterWithJoystick;
import org.team2168.utils.TCPSocketSender;
import org.team2168.utils.consoleprinter.ConsolePrinter;

import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.command.Subsystem;

/**
 * Shooter Subsystem 
 * @author Krystina
 */
public class ShooterWheel extends Subsystem {
	private static ShooterWheel instance = null;
	
	private static Talon shooterLeft;
	private static Talon shooterRight;
	private static AverageEncoder shooterEncoder;

	public PIDSpeed shooterSpeedController;
	TCPSocketSender TCPShooterController;
	
	private volatile double rightMotorVoltage = 0.0;
	private volatile double leftMotorVoltage = 0.0;
		
	/**
	 * Private singleton constructor for the Shooter subsystem
	 */
	private ShooterWheel () {
		shooterLeft = new Talon (RobotMap.SHOOTER_WHEEL_LEFT);
		shooterLeft.setExpiration(0.1);
		shooterLeft.setSafetyEnabled(true);
		
		shooterRight = new Talon (RobotMap.SHOOTER_WHEEL_RIGHT);
		shooterRight.setExpiration(0.1);
		shooterRight.setSafetyEnabled(true);	
		
		if (Robot.isPracticeRobot())
		{	
		shooterEncoder = new AverageEncoder(RobotMap.SHOOTER_ENCODER_A_PBOT, 
				   							   RobotMap.SHOOTER_ENCODER_B_PBOT, //uncomment for encoder
				   							   RobotMap.SHOOTER_ENCODER_PULSE_PER_ROT,
				   							   RobotMap.SHOOTER_ENCODER_DIST_PER_TICK,
				   							   RobotMap.SHOOTER_ENCODER_REVERSE,
				   							   RobotMap.SHOOTER_ENCODING_TYPE, //uncomment for encoder
				   							   RobotMap.SHOOTER_SPEED_RETURN_TYPE,
				   							   RobotMap.SHOOTER_POS_RETURN_TYPE,
				   							   RobotMap.SHOOTER_AVG_ENCODER_VAL);
		}
		else
		{
			shooterEncoder = new AverageEncoder(RobotMap.SHOOTER_ENCODER_A, 
					   RobotMap.SHOOTER_ENCODER_B, //uncomment for encoder
					   RobotMap.SHOOTER_ENCODER_PULSE_PER_ROT,
					   RobotMap.SHOOTER_ENCODER_DIST_PER_TICK,
					   RobotMap.SHOOTER_ENCODER_REVERSE,
					   RobotMap.SHOOTER_ENCODING_TYPE, //uncomment for encoder
					   RobotMap.SHOOTER_SPEED_RETURN_TYPE,
					   RobotMap.SHOOTER_POS_RETURN_TYPE,
					   RobotMap.SHOOTER_AVG_ENCODER_VAL);
		}	
		
		
		shooterEncoder.setMinRate(RobotMap.SHOOTER_ENCODER_MIN_RATE);
	
		//Spawn new PID Controller
		shooterSpeedController = new PIDSpeed(
				"ShooterSpeedController",
				RobotMap.SHOOTER_SPEED_P,
				RobotMap.SHOOTER_SPEED_I,
				RobotMap.SHOOTER_SPEED_D,
				RobotMap.SHOOTER_SPEED_N,
				shooterEncoder,
				RobotMap.DRIVE_TRAIN_PID_PERIOD);
		
		shooterSpeedController.setSIZE(RobotMap.DRIVE_TRAIN_PID_ARRAY_SIZE);

		//start controller threads
		shooterSpeedController.startThread();
		
		
		TCPShooterController = new TCPSocketSender(RobotMap.TCP_SERVER_SHOOTER_SPEED, shooterSpeedController);
		TCPShooterController.start();
		
       	ConsolePrinter.putNumber("Shooter_rpm", () -> {return Robot.shooterWheel.getSpeed();}, true, true);
        ConsolePrinter.putBoolean("Shooter_atspeed_status", () -> {return Robot.shooterWheel.shooterSpeedController.isFinished();}, true, true);
        ConsolePrinter.putNumber("Shooter Position", () -> {return Robot.shooterWheel.getPosition();}, true, true);
        ConsolePrinter.putNumber("ShooterMotorLeftCurrent", () -> {return Robot.pdp.getChannelCurrent(RobotMap.SHOOTER_MOTOR_LEFT_PDP);}, true, true);
		ConsolePrinter.putNumber("ShooterMotorRightCurrent", () -> {return Robot.pdp.getChannelCurrent(RobotMap.SHOOTER_MOTOR_RIGHT_PDP);}, true, true);
		ConsolePrinter.putBoolean("ShooterMotorLeftCurrentTrip", () -> {return !Robot.pdp.isShooterMotorLeftTrip();}, true, false);
		ConsolePrinter.putBoolean("ShooterMotorRightCurrentTrip", () -> {return !Robot.pdp.isShooterMotorRightTrip();}, true, false);
		ConsolePrinter.putNumber("ShooterRightMotorVoltage",() -> {return Robot.shooterWheel.getRightMotorVoltage();}, true, true);
		ConsolePrinter.putNumber("ShooterLeftMotorVoltage",() -> {return Robot.shooterWheel.getLeftMotorVoltage();}, true, true);

	}
	
	/**
	 * singleton object for Shooter
	 * @return returns the shooter singleton object
	 * @author Krystina
	 */
	public static ShooterWheel getInstance() {
		if (instance == null)
			instance = new ShooterWheel();
		
		return instance;
	}
	
	/**
	 * Takes in a speed and drives the both motors at the same instance
	 * @param speed -1 to 1. positive shoots the ball out of the robot. negative moves the ball inward.
	 * @author Krystina
	 */
	public void setShooterSpeed(double speed) {
		driveLeftShooterWheel(speed);
		driveRightShooterWheel(speed);
	}
	
	/**
	 * Takes in a speed and drives the first shooter motor
	 * @param speed -1 to 1
	 * @author Krystina
	 */
	private void driveLeftShooterWheel(double speed) {
		if(RobotMap.REVERSE_SHOOTER_WHEEL_LEFT)
			speed = -speed;
		
		shooterLeft.set(speed);
		leftMotorVoltage = Robot.pdp.getBatteryVoltage() * speed;
	}
	
	/**
	 * Takes in a speed and drives the second shooter motor
	 * @param speed -1 to 1
	 * @author Krystina
	 */
	private void driveRightShooterWheel(double speed) {
		if(RobotMap.REVERSE_SHOOTER_WHEEL_RIGHT)
			speed = -speed;
			
		shooterRight.set(speed);
		rightMotorVoltage = Robot.pdp.getBatteryVoltage() * speed;
	}
	
	/**
	 * Gets the speed of the shooter wheel
	 * @return speed in RPM
	 */
	public double getSpeed() {
		return shooterEncoder.getRate();
	}
	
	
	/**
	 * zeros the position traveled by motors
	 */
	public void resetPosition() {
		shooterEncoder.reset();
	}
	/**
	 * Returns the last commanded voltage to the motor
	 * @return double in volts representing last commanded voltage to motor
	 */
	public double getRightMotorVoltage() {
		return rightMotorVoltage;
	}

	/**
	 * Returns the last commanded voltage to the motor
	 * @return double in volts representing last commanded voltage to motor
	 */
	public double getLeftMotorVoltage() {
		return leftMotorVoltage;
	}
	
	
    public void initDefaultCommand() {
        // Set the default command for a subsystem here.
    	setDefaultCommand(new DriveShooterWithJoystick());
    }

	public double getPosition() {
		// TODO Auto-generated method stub
		return shooterEncoder.getPos();
	}
}
