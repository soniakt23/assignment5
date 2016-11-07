/* CRITTERS <Critter1.java>

 * EE422C Project 4 submission by
 * Replace <...> with your actual data.
 * <Mary Gwozdz>
 * <mlg3646>
 * <16450>
 * <Sonia Taneja>
 * <skt638>
 * <16445>
 * Slip days used: <0>
 * Fall 2016
 */
package assignment5;

import assignment5.Critter.CritterShape;

/**
 * Critter1s will walk to the right on every timestep and reproduce every timestep after the fifth timestep.
 * If the Critter1's energy is greater than a random number between 0 and 100, then it will fight; otherwise,
 * it will try to run to the right.
 */
public class Critter1 extends Critter {
	int rep = 0;
	private int crit1Dir = 0;

	/**
	 * Critter1s will walk to the right on every timestep and reproduce every timestep after the fifth timestep.
	 */
	@Override
	public void doTimeStep() {
		walk(crit1Dir);
		rep++;
		if(rep > 5) {
			Critter1 child = new Critter1();
			reproduce(child, crit1Dir);
		}
	}
	/**
	 * If the Critter1's energy is greater than a random number between 0 and 100, then it will fight.
	 * Otherwise it will try to run to the right.
	 */
	@Override
	public boolean fight(String oponent) {
		//Fight?
		int randFight = getRandomInt(100);
		if (this.getEnergy() > randFight) {
			return true;
		}
		
		//run away
		run(crit1Dir);
		return false;
	}
	
	public String toString() {
		return "1";
	}

	@Override
	public CritterShape viewShape() {
		return CritterShape.DIAMOND;
	}

	public javafx.scene.paint.Color viewColor() { return javafx.scene.paint.Color.AQUA; }
	
	@Override
	public javafx.scene.paint.Color viewOutlineColor() { return javafx.scene.paint.Color.BLACK; }
	
	

}
