package org.team2168.subsystems;

import edu.wpi.first.wpilibj.AnalogInput;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.VictorSP;

import org.team2168.Robot;
import org.team2168.RobotMap;
import org.team2168.PID.controllers.PIDPosition;
import org.team2168.PID.controllers.PIDSpeed;
import org.team2168.PID.sensors.ADXRS453Gyro;
import org.team2168.PID.sensors.AverageEncoder;
import org.team2168.PID.sensors.IMU;
import org.team2168.PID.sensors.TCPCamSensor;
import org.team2168.commands.drivetrain.DriveWithJoystick;
import org.team2168.subsystems.Drivetrain;
import org.team2168.utils.LinearInterpolator;
import org.team2168.utils.TCPSocketSender;
import org.team2168.utils.consoleprinter.ConsolePrinter;

import edu.wpi.first.wpilibj.command.Subsystem;

/**
 * Subsystem class for the Drivetrain
 * @author Aidan Sullivan
 */
public class Drivetrain extends Subsystem {
	
	private static VictorSP leftMotor1;
	private static VictorSP leftMotor2;
	private static VictorSP rightMotor1;
	private static VictorSP rightMotor2;

	private ADXRS453Gyro gyroSPI;
	private AverageEncoder drivetrainLeftEncoder;
	private AverageEncoder drivetrainRightEncoder;
	
	private static AnalogInput DrivetrainIRSensor;
	
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
	
	private static Drivetrain instance = null;
	
    private static LinearInterpolator gunStyleInterpolator;
    //These values represent the x axis on the gun style controller
    //TODO test whether values should be ascending or descending
    private double[][] gunStyleRange = {{-0.921,1.0},
    		                          {0.09,0.0},
    		                          {0.12,0.0},
    		                          {0.551,-1.0}};
	
	//declare TCP severs...ONLY FOR DEBUGGING PURPOSES, SHOULD BE REMOVED FOR COMPITITION
	TCPSocketSender TCPdrivePosController;
	TCPSocketSender TCPrightSpeedController;
	TCPSocketSender TCPleftSpeedController;
	TCPSocketSender TCProtateController;
	TCPSocketSender TCProtateCameraController;
	
	
	public volatile double leftMotor1Voltage;
	public volatile double leftMotor2Voltage;
	public volatile double rightMotor1Voltage;
	public volatile double rightMotor2Voltage;
	
	
	/**
	 * Default constructors for Drivetrain
	 */
	private Drivetrain() {
		leftMotor1 = new VictorSP(RobotMap.LEFT_DRIVE_MOTOR_1);
		leftMotor2 = new VictorSP(RobotMap.LEFT_DRIVE_MOTOR_2);
		rightMotor1 = new VictorSP(RobotMap.RIGHT_DRIVE_MOTOR_1);
		rightMotor2 = new VictorSP(RobotMap.RIGHT_DRIVE_MOTOR_2);
		
    	gunStyleInterpolator = new LinearInterpolator(gunStyleRange);
		
		if(Robot.isPracticeRobot())
		{
			drivetrainRightEncoder = new AverageEncoder(
					RobotMap.RIGHT_DRIVE_ENCODER_A_PBOT,
					RobotMap.RIGHT_DRIVE_ENCODER_B_PBOT,
					RobotMap.DRIVE_ENCODER_PULSE_PER_ROT,
					RobotMap.DRIVE_ENCODER_DIST_PER_TICK,
					RobotMap.RIGHT_DRIVE_TRAIN_ENCODER_REVERSE_PBOT,
					RobotMap.DRIVE_ENCODING_TYPE,
					RobotMap.DRIVE_SPEED_RETURN_TYPE,
					RobotMap.DRIVE_POS_RETURN_TYPE,
					RobotMap.DRIVE_AVG_ENCODER_VAL);

			drivetrainLeftEncoder = new AverageEncoder(
					RobotMap.LEFT_DRIVE_ENCODER_A_PBOT, 
					RobotMap.LEFT_DRIVE_ENCODER_B_PBOT,
					RobotMap.DRIVE_ENCODER_PULSE_PER_ROT,
					RobotMap.DRIVE_ENCODER_DIST_PER_TICK,
					RobotMap.LEFT_DRIVE_TRAIN_ENCODER_REVERSE_PBOT,
					RobotMap.DRIVE_ENCODING_TYPE,
					RobotMap.DRIVE_SPEED_RETURN_TYPE,
					RobotMap.DRIVE_POS_RETURN_TYPE,
					RobotMap.DRIVE_AVG_ENCODER_VAL);
		}
		else
		{
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
		}
		
		gyroSPI = new ADXRS453Gyro();
		gyroSPI.startThread();
		
		imu = new IMU(drivetrainLeftEncoder,drivetrainRightEncoder,RobotMap.WHEEL_BASE);

		tcpCamSensor = new TCPCamSensor("GearCam", RobotMap.GEAR_CAMERA_LISTEN_PORT, RobotMap.CAMERA_SENSOR_PERIOD);
		
		DrivetrainIRSensor = new AnalogInput(RobotMap.DRIVETRAIN_IR_SENSOR);
		
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
						"rightSpeedController",
						RobotMap.DRIVE_TRAIN_RIGHT_SPEED_P,
						RobotMap.DRIVE_TRAIN_RIGHT_SPEED_I,
						RobotMap.DRIVE_TRAIN_RIGHT_SPEED_D, 1,
						drivetrainRightEncoder,
						RobotMap.DRIVE_TRAIN_PID_PERIOD);

