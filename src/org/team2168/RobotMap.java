package org.team2168;

import org.team2168.PID.sensors.AverageEncoder;
import edu.wpi.first.wpilibj.CounterBase;
import edu.wpi.first.wpilibj.I2C;

/**
 * The RobotMap is a mapping from the ports sensors and actuators are wired into
 * to a variable name. This provides flexibility changing wiring, makes checking
 * the wiring easier and significantly reduces the number of magic numbers
 * floating around.
 */
public class RobotMap {

	/*************************************************************************
	 *                              ROBORIO WIRING MAP
	 *************************************************************************/

	// Joysticks/////////////////////////////////////////////////////////////
	public static final int DRIVER_JOYSTICK = 0;
	public static final int OPERATOR_JOYSTICK = 1;
	public static final int COMMANDS_TEST_JOYSTICK = 4;

	// PWM (0 to 9) on RoboRio//////////////////////////////////////////////////
	public static final int RIGHT_DRIVE_MOTOR_1 = 0;
	public static final int RIGHT_DRIVE_MOTOR_2 = 1;
	public static final int LEFT_DRIVE_MOTOR_1 = 2;
	public static final int LEFT_DRIVE_MOTOR_2 = 3;
	public static final int BALL_INTAKE_MOTOR = 4;
	public static final int INDEXER_WHEEL = 5;
	public static final int TURRET_MOTOR = 6;
	public static final int CLIMBER_MOTOR_LEFT = 7;
	public static final int CLIMBER_MOTOR_RIGHT = 8;
	public static final int ELEVATOR_MOTOR = 9;

	//Channels 10-25 on MXP

	public static final int SHOOTER_WHEEL_LEFT = 10;
	public static final int SHOOTER_WHEEL_RIGHT = 11;


	//Solenoid Channels////////////////////////////////////////////////////////
	public final static int DRIVETRAIN_HIGH_GEAR = 0;
	public final static int DRIVETRAIN_LOW_GEAR= 1;
	public final static int GEAR_INTAKE_PISTON_EXTEND = 2;
	public final static int GEAR_INTAKE_PISTON_RETRACT = 3;



	//Digital IO Channels//////////////////////////////////////////////////////
	//Channels 0-9 on RoboRio
	public static final int LEFT_DRIVE_ENCODER_A = 0;
	public static final int LEFT_DRIVE_ENCODER_B = 1;
	public static final int RIGHT_DRIVE_ENCODER_A = 2;
	public static final int RIGHT_DRIVE_ENCODER_B = 3;
	public static final int SHOOTER_ENCODER_A = 4;
	public static final int SHOOTER_ENCODER_B = 5;
	public static final int TURRET_LIMIT_SWITCH_RIGHT = 6;
	public static final int TURRET_LIMIT_SWITCH_LEFT = 7;
	public static final int INDEXER_UPPER_BALL_PRESENT= 8;
	public static final int INDEXER_LOWER_BALL_PRESENT= 9;
	public static final int GEAR_INTAKE_HALL_EFECT = 10;

	public static final int PRACTICE_BOT_JUMPER = 24;       //MXP pin 32
	

	//Analog Input Channels////////////////////////////////////////////////////
	//Channels 0-3 on Roborio
	public static final int TURRET_POTENTIOMETER = 0;
	public static final int GEAR_INTAKE_ROLLER_IR = 1;



	//Channels 4-7 on MXP


