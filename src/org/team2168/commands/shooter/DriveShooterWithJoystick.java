package org.team2168.commands.shooter;

import org.team2168.OI;
import org.team2168.Robot;

import edu.wpi.first.wpilibj.command.Command;

public class DriveShooterWithJoystick extends Command {

		/**
		 *Drives both shooter wheels with the operator joystick
		 *@author Krystina
		 */
	    public DriveShooterWithJoystick() {
	        // Use requires() here to declare subsystem dependencies
	       requires(Robot.shooterWheel);
	    }

	    // Called just before this Command runs the first time
	    protected void initialize() {
	    }

	    // Called repeatedly when this Command is scheduled to run
	    //TODO Put OI method in
	    protected void execute() {
	    	Robot.shooterWheel.setShooterSpeed(OI.getDriveShooterJoystick() + OI.getDriveShooterPIDTestJoystick());
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