				leftSpeedController = new PIDSpeed(
						"leftSpeedController",
						RobotMap.DRIVE_TRAIN_LEFT_SPEED_P,
						RobotMap.DRIVE_TRAIN_LEFT_SPEED_I,
						RobotMap.DRIVE_TRAIN_LEFT_SPEED_D, 1,
						drivetrainLeftEncoder,
						RobotMap.DRIVE_TRAIN_PID_PERIOD);


				//add min and max output defaults and set array size
				rightSpeedController.setSIZE(RobotMap.DRIVE_TRAIN_PID_ARRAY_SIZE);
				leftSpeedController.setSIZE(RobotMap.DRIVE_TRAIN_PID_ARRAY_SIZE);
				driveTrainPosController.setSIZE(RobotMap.DRIVE_TRAIN_PID_ARRAY_SIZE);
				rotateController.setSIZE(RobotMap.DRIVE_TRAIN_PID_ARRAY_SIZE);
				rotateDriveStraightController.setSIZE(RobotMap.DRIVE_TRAIN_PID_ARRAY_SIZE);
				rotateCameraController.setSIZE(RobotMap.DRIVE_TRAIN_PID_ARRAY_SIZE);

				//start controller threads
				rightSpeedController.startThread();
				leftSpeedController.startThread();
				driveTrainPosController.startThread();
				rotateController.startThread();
				rotateDriveStraightController.startThread();
				rotateCameraController.startThread();

				
				
				
				
				//start TCP Servers for DEBUGING ONLY
				TCPdrivePosController = new TCPSocketSender(RobotMap.TCP_SERVER_DRIVE_TRAIN_POS, driveTrainPosController);
				TCPdrivePosController.start();

				TCPrightSpeedController = new TCPSocketSender(RobotMap.TCO_SERVER_RIGHT_DRIVE_TRAIN_SPEED, rightSpeedController);
				TCPrightSpeedController.start();

				TCPleftSpeedController = new TCPSocketSender(RobotMap.TCP_SERVER_LEFT_DRIVE_TRAIN_SPEED, leftSpeedController);
				TCPleftSpeedController.start();

				TCProtateController = new TCPSocketSender(RobotMap.TCP_SERVER_ROTATE_CONTROLLER, rotateController);
				TCProtateController.start();
				
