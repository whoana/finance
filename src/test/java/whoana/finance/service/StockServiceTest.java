package whoana.finance.service;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.util.Calendar;

import org.apache.commons.math3.stat.descriptive.moment.GeometricMean;
import org.apache.commons.math3.stat.descriptive.summary.SumOfLogs;
import org.h2.util.geometry.GeometryUtils;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
//import org.junit.runner.RunWith;
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


//@RunWith(SpringBootTestContextBootstrapper.class)
//@ContextConfiguration(locations={"classpath:/finance-context.xml"})
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
public class StockServiceTest {

	@Autowired
	StockService stockService;

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
			Stock stock = YahooFinance.get(symbol, from);

			System.out.println("stock:"+Util.toJSONPrettyString(stock));
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

}
