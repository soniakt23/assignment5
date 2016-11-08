/* CRITTERS Critter1.java
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

/**During a fight, if the Critter4 has already expended energy moving previously, it will decide to fight now instead.
 * Else if the random number is greater than 2, we will walk otherwise run.
 */
public class Critter4 extends Critter {
		public String toString() { return "4"; }
		private boolean hasMoved;
		private int dir;
			
		public Critter4(){
		hasMoved = false;
		dir = getRandomInt(4);
		}

		@Override
		public boolean fight(String oponent) {
			if(hasMoved == true )
				return true;
			else if (getRandomInt(4) > 2){
				walk(dir);				
				return false;

			}
				else{
					run(dir);
					return false;
				}
					
			}
		/**
		 * For each timestep,if the critter has an energy source 3X the reproduce energy required, the critter will
		 * reproduce. Else, the critter will run if it has 3X the energy to run, otherwise walk.
		 */
		@Override
		public void doTimeStep() {
			hasMoved = false;
			if(this.getEnergy() > (Params.min_reproduce_energy *3))
				reproduce(new Critter4(), getRandomInt(7));
			else if (this.getEnergy() > 3 * Params.run_energy_cost){
				run(getRandomInt(7));
				hasMoved = true;
			}
			else{
				walk(getRandomInt(7));
				hasMoved = true;
			}				
		}
		@Override
		public CritterShape viewShape() {
			return CritterShape.TRIANGLE;
		}

		public javafx.scene.paint.Color viewColor() { return javafx.scene.paint.Color.MEDIUMPURPLE; }
		
		@Override
		public javafx.scene.paint.Color viewOutlineColor() { return javafx.scene.paint.Color.CRIMSON; }
}