	//TODO: Confirm PDP Ports
	//PDP Channels/////////////////////////////////////////////////////////////
	public final static int DRIVETRAIN_RIGHT_MOTOR_1_PDP = 13;
	public final static int DRIVETRAIN_RIGHT_MOTOR_2_PDP = 12;
	public final static int DRIVETRAIN_RIGHT_MOTOR_3_PDP = 14;
	public final static int DRIVETRAIN_LEFT_MOTOR_1_PDP = 2;
	public final static int DRIVETRAIN_LEFT_MOTOR_2_PDP = 3;
	public final static int DRIVETRAIN_LEFT_MOTOR_3_PDP = 4;
	public final static int INTAKE_MOTOR_1_PDP = 11;
	public final static int INTAKE_MOTOR_2_PDP = 5;
	public final static int INDEXER_MOTOR_PDP = 10;
	public final static int SHOOTER_MOTOR_FWD_PDP = 0;
	public final static int SHOOTER_MOTOR_AFT_PDP = 15;
	public final static int PCM_POWER = 7;

	
	//CAN Device IDs///////////////////////////////////////////////////////////


	//Relay Channels///////////////////////////////////////////////////////////
	public static final int FLASHLIGHT_RELAY = 0;


	/*************************************************************************
	 *                         DRIVETRAIN PARAMETERS
	 *************************************************************************/
	//TODO check if the reverse values match the physical robot
	public static final boolean DT_REVERSE_RIGHT = true;
	public static final boolean DT_REVERSE_LEFT = false;

	private static final int DRIVE_PULSE_PER_ROTATION = 256; //encoder ticks per rotation
	//TODO find ratio
	private static final double DRIVE_GEAR_RATIO = 1.0/1.0; //ratio between wheel over encoder
	private static final double DRIVE_WHEEL_DIAMETER = 6;
	public static final int DRIVE_ENCODER_PULSE_PER_ROT = (int) (DRIVE_PULSE_PER_ROTATION * DRIVE_GEAR_RATIO); //pulse per rotation * gear ratio
	public static final double DRIVE_ENCODER_DIST_PER_TICK = (Math.PI * DRIVE_WHEEL_DIAMETER / DRIVE_ENCODER_PULSE_PER_ROT);
	public static final CounterBase.EncodingType DRIVE_ENCODING_TYPE = CounterBase.EncodingType.k4X; //count rising and falling edges on
	public static final AverageEncoder.PositionReturnType DRIVE_POS_RETURN_TYPE = AverageEncoder.PositionReturnType.FEET;
	public static final AverageEncoder.SpeedReturnType DRIVE_SPEED_RETURN_TYPE = AverageEncoder.SpeedReturnType.FPS;
	public static final int DRIVE_ENCODER_MIN_RATE = 0;
	public static final int DRIVE_ENCODER_MIN_PERIOD = 1;
	public static final boolean LEFT_DRIVE_TRAIN_ENCODER_REVERSE = false;
	public static final boolean RIGHT_DRIVE_TRAIN_ENCODER_REVERSE = true;
	public static final int DRIVE_AVG_ENCODER_VAL = 5;
	public static final double MIN_DRIVE_SPEED = 0.2;
	public static final double AUTO_NORMAL_SPEED = 0.5;
	public static final double WHEEL_BASE = 2; //units must match PositionReturnType (feet)


	/*************************************************************************
	 *                         Shooter PARAMETERS
	 *************************************************************************/
	//TODO check if the reverse values match the physical robot
	public static final boolean REVERSE_SHOOTER_WHEEL_LEFT= false;
	public static final boolean REVERSE_SHOOTER_WHEEL_RIGHT= false;

