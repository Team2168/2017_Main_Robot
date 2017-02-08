package org.team2168.subsystems;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.SpeedController;
import edu.wpi.first.wpilibj.VictorSP;

import org.team2168.Robot;
import org.team2168.RobotMap;
import org.team2168.PID.controllers.PIDPosition;
import org.team2168.PID.controllers.PIDSpeed;
import org.team2168.PID.sensors.ADXRS453Gyro;
import org.team2168.PID.sensors.AverageEncoder;
import org.team2168.PID.sensors.BNOHeading;
import org.team2168.PID.sensors.IMU;
import org.team2168.PID.sensors.TCPCamSensor;
import org.team2168.commands.drivetrain.DriveWithJoystick;
import org.team2168.subsystems.Drivetrain;
import org.team2168.utils.BNO055;
import org.team2168.utils.consoleprinter.ConsolePrinter;

import edu.wpi.first.wpilibj.command.Subsystem;

/**
 * Subsystem class for the Drivetrain
 * @author Aidan Sullivan
 */
public class Drivetrain extends Subsystem {
	private SpeedController leftMotor1;
	private SpeedController leftMotor2;
	private SpeedController leftMotor3;
	private SpeedController rightMotor1;
	private SpeedController rightMotor2;
	private SpeedController rightMotor3;
	
	private static VictorSP LeftMotor1;
	private static VictorSP LeftMotor2;
	private static VictorSP RightMotor1;
	private static VictorSP RightMotor2;

	private static ADXRS453Gyro gyroSPI;	
	private static DoubleSolenoid gearChanger;
	private static AverageEncoder drivetrainRightEncoder;
	private static AverageEncoder drivetrainLeftEncoder;
	
	private static Drivetrain instance = null;

	private BNO055 gyro;
	private BNOHeading stupidPIDSensorGyro;
	public IMU imu;
	public TCPCamSensor tcpCamSensor;
	
	//declare position/speed controllers
	public PIDPosition driveTrainPosController;
	public PIDPosition rotateController;
	public PIDPosition rotateDriveStraightController;
	public PIDPosition rotateCameraController;	
	
	//declare speed controllers
	public PIDSpeed rightSpeedController;
	public PIDSpeed leftSpeedController;
	
	//For debugging only
	public static volatile double LeftMotor1Voltage = 0.0;
	public static volatile double LeftMotor2Voltage = 0.0;
	public static volatile double RightMotor1Voltage = 0.0;
	public static volatile double RightMotor2Voltage = 0.0;
	
	
	/**
	 * Default constructors for Drivetrain
	 */
	private Drivetrain() {
		
		LeftMotor1 = new VictorSP(RobotMap.LEFT_DRIVE_MOTOR_1);
		LeftMotor2 = new VictorSP(RobotMap.LEFT_DRIVE_MOTOR_2);
		RightMotor1 = new VictorSP(RobotMap.RIGHT_DRIVE_MOTOR_1);
		RightMotor2 = new VictorSP(RobotMap.RIGHT_DRIVE_MOTOR_2);
		
		gearChanger = new DoubleSolenoid(RobotMap.DRIVETRAIN_LOW_GEAR, RobotMap.DRIVETRAIN_HIGH_GEAR);
		
		drivetrainRightEncoder = new AverageEncoder(
				RobotMap.RIGHT_DRIVE_ENCODER_A,
				RobotMap.RIGHT_DRIVE_ENCODER_B,
				RobotMap.DRIVE_ENCODER_PULSE_PER_ROT,
				RobotMap.DRIVE_ENCODER_DIST_PER_TICK,
				RobotMap.RIGHT_DRIVE_TRAIN_ENCODER_REVERSE,
				RobotMap.DRIVE_ENCODING_TYPE,
				RobotMap.DRIVE_SPEED_RETURN_TYPE,
				RobotMap.DRIVE_POS_RETURN_TYPE,
				RobotMap.DRIVE_AVG_ENCODER_VAL);

		drivetrainLeftEncoder = new AverageEncoder(
				RobotMap.LEFT_DRIVE_ENCODER_A, 
				RobotMap.LEFT_DRIVE_ENCODER_B,
				RobotMap.DRIVE_ENCODER_PULSE_PER_ROT,
				RobotMap.DRIVE_ENCODER_DIST_PER_TICK,
				RobotMap.LEFT_DRIVE_TRAIN_ENCODER_REVERSE,
				RobotMap.DRIVE_ENCODING_TYPE,
				RobotMap.DRIVE_SPEED_RETURN_TYPE,
				RobotMap.DRIVE_POS_RETURN_TYPE,
				RobotMap.DRIVE_AVG_ENCODER_VAL);
		
		//DriveStraight Controller
		rotateController = new PIDPosition(
				"RotationController",
				RobotMap.ROTATE_POSITION_P,
				RobotMap.ROTATE_POSITION_I,
				RobotMap.ROTATE_POSITION_D,
				gyroSPI,
				RobotMap.DRIVE_TRAIN_PID_PERIOD);
		
		rotateCameraController = new PIDPosition(
				"RotationCameraController",
				RobotMap.ROTATE_POSITION_P,
				RobotMap.ROTATE_POSITION_I,
				RobotMap.ROTATE_POSITION_D,
				tcpCamSensor,
				RobotMap.DRIVE_TRAIN_PID_PERIOD);
		
		rotateDriveStraightController = new PIDPosition(
				"RotationStraightController",
				RobotMap.ROTATE_POSITION_P_Drive_Straight,
				RobotMap.ROTATE_POSITION_I_Drive_Straight,
				RobotMap.ROTATE_POSITION_D_Drive_Straight,
				gyroSPI,
				RobotMap.DRIVE_TRAIN_PID_PERIOD);

		driveTrainPosController = new PIDPosition(
				"driveTrainPosController",
				RobotMap.DRIVE_TRAIN_RIGHT_POSITION_P,
				RobotMap.DRIVE_TRAIN_RIGHT_POSITION_I,
				RobotMap.DRIVE_TRAIN_RIGHT_POSITION_D,
				imu,
				RobotMap.DRIVE_TRAIN_PID_PERIOD);

		//Spawn new PID Controller
		rightSpeedController = new PIDSpeed(
				"RightSpeedController",
				RobotMap.DRIVE_TRAIN_RIGHT_SPEED_P,
				RobotMap.DRIVE_TRAIN_RIGHT_SPEED_I,
				RobotMap.DRIVE_TRAIN_RIGHT_SPEED_D, 1,
				drivetrainRightEncoder,
				RobotMap.DRIVE_TRAIN_PID_PERIOD);

		leftSpeedController = new PIDSpeed(
				"LeftSpeedController",
				RobotMap.DRIVE_TRAIN_LEFT_SPEED_P,
				RobotMap.DRIVE_TRAIN_LEFT_SPEED_I,
				RobotMap.DRIVE_TRAIN_LEFT_SPEED_D, 1,
				drivetrainLeftEncoder,
				RobotMap.DRIVE_TRAIN_PID_PERIOD);
		
		//Log sensor data
		ConsolePrinter.putNumber("Drivetrain Right Encoder", Drivetrain::getRightPosition, true, false);
		ConsolePrinter.putNumber("Drivetrain Left Encoder", Drivetrain::getLeftPosition, true, false);
	}
	
	
	
