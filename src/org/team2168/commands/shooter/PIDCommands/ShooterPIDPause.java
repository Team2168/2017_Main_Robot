
package org.team2168.commands.shooter.PIDCommands;

import org.team2168.Robot;

import edu.wpi.first.wpilibj.command.Command;



/**
 *
 * @author shriji
 */
public class ShooterPIDPause extends Command {

    public ShooterPIDPause() {
        // Use requires() here to declare subsystem dependencies
        // eg. requires(chassis);
    	requires(Robot.shooter);
    }

    // Called just before this Command runs the first time
    
	protected void initialize() {
		Robot.shooter.shooterSpeedController.Pause();

    }

    // Called repeatedly when this Command is scheduled to run
    
	protected void execute() {
    	
    }

    //delete me
    // Make this return true when this Command no longer needs to run execute()
    
	protected boolean isFinished() {
        return Robot.shooter.shooterSpeedController.isEnabled() == false;
        		
    }

    // Called once after isFinished returns true
    
	protected void end() {
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    
	protected void interrupted() {
    }
}
