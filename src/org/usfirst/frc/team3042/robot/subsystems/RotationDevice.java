package org.usfirst.frc.team3042.robot.subsystems;

import org.usfirst.frc.team3042.robot.RobotMap;
import org.usfirst.frc.team3042.robot.commands.RotationDevice_Stop;

import com.ctre.CANTalon;
import com.ctre.CANTalon.FeedbackDevice;
import com.ctre.CANTalon.TalonControlMode;

import edu.wpi.first.wpilibj.command.Subsystem;

/**
 *
 */
public class RotationDevice extends Subsystem {

    // Put methods for controlling this subsystem
    // here. Call these from Commands.
    CANTalon motor = new CANTalon(RobotMap.ROTATIONDEVICE_ROTATING_MOTOR);
    
    public double speed = 314;
    
    double kP = 0, kI = 0, kD = 0;
    
    int motorZero = 0;
    
    public void initDefaultCommand() {
        // Set the default command for a subsystem here.
        //setDefaultCommand(new MySpecialCommand());
        setDefaultCommand(new RotationDevice_Stop());
    }
    
    public RotationDevice(){
        motor.changeControlMode(TalonControlMode.Speed);
        
        motor.setPID(kP, kI, kD);
        
        initEncoder();
    }
    
    public void initEncoder(){
        motor.setStatusFrameRateMs(CANTalon.StatusFrameRate.QuadEncoder, 10);
        motor.setFeedbackDevice(FeedbackDevice.QuadEncoder);
        motor.configEncoderCodesPerRev(1024);
        motor.reverseSensor(false);
        
        motorZero = motor.getEncPosition();
    }
    
    public void setRPM(double speed){; 
        motor.set(speed);
    }
    
    public void stop(){
        motor.set(0);
    }
}

