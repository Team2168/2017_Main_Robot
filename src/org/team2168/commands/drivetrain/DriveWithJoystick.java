package org.team2168.commands.drivetrain;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.TimeZone;

import org.team2168.OI;
import org.team2168.Robot;
import org.team2168.RobotMap;
import org.team2168.utils.consoleprinter.ConsolePrinter;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 *
 */
public class DriveWithJoystick extends Command {
	
	int ctrlStyle;
	/**
	 * Controller Styles
	 * 0 = Tank Drive (Default)
	 * 1 = Gun Style
	 * 2 = Arcade Drive
	 * 3 = GTA
	 * @param inputStyle
	 */
	
	private double distanceGoal;
	private double speed;
	private double endDistance;
	private boolean finished;
	private double angle;
	private double error = 0.3;
	
	private double powerShift;

	double rightSpeed = 0;
	double leftSpeed = 0;

	static final double DIST_ERROR_TOLERANCE_INCH = 1;
	static final double TURN_ERROR_TOLERANCE_DEG =1;
	
	static final int secondsToRecord = 10;
	private double[][] driverRecordArray = new double[2][(int) (secondsToRecord*(1/0.02))];  
	int timeIndex;
	private boolean write = false;
	
	double lastRotateOutput;
	
    public DriveWithJoystick(int inputStyle) {
        // Use requires() here to declare subsystem dependencies
        // eg. requires(chassis);
    	requires(Robot.drivetrain);
    	ctrlStyle = inputStyle;
    	//TODO not sure where distanceGoal is used but for our tests we used a value of 1
		this.distanceGoal = 1;
		this.speed = RobotMap.AUTO_NORMAL_SPEED;
		this.powerShift = 1;
		this.lastRotateOutput = 0;
		this.timeIndex = 0;
		
		//initialize array
		 for (int row = 0; row < driverRecordArray.length; row++) {
		    for (int col = 0; col < driverRecordArray[row].length; col++) {
		    	driverRecordArray[row][col] = 0.0;
		    }
		 }


		//Read more: http://javarevisited.blogspot.com/2015/09/how-to-loop-two-dimensional-array-in-java.html#ixzz54VMFEMeX

		
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    	
    	
    	timeIndex = 0;
    	
    	ctrlStyle = Robot.getControlStyleInt();
    	switch(ctrlStyle){
		/**
		 * Initialize driveStraightController for Gun style
		 */
		case 1:
			finished = false;
			Robot.drivetrain.getInstance();
			Robot.drivetrain.resetPosition();

			//reset controller
			Robot.drivetrain.imu.reset();
			Robot.drivetrain.driveTrainPosController.reset();
			Robot.drivetrain.rotateDriveStraightController.reset();


			//drivetrain.resetGyro();
			endDistance = Robot.drivetrain.getAverageDistance() + distanceGoal;
			angle = Robot.drivetrain.getHeading();

//			Robot.drivetrain.rotateDriveStraightController.setpGain(RobotMap.ROTATE_POSITION_P_Drive_Straight);
//			Robot.drivetrain.rotateDriveStraightController.setiGain(RobotMap.ROTATE_POSITION_I_Drive_Straight);
//			Robot.drivetrain.rotateDriveStraightController.setdGain(RobotMap.ROTATE_POSITION_D_Drive_Straight);
			Robot.drivetrain.driveTrainPosController.setSetPoint(endDistance);
			Robot.drivetrain.driveTrainPosController.setMaxPosOutput(speed);
			Robot.drivetrain.driveTrainPosController.setMinPosOutput(-speed);
			Robot.drivetrain.driveTrainPosController.setAcceptErrorDiff(error); //feet
			Robot.drivetrain.rotateDriveStraightController.setSetPoint(angle);
			
			
			
			// 		//This code helps offset uneven left/right gearbox power, like a feedworward term
			//		//modify speeds based on power shift, - means put more power to left side, + means put more power to right side
			//		//this power shift helps accommodate for unequal power in drivetrains
			//
			//		if (powerShift > 1) //reduce left speed so right power is increased
			//		{
			//			rightSpeed = speed;
			//			leftSpeed = speed - speed*Math.abs(powerShift%1);
			//		}
			//
			//		else if (powerShift < 1) //reduce right speed so left if increased
			//		{
			//			rightSpeed = speed - speed*Math.abs(powerShift%1);
			//			leftSpeed = speed;
			//		}
			//
			//		Robot.drivetrain.rotateDriveStraightController.setSetPoint(Robot.drivetrain.gyroSPI.getAngleDeg());

			Robot.drivetrain.driveTrainPosController.Enable();
			Robot.drivetrain.rotateDriveStraightController.Enable();
		
			System.out.println("Initialize case ran");
		default: 
		
		break;
		}
    }

