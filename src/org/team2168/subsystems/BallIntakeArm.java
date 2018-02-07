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
	
	private DoubleSolenoid ballIntakeArmPiston;
	private DigitalInput BallIntakeHallEffectSensor;
	
	private static BallIntakeArm instance = null;
	
	private BallIntakeArm(){
			ballIntakeArmPiston = new DoubleSolenoid(RobotMap.PCM_CHANNEL,RobotMap.BALL_INTAKE_PISTON_EXTEND,
													 RobotMap.BALL_INTAKE_PISTON_RETRACT);

			if(Robot.isPracticeRobot())
			{
				BallIntakeHallEffectSensor = new DigitalInput(RobotMap.BALL_INTAKE_ARM_HALL_EFFECT_PBOT);
			}
			else
			{
				BallIntakeHallEffectSensor = new DigitalInput(RobotMap.BALL_INTAKE_ARM_HALL_EFFECT);
			}
			
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
		ballIntakeArmPiston.set(Value.kReverse);
	}
	
	public void Lower(){
		ballIntakeArmPiston.set(Value.kForward);
	}
	
	public boolean isArmRaised() {
		//When no sensor is installed, use last commanded value
		return ballIntakeArmPiston.get() == DoubleSolenoid.Value.kReverse;
		//When sensor is installed, use it
		//return !BallIntakeHallEffectSensor.get();
	}
	
	public boolean isArmLowered() {
		//When no sensor is installed, use last commanded value
		return ballIntakeArmPiston.get() == DoubleSolenoid.Value.kForward;
		//When sensor is installed, use it
		//return BallIntakeHallEffectSensor.get();
	}
	
	public void initDefaultCommand() {
	}
}