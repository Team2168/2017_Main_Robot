package org.team2168.commands.gearintake;

import org.team2168.Robot;
import org.team2168.RobotMap;

import edu.wpi.first.wpilibj.command.Command;

/**
 *Pretty much SpinRollerConstant except that it stops using the satanic powers found within the Sharp IR sensor.
 *@author Elijah Reeds
 */
public class DriveRollerUntilGearPresent extends Command {

    public DriveRollerUntilGearPresent() {
        // Use requires() here to declare subsystem dependencies
        // eg. requires(chassis);
    	requires(Robot.gearIntakeRoller);
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    	Robot.gearIntakeRoller.setMotorSpeed(RobotMap.GEAR_INTAKE_SPEED_CONSTANT);
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return Robot.gearIntakeRoller.isGearPresent();
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
