package assignment5;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import assignment5.Critter.CritterShape;
import javafx.scene.Scene;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.stage.Stage;

public abstract class Critter {
	/* NEW FOR PROJECT 5 */
	public enum CritterShape {
		CIRCLE,
		SQUARE,
		TRIANGLE,
		DIAMOND,
		STAR
	}
	
	/* the default color is white, which I hope makes critters invisible by default
	 * If you change the background color of your View component, then update the default
	 * color to be the same as you background 
	 * 
	 * critters must override at least one of the following three methods, it is not 
	 * proper for critters to remain invisible in the view
	 * 
	 * If a critter only overrides the outline color, then it will look like a non-filled 
	 * shape, at least, that's the intent. You can edit these default methods however you 
	 * need to, but please preserve that intent as you implement them. 
	 */
	static GridPane grid = new GridPane();
	static Scene scene = new Scene(grid, 20*Params.world_height, 20*Params.world_width);

	public javafx.scene.paint.Color viewColor() { 
		return javafx.scene.paint.Color.WHITE;//change this 
	}
	
	public javafx.scene.paint.Color viewOutlineColor() { return viewColor(); }
	public javafx.scene.paint.Color viewFillColor() { return viewColor(); }
	
	public abstract CritterShape viewShape(); 
	
	private static String myPackage;
	private	static List<Critter> population = new java.util.ArrayList<Critter>();
	private static List<Critter> babies = new java.util.ArrayList<Critter>();
	private boolean moved;
	private static ArrayList<Integer> old_x_vals = new ArrayList<Integer>();
	private static ArrayList<Integer> old_y_vals = new ArrayList<Integer>();
	private static ArrayList<String> old_crit_strings = new ArrayList<String>();

	// Gets the package name.  This assumes that Critter and its subclasses are all in the same package.
	static {
		myPackage = Critter.class.getPackage().toString().split(" ")[1];
	}
	
	protected String look(int direction, boolean steps) {
		this.energy -= Params.look_energy_cost;
		int [] finalPosition;
		finalPosition = this.move(direction, this.x_coord, this.y_coord);
		StackTraceElement[] stackTraceElements = Thread.currentThread().getStackTrace();
		
		//if called from doTimeStep reference old critter locations
		if (!stackTraceElements[2].getMethodName().equals("fight")) {
			for (int i = 0; i < old_x_vals.size(); i++){
				if (old_x_vals.get(i)==finalPosition[0] && old_y_vals.get(i) == finalPosition[1])
					return old_crit_strings.get(i);
			}
		}
		
		//if called from fight reference newest critter locations
		if (stackTraceElements[2].getMethodName().equals("fight")) {
			for (Critter c: population){
				if (c.x_coord==finalPosition[0] && c.y_coord == finalPosition[1])
					return c.toString();
			}
		}
		
		return null;
	}

	
	/**
	 * Retrieves a "random" number from the random number generator.
	 * @param max is the maximum number to possibly be output by the random number generator
	 * @return "random" number
	 */
	private static java.util.Random rand = new java.util.Random();
	public static int getRandomInt(int max) {
		return rand.nextInt(max);
	}
	
	/**
	 * Provides the random number generator with a new starting point to help produce a specific set of random numbers
	 * for repeatability in code testing.
	 * @param new_seed gives the random number generator a specific starting point to its generation
	 */
	public static void setSeed(long new_seed) {
		rand = new java.util.Random(new_seed);
	}
	
	
	/* a one-character long string that visually depicts your critter in the ASCII interface */
	public String toString() { return ""; }
	
	private int energy = 0;
	protected int getEnergy() { return energy; }
	
	private int x_coord;
	private int y_coord;
	private boolean alive; //when reproduce make sure this value is set to true
	
	private boolean isAlive(){
		return this.alive;
	}
	
