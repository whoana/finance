package whoana.finance.service;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import whoana.finance.util.Util;
import whoana.finance.data.Performance;
import whoana.finance.data.Portfolio;
import whoana.finance.data.Position;
import whoana.finance.mapper.StockMapper;
import yahoofinance.Stock;
import yahoofinance.YahooFinance;
import yahoofinance.histquotes.HistoricalQuote;
import yahoofinance.histquotes.Interval;
import yahoofinance.histquotes2.HistoricalDividend;
import yahoofinance.quotes.fx.FxQuote;
import yahoofinance.quotes.stock.StockDividend;
import yahoofinance.quotes.stock.StockQuote;
import yahoofinance.quotes.stock.StockStats;

@Service
public class StockService {
	Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	StockMapper stockMapper;


	@Transactional
	public FxQuote getFx(String symbol, String getDay) throws IOException {
		FxQuote fx = YahooFinance.getFx(symbol);
		String date = Util.getFormatedDate(Util.DEFAULT_DATE_FORMAT_MI);
		int exists = stockMapper.existsFx(symbol, getDay);
		if(exists == 0) {
			stockMapper.insertFx(date, getDay, fx);
		}else {
			stockMapper.updateFx(date, getDay, fx);
		}

		//System.out.println("fx:"+Util.toJSONPrettyString(fx));
		return fx;
	}


	@Transactional
	public FxQuote getFx(String symbol) throws IOException {

		String date = Util.getFormatedDate(Util.DEFAULT_DATE_FORMAT_MI);
		String getDay = date.substring(0, 8);

		return getFx( symbol,  getDay);
	}

	@Transactional
	public FxQuote getDollarWonFx() throws IOException {
		return getFx("USDKRW=X");
	}



	/**
	 * <pre>
	 *  야후 파이낸스를 통해 주식정보를 가져와 로컬 데이터로 저장한다.
	 * </pre>
	 * @param symbol
	 * @return
	 * @throws IOException
	 */
	@Transactional
	public Stock getStock(String symbol, String day) throws IOException {

		Calendar fromDate = Calendar.getInstance();
		fromDate.set(Calendar.YEAR, Integer.parseInt(day.substring(0, 4)));
		fromDate.set(Calendar.MONTH, Integer.parseInt(day.substring(4, 6)) - 1);
		fromDate.set(Calendar.DAY_OF_MONTH, Integer.parseInt(day.substring(6, 8)));
		logger.debug("fromDate:" + fromDate);

		Stock stock  = YahooFinance.get(symbol, fromDate, Interval.DAILY);
		if(stock == null) return null;
		//stock.print();
		String regDate = Util.getFormatedDate(Util.DEFAULT_DATE_FORMAT_MI);
		Map<String, String> stockInfo = new HashMap<String, String>();
		stockInfo.put("symbol",		stock.getSymbol());
		stockInfo.put("currency",	stock.getCurrency());
		stockInfo.put("exchange",	stock.getStockExchange());
		stockInfo.put("name",		stock.getName());
		stockInfo.put("regDate", 	regDate);
		stockInfo.put("modDate", 	regDate);



		int exists = stockMapper.existsStock(symbol);

		//String getDate = date.substring(0, 8);
		String getDate = day;
		int quoteExists = stockMapper.existsQuote(symbol);
		StockQuote quote = stock.getQuote();
		String lastTradeTime = Util.getFormatedDate(quote.getLastTradeTime().getTimeInMillis(), Util.DEFAULT_DATE_FORMAT_MI);
		quote.setLastTradeTimeStr(lastTradeTime);

		if(exists == 0) {
			stockMapper.insertStock(stockInfo);

		}else {
			stockMapper.updateStock(stockInfo);
		}

		if(quoteExists == 0) {
			stockMapper.insertQuote(regDate, getDate, quote);
		}else {
			stockMapper.updateQuote(regDate, getDate, quote);
		}

		StockStats stockStats = stock.getStats();

		String earningsAnnouncement = stockStats.getEarningsAnnouncement() == null ? "": Util.getFormatedDate(stockStats.getEarningsAnnouncement().getTimeInMillis(), "yyyyMMdd");
		double roe = stockStats.getROE() == null ? 0 : stockStats.getROE().doubleValue();
		double ebitda = stockStats.getEBITDA() == null ? 0 : stockStats.getEBITDA().doubleValue();
		int stockStatsExist = stockMapper.existsStockStats(symbol);
		if(stockStatsExist == 0) {
			stockMapper.insertStockStats(regDate, getDate, earningsAnnouncement, roe, ebitda, stockStats);
		}else {
			stockMapper.updateStockStats(regDate, getDate, earningsAnnouncement, roe, ebitda, stockStats);
		}




		return stock;
	}


