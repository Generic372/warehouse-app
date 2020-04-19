package com.generic.xml.models;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

/**
 * This class models a shipment element
 * adhering to simplexml API requirements.
 * @author GENERIC TEAM
 *
 */
@Root(name = "Shipment")
public class ShipmentXml {

	@Attribute(name = "type", required = false)
	String type;

	@Attribute(name = "id")
	String id;

	@Element(name = "ReceiptDate", required = false)
	long receiptDate;

	@Element(name = "Weight", required = false)
	Weight Weight;

	public String getType() {
		return type.toUpperCase();
	}

	public String getId() {
		return id;
	}

	@Override
	public String toString() {
		return String.format("ID: %s || Type: %s || Receipt Date: %s || Weight: %.2f || Unit: %s\n"
				, id, type, Long.toString(receiptDate), Weight.getWeight(), Weight.getUnit());
	}

	public long getReceiptDate() {
		return receiptDate;
	}

	public double getWeight() {
		return Weight.getWeight();
	}

	public String getWeightUnit() {
		return Weight.getUnit().toUpperCase();
	}




}
