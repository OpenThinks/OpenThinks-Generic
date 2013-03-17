package org.openthinks.generic.pattern;

/**
 * A part of interfaces for Push-Mode Observer Pattern.
 * 
 * Interface of observer, updates will be pushed by observed subject.
 * 
 * @author Zhang Junlong
 * 
 * @param <T>
 *            actual type of observer
 * @param <ID>
 *            actual type of ID of observer
 */
public interface PushObserver<T, ID> {
	/**
	 * Get ID of this observer.
	 * 
	 * @return
	 */
	public ID getId();

	void update(T updates);

	/**
	 * Notifies this observer has derigistered.
	 * 
	 */
	void onDeregister();
}
