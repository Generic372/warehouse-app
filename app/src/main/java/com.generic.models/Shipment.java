package com.generic.models;

import org.json.simple.JSONObject;

import com.generic.utils.DateUtil;

/**
 * @author GENERIC TEAM
 * This models a Shipment
 */

public class Shipment extends PersistentJson {

	private static final String SHIPMENT_DETAIl_FORMAT_STRING = "Shipment_Id: %s\n  Freight_Type: %s\n  Weight: %.2f\n  Departure_Date: %s\n Receipt_Date: %s\n  Weight_Unit: %s";

	private FreightType freight; // Freight type
	private WeightUnit weightUnit; // Shipment weight unit
	private double weight; // Shipment weight
	private long receiptDate; // Receipt Date
	private long departureDate; //Departure Date

	/**
	 * Constructs a new Shipment
	 * @param shipmentID shipment identification number
	 * @param freight freight type
	 * @param weight shipment weight
	 * @param receiptDate shipment receipt
	 */
	private Shipment(String shipmentID, FreightType freight, double weight, long receiptDate, WeightUnit weightUnit) {
		this.id = shipmentID;
		this.freight = freight;
		this.weight = weight;
		this.receiptDate = receiptDate;
		this.weightUnit = weightUnit; // Handles .json Null fields?
	}


	public String getShipmentID() {
		return id;
	}

	public FreightType getFreight() {
		return freight;
	}

	public double getWeight() {
		return weight;
	}

	public long getReceiptDate() {
		return receiptDate;
	}

	public String getReceiptDateString() {
		return DateUtil.milliToDate(receiptDate);
	}

	public long getDepartureDate() { return departureDate; }

	public String getDepartureDateString() {
		if (departureDate == 0L) {
			return "Shipment has not Departed";
		} else {
			return DateUtil.milliToDate(departureDate);
		}
	}

	public WeightUnit getWeightUnit() { return weightUnit; }

	/**
	 *
	 * Set the departure date to current date
	 */
	public void departureDate() {
		departureDate = DateUtil.currentDate();
	}

	@Override
	public String toString() {
		return String.format(SHIPMENT_DETAIl_FORMAT_STRING, id, freight.toString().toLowerCase(), weight, getDepartureDateString(), getReceiptDateString(), getWeightUnit().toString());
	}

	@Override
	@SuppressWarnings("unchecked")
	public JSONObject toJSON() {
		JSONObject shipmentJSON = new JSONObject();
		shipmentJSON.put("shipment_method", freight.toString().toLowerCase());
		shipmentJSON.put("shipment_id", id);
		shipmentJSON.put("weight", weight);
		shipmentJSON.put("weight_unit", weightUnit.toString().toLowerCase());
		shipmentJSON.put("receipt_date", receiptDate);

		return shipmentJSON;
	}

	public static final class Builder {
		private String shipmentID;
		private FreightType freight;
		private double weight;
		private long receiptDate;
		private WeightUnit weightUnit;

		public Builder() {}

		public Builder id(String shipmentID) {
			this.shipmentID = shipmentID;
			return this;
		}

		public Builder type(FreightType type) {
			this.freight = type;
			return this;
		}

		public Builder weight(double weight) {
			this.weight = weight;
			return this;
		}

		public Builder date(long receiptDate) {
			this.receiptDate = receiptDate;
			return this;
		}

		public Builder weightUnit(WeightUnit weightUnit) {
			this.weightUnit = weightUnit;
			return this;
		}

		public Shipment build() {
			return new Shipment(shipmentID, freight, weight, receiptDate, weightUnit);
		}
	}
}
