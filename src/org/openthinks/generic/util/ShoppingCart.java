package org.openthinks.generic.util;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;

public class ShoppingCart {
	/**
	 * A table of items in shopping cart
	 */
	HashMap<String, ShoppingCartItem> items;

	int numberOfItems;

	public ShoppingCart() {
		items = new HashMap<String, ShoppingCartItem>();
	}

	public synchronized void addItem(ShoppingCartItem item) {
		String itemId = item.getId();

		if (items.containsKey(itemId)) {
			ShoppingCartItem scitem = (ShoppingCartItem) items.get(itemId);
			scitem.setQuantity(scitem.getQuantity() + item.getQuantity());
		} else {
			items.put(itemId, item);
		}

		numberOfItems++;
	}

	public synchronized void removeItemById(String itemId) {
		items.remove(itemId);
		numberOfItems--;

	}

	public synchronized Collection<ShoppingCartItem> getItems() {
		return items.values();
	}

	public synchronized int getNumberOfItems() {
		return numberOfItems;
	}

	public synchronized double getTotalAmount() {
		double amount = 0.0;

		for (Iterator<?> i = getItems().iterator(); i.hasNext();) {
			ShoppingCartItem item = (ShoppingCartItem) i.next();
			amount += item.getQuantity() * item.getUnitPrice();
		}
		
		return roundOff(amount);
	}

	private double roundOff(double x) {
		long val = Math.round(x * 100); // cents
		return val / 100.0;
	}

	public synchronized void clear() {
		items.clear();
		numberOfItems = 0;
	}

	public void updateQuantity(String itemId, int quantity) {
		ShoppingCartItem scitem = (ShoppingCartItem) items.get(itemId);
		numberOfItems += quantity - scitem.getQuantity();
		scitem.setQuantity(quantity);
	}

	protected void finalize() throws Throwable {
		items.clear();
	}

}
