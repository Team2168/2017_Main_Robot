package org.team2168.subsystems;

import org.team2168.Robot;
import org.team2168.RobotMap;
import org.team2168.PID.sensors.TCPCamSensor;
import org.team2168.commands.turret.DriveTurretWithJoystick;
import org.team2168.utils.LinearInterpolator;
import org.team2168.utils.consoleprinter.ConsolePrinter;

import edu.wpi.first.wpilibj.AnalogInput;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.SafePWM;
import edu.wpi.first.wpilibj.Spark;
import edu.wpi.first.wpilibj.SpeedController;
import edu.wpi.first.wpilibj.Victor;
import edu.wpi.first.wpilibj.command.Subsystem;

/**
 * Subsystem class for Turret
 * @author Wen Baid
 */
public class Turret extends Subsystem {

    private static SpeedController turretMotor;
    private static AnalogInput potentiometer;
    private static DigitalInput limitSwitchRight;
    private static DigitalInput limitSwitchLeft;
    
    public TCPCamSensor tcpCamSensor;

    private static Turret instance = null;
    
    private static LinearInterpolator turretInterpolator;
    //TODO get these values plez format for points: (volts, degrees)
    private double[][] turretRange = {{-1.0,-100.0},
    		                          {0.0,0.0},
    		                          {1.0,100.0}};
    
    /**
     * Default constructor for Turret subsystem
     */
    private Turret() {
    	if(Robot.isPracticeRobot())
		{
        	turretMotor = new Victor(RobotMap.TURRET_MOTOR);
		}
		else
		{
	    	turretMotor = new Spark(RobotMap.TURRET_MOTOR);
		}
    	
    	potentiometer = new AnalogInput(RobotMap.TURRET_POTENTIOMETER);
    	limitSwitchRight = new DigitalInput(RobotMap.TURRET_LIMIT_SWITCH_RIGHT);
    	limitSwitchLeft = new DigitalInput(RobotMap.TURRET_LIMIT_SWITCH_LEFT);
    	turretInterpolator = new LinearInterpolator(turretRange);
    	
    	tcpCamSensor = new TCPCamSensor("BoilerCam", RobotMap.BOILER_CAMERA_LISTEN_PORT, RobotMap.CAMERA_SENSOR_PERIOD);

    	
    	
    	//For to be the very safest and to not break robot
    	((SafePWM) turretMotor).setExpiration(0.1);
    	((SafePWM) turretMotor).setSafetyEnabled(true);
    	
		ConsolePrinter.putBoolean("Turret Right Limit Switch", 
				() -> {return Robot.turret.isLimitSwitchRightActive();}, true, false);
		ConsolePrinter.putBoolean("Turret Left Limit Switch", 
				() -> {return Robot.turret.isLimitSwitchLeftActive();}, true, false);
		ConsolePrinter.putNumber("Turret Potentiometer", 
				() -> {return Robot.turret.getPosition();}, true, false);
    }

    /**
     * Returns turret singleton object
     * @return turret singleton object
     */
	public static Turret getInstance() {
		if(instance == null)
			instance = new Turret();
		
		return instance;
	}

	/**
	 * Sets the speed of the motor
	 * @param speed of -1.0 (left) to 1.0 (right)
	 */
	public void setSpeed(double speed) {
		if((speed > 0 && isLimitSwitchRightActive())||(speed < 0 && isLimitSwitchLeftActive())){
			turretMotor.set(0);
		}
		else {
			turretMotor.set(speed);
		}
	}

	/**
	 * Returns the current position of the turret
	 * @param x is voltage
	 * @return Potentiometer position
	 */
	public double getPosition() {
		return turretInterpolator.interpolate(potentiometer.getVoltage());
	}

	/**
	 * Returns status of limit switch
	 * @return true if pressed, false if unpressed
	 */
	public boolean isLimitSwitchRightActive() {
		return !limitSwitchRight.get();
	}

	/**
	 * Returns status of limit switch
	 * @return true if pressed, false if unpressed
	 */
	public boolean isLimitSwitchLeftActive() {
		return !limitSwitchLeft.get();
	}

    public void initDefaultCommand() {
       // setDefaultCommand(new DriveTurretWithJoystick());
    }
}
