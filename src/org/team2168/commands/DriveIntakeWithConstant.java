package org.team2168.commands;

import org.team2168.Robot;

import edu.wpi.first.wpilibj.command.Command;

/**
 * Command for intake
 * @author kvictorino thanks John
 */
public class DriveIntakeWithConstant extends Command {

	double speed;
	
    public DriveIntakeWithConstant(double inputSpeed) {
        // Use requires() here to declare subsystem dependencies
        requires(Robot.intake);
        speed = inputSpeed;
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
		Robot.intake.driveIntake(speed);
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