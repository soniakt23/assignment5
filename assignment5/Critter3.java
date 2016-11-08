/*
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

public class Critter3 extends Critter {
	@Override
	/**
	 * If an enemy is not of the same species, the critter will choose to fight. Otherwise, if of the 
	 * same species, the critter will have a check to ensure it did not consume energy previously walking and has
	 * a certain amount of energy to fight. Otherwise, it will run.
	 * 
	 */
	public String toString() { return "3"; }
	private boolean hasMoved;
		
	public Critter3(){
	hasMoved = false;
	}
	
	
	public boolean fight (String enemy){
		if(!enemy.equals("3"))
			return true;
		if(hasMoved == false && getEnergy()<100){
				return true;
			}
		
			else{
				run(getRandomInt(7));
				return false;
			}
	}

	@Override
	/**
	 * The critter will walk in the direction 0 if the random number is even, otherwise, we get another
	 * random number to and if it is less than 4 run, otherwise do nothing. If the critter has a minimum of 2* the energy
	 * needed to reproduce, it will reproduce.
	 */
	public void doTimeStep() {
		hasMoved = false;
		int cardinals = getRandomInt(7) % 2;
		if(cardinals == 0){ //equals zero
			walk(cardinals);
			hasMoved = true;
		}
		
		else if(getRandomInt(7) <4){
			run(getRandomInt(7));
			hasMoved=true;
	}
	
	if(this.getEnergy() > 2 * Params.min_reproduce_energy)
		reproduce(new Critter3(), getRandomInt(7));

	}
	@Override
	public CritterShape viewShape() {
		return CritterShape.DIAMOND;
	}

	public javafx.scene.paint.Color viewColor() { return javafx.scene.paint.Color.CORAL; }
	
	@Override
	public javafx.scene.paint.Color viewOutlineColor() { return javafx.scene.paint.Color.BISQUE; }
}