	/**
	 * Changes the x_coord and y_coord variable for a critter that hasn't already moved during this timestep,
	 * but charges it the walk_energy_cost every time this function is called. If this function is being called during a fight,
	 * then the critter is not allowed to be relocated to a spot that is already occupied.
	 * @param direction indicates which of the 8 directions to walk in
	 */
	protected final void walk(int direction) {
		this.energy -= Params.walk_energy_cost;
		int[] spot = move(direction, x_coord, y_coord);
		StackTraceElement[] stackTraceElements = Thread.currentThread().getStackTrace();
		
		//if already moved, return
		if (moved)
			return;
		
		//if fromFight and spot already taken, return
		if (stackTraceElements[2].getMethodName().equals("fight")) {
			for (Critter crit : population) {
				if (crit.x_coord == spot[0] && crit.y_coord == spot[1])
					return;
			}
		}
		
		x_coord = spot[0];
		y_coord = spot[1];
		this.moved = true;
	}
	
	/**
	 * Changes the x_coord and y_coord variable for a critter that hasn't already moved during this timestep,
	 * but charges it the run_energy_cost every time this function is called. If this function is being called during a fight,
	 * then the critter is not allowed to be relocated to a spot that is already occupied. This method moves the critter
	 * twice as far as it would be moved with the walk method.
	 * @param direction indicates which of the 8 directions to walk in
	 */
	protected final void run(int direction) {
		this.energy -= Params.run_energy_cost;
		int[] one = move(direction, x_coord, y_coord);
		int[] two = move(direction, one[0], one[1]);
		StackTraceElement[] stackTraceElements = Thread.currentThread().getStackTrace();
		
		//if already moved, return
		if (moved)
			return;
		
		//if fromFight and spot already taken, return
		if (stackTraceElements[2].getMethodName().equals("fight")) {
			for (Critter crit : population) {
				if (!this.equals(crit) && crit.x_coord == two[0] && crit.y_coord == two[1])
					return;
			}
		}
		
		x_coord = two[0];
		y_coord = two[1];
		this.moved = true;
	}
	
	/**
	 * Determines the new position of a critter based off of the passed in direction and initial x and y coordinates.
	 * @param direction indicates which of the 8 directions the critter will move in
	 * @param xi indicates the initial x coordinate of the critter prior to movement
	 * @param yi indicates the initial y coordinate of the critter prior to movement
	 * @return int[] of the new {x_coord, y_coord} that would correspond to the movement input
	 */
	private final int[] move(int direction, int xi, int yi) {
		int[] r = {xi,yi};
		switch (direction) {
		case 0: //x++;
			r[0] = (xi + 1) % Params.world_width;
			break;
		case 1: //x++; y--;
			r[0] = (xi + 1) % Params.world_width;
				r[1] = yi - 1;
				if (r[1] < 0)
					r[1] = Params.world_height-1; 
				break;
		case 2:	//y--;
			r[1] = yi - 1;
			if (r[1] < 0)
				r[1] = Params.world_height-1; 
			break;
		case 3: // x--; y--;
			r[0] = xi - 1;
			if (r[0] < 0)
				r[0] = Params.world_width-1;
			r[1] = yi - 1;
			if (r[1] < 0)
				r[1] = Params.world_height-1;
			break;
		case 4: //x--;
			r[0] = xi - 1;
			if (r[0] < 0)
				r[0] = Params.world_width-1;
			break;
		case 5: //x--; y++;
			r[0] = xi - 1;
			if (r[0] < 0)
				r[0] = Params.world_width-1;
			r[1] = (yi + 1) % Params.world_height;
			break;
		case 6: //y++
			r[1] = (yi + 1) % Params.world_height;
			break;
		case 7: //x++; y++;
			r[0] = (xi + 1) % Params.world_width;
			r[1] = (yi + 1) % Params.world_height;
			break;
		}
		return r;
	}
	
	/**
	 * Initializes a baby offspring critter if the parent has at least min_reproduce_energy. Initialization
	 * consists of giving the child half of the parent's energy (rounded down), placing it one movement (walk)
	 * away from the parent's position in the specified direction, and adding it to the babies list.
	 * @param offspring is the new critter that needs to be initialized if the parent has enough energy
	 * @param direction indicates the spot to put the offspring critter relative to the parent
	 */
	protected final void reproduce(Critter offspring, int direction) {
		//Enough energy to reproduce?
		if (this.energy < Params.min_reproduce_energy) {
			return;
		}
		
		//Assign child energy, reassign parent energy
		offspring.energy = this.energy/2;
		this.energy = this.energy/2 + this.energy%2;
		
		//Assign the child a position
		offspring.x_coord = this.x_coord;
		offspring.y_coord = this.y_coord;
		offspring.setAlive(true);
		int[] spot = offspring.move(direction, offspring.x_coord, offspring.y_coord);
		x_coord = spot[0];
		y_coord = spot[1];
		
		//Add to babies list so grading will work?
		babies.add(offspring);
	}