	/**
	 * Calls instance object and makes it a singleton object of type Drivetrain
	 * @returns Drivetrain object "instance"
	 */
	public static Drivetrain getInstance(){
		if(instance == null)
			instance = new Drivetrain();
		
		return instance;
	}
	
	
    
	/**
     * Calls left motor 1 and creates a local variable "speed"
     * Refers to boolean in Robot map and if true, speed = - speed
     * Uses set() command to assign the new speed to left motor 1
     * @param double speed between -1 and 1
     * negative is reverse, positive if forward, 0 is stationary
     */
    private void driveLeftMotor1(double speed) {
    	if(RobotMap.DT_REVERSE_LEFT)
        	speed = -speed;

    	LeftMotor1.set(speed);
    	LeftMotor1Voltage = Robot.pdp.getBatteryVoltage() * speed;

    }
    
    /**
     * Calls left motor 2 and creates a local variable "speed"
     * Refers to boolean in Robot map and if true, speed = - speed
     * Uses set() command to assign the new speed to left motor 2
     * @param double speed between -1 and 1
     * negative is reverse, positive if forward, 0 is stationary
     */
    private void driveLeftMotor2(double speed) {
    	if(RobotMap.DT_REVERSE_LEFT)
        	speed = -speed;

    	LeftMotor2.set(speed);
    	LeftMotor2Voltage = Robot.pdp.getBatteryVoltage() * speed;
    }
    
    /**
     * Take in double speed and sets it to left motors 1, 2, and 3
     * @param speed is a double between -1 and 1
     * negative is reverse, positive if forward, 0 is stationary
     */
    public void driveLeftSide(double speed) {
    	driveLeftMotor1(speed);
    	driveLeftMotor2(speed);
    }
    
    /**
     * Calls right motor 1 and creates a local variable "speed"
     * Refers to boolean in Robot map and if true, speed = - speed
     * Uses set() command to assign the new speed to right motor 1
     * @param double speed between -1 and 1
     * negative is reverse, positive if forward, 0 is stationary
     */
   private void driveRightMotor1(double speed) {
	   if(RobotMap.DT_REVERSE_RIGHT)
	    	speed = -speed;

    	RightMotor1.set(speed);
    	RightMotor1Voltage = Robot.pdp.getBatteryVoltage() *speed;
    }
    
