package org.team2168.commands.shooter;

import org.team2168.Robot;

import edu.wpi.first.wpilibj.command.Command;

/**
 * Drives ShooterIndexer at constant speed
 */
public class DriveShooterIndexerWithConstant extends Command {

	double speed;
	
	/**
	 * 
	 * @param inputSpeed
	 */
    public DriveShooterIndexerWithConstant(double inputSpeed) {
        // Use requires() here to declare subsystem dependencies
        // eg. requires(chassis);
    	requires(Robot.shooterIndexer);
    	
    	speed = inputSpeed;
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    	Robot.shooterIndexer.setSpeed(speed);
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return false;
    }

    // Called once after isFinished returns true
    protected void end() {
    	Robot.shooterIndexer.setSpeed(0);
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    	end();
    }
}
