
package org.team2168;

import org.team2168.subsystems.*;
import org.team2168.commands.auto.*;
import org.team2168.commands.pneumatics.StartCompressor;
import org.team2168.utils.Debouncer;
import org.team2168.utils.I2CLights;
import org.team2168.utils.I2CLights.Range;
import org.team2168.utils.PowerDistribution;
import org.team2168.utils.TX1TurnON;
import org.team2168.utils.consoleprinter.ConsolePrinter;

import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.RobotState;
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
	
	private static DigitalInput practiceBot;

	public static Agitator agitator; 
	public static BallConvelator ballConvelator;
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
	public static I2CLights lights;

	
	static boolean autoMode;
    private static boolean matchStarted = false;
    public static int gyroReinits;
	private double lastAngle;
	private Debouncer gyroDriftDetector = new Debouncer(1.0);
	public static boolean gyroCalibrating = false;
	private boolean lastGyroCalibrating = false;
	private double curAngle = 0.0;
	
	//Whether or not low battery pattern shows above others in auto and teleop mode. KEEP THIS FALSE DURING COMPETITION!
	public static final boolean BAT_OVERRIDE = false; 
	public static final double LOW_BAT_VOLTAGE = 12.1;//The voltage that the lowbat light warning shows up.
	public static boolean opWarning = false;//Goes true when the button on the gamepad is pressed. For alerting airship pilots quickly.
    
    public static DriverStation driverstation;
	
	
	public static PowerDistribution pdp;
	Compressor comp;

	public static OI oi;
	
    static Command autonomousCommand;
    public static SendableChooser<Command> autoChooser;
    
    static int controlStyle;
    public static SendableChooser<Number> controlStyleChooser;
    
    TX1TurnON tx1;

    /**
     * This function is run when the robot is first started up and should be
     * used for any initialization code.
     */
    public void robotInit() {
    	ConsolePrinter.init();
    	ConsolePrinter.setRate(RobotMap.CONSOLE_PRINTER_LOG_RATE_MS);
    	
    	practiceBot = new DigitalInput(RobotMap.PRACTICE_BOT_JUMPER);

    	// instantiate the commands used for the autonomous period
    	agitator = Agitator.getInstance();
    	ballConvelator = BallConvelator.getInstance();
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
    	lights = I2CLights.getInstance();
    	
        oi = OI.getInstance();
        
        //run compressor
        new StartCompressor();

        autoSelectInit();
        controlStyleSelectInit();
     
		pdp = new PowerDistribution(RobotMap.PDPThreadPeriod);
		pdp.startThread();
		
		tx1 = new TX1TurnON(RobotMap.PDPThreadPeriod);
		tx1.startThread();
		
		
		
        drivetrain.calibrateGyro();
        
        SmartDashboard.putData("Autonomous Mode Chooser", Robot.autoChooser);
        SmartDashboard.putData("Control Style Chooser", Robot.controlStyleChooser);
		//ConsolePrinter.putData("Autonomous Mode Chooser", () -> {return Robot.autoChooser;}, true, false);
		ConsolePrinter.putString("AutoName", () -> {return Robot.getAutoName();}, true, false);
		ConsolePrinter.putString("Control Style Name", () -> {return Robot.getControlStyleName();}, true, false);
		//ConsolePrinter.putBoolean("isPracticeBot", Robot.isPracticeRobot());
		ConsolePrinter.putNumber("gameClock", () -> {return DriverStation.getInstance().getMatchTime();}, true, false);
        ConsolePrinter.putNumber("Robot Pressure", () -> {return Robot.pneumatics.getPSI();}, true, false);
        

        
        ConsolePrinter.putBoolean("Is Practice Bot", () -> {return isPracticeRobot();}, true, false);
        
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
		
        getControlStyleInt();
        controlStyle = (int) controlStyleChooser.getSelected();
        
		// Kill all active commands
		Scheduler.getInstance().run();

		
		autoMode = false;
		
		// Check to see if the gyro is drifting, if it is re-initialize it.
		gyroReinit();
		
		setLights();
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
        setLights();
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
    
    public static int getControlStyleInt() {
    	return (int) controlStyleChooser.getSelected();
    }

    /**
     * This function is called periodically during operator control
     */
    public void teleopPeriodic() {
    	
    	autoMode = false;
        Scheduler.getInstance().run();
        
        controlStyle = (int) controlStyleChooser.getSelected();
        
        SmartDashboard.putNumber("GunStyleXValueMakingThisLongSoWeCanFindIt", Robot.oi.driverJoystick.getLeftStickRaw_X());
        SmartDashboard.putNumber("GunStyleXInterpolatedValueMakingThisLongSoWeCanFindIt", Robot.drivetrain.getGunStyleXValue());
        setLights();
    
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
        autoChooser.addObject("Drive Over Line", new DriveStraightOverLine());
        autoChooser.addObject("Line Up and Place Gear", new DriveStraightAndPlaceGearCenter());
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
	
	/**
	 * Call this to set the lights via I2C
	 * Should be called in:
	 * -Disabled Periodic
	 * -Auto Periodic
	 * -Teleop Periodic
	 * @author Elijah Reeds
	 */
	@SuppressWarnings("unused")
	public static void setLights(){
		if(opWarning){
			lights.FastBlink(255, 255, 255, Range.DriveTrain);//Kwasmallski! DROP DAT ROPE BOI!
			lights.FastBlink(255, 255, 255, Range.ShooterIntake);
			lights.FastBlink(255, 255, 255, Range.Turret);
		}else{
			if(pdp.getBatteryVoltage() < LOW_BAT_VOLTAGE && BAT_OVERRIDE){
				lights.FastBlink(255, 0, 0, Range.DriveTrain);//Light Warning for low bat. FBlink Red.
				lights.FastBlink(255, 0, 0, Range.ShooterIntake);
				lights.FastBlink(255, 0, 0, Range.Turret);
			}else{
				if(RobotState.isDisabled()){
					if(pdp.getBatteryVoltage() < LOW_BAT_VOLTAGE){
						lights.FastBlink(255, 0, 0, Range.DriveTrain);//Light Warning for low bat. FBlink Red.
						lights.FastBlink(255, 0, 0, Range.ShooterIntake);
						lights.FastBlink(255, 0, 0, Range.Turret);
					}else{
						lights.ChaseAll(Range.DriveTrain);// Chase All Colors on disabled.
						lights.ChaseAll(Range.ShooterIntake);
						lights.ChaseAll(Range.Turret);
					}
				}else if(RobotState.isAutonomous()){
					lights.Rainbow(); //<--- set all strips rainbow in auto
				}else if(RobotState.isOperatorControl()){
					if(Robot.gearIntakeRoller.isGearPresent()){
						lights.Solid(255, 255, 0, Range.ShooterIntake);//<--- Solid Yellow while gear present
					}else{
						lights.Fade(255, 0, 0, Range.ShooterIntake);//<--- Fade red while no gear present.
					}
			
					if(Robot.drivetrainShifter.inLowGear()){
						lights.ChaseIn(255, 0, 0, Range.DriveTrain);//<--- Chase in red in low gear.
					}else if(Robot.drivetrainShifter.inHighGear()){
						lights.ChaseIn(0, 255, 0, Range.DriveTrain);//<--- Chae in green in high gear
					}
				}
			}
		}
		
	}
	
	
	/**
	 * Returns the status of DIO pin 24 
	 *
	 * @return true if this is the practice robot
	 */
	public static boolean isPracticeRobot() {
		return !practiceBot.get();
	}
	
}
