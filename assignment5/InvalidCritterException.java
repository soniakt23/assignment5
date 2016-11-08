/* CRITTERS InvalidCritterException.java
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

public class InvalidCritterException extends Exception {
	String offending_class;
	
	public InvalidCritterException(String critter_class_name) {
		offending_class = critter_class_name;
	}
	
	public String toString() {
		return "Invalid Critter Class: " + offending_class;
	}

}
