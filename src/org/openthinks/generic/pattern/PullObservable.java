package org.openthinks.generic.pattern;

/**
 * A part of interfaces for Pull-Mode Observer Pattern.
 * 
 * Interface of observable subject could be observed by observers. Observers
 * could be informed if something updated ,but should pull what's update itself.
 * 
 * @author Zhang Junlong
 */
public interface PullObservable {
	/**
	 * Register an observer to this subject.
	 * 
	 * @param pullObserver
	 */
	void register(PullObserver pullObserver);

	/**
	 * Inform all observers to update, a default update method will be called.
	 */
	void inform();
}
