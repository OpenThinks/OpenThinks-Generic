package org.openthinks.generic.pattern;

/**
 * A part of interfaces for Pull-Mode Observer Pattern.
 * 
 * Interface of observer, observer will be informed if update occur, observer
 * should pull update content in its implementation.
 * 
 * @author Zhang Junlong
 * 
 */
public interface PullObserver {
	/**
	 * This method will be called when observed subject updates.
	 */
	void update();
}
