package whoana.finance.batch;
 

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;


import whoana.finance.service.StockService;

@Controller
public class LoadStockInfo {

	Logger logger = LoggerFactory.getLogger(getClass());
	
	@Autowired
	StockService stockService;

	
	/**
	 * <pre>
	       *           *　　　　　　*　　　　　　*　　　　　　*　　　　　　*
		초(0-59)   분(0-59)　　시간(0-23)　　일(1-31)　　월(1-12)　　요일(0-7) 
		각 별 위치에 따라 주기를 다르게 설정 할 수 있다.
		순서대로 초-분-시간-일-월-요일 순이다. 그리고 괄호 안의 숫자 범위 내로 별 대신 입력 할 수도 있다.
		요일에서 0과 7은 일요일이며, 1부터 월요일이고 6이 토요일이다.
	 * </pre>
	 */
	//@Scheduled(fixedDelay = 10 * 60 * 1000)
	public void schedule() {
		try {
			logger.info("start to get stock information:");
			
			
			List<String> symbols = stockService.getStockSymbols();
			for (String symbol : symbols) {
				stockService.getStock(symbol);
			}
			
			logger.info("success stock information :");
		} catch (Exception e) {
			logger.info("fail to stock information :");
			logger.error("", e);
		} finally {
			logger.info("end to stock information :");
		}
	}
}