				TCProtateController = new TCPSocketSender(RobotMap.TCP_SERVER_ROTATE_CONTROLLER_STRAIGHT, rotateDriveStraightController);
				TCProtateController.start();
				
				TCProtateCameraController = new TCPSocketSender(RobotMap.TCP_SERVER_ROTATE_CAMERA_CONTROLLER, rotateCameraController);
				TCProtateCameraController.start();
				
				leftMotor1Voltage = 0;
				leftMotor2Voltage = 0;

				rightMotor1Voltage = 0;
				rightMotor2Voltage = 0;

				//Log sensor data	
				ConsolePrinter.putNumber("Left Encoder Distance",() -> {return Robot.drivetrain.getLeftPosition();}, true, true);
				ConsolePrinter.putNumber("Right Encoder Distance:",() -> {return Robot.drivetrain.getRightPosition();}, true, true);
				ConsolePrinter.putNumber("Average Drive Encoder Distance", () -> {return Robot.drivetrain.getAverageDistance();}, true, true);
				
				ConsolePrinter.putNumber("Right Drive Encoder Rate", () -> {return Robot.drivetrain.getRightEncoderRate();}, true, true);
				ConsolePrinter.putNumber("Left Drive Encoder Rate", () -> {return Robot.drivetrain.getLeftEncoderRate();}, true, true);
				ConsolePrinter.putNumber("Average Drive Encoder Rate", () -> {return Robot.drivetrain.getAverageEncoderRate();}, true, true);

				ConsolePrinter.putNumber("Gyro Angle:", () -> {return Robot.drivetrain.getHeading();}, true, false);
				ConsolePrinter.putNumber("GYRO Driftrate:", () -> {return Robot.drivetrain.gyroSPI.driftRate;}, true, false);
				ConsolePrinter.putNumber("GYRO Rate:", () -> {return Robot.drivetrain.gyroSPI.getRate();}, true, false);
				ConsolePrinter.putNumber("GYRO Angle SPI:", () -> {return Robot.drivetrain.gyroSPI.getAngle();}, true, false);
				ConsolePrinter.putNumber("GYRO reInits:", () -> {return (double)Robot.gyroReinits;}, true, false);
				ConsolePrinter.putBoolean("Gyro Cal Status", () -> {return !Robot.gyroCalibrating;}, true, false);
				ConsolePrinter.putNumber("GYRO Status:", () -> {return (double)Robot.drivetrain.gyroSPI.getStatus();}, true, false);
				ConsolePrinter.putNumber("GYRO Temp:", () -> {return Robot.drivetrain.gyroSPI.getTemp();}, true, false);

				ConsolePrinter.putNumber("DTRight1MotorCurrent", 
						() -> {return Robot.pdp.getChannelCurrent(RobotMap.DRIVETRAIN_RIGHT_MOTOR_1_PDP);}, true, true);
				ConsolePrinter.putNumber("DTRight2MotorCurrent", 
						() -> {return Robot.pdp.getChannelCurrent(RobotMap.DRIVETRAIN_RIGHT_MOTOR_2_PDP);}, true, true);
			
