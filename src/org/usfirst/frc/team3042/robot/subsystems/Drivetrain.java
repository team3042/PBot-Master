package org.usfirst.frc.team3042.robot.subsystems;

import org.usfirst.frc.team3042.robot.RobotMap;

import com.ctre.CANTalon;
import com.ctre.CANTalon.FeedbackDevice;
import com.ctre.CANTalon.FeedbackDeviceStatus;
import com.ctre.CANTalon.MotionProfileStatus;
import com.ctre.CANTalon.TalonControlMode;
import edu.wpi.first.wpilibj.Notifier;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

import edu.wpi.first.wpilibj.command.Subsystem;

/**
 *
 */
public class Drivetrain extends Subsystem {
	
	CANTalon leftFrontMotor = new CANTalon(RobotMap.DRIVETRAIN_LEFT_FRONT_TALON);
	CANTalon leftRearMotor = new CANTalon(RobotMap.DRIVETRAIN_LEFT_REAR_TALON);
	CANTalon rightFrontMotor = new CANTalon(RobotMap.DRIVETRAIN_RIGHT_FRONT_TALON);
	CANTalon rightRearMotor = new CANTalon(RobotMap.DRIVETRAIN_RIGHT_REAR_TALON);

    // Put methods for controlling this subsystem
    // here. Call these from Commands.
	
	boolean  leftReverseEnc = true;
	boolean rightReverseEnc = false;
	double rightEncSign, leftEncSign;
	double rightEncoderZero = 0, leftEncoderZero = 0;
	
	int COUNTS_PER_REV = 1024;
	
	double fPos;
	double kFRight;
	double kFLeft;
	double kPRight;
	double kIRight;
	double kILeft;
	double kPLeft;
	int iZone;
	double kDRight;
	double pPos;
	double iPos;
	double kDLeft;
	
	double WHEEL_DIAMETER_IN;
	
	public Drivetrain() {
		leftRearMotor.changeControlMode(TalonControlMode.Follower);
		rightRearMotor.changeControlMode(TalonControlMode.Follower);
		leftFrontMotor.changeControlMode(TalonControlMode.PercentVbus);
		rightFrontMotor.changeControlMode(TalonControlMode.PercentVbus);
		leftRearMotor.set(leftFrontMotor.getDeviceID());
		rightRearMotor.set(rightFrontMotor.getDeviceID());
		
		leftFrontMotor.enableBrakeMode(true);
    	rightFrontMotor.enableBrakeMode(true);
    	
    	leftFrontMotor.reverseOutput(false);
    	leftFrontMotor.setInverted(false);
    	
    	rightFrontMotor.setInverted(true);
    	rightFrontMotor.reverseOutput(true);
    	
    	initEncoders();
    	
    	leftFrontMotor.changeMotionControlFramePeriod(5);
    	rightFrontMotor.changeMotionControlFramePeriod(5);
    	notifier.startPeriodic(0.005);
    	
    	//Initializing PIDF
    	leftFrontMotor.setProfile(1);
    	rightFrontMotor.setProfile(1);
		leftFrontMotor.setPID(pPos, iPos, kDLeft);
		rightFrontMotor.setPID(pPos, iPos, kDRight);
		leftFrontMotor.setIZone(iZone);
    	rightFrontMotor.setIZone(iZone);
		leftFrontMotor.setF(fPos);
    	rightFrontMotor.setF(fPos);
    	
    	leftFrontMotor.setProfile(0);
    	rightFrontMotor.setProfile(0);
		leftFrontMotor.setPID(kPLeft, kILeft, kDLeft);
		rightFrontMotor.setPID(kPRight, kIRight, kDRight);
		leftFrontMotor.setF(kFLeft);
		rightFrontMotor.setF(kFRight);
	}

    public void initDefaultCommand() {
        // Set the default command for a subsystem here.
        //setDefaultCommand(new MySpecialCommand());
    }
    
    public void setLeftPower(double pow){
        leftFrontMotor.set(pow);
    }
    
    public void setRightPower(double pow){
        rightFrontMotor.set(pow);
    }
    
    private double safetyTest(double pow){
        return (Math.abs(pow) < 1)? pow : (Math.abs(pow)/pow);
    }
    
    public void setMotors(double left, double right){
        setLeftPower(safetyTest(left));
        setRightPower(safetyTest(right));
    }
    
    class PeriodicRunnable implements java.lang.Runnable {
		public void run() { 
			leftFrontMotor.processMotionProfileBuffer();
			rightFrontMotor.processMotionProfileBuffer();
		}
	}
    
    Notifier notifier = new Notifier (new PeriodicRunnable());
    
    public MotionProfileStatus[] getMotionProfileStatus() {
		MotionProfileStatus[] motionProfileStatus = new MotionProfileStatus[2];
		motionProfileStatus[0] = new MotionProfileStatus();
		motionProfileStatus[1] = new MotionProfileStatus();
		leftFrontMotor.getMotionProfileStatus(motionProfileStatus[0]);
		rightFrontMotor.getMotionProfileStatus(motionProfileStatus[1]);
		
		return motionProfileStatus;
	}
    
    public void pushPoints(CANTalon.TrajectoryPoint leftPoint, CANTalon.TrajectoryPoint rightPoint) {
		leftFrontMotor.pushMotionProfileTrajectory(leftPoint);
		rightFrontMotor.pushMotionProfileTrajectory(rightPoint);
	}
    
    private void initEncoders() {
		leftFrontMotor.setFeedbackDevice(FeedbackDevice.QuadEncoder);
		rightFrontMotor.setFeedbackDevice(FeedbackDevice.QuadEncoder);
		
		leftFrontMotor.setStatusFrameRateMs(CANTalon.StatusFrameRate.QuadEncoder, 10);
		rightFrontMotor.setStatusFrameRateMs(CANTalon.StatusFrameRate.QuadEncoder, 10);
	
		leftFrontMotor.configEncoderCodesPerRev(COUNTS_PER_REV);
		rightFrontMotor.configEncoderCodesPerRev(COUNTS_PER_REV);
		
		leftFrontMotor.reverseSensor(leftReverseEnc);
		rightFrontMotor.reverseSensor(rightReverseEnc);
	}
    
    private double rotationsToInches(double rotations) {
		return rotations * (Math.PI * WHEEL_DIAMETER_IN);
	}
    
    public int getLeftEncoder() {
		return (int)(leftEncSign * (leftFrontMotor.getEncPosition() - leftEncoderZero));
	}
    
    public int getRightEncoder() {
		return (int)(rightEncSign * (rightFrontMotor.getEncPosition() - rightEncoderZero));
	}
	
	public double getLeftPositionInches() {
		double rotations = ((double) getLeftEncoder()) / (4 * COUNTS_PER_REV);
		
		return rotationsToInches(rotations);
	}
	
	public double getRightPositionInches() {
		double rotations = ((double) getRightEncoder()) / (4 * COUNTS_PER_REV);
		
		return rotationsToInches(rotations);
	}
	
	public double getLeftVelocity() {
		return leftFrontMotor.getSpeed();
	} 
	
	public double getRightVelocity() {
		return rightFrontMotor.getSpeed();
	}
	
	public double getLeftVelocityInchesPerSecond() {
		return rpmToInchesPerSecond(getLeftVelocity());
	}
	
	public double getRightVelocityInchesPerSecond() {
		return rpmToInchesPerSecond(getRightVelocity());
	}
	
	private double rpmToInchesPerSecond(double rpm) {
		return rotationsToInches(rpm) / 60;
	}
}

