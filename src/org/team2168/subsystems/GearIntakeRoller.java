package org.team2168.subsystems;


import org.team2168.Robot;
import org.team2168.RobotMap;
import org.team2168.utils.consoleprinter.ConsolePrinter;
import org.team2168.commands.gearintake.*;

import edu.wpi.first.wpilibj.AnalogInput;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.Spark;
import edu.wpi.first.wpilibj.SpeedController;
import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.Victor;
import edu.wpi.first.wpilibj.command.Subsystem;

/**
 *Subsystem for the Gear Intake
 *@author Elijah Reeds
 */
public class GearIntakeRoller extends Subsystem {
	
	//create variables, acquire female dogs.
	private static SpeedController GearIntakeMotor;
	private static AnalogInput GearIntakeMotorIRSensor;
	
	private static GearIntakeRoller instance = null;
	
	/**
	 * Want to hear a construction joke? Sorry, I'm still working on it.
	 * @author Elijah Reeds
	 */
	private GearIntakeRoller() {
		if(Robot.isPracticeRobot())
		{
			GearIntakeMotor = new Victor(RobotMap.GEAR_INTAKE_MOTOR);
		}
		else
		{
			GearIntakeMotor  = new Spark(RobotMap.GEAR_INTAKE_MOTOR);
		}

		GearIntakeMotorIRSensor = new AnalogInput(RobotMap.GEAR_INTAKE_ROLLER_IR);
		
		//ConsolePrinter.putNumber("GearIntakeMotor1Current", () -> {return Robot.pdp.getChannelCurrent(RobotMap.ge));
		ConsolePrinter.putNumber("Raw Gear IR", () -> {return Robot.gearIntakeRoller.getIRVoltage();}, true, false);
		ConsolePrinter.putBoolean("IsGearPresent", () -> {return Robot.gearIntakeRoller.isGearPresent();}, true, false);


		
	}
	
	/**
	 * Creates the singleton instance of the GearIntakeRoller subsystem.
	 * @return Returns the current instance of the GearIntakeRoller.
	 * @author Elijah Reeds
	 */
	public static GearIntakeRoller getInstance(){
		if(instance == null)
			instance = new GearIntakeRoller();
		
		return instance;
	}
	
	/**
	 * Gets the voltage given by the Sharp IR sensor on the Gear Intake.
	 * @return the raw voltage from the gear presence sensor
	 */
	public double getIRVoltage(){
		return GearIntakeMotorIRSensor.getVoltage();
	}
	
	/**
	 * Automatically determines if the gear is present by contrasting the IR Sensor voltage to a threshold set in the RobotMap.java 
	 * @return true if a gear is present within the gear intake
	 * @author Elijah Reeds
	 */
	public boolean isGearPresent(){
		return (getIRVoltage() >= RobotMap.GEAR_INTAKE_IR_THRESHOLD);
	}
	
	/**
	 * Sets the speed of the roller.
	 * @param The speed in which you wish to spin the roller motor.
	 * @author Elijah Reeds
	 */
	public void setMotorSpeed(double speed){
		
		if(Robot.isPracticeRobot() && RobotMap.REVERSE_GEAR_INTAKE_WHEEL_PBOT)
			speed = -speed;
		else if(!Robot.isPracticeRobot() && RobotMap.REVERSE_GEAR_INTAKE_WHEEL)
			speed = -speed;
		
		GearIntakeMotor.set(speed);
	}

    public void initDefaultCommand() {
        // Set the default command for a subsystem here.
        //setDefaultCommand(new MySpecialCommand());
    	//setDefaultCommand(new DriveGearIntakeRollerWithJoystick());
    }
}