   /**
    * Calls right motor 2 and creates a local variable "speed"
    * Refers to boolean in Robot map and if true, speed = - speed
    * Uses set() command to assign the new speed to right motor 2
    * @param double speed between -1 and 1
    * negative is reverse, positive if forward, 0 is stationary
    */
    private void driveRightMotor2(double speed) {
    	if(RobotMap.DT_REVERSE_RIGHT)
        	speed = -speed;
    	
    	RightMotor2.set(speed);
    	RightMotor2Voltage = Robot.pdp.getBatteryVoltage() * speed;
    }
    
    /**
     * Takes in a double speed and sets it to their right motors 1, 2, and 3
     * @param speed is a double  between -1 and 1
     * negative is reverse, positive if forward, 0 is stationary
     */
    public void driveRightSide(double speed) {
    	driveRightMotor1(speed);
    	driveRightMotor2(speed);
    }
    
    /**
     * Takes in speed for right and speed for left and sets them to their respective sides 
     * @param leftSpeed is a double between -1 and 1 
     * @param rightSpeed is a double between -1 and 1
     * negative is reverse, positive if forward, 0 is stationary
     */
    public void tankDrive(double leftSpeed, double rightSpeed) {
    	driveLeftSide(leftSpeed);
    	driveRightSide(rightSpeed);
    }

    /**
     * Calls for default command of the drivetrain to be DriveWithJoystick
     */
    public void initDefaultCommand() {
        setDefaultCommand(new DriveWithJoystick());
    }
    
    /**
     * returns total distance traveled by right side of drivetrain
     * @return double in feet of total distance traveled by right encoder
     */
    public static double getRightPosition(){
    	return drivetrainRightEncoder.getPos();
    }
    
    /**
     * returns total distance traveled by left side of drivetrain
     * @return double in feet of total distance traveled by left encoder
     */
    public static double getLeftPosition(){
    	return drivetrainLeftEncoder.getPos();
    }
    
    /**
     * returns total distance traveled by drivetrain
     * @return double in inches of average distance traveled by both encoders
     */
    public static double getAverageDistance(){
    	return (getRightPosition() + getLeftPosition())/2.0;
    }
    
    /**
     * resets position of right encoder to 0 inches
     */
    public static void resetRightPosition(){
    	drivetrainRightEncoder.reset();
    }
    /**
     * resets position of left encoder to 0 inches
     */
    public static void resetLeftPosition(){
    	drivetrainLeftEncoder.reset();
    }
    
    /**
     * resets position of both Encoders to 0 inches
     */
    public static void resetPosition(){
    	resetLeftPosition();
    	resetRightPosition();
    }
    
    /**
     * returns heading of robot
     * @return double between 0 degrees and 360 degrees
     */
    public static double getHeading(){
    	return gyroSPI.getPos();		
    }
    
    /**
     * Resets heading of IMU to 0 degrees
     */
    public static void resetHeading(){
    	gyroSPI.reset();
    }
    
    /**
     * calls to calibrate gyro
     * Only used when robot is stationary
     */
    public static void calibrateGyro(){
    	gyroSPI.calibrate();
    }
    
    /**
     * Terminates active gyro calibration sequence
     */
    public static void stopGyroCalibrating(){
    	gyroSPI.stopCalibrating();
    }
    
    /**
     * Returns true if Gyro has calibrated
     */
    public static boolean isGyroCalibrated(){
    	return gyroSPI.hasCompletedCalibration();
    }
    
    /**
     * Returns the last commanded voltage of Left Motor 1
     * @return Double in volts between 0 and 12
     */
    public double getLeftMotor1Voltage(){
    	return LeftMotor1Voltage;
    }
    
    /**
     * Returns the last commanded voltage of Left Motor 2
     * @return Double in volts between 0 and 12
     */
    public double getLeftMotor2Voltage(){
    	return LeftMotor2Voltage;
    }
    
    /**
     * Returns the last commanded voltage of Right Motor 1
     * @return Double in volts between 0 and 12
     */
    public double getRightMotor1Voltage(){
    	return RightMotor1Voltage;
    } 
    
    /**
     * Returns the last commanded voltage of Right Motor 2
     * @return Double in volts between 0 and 12
     */
    public double getRightMotor2Voltage(){
    	return RightMotor2Voltage;
    } 
    
	/**
	 * Shifts the Drivetrain from High to Low Gear
	 */
    public void shiftGearsHighToLow(){
    	gearChanger.set(DoubleSolenoid.Value.kForward);
    }
    
	/**
	 * Returns true if last commanded shift was High Gear
	 */
    public boolean gearIsHigh()
    {
    	return gearChanger.get()==DoubleSolenoid.Value.kReverse;
    }
    
	/**
	 * Shifts the Drivetrain from Low to High Gear
	 */
    public void shiftGearsLowToHigh(){
    	gearChanger.set(DoubleSolenoid.Value.kReverse);
    }
    
	/**
	 * Returns true if last commanded shift was Low Gear
	 */
    public boolean gearIsLow()
    {
    	return gearChanger.get()==DoubleSolenoid.Value.kForward;
    }
    
    
    
}