	private static final int SHOOTER_PULSE_PER_ROTATION = 256; //encoder ticks per rotation
	//TODO find ratio
	private static final double SHOOTER_GEAR_RATIO = 14.0/28.0; //ratio between wheel over encoder
	private static final double SHOOTER_WHEEL_DIAMETER = 4;
	public static final int SHOOTER_ENCODER_PULSE_PER_ROT = (int) (SHOOTER_PULSE_PER_ROTATION * SHOOTER_GEAR_RATIO); //pulse per rotation * gear ratio
	public static final double SHOOTER_ENCODER_DIST_PER_TICK = (Math.PI * SHOOTER_WHEEL_DIAMETER / SHOOTER_ENCODER_PULSE_PER_ROT);
	public static final CounterBase.EncodingType SHOOTER_ENCODING_TYPE = CounterBase.EncodingType.k1X; //count only the rising edge
	public static final AverageEncoder.PositionReturnType SHOOTER_POS_RETURN_TYPE = AverageEncoder.PositionReturnType.FEET;
	public static final AverageEncoder.SpeedReturnType SHOOTER_SPEED_RETURN_TYPE = AverageEncoder.SpeedReturnType.RPM;
	public static final double SHOOTER_ENCODER_MIN_RATE = 0.1; //minimum inch per second
	public static final int SHOOTER_ENCODER_MIN_PERIOD = 1;
	public static final boolean SHOOTER_ENCODER_REVERSE = false;
	public static final int SHOOTER_AVG_ENCODER_VAL = 120;
	public static final double MIN_SHOOTER_SPEED = 0.2;
	public static final double SHOOTER_AUTO_NORMAL_SPEED = 0.5;
	public static final double SHOOTER_WHEEL_BASE = 2; //units must match PositionReturnType (feet)
	//TODO get correct values
	public static final double SHOOTER_BOULDER_STOP_VOLTAGE = 0.2;
	public static final double SHOOTER_CONSTANT_SPEED = 0.2;
	public static double CAMERA_OFFSET_ANGLE = 0; //degrees, camera in center of shooter

	/*************************************************************************
	 *                       BALL INTAKE PARAMETERS
	 *************************************************************************/
	public static final double INTAKE_SPEED_CONSTANT = 0.5;
	public static final boolean REVERSE_INTAKE_WHEEL = false;


	/*************************************************************************
	 *                      GEAR INTAKE PARAMETERS
	 *************************************************************************/
	public static final double GEAR_INTAKE_IR_THRESHOLD = 2.0;
	public static final double GEAR_INTAKE_SPEED_CONSTANT = 1;


	/*************************************************************************
	 *                         INDEXER PARAMETERS
	 *************************************************************************/
	public static final double INDEXER_SPEED_CONSTANT = 0.5;
	public static final boolean REVERSE_INDEXER = false;
	

	/*************************************************************************
	 *                         HOOD PARAMETERS
	 *************************************************************************/
	public static final double HOOD_JOYSTICK_MULTIPLIER = 1;
	public static final double MIN_HOOD_VALUE = 0;
	public static final double MAX_HOOD_VALUE = 0;


	/*************************************************************************
	 *                       CLIMBER PARAMETERS
	 *************************************************************************/
	public static final boolean CLIMB_MOTOR_REVERSE_LEFT = true;
	public static final boolean CLIMB_MOTOR_REVERSE_RIGHT = false;


	/*************************************************************************
	 *                        VISION PARAMETERS
	 *************************************************************************/


	/*************************************************************************
	 *                            PDP PARAMETERS
	 *************************************************************************/
	public final static long PDPThreadPeriod = 20;
	public static final double WARNING_CURRENT_LIMIT = 20;
	public static final double STALL_CURRENT_LIMIT = 35;

	/*************************************************************************
	 *                            PID PARAMETERS
	 *************************************************************************/
	//period to run PID loops on drive train
	public static final long DRIVE_TRAIN_PID_PERIOD = 20;//70ms loop
	public static final int DRIVE_TRAIN_PID_ARRAY_SIZE = 30;

	//PID Gains for Left & Right Speed and Position
	//Bandwidth =
	//Phase Margin =
	public static final double DRIVE_TRAIN_LEFT_SPEED_P =  0.4779;
	public static final double DRIVE_TRAIN_LEFT_SPEED_I =  1.0526;
	public static final double DRIVE_TRAIN_LEFT_SPEED_D =  0.0543;

	public static final double DRIVE_TRAIN_RIGHT_SPEED_P = 0.4779;
	public static final double DRIVE_TRAIN_RIGHT_SPEED_I = 1.0526;
	public static final double DRIVE_TRAIN_RIGHT_SPEED_D = 0.0543;

