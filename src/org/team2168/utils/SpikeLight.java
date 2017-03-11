package org.team2168.utils;

import org.team2168.RobotMap;

import edu.wpi.first.wpilibj.I2C;
import edu.wpi.first.wpilibj.Relay;

/**
 *The util for controlling the quick 'n' dirtylights.
 *@Author James
 */
public class SpikeLight extends Relay{

	/**
	 * @param channel
	 */
	public SpikeLight(int channel) {
		super(channel);
	}

	/**
	 * @param channel
	 * @param direction
	 */
	public SpikeLight(int channel, Direction direction) {
		super(channel, direction);
	}

	/**
	 * @param moduleNumber
	 * @param channel
	 */
	public SpikeLight(int moduleNumber, int channel) {
		super(moduleNumber);
	}

	/**
	 * @param moduleNumber
	 * @param channel
	 * @param direction
	 */
	public SpikeLight(int moduleNumber, int channel, Direction direction) {
		super(moduleNumber);
	}
	
	/**
	 * Set the state of the forward channel.
	 * The innermost pin (A) on the Relay connector is "forward".
	 * This method will not affect the state of the reverse channel.
	 * 
	 * @param val true if on, false if off
	 */
	public void setForward(boolean fwdVal) {
		Relay.Value currentValue = this.get(),
				    newValue = currentValue;
		
		if(currentValue == Relay.Value.kForward) {
			if(!fwdVal)
				newValue = Relay.Value.kOff;
		} else if(currentValue == Relay.Value.kOff) {
			if(fwdVal)
				newValue = Relay.Value.kForward;
		} else if(currentValue == Relay.Value.kOn) {
			if(!fwdVal)
				newValue = Relay.Value.kReverse;
		} else if(currentValue == Relay.Value.kReverse) {
			if(fwdVal)
				newValue = Relay.Value.kOn;
		}
		
		this.set(newValue);
	}
	
	/**
	 * Set the state of the reverse channel.
	 * The center pin (B) on the Relay connector is "reverse".
	 * This method will not affect the state of the forward channel.
	 * 
	 * @param val true if on, false if off
	 */
	public void setReverse(boolean revVal) {
		Relay.Value currentValue = this.get(),
			    newValue = currentValue;
	
	if(currentValue == Relay.Value.kForward) {
		if(revVal)
			newValue = Relay.Value.kOn;
	} else if(currentValue == Relay.Value.kOff) {
		if(revVal)
			newValue = Relay.Value.kReverse;
	} else if(currentValue == Relay.Value.kOn) {
		if(!revVal)
			newValue = Relay.Value.kForward;
	} else if(currentValue == Relay.Value.kReverse) {
		if(!revVal)
			newValue = Relay.Value.kOff;
	}
	
	this.set(newValue);
}
}
