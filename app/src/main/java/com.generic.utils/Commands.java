package com.generic.utils;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import com.generic.models.FreightType;
import com.generic.models.PersistentJson;
import com.generic.models.Shipment;
import com.generic.models.WarehouseFactory;

@Deprecated
public class Commands {
	private static Commands command;

	private Map<String, Executable> commandList;

	private Commands() {}

	public void execute(String command, String[] args) throws CommandsException {
		commandList.get(command).execute(args);
	}

	public static Commands getInstance() {
		if (command == null) {
			synchronized(Commands.class) {
				command = new Commands();
				command.commandList = new HashMap<>();
				command.init();
			}
		}
		return command;
	}

	public List<String> getCommandList() {
		return new ArrayList<>(commandList.keySet());
	}

	public boolean exist(String command) {
		return commandList.containsKey(command);
	}

	private void init() {
		// some annotations here to help users how to use commands
		commandList.put("import", (arg -> {
			try {
				Parsers.parseJson(new File("resource/" + arg[0]).getAbsolutePath());
				System.out.println("Sucessfully imported "+arg[0]);
			} catch (Exception e) {
				System.out.println("** System can not read the file!");
				e.printStackTrace();
			}
		}));

		commandList.put("export", (arg -> {
			WarehouseFactory warehouseTracker = WarehouseFactory.getInstance();
			try {
				String mWarehouseID = arg[0];
				if (!warehouseTracker.warehouseExists(mWarehouseID)) {
					return;
				}
				PersistentJson warehouse = warehouseTracker.getWarehouse(mWarehouseID);
				warehouse.save(warehouse.getId()+".json");

				System.out.println("Sucessfully exported warehouse");
			} catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {
				System.out.println("** Please input a warehouse ID to export");
			}
		}));

		commandList.put("print", (arg -> {
			String warehouseID;
			try {
				WarehouseFactory warehouseTracker = WarehouseFactory.getInstance();
				warehouseID = arg[0];
				warehouseTracker.printWarehouseDetails(warehouseID);
			} catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {
				System.out.println("** Please input a warehouseID");
			}
		}));

		commandList.put("printall", (arg -> {
			WarehouseFactory warehouseTracker = WarehouseFactory.getInstance();
			if (!warehouseTracker.isEmpty()) {
				warehouseTracker.printAll();
			} else {
				System.out.println("** No warehouses avaliable, please import data first");
			}
		}));

		commandList.put("enablef", (arg -> {
			String warehouseID1;
			WarehouseFactory warehouseTracker = WarehouseFactory.getInstance();
			try {
				warehouseID1 = arg[0];
				if (!warehouseTracker.isEmpty()) {
					warehouseTracker.enableFreight(warehouseID1);
					System.out.println("** Freight enabled for warehouse #" + warehouseID1);

				} else {
					System.out.println("** No warehouse avaliable, please import data first");
				}
			} catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {
				System.out.println("** Please input an integer");
			}
		}));

		commandList.put("disablef", (arg -> {
			String warehouseID2;
			WarehouseFactory warehouseTracker = WarehouseFactory.getInstance();
			try {
				warehouseID2 = arg[0];
				if (!warehouseTracker.isEmpty()) {
					warehouseTracker.endFreight(warehouseID2);
					System.out.println("** Freight ended for warehouse #" + warehouseID2);

				} else {
					System.out.println("** No warehouses avaliable, please import data first");
				}
			} catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {
				System.out.println("** Please input an integer");
			}
		}));

		commandList.put("exit", (arg -> {
			throw new CommandsException("Exit");
		}));

		commandList.put("add", (arg -> {
			WarehouseFactory warehouseTracker = WarehouseFactory.getInstance();
			try {
				String mWarehouseID = arg[0];
				if (!warehouseTracker.isEmpty()) {

					if (!warehouseTracker.warehouseExists(mWarehouseID)) {
						System.out.println("** Warehouse with ID #" + mWarehouseID + " does not exist");
					} else if (!warehouseTracker.freightIsEnabled(mWarehouseID)) {
						System.out.println("** Warehouse with id #" + mWarehouseID + " freight has ended");
					} else{
						boolean added = addShipmentOp(mWarehouseID);
						if (!added) {
							System.out.println("** Shipment could not be added because"
									+ " freight has ended for this warehouse");
						}
					}
				} else {
					System.out.println("** No warehouses with avaliable, please import data first");
				}
			} catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {
				System.out.println("** Please input a warehouse_id");
			}
		}));

		commandList.put("exportall", (arg -> {
			try {
				PersistentJson warehouseTracker = WarehouseFactory.getInstance();
				warehouseTracker.save(warehouseTracker.getId()+".json");

				System.out.println("Sucessfully exported warehouse");
			} catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {
				System.out.println("** Please input a warehouse ID to export");
			}
		}));
	}

	/**
	 * Adds shipments to the warehouse using the command line interface
	 *
	 * @param warehouseID
	 * @return
	 */
	@Deprecated
	private boolean addShipmentOp(String warehouseID) {
		Scanner sc = new Scanner(System.in);
		WarehouseFactory warehouseTracker = WarehouseFactory.getInstance();
		boolean added = false;
		String option = "";

		do {
			String shipmentID;
			String shipmentString;
			long receiptDate = 0;
			double weight = 0;

			System.out.print("Enter shipmentID(i.e. iafr4, 15566) -> ");
			shipmentID = sc.next();

			System.out.print("Enter shipment_method(i.e air, truck, ship, rail) -> ");

			//TODO:
			ArrayList<String> shipmentMethodList = new ArrayList<String>(Arrays.asList(new String [] {FreightType.AIR.toString()
					, FreightType.RAIL.toString()
					, FreightType.TRUCK.toString()
					, FreightType.SHIP.toString()}));
			shipmentString = sc.next().toUpperCase();
			while(!shipmentMethodList.contains(shipmentString))
			{
				System.out.print("Please enter a valid shipment_method(i.e air, truck, ship, rail) -> ");
				shipmentString = sc.next().toUpperCase();
			}

			System.out.print("Enter weight(i.e. 84.71, 321) -> ");
			while (!sc.hasNextDouble()) {
				System.out.print("Please enter a valid weight(i.e. 84.71, 321) -> ");
				sc.next();
			}
			weight = sc.nextDouble();

			System.out.print("Enter receipt_date(i.e 1515354694451) -> ");
			while (!sc.hasNextDouble()) {
				System.out.print("Please enter a valid receipt_date(i.e 1515354694451) -> ");
				sc.next();
			}
			receiptDate = sc.nextLong();

			// build a shipment
			Shipment shipment = new Shipment.Builder()
					.id(shipmentID)
					.type(FreightType.valueOf(shipmentString))
					.weight(weight)
					.date(receiptDate)
					.build();

			// add the shipment to the warehouse
			added = warehouseTracker.addShipment(warehouseID, shipment);

			if (added) {
				System.out.println("Incoming shipment has been sucessfully added");
				System.out.print("Would you like to add another shipment to warehouse_" + warehouseID + "?(Y/N) -> ");
				while (!sc.hasNext()) {
					System.out.print("Please enter ('Y/N') ->");
				}
				option = sc.next();
			}

		} while (option.equalsIgnoreCase("Y") && added);

		sc.close();
		return added;
	}
}