				ConsolePrinter.putNumber("DTLeft1MotorCurrent", 
						() -> {return Robot.pdp.getChannelCurrent(RobotMap.DRIVETRAIN_LEFT_MOTOR_1_PDP);}, true, true);
				ConsolePrinter.putNumber("DTLeft2MotorCurrent", 
						() -> {return Robot.pdp.getChannelCurrent(RobotMap.DRIVETRAIN_LEFT_MOTOR_2_PDP);}, true, true);
		
	
				ConsolePrinter.putBoolean("Camera Status", 
						() -> {return Robot.drivetrain.tcpCamSensor.isCameraConnected();}, true, false);
				ConsolePrinter.putBoolean("Bone Status", 
						() -> {return Robot.drivetrain.tcpCamSensor.isClientConnected();}, true, false);
				ConsolePrinter.putBoolean("Processing Status", 
						() -> {return Robot.drivetrain.tcpCamSensor.isProcessingTreadRunning();}, true, false);
				ConsolePrinter.putBoolean("MJPEG Status", 
						() -> {return Robot.drivetrain.tcpCamSensor.isMJPEGConnected();}, true, false);
				ConsolePrinter.putNumber("Vision Target Dist", 
						() -> {return Robot.drivetrain.tcpCamSensor.getTargetDistance();}, true, false);
				ConsolePrinter.putNumber("Vision Target Bearing", 
						() -> {return Robot.drivetrain.tcpCamSensor.getRotationAngle();}, true, false);
				ConsolePrinter.putBoolean("Is Target Detected", 
						() -> {return Robot.drivetrain.tcpCamSensor.isTargetDetected();}, true, false);
				ConsolePrinter.putBoolean("Is Target Scorable", 
						() -> {return Robot.drivetrain.tcpCamSensor.isTargetScorable();}, true, false);
				
				ConsolePrinter.putNumber("Drivetrain raw IR", 
						() -> {return Robot.drivetrain.getIRVoltage();}, true, false);
								
				ConsolePrinter.putBoolean("Left Motor One Trip", () -> {return !Robot.pdp.isLeftMotorOneTrip();}, true, false);
				ConsolePrinter.putBoolean("Left Motor Two Trip", () -> {return !Robot.pdp.isLeftMotorTwoTrip();}, true, false);
				ConsolePrinter.putBoolean("Right Motor One Trip", () -> {return !Robot.pdp.isRightMotorOneTrip();}, true, false);
				ConsolePrinter.putBoolean("Right Motor Two Trip", () -> {return !Robot.pdp.isRightMotorTwoTrip();}, true, false);

				
				ConsolePrinter.putNumber("DTLeft1MotorVoltage",() -> {return Robot.drivetrain.getleftMotor1Voltage();}, true, true);
				ConsolePrinter.putNumber("DTLeft2MotorVoltage",() -> {return Robot.drivetrain.getleftMotor2Voltage();}, true, true);
				ConsolePrinter.putNumber("DTRight1MotorVoltage",() -> {return Robot.drivetrain.getrightMotor1Voltage();}, true, true);
				ConsolePrinter.putNumber("DTRight2MotorVoltage",() -> {return Robot.drivetrain.getrightMotor2Voltage();}, true, true);
				
				
				ConsolePrinter.putNumber("GunStyleXValueInterpolated", () -> {return Robot.drivetrain.getGunStyleXValue();}, true, false);
	
