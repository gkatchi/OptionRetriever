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
public class StockControllerGen {

	private static final String SBI = "SBIN";

	private static final String NIFTY = "NIFTY";
	
	private static final String AXIS = "AXISBANK";
	
	private static final String HDFCBANK = "HDFCBANK";
	
	private static final String HDFC = "HDFC";
	
	private static final String RELIANCE = "RELIANCE";
	
	private static final String ICICI = "ICICIBANK";
	
	private static final String INFY = "INFY";
	
	private static final String TCS = "TCS";
	
	private static final String FINNIFTY = "FINNIFTY";
	
	private static final String BANKNIFTY = "BANKNIFTY";
	
	private static final String ITC = "ITC";

	private static final String G_STOCKS_STOCK_CSV = "G:\\Stocks1\\stock_csv\\";

	@Autowired
	private OptionRepository optionRepository;

	@GetMapping(path = "/postdata/all")
	public String postData() throws IOException {
		Map<String, Integer> priceMap = new HashMap<String, Integer>();
		Map<String, Integer> monthMap = new HashMap<String, Integer>();
		priceMap(priceMap);
		monthMap.put("Jun", Calendar.JUNE);
		Map<String, Integer> map = new HashMap<String, Integer>();
		stockMap(map);
		Map<String, Set<Integer>> listMap = new HashMap<String, Set<Integer>>();
		String[] pathnames;
		Set<Integer> rangeList = new HashSet<Integer>();
		File f = new File(G_STOCKS_STOCK_CSV);
		pathnames = f.list();
		OptionData od = new OptionData();
		for (String pathname : pathnames) {
			editcsv(G_STOCKS_STOCK_CSV + pathname);
			rangeList = popInsVals(priceMap, map, listMap,pathname.split("-")[3]);
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
					cal.set(2021, monthMap.get(pathname.split("-")[5]), Integer.parseInt(pathname.split("-")[4])); // Year, month and day of
																							// month
					java.util.Date date = cal.getTime();
					OptionData od1 = new OptionData();
					String strikePrice=null;
					if(record.get("STRIKE PRICE").contains(",")) {
					 strikePrice = record.get("STRIKE PRICE").replace(",", "").substring(0,
							record.get("STRIKE PRICE").indexOf(".") - 1);
					}
					else {
						strikePrice = record.get("STRIKE PRICE").substring(0,
								record.get("STRIKE PRICE").indexOf("."));
					}
					if (rangeList.contains(Integer.valueOf(strikePrice))) {
						String openInterst = record.get(1);
						String volume = record.get(3);
						od1.setVolume(volume);
						od1.setOpeninterst(openInterst);
						od1.setInsName(pathname.split("-")[3]);
						od1.setOptionType("CALL");
						od1.setPremium(record.get(5));
						od1.setStrikePrice(strikePrice);
						od1.setCurDate(new Date(System.currentTimeMillis()));
						od1.setExpDate(new Date(date.getTime()));
						od1.setClosingPrice(String.valueOf(priceMap.get(pathname.split("-")[3])));
						od1.setMonth(pathname.split("-")[5]);
						od1.setYear("2021");
						optionRepository.save(od1);
					}
				}
			}
		}
		for (String pathname : pathnames) {
			rangeList = popInsVals(priceMap, map, listMap,pathname.split("-")[3]);
			try (Reader reader = new FileReader(G_STOCKS_STOCK_CSV + pathname);

					CSVParser csvParser = new CSVParser(reader, CSVFormat.DEFAULT.withFirstRecordAsHeader()
							.withIgnoreHeaderCase().withTrim().withAllowMissingColumnNames(true))) {
				for (CSVRecord record : csvParser) {
					Calendar cal = Calendar.getInstance();
					cal.set(2021, monthMap.get(pathname.split("-")[5]), Integer.parseInt(pathname.split("-")[4])); // Year, month and day of
																							// month
					java.util.Date date = cal.getTime();
					OptionData od1 = new OptionData();
					String strikePrice=null;
					if(record.get("STRIKE PRICE").contains(",")) {
					 strikePrice = record.get("STRIKE PRICE").replace(",", "").substring(0,
							record.get("STRIKE PRICE").indexOf(".") - 1);
					}else {
						 strikePrice = record.get("STRIKE PRICE").substring(0,
									record.get("STRIKE PRICE").indexOf("."));
					}
					if (rangeList.contains(Integer.valueOf(strikePrice))) {
						String openInterst = record.get(21);
						String volume = record.get(19);
						od1.setVolume(volume);
						od1.setOpeninterst(openInterst);
						od1.setInsName(pathname.split("-")[3]);
						od1.setOptionType("PUT");
						od1.setPremium(record.get(17));
						od1.setStrikePrice(strikePrice);
						od1.setCurDate(new Date(System.currentTimeMillis()));
						od1.setExpDate(new Date(date.getTime()));
						od1.setClosingPrice(String.valueOf(priceMap.get(pathname.split("-")[3])));
						od1.setMonth(pathname.split("-")[5]);
						od1.setYear("2021");
						optionRepository.save(od1);
					}
				}
			}
		}
		return "success";
	}

	private void stockMap(Map<String, Integer> map) {
		map.put(NIFTY, 50);
		map.put(SBI, 5);
		map.put(AXIS, 10);
		map.put(HDFC, 50);
		map.put(HDFCBANK, 10);
		map.put(RELIANCE, 20);
		map.put(ICICI, 10);
		map.put(INFY, 20);
		map.put(TCS, 50);
		map.put(ITC, 5);
		map.put(FINNIFTY, 100);
		map.put(BANKNIFTY, 100);
		
	}

	private void priceMap(Map<String, Integer> priceMap) {
		priceMap.put(NIFTY, 15435);
		priceMap.put(SBI, 422);
		priceMap.put(AXIS, 743);
		priceMap.put(HDFCBANK, 1507);
		priceMap.put(HDFC, 2543);
		priceMap.put(RELIANCE, 2094);
		priceMap.put(TCS, 3143);
		priceMap.put(INFY, 1406);
		priceMap.put(ICICI, 643);
		priceMap.put(ITC, 213);
		priceMap.put(FINNIFTY, 16495);
		priceMap.put(BANKNIFTY, 35141);
	}

	private Set<Integer> popInsVals(Map<String, Integer> priceMap, Map<String, Integer> map,
			Map<String, Set<Integer>> listMap,String insName) {
		Integer base = priceMap.get(insName) + (map.get(insName) - priceMap.get(insName) % map.get(insName));
		String[] pathnames;

		// String[] headers = new String[] {"OI","CHNG IN OI","VOLUME","STRIKE PRICE" };
		Set<Integer> rangeList = new HashSet<Integer>();
		for (int i = 0; i <= 10; i++) {
			Integer temp = base + map.get(insName) * i;
			if (temp % 500 == 0 && "NIFTY".equals(insName)) {
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
			rangeList.add(Integer.valueOf(String.valueOf(base - map.get(insName) * i).trim()));
		}
		listMap.put(insName, rangeList);
		return rangeList;
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
