package org.team2168.subsystems;

import org.team2168.Robot;
import org.team2168.RobotMap;
import org.team2168.commands.ballIntake.DriveIntakeWithJoystick;

import edu.wpi.first.wpilibj.Spark;
import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.command.Subsystem;

/**
 * Subsystem class for the ball intake
 */
public class BallIntakeRoller extends Subsystem {

	private static Talon intakeMotor;
	
	private static BallIntakeRoller instance = null;
	
	private BallIntakeRoller() {
		intakeMotor = new Talon(RobotMap.BALL_INTAKE_MOTOR);
	}
	
	public static BallIntakeRoller getInstance(){
		if(instance == null)
			instance = new BallIntakeRoller();
		
		return instance;
	}
	
	/**
	 * 
	 * @param speed 1.0 to -1.0, positive values bring balls into the robot.
	 */
	public void driveIntake(double speed) {
		if(Robot.isPracticeRobot() && RobotMap.REVERSE_BALL_INTAKE_WHEEL_PBOT)
			speed = -speed;
		else if(!Robot.isPracticeRobot() && RobotMap.REVERSE_BALL_INTAKE_WHEEL)
			speed = -speed;
		
		intakeMotor.set(speed);
	}
	
    public void initDefaultCommand() {
        //setDefaultCommand(new DriveIntakeWithJoystick());
    }
}

