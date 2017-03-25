package org.team2168.utils;

import org.team2168.RobotMap;

import edu.wpi.first.wpilibj.I2C;

/**
 *The Util for controlling lights over I2C to an Arduino.
 *@Author Elijah and Bennett<--- plz cum bak bbz
 */
public class I2CLights {
	private I2C i2c;
	private static I2CLights instance = null;
	public static final int RANGE_COUNT = 3;
	
	/**A continued pattern of behavior.
	 * @author Elijah Reeds.
	 */
	public enum Pattern {
		Off (0),
		Solid (1),
		Blink (2),
		Fade (3),
		Chase (4),
		Rainbow (5);
		
		private final int val;

		Pattern(int val) {
			this.val = val;
		}

		public int getVal() {
			return val;
		}
	}
	
	/**Out on the range.
	 * @author	Elijah Reeds
	 */
	public enum Range {
		DriveTrain(0), ShooterIntake(1), Turret(2);
		private final int val;

		Range(int val) {
			this.val = val;
		}

		public int getVal() {
			return val;
		}
	}
    
	/**
	 * Don't look at my privates!
	 */
	private I2CLights(){
		i2c = new I2C(RobotMap.I2C_PORT, RobotMap.I2C_ADDRESS);
	}
		
	/**
     * Singleton constructor for Lights subsystem.
     * @return singleton instance of Lights subsystem
     * @author Elijah Reeds.
     */
    public static I2CLights getInstance(){
    	if(instance == null)
    		instance = new I2CLights();
    	return instance;
    }

    /**Takes in value to directly right to LED's over I2C.
     * @param r Red value between 0 and 255.
     * @param g Green value between 0 and 255.
     * @param b Blue value between 0 and 255.
     * @param pat Pattern value between 0 and 6.
     * @param rang The range of lights you wish to adress.
     * @author Elijah
     */
    public void writeLED(int r, int g, int b, Pattern pat, int patData, Range range){
    	byte[] data = new byte[RANGE_COUNT * 4];
    	int rangeIn = 4 * range.getVal();
    	data[0 + rangeIn] = (byte) r;
		data[1 + rangeIn] = (byte) g;
		data[2 + rangeIn] = (byte) b;
		data[3 + rangeIn] = (byte) pat.getVal();
		data[4 + rangeIn] = (byte) patData;
    	i2c.writeBulk(data);
    }
    
    /**
     * Takes in a range and turns it's LED's off.
     * @param rang Range of the LED's to turn off.
     * @author Elijah
     */
    public void Off(Range range){
    	writeLED(0,0,0,Pattern.Off, 0, range);	
    }
    
    
    /**
     * Sets the LED's to a solid color.
     * @param r Red value from 0 - 255.
     * @param g Green value from 0 - 255.
     * @param b Blue value form 0 -255.
     * @param range LED range.
     * @author Elijah
     */
    public void Solid(int r, int g, int b, Range range){
    	writeLED(r, g, b, Pattern.Solid, 0, range);
    }
    
    
    /**
     * When your lights gotta go fast!
     * @param r Red value from 0 - 255.
     * @param g Green value from 0 - 255.
     * @param b Blue value form 0 -255.
     * @param range LED range.
     * @author Elijah
     */
    public void FastBlink(int r, int g, int b, Range range){
    	Blink(r, g, b, 5, range);
    }
    
    /**
     * Great for inducing seizures!
     * @param r Red value from 0 - 255.
     * @param g Green value from 0 - 255.
     * @param b Blue value form 0 -255.
     * @param range LED range.
     * @author Elijah
     */    
    public void ReallyFastBlink(int r, int g, int b, Range range){
    	Blink(r, g, b, 10, range);
    }
    
    /**
     * A slower blink than Fast Blink.
     * @param r Red value from 0 - 255.
     * @param g Green value from 0 - 255.
     * @param b Blue value form 0 -255.
     * @param range LED range.
     * @author Elijah
     */
    public void SlowBlink(int r, int g, int b, Range range){
    	Blink(r, g, b, 1, range);
    }
    
    /**
     * WARNING: DO NOT USE AROUND WEEPING ANGELS!
     * @param r Red value from 0 - 255.
     * @param g Green value from 0 - 255.
     * @param b Blue value form 0 -255.
     * @param spd Speed of lights in pain. Ow that hertz!
     * @param range LED range.
     * @author Elijah
     */
    public void Blink(int r, int g, int b, int spd, Range range){
    	writeLED(r, g, b, Pattern.Blink, spd, range);
    }
    
    /**
     * Fades the lights in and out.
     * @param r Red value from 0 - 255.
     * @param g Green value from 0 - 255.
     * @param b Blue value form 0 -255.
     * @param range LED range.
     * @author Elijah
     */
    public void Fade(int r, int g, int b, Range range){
    	writeLED(r, g, b, Pattern.Fade, 0, range);
    }
    
    /**
     * Makes the lights chase in towards the middle of the range in the color chosen.
     * @param r Red value from 0 - 255.
     * @param g Green value from 0 - 255.
     * @param b Blue value form 0 -255.
     * @param range LED range.
     * @author Elijah
     */
    public void ChaseIn(int r, int g, int b, Range range){
    	writeLED(r, g, b, Pattern.Chase, 0, range);
    }
    
    /**
     * Makes a pretty rainbow out of all of the LED's on the robot.
     * @author Elijah
     */
    public void Rainbow(){
    	writeLED(0, 0, 0, Pattern.Rainbow, 0, Range.ShooterIntake);
    	writeLED(0, 0, 0, Pattern.Rainbow, 0, Range.DriveTrain);
    }
    
    /**
     * Chases in red green and blue on range.
     * @param range
     */
    public void ChaseAll(Range range) {
    	writeLED(0, 0, 0, Pattern.Chase, 2, range);
    }
    
    /**
     * Makes the lights chase out from the middle of the range in the color chosen.
     * @param r Red value from 0 - 255.
     * @param g Green value from 0 - 255.
     * @param b Blue value form 0 -255.
     * @param range LED range.
     * @author Elijah
     */
    public void ChaseOut(int r, int g, int b, Range range){
    	writeLED(r, g, b, Pattern.Chase, 1, range);
    }
}