//---------Real___Robo----------\\

package org.usfirst.frc.team3133.robot;


import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.Ultrasonic;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;




public class Robot extends IterativeRobot {
	
		public Timer timer;
		int auto = 0;
		
		Joystick[] joysticks;
		Talon rightMotors;
		Talon leftMotors;
		Talon angler;
		Talon grabber;
		Talon anglerS;
		Talon grabberS;
		Talon shovel;
		Ultrasonic ultra;
		Encoder enc;
		Encoder enc2;
		
		
		//---------Autonomous Chooser----------//
		final String rightAuto = "Right";
		final String leftAuto = "Left";
		final String middleAuto = "Middle";
		final String testAuto = "Test";
		String autoSelected;
		SendableChooser<String> chooser;		
		
		public double count2 = 0;
		public double count = 0;
		
		Command autonomousCommand;
		
		//Main four colored buttons
		class Buttons{
			public final static int A = 1;
			public final static int B = 2;
			public final static int X = 3;
			public final static int Y = 4;
			
		//Bumper butons
			public final static int LEFT_BUMPER = 5, RIGHT_BUMPER = 6;
		//Middle buttans
			public final static int BACK = 7, START = 8;
		}	
		//Joystick and triggers
		class Axises{
			public final static int LEFT_X = 0, LEFT_Y = 1;
			public final static int LEFT_TRIGGER = 2, RIGHT_TRIGGER = 3;
			public final static int RIGHT_X = 4, RIGHT_Y = 5;
		}
		
		
	@Override
	public void robotInit() {
		joysticks = new Joystick[2];
		joysticks[0] = new Joystick (0);
		joysticks[1] = new Joystick (1);
		
		timer = new Timer();
		
		leftMotors = new Talon(0);
		rightMotors = new Talon(1);
		grabber = new Talon(2);
		angler = new Talon(3);
		
		enc = new Encoder(0, 1, false, Encoder.EncodingType.k4X);
		enc2 = new Encoder(2, 3, false, Encoder.EncodingType.k4X);
		
		
		
		initalizeMotorSpeeds();
		
	}
	private void initalizeMotorSpeeds() {
		
	}
	
	
	//Called once when robot stops receiving communication
	public void disabledInit() {
		chooser = new SendableChooser<String>();
		chooser.addObject("Right", rightAuto);
		chooser.addObject("Left", leftAuto);
		chooser.addDefault("Middle", middleAuto);
		chooser.addObject("Test", testAuto);
		SmartDashboard.putData("Field Position", chooser);
	}
	
	//Called periodically while robot is not receiving communication
	public void disabledPeriodic() {
	}
	
	
	
