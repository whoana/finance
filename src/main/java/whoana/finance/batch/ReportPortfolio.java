package whoana.finance.batch;
 

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;

import whoana.finance.service.GoogleSheetService;
import whoana.finance.service.ReportService;
import whoana.finance.service.StockService;
import whoana.finance.service.UploadSheetDataService;

@Controller
public class ReportPortfolio {

	Logger logger = LoggerFactory.getLogger(getClass());
	 
	
	@Autowired 
	GoogleSheetService gooleSheetService;
	
	@Autowired
	ReportService reportService;
	
	/**
	 * <pre>
	       *           *　　　　　　*　　　　　　*　　　　　　*　　　　　　*
		초(0-59)   분(0-59)　　시간(0-23)　　일(1-31)　　월(1-12)　　요일(0-7) 
		각 별 위치에 따라 주기를 다르게 설정 할 수 있다.
		순서대로 초-분-시간-일-월-요일 순이다. 그리고 괄호 안의 숫자 범위 내로 별 대신 입력 할 수도 있다.
		요일에서 0과 7은 일요일이며, 1부터 월요일이고 6이 토요일이다.
	 * </pre>
	 */
	//@Scheduled(fixedDelay = 5 * 60 * 1000)
	public void reportDailyPortfolio() {
		try {
			logger.info("start to reportDailyPortfolio: ");
			String spreadsheetId = "1a92otuuxjHw_vCoowXYnKpJjaZTABHD7aEml7mIvuic";

	        String range = "DAILY-REPORT!A1";//컬럼제외영역 설정하기 
	        List<List<Object>> data = reportService.reportDailyPortfolio("1");
			 
	        if(data != null && data.size() > 0) { 
	        	List<Object> columns = new ArrayList<Object>();
	        	
	  
	        	columns.add("자산");
	        	columns.add("시장");
	        	columns.add("상품");
	        	columns.add("보유금액");

	        	columns.add("평가금액");
	        	columns.add("보유비중(%)");
	        	columns.add("목표비중(%)");
	        	
	        	columns.add("누적수익");
	        	columns.add("누적수익률(%)");
	        	columns.add("일일수익");
	        	
	        	columns.add("일일수익률(%)");
	        	
	        	data.add(0, columns);
	        	gooleSheetService.toSheet(spreadsheetId, range, data);
	        }
	        
			logger.info("success to reportDailyPortfolio:");
		} catch (Exception e) {
			logger.info("fail to reportDailyPortfolio:");
			logger.error("", e);
		} finally {
			logger.info("end to reportDailyPortfolio:");
		}
	}
	 
	
}
