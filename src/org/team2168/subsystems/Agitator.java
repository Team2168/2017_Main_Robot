package org.team2168.subsystems;

import org.team2168.Robot;
import org.team2168.RobotMap;
import org.team2168.commands.elevator.DriveElevatorWithJoystick;

import edu.wpi.first.wpilibj.Spark;
import edu.wpi.first.wpilibj.SpeedController;
import edu.wpi.first.wpilibj.Victor;
import edu.wpi.first.wpilibj.command.Subsystem;

/**
 *Ball Agitator subsystem 
 *@author David
 */
public class Agitator extends Subsystem {

	private static Agitator instance = null;
	private static SpeedController agitatorMotor;

	private Agitator() {
		if(Robot.isPracticeRobot())
        	agitatorMotor = new Spark(RobotMap.AGITATOR_WHEEL);

		
	}

	/** 
	 * singleton getter for Ball Elevator 
	 * @return instance 
	 */
	public static Agitator getInstance(){
		if(instance==null)
			instance = new Agitator();
		return instance;
	}
	
    /**
     * @param speed -1.0 to 1.0, positive runs the ball up the elevator, negative down
     */
	public void driveAgitator(double speed){
		if(RobotMap.REVERSE_AGITATOR)
			speed = -speed;

		agitatorMotor.set(speed);
	}
	
	public void initDefaultCommand() {
        //setDefaultCommand(new DriveElevatorWithJoystick());
    }
}
