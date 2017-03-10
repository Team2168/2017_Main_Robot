package org.team2168.subsystems;

import org.team2168.Robot;
import org.team2168.RobotMap;
import org.team2168.commands.ballIntake.RaiseBallIntakeArm;
import org.team2168.utils.consoleprinter.ConsolePrinter;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import edu.wpi.first.wpilibj.command.Subsystem;

public class BallIntakeArm extends Subsystem {
	
	private DoubleSolenoid BallIntakeArmPiston;
	private DigitalInput BallIntakeHallEffectSensor;
	
	private static BallIntakeArm instance = null;
	
	private BallIntakeArm(){
			BallIntakeArmPiston = new DoubleSolenoid(RobotMap.BALL_INTAKE_PISTON_EXTEND,
													 RobotMap.BALL_INTAKE_PISTON_RETRACT);
			BallIntakeHallEffectSensor = new DigitalInput(RobotMap.BALL_INTAKE_ARM_HALL_EFFECT);
			
			ConsolePrinter.putBoolean("Ball Intake Arm Raised", 
					() -> {return Robot.ballIntakeArm.isArmRaised();}, true, false);
			ConsolePrinter.putBoolean("Ball Intake Arm Lowered", 
					() -> {return Robot.ballIntakeArm.isArmLowered();}, true, false);
	}
	
	public static BallIntakeArm getInstance(){
		if(instance == null)
			instance = new BallIntakeArm();
		
		return instance;
	}
	
	public void Raise(){
		BallIntakeArmPiston.set(Value.kReverse);
	}
	
	public void Lower(){
		BallIntakeArmPiston.set(Value.kForward);
	}
	
	public boolean isArmRaised(){
		return !BallIntakeHallEffectSensor.get();
	}
	
	public boolean isArmLowered(){
		return BallIntakeHallEffectSensor.get();
	}
	
	public void initDefaultCommand() {
	}
}