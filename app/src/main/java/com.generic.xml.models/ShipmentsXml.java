package com.generic.xml.models;

import java.util.List;

import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;
/**
 * This class models a shipments(root node) element
 * adhering to simplexml API requirements.
 * @author GENERIC TEAM
 *
 */
@Root
public class ShipmentsXml {
	@ElementList(inline = true, name = "Warehouse")
	List<WarehouseXml> warehouses;

	public List<WarehouseXml> getWarehouse() {
		return warehouses;
	}
}
