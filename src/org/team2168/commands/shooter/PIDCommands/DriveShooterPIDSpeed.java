
package org.team2168.commands.shooter.PIDCommands;

import org.team2168.Robot;

import edu.wpi.first.wpilibj.command.Command;



/**
 *
 * @author shriji
 */
public class DriveShooterPIDSpeed extends Command {
	
	private double setPoint;
	private double lastControlOut;
	private double count;

    public DriveShooterPIDSpeed() {
        // Use requires() here to declare subsystem dependencies
        // eg. requires(chassis);
    	requires(Robot.shooterWheel);
    	this.lastControlOut = 0;
    	this.setPoint = 0;
    	this.count = 0;
    }
    
   public DriveShooterPIDSpeed(double setPoint){
	   this();
	   this.setPoint = setPoint;
	   
   }

    // Called just before this Command runs the first time
	protected void initialize() {
		//Robot.shooter.shooterSpeedController.reset();
		Robot.shooterWheel.shooterSpeedController.Enable();
		count = 0;
    }

    // Called repeatedly when this Command is scheduled to run
	protected void execute() {
    	if (setPoint != 0)
    		Robot.shooterWheel.shooterSpeedController.setSetPoint(setPoint);
    	
    	if (Robot.shooterWheel.shooterSpeedController.isFinished())
    		count = 1;
    	
    	if (count==0)
    	{
    		Robot.shooterWheel.setShooterSpeed(Robot.shooterWheel.shooterSpeedController.getControlOutput());
    		this.lastControlOut = Robot.shooterWheel.shooterSpeedController.getControlOutput();
    	}
    	else
    	{
    		Robot.shooterWheel.setShooterSpeed(lastControlOut);
    		System.out.println("PID Constant Out");
    	}
    		
    		
    	
    }

    // Make this return true when this Command no longer needs to run execute()
    
	protected boolean isFinished() {
        return Robot.shooterWheel.shooterSpeedController.isEnabled() == false;
    }

    // Called once after isFinished returns true
    
	protected void end() {
		Robot.shooterWheel.shooterSpeedController.Pause();
    }

    //delete me
    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    
	protected void interrupted() {
    	end();
    }
}
