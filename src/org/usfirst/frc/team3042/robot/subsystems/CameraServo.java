package org.usfirst.frc.team3042.robot.subsystems;

import edu.wpi.first.wpilibj.Servo;
import edu.wpi.first.wpilibj.command.Subsystem;

/**
 *
 */
public class CameraServo extends Subsystem {
    
	Servo servo = new Servo(0);
	Servo camera = new Servo(1); 

    // Put methods for controlling this subsystem
    // here. Call these from Commands.

    public void initDefaultCommand() {
        // Set the default command for a subsystem here.
        //setDefaultCommand(new MySpecialCommand());
    }
    
    public void testZero() {
    		servo.setAngle(servoConversion(0));
    }
    
    public void testOne() {
    		servo.setAngle(servoConversion(180));
    }
    
    public void setServo(double pos) {
            servo.setAngle(servoConversion(pos));
    }
    
    public void setCamera(double pos) {
            camera.setAngle(servoConversion(pos));
    }
    
    private double servoConversion(double pos) {
        double newMinPWM = 0.8;
        double newMaxPWM = 2.2;
        double convertedPos = ((newMaxPWM - newMinPWM) * pos - (newMinPWM - 0.6)) / 1.8;
        return convertedPos;
    }
}

