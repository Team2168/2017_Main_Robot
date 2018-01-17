package org.team2168;

import org.team2168.commands.agitator.DriveAgitatorWithConstant;
import org.team2168.commands.auto.DriveStraightAndScoreCenter;
import org.team2168.commands.auto.DriveStraightAndScoreRight;
import org.team2168.commands.drivetrain.PIDCommands.DriveSraightXDistancePIDZZZCameraWithGyro;
import org.team2168.commands.ballIntake.DriveIntakeWithConstant;
import org.team2168.commands.ballIntake.LowerBallIntakeArm;
import org.team2168.commands.ballIntake.RaiseBallIntakeArm;
import org.team2168.commands.ballIntake.ToggleBallIntakeArm;
import org.team2168.commands.climber.DriveClimberWithConstant;
import org.team2168.commands.climber.DriveClimberWithConstantUntilCurrentLimit;
import org.team2168.commands.drivetrain.DriveWithJoystickEmergencyJoystick;
import org.team2168.commands.drivetrain.ShiftHigh;
import org.team2168.commands.drivetrain.ShiftLow;
import org.team2168.commands.drivetrain.PIDCommands.RotateXDistancePIDZZZ;
import org.team2168.commands.drivetrain.PIDCommands.RotateXDistancePIDZZZCameraWithGyro;
import org.team2168.commands.elevator.DriveElevatorWithConstant;
import org.team2168.commands.flashlight.DisableFlashlight;
import org.team2168.commands.flashlight.EnableFlashlight;
import org.team2168.commands.gearintake.AutomaticGearIntake;
import org.team2168.commands.gearintake.AutomaticGearIntakeDANGEROUS;
import org.team2168.commands.gearintake.DriveGearIntakeRollerWithConstant;
import org.team2168.commands.gearintake.LowerGearArm;
import org.team2168.commands.gearintake.LowerGearArmDANGEROUS;
import org.team2168.commands.gearintake.RaiseGearArm;
import org.team2168.commands.indexer.DriveIndexerWithConstant;
import org.team2168.commands.shooter.DriveHoodDownWithButton;
import org.team2168.commands.shooter.DriveHoodUpWithButton;
import org.team2168.commands.shooter.DriveShooterWithConstant;
import org.team2168.commands.shooter.IndexSingleBall;
import org.team2168.commands.shooter.SetHoodToAngle;
import org.team2168.commands.shooter.PIDCommands.DriveShooterPIDSpeed;
import org.team2168.commands.shooter.PIDCommands.ShooterPIDPause;
import org.team2168.commands.turret.DriveTurretWithConstant;
import org.team2168.commands.turret.PIDCommands.EnableTurretAnglePIDZZZ;
import org.team2168.commands.turret.PIDCommands.RotateTurretAnglePIDZZZ;
import org.team2168.commands.turret.PIDCommands.TurretToAnglePIDZZZCameraWithPot;
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
	
	public static F310 driverOperatorEJoystick = new F310(RobotMap.DRIVER_OPERATOR_E_BACKUP);
	
	public static F310 testJoystick = new F310(RobotMap.COMMANDS_TEST_JOYSTICK);
	public static F310 pidTestJoystick = new F310(RobotMap.PID_TEST_JOYSTICK);

	/**
	 * Private constructor for singleton class which instantiates the OI object
	 */
	private OI() {
		
		
		//////////////Driver Joystick//////////////
		driverJoystick.ButtonStart().whenPressed(new ShiftLow());
		driverJoystick.ButtonA().whenPressed(new ShiftHigh());
		
		//////////////Operator Joystick//////////////

		
		//Emergency Gear Pickup
		operatorJoystick.ButtonX().whenPressed(new AutomaticGearIntakeDANGEROUS());
		operatorJoystick.ButtonX().whenReleased(new RaiseGearArm());

		//Blue Hopper Shot Angle
		operatorJoystick.ButtonY().whenPressed(new SetHoodToAngle(101.16));
		operatorJoystick.ButtonY().whenPressed(new RotateTurretAnglePIDZZZ(-79.7, 0.7, 0.2, 0.2,true));
		operatorJoystick.ButtonY().whenPressed(new DriveShooterPIDSpeed(3000));

		//Fire
		operatorJoystick.ButtonA().whileHeld(new DriveElevatorWithConstant(0.45));
		operatorJoystick.ButtonA().whileHeld(new DriveIndexerWithConstant(0.75));
		operatorJoystick.ButtonA().whileHeld(new DriveIntakeWithConstant(0.75));
		operatorJoystick.ButtonA().whileHeld(new DriveAgitatorWithConstant(0.75));

		
		//Kill Shooter
		operatorJoystick.ButtonB().whenPressed(new ShooterPIDPause());
		operatorJoystick.ButtonB().whenPressed(new DriveTurretWithConstant(0.0));
		
		//turret
		operatorJoystick.ButtonRightDPad().whileHeld(new DriveTurretWithConstant(RobotMap.TURRET_MAX_DRIVE));
		operatorJoystick.ButtonLeftDPad().whileHeld(new DriveTurretWithConstant(-RobotMap.TURRET_MAX_DRIVE));
		operatorJoystick.ButtonUpDPad().whileHeld(new DriveHoodUpWithButton());
		operatorJoystick.ButtonDownDPad().whileHeld(new DriveHoodDownWithButton());
		
		
		//Gear Assembly
		operatorJoystick.ButtonRightTrigger().whileHeld(new AutomaticGearIntake());
		operatorJoystick.ButtonRightTrigger().whenReleased(new RaiseGearArm());
		operatorJoystick.ButtonRightBumper().whenPressed(new LowerGearArmDANGEROUS());
		operatorJoystick.ButtonRightBumper().whenReleased(new RaiseGearArm());
		//operatorJoystick.isPressedButtonRightBumper()/whenPressed .... SCORE GEAR
		
		
		//Spit Gear Intake
		operatorJoystick.ButtonStart().whenPressed(new LowerGearArmDANGEROUS());
		operatorJoystick.ButtonStart().whileHeld(new DriveGearIntakeRollerWithConstant(-1.0));
		operatorJoystick.ButtonStart().whenReleased(new RaiseGearArm());
		
		
		//Ball Intake Assembly
		operatorJoystick.ButtonLeftTrigger().whenPressed(new LowerBallIntakeArm());
//		operatorJoystick.ButtonLeftTrigger().whileHeld(new DriveIntakeWithConstant(1.0));
//		operatorJoystick.ButtonLeftTrigger().whileHeld(new DriveAgitatorWithConstant(1.0));
		operatorJoystick.ButtonLeftBumper().whenReleased(new RaiseBallIntakeArm());
//		operatorJoystick.ButtonLeftBumper().whileHeld(new DriveIntakeWithConstant(-1.0));
//		operatorJoystick.ButtonLeftBumper().whileHeld(new DriveAgitatorWithConstant(-1.0));
		
		//Climber
		operatorJoystick.ButtonBack().whileHeld(new DriveClimberWithConstant(1.0));
		   
		//Boiler Shot Angle
		operatorJoystick.ButtonLeftStick().whenPressed(new SetHoodToAngle(45.0));
		operatorJoystick.ButtonLeftStick().whenPressed(new RotateTurretAnglePIDZZZ(0.0, 0.7, 0.2, 0.2));
		operatorJoystick.ButtonLeftStick().whenPressed(new DriveShooterPIDSpeed(3600));
		
		//Toggle Hopper
		//operatorJoystick.ButtonY().whileHeld(new ToggleBallIntakeArm());
		
		
		
		
		
		//////////////Driver Operator Emergency Joystick//////////////
			
		//Boiler Shot
		//	driverOperatorEJoystick.ButtonX().whenPressed(new DriveShooterPIDSpeed());
		
		//Emergency Gear Pickup
		driverOperatorEJoystick.ButtonX().whenPressed(new AutomaticGearIntakeDANGEROUS());
		driverOperatorEJoystick.ButtonX().whenReleased(new RaiseGearArm());
	
		//Red Hopper Shot Angle
		driverOperatorEJoystick.ButtonY().whenPressed(new SetHoodToAngle(101.16));
		driverOperatorEJoystick.ButtonY().whenPressed(new RotateTurretAnglePIDZZZ(79.7, 0.7, 0.2, 0.2,true));
		driverOperatorEJoystick.ButtonY().whenPressed(new DriveShooterPIDSpeed(3000));
	
		//Fire
		driverOperatorEJoystick.ButtonA().whileHeld(new DriveElevatorWithConstant(0.75));
		driverOperatorEJoystick.ButtonA().whileHeld(new DriveIndexerWithConstant(0.75));
		driverOperatorEJoystick.ButtonA().whileHeld(new DriveIntakeWithConstant(0.75));
		driverOperatorEJoystick.ButtonA().whileHeld(new DriveAgitatorWithConstant(0.75));
	
		
		//Kill Shooter
		driverOperatorEJoystick.ButtonB().whenPressed(new ShooterPIDPause());
		driverOperatorEJoystick.ButtonB().whenPressed(new DriveTurretWithConstant(0.0));
		
		//turret
		driverOperatorEJoystick.ButtonRightDPad().whileHeld(new DriveTurretWithConstant(RobotMap.TURRET_MAX_DRIVE));
		driverOperatorEJoystick.ButtonLeftDPad().whileHeld(new DriveTurretWithConstant(-RobotMap.TURRET_MAX_DRIVE));
		driverOperatorEJoystick.ButtonUpDPad().whileHeld(new DriveHoodUpWithButton());
		driverOperatorEJoystick.ButtonDownDPad().whileHeld(new DriveHoodDownWithButton());
		
		
		//Gear Assembly
		driverOperatorEJoystick.ButtonRightTrigger().whileHeld(new AutomaticGearIntake());
		driverOperatorEJoystick.ButtonRightTrigger().whenReleased(new RaiseGearArm());
		driverOperatorEJoystick.ButtonRightBumper().whenPressed(new LowerGearArmDANGEROUS());
		driverOperatorEJoystick.ButtonRightBumper().whenReleased(new RaiseGearArm());
		//driverOperatorEJoystick.isPressedButtonRightBumper()/whenPressed .... SCORE GEAR
		
		
		//Spit Gear Intake
		driverOperatorEJoystick.ButtonStart().whenPressed(new LowerGearArmDANGEROUS());
		driverOperatorEJoystick.ButtonStart().whileHeld(new DriveGearIntakeRollerWithConstant(-1.0));
		driverOperatorEJoystick.ButtonStart().whenReleased(new RaiseGearArm());
		
		
		//Ball Intake Assembly
		driverOperatorEJoystick.ButtonLeftTrigger().whenPressed(new LowerBallIntakeArm());
	//	driverOperatorEJoystick.ButtonLeftTrigger().whileHeld(new DriveIntakeWithConstant(1.0));
	//	driverOperatorEJoystick.ButtonLeftTrigger().whileHeld(new DriveAgitatorWithConstant(1.0));
		driverOperatorEJoystick.ButtonLeftBumper().whenReleased(new RaiseBallIntakeArm());
	//	driverOperatorEJoystick.ButtonLeftBumper().whileHeld(new DriveIntakeWithConstant(-1.0));
	//	driverOperatorEJoystick.ButtonLeftBumper().whileHeld(new DriveAgitatorWithConstant(-1.0));
		
		//Climber
		driverOperatorEJoystick.ButtonBack().whileHeld(new DriveClimberWithConstant(1.0));
		   
		//Boiler Shot Angle
		driverOperatorEJoystick.ButtonLeftStick().whenPressed(new SetHoodToAngle(45.0));
		driverOperatorEJoystick.ButtonLeftStick().whenPressed(new RotateTurretAnglePIDZZZ(0.0, 0.7, 0.2, 0.2));
		driverOperatorEJoystick.ButtonLeftStick().whenPressed(new DriveShooterPIDSpeed(3600));
			
		
		driverOperatorEJoystick.ButtonRightStick().whenPressed(new DriveWithJoystickEmergencyJoystick());
		
		
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
		
		
		//////////////PID Test Joystick//////////////
//		pidTestJoystick.ButtonA().whenPressed(new DriveStraightAndScoreCenter());
		pidTestJoystick.ButtonY().whenPressed(new RotateXDistancePIDZZZ(25, 0.8, 0.20, 0.1));
		pidTestJoystick.ButtonX().whenPressed(new RotateXDistancePIDZZZ(10, 0.8, 0.20, 0.1));
		pidTestJoystick.ButtonA().whenPressed(new RotateXDistancePIDZZZ(-10, 0.8, 0.20, 0.1));
		pidTestJoystick.ButtonB().whenPressed(new RotateXDistancePIDZZZ(-25, 0.8, 0.20, 0.1));
//		pidTestJoystick.ButtonX().whenPressed(new RotateXDistancePIDZZZCameraWithGyro(0, RobotMap.ROTATE_POSITION_CAMERA_MAX, RobotMap.ROTATE_POSITION_CAMERA_MIN, 0.2));
//		pidTestJoystick.ButtonY().whenPressed(new DriveStraightAndScoreRight());
		//Gear Assembly
//		pidTestJoystick.ButtonRightTrigger().whileHeld(new AutomaticGearIntake());
//		pidTestJoystick.ButtonRightTrigger().whenReleased(new RaiseGearArm());
//		pidTestJoystick.ButtonRightBumper().whenPressed(new LowerGearArm());
//		pidTestJoystick.ButtonRightBumper().whenReleased(new RaiseGearArm());
		
		//PIDJoystick
//		//Turret to target with camera (Y)
//		pidTestJoystick.ButtonY().whenPressed(new EnableTurretAnglePIDZZZ(0, 0.7, 0.1, 0.2,true));
//
//		//Turret to 20 off target with camera (X)
//		pidTestJoystick.ButtonX().whenPressed(new EnableTurretAnglePIDZZZ(60, 0.7, 0.1, 0.2,true));
//		
//		//Turret -20 off target with camera (A)
//		pidTestJoystick.ButtonA().whenPressed(new EnableTurretAnglePIDZZZ(-60, 0.7, 0.1, 0.2,true));
//		
//		//Toggle ball intake (Right Bumper)
//		pidTestJoystick.ButtonRightBumper().whileHeld(new ToggleBallIntakeArm());
//		
//		//Turret (B)
//		pidTestJoystick.ButtonB().whenPressed(new TurretToAnglePIDZZZCameraWithPot(0, 0.7, 0.1, 0.2));
//		
//		//Turret movement
//		pidTestJoystick.ButtonRightDPad().whileHeld(new DriveTurretWithConstant(RobotMap.TURRET_MAX_DRIVE));
//		pidTestJoystick.ButtonLeftDPad().whileHeld(new DriveTurretWithConstant(-RobotMap.TURRET_MAX_DRIVE));
//		pidTestJoystick.ButtonUpDPad().whileHeld(new DriveHoodUpWithButton());
//		pidTestJoystick.ButtonDownDPad().whileHeld(new DriveHoodDownWithButton());
		

		//pidTestJoystick.ButtonStart().whenPressed(new RotateXDistancePIDZZZCameraWithGyro(0, RobotMap.ROTATE_POSITION_CAMERA_MAX, RobotMap.ROTATE_POSITION_CAMERA_MIN, 0.5));
		//pidTestJoystick.ButtonLeftBumper().whenPressed(new EnableFlashlight());
		//pidTestJoystick.ButtonRightBumper().whenPressed(new DisableFlashlight());

	
	
		
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

