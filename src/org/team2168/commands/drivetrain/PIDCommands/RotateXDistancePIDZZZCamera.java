
package org.team2168.commands.drivetrain.PIDCommands;

import org.team2168.Robot;
import org.team2168.RobotMap;

import edu.wpi.first.wpilibj.command.Command;



/**
 *
 * @author Vittorio
 */
public class RotateXDistancePIDZZZCamera extends Command {

	private double setPoint;
	private double maxSpeed;
	private double minSpeed;
	private double error = 0.3;  // Rotational degree error, default 0 never ends.
	
    public RotateXDistancePIDZZZCamera() {
        // Use requires() here to declare subsystem dependencies
    	requires(Robot.drivetrain);
    	this.setPoint = Robot.drivetrain.rotateCameraController.getSetPoint();
    	this.maxSpeed = 1;
    	this.minSpeed = 0;
    }

    public RotateXDistancePIDZZZCamera(double setPoint){
 	   this();
 	   this.setPoint = setPoint;
    }

    public RotateXDistancePIDZZZCamera(double setPoint, double maxSpeed){
  	   this(setPoint);
  	   this.maxSpeed = maxSpeed;
     }
    
    public RotateXDistancePIDZZZCamera(double setPoint, double maxSpeed, double minSpeed){
   	   this(setPoint, maxSpeed);
   	   this.minSpeed = minSpeed;
      }    

    public RotateXDistancePIDZZZCamera(double setPoint, double maxSpeed, double minSpeed, double error) {
    	this(setPoint, maxSpeed, minSpeed);
    	this.error = error;
    }
    
    // Called just before this Command runs the first time
    
	protected void initialize() {

		Robot.drivetrain.rotateCameraController.reset();
		this.setPoint += RobotMap.CAMERA_OFFSET_ANGLE;
		Robot.drivetrain.rotateCameraController.setSetPoint(setPoint);
		Robot.drivetrain.rotateCameraController.setMaxPosOutput(maxSpeed);
		Robot.drivetrain.rotateCameraController.setMaxNegOutput(-maxSpeed);
		Robot.drivetrain.rotateCameraController.setMinPosOutput(minSpeed);
		Robot.drivetrain.rotateCameraController.setMinNegOutput(-minSpeed);
		Robot.drivetrain.rotateCameraController.setAcceptErrorDiff(error);
		//Robot.drivetrain.gyroSPI.reset();
		Robot.drivetrain.rotateCameraController.Enable();
		
    }

    // Called repeatedly when this Command is scheduled to run
    
	protected void execute() {
		Robot.drivetrain.tankDrive(Robot.drivetrain.rotateCameraController.getControlOutput(),-Robot.drivetrain.rotateCameraController.getControlOutput());
	
		
    }

    // Make this return true when this Command no longer needs to run execute()
    
	protected boolean isFinished() {
		//TODO Should the command be stopped????????!?!?!?!?!? after PID is tuned
//    	if( Robot.drivetrain.rotateCameraController.isFinished())
//    	{
//    		setPoint = Robot.drivetrain.getHeading() - Robot.tcpCamSensor.getRotationAngle();
//    		Robot.drivetrain.rotateCameraController.setSetPoint(setPoint);
//    	}
//    	
    	
		return Robot.drivetrain.rotateCameraController.isFinished();
//    	return false; //return cam is scorable
		
    }

    // Called once after isFinished returns true
    
	protected void end() {
		Robot.drivetrain.rotateCameraController.Pause();
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    
	protected void interrupted() {
    	end();
    }
}