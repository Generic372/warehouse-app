package com.generic.models;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import org.json.simple.JSONObject;

import com.fasterxml.jackson.databind.ObjectMapper;
/**
 * Implement of data(Json) persistence
 * @author GENERIC TEAM
 *
 */
public abstract class PersistentJson implements IPersistentJson {
	protected String id;

	public abstract JSONObject toJSON();

	@Override
	public String getId() {
		return id;
	}

	/**
	 * Saves a file in a default(output) folder
	 * located in the project's folder.
	 * @param fileName name of the file to save.
	 */
	@Override
	public void save(String fileName) {
		//String filePath = "output/"+ filename;
		String filePath = "output/"+ fileName;
		File file = new File(filePath);

		// prettify the json output
		ObjectMapper mapper = new ObjectMapper();

		// Check and create directory
		if (!file.getParentFile().exists())
			file.getParentFile().mkdirs();

		//Write JSON file
		try (FileWriter fw = new FileWriter(filePath)) {
			PrintWriter printWriter = new PrintWriter(fw);
			String json = mapper
					.writerWithDefaultPrettyPrinter()
					.writeValueAsString(toJSON());

			printWriter.println(json);
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}
	}

	/**
	 * Saves a file in an already specified directory
	 * @param filePath the file path containing
	 * 		  directory and name to save exported file in.
	 */
	public void saveToDir(String filePath) {

		// prettify the json output
		ObjectMapper mapper = new ObjectMapper();

		//Write JSON file
		try (FileWriter fw = new FileWriter(filePath)) {
			PrintWriter printWriter = new PrintWriter(fw);
			String json = mapper
					.writerWithDefaultPrettyPrinter()
					.writeValueAsString(toJSON());

			printWriter.println(json);
		}catch (IOException e) {
			System.out.println(e.getMessage());
		}
	}
}
