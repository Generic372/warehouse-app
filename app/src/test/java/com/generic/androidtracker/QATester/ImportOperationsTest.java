package com.generic.androidtracker.QATester;

import com.generic.models.WarehouseFactory;
import com.generic.utils.IParser;
import com.generic.utils.JsonParser;
import com.generic.utils.Parsers;
import com.generic.utils.XmlParser;

import org.junit.Test;

import java.io.File;
import java.util.logging.Logger;

import static org.junit.Assert.assertEquals;

/**
 * A class to run unit tests against all import operations
 * @author GENERIC TEAM
 *
 */
public class ImportOperationsTest {

	private final Logger logger = Logger.getAnonymousLogger();

	@Test
	public void parseJson() {

		File file = new File("resource/example.json");
		String absoluteFilePath = file.getAbsolutePath();

		IParser jsonParser = new JsonParser();

		try {
			jsonParser.parse(absoluteFilePath);
		} catch (Exception e) {
			logger.info(e.getMessage());
		}

		WarehouseFactory warehouseFactory = WarehouseFactory.getInstance();

		int warehouseSize = warehouseFactory.getWarehousesList().size();

		// assert that the number of warehouses parsed
		// into object is same as the warehouse size in json file
		assertEquals(3, warehouseSize);
		warehouseFactory.deleteAllWarehouses();
	}

	@Test
	public void parseXml() {
		File xmlFile = new File("resource/example2.xml");
		IParser xmlParser = new XmlParser();
		try {
			xmlParser.parse(xmlFile.getAbsolutePath());
		} catch (Exception e) {
			logger.info(e.getLocalizedMessage());
		}

		WarehouseFactory warehouseFactory = WarehouseFactory.getInstance();
		int warehouseSize = warehouseFactory.getWarehousesList().size();

		// assert that the number of warehouses is
		// same as the warehouse size in xml file
		assertEquals(2, warehouseSize);
		warehouseFactory.deleteAllWarehouses();

	}

}
