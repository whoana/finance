package whoana.finance.service;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.util.Calendar;
import java.util.List;

import org.apache.commons.math3.stat.descriptive.moment.GeometricMean;
import org.apache.commons.math3.stat.descriptive.summary.SumOfLogs;
import org.h2.util.geometry.GeometryUtils;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
//import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTestContextBootstrapper;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import whoana.finance.util.Util;
import yahoofinance.Stock;
import yahoofinance.YahooFinance;
import yahoofinance.histquotes.Interval;
import yahoofinance.quotes.fx.FxQuote;


//@RunWith(SpringBootTestContextBootstrapper.class)
//@ContextConfiguration(locations={"classpath:/finance-context.xml"})
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
public class StockServiceTest {

	Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	StockService stockService;


	@Test
	public void getDollarWonFxTest2() throws Exception {
		try {
			LocalDate from = LocalDate.parse("20181231", DateTimeFormatter.BASIC_ISO_DATE);
			LocalDate to   = LocalDate.now().plusDays(1);
			for(LocalDate date = from ; date.isBefore(to) ; date = date.plusDays(1)) {
				String tday = date.format(DateTimeFormatter.BASIC_ISO_DATE);
				stockService.getFx("USDKRW=X", tday);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw e;
		}
	}

	@Test
	public void getDollarWonFxTest() throws Exception {
		try {
			stockService.getDollarWonFx();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw e;
		}
	}

	@Test
	public void geometryMeanTest() {
		SumOfLogs sl = new SumOfLogs();
		System.out.println(sl.evaluate(new double[] {1,2,3,4,5,6,7,8,9,10}));

		GeometricMean gm = new GeometricMean(sl);
		System.out.println(gm.evaluate());
	}

	@Test
	public void updateAllStocInfoTest() throws Exception {
		try {
			stockService.getDollarWonFx();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw e;
		}

		try {
			stockService.updateAllStrockInfo("20210111");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw e;
		}
	}


	@Test
	public void getStockSymbolsTest() {
		try {
			stockService.getStockSymbols();
		}catch(Exception e) {
			e.printStackTrace();
		}
	}

	@Test
	public void updateStockInfoTest() {
		try {
		String symbol = "AAPL";
		String fromDay = "20200101";
		stockService.updateStockInfo(symbol, fromDay);
		}catch(Exception e) {
			e.printStackTrace();
		}
	}

	@Test
	void test() {
		//fail("Not yet implemented");

		//String symbol = "292190.KS";
		String symbol = "DIS";
		boolean includeHistorical = true;
		try {
			Calendar from = Calendar.getInstance();
			from.set(Calendar.YEAR, 2020);
			from.set(Calendar.MONTH, 9);
			from.set(Calendar.DAY_OF_MONTH, 16);

			Calendar to = Calendar.getInstance();
			to.set(Calendar.YEAR, 2020);
			to.set(Calendar.MONTH, 9);
			to.set(Calendar.DAY_OF_MONTH, 16);



			//Stock stock = YahooFinance.get(symbol, from, to, Interval.DAILY);
			//Stock stock = YahooFinance.get(symbol); history price 는 제외하고 가져온다.
			Stock stock = YahooFinance.get(symbol, from);
			//System.out.println("stock:"+Util.toJSONPrettyString(stock));
			//StockDividend sd = stock.getDividend();
			//System.out.println("exDate:"+sd.getExDate().toString());
			//System.out.println("exDate:"+sd.getPayDate().toString());
			//System.out.println("exDate:"+sd.getAnnualYield());
			//System.out.println("exDate:"+sd.getAnnualYieldPercent());

			//System.out.println("dividend:" + sd.getAnnualYieldPercent());





		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}


	@Test
	public void testGetMySymbols(){
		List<String> symbols = stockService.getMySymbols();
		symbols.stream().forEach(symbol ->{System.out.println(symbol);});
	}

	@Test
	public void testUpsertStockInfo() {
		logger.debug("1 종목 리스트 조회 & 정보 업데이트");


//		//2017
//		Calendar from = Calendar.getInstance();
//		from.set(Calendar.YEAR, 2018);
//		from.set(Calendar.MONTH, 0);
//		from.set(Calendar.DAY_OF_MONTH, 1);
//
//		Calendar to = Calendar.getInstance();
//		to.set(Calendar.YEAR, 2019);
//		to.set(Calendar.MONTH, 0);
//		to.set(Calendar.DAY_OF_MONTH, 1);

//		//2018
//		Calendar from = Calendar.getInstance();
//		from.set(Calendar.YEAR, 2019);
//		from.set(Calendar.MONTH, 0);
//		from.set(Calendar.DAY_OF_MONTH, 1);
//
//		Calendar to = Calendar.getInstance();
//		to.set(Calendar.YEAR, 2020);
//		to.set(Calendar.MONTH, 0);
//		to.set(Calendar.DAY_OF_MONTH, 1);


//		//2019
//		Calendar from = Calendar.getInstance();
//		from.set(Calendar.YEAR, 2019);
//		from.set(Calendar.MONTH, 0);
//		from.set(Calendar.DAY_OF_MONTH, 1);
//
//		Calendar to = Calendar.getInstance();
//		to.set(Calendar.YEAR, 2020);
//		to.set(Calendar.MONTH, 0);
//		to.set(Calendar.DAY_OF_MONTH, 1);

//		2020
//		Calendar from = Calendar.getInstance();
//		from.set(Calendar.YEAR, 2020);
//		from.set(Calendar.MONTH, 0);
//		from.set(Calendar.DAY_OF_MONTH, 1);
//
//		Calendar to = Calendar.getInstance();
//		to.set(Calendar.YEAR, 2021);
//		to.set(Calendar.MONTH, 0);
//		to.set(Calendar.DAY_OF_MONTH, 1);




		//2021
		Calendar from = Calendar.getInstance();
		from.set(Calendar.YEAR, 2021);
		from.set(Calendar.MONTH, 11);
		from.set(Calendar.DAY_OF_MONTH, 14);

		Calendar to = Calendar.getInstance();
		to.set(Calendar.YEAR, 2021);
		to.set(Calendar.MONTH, 11);
		to.set(Calendar.DAY_OF_MONTH, 25);

		List<String> symbols = stockService.getMySymbols();
		symbols.stream().forEach(symbol ->{
			try {
				//if(symbol.contains(".KS")) {
					logger.debug("upsert symbol:" + symbol);
					stockService.upsertStockInfo(symbol, from, to);
				//}
			} catch (Exception e) {
				logger.error("upsert error symbol:" + symbol, e);
				try { Thread.sleep(5000); } catch (InterruptedException ex) { }
			}
		});

	}

}
