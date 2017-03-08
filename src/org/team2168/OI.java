package org.team2168;

import org.team2168.commands.agitator.DriveAgitatorWithConstant;
import org.team2168.commands.ballIntake.DriveIntakeWithConstant;
import org.team2168.commands.ballIntake.LowerBallIntakeArm;
import org.team2168.commands.ballIntake.RaiseBallIntakeArm;
import org.team2168.commands.climber.DriveClimberWithConstant;
import org.team2168.commands.climber.DriveClimberWithConstantUntilCurrentLimit;
import org.team2168.commands.drivetrain.ShiftHigh;
import org.team2168.commands.drivetrain.ShiftLow;
import org.team2168.commands.drivetrain.PIDCommands.RotateXDistancePIDZZZCameraWithGyro;
import org.team2168.commands.elevator.DriveElevatorWithConstant;
import org.team2168.commands.flashlight.DisableFlashlight;
import org.team2168.commands.flashlight.EnableFlashlight;
import org.team2168.commands.gearintake.AutomaticGearIntake;
import org.team2168.commands.gearintake.DriveGearIntakeRollerWithConstant;
import org.team2168.commands.gearintake.LowerGearArm;
import org.team2168.commands.gearintake.RaiseGearArm;
import org.team2168.commands.indexer.DriveIndexerWithConstant;
import org.team2168.commands.shooter.DriveHoodDownWithButton;
import org.team2168.commands.shooter.DriveHoodUpWithButton;
import org.team2168.commands.shooter.DriveShooterWithConstant;
import org.team2168.commands.shooter.SetHoodToAngle;
import org.team2168.commands.shooter.PIDCommands.DriveShooterPIDSpeed;
import org.team2168.commands.shooter.PIDCommands.ShooterPIDPause;
import org.team2168.commands.turret.DriveTurretWithConstant;
import org.team2168.utils.F310;
import org.team2168.utils.consoleprinter.ConsolePrinter;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.buttons.Button;


/**
 * This class is the glue that binds the controls on the physical operator
 * interface to the commands and command groups that allow control of the robot.
 */
public class OI {
    //// CREATING BUTTONS
    // One type of button is a joystick button which is any button on a joystick.
    // You create one by telling it which joystick it's on and which button
    // number it is.
    // Joystick stick = new Joystick(port);
    // Button button = new JoystickButton(stick, buttonNumber);
    
    // There are a few additional built in buttons you can use. Additionally,
    // by subclassing Button you can create custom triggers and bind those to
    // commands the same as any other Button.
    
    //// TRIGGERING COMMANDS WITH BUTTONS
    // Once you have a button, it's trivial to bind it to a button in one of
    // three ways:
    
    // Start the command when the button is pressed and let it run the command
    // until it is finished as determined by it's isFinished method.
    // button.whenPressed(new ExampleCommand());
    
	
	
    // Run the command while the button is being held down and interrupt it once
    // the button is released.
    // button.whileHeld(new ExampleCommand());
    
    // Start the command when the button is released  and let it run the command
    // until it is finished as determined by it's isFinished method.
    // button.whenReleased(new ExampleCommand());
	private static OI instance = null;
	
	public static F310 driverJoystick = new F310(RobotMap.DRIVER_JOYSTICK);
	public static F310 operatorJoystick = new F310(RobotMap.OPERATOR_JOYSTICK);
	
	public static F310 testJoystick = new F310(RobotMap.COMMANDS_TEST_JOYSTICK);
	public static F310 pidTestJoystick = new F310(RobotMap.PID_TEST_JOYSTICK);