	//Called once when the RIO is booted
	@Override
	public void autonomousInit() {
		autoSelected = (String) chooser.getSelected();
		timer.reset();
		timer.start();
		count = 0;
		count2 = 0;					
	}
	
	
	//Called periodically during autonomous control
	public void autonomousPeriodic() {
		
		
		double rate = enc.getRate();
		count = count + rate;
		double distanceRaw = (count / 1440) / 146.875;
		double distance = distanceRaw * (6*Math.PI);
		boolean direction = enc.getDirection();
		boolean stopped = enc.getStopped();
		SmartDashboard.putNumber("rate", rate);
		SmartDashboard.putNumber("revolutions", distanceRaw);
		SmartDashboard.putNumber("distance", distance);
		SmartDashboard.putNumber("count", count);
		SmartDashboard.putBoolean("direction",direction);
		SmartDashboard.putBoolean("stopped",stopped);
		enc.setMaxPeriod(.1); 
		enc.setMinRate(10);
		enc.setDistancePerPulse(5);
		enc.setReverseDirection(false);
		enc.setSamplesToAverage(7);
		enc.reset();
		double rate2 = enc2.getRate();
		count2 = count2 + rate2;
		double distanceRaw2 = (count2 / 1440) / 146.875;
		double distance2 = distanceRaw2 * (6*Math.PI);
		boolean direction2 = enc2.getDirection();
		boolean stopped2 = enc2.getStopped();
		SmartDashboard.putNumber("rate2", rate2);
		SmartDashboard.putNumber("revolutions2", distanceRaw2);
		SmartDashboard.putNumber("distance2", distance2);
		SmartDashboard.putNumber("count2", count2);
		SmartDashboard.putBoolean("direction2",direction2);
		SmartDashboard.putBoolean("stopped2",stopped2);
		enc2.setMaxPeriod(.1); 
		enc2.setMinRate(10);
		enc2.setDistancePerPulse(5);
		enc2.setReverseDirection(true);
		enc2.setSamplesToAverage(7);
		enc2.reset();
		
		
		
		switch(autoSelected) {
		case rightAuto:
			String gameDataR;
			gameDataR = DriverStation.getInstance().getGameSpecificMessage();
			if(gameDataR.charAt(0) == 'L')//(If the scale for team color is on the left;
			{							  //the driverstation receives a string from the
				//LEFT auto code          //compeition that is then given to the RIO)	
				if(timer.get() < 4) {
					leftMotors.set(0.5);
					rightMotors.set(-0.5 * 0.94627071757194956350620969787065);
				}
				else if(timer.get() > 4.01) {
					leftMotors.set(0.0);
					rightMotors.set(0.0);
				}
			} else {
				//RIGHT auto code
				if(timer.get() < 2) {
					leftMotors.set(0.5);
					rightMotors.set(-0.7 * 0.94627071757194956350620969787065);
				}
				else if(timer.get() > 2.01 && timer.get() < 3.0) {
					leftMotors.set(0.0);
					rightMotors.set(0.0);
					angler.set(-1.0);
				}
				else if(timer.get() > 3.01 && timer.get() < 4.5) {
					grabber.set(1.0);
				}
				else if(timer.get() > 4.51) {
					leftMotors.set(0.0);
					rightMotors.set(0.0);
					angler.set(0.0);
					grabber.set(0.0);
				}
			}
				
			break;
			
		case leftAuto:
			String gameDataL;
			gameDataL = DriverStation.getInstance().getGameSpecificMessage();
			if(gameDataL.charAt(0) == 'L')
			{
				//LEFT auto code
				if(timer.get() < 2) {
					leftMotors.set(0.7);
					rightMotors.set(-0.5 * 0.94627071757194956350620969787065);
				}
				else if(timer.get() > 2.01 && timer.get() < 3.0) {
					leftMotors.set(0.0);
					rightMotors.set(0.0);
					angler.set(-1.0);
				}
				else if(timer.get() > 3.01 && timer.get() < 4.5) {
					grabber.set(1.0);
				}
				else if(timer.get() > 4.51) {
					leftMotors.set(0.0);
					rightMotors.set(0.0);
					angler.set(0.0);
					grabber.set(0.0);
				}
			} else {
				//RIGHT auto code
				if(timer.get() < 4) {
					leftMotors.set(0.5);
					rightMotors.set(-0.5 * 0.94627071757194956350620969787065);
				}
				else if(timer.get() > 4.01) {
					leftMotors.set(0.0);
					rightMotors.set(0.0);
				}
			}
			break;
			
		case middleAuto:
			String gameDataM;
			gameDataM = DriverStation.getInstance().getGameSpecificMessage();
			if(gameDataM.charAt(0) == 'L')
			{
				//LEFT auto code
				if(timer.get() < 2) {
					leftMotors.set(0.464);
					rightMotors.set(-0.663 * 0.94627071757194956350620969787065);
					angler.set(0.0);
				}
				else if(timer.get() > 2.01 && timer.get() < 3.5) {
					leftMotors.set(0.763);
					rightMotors.set(-0.164);
				}
				else if(timer.get() > 4.01 && timer.get() < 6.5) {
					leftMotors.set(0.0);
					rightMotors.set(0.0);
					angler.set(1.0);
				}
				else if(timer.get() > 6.51 && timer.get() < 7.5) {
					grabber.set(1.0);
				}
				else if(timer.get() > 7.51) {
					leftMotors.set(0.0);
					rightMotors.set(0.0);
					angler.set(0.0);
					grabber.set(0.0);
				}
			} else {
				
				//RIGHT auto code
				if(timer.get() < 2) {
					leftMotors.set(0.663);
					rightMotors.set(-0.464 * 0.94627071757194956350620969787065);
					angler.set(0.0);
				}
				else if(timer.get() > 2.01 && timer.get() < 3.5) {
					leftMotors.set(0.164);
					rightMotors.set(-0.763);
				}
				else if(timer.get() > 4.01 && timer.get() < 6.5) {
					leftMotors.set(0.0);
					rightMotors.set(0.0);
					angler.set(1.0);
				}
				else if(timer.get() > 6.51 && timer.get() < 7.5) {
					grabber.set(1.0);
				}
				else if(timer.get() > 7.51) {
					leftMotors.set(0.0);
					rightMotors.set(0.0);
					angler.set(0.0);
					grabber.set(0.0);
				}
				
				
		}
			break;
			 
		case testAuto:
			if(timer.get() < 1) {
				leftMotors.set(0.5);
				rightMotors.set((-0.5 * 0.87613906785071678611580806447601));
			}
			else if(timer.get() > 1 && timer.get() < 2){
				leftMotors.set(-0.5);
				rightMotors.set((0.5 * 0.87613906785071678611580806447601));
			}
			else {
			leftMotors.set(0.0);
			rightMotors.set(0.0);
			}
			break;
		
		}
		
		
	}

	
	//Called periodically during operator control.
	@Override
	public void teleopPeriodic() {
		//Set variables for motor speeds using the functions that apply to them
		double leftSpeed = -deadband(getMotor(Side.LEFT));
		double rightSpeed = -deadband(getMotor(Side.RIGHT));
		double turnright = deadband2(getTurn(Direction.TURNRIGHT));
		double turnleft = deadband2(getTurn(Direction.TURNLEFT));
		double grab = deadband(getGrab(Grab.LIFT));
		double angle = deadband2(getAngle(Angle.LIFT));
		
		
		
		int count = enc.get();
		double distanceRaw = enc.getRaw();
		double distance = enc.getDistance();
		double rate = enc.getRate();
		boolean direction = enc.getDirection();
		boolean stopped = enc.getStopped();
		SmartDashboard.putNumber("rate", rate);
		SmartDashboard.putNumber("raw distance", distanceRaw);
		SmartDashboard.putNumber("distance", distance);
		SmartDashboard.putNumber("count", count);
		SmartDashboard.putBoolean("direction",direction);
		SmartDashboard.putBoolean("stopped",stopped);
		enc.setMaxPeriod(.1); 
		enc.setMinRate(10);
		enc.setDistancePerPulse(5);
		enc.setReverseDirection(true);
		enc.setSamplesToAverage(7);
		enc.reset();
		int count2 = enc2.get();
		double distanceRaw2 = enc2.getRaw();
		double distance2 = enc2.getDistance();
		double rate2 = enc2.getRate();
		boolean direction2 = enc2.getDirection();
		boolean stopped2 = enc2.getStopped();
		SmartDashboard.putNumber("rate2", rate2);
		SmartDashboard.putNumber("raw distance2", distanceRaw2);
		SmartDashboard.putNumber("distance2", distance2);
		SmartDashboard.putNumber("count2", count2);
		SmartDashboard.putBoolean("direction2",direction2);
		SmartDashboard.putBoolean("stopped2",stopped2);
		enc2.setMaxPeriod(.1); 
		enc2.setMinRate(10);
		enc2.setDistancePerPulse(5);
		enc2.setReverseDirection(true);
		enc2.setSamplesToAverage(7);
		enc2.reset();
		
		
		
		
		//This calls the functions to set motor speeds
		setDrive(leftSpeed, rightSpeed, turnright, turnleft);
		pullInOut(grab);
		changeAngle(angle);

		
		
		}
		//deadband settings (value changes based on motor &/or joysticks)
		double deadband (double rawValue) {
		return deadband (rawValue, 0.2);
		}
		double deadband2 (double rawValue) {
		return deadband2 (rawValue, -0.02);
		}
		//Deadband 3 is unused
		double deadband3 (double rawValue) {
		return deadband3 (rawValue, 0.6);
		}

		
		//These functions do the math for deadband values
double deadband(double rawValue, double deadspace) {
		if (rawValue > deadspace) {
			return (rawValue - deadspace) / (1 - deadspace);
		}
		if (rawValue < -deadspace) {
			return (rawValue + deadspace) / (1 - deadspace);
		}
		return 0;
		}

double deadband2(double rawValue, double deadspace) {
	if (rawValue > deadspace) {
		return (rawValue - deadspace) / (1 - deadspace);
	}
	if (rawValue < -deadspace) {
		return (rawValue + deadspace) / (1 - deadspace);
	} 
	return 0;
    }
double deadband3(double rawValue, double deadspace) {
	if (rawValue > deadspace) {
		return (rawValue - deadspace) / (1 - deadspace);
	}
	if (rawValue < -deadspace) {
		return (rawValue + deadspace) / (1 - deadspace);
	}
	return 0;
	}


//These are classes, in essence objects with values that can be changed and used
class Side {
	public final static boolean LEFT = false;
	public final static boolean RIGHT = true;
	
}
class Direction {
	public final static boolean TURNLEFT = false;
	public final static boolean TURNRIGHT = true;
}

class Grab {
	public final static boolean LIFT = true;
}

class Angle {
	public final static boolean LIFT = true;
}



//These functions get values from the joysticks and set the class' values
double getMotor(boolean Side) {
	return joysticks[0].getRawAxis(Side ? Axises.RIGHT_Y : Axises.LEFT_Y);
}
double getTurn(boolean Direction) {
	return joysticks[0].getRawAxis(Direction ? Axises.RIGHT_TRIGGER : Axises.LEFT_TRIGGER);
}

double getGrab(boolean Grab) {
	return joysticks[1].getRawAxis(Axises.LEFT_Y);
}
double getAngle(boolean Angle) {
	return joysticks[1].getRawAxis(Axises.RIGHT_Y );
}


//These functions take the values gotten from the classes and set them to motor speeds
void setDrive(double left, double right, double turnleft, double turnright) {
	if (left >= 0.0) {
		leftMotors.set((left * left) + (-turnleft / 2) + (turnright / 2));
	}
	else {
		leftMotors.set(-left * left);
	}
	if (right >= 0.0) {
		rightMotors.set((-right * right) + (turnright / 2) + (-turnleft / 2));
	}
	else {
		rightMotors.set(right * right);
	
	}	
	
}

void setDrive(double speed) {
	setDrive(speed, speed, speed, speed);
}

void pullInOut(double grab) {
	grabber.set(-grab);
}
void changeAngle(double angle) {
	angler.set(angle);
}


public void testPeriodic() {
	}
}