				//TODO: Make methods to return proper test values
//				ConsolePrinter.putBoolean("Left Drive Motor 1 Pass", () -> {return Robot.drivetrain.leftMotor1Pass);
//				ConsolePrinter.putBoolean("Left Drive Motor 2 Pass", () -> {return Robot.drivetrain.leftMotor2Pass);
//				ConsolePrinter.putBoolean("Left Drive Motor 3 Pass", () -> {return Robot.drivetrain.leftMotor3Pass);
//				ConsolePrinter.putBoolean("Right Drive Motor 1 Pass", () -> {return Robot.drivetrain.rightMotor1Pass);
//				ConsolePrinter.putBoolean("Right Drive Motor 2 Pass", () -> {return Robot.drivetrain.rightMotor2Pass);
//				ConsolePrinter.putBoolean("Right Drive Motor 3 Pass", () -> {return Robot.drivetrain.rightMotor3Pass);
//				ConsolePrinter.putBoolean("Indexer Motor Pass", Indexer.indexerPass);
//				ConsolePrinter.putBoolean("Intake Left Motor Pass", IntakeRoller.intakeLeftPass);
//				ConsolePrinter.putBoolean("Intake Right Motor Pass", IntakeRoller.intakeRightPass);
//				ConsolePrinter.putBoolean("Shooter FWD Motor Pass", Shooter.shooterFWDPass);
//				ConsolePrinter.putBoolean("Shooter AFT Motor Pass", Shooter.shooterAFTPass);
	
	
	}
	
	/**
	 * Calls instance object and makes it a singleton object of type Drivetrain
	 * @returns Drivetrain object "instance"
	 */
	public static Drivetrain getInstance() {
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
    private void driveleftMotor1(double speed) {
    	if(!RobotMap.DT_REVERSE_LEFT)
        	speed = -speed;

    	leftMotor1.set(speed);
    	leftMotor1Voltage = Robot.pdp.getBatteryVoltage() * speed;

    }
    
    /**
     * Calls left motor 2 and creates a local variable "speed"
     * Refers to boolean in Robot map and if true, speed = - speed
     * Uses set() command to assign the new speed to left motor 2
     * @param double speed between -1 and 1
     * negative is reverse, positive if forward, 0 is stationary
     */
    private void driveleftMotor2(double speed) {
    	if(RobotMap.DT_REVERSE_LEFT)
        	speed = -speed;

    	leftMotor2.set(speed);
    	leftMotor2Voltage = Robot.pdp.getBatteryVoltage() * speed;
    }
    
    /**
     * Take in double speed and sets it to left motors 1, 2, and 3
     * @param speed is a double between -1 and 1
     * negative is reverse, positive if forward, 0 is stationary
     */
    public void driveLeft(double speed) {
    	driveleftMotor1(speed);
    	driveleftMotor2(speed);
    }
    
    /**
     * Calls right motor 1 and creates a local variable "speed"
     * Refers to boolean in Robot map and if true, speed = - speed
     * Uses set() command to assign the new speed to right motor 1
     * @param double speed between -1 and 1
     * negative is reverse, positive if forward, 0 is stationary
     */
   private void driverightMotor1(double speed) {
	   if(!RobotMap.DT_REVERSE_RIGHT)
	    	speed = -speed;

    	rightMotor1.set(speed);
    	rightMotor1Voltage = Robot.pdp.getBatteryVoltage() *speed;
    }
    
   /**
    * Calls right motor 2 and creates a local variable "speed"
    * Refers to boolean in Robot map and if true, speed = - speed
    * Uses set() command to assign the new speed to right motor 2
    * @param double speed between -1 and 1
    * negative is reverse, positive if forward, 0 is stationary
    */
    private void driverightMotor2(double speed) {
    	if(RobotMap.DT_REVERSE_RIGHT)
        	speed = -speed;
    	
    	rightMotor2.set(speed);
    	rightMotor2Voltage = Robot.pdp.getBatteryVoltage() * speed;
    }
    
    /**
     * Takes in a double speed and sets it to their right motors 1, 2, and 3
     * @param speed is a double  between -1 and 1
     * negative is reverse, positive if forward, 0 is stationary
     */
    public void driveRight(double speed) {
    	driverightMotor1(speed);
    	driverightMotor2(speed);
    }
    
    /**
     * Takes in speed for right and speed for left and sets them to their respective sides 
     * @param leftSpeed is a double between -1 and 1 
     * @param rightSpeed is a double between -1 and 1
     * negative is reverse, positive if forward, 0 is stationary
     */
    public void tankDrive(double leftSpeed, double rightSpeed) {
    	driveLeft(leftSpeed);
    	driveRight(rightSpeed);
    }

    /**
     * Calls for default command of the drivetrain to be DriveWithJoystick
     */
    public void initDefaultCommand() {
       setDefaultCommand(new DriveWithJoystick(0));
    }
    
    /**
     * returns total distance traveled by right side of drivetrain
     * @return double in feet of total distance traveled by right encoder
     */
    public double getRightPosition() {
    	return drivetrainRightEncoder.getPos();
    }
    
    /**
     * returns total distance traveled by left side of drivetrain
     * @return double in feet of total distance traveled by left encoder
     */
    public double getLeftPosition() {
    	return drivetrainLeftEncoder.getPos();
    }
    
    /**
     * returns total distance traveled by drivetrain
     * @return double in inches of average distance traveled by both encoders
     */
    public double getAverageDistance() {
    	return imu.getPos();
    }
    
    /**
     * resets position of right encoder to 0 inches
     */
    public void resetRightPosition() {
    	drivetrainRightEncoder.reset();
    }
    /**
     * resets position of left encoder to 0 inches
     */
    public void resetLeftPosition() {
    	drivetrainLeftEncoder.reset();
    }
    
    /**
     * resets position of both Encoders to 0 inches
     */
    public void resetPosition() {
    	resetLeftPosition();
    	resetRightPosition();
    }
    
	/**
	 * Gets the voltage given by the Sharp IR sensor on the Gear Intake.
	 * @return the raw voltage from the gear presence sensor
	 */
	public double getIRVoltage(){
		return DrivetrainIRSensor.getVoltage();
	}
    
    /**
     * returns heading of robot
     * @return double between 0 degrees and 360 degrees
     */
    public double getHeading() {
    	return gyroSPI.getPos();		
    }
    
    /**
	 * Reset robot heading to zero.
	 */
	public void resetGyro() {
		gyroSPI.reset();
	}

	/**
	 * Calibrate gyro.
	 * This should only be called if the robot will be stationary for the calibration period.
	 */
	public void calibrateGyro() {
		gyroSPI.calibrate();
	}

	/**
	 * @return true if the gyro completed its previous calibration sequence.
	 */
	public boolean isGyroCalibrated() {
		return gyroSPI.hasCompletedCalibration();
	}

	/**
	 * @return true if the gyro is being calibrated.
	 */
	public boolean isGyroCalibrating() {
		return gyroSPI.isCalibrating();
	}

	/**
	 * Call to stop an active gyro calibration sequence.
	 */
	public void stopGyroCalibrating() {
		gyroSPI.stopCalibrating();
	}
	
	/**
	 * Call to start an active gyro calibration sequence.
	 */
	public void startGyroCalibrating() {
		gyroSPI.startCalibrating();
	}
    
    /**
     * Returns the last commanded voltage of left Motor 1
     * @return Double in volts between 0 and 12
     */
    public double getleftMotor1Voltage() {
    	return leftMotor1Voltage;
    }
    
    /**
     * Returns the last commanded voltage of left Motor 2
     * @return Double in volts between 0 and 12
     */
    public double getleftMotor2Voltage() {
    	return leftMotor2Voltage;
    }
    
    /**
     * Returns the last commanded voltage of right Motor 1
     * @return Double in volts between 0 and 12
     */
    public double getrightMotor1Voltage() {
    	return rightMotor1Voltage;
    } 
    
    /**
     * Returns the last commanded voltage of right Motor 2
     * @return Double in volts between 0 and 12
     */
    public double getrightMotor2Voltage() {
    	return rightMotor2Voltage;
    }
    
    public double getRightEncoderRate() {
    	return drivetrainRightEncoder.getRate();
    }
    
    public double getLeftEncoderRate() {
    	return drivetrainLeftEncoder.getRate();
    }
    
    public double getAverageEncoderRate() {
    	return ((getRightEncoderRate() + getLeftEncoderRate())/2);
    }
    
	/**
	 * Returns the current position of the gun style controller interpolated
	 * @param x is voltage
	 * @return Potentiometer position
	 */
	public double getGunStyleXValue() {
		//return gunStyleInterpolator.interpolate(Robot.oi.driverJoystick.getLeftStickRaw_X());
		return -Robot.oi.driverJoystick.getLeftStickRaw_X();
	}
    
}
