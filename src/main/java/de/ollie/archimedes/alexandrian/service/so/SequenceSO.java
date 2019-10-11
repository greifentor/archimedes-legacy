package de.ollie.archimedes.alexandrian.service.so;

/**
 * An interface for the sequences of a data scheme.
 * 
 * @author Oliver.Lieshoff
 *
 */
public interface SequenceSO {

	/**
	 * Returns the increment of the sequence.
	 * 
	 * @return The increment of the sequence.
	 */
	int getIncrement();

	/**
	 * Returns the name of the sequence.
	 * 
	 * @return The name of the sequence.
	 */
	String getName();

	/**
	 * Returns the start value of the sequence.
	 * 
	 * @return The start value of the sequence.
	 */
	int getStart();

}