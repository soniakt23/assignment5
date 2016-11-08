/* CRITTERS Critter2.java
 * EE422C Project 5 submission by
 * Replace <...> with your actual data.
 * Mary Gwozdz
 * mlg3646
 * 16450
 * Sonia Taneja
 * skt638
 * 16445
 * Slip days used: <0>
 * Fall 2016
 */
package assignment5;

import assignment5.Critter.CritterShape;

/**
 * Critter2s will move every other timestep, and can only move in 
 * four of the directions, but it will run every time it moves.
 * They will try to reproduce every fifth timestep. Critter2s will
 * fight every time if their energy is greater than 50;
 * otherwise, they will attempt to run away in the same direction 
 * set from the previous timestep.
 **/
public class Critter2 extends Critter {
	private boolean flag = false;
	private int crit2Dir = 0;
	int rep = 0;
	
	/**
	 * Critter2s will move every other timestep, and can only move in 
	 * four of the directions, but it will run every time it moves.
	 * They will try to reproduce every fifth timestep.
	 **/
	@Override
	public void doTimeStep() {
		if (flag) { //only moves every other timestep
			run(crit2Dir);
			crit2Dir = (crit2Dir +2)%8; //Critter2 can only move in four directions
		}
		flag = !flag;
		rep++;
		if(rep > 5) {
			Critter2 child = new Critter2();
			reproduce(child, crit2Dir);
			rep = 0;
		}
	}

	/**
	 * Critter2s will fight every time if their energy is greater than 50;
	 * otherwise, they will attempt to run away in the same direction set from the
	 * previous timestep.
	 **/
	@Override
	public boolean fight(String oponent) {
		if (this.getEnergy() > 50 && look(crit2Dir, false) == "C") {
			return true;
		}
		
		this.run(crit2Dir);
		return false;
	}
	
	public String toString() {
		return "2";
	}

	@Override
	public CritterShape viewShape() {
		return CritterShape.STAR;
	}

	public javafx.scene.paint.Color viewColor() { return javafx.scene.paint.Color.BLACK; }
	
	@Override
	public javafx.scene.paint.Color viewOutlineColor() { return javafx.scene.paint.Color.BLUEVIOLET; }
	
}
