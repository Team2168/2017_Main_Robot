
package org.team2168;

import org.team2168.subsystems.*;
import org.team2168.commands.auto.*;
import org.team2168.commands.drivetrain.DriveWithJoystick;
import org.team2168.commands.pneumatics.StartCompressor;
import org.team2168.utils.Debouncer;
import org.team2168.utils.PowerDistribution;
import org.team2168.utils.consoleprinter.ConsolePrinter;

import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.DigitalOutput;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Scheduler;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the IterativeRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the manifest file in the resource
 * directory.
 */
public class Robot extends IterativeRobot {

	public static BallConvelator ballElevator;
	public static BallIntakeArm ballIntakeArm;
	public static BallIntakeRoller ballIntakeRoller;
	public static Climber climber;
	public static Drivetrain drivetrain;
	public static DrivetrainShifter drivetrainShifter;
	public static Flashlight flashlight;
	public static GearIntakeArm gearIntakeArm;
	public static GearIntakeRoller gearIntakeRoller;
	public static Pneumatics pneumatics;
	public static ShooterHood shooterHood;
	public static ShooterIndexer shooterIndexer;
	public static ShooterWheel shooterWheel;
	public static Turret turret;
	
	public static DigitalOutput tx1TurnOn;
	public static DigitalInput tx1OnStatus;
	
	static boolean autoMode;
    private static boolean matchStarted = false;
    public static int gyroReinits;
	private double lastAngle;
	private Debouncer gyroDriftDetector = new Debouncer(1.0);
	public static boolean gyroCalibrating = false;
	private boolean lastGyroCalibrating = false;
	private double curAngle = 0.0;
    
    public static DriverStation driverstation;
	
	
	public static PowerDistribution pdp;
	Compressor comp;

	public static OI oi;
	
    static Command autonomousCommand;
    public static SendableChooser<Command> autoChooser;
    
    static int controlStyle;
    public static SendableChooser<Number> controlStyleChooser;

    /**
     * This function is run when the robot is first started up and should be
     * used for any initialization code.
     */
    public void robotInit() {
    	ConsolePrinter.init();
    	ConsolePrinter.setRate(RobotMap.CONSOLE_PRINTER_LOG_RATE_MS);

    	// instantiate the commands used for the autonomous period
    	ballElevator = BallConvelator.getInstance();
    	ballIntakeArm = BallIntakeArm.getInstance();
    	ballIntakeRoller = BallIntakeRoller.getInstance();
    	climber = Climber.getInstance();
    	drivetrain = Drivetrain.getInstance();
    	drivetrainShifter = DrivetrainShifter.getInstance();
    	flashlight = Flashlight.getInstance();
    	gearIntakeArm = GearIntakeArm.getInstance();
    	gearIntakeRoller = GearIntakeRoller.getInstance();
    	pneumatics = Pneumatics.getInstance();
    	shooterHood = ShooterHood.getInstance();
    	shooterIndexer = ShooterIndexer.getInstance();
    	shooterWheel = ShooterWheel.getInstance();
    	turret = Turret.getInstance();
    	
    	tx1TurnOn = new DigitalOutput(RobotMap.TX1_TURN_ON);
    	tx1OnStatus = new DigitalInput(RobotMap.TX1_ON_STATUS);
    	
        oi = OI.getInstance();
        
        //run compressor
        new StartCompressor();

        autoSelectInit();
        controlStyleSelectInit();
     
		pdp = new PowerDistribution(RobotMap.PDPThreadPeriod);
		pdp.startThread();
		
        drivetrain.calibrateGyro();
        
        SmartDashboard.putData("Autonomous Mode Chooser", Robot.autoChooser);
        SmartDashboard.putData("Control Style Chooser", Robot.controlStyleChooser);
		//ConsolePrinter.putData("Autonomous Mode Chooser", () -> {return Robot.autoChooser;}, true, false);
		ConsolePrinter.putString("AutoName", () -> {return Robot.getAutoName();}, true, false);
		ConsolePrinter.putString("Control Style Name", () -> {return Robot.getControlStyleName();}, true, false);
		//ConsolePrinter.putBoolean("isPracticeBot", Robot.isPracticeRobot());
		ConsolePrinter.putNumber("gameClock", () -> {return DriverStation.getInstance().getMatchTime();}, true, false);
        ConsolePrinter.putNumber("Robot Pressure", () -> {return Robot.pneumatics.getPSI();}, true, false);
        
        ConsolePrinter.putBoolean("TX1TurnOn", () -> {return getTX1TurnOn();}, true, false);
        ConsolePrinter.putBoolean("TX1OnStatus", () -> {return getTX1OnStatus();}, true, false);
        
        ConsolePrinter.startThread();
        System.out.println("Robot Done Loading");
    }
    
    /**
	 * Get the name of an autonomous mode command.
	 * @return the name of the auto command.
	 */
	public static String getAutoName() {
		if (autonomousCommand != null) {
			return autonomousCommand.getName();
		} else {
			return "None";
		}
	}
	
    /**
	 * Get the name of an control style.
	 * @return the name of the control style.
	 */
	public static String getControlStyleName() {

		if(controlStyle==0) {
			return ("Tank Drive");
		}
		if(controlStyle==1) {
			return ("Gun Style");
		}
		if(controlStyle==2) {
			return ("Arcade Drive");
		}
		if(controlStyle==3) {
			return ("GTA Drive");
		}
		else {
			return null;
		}		
				
	}
    
    /**
     * Adds control styles to the selector
     */
    public void controlStyleSelectInit(){
    	controlStyleChooser = new SendableChooser<>();
    	controlStyleChooser.addDefault("Tank Drive", 0);
    	controlStyleChooser.addObject("Gun Style Controller", 1);
    	controlStyleChooser.addObject("Arcade Drive", 2);
    	controlStyleChooser.addObject("GTA Drive", 3);
    }
	
	/**
     * This method is called once each time the robot enters Disabled mode.
     * You can use it to reset any subsystem information you want to clear when
	 * the robot is disabled.
     */
    public void disabledInit(){
    		autoMode = false;
    		matchStarted = false;
    }
    
	public void disabledPeriodic() {

        SmartDashboard.putNumber("GunStyleXValueMakingThisLongSoWeCanFindIt", Robot.oi.driverJoystick.getLeftStickRaw_X());
        SmartDashboard.putNumber("GunStyleXInterpolatedValueMakingThisLongSoWeCanFindIt", Robot.drivetrain.getGunStyleXValue());
		
		// Kill all active commands
		Scheduler.getInstance().run();

		turnTX1On();
		
		autoMode = false;
		
		// Check to see if the gyro is drifting, if it is re-initialize it.
		gyroReinit();
	}

    public void autonomousInit() {
    	
    	autoMode = true;
    	
		matchStarted = true;
		drivetrain.stopGyroCalibrating();
		drivetrain.resetGyro();
    	
        // schedule the autonomous command (example)
        if (autonomousCommand != null) autonomousCommand.start();
    }

    /**
     * This function is called periodically during autonomous
     */
    public void autonomousPeriodic() {
        Scheduler.getInstance().run();
    }

    public void teleopInit() {
    	
    	autoMode = false;
    	
		matchStarted = true;
		drivetrain.stopGyroCalibrating();
    	
    	// This makes sure that the autonomous stops running when
        // teleop starts running. If you want the autonomous to 
        // continue until interrupted by another command, remove
        // this line or comment it out.
        if (autonomousCommand != null) autonomousCommand.cancel();
    
        controlStyle = (int) controlStyleChooser.getSelected();
    	// Select the control style
    }

    /**
     * This function is called periodically during operator control
     */
    public void teleopPeriodic() {
    	
    	autoMode = false;
        Scheduler.getInstance().run();
        
        SmartDashboard.putNumber("GunStyleXValueMakingThisLongSoWeCanFindIt", Robot.oi.driverJoystick.getLeftStickRaw_X());
        SmartDashboard.putNumber("GunStyleXInterpolatedValueMakingThisLongSoWeCanFindIt", Robot.drivetrain.getGunStyleXValue());
        
    }
    
    /**
     * This function is called periodically during test mode
     */
    public void testPeriodic() {
        LiveWindow.run();
    }
    
    /**
     * Adds the autos to the selector
     */
    public void autoSelectInit() {
        autoChooser = new SendableChooser<Command>();
        autoChooser.addDefault("Do Nothing", new DoNothing());
        //  autoChooser.addObject("Do Something", new DoSomething());
    }
    
    /**
	 * Method which checks to see if gyro drifts and resets the gyro. Call this
	 * in a loop.
	 */
	private void gyroReinit() {
		//Check to see if the gyro is drifting, if it is re-initialize it.
		//Thanks FRC254 for orig. idea.
		curAngle = drivetrain.getHeading();
		gyroCalibrating = drivetrain.isGyroCalibrating();

		if (lastGyroCalibrating && !gyroCalibrating) {
			//if we've just finished calibrating the gyro, reset
			gyroDriftDetector.reset();
			curAngle = drivetrain.getHeading();
			System.out.println("Finished auto-reinit gyro");
		} else if (gyroDriftDetector.update(Math.abs(curAngle - lastAngle) > (0.75 / 50.0))
				&& !matchStarted && !gyroCalibrating) {
			//&& gyroReinits < 3) {
			gyroReinits++;
			System.out.println("!!! Sensed drift, about to auto-reinit gyro ("+ gyroReinits + ")");
			drivetrain.calibrateGyro();
		}

		lastAngle = curAngle;
		lastGyroCalibrating = gyroCalibrating;
	}
	
	public boolean getTX1TurnOn() {
		return !tx1TurnOn.get();
	}
	
	public boolean getTX1OnStatus() {
		return !tx1OnStatus.get();
	}
	
	public void turnTX1On() {
		if(Timer.getFPGATimestamp() > 0.03 && Timer.getFPGATimestamp() < 10) {
			tx1TurnOn.set(false);
		}
		else {
			tx1TurnOn.set(true);
		}
	}
}
