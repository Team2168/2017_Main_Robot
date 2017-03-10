package org.team2168.subsystems;

import org.team2168.Robot;
import org.team2168.RobotMap;
import org.team2168.commands.gearintake.RaiseGearArm;
import org.team2168.utils.consoleprinter.ConsolePrinter;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import edu.wpi.first.wpilibj.command.Subsystem;

/**
 *Subsystem code for the Gear Intake Arm Subsystem.
 *@author Elijah Reeds
 */
public class GearIntakeArm extends Subsystem {
	
	private DoubleSolenoid GearIntakeArmPiston;
	private DigitalInput GearIntakeHallEffectSensor;
	
	private static GearIntakeArm instance = null;
	
	/**
	 * WARNING! CONSTRUCTION AHEAD!
	 * @author Elijah Reeds
	 */
	private GearIntakeArm() {
		GearIntakeArmPiston = new DoubleSolenoid(RobotMap.GEAR_INTAKE_PISTON_EXTEND, RobotMap.GEAR_INTAKE_PISTON_RETRACT);
		GearIntakeHallEffectSensor = new DigitalInput(RobotMap.GEAR_INTAKE_ARM_HALL_EFECT);
		
		ConsolePrinter.putBoolean("Is Gear Arm Down", 
				() -> {return Robot.gearIntakeArm.isArmLowered();}, true, false);
		ConsolePrinter.putBoolean("Is Gear Arm Up", 
				() -> {return Robot.gearIntakeArm.isArmRaised();}, true, false);
	}
	
	/**
	 * Gets the instance of the GearIntakeArm.
	 * @return Returns the current instance of the Gear Intake Arm subsystem.
	 * @author Elijah Reeds
	 */
	public static GearIntakeArm getInstance(){
		if(instance == null)
			instance = new GearIntakeArm();
		
		return instance;
	}
	
	/**
	 * Raises the arm of the Gear Intake Subsystem.
	 * @author Elijah Reeds
	 */
	public void Raise(){
		GearIntakeArmPiston.set(Value.kReverse);
	}
	
	/**
	 * Lowers the arm of the Gear Intake Subsystem.
	 * @author Elijah Reeds
	 */
	public void Lower(){
		GearIntakeArmPiston.set(Value.kForward);
	}
	
	/**
	 * Gets the whether the arm is extended or not.
	 * @return Returns true if the arm of the Gear Intake is Raised based on the Hall Effect Sensor.
	 * @author Elijah Reeds
	 */
	public boolean isArmRaised(){
		return !GearIntakeHallEffectSensor.get();
	}
	
	/**
	 * Gets the whether the arm is retracted or not.
	 * @return Returns true if the arm of the Gear Intake is Lowered based on the Hall Effect Sensor.
	 * @author Elijah Reeds
	 */
	public boolean isArmLowered(){
		return GearIntakeHallEffectSensor.get();
	}
	
	
    // Put methods for controlling this subsystem
    // here. Call these from Commands.

    public void initDefaultCommand() {
        // Set the default command for a subsystem here.
        //setDefaultCommand(new MySpecialCommand());
    }
}

