package org.team2168.commands.turret;

import org.team2168.OI;
import org.team2168.Robot;

import edu.wpi.first.wpilibj.command.Command;

/**
 * Drives turret with joystick
 * @author Wen Baid
 */
public class DriveTurretWithJoystick extends Command {

    public DriveTurretWithJoystick() {
        // Use requires() here to declare subsystem dependencies
        // eg. requires(chassis);
    	requires(Robot.turret);
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    }

    // Called repeatedly when this Command is scheduled to run
    /**
     * Controls turret with operator left joystick
     */
    protected void execute() {
    	//TODO set this to whatever we need
    	Robot.turret.setSpeed(OI.getDriveTurretJoystick());
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return false;
    }

    // Called once after isFinished returns true
    /**
     * Stops turret motor
     */
    protected void end() {
    	Robot.turret.setSpeed(0);
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    /**
     * calls to stop when interrupted
     */
    protected void interrupted() {
    	end();
    }
}
