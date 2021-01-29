package whoana.finance.controller;
 
 

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import whoana.finance.util.Util;
import whoana.finance.data.Portfolio;
import whoana.finance.data.Position;
import whoana.finance.service.GoogleSheetService;
import whoana.finance.service.StockService;
import yahoofinance.Stock;
import yahoofinance.quotes.fx.FxQuote; 

@Controller
public class StockController {
	
	Logger logger = LoggerFactory.getLogger(getClass());
	
	@Autowired
	StockService stockService;
	
	@Autowired
	GoogleSheetService googleSheetService;
	
	@RequestMapping(
			value = "/stocks-all",
			method = RequestMethod.GET)
	public @ResponseBody List<Stock> getStocks(HttpServletRequest request ) throws Exception {
		List<Stock> stocks = new ArrayList<Stock>() ;
		
		
		try {
			logger.info("start to get stocks-all information:");
			
			
			String day = request.getParameter("day");
			List<String> symbols = stockService.getStockSymbols();
			for (String symbol : symbols) {
				Stock stock = stockService.getStock(symbol, day);
				stocks.add(stock);
			}
			
			logger.info("success stocks information :");
		} catch (Exception e) {
			logger.info("fail to stocks information :");
			logger.error("", e);
		} finally {
			logger.info("end to stocks information :");
		}
		
		logger.debug("stocks info:" + Util.toJSONPrettyString(stocks));
		return stocks;
	}
	
	@RequestMapping(
			value = "/stocks",
			method = RequestMethod.GET)
	public @ResponseBody Stock getStock(HttpServletRequest request ) throws Exception {
		String symbol = request.getParameter("symbol");	
		String day = request.getParameter("day");
		Stock stock = stockService.getStock(symbol, day);
		logger.debug("stock info:" + Util.toJSONPrettyString(stock));
		return stock;
	}
	
	@RequestMapping(
			value = "/stocks/update-price",
			method = RequestMethod.GET)
	public @ResponseBody Map updateStockPrice(HttpServletRequest request ) throws Exception {
		logger.debug("update-price start");
		Map<String, String> res = stockService.updateStockPrice();
		logger.debug("update-price start");
		return res;
	}
	
	@RequestMapping(
			value = "/fx",
			method = RequestMethod.GET)
	public @ResponseBody FxQuote getFx( HttpServletRequest request) throws Exception {
		String symbol = request.getParameter("symbol");	
		String day = request.getParameter("day");
		if(symbol == null || symbol.trim().length() == 0){
			FxQuote fx = stockService.getFx("USDKRW=X", day);
			logger.debug("fx info:" + Util.toJSONPrettyString(fx));
			return fx;
		}else {
			FxQuote fx = stockService.getFx(symbol + "=X", day);
			logger.debug("fx info:" + Util.toJSONPrettyString(fx));
			return fx;
		}
	}
	
	
	@RequestMapping(
			value = "/portfolios/upload",
			method = RequestMethod.GET)
	public @ResponseBody Map uploadPortFolio(HttpServletRequest request) throws Exception {
		
		Map<String, Object> datas = googleSheetService.getData();
		
		
		
		if(datas != null) {
			
			Portfolio portfolio = new Portfolio();
			String portfoioId = "1";
			String tradeDay   = "20190630";
			portfolio.setPortfolioId(portfoioId);
			portfolio.setName("freedom.a");
			

			List<Position> positions = new ArrayList<Position>();
			portfolio.setPositions(positions);
			List<Map<String, Object>> data = (List<Map<String, Object>>)datas.get("data");
			for (Map<String, Object> record : data) {
				
				logger.debug("portfolio record:" + Util.toJSONPrettyString(record));
				String symbol = (String)record.get("symbol");
				int qty 	  = Integer.parseInt(((String)record.get("qty")).replaceAll(",", ""));
				double price  = Double.parseDouble(((String)record.get("price")).replaceAll(",", ""));
				double amt 	  = Double.parseDouble(((String)record.get("amt")).replaceAll(",", ""));
				

				Position position = new Position();
				position.setPortfolioId(portfoioId);
				position.setSymbol(symbol);
				position.setPrice(price);
				position.setQty(qty);
				position.setAmt(amt);
				position.setTradeDay(tradeDay);
				
				positions.add(position);
				
				
				//요건 이제 배치가 실행하므로 실행할 필요 없음.
				//stockService.getStock(symbol);
				
				
				
			}
			
			stockService.upload(portfolio);
		}
		
		
		return datas;
	}
	
}
