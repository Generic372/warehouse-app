package com.generic.xml.models;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Text;

/**
 * This class models a weight element
 * adhering to simplexml API requirements.
 * @author GENERIC TEAM
 *
 */
public class Weight {

	@Text
	double weight;

	@Attribute(name = "unit")
	String unit;

	public double getWeight () {
		return weight;
	}

	public String getUnit () {
		return unit;
	}
}
