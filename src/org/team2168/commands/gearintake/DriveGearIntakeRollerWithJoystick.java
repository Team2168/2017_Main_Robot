package org.team2168.commands.gearintake;

import org.team2168.Robot;
import org.team2168.RobotMap;
import org.team2168.OI;

import edu.wpi.first.wpilibj.command.Command;

/**
 *Controls the roller with the right joystick.
 *@author Elijah Reeds
 */
public class DriveGearIntakeRollerWithJoystick extends Command {

    public DriveGearIntakeRollerWithJoystick() {
        // Use requires() here to declare subsystem dependencies
        // eg. requires(chassis);
    	requires(Robot.gearIntakeRoller);
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    	Robot.gearIntakeRoller.setMotorSpeed(OI.getDriveGearIntakeRollerJoystick());
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return false;
    }

    // Called once after isFinished returns true
    protected void end() {
    	Robot.gearIntakeRoller.setMotorSpeed(0.0);
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    	end();
    }
}
