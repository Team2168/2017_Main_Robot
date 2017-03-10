package org.team2168.subsystems;

import org.team2168.Robot;
import org.team2168.RobotMap;
import org.team2168.commands.climber.DriveClimberWithConstant;
import org.team2168.commands.climber.DriveClimberWithJoystick;
import org.team2168.utils.consoleprinter.ConsolePrinter;

import edu.wpi.first.wpilibj.Spark;
import edu.wpi.first.wpilibj.command.Subsystem;

/**
 * Subsystem for the 2017 climber :)
 * @author John Karoul
 */
public class Climber extends Subsystem {

	private static Spark climberMotorLeft;
	private static Spark climberMotorRight;
	
	private static Climber instance = null;
	
	private Climber(){
		climberMotorLeft = new Spark(RobotMap.CLIMBER_MOTOR_LEFT);
		climberMotorRight = new Spark(RobotMap.CLIMBER_MOTOR_RIGHT);
		
		climberMotorLeft.setExpiration(0.1);
		climberMotorRight.setExpiration(0.1);
		
		climberMotorLeft.setSafetyEnabled(true);
		climberMotorRight.setSafetyEnabled(true);
		
		//TODO: FIX PDP Class Current Monitoring functions

		ConsolePrinter.putNumber("ClimberMotorLeftCurrent", () -> {return Robot.pdp.getChannelCurrent(RobotMap.CLIMBER_MOTOR_LEFT_PDP);}, true, true);
		ConsolePrinter.putNumber("ClimberMotorRightCurrent", () -> {return Robot.pdp.getChannelCurrent(RobotMap.CLIMBER_MOTOR_RIGHT_PDP);}, true, true);
		
		ConsolePrinter.putBoolean("ClimberLeftMotorTrip", () -> {return !Robot.pdp.isLeftHangerMotorTrip();}, true, false);
		ConsolePrinter.putBoolean("CllimberRightMotorTrip", () -> {return !Robot.pdp.isRighttHangerMotorTrip();}, true, false);

		
	}
	
	public static Climber getInstance(){
		if(instance == null)
				instance = new Climber();
		return instance;
	}
	
	/**
	 * 
	 * @param speed 1.0 to 0.0. Positive climbs up.
	 */
	public void driveClimber(double speed){
		driveLeftClimberMotor(speed);
		driveRightClimberMotor(speed);
	}
	
	private void driveLeftClimberMotor(double speed){
		//ONLY DRIVE THE CLIMBER MOTOR IN ONE DIRECTION
		if(speed < 0)
			speed = 0;
		
		if(RobotMap.CLIMB_MOTOR_REVERSE_LEFT)
			speed = -speed;
		
		climberMotorLeft.set(speed);
	}
		
	private void driveRightClimberMotor(double speed){
		//ONLY DRIVE THE CLIMBER MOTOR IN ONE DIRECTION
		if(speed < 0)
			speed = 0;
		
		if(RobotMap.CLIMB_MOTOR_REVERSE_RIGHT)
			speed = -speed;
		
		climberMotorRight.set(speed);
	}

    public void initDefaultCommand() {
        // Set the default command for a subsystem here.
        //setDefaultCommand(new DriveClimberWithJoystick());
    }
}

