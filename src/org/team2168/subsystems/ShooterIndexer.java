package org.team2168.subsystems;

import org.team2168.Robot;
import org.team2168.RobotMap;
import org.team2168.commands.shooter.DriveShooterIndexerWithJoystick;
import org.team2168.utils.consoleprinter.ConsolePrinter;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Spark;
import edu.wpi.first.wpilibj.command.Subsystem;

/**
 * Subsystem class for the Shooter Indexer
 * @author Wen Baid
 */
public class ShooterIndexer extends Subsystem {

    // Put methods for controlling this subsystem
    // here. Call these from Commands.
	private static Spark indexerMotor;
	private static DigitalInput upperBallPresentSensor;
	private static DigitalInput lowerBallPresentSensor;
	
	private static ShooterIndexer instance = null;
	
	/**
	 * Default constructor for Shooter Indexer subsystem
	 */
	private ShooterIndexer() {
		indexerMotor = new Spark(RobotMap.INDEXER_WHEEL);
		
		if(Robot.isPracticeRobot())
		{
			upperBallPresentSensor = new DigitalInput(RobotMap.INDEXER_UPPER_BALL_PRESENT_PBOT);
			lowerBallPresentSensor = new DigitalInput(RobotMap.INDEXER_LOWER_BALL_PRESENT_PBOT);
		}
		else
		{
			upperBallPresentSensor = new DigitalInput(RobotMap.INDEXER_UPPER_BALL_PRESENT);
			lowerBallPresentSensor = new DigitalInput(RobotMap.INDEXER_LOWER_BALL_PRESENT);
		}
		
		ConsolePrinter.putBoolean("Indexer Upper Sensor", 
				() -> {return Robot.shooterIndexer.isUpperSensorActive();}, true, false);
		ConsolePrinter.putBoolean("Indexer Lower Sensor", 
				() -> {return Robot.shooterIndexer.isLowerSensorActive();}, true, false);
	}
	
	/**
	 * Singleton getter for ShooterIndexer
	 * @return ShooterIndexer singleton
	 */
	public static ShooterIndexer getInstance(){
		if(instance == null)
			instance = new ShooterIndexer();
		
		return instance;
	}
	
	/**
	 * Sets the speed of the indexer motor
	 * @param speed 1.0 to -1.0. Positive values bring the balls up into the shooter.
	 */
	public void setSpeed(double speed) {
		if(RobotMap.REVERSE_INDEXER)
			speed = -speed;

		indexerMotor.set(speed);
	}
	
	/**
	 * Checks if the upper ball sensor is activated
	 * @return if ball is present (true=present, false=not present)
	 */
	private boolean isUpperSensorActive() {
		return !upperBallPresentSensor.get();
	}
	
	/**
	 * Checks if the lower ball sensor is activated
	 * @return if ball is present (true=present, false=not present)
	 */
	private boolean isLowerSensorActive() {
		return !lowerBallPresentSensor.get();
	}
	
	/**
	 * Checks if ball is present
	 * @return if ball is present (true=present, false=not present)
	 */
	public boolean isBallPresent() {
		if(isUpperSensorActive() || isLowerSensorActive()) {
			//Either sensor sees a ball
			return true;
		}
		else return false;
	}
	
	/**
	 * Checks if ball is absent
	 * @return if ball is absent (true=present, false=not present)
	 */
	public boolean isBallAbsent() {
		if(!isUpperSensorActive() && !isLowerSensorActive()) {
			//Neither sensor sees a ball
			return true;
		}
		else return false;
	}
	
    public void initDefaultCommand() {
        // Set the default command for a subsystem here.
        //setDefaultCommand(new DriveShooterIndexerWithJoystick());
    }
}

