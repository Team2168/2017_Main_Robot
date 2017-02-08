package org.team2168.subsystems;

import org.team2168.Robot;
import org.team2168.RobotMap;
import org.team2168.PID.controllers.PIDSpeed;
import org.team2168.PID.sensors.AverageCounter;
import org.team2168.PID.sensors.AverageEncoder;
import org.team2168.commands.DriveShooterWithJoystick;
import org.team2168.commands.DriveShooterWithJoystick;
import org.team2168.utils.TCPSocketSender;
import org.team2168.utils.Util;

import edu.wpi.first.wpilibj.AnalogInput;
import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.command.Subsystem;

/**
 * Shooter Subsystem 
 * @author Krystina
 */
public class Shooter extends Subsystem {
	private Talon shooterLeft;
	private Talon shooterRight;
	private AverageEncoder shooterEncoder;
	
	
	static Shooter instance = null;
	
	//declare speed controllers
	public PIDSpeed shooterSpeedController;
	
	//declare TCP severs...ONLY FOR DEBUGGING PURPOSES, SHOULD BE REMOVED FOR COMPITITION
	TCPSocketSender TCPShooterController;
	
	//output voltage...ONLY FOR DEBUGGING PURPOSES, SHOULD BE REMOVED FOR COMPITITION
	private volatile double RightMotorVoltage;
	private volatile double LeftMotorVoltage;
		
	/**
	 * Private singleton constructor for the Shooter subsystem
	 */
	private Shooter () {
			shooterLeft = new Talon (RobotMap.SHOOTER_WHEEL_LEFT);
			shooterLeft.setExpiration(0.1);
			shooterLeft.setSafetyEnabled(true);
			
			shooterRight = new Talon (RobotMap.SHOOTER_WHEEL_RIGHT);
			shooterRight.setExpiration(0.1);
			shooterRight.setSafetyEnabled(true);	
		

		shooterEncoder = new AverageEncoder(RobotMap.SHOOTER_ENCODER_A, 
				   							   RobotMap.SHOOTER_ENCODER_B, //uncomment for encoder
				   							   RobotMap.SHOOTER_ENCODER_PULSE_PER_ROT,
				   							   RobotMap.SHOOTER_ENCODER_DIST_PER_TICK,
				   							   RobotMap.SHOOTER_ENCODER_REVERSE,
				   							   RobotMap.SHOOTER_ENCODING_TYPE, //uncomment for encoder
				   							   RobotMap.SHOOTER_SPEED_RETURN_TYPE,
				   							   RobotMap.SHOOTER_POS_RETURN_TYPE,
				   							   RobotMap.SHOOTER_AVG_ENCODER_VAL);
		
		shooterEncoder.setMinRate(RobotMap.SHOOTER_ENCODER_MIN_RATE);
		
		
		
		RightMotorVoltage = 0;
		LeftMotorVoltage = 0;
	}
	
	/**
	 * singleton object for Shooter
	 * @return returns the shooter singleton object
	 * @author Krystina
	 */
	public static Shooter getInstance() {
		if (instance == null)
			instance = new Shooter();
		
		return instance;
	}
	
	/**
	 * Takes in a speed and drives the both motors at the same instance
	 * @param speed -1 to 1 if given a positive value the ball will move outward. If negative the ball will inward.
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
		return RightMotorVoltage;
	}

	/**
	 * Returns the last commanded voltage to the motor
	 * @return double in volts representing last commanded voltage to motor
	 */
	public double getLeftMotorVoltage() {
		return LeftMotorVoltage;
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