	/**
	 * <pre>
	 *  야후 파이낸스를 통해 주식정보를 가져와 로컬 데이터로 저장한다.
	 * </pre>
	 * @param symbol
	 * @return
	 * @throws IOException
	 */
	@Transactional
	public Stock updateStockInfo(String symbol, String day) throws IOException {

		Calendar fromDate = Calendar.getInstance();
		fromDate.set(Calendar.YEAR, Integer.parseInt(day.substring(0, 4)));
		fromDate.set(Calendar.MONTH, Integer.parseInt(day.substring(4, 6)) - 1);
		fromDate.set(Calendar.DAY_OF_MONTH, Integer.parseInt(day.substring(6, 8)));
		logger.debug("fromDate:" + fromDate);

		Stock stock  = YahooFinance.get(symbol, fromDate, Interval.DAILY);

		if(stock == null) return null;
		//stock.print();
		String regDate = Util.getFormatedDate(Util.DEFAULT_DATE_FORMAT_MI);
		Map<String, String> stockInfo = new HashMap<String, String>();
		stockInfo.put("symbol",		stock.getSymbol());
		stockInfo.put("currency",	stock.getCurrency());
		stockInfo.put("exchange",	stock.getStockExchange());
		stockInfo.put("name",		stock.getName());
		stockInfo.put("regDate", 	regDate);
		stockInfo.put("modDate", 	regDate);



		int exists = stockMapper.existsStock(symbol);

		//String getDate = date.substring(0, 8);
		//String getDate = day;
		String getDate = Util.getFormatedDate(Util.DEFAULT_YMD_FORMAT);
		int quoteExists = stockMapper.existsQuote(symbol);
		StockQuote quote = stock.getQuote();
		String lastTradeTime = Util.getFormatedDate(quote.getLastTradeTime().getTimeInMillis(), Util.DEFAULT_DATE_FORMAT_MI);
		quote.setLastTradeTimeStr(lastTradeTime);

		if(exists == 0) {
			stockMapper.insertStock(stockInfo);
		}else {
			stockMapper.updateStock(stockInfo);
		}

		if(quoteExists == 0) {
			stockMapper.insertQuote(regDate, getDate, quote);
		}else {
			stockMapper.updateQuote(regDate, getDate, quote);
		}

		StockStats stockStats = stock.getStats();

		System.out.println("quote:\n" + Util.toJSONPrettyString(quote));
		System.out.println("stockstat:\n" + Util.toJSONPrettyString(stockStats));
		//System.out.println("eps:" + stockStats.getEps());

		String earningsAnnouncement = stockStats.getEarningsAnnouncement() == null ? "": Util.getFormatedDate(stockStats.getEarningsAnnouncement().getTimeInMillis(), "yyyyMMdd");
		double roe = stockStats.getROE() == null ? 0 : stockStats.getROE().doubleValue();
		double ebitda = stockStats.getEBITDA() == null ? 0 : stockStats.getEBITDA().doubleValue();
		int stockStatsExist = stockMapper.existsStockStats(symbol);
		if(stockStatsExist == 0) {
			stockMapper.insertStockStats(regDate, getDate, earningsAnnouncement, roe, ebitda, stockStats);
		}else {
			stockMapper.updateStockStats(regDate, getDate, earningsAnnouncement, roe, ebitda, stockStats);
		}

		StockDividend dividend = stock.getDividend();
		if(dividend != null && dividend.getPayDate() != null) {
			System.out.println("dividend:\n" + Util.toJSONPrettyString(dividend));
			upsertDividend(dividend);
		}

		List<HistoricalDividend> historicalDividends = stock.getDividendHistory(fromDate);
		if(!Util.isEmpty(historicalDividends)) upsertHistoricalDividends(historicalDividends);

		List<HistoricalQuote> historicalQuotes = stock.getHistory(fromDate, Interval.DAILY);
		if(!Util.isEmpty(historicalQuotes)) upsertHistoricalQuotes(historicalQuotes);

		return stock;
	}