    /**
     * Gets the joystick positions from OI and sends them to the drivetrain subsystem.
     * @author Krystina
     */
    protected void execute() {
    	ctrlStyle = Robot.getControlStyleInt();
    	
    	switch(ctrlStyle){
    	/**
    	 *Tank Drive
    	 */
    	case 0:
    		Robot.drivetrain.driveLeft(Robot.oi.driverJoystick.getLeftStickRaw_Y());
        	Robot.drivetrain.driveRight(Robot.oi.driverJoystick.getRightStickRaw_Y());
        	break;
        
        /**
         * Gun Style Controller
         */
        //X Values
        //full in: -0.516
        //nothing: 0.354 & 0.342
        //full out: 0.622
    	case 1:

		lastRotateOutput = Robot.drivetrain.rotateDriveStraightController.getControlOutput();
		double headingCorrection = (Robot.drivetrain.rotateDriveStraightController.getControlOutput()) ;

//		if ((Robot.oi.driverJoystick.getLeftStickRaw_X() > 0.25 || Robot.oi.driverJoystick.getLeftStickRaw_X() < -0.25)
//		&&!(Robot.oi.driverJoystick.getLeftStickRaw_Y() > 0.1 || Robot.oi.driverJoystick.getLeftStickRaw_Y() < -0.1)) {
//			Robot.drivetrain.tankDrive((-Robot.oi.driverJoystick.getLeftStickRaw_X()+0.35)+headingCorrection,
//									   (-Robot.oi.driverJoystick.getLeftStickRaw_X()+0.35)-headingCorrection);
//		}	
//		else {
//			Robot.drivetrain.tankDrive((-Robot.oi.driverJoystick.getLeftStickRaw_X()+0.35)+Robot.oi.driverJoystick.getLeftStickRaw_Y(),
//									   (-Robot.oi.driverJoystick.getLeftStickRaw_X()+0.35)-Robot.oi.driverJoystick.getLeftStickRaw_Y());
//			Robot.drivetrain.rotateDriveStraightController.setSetPoint(Robot.drivetrain.getHeading());
//		}
//		
		Robot.drivetrain.tankDrive(Robot.drivetrain.getGunStyleXValue(), Robot.drivetrain.getGunStyleXValue());
		if ((Robot.drivetrain.getGunStyleXValue() > 0.25 || Robot.drivetrain.getGunStyleXValue() < -0.25)
				&&!(Robot.oi.driverJoystick.getLeftStickRaw_Y() > 0.1 || Robot.oi.driverJoystick.getLeftStickRaw_Y() < -0.1)) 
		{
				Robot.drivetrain.tankDrive(Robot.drivetrain.getGunStyleXValue(), Robot.drivetrain.getGunStyleXValue());		
		}	
				else {
					Robot.drivetrain.tankDrive((Robot.drivetrain.getGunStyleXValue())+Robot.oi.driverJoystick.getLeftStickRaw_Y(),
											   (Robot.drivetrain.getGunStyleXValue())-Robot.oi.driverJoystick.getLeftStickRaw_Y());
					Robot.drivetrain.rotateDriveStraightController.setSetPoint(Robot.drivetrain.getHeading());					
				}
		
		if (timeIndex < driverRecordArray[0].length)
		{					
			driverRecordArray[0][timeIndex] = Robot.drivetrain.getleftMotor1Voltage();
			driverRecordArray[1][timeIndex] = Robot.drivetrain.getrightMotor1Voltage();
		}
		else
		{
			if (!write)
			{
				dataToFile();
				write = true;
			}
		}
		
        break;
        
        /**
        * Arcade Drive
        */
    	case 2:
    		Robot.drivetrain.driveLeft(Robot.oi.driverJoystick.getLeftStickRaw_Y() + Robot.oi.driverJoystick.getRightStickRaw_X());
    		Robot.drivetrain.driveRight(Robot.oi.driverJoystick.getLeftStickRaw_Y() - Robot.oi.driverJoystick.getRightStickRaw_X());
    		break;
    	/**
    	* GTA Drive
    	*/
    	case 3:
    		double fwdSpeed = Robot.oi.driverJoystick.getRightTriggerAxisRaw();
    		double revSpeed = Robot.oi.driverJoystick.getLeftTriggerAxisRaw();
    		double speed = fwdSpeed - revSpeed;
    		double rotation = Robot.oi.driverJoystick.getRightStickRaw_X();
    		
    		//Adjusts angle while moving
    		if(speed != 0 && rotation !=0){
    			Robot.drivetrain.driveLeft(rotation * speed);
    			Robot.drivetrain.driveRight(-rotation * speed);
    		}
    		//Allows Robot to spin in place without needing to press in triggers
    		else if(speed == 0 && rotation != 0) {
    			Robot.drivetrain.driveLeft(rotation);
    			Robot.drivetrain.driveRight(-rotation);
    		}
    		//Allows Robot to drive straight
    		else if(speed !=0 && rotation == 0){
    			Robot.drivetrain.driveLeft(speed);
    			Robot.drivetrain.driveRight(speed);
    		}
    		break;
    	/**
    	 *Defaults to Tank Drive
    	 */
    	default:
    		Robot.drivetrain.driveLeft(Robot.oi.driverJoystick.getLeftStickRaw_Y());
        	Robot.drivetrain.driveRight(Robot.oi.driverJoystick.getRightStickRaw_Y());
        	break;
    	}
    }


    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return false;
    }

    // Called once after isFinished returns true
    protected void end() {
    	Robot.drivetrain.tankDrive(0.0, 0.0);
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    	end();
    }
    
    
 

/**
 * Writes current values for all data elements to the log file for data elements that have file logging enabled.
 */
private void dataToFile() {
	
	PrintWriter log;
	
	try {
		File file = new File("/home/lvuser/DriverRecorder");
		if (!file.exists()) {
			if (file.mkdir()) {
				System.out.println("Recorder Directory is created!");
			} else {
				System.out.println("Failed to create Recorder directory!");
			}
		}
		Date date = new Date() ;
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss");
		dateFormat.setTimeZone(TimeZone.getTimeZone("EST5EDT"));
		log = new PrintWriter("/home/lvuser/DriverRecorder/DriverRecorder10SecLog.txt", "UTF-8");
		
		for (int i=0; i<driverRecordArray[0].length; i++)
		{
					log.println(driverRecordArray[0][i] + "," + driverRecordArray[1][i]);
					log.flush();
		}
		
		System.out.println("Driver Recorder File Written");
	} catch (FileNotFoundException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} catch (UnsupportedEncodingException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}

}
}
