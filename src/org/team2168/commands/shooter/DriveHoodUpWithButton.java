package org.team2168.commands.shooter;

import org.team2168.Robot;
import org.team2168.RobotMap;

import edu.wpi.first.wpilibj.command.Command;

/**
 * Command to move the turret shooter hood with a controller joystick
 */
public class DriveHoodUpWithButton extends Command {

    public DriveHoodUpWithButton() {
        // Use requires() here to declare subsystem dependencies
        requires(Robot.shooterHood);
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    }

    // Takes the current angle of the shooter hood servo and adds to it based on
    // how far the right operator joystick is pushed on the Y-axis
    
    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    	
		Robot.shooterHood.setAngle(Robot.shooterHood.getAngle() + RobotMap.SHOOTER_DEGREE_PER_BUTTON_RATE);
   	
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
