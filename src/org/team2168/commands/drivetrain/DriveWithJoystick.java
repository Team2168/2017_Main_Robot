package org.team2168.commands.drivetrain;

import org.team2168.OI;
import org.team2168.Robot;
import org.team2168.RobotMap;

import edu.wpi.first.wpilibj.command.Command;

/**
 *	Choose and use a control style for the drivetrain
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
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    	
    	switch(ctrlStyle){
    	/**
    	 *Tank Drive
    	 */
    	case 0:
    		Robot.drivetrain.driveLeftSide(Robot.oi.driverJoystick.getLeftStickRaw_Y());
        	Robot.drivetrain.driveRightSide(Robot.oi.driverJoystick.getRightStickRaw_Y());
        	break;
        
        /**
         * Gun Style Controller
         */
    	case 1:

		lastRotateOutput = Robot.drivetrain.rotateDriveStraightController.getControlOutput();
		double headingCorrection = (Robot.drivetrain.rotateDriveStraightController.getControlOutput()) ;

		Robot.drivetrain.tankDrive(Robot.oi.driverJoystick.getLeftStickRaw_X()+headingCorrection+Robot.oi.driverJoystick.getRightStickRaw_X(),
								   Robot.oi.driverJoystick.getLeftStickRaw_X()-headingCorrection-Robot.oi.driverJoystick.getRightStickRaw_X());
		//finished = Robot.drivetrain.driveTrainPosController.isFinished();
//		if(Robot.oi.driverJoystick.getRightStickRaw_X() > 0.1 || Robot.oi.driverJoystick.getRightStickRaw_X() < -0.1) {
//			Robot.drivetrain.resetGyro();
//		}

		if ((Robot.oi.driverJoystick.getLeftStickRaw_X() > 0.25 || Robot.oi.driverJoystick.getLeftStickRaw_X() < -0.25)
		&&!(Robot.oi.driverJoystick.getLeftStickRaw_Y() > 0.1 || Robot.oi.driverJoystick.getLeftStickRaw_Y() < -0.1)) {
			Robot.drivetrain.tankDrive((-Robot.oi.driverJoystick.getLeftStickRaw_X()+0.3)+headingCorrection,
									   (-Robot.oi.driverJoystick.getLeftStickRaw_X()+0.3)-headingCorrection);
		}	
		else {
			Robot.drivetrain.tankDrive((-Robot.oi.driverJoystick.getLeftStickRaw_X()+0.3)+Robot.oi.driverJoystick.getLeftStickRaw_Y(),
									   (-Robot.oi.driverJoystick.getLeftStickRaw_X()+0.3)-Robot.oi.driverJoystick.getLeftStickRaw_Y());
			Robot.drivetrain.rotateDriveStraightController.setSetPoint(Robot.drivetrain.getHeading());
		}
        	
        /**
        * Arcade Drive
        */
    	case 2:
    		Robot.drivetrain.driveLeftSide(Robot.oi.driverJoystick.getLeftStickRaw_Y() + Robot.oi.driverJoystick.getRightStickRaw_X());
    		Robot.drivetrain.driveRightSide(Robot.oi.driverJoystick.getLeftStickRaw_Y() - Robot.oi.driverJoystick.getRightStickRaw_X());
    		break;
    	/**
    	* GTA Drive
    	*/
    	case 3:
    		double fwdSpeed = Robot.oi.driverJoystick.getRightTriggerAxisRaw();
    		double revSpeed = Robot.oi.driverJoystick.getLeftTriggerAxisRaw();
    		double speed = fwdSpeed - revSpeed;
    		
    		//Allows Robot to spin in place without needing to press in triggers
    		if(speed != 0){
    			Robot.drivetrain.driveLeftSide(Robot.oi.driverJoystick.getRightStickRaw_X() * speed);
    			Robot.drivetrain.driveRightSide(-(Robot.oi.driverJoystick.getRightStickRaw_X()) * speed);
    		}
    		else if(speed == 0) {
    			Robot.drivetrain.driveLeftSide(Robot.oi.driverJoystick.getRightStickRaw_X());
    			Robot.drivetrain.driveRightSide(-(Robot.oi.driverJoystick.getRightStickRaw_X()));
    		}
    		break;
    	/**
    	 *Defaults to Tank Drive
    	 */
    	default:
    		Robot.drivetrain.driveLeftSide(Robot.oi.driverJoystick.getLeftStickRaw_Y());
        	Robot.drivetrain.driveRightSide(Robot.oi.driverJoystick.getRightStickRaw_Y());
        	break;
    	}
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return false;
    }

    // Called once after isFinished returns true
    protected void end() {
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    }
}