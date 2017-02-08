
package org.team2168.commands.shooter.PIDCommands;

import org.team2168.Robot;

import edu.wpi.first.wpilibj.command.Command;



/**
 *
 * @author shriji
 */
public class DriveShooterPIDSpeed extends Command {
	
	private double setPoint;

    public DriveShooterPIDSpeed() {
        // Use requires() here to declare subsystem dependencies
        // eg. requires(chassis);
    	requires(Robot.shooter);
    	this.setPoint = 0;
    }
    
   public DriveShooterPIDSpeed(double setPoint){
	   this();
	   this.setPoint = setPoint;
	   
   }

    // Called just before this Command runs the first time
	protected void initialize() {
		//Robot.shooter.shooterSpeedController.reset();
		Robot.shooter.shooterSpeedController.Enable();
    }

    // Called repeatedly when this Command is scheduled to run
	protected void execute() {
    	if (setPoint != 0)
    		Robot.shooter.shooterSpeedController.setSetPoint(setPoint);
    	Robot.shooter.driveShooter(Robot.shooter.shooterSpeedController.getControlOutput());
    	
    }

    // Make this return true when this Command no longer needs to run execute()
    
	protected boolean isFinished() {
        return Robot.shooter.shooterSpeedController.isEnabled() == false;
    }

    // Called once after isFinished returns true
    
	protected void end() {
		Robot.shooter.shooterSpeedController.Pause();
    }

    //delete me
    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    
	protected void interrupted() {
    	end();
    }
}
