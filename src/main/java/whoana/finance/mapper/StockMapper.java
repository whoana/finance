package whoana.finance.mapper;

import java.util.Calendar;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import whoana.finance.data.Performance;
import whoana.finance.data.Portfolio;
import whoana.finance.data.Position;
import yahoofinance.histquotes.HistoricalQuote;
import yahoofinance.histquotes2.HistoricalDividend;
import yahoofinance.quotes.fx.FxQuote;
import yahoofinance.quotes.stock.StockDividend;
import yahoofinance.quotes.stock.StockQuote;
import yahoofinance.quotes.stock.StockStats;

@Mapper
public interface StockMapper {


	public int existsStock(@Param("symbol") String symbol);


	public int insertStock(Map<String, String> stockInfo);


	public int existsQuote(@Param("symbol") String symbol, @Param("getDate")  String getDate);


	public int insertQuote(@Param("regDate")  String regDate, @Param("getDate") String getDate, @Param("quote") StockQuote quote);


	public int updateStock(Map<String, String> stockInfo);


	public int updateQuote(@Param("modDate")  String regDate, @Param("getDate") String getDate, @Param("quote") StockQuote quote);

	public int insertFx(@Param("regDate")  String regDate, @Param("getDay") String getDay, @Param("fx") FxQuote fx);

	public int updateFx(@Param("modDate")  String modDate, @Param("getDay") String getDay, @Param("fx") FxQuote fx);


	public int existsFx(@Param("symbol") String simbol, @Param("getDay") String getDay );


	public int insertPortfolio(Portfolio portfolio);


	public int insertPortfolioSymbol(@Param("portfolioId") String portfolioId, @Param("symbol")  String symbol);


	public int insertPortfolioPosition(Position position);


	public void deletePortfolio(@Param("portfolioId") String portfolioId);


	public void deletePortfolioSymbols(@Param("portfolioId") String portfolioId);


	public void deletePortfolioPositions(@Param("portfolioId") String portfolioId);


	public List<String> getStockSymbols();


	public int existsStockStats(@Param("symbol") String symbol, @Param("getDate")  String getDate);


	public void insertStockStats(@Param("regDate")  String regDate, @Param("getDate") String getDate,  @Param("earningsAnnouncement") String earningsAnnouncement, @Param("roe") double roe,  @Param("ebitda") double ebitda,  @Param("stockStats") StockStats stockStats);


	public void updateStockStats(@Param("modDate")  String regDate, @Param("getDate") String getDate,  @Param("earningsAnnouncement") String earningsAnnouncement, @Param("roe") double roe,  @Param("ebitda") double ebitda,  @Param("stockStats") StockStats stockStats);


	public void updateStockPrice(@Param("symbol") String symbol, @Param("getDate") String getDate, @Param("price") double price, @Param("modDate")  String modDate);



	public void deleteStockDividend(@Param("symbol") String symbol, @Param("payDate") String payDate ,@Param("exDate") String exDate, @Param("dividend")  StockDividend dividend);


	public void insertStockDividend(@Param("symbol") String symbol, @Param("payDate") String payDate ,@Param("exDate") String exDate, @Param("dividend") StockDividend dividend,  @Param("regDate") String regDate);



	public void deleteHistoricalQuotes(@Param("symbol") String symbol, @Param("tradeDate") String tradeDate);


	public void insertHistoricalQuotes(@Param("symbol") String symbol, @Param("tradeDate") String tradeDate, @Param("historicalQuote") HistoricalQuote historicalQuote, @Param("regDate") String regDate);


	public void deleteHistoricalDividends(@Param("symbol") String symbol, @Param("dividendDate") String dividendDate);


	public void insertHistoricalDividends(@Param("symbol") String symbol, @Param("dividendDate") String dividendDate, @Param("historicalDividend") HistoricalDividend historicalDividend, @Param("regDate") String regDate);

	public Performance getPortfolioPerformance(@Param("portfolio") Portfolio portfolio, @Param("performance") Performance Performance);

	public Performance getStockPerformance(@Param("symbol") String symbol, @Param("performance") Performance Performance);

}
