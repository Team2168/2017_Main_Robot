
package org.team2168;

import org.team2168.subsystems.Climber;
import org.team2168.subsystems.Drivetrain;
import org.team2168.subsystems.GearIntakeArm;
import org.team2168.subsystems.GearIntakeRoller;
import org.team2168.utils.Debouncer;
import org.team2168.utils.PowerDistribution;
import org.team2168.subsystems.Turret;
import org.team2168.subsystems.ShooterIndexer;
import org.team2168.commands.drivetrain.DriveWithJoystick;
import org.team2168.subsystems.BallElevator;
import org.team2168.subsystems.BallIntake;
import org.team2168.subsystems.ShooterHood;
import org.team2168.subsystems.Shooter;
import org.team2168.utils.consoleprinter.ConsolePrinter;

import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Scheduler;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the IterativeRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the manifest file in the resource
 * directory.
 */
public class Robot extends IterativeRobot {

	public static Drivetrain drivetrain;
	public static Climber climber;
	public static BallIntake ballIntake;
	public static Turret turret;
	public static ShooterIndexer shooterIndexer;
	public static BallElevator ballElevator;
	public static GearIntakeArm gearIntakeArm;
	public static GearIntakeRoller gearIntakeRoller;
	public static ShooterHood shooterhood;
	public static PowerDistribution pdp;
	public static Shooter shooter;

	public static OI oi;

    private static boolean matchStarted = false;

    public static int gyroReinits;
	private double lastAngle;
	private Debouncer gyroDriftDetector = new Debouncer(1.0);
	public static boolean gyroCalibrating = false;
	private boolean lastGyroCalibrating = false;
	private double curAngle = 0.0;
	
    Command autonomousCommand;
    
    static Command controlStyle;
    public static SendableChooser<Command> controlStyleChooser;

    /**
     * This function is run when the robot is first started up and should be
     * used for any initialization code.
     */
    public void robotInit() {
    	ConsolePrinter.init();
    	ConsolePrinter.setRate(RobotMap.CONSOLE_PRINTER_LOG_RATE_MS);

    	// instantiate the commands used for the autonomous period
    	turret = Turret.getInstance();
        drivetrain = Drivetrain.getInstance();
        shooterIndexer = ShooterIndexer.getInstance();	
        ballIntake = BallIntake.getInstance();
		climber = Climber.getInstance();
        gearIntakeArm = GearIntakeArm.getInstance();
        gearIntakeRoller = GearIntakeRoller.getInstance();
        shooterhood = ShooterHood.getInstance();

        // instantiate the command used for the autonomous period


        oi = OI.getInstance();
		ConsolePrinter.startThread();
		
		pdp = new PowerDistribution(RobotMap.PDPThreadPeriod);
		pdp.startThread();
		
		System.out.println("Robot Finished Loading");

        drivetrain.calibrateGyro();
    }
	
    
	/**
     * This method is called once each time the robot enters Disabled mode.
     * You can use it to reset any subsystem information you want to clear when
	 * the robot is disabled.
     */
    public void disabledInit(){
    		matchStarted = false;
    }
    
	public void disabledPeriodic() {
		// Kill all active commands
		Scheduler.getInstance().run();
		
		// Check to see if the gyro is drifting, if it is re-initialize it.
		gyroReinit();
	}

    public void autonomousInit() {
        // schedule the autonomous command (example)
        if (autonomousCommand != null) autonomousCommand.start();

		matchStarted = true;
        
        drivetrain.stopGyroCalibrating();
		drivetrain.gyroSPI.reset();
    }

    /**
     * This function is called periodically during autonomous
     */
    public void autonomousPeriodic() {
        Scheduler.getInstance().run();
    }

    public void teleopInit() {
		// This makes sure that the autonomous stops running when
        // teleop starts running. If you want the autonomous to 
        // continue until interrupted by another command, remove
        // this line or comment it out.
        if (autonomousCommand != null) autonomousCommand.cancel();

		matchStarted = true;
        
		drivetrain.stopGyroCalibrating();
    }

    /**
     * This function is called periodically during operator control
     */
    public void teleopPeriodic() {
        Scheduler.getInstance().run();
    }
    
    /**
     * This function is called periodically during test mode
     */
    public void testPeriodic() {
        LiveWindow.run();
    }
    
    /**
     * Adds control styles to the selector
     */
    public void controlStyleSelectInit(){
    	controlStyleChooser = new SendableChooser<>();
    	controlStyleChooser.addDefault("Tank Drive", new DriveWithJoystick(0));
    	controlStyleChooser.addObject("Arcade Drive", new DriveWithJoystick(1));
    	controlStyleChooser.addObject("GTA Drive", new DriveWithJoystick(2));
    	controlStyleChooser.addObject("Gun Style Controller", new DriveWithJoystick(3));
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
}
