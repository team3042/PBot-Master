package org.usfirst.frc.team3042.robot;

import org.usfirst.frc.team3042.robot.commands.CameraServo_SetRotation;
import org.usfirst.frc.team3042.robot.commands.CameraServo_SetTilt;
import org.usfirst.frc.team3042.robot.commands.CameraServo_TestOne;
import org.usfirst.frc.team3042.robot.commands.CameraServo_TestZero;
import org.usfirst.frc.team3042.robot.commands.RotationDevice_SetRPM;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.buttons.Button;
import edu.wpi.first.wpilibj.buttons.JoystickButton;


/**
 * This class is the glue that binds the controls on the physical operator
 * interface to the commands and command groups that allow control of the robot.
 */
public class OI {
	//// CREATING BUTTONS
	// One type of button is a joystick button which is any button on a
	//// joystick.
	// You create one by telling it which joystick it's on and which button
	// number it is.
	// Joystick stick = new Joystick(port);
	// Button button = new JoystickButton(stick, buttonNumber);

	// There are a few additional built in buttons you can use. Additionally,
	// by subclassing Button you can create custom triggers and bind those to
	// commands the same as any other Button.

	//// TRIGGERING COMMANDS WITH BUTTONS
	// Once you have a button, it's trivial to bind it to a button in one of
	// three ways:

	// Start the command when the button is pressed and let it run the command
	// until it is finished as determined by it's isFinished method.
	// button.whenPressed(new ExampleCommand());

	// Run the command while the button is being held down and interrupt it once
	// the button is released.
	// button.whileHeld(new ExampleCommand());

	// Start the command when the button is released and let it run the command
	// until it is finished as determined by it's isFinished method.
	// button.whenReleased(new ExampleCommand());
	public Joystick joystickLeft = new Joystick(0);
	public Joystick joystickRight = new Joystick(1); 
	
	Button testButton = new JoystickButton(joystickLeft, 1);
	Button testThing = new JoystickButton(joystickLeft, 2);
	
	Button RightTrigger = new JoystickButton(joystickRight, 1);
	Button RightButton7 = new JoystickButton(joystickRight, 7);
	Button RightButton8 = new JoystickButton(joystickRight, 8);
	
	public OI() {
		testButton.whenPressed(new CameraServo_TestZero());
		testThing.whenPressed(new CameraServo_TestOne());
		
		RightTrigger.whileHeld(new RotationDevice_SetRPM(314));
		
		RightButton7.whenPressed(new CameraServo_SetRotation(90));
		RightButton8.whenPressed(new CameraServo_SetTilt(90));
		
	}
	
}