	public abstract void doTimeStep();
	public abstract boolean fight(String oponent);
	
	/**
	 * create and initialize a Critter subclass.
	 * critter_class_name must be the unqualified name of a concrete subclass of Critter, if not,
	 * an InvalidCritterException must be thrown.
	 * (Java weirdness: Exception throwing does not work properly if the parameter has lower-case instead of
	 * upper. For example, if craig is supplied instead of Craig, an error is thrown instead of
	 * an Exception.)
	 * @param critter_class_name
	 * @throws InvalidCritterException
	 */
	public static void makeCritter(String critter_class_name) throws InvalidCritterException {
		//throw exception for invalid type of critter
		Critter crit;
		try {
		Class<?> newCritter = Class.forName(myPackage + "." + critter_class_name);
		Constructor<?> newConstructor = newCritter.getConstructor();
		Object obj = newConstructor.newInstance();
		crit  = (Critter)obj;
		} catch (Exception e) {
			throw new InvalidCritterException(critter_class_name);//how do we know this is the exception
		}
		//create and initialize a critter
		
		//install the critter into the collection
		population.add(crit);
		
		//prepare for simulation
		crit.alive =true;
		crit.energy = Params.start_energy;
		crit.x_coord = getRandomInt(Params.world_width);
		crit.y_coord = getRandomInt(Params.world_height);
	}

	
	/**
	 * Gets a list of critters of a specific type.
	 * @param critter_class_name What kind of Critter is to be listed.  Unqualified class name.
	 * @return List of Critters.
	 * @throws InvalidCritterException
	 */
	public static List<Critter> getInstances(String critter_class_name) throws InvalidCritterException {
		List<Critter> result = new ArrayList <Critter>();
		Class <?> crit_class = null;
		try {
		 crit_class = Class.forName(myPackage + "." + critter_class_name);
		} catch(Exception e) {
			throw new InvalidCritterException(critter_class_name);}
			for (Critter crit : population) {
			if (crit_class.isInstance(crit)) 
				result.add(crit);
		}
		return result;
	}
	
	/**
	 * Prints out how many Critters of each type there are on the board.
	 * @param critters List of Critters.
	 */
	public static void runStats(List<Critter> critters) {
		System.out.print("" + critters.size() + " critters as follows -- ");
		java.util.Map<String, Integer> critter_count = new java.util.HashMap<String, Integer>();
		for (Critter crit : critters) {
			String crit_string = crit.toString();
			Integer old_count = critter_count.get(crit_string);
			if (old_count == null) {
				critter_count.put(crit_string,  1);
			} else {
				critter_count.put(crit_string, old_count.intValue() + 1);
			}
		}
		String prefix = "";
		for (String s : critter_count.keySet()) {
			System.out.print(prefix + s + ":" + critter_count.get(s));
			prefix = ", ";
		}
		System.out.println();		
	}
	
	/* the TestCritter class allows some critters to "cheat". If you want to 
	 * create tests of your Critter model, you can create subclasses of this class
	 * and then use the setter functions contained here. 
	 * 
	 * NOTE: you must make sure that the setter functions work with your implementation
	 * of Critter. That means, if you're recording the positions of your critters
	 * using some sort of external grid or some other data structure in addition
	 * to the x_coord and y_coord functions, then you MUST update these setter functions
	 * so that they correctly update your grid/data structure.
	 */
	static abstract class TestCritter extends Critter {
		protected void setEnergy(int new_energy_value) {
			super.energy = new_energy_value;
		}
		
		protected void setX_coord(int new_x_coord) {
			super.x_coord = new_x_coord;
		}
		
		protected void setY_coord(int new_y_coord) {
			super.y_coord = new_y_coord;
		}
		
		protected int getX_coord() {
			return super.x_coord;
		}
		
		protected int getY_coord() {
			return super.y_coord;
		}
		

		/*
		 * This method getPopulation has to be modified by you if you are not using the population
		 * ArrayList that has been provided in the starter code.  In any case, it has to be
		 * implemented for grading tests to work.
		 */
		protected static List<Critter> getPopulation() {
			return population;
		}
		
