package org.team2168.subsystems;

import org.team2168.RobotMap;
import org.team2168.commands.flashlight.SetFlashlight;


import edu.wpi.first.wpilibj.AnalogInput;
import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.Relay;
import edu.wpi.first.wpilibj.Relay.Value;
import edu.wpi.first.wpilibj.command.Subsystem;

public class Flashlight extends Subsystem {
    
    private Relay flashlight;
     
    private static Flashlight instance = null;
    private boolean isFlashlightSettable = true;
    
    /**
     * Private constructor for the Pneumatics subsystem
     */
    private Flashlight(){
    	flashlight = new Relay(RobotMap.FLASHLIGHT_RELAY);
    }
    
    public static Flashlight getInstance(){
    	if(instance == null)
    		instance = new Flashlight();
    	return instance;
    }
    
    public void setFlashlightOn() {
    	if (isFlashlightSettable) {
    		flashlight.set(Value.kForward);
    	}
    }
    
    public void setFlashlightOff() {
    	if (isFlashlightSettable) {
    		flashlight.set(Value.kOff);
    	}
    }
    
    public boolean isFlashlightOff() {
    	return (flashlight.get() == Value.kOff);
    }
    
    public void disableFlashlight() {
    	isFlashlightSettable = false;
    	flashlight.set(Value.kOff);
    }
    
    public void enableFlashlight() {
    	isFlashlightSettable = true;
    }
    
	@Override
	protected void initDefaultCommand() {
		setDefaultCommand(new SetFlashlight());
	}
    
}

