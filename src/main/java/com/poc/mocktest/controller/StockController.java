package com.poc.mocktest.controller;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.sql.Date;
import java.util.Calendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import com.poc.mocktest.model.OptionData;
import com.poc.mocktest.repository.OptionRepository;

@RestController
public class StockController {

	private static final String G_STOCKS_STOCK_CSV = "G:\\Stocks1\\stock_csv\\";

	@Autowired
	private OptionRepository optionRepository;

	@GetMapping(path = "/postdata")
	public String postData() throws IOException {
		Map<String, Integer> priceMap = new HashMap<String, Integer>();
		priceMap.put("NIFTY", 15208);
		Map<String, Integer> map = new HashMap<String, Integer>();
		map.put("NIFTY", 50);
		Map<String, Set<Integer>> listMap = new HashMap<String, Set<Integer>>();
		Integer base = priceMap.get("NIFTY") + (map.get("NIFTY") - priceMap.get("NIFTY") % map.get("NIFTY"));
		String[] pathnames;

		// String[] headers = new String[] {"OI","CHNG IN OI","VOLUME","STRIKE PRICE" };
		Set<Integer> rangeList = new HashSet<Integer>();
		for (int i = 0; i <= 10; i++) {
			Integer temp = base + map.get("NIFTY") * i;
			if (temp % 500 == 0) {
				for (int j = 1; j <= 3; j++) {
					rangeList.add(temp + 500 * j);
				}
				for (int k = 1; k <= 3; k++) {
					rangeList.add(temp - 500 * k);
				}
			}
			rangeList.add(temp);
		}
		for (int i = 1; i <= 10; i++) {
			rangeList.add(Integer.valueOf(String.valueOf(base - 50 * i).trim()));
		}
		listMap.put("NIFTY", rangeList);
		File f = new File(G_STOCKS_STOCK_CSV);
		pathnames = f.list();
		OptionData od = new OptionData();
		for (String pathname : pathnames) {
			editcsv(G_STOCKS_STOCK_CSV + pathname);
			try (Reader reader = new FileReader(G_STOCKS_STOCK_CSV + pathname);

					CSVParser csvParser = new CSVParser(reader, CSVFormat.DEFAULT.withFirstRecordAsHeader()
							.withIgnoreHeaderCase().withTrim().withAllowMissingColumnNames(true))) {
				/*
				 * CSVParser csvParser = new CSVParser(reader,
				 * CSVFormat.DEFAULT.withFirstRecordAsHeader().withIgnoreHeaderCase().withTrim()
				 * .withAllowMissingColumnNames(true).withFirstRecordAsHeader().
				 * withIgnoreHeaderCase().withTrim().withAllowMissingColumnNames(true))) {
				 */
				for (CSVRecord record : csvParser) {
					Calendar cal = Calendar.getInstance();
					cal.set(2021, Calendar.MAY, Integer.parseInt(pathname.split("-")[4])); // Year, month and day of
																							// month
					java.util.Date date = cal.getTime();
					OptionData od1 = new OptionData();
					String strikePrice = record.get("STRIKE PRICE").replace(",", "").substring(0,
							record.get("STRIKE PRICE").indexOf(".") - 1);
					if (rangeList.contains(Integer.valueOf(strikePrice))) {
						String openInterst = record.get(1);
						String volume = record.get(3);
						od1.setVolume(volume);
						od1.setOpeninterst(openInterst);
						od1.setInsName("NIFTY");
						od1.setOptionType("CALL");
						od1.setPremium(record.get(5));
						od1.setStrikePrice(strikePrice);
						od1.setCurDate(new Date(System.currentTimeMillis()));
						od1.setExpDate(new Date(date.getTime()));
						od1.setClosingPrice(String.valueOf(priceMap.get("NIFTY")));
						od1.setMonth(pathname.split("-")[5]);
						od1.setYear("2021");
						optionRepository.save(od1);
					}
				}
			}
		}
		for (String pathname : pathnames) {

			try (Reader reader = new FileReader(G_STOCKS_STOCK_CSV + pathname);

					CSVParser csvParser = new CSVParser(reader, CSVFormat.DEFAULT.withFirstRecordAsHeader()
							.withIgnoreHeaderCase().withTrim().withAllowMissingColumnNames(true))) {
				for (CSVRecord record : csvParser) {
					Calendar cal = Calendar.getInstance();
					cal.set(2021, Calendar.MAY, Integer.parseInt(pathname.split("-")[4])); // Year, month and day of
																							// month
					java.util.Date date = cal.getTime();
					OptionData od1 = new OptionData();
					String strikePrice = record.get("STRIKE PRICE").replace(",", "").substring(0,
							record.get("STRIKE PRICE").indexOf(".") - 1);
					if (rangeList.contains(Integer.valueOf(strikePrice))) {
						String openInterst = record.get(21);
						String volume = record.get(19);
						od1.setVolume(volume);
						od1.setOpeninterst(openInterst);
						od1.setInsName("NIFTY");
						od1.setOptionType("PUT");
						od1.setPremium(record.get(17));
						od1.setStrikePrice(strikePrice);
						od1.setCurDate(new Date(System.currentTimeMillis()));
						od1.setExpDate(new Date(date.getTime()));
						od1.setClosingPrice(String.valueOf(priceMap.get("NIFTY")));
						od1.setMonth(pathname.split("-")[5]);
						od1.setYear("2021");
						optionRepository.save(od1);
					}
				}
			}
		}
		return "success";
	}

	private void editcsv(String path) {
		try {
			CSVReader reader2 = new CSVReader(new FileReader(path));
			List<String[]> allElements = reader2.readAll();
			allElements.remove(0);
			FileWriter sw = new FileWriter(path);
			CSVWriter writer = new CSVWriter(sw);
			writer.writeAll(allElements);
			writer.close();
		} catch (Exception e) {

		}

	}

}
