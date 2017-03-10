package org.team2168.subsystems;

import org.team2168.Robot;
import org.team2168.RobotMap;
import org.team2168.commands.elevator.DriveElevatorWithJoystick;

import edu.wpi.first.wpilibj.Spark;
import edu.wpi.first.wpilibj.SpeedController;
import edu.wpi.first.wpilibj.Victor;
import edu.wpi.first.wpilibj.command.Subsystem;

/**
 *Ball Elevator subsystem 
 *@author David
 */
public class BallConvelator extends Subsystem {

	private static BallConvelator instance = null;
	private static SpeedController elevatorMotor;

	private BallConvelator() {
		if(Robot.isPracticeRobot())
		{
        	elevatorMotor = new Victor(RobotMap.CONVELATOR_MOTOR);
		}
		else
		{
	    	elevatorMotor = new Spark(RobotMap.CONVELATOR_MOTOR);
		}
	}

	/** 
	 * singleton getter for Ball Elevator 
	 * @return instance 
	 */
	public static BallConvelator getInstance(){
		if(instance==null)
			instance = new BallConvelator();
		return instance;
	}
	
    /**
     * @param speed -1.0 to 1.0, positive runs the ball up the elevator, negative down
     */
	public void driveElevator(double speed){
		if(RobotMap.REVERSE_CONVELATOR_WHEEL)
			speed = -speed;

		elevatorMotor.set(speed);
	}
	
	public void initDefaultCommand() {
        //setDefaultCommand(new DriveElevatorWithJoystick());
    }
}
