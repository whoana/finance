package whoana.finance.batch;
 

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;

import whoana.finance.service.GoogleSheetService;
import whoana.finance.service.StockService;
import whoana.finance.service.UploadSheetDataService;

@Controller
public class LoadSheetData {

	Logger logger = LoggerFactory.getLogger(getClass());
	
	@Autowired
	StockService stockService;
	
	@Autowired 
	GoogleSheetService gooleSheetService;
	
	@Autowired
	UploadSheetDataService uploadSheetDataService;
	
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
	public void tstc001() {
		try {
			logger.info("start to load sheet data: TSTC001");
			String spreadsheetId = "1a92otuuxjHw_vCoowXYnKpJjaZTABHD7aEml7mIvuic";

	        String range = "TSTC001!A2:G14";//컬럼제외영역 설정하기 
	        List<List<Object>> data = gooleSheetService.getData(spreadsheetId, range);
			 
	        if(data != null && data.size() > 0) { 
	        	uploadSheetDataService.uploadTSTC001(data);
	        }
	        
			logger.info("success to load sheet data: TSTC001");
		} catch (Exception e) {
			logger.info("fail to load sheet data: TSTC001");
			logger.error("", e);
		} finally {
			logger.info("end to load sheet data: TSTC001");
		}
	}
	
	
	//@Scheduled(fixedDelay = 10 * 60 * 1000)
	public void tstc002() {
		try {
			logger.info("start to load sheet data: TSTC002");
			String spreadsheetId = "1a92otuuxjHw_vCoowXYnKpJjaZTABHD7aEml7mIvuic";

	        String range = "TSTC002!A2:G2000";//컬럼제외영역 설정하기 
	        List<List<Object>> data = gooleSheetService.getData(spreadsheetId, range);
			 
	        if(data != null && data.size() > 0) { 
	        	 
	        	uploadSheetDataService.uploadTSTC002(data);
	        	 
	        }
	        
			logger.info("success to load sheet data: TSTC002");
		} catch (Exception e) {
			logger.info("fail to load sheet data: TSTC002");
			logger.error("", e);
		} finally {
			logger.info("end to load sheet data: TSTC002");
		}
	}
	
	//@Scheduled(fixedDelay = 10 * 60 * 1000)
	public void tpor001() {
		try {
			logger.info("start to load sheet data: TPOR001");
			String spreadsheetId = "1a92otuuxjHw_vCoowXYnKpJjaZTABHD7aEml7mIvuic";

	        String range = "TPOR001!A2:E1000";//컬럼제외영역 설정하기 
	        List<List<Object>> data = gooleSheetService.getData(spreadsheetId, range);
			 
	        if(data != null && data.size() > 0) { 	        	
	        	uploadSheetDataService.uploadTPOR001(data);
	        }
	        
			logger.info("success to load sheet data: TPOR001");
		} catch (Exception e) {
			logger.info("fail to load sheet data: TPOR001");
			logger.error("", e);
		} finally {
			logger.info("end to load sheet data: TPOR001");
		}
	}
	
	
	//@Scheduled(fixedDelay = 60 * 10 * 1000)
	public void tpor002() {
		try {
			logger.info("start to load sheet data: TPOR002");
			String spreadsheetId = "1a92otuuxjHw_vCoowXYnKpJjaZTABHD7aEml7mIvuic";

	        String range = "TPOR002!A2:F1000";//컬럼제외영역 설정하기 
	        List<List<Object>> data = gooleSheetService.getData(spreadsheetId, range);
			 
	        if(data != null && data.size() > 0) { 
	        	uploadSheetDataService.uploadTPOR002(data);
	        }
	        
			logger.info("success to load sheet data: TPOR002");
		} catch (Exception e) {
			logger.info("fail to load sheet data: TPOR002");
			logger.error("", e);
		} finally {
			logger.info("end to load sheet data: TPOR002");
		}
	}
	
	
	//@Scheduled(fixedDelay = 10 * 60 * 1000)
	public void tpor003() {
		try {
			logger.info("start to load sheet data: TPOR003");
			String spreadsheetId = "1a92otuuxjHw_vCoowXYnKpJjaZTABHD7aEml7mIvuic";

	        String range = "TPOR003!A2:L1000";//컬럼제외영역 설정하기 
	        List<List<Object>> data = gooleSheetService.getData(spreadsheetId, range);
			 
	        if(data != null && data.size() > 0) { 
	        	uploadSheetDataService.uploadTPOR003(data);
	        }
	        
			logger.info("success to load sheet data: TPOR003");
		} catch (Exception e) {
			logger.info("fail to load sheet data: TPOR003");
			logger.error("", e);
		} finally {
			logger.info("end to load sheet data: TPOR003");
		}
	}
	
	//@Scheduled(fixedDelay = 60 * 60 * 1000)
	public void tdiv001() {
		String spreadsheetId = "1a92otuuxjHw_vCoowXYnKpJjaZTABHD7aEml7mIvuic";
		String range = "TDIV001!A2:K1000";//컬럼제외영역 설정하기 
		tdiv001( spreadsheetId,  range);
	}

	public void tdiv001(String spreadsheetId, String range) {
		try {
			logger.info("start to load sheet data: TDIV001");  
	        List<List<Object>> data = gooleSheetService.getData(spreadsheetId, range);
			 
	        if(data != null && data.size() > 0) { 
	        	uploadSheetDataService.uploadTDIV001(data);
	        }
	        
			logger.info("success to load sheet data: TDIV001");
		} catch (Exception e) {
			logger.info("fail to load sheet data: TDIV001");
			logger.error("", e);
		} finally {
			logger.info("end to load sheet data: TDIV001");
		}
	}
	

	
	
	//@Scheduled(fixedDelay = 60 * 60 * 1000)
	public void tcas001() {
		String spreadsheetId = "1a92otuuxjHw_vCoowXYnKpJjaZTABHD7aEml7mIvuic";
	    String range = "TCAS001!A2:M9999";//컬럼제외영역 설정하기 
	    tcas001( spreadsheetId,  range);
	}
	
	
	public void tcas001(String spreadsheetId,  String range) {
		try {
			logger.info("start to load sheet data: TCAS001"); 
	        List<List<Object>> data = gooleSheetService.getData(spreadsheetId, range);
			logger.info("TCAS001 data length:" + data.size());
	        if(data != null && data.size() > 0) { 
	        	uploadSheetDataService.uploadTCAS001(data);
	        }
	        
			logger.info("success to load sheet data: TCAS001");
		} catch (Exception e) {
			logger.info("fail to load sheet data: TCAS001");
			logger.error("", e);
		} finally {
			logger.info("end to load sheet data: TCAS001");
		}
	}
	
	
}