	/**
	 * Private constructor for singleton class which instantiates the OI object
	 */
	private OI() {
		
		
		//////////////Operator Joystick//////////////
		driverJoystick.ButtonLeftBumper().whenPressed(new ShiftLow());
		driverJoystick.ButtonRightBumper().whenPressed(new ShiftHigh());
		
		//////////////Operator Joystick//////////////
		
		//Boiler Shot
		operatorJoystick.ButtonX().whenPressed(new SetHoodToAngle(5));
		operatorJoystick.ButtonX().whenPressed(new DriveShooterPIDSpeed());
		
		//AirshipShot
		operatorJoystick.ButtonY().whenPressed(new SetHoodToAngle(180));
		operatorJoystick.ButtonY().whenPressed(new DriveShooterPIDSpeed(3825));

		//Fire
		operatorJoystick.ButtonA().whileHeld(new DriveElevatorWithConstant(-1.0));
		operatorJoystick.ButtonA().whileHeld(new DriveIndexerWithConstant(1.0));
		operatorJoystick.ButtonA().whileHeld(new DriveIntakeWithConstant(1.0));
		operatorJoystick.ButtonA().whileHeld(new DriveAgitatorWithConstant(1));
		
		//Kill Shooter
		operatorJoystick.ButtonB().whenPressed(new ShooterPIDPause());
		
		//turret
		operatorJoystick.ButtonRightDPad().whileHeld(new DriveTurretWithConstant(RobotMap.TURRET_MAX_DRIVE));
		operatorJoystick.ButtonLeftDPad().whileHeld(new DriveTurretWithConstant(-RobotMap.TURRET_MAX_DRIVE));
		operatorJoystick.ButtonUpDPad().whileHeld(new DriveHoodUpWithButton());
		operatorJoystick.ButtonDownDPad().whileHeld(new DriveHoodDownWithButton());
		
		
		//Gear Assembly
		operatorJoystick.ButtonRightTrigger().whileHeld(new AutomaticGearIntake());
		operatorJoystick.ButtonRightTrigger().whenReleased(new RaiseGearArm());
		operatorJoystick.ButtonStart().whenPressed(new LowerGearArm());
		operatorJoystick.ButtonStart().whenReleased(new RaiseGearArm());
		//operatorJoystick.isPressedButtonRightBumper()/whenPressed .... SCORE GEAR
		
		
		//Ball Intake Assembly
		operatorJoystick.ButtonLeftTrigger().whileHeld(new DriveIntakeWithConstant(1));
		operatorJoystick.ButtonLeftTrigger().whileHeld(new DriveAgitatorWithConstant(1));
		operatorJoystick.ButtonLeftTrigger().whenPressed(new LowerBallIntakeArm());
		operatorJoystick.ButtonLeftBumper().whenReleased(new RaiseBallIntakeArm());
		
		//Climber
		operatorJoystick.ButtonBack().whileHeld(new DriveClimberWithConstant(1.0));
		
		
		
		
		//////////////Test Joystick//////////////
		testJoystick.ButtonA().whileHeld(new DriveIntakeWithConstant(1.0));
		testJoystick.ButtonB().whileHeld(new DriveIntakeWithConstant(-1.0));
		testJoystick.ButtonX().whileHeld(new DriveClimberWithConstant(1.0));
		testJoystick.ButtonY().whileHeld(new DriveClimberWithConstant(-1.0));
		testJoystick.ButtonLeftDPad().whileHeld(new DriveElevatorWithConstant(1.0));
		testJoystick.ButtonRightDPad().whileHeld(new DriveElevatorWithConstant(-1.0));
		testJoystick.ButtonRightBumper().whileHeld(new DriveGearIntakeRollerWithConstant(1.0));
		testJoystick.ButtonLeftBumper().whileHeld(new DriveGearIntakeRollerWithConstant(-1.0));
		testJoystick.ButtonStart().whileHeld(new DriveIndexerWithConstant(1.0));
		testJoystick.ButtonBack().whileHeld(new DriveIndexerWithConstant(-1.0));
		//testJoystick.ButtonLeftBumper().whileHeld(new DriveShooterWithConstant(0.3));
		//testJoystick.ButtonRightBumper().whileHeld(new DriveShooterWithConstant(-0.3));
		testJoystick.ButtonLeftStick().whileHeld(new DriveTurretWithConstant(1.0));
		testJoystick.ButtonRightStick().whileHeld(new DriveTurretWithConstant(-1.0));
		
		//PIDJoystick
		//Shoot Far Preset (Y)
		pidTestJoystick.ButtonY().whenPressed(new DriveShooterPIDSpeed(6700));

		//Shoot Close Preset (X)
		pidTestJoystick.ButtonX().whenPressed(new DriveShooterPIDSpeed(4000));
		
		pidTestJoystick.ButtonA().whenPressed(new DriveShooterPIDSpeed());
		//Camera Shot Align (Start Button)
		//operatorJoystick.ButtonStart().whenPressed(new RotateXDistancePIDZZZCameraWithGyro(0, 0.4, 0.22, 0.5));
		pidTestJoystick.ButtonStart().whenPressed(new RotateXDistancePIDZZZCameraWithGyro(0, RobotMap.ROTATE_POSITION_CAMERA_MAX, RobotMap.ROTATE_POSITION_CAMERA_MIN, 0.5));
		pidTestJoystick.ButtonLeftBumper().whenPressed(new EnableFlashlight());
		pidTestJoystick.ButtonRightBumper().whenPressed(new DisableFlashlight());
	
	
		
		ConsolePrinter.putNumber("Left Stick Raw Value", () -> {return Robot.oi.driverJoystick.getLeftStickRaw_Y();}, true, false);
		ConsolePrinter.putNumber("Right Stick Raw Value", () -> {return Robot.oi.driverJoystick.getRightStickRaw_Y();}, true, false);

		ConsolePrinter.putNumber("Left Trigger Raw Value", () -> {return Robot.oi.driverJoystick.getLeftTriggerAxisRaw();}, true, false);
		ConsolePrinter.putNumber("Right Trigger Raw Value", () -> {return Robot.oi.driverJoystick.getRightTriggerAxisRaw();}, true, false);

		ConsolePrinter.putNumber("Operator Left Stick Raw Value", () -> {return Robot.oi.operatorJoystick.getLeftStickRaw_Y();}, true, false);
		ConsolePrinter.putNumber("Operator Right Stick Raw Value", () -> {return Robot.oi.operatorJoystick.getRightStickRaw_Y();}, true, false);

		ConsolePrinter.putNumber("Operator Left Trigger Raw Value", () -> {return Robot.oi.operatorJoystick.getLeftTriggerAxisRaw();}, true, false);
		ConsolePrinter.putNumber("Operator Right Trigger Raw Value", () -> {return Robot.oi.operatorJoystick.getRightTriggerAxisRaw();}, true, false);

	
	
	}

