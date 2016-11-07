package assignment5;

/* CRITTERS Painter.java
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

import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;

public class Painter {

	/*
	 * Returns a square or a circle, according to shapeIndex
	 */
	static Shape getIcon(int shapeIndex) {
		Shape s = null;
		int size = 100;
		
		switch(shapeIndex) {
		case 0: s = new Rectangle(size, size); 
			s.setFill(javafx.scene.paint.Color.RED); break;
		case 1: s = new Circle(size/2); 
			s.setFill(javafx.scene.paint.Color.GREEN); break;
		}
		// set the outline of the shape
		s.setStroke(javafx.scene.paint.Color.BLUE); // outline
		return s;
	}
	
	/*
	 * Paints the shape on a grid.
	 */
	public static void paint() {
		Main.grid.getChildren().clear(); // clean up grid.
		for (int i = 0; i <= 1; i++) {
			Shape s = getIcon(i);	// convert the index to an icon.
			Main.grid.add(s, i, i); // add the shape to the grid.
		}
		
	}
}