	private void upsertHistoricalQuotes(List<HistoricalQuote> historicalQuotes) {
		String regDate = Util.getFormatedDate(Util.DEFAULT_DATE_FORMAT_MI);
		for(HistoricalQuote hq : historicalQuotes) {
			String symbol = hq.getSymbol();
			String tradeDate = Util.getFormatedDate(hq.getDate().getTimeInMillis(), Util.DEFAULT_YMD_FORMAT);
			stockMapper.deleteHistoricalQuotes(symbol, tradeDate);
			stockMapper.insertHistoricalQuotes(symbol, tradeDate, hq, regDate);
		}
	}



	private void upsertHistoricalDividends(List<HistoricalDividend> historicalDividends) {
		String regDate = Util.getFormatedDate(Util.DEFAULT_DATE_FORMAT_MI);
		for (HistoricalDividend hd : historicalDividends) {
			String symbol = hd.getSymbol();
			String dividendDate = Util.getFormatedDate(hd.getDate().getTimeInMillis(), Util.DEFAULT_YMD_FORMAT);
			stockMapper.deleteHistoricalDividends(symbol, dividendDate);
			stockMapper.insertHistoricalDividends(symbol, dividendDate, hd, regDate);
		}
	}


	private void upsertDividend(StockDividend dividend) {
		// TODO Auto-generated method stub
		String regDate = Util.getFormatedDate(Util.DEFAULT_DATE_FORMAT_MI);

		String symbol = dividend.getSymbol();


		String payDate = Util.getFormatedDate(dividend.getPayDate().getTimeInMillis(), Util.DEFAULT_YMD_FORMAT);

		String exDate = dividend.getExDate() == null ? "" : Util.getFormatedDate(dividend.getExDate().getTimeInMillis(), Util.DEFAULT_YMD_FORMAT);

		stockMapper.deleteStockDividend(symbol, payDate, exDate, dividend);
		stockMapper.insertStockDividend(symbol, payDate, exDate, dividend, regDate);
	}


	@Transactional
	public Map updateStockPrice() throws IOException {
		Map<String, String> result = new HashMap<>();
		List<String> symbols = stockMapper.getStockSymbols();
		for (String symbol : symbols) {
			Stock stock = YahooFinance.get(symbol, Interval.DAILY);
			if(stock == null) continue;
			List<HistoricalQuote> his = stock.getHistory();
			if(his != null) {
				for (HistoricalQuote historicalQuote : his) {
					Calendar date = historicalQuote.getDate();
					String getDate = Util.getFormatedDate(date.getTimeInMillis(), "yyyyMMdd");
					String modDate = Util.getFormatedDate(Util.DEFAULT_DATE_FORMAT_MI);
					//long volume = historicalQuote.getVolume();
					double price = historicalQuote.getClose() == null ? 0 : historicalQuote.getClose().doubleValue();
					stockMapper.updateStockPrice(symbol, getDate, price, modDate);
				}
			}
		}

		result.put("resultCd", "success");

		return result;
	}



	/**
	 * <pre>
	 *  야후 파이낸스를 통해 주식정보를 가져와 로컬 데이터로 저장한다.
	 * </pre>
	 * @param symbol
	 * @return
	 * @throws IOException
	 */
	@Transactional
	public Stock getStock(String symbol) throws IOException {
		String day = Util.getFormatedDate(Util.DEFAULT_DATE_FORMAT_MI);
		return getStock(symbol, day);
	}

	@Transactional
	public void upload(Portfolio portfolio) {
		String portfolioId = portfolio.getPortfolioId();

		stockMapper.deletePortfolioPositions(portfolioId);
		stockMapper.deletePortfolio(portfolioId);

		stockMapper.insertPortfolio(portfolio);




		List<Position> positions = portfolio.getPositions();
		if(positions != null && positions.size() > 0) {

			for (Position position : positions) {
				stockMapper.insertPortfolioPosition(position);
			}
		}
	}



	public List<String> getStockSymbols() {
		// TODO Auto-generated method stub
		return stockMapper.getStockSymbols();
	}


	public void updateAllStrockInfo(String fromDate)  {
		List<String> symbols = stockMapper.getStockSymbols();
		for (String symbol : symbols) {
			try {
				updateStockInfo(symbol, fromDate);
			} catch (Exception e) {
				logger.error("",e);
			}
		}
	}


	public Performance getPerformance(Portfolio portfolio, Performance Performance) {
		return  stockMapper.getPortfolioPerformance(portfolio, Performance);
	}

	public Performance getPerformance(String symbol, Performance Performance) {
		return  stockMapper.getStockPerformance(symbol, Performance);
	}