		/*
		 * This method getBabies has to be modified by you if you are not using the babies
		 * ArrayList that has been provided in the starter code.  In any case, it has to be
		 * implemented for grading tests to work.  Babies should be added to the general population 
		 * at either the beginning OR the end of every timestep.
		 */
		protected static List<Critter> getBabies() {
			return babies;
		}
	}

	/**
	 * Clear the world of all critters, dead and alive
	 */
	public static void clearWorld() {
		population.clear();
	}
	
	/**
	 * We first apply the timestep appropriate to each of the alive critters and their species. Then we double check
	 * to ensure that no two critters are at the same location. If they are, then we must enact each of their fight methods
	 * and double check if the critters are both still alive and at the same location. If not, we can continue with the 
	 * next pair. If they are still at the same position, then each critter rolls a dice, and the critter with the higher
	 * return value stays alive, while the other dies. If they role the same number, then the winner is arbitrarily chosen.
	 * Lastly we add the babies to the population, refresh the algae count, and decrement the rest energy cost from each critter.
	 * The dead critters are then removed from the population.
	 * @throws InvalidCritterException
	 */
	public static void worldTimeStep() throws InvalidCritterException {
		//Gather up old critter locations for look function
		for (Critter crit : population) {
			old_x_vals.add(crit.x_coord);
			old_y_vals.add(crit.y_coord);
			old_crit_strings.add(crit.toString());
		}
		
		//Perform each critter's timestep, updating their position
			for (Critter crit : population) {
				crit.moved = false;
				crit.doTimeStep();
			}
		//movements applied
		// encounters resolve
		//offspring created
		//add new critters to collection
		//encounter takes place in next time step
		//cull dead critters
		//if  any energy has dropped to zero or below
		//apply Params.rest_enery to all critters before deciding if they are dead
		boolean A_fight;
		boolean B_fight;
		int A_roll;
		int B_roll;
		
		for (Critter A: population){
			for (Critter B: population){
				
				if(A.equals(B))
					continue;
				
				if(A.x_coord == B.x_coord && A.y_coord == B.y_coord && A.isAlive() && B.isAlive())
				{
					A_fight = A.fight(B.toString());
					B_fight = B.fight(A.toString());					//resolve encounter
					
					if(A.getEnergy()<=0)
						A.setAlive(false);
					if(B.getEnergy()<=0)
						B.setAlive(false);
								
					if (A.x_coord == B.x_coord && A.y_coord == B.y_coord && A.isAlive() && B.isAlive()) //still in the same position
					{
						
						if(A_fight)
							 A_roll = getRandomInt(A.energy);
							
						else 
							A_roll =0;
						
						if(B_fight)
							B_roll = getRandomInt(B.energy);
						else
							B_roll = 0;

						if (B_roll > A_roll){
							B.energy = B.energy + (A.energy/2);
							A.setAlive(false);
							A.energy =0;
						}	
							//kill A, check energy type
							
						else if (A_roll > B_roll){
							A.energy= A.energy +( B.energy/2);
							B.setAlive(false);
							B.energy=0;
							//kill B
						}
							
						else if (A_roll == B_roll){
							A.energy = A.energy +( B.energy/2);
							B.setAlive(false);
							B.energy =0;
						}
						
						}
					}

				}
				
			}
	
		//Add babies to population
		population.addAll(babies);
		
		//Update algae
		for (int i = 0; i < Params.refresh_algae_count; i++) {
			makeCritter("Algae"); //uncomment after debugging
		}


		for (Critter c : population){
			c.energy = c.energy - Params.rest_energy_cost;
			if (!(c.energy >0)){
				c.setAlive(false);
			}
		}
		
		Iterator <Critter> itcrit = population.iterator();
		List<Critter> dead = new ArrayList<Critter>();
		while(itcrit.hasNext()){
			Critter temp = itcrit.next();
			if(!temp.isAlive() || temp.energy <= 0)
				dead.add(temp);
		}
		population.removeAll(dead);
		
	}
	

	public static void displayWorld() {
		try {			
			
			int numCols = Params.world_height ;
	        int numRows = Params.world_width;
			for (int i = 0; i < numCols; i++) {
	            ColumnConstraints colConst = new ColumnConstraints();
	            colConst.setPercentWidth(100.0 / numCols);
	            grid.getColumnConstraints().add(colConst);
	        }
	        for (int i = 0; i < numRows; i++) {
	            RowConstraints rowConst = new RowConstraints();
	            rowConst.setPercentHeight(100.0 / numRows);
	            grid.getRowConstraints().add(rowConst);         
	        }
			grid.setGridLinesVisible(true);

			//Scene scene = new Scene(grid, 20*Params.world_height, 20*Params.world_width);
			Main.displayStage.setScene(scene);
			
			Main.displayStage.show();
			 
			// Paints the icons.
			paint();
		} catch(Exception e) {
			e.printStackTrace();		
		}
	}
	

	
	/*
	 * Paints the shape on a grid.
	 */
	public static void paint() {
		grid.getChildren().clear(); // clean up grid.
		Shape shape = null;
		Polygon p = new Polygon();
		Double[] trianglePoints ={10.0, 0.0, 15.0, 10.0, 5.0, 10.0 };
		Double[] diamondPoints ={10.0, 0.0,15.0, 5.0, 10.0, 10.0, 5.0, 5.0 };
		Double[] starPoints = {10.0,0.0,12.0 , 8.0, 20.0 , 8.0 , 12.0, 12.0, 16.0, 20.0, 10.0, 15.0,
				4.0, 20.0, 8.0, 12.0, 0.0, 8.0, 8.0, 8.0};
		
		
		for(Critter c: population){
			CritterShape s = c.viewShape();
			Color fillColor = c.viewFillColor();
			Color outlineColor = c.viewOutlineColor();
			int size =0;
			
			switch(s) {
			case CIRCLE: shape = new Circle(20/2); 
				shape.setFill(fillColor); 
				shape.setStroke(outlineColor);
				grid.add(shape, c.x_coord, c.y_coord); // add the shape to the grid.
				break;
			case SQUARE: shape = new Rectangle (20, 20); 
				shape.setFill(fillColor); 
				shape.setStroke(outlineColor);
				grid.add(shape, c.x_coord, c.y_coord); // add the shape to the grid.
				break;
			case STAR: p = new Polygon();
				p.getPoints().addAll(starPoints);
				p.setFill(fillColor);
				p.setStroke(outlineColor);
				grid.add(p, c.x_coord, c.y_coord); // add the shape to the grid.
				break;
			case DIAMOND: p = new Polygon();
				p.getPoints().addAll(diamondPoints);
				p.setFill(fillColor);
				p.setStroke(outlineColor);
				grid.add(p, c.x_coord, c.y_coord); // add the shape to the grid.
				break;
			case TRIANGLE: p = new Polygon(); 
				p.getPoints().addAll(trianglePoints);
				p.setFill(fillColor);
				p.setStroke(outlineColor);
				grid.add(p, c.x_coord, c.y_coord); // add the shape to the grid.
				break;
			}
			
		
			
		}
		
	}
	
	/**
	 * Displays a character grid of the critters in the population list by indicating their string value
	 * in the appropriate spot. The edges of the grid are formed with pipes, dashes, and pluses in the corners.
	 */
	/*
	public static void displayWorld() {
		
		//ensure correct worldGrid
	
		 
		for (Critter crit : population)
			worldGrid[crit.y_coord][crit.x_coord] = crit.toString();
		
		// top "+-----+"
		String top = "+";
		for (int i = 0; i < Params.world_width; i ++) {
			top = top + "-";
		}
		top = top + "+";
		System.out.println(top);
		
		//middle - critters with left and right walls
		String middle = "|";
		for (int h = 0; h < Params.world_height; h++) { //for each row
			for (int w = 0; w < Params.world_width; w++) {
				if (worldGrid[h][w] == null){
					middle = middle + " ";
				} else {
					middle = middle + worldGrid[h][w];
				}
			}
			middle = middle + "|";
			System.out.println(middle);
			middle = "|";
		}
		
		//bottom = top
		System.out.println(top);
		
		
		
	}
	*/
/**
 * Helper method used to set the critter as alive
 * @param alive
 */
	private void setAlive(boolean alive) {
		this.alive = alive;
	}
}
