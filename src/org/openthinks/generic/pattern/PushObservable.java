package org.openthinks.generic.pattern;

/**
 * 
 * A Part of interfaces for Push-Mode Observer Pattern.
 * 
 * Interface of observable subject, updates will be pushed to observers.
 * 
 * 
 * @author Zhang Junlong
 * 
 * @param <T>
 *            Type of updated message
 * @param <ID>
 *            ID type of Observer
 */
public interface PushObservable<T, ID> {

	/**
	 * Register a observer to the observable object.
	 * 
	 * @param observer
	 */
	void register(PushObserver<T, ID> observer);

	/**
	 * Deregister from a observing subject
	 * 
	 * @param id
	 */
	void deregister(PushObserver<T, ID> observer);

	/**
	 * Informs all observers to update with updates content.
	 * 
	 * @param updates
	 */
	void inform(T updates);

}