	/**
	 * <pre>
	 *     finance.yahoo.com 으로 부터 내려받은 포트폴리오 데이터인 quotes 테이블로 부터 종목 리스트를 읽어 들인다.
	 * </pre>
	 * @return
	 * @since 2021.12
	 */
	public List<String> getMySymbols(){
		return stockMapper.getMySymbols();
	}

	/**
	 * <pre>
	 *     finance.yahoo.com 으로 부터 종목 정보를 읽어들여 TYAH001 ~ TYAH005 테이블 정보를 갱신한다.
	 * </pre>
	 * @param symbol
	 * @param from
	 * @param to
	 * @since 2021.12
	 */
	public void upsertStockInfo(String symbol, Calendar from, Calendar to) throws Exception {
		String regDate = Util.getFormatedDate(Util.DEFAULT_DATE_FORMAT_MI);
		Stock stock = YahooFinance.get(symbol, from, to, Interval.DAILY);

		//---------------------------------------------------------
		// //TYAH001 종목 정보 upsert
		//---------------------------------------------------------
		Map<String, String> stockInfo = new HashMap<String, String>();
		stockInfo.put("symbol",		stock.getSymbol());
		stockInfo.put("currency",	stock.getCurrency());
		stockInfo.put("exchange",	stock.getStockExchange());
		stockInfo.put("name",		stock.getName());
		stockInfo.put("regDate", 	regDate);
		stockInfo.put("modDate", 	regDate);
		int exists = stockMapper.existsStock(symbol); //TYAH001 종목 존재 유무
		if(exists == 0) {
			stockMapper.insertStock(stockInfo);
		}else {
			stockMapper.updateStock(stockInfo);
		}
		//---------------------------------------------------------
		// //TYAH002 기존 정보 upsert
		//---------------------------------------------------------
		String getDate = Util.getFormatedDate(Util.DEFAULT_YMD_FORMAT);
		int quoteExists = stockMapper.existsQuote(symbol); //TYAH0002 기본정보 유무
		StockQuote quote = stock.getQuote();
		String lastTradeTime = Util.getFormatedDate(stock.getQuote().getLastTradeTime().getTimeInMillis(), Util.DEFAULT_DATE_FORMAT_MI);
		quote.setLastTradeTimeStr(lastTradeTime);
		if(quoteExists == 0) {
			stockMapper.insertQuote(regDate, getDate, quote);
		}else {
			stockMapper.updateQuote(regDate, getDate, quote);
		}

		//---------------------------------------------------------
		// //TYAH003 펀더멘탈 정보 upsert
		//---------------------------------------------------------
		StockStats stockStats = stock.getStats();
		String earningsAnnouncement = stockStats.getEarningsAnnouncement() == null ? "": Util.getFormatedDate(stockStats.getEarningsAnnouncement().getTimeInMillis(), "yyyyMMdd");
		double roe = stockStats.getROE() == null ? 0 : stockStats.getROE().doubleValue();
		double ebitda = stockStats.getEBITDA() == null ? 0 : stockStats.getEBITDA().doubleValue();
		int stockStatsExist = stockMapper.existsStockStats(symbol);
		if(stockStatsExist == 0) {
			stockMapper.insertStockStats(regDate, getDate, earningsAnnouncement, roe, ebitda, stockStats);
		}else {
			stockMapper.updateStockStats(regDate, getDate, earningsAnnouncement, roe, ebitda, stockStats);
		}

		//-------------------------------------------------
		// upsert dividend price TYAH004
		//-------------------------------------------------
		StockDividend dividend = stock.getDividend();
		if(dividend != null && dividend.getPayDate() != null) {
			upsertDividend(dividend);
		}

		//-------------------------------------------------
		// upsert historical price TYAH005
		//-------------------------------------------------
		List<HistoricalQuote> history = stock.getHistory();
		history.stream().forEach(historicalQuote -> {
			String tradeDate = Util.getFormatedDate(historicalQuote.getDate().getTimeInMillis(), Util.DEFAULT_YMD_FORMAT);
			stockMapper.deleteHistoricalQuotes(symbol, tradeDate);
			stockMapper.insertHistoricalQuotes(symbol, tradeDate, historicalQuote, regDate);
		});

		//-------------------------------------------------
		// upsert historical dividend TYAH006
		//-------------------------------------------------
		List<HistoricalDividend> historicalDividends = stock.getDividendHistory(from, to);
		if(!Util.isEmpty(historicalDividends)) upsertHistoricalDividends(historicalDividends);

	}
}
