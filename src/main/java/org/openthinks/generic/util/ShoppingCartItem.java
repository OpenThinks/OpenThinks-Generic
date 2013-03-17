package org.openthinks.generic.util;

public interface ShoppingCartItem {
	
	abstract public String getId();

	abstract public float getQuantity();

	abstract public void setQuantity(float quantity);
	
	abstract public double getUnitPrice();
}