	/**
	 * Returns an instance of the Operator Interface.
	 * @return is the current OI object
	 */
	public static OI getInstance(){
		if(instance == null)
			instance = new OI();

		return instance;
	}

	/**
	 *  Method that sets that Left side of the drive train so that it drives with LeftStick Y
	 *  @author Krystina
	 */
	public static double getDriveTrainLeftJoystick () {
		return driverJoystick.getLeftStickRaw_Y();
	}

	/**
	 *  Method that sets that Right side of the drive train so that it drives with RightStick Y
	 *  @author Krystina
	 */
	public static double getDriveTrainRightJoystick () {
		return driverJoystick.getRightStickRaw_Y();
	}
	
	/**
	 *  Method that sets that Left side of the drive train so that it drives with Operator RightStick Y
	 *  @author Krystina
	 */
	public static double getDriveShooterJoystick() {
		return operatorJoystick.getRightStickRaw_Y();
	}
	
	public static double getDriveElevatorJoystick() {
		return operatorJoystick.getLeftStickRaw_Y();
	}
	
	public static double getDriveGearIntakeRollerJoystick() {
		return operatorJoystick.getLeftStickRaw_Y();
	}
	
	public static double getDriveConveryorJoystick() {
		return operatorJoystick.getLeftStickRaw_Y();
	}
	
	public static double getDriveBallIntakeJoystick() {
		return operatorJoystick.getLeftStickRaw_Y();
	}
	
	public static double getDriveShooterIndexerJoystick() {
		return operatorJoystick.getLeftStickRaw_Y();
	}
	
	public static double getDriveShooterPIDTestJoystick(){
		return pidTestJoystick.getRightStickRaw_Y();
	}
	
	public static double getDriveElevatorPIDTestJoystick(){
		return pidTestJoystick.getLeftStickRaw_Y();
	}
		
	public static double getDriveIndexerPIDTestJoystick(){
			return (pidTestJoystick.getRightTriggerAxisRaw() + pidTestJoystick.getLeftTriggerAxisRaw());
	}
	
	public static double getDriveTurretJoystick(){
		return pidTestJoystick.getLeftStickRaw_Y();
	}
	
	public static double  getDriveAgitatorJoystick(){
		return 0;
	}
}

