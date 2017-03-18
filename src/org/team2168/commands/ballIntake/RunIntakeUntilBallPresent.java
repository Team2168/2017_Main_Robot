package org.team2168.commands.ballIntake;

import org.team2168.Robot;

import edu.wpi.first.wpilibj.command.Command;

/**
 * Runs elevator until a ball is not present
 * @author Wen Baid
 */
public class RunIntakeUntilBallPresent extends Command {
	double speed;
	
	/**
	 * 
	 * @param speed -1.0 to 1.0. Positive values bring a ball into the shooter.
	 */
    public RunIntakeUntilBallPresent(double speed) {
    	requires(Robot.ballIntakeRoller);
    	this.speed = speed;
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
		Robot.ballIntakeRoller.driveIntake(speed);
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
    	return Robot.shooterIndexer.isBallPresent();
    }

    // Called once after isFinished returns true
    protected void end() {
    	Robot.ballIntakeRoller.driveIntake(0.0);
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    	end();
    }
}