	public static final double DRIVE_TRAIN_LEFT_POSITION_P = 0.2;
	public static final double DRIVE_TRAIN_LEFT_POSITION_I = 0.0001412646174233;
	public static final double DRIVE_TRAIN_LEFT_POSITION_D = 0.0074778888124088;

	public static final double DRIVE_TRAIN_RIGHT_POSITION_P = 0.25;
	public static final double DRIVE_TRAIN_RIGHT_POSITION_I = 0.0001412646174233;
	public static final double DRIVE_TRAIN_RIGHT_POSITION_D = 0.0074778888124088;

	public static final double ROTATE_POSITION_P = 0.024;
	public static final double ROTATE_POSITION_I = 0.027;
	public static final double ROTATE_POSITION_D = 0.000000067;
	
	
	public static final double ROTATE_POSITION_P_CAM = 0.024;
	public static final double ROTATE_POSITION_I_CAM = 0.027;
	public static final double ROTATE_POSITION_D_CAM = 0.000000067;
	

	public static final double ROTATE_POSITION_CAMERA_MAX = 0.28;
	public static final double ROTATE_POSITION_CAMERA_MIN = 0.15;
	
	public static final double ROTATE_POSITION_P_Drive_Straight = 0.045;
	public static final double ROTATE_POSITION_I_Drive_Straight = 0.001;
	public static final double ROTATE_POSITION_D_Drive_Straight = 0.0064778888124088;

	//Shooter PID Speed
	//Bandwidth =
	//Phase Margin =
//	public static final double SHOOTER_SPEED_P = 0.000035;
//	public static final double SHOOTER_SPEED_I = 0.000053; 
//	public static final double SHOOTER_SPEED_D = 0.0000011838;
//	public static final double SHOOTER_SPEED_N = 6.8807;
	
	public static final double SHOOTER_SPEED_P = 0.000037;
	public static final double SHOOTER_SPEED_I = 0.000083; 
	public static final double SHOOTER_SPEED_D = 0.0000011838;
	public static final double SHOOTER_SPEED_N = 6.8807;
	



	/****************************************************************
	 *                TCP Servers  (ONLY FOR DEBUGGING)             *
	 ****************************************************************/
	public static final int TCP_SERVER_DRIVE_TRAIN_POS = 1180;
	public static final int TCP_SERVER_ROTATE_CONTROLLER = 1181;
	public static final int TCO_SERVER_RIGHT_DRIVE_TRAIN_SPEED = 1182;
	public static final int TCP_SERVER_LEFT_DRIVE_TRAIN_SPEED = 1183;
	public static final int TCP_SERVER_SHOOTER_SPEED = 1184;
	public static final int TCP_SERVER_ROTATE_CONTROLLER_STRAIGHT = 1185;
	public static final int TCP_SERVER_ROTATE_CAMERA_CONTROLLER = 1186;

	/******************************************************************
	 *                    ConsolePrinter PARAMETERS                   *
	 ******************************************************************/
	public static final boolean PRINT_SD_DEBUG_DATA = true;
	public static final long SmartDashThreadPeriod = 100; //ms
	public static final long CONSOLE_PRINTER_LOG_RATE_MS = 100; //ms


	/******************************************************************
	 *                    BallElevator PARAMETERS
	 ******************************************************************/
	public static final boolean REVERSE_ELEVATOR_WHEEL = false;


	/********************************************
	 * 				Kevin PARAMETERS			*
	 ********************************************/
	public static final boolean KEVIN_IS_DA_BOMB = true;
	public static final boolean GUYANA_HAS_SUNK = false; //debatable


	
	/********************************************
	 * 				Lights I2C					*
	 * *****************************************/
	public static final I2C.Port I2C_PORT = I2C.Port.kOnboard;
	public static final int I2C_ADDRESS = 10;
	
	/***************
	 * Meme Values *
	 **************/
	private static final boolean confirmed = true;
	private static final boolean Illuminati = confirmed;
	//    /\
	//   /  \
	//  / (o)\
	// /______\
	
}
