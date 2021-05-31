package com.poc.mocktest;

import java.io.FileReader;
import java.io.FileWriter;
import java.util.List;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;

public class TestStock {

	public static void main(String[] args) throws Exception {
		CSVReader reader2 = new CSVReader(new FileReader("G:\\Stocks1\\stock_csv\\option-chain-ED-NIFTY-27-May-2021.csv"));
		List<String[]> allElements = reader2.readAll();
		allElements.remove(0);
		FileWriter sw = new FileWriter("G:\\Stocks1\\stock_csv\\option-chain-ED-NIFTY-27-May-2021.csv");
		CSVWriter writer = new CSVWriter(sw);
		writer.writeAll(allElements);
		writer.close();

	}

}
