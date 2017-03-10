
package org.team2168.commands.drivetrain.PIDCommands;

import javax.rmi.CORBA.Util;

import org.team2168.Robot;
import org.team2168.RobotMap;
import org.team2168.utils.Debouncer;

import edu.wpi.first.wpilibj.command.Command;



/**
 *
 * @author Vittorio
 */
public class RotateXDistancePIDZZZCameraWithGyroMidnight extends Command {

	
	private double CONSTANT_CAM_ERROR_TIMEOUT_PERIOD = 1.0;
	private double CONSTANT_PID_ERROR_TIMEOUT_PERIOD = 0.5;
	
	
	private double setPoint;
	private double maxSpeed;
	private double minSpeed;
	private double error = 0.5;  // Rotational degree error, default 0 never ends.
	
	private Debouncer camDebouncer;
	private Debouncer pidDebouncer;
	
	
    public RotateXDistancePIDZZZCameraWithGyroMidnight() {
        // Use requires() here to declare subsystem dependencies
    	requires(Robot.drivetrain);
    	this.setPoint = Robot.drivetrain.rotateController.getSetPoint();
    	this.maxSpeed = 1;
    	this.minSpeed = 0;
    	this.camDebouncer = new Debouncer(CONSTANT_CAM_ERROR_TIMEOUT_PERIOD);
    	this.pidDebouncer = new Debouncer(CONSTANT_PID_ERROR_TIMEOUT_PERIOD);
    }

    public RotateXDistancePIDZZZCameraWithGyroMidnight(double setPoint){
 	   this();
 	   this.setPoint = setPoint;
    }

    public RotateXDistancePIDZZZCameraWithGyroMidnight(double setPoint, double maxSpeed){
  	   this(setPoint);
  	   this.maxSpeed = maxSpeed;
     }
    
    public RotateXDistancePIDZZZCameraWithGyroMidnight(double setPoint, double maxSpeed, double minSpeed){
   	   this(setPoint, maxSpeed);
   	   this.minSpeed = minSpeed;
      }    

    public RotateXDistancePIDZZZCameraWithGyroMidnight(double setPoint, double maxSpeed, double minSpeed, double error) {
    	this(setPoint, maxSpeed, minSpeed);
    	this.error = error;
    }
    
    // Called just before this Command runs the first time
    
	protected void initialize() {
		setPoint = Robot.drivetrain.getHeading() - Robot.drivetrain.tcpCamSensor.getRotationAngle() + RobotMap.CAMERA_OFFSET_ANGLE;
		Robot.drivetrain.rotateController.reset();
		Robot.drivetrain.rotateController.setSetPoint(setPoint);
		Robot.drivetrain.rotateController.setMaxPosOutput(maxSpeed);
		Robot.drivetrain.rotateController.setMaxNegOutput(-maxSpeed);
		Robot.drivetrain.rotateController.setMinPosOutput(minSpeed);
		Robot.drivetrain.rotateController.setMinNegOutput(-minSpeed);
		//Robot.drivetrain.rotateController.setAcceptErrorDiff(error);
		//Robot.drivetrain.gyroSPI.reset();
		Robot.drivetrain.rotateController.Enable();
		
    }

    // Called repeatedly when this Command is scheduled to run
    
	protected void execute() {
		
		Robot.flashlight.disableFlashlight();
		//keep robot moving until we are at center
		
		camDebouncer.update(Math.abs(Robot.drivetrain.tcpCamSensor.getPos()) < error);
		pidDebouncer.update(Robot.drivetrain.rotateController.isFinished() || !Robot.drivetrain.rotateController.isEnabled());
		
		if(pidDebouncer.getStatus()) {
			Robot.drivetrain.rotateController.setSetPoint(Robot.drivetrain.getHeading() - Robot.drivetrain.tcpCamSensor.getRotationAngle());
		}
		
		Robot.drivetrain.tankDrive(Robot.drivetrain.rotateController.getControlOutput(),-Robot.drivetrain.rotateController.getControlOutput());
		
		
    }

    // Make this return true when this Command no longer needs to run execute()
    
	protected boolean isFinished() {
		//TODO Should the command be stopped????????!?!?!?!?!? after PID is tuned
//    	if( Robot.drivetrain.rotateController.isFinished())
//    	{
//    		setPoint = Robot.drivetrain.getHeading() - Robot.tcpCamSensor.getRotationAngle();
//    		Robot.drivetrain.rotateController.setSetPoint(setPoint);
//    	}
//    	
//		
		
//		return Robot.drivetrain.rotateController.isFinished() || Math.abs(Robot.drivetrain.tcpCamSensor.getPos()) < error;
//		return Math.abs(Robot.drivetrain.tcpCamSensor.getPos()) < error;
		
		return camDebouncer.getStatus();
    }

    // Called once after isFinished returns true
    
	protected void end() {
		Robot.flashlight.enableFlashlight();
		Robot.drivetrain.rotateController.Pause();
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    
	protected void interrupted() {
    	end();
    }
}