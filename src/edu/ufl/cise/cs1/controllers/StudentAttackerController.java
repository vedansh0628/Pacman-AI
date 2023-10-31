package edu.ufl.cise.cs1.controllers;
import game.controllers.AttackerController;
import game.models.*;
import game.system._Maze;

import java.awt.*;
import java.util.List;

public final class StudentAttackerController implements AttackerController
{
	public void init(Game game) { }

	public void shutdown(Game game) { }

	public int update(Game game,long timeDue)
	{
		//Initializing action variable.
		int action = -1;

		//Gets node for closest pill.
		Node closestP = game.getAttacker().getTargetNode(game.getPillList(), true);

		//Gets the closest defender and the integer distance it is away from the attacker.
		Defender closestDefender = (Defender) game.getAttacker().getTargetActor(game.getDefenders(), true);
		int defenderDistance = game.getAttacker().getLocation().getPathDistance(closestDefender.getLocation());

		//If all the power pills aren't eaten already.
		if(!game.getPowerPillList().isEmpty()) {
			//Gets the node from the closest power pill and the integer distance it is away from the attacker.
			Node closestPP = game.getAttacker().getTargetNode(game.getPowerPillList(), true);
			int ppDistance = game.getAttacker().getLocation().getPathDistance(closestPP);

			//Attacker eats the closest pills.
			action = game.getAttacker().getNextDir(closestP, true);

			//If the power pill distance is 1 or less away the attacker stays still.
			if(ppDistance <= 1) {
				action = game.getAttacker().getReverse();
				//If a defender is 4 or less away the attacker eats the power pill.
				if(defenderDistance <= 4) {
					action = game.getAttacker().getNextDir(closestPP, true);
				}
				return action;
			}
			//The attacker chases the defender until they aren't vulnerable anymore.
			if(closestDefender.isVulnerable()) {
				action = game.getAttacker().getNextDir(closestDefender.getLocation(), true);
				return action;
			}
			return action;
		}
		//If all the power pills are eaten.
		else {
			//Attacker eats the closest pills.
			action = game.getAttacker().getNextDir(closestP, true);

			//Once the last power pill is eaten the attacker chases the defender until they aren't vulnerable.
			if(closestDefender.isVulnerable()) {
				action = game.getAttacker().getNextDir(closestDefender.getLocation(), true);
				return action;
			}
			//If the defender is 1 or less away the attacker runs away.
			else if(defenderDistance <= 1) {
				action = game.getAttacker().getNextDir(closestDefender.getLocation(), false);
				return action;
			}
		}
		return action;
	}
}