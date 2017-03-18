package org.team2168.commands.auto;

import org.team2168.commands.ballIntake.RunIntakeUntilBallNotPresent;
import org.team2168.commands.ballIntake.RunIntakeUntilBallPresent;
import org.team2168.commands.elevator.RunElevatorUntilBallNotPresent;
import org.team2168.commands.elevator.RunElevatorUntilBallPresent;
import org.team2168.commands.indexer.RunIndexerUntilBallPresent;

import edu.wpi.first.wpilibj.command.CommandGroup;

/**
 * Indexes and shoots single ball
 * @author Wen Baid
 */
public class ShootSingleBall extends CommandGroup {

    public ShootSingleBall() {
    	//Get ball into position
    	addParallel(new RunIntakeUntilBallPresent(1.0));
    	addParallel(new RunElevatorUntilBallPresent(1.0));
    	//Pause
    	addSequential(new Sleep(),0.1);
    	//Shoot ball
    	addParallel(new RunIntakeUntilBallNotPresent(1.0));
    	addParallel(new RunElevatorUntilBallNotPresent(1.0));
    	addParallel(new RunIndexerUntilBallPresent(1.0));
    	//Pause
    	addSequential(new Sleep(),0.1);
    	//Prepare next ball
    	addParallel(new RunIntakeUntilBallPresent(1.0));
    	addParallel(new RunElevatorUntilBallPresent(1.0));
    }
}
