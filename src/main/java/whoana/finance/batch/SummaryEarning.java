package whoana.finance.batch;
 

import java.util.Calendar;
import java.util.GregorianCalendar;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;
 
import whoana.finance.service.SummaryService;
import whoana.finance.util.Util;

@Controller
public class SummaryEarning {

	Logger logger = LoggerFactory.getLogger(getClass());
	
	@Autowired
	SummaryService summaryService;

	
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
	public void scheduleDailySummary() {
		//D-1 : 전일 
		try {
			logger.info("start summary daily(addDay:-1):");
			Calendar cal = new GregorianCalendar();
	        cal.add(Calendar.DATE, -1);
	        long yesterday = cal.getTimeInMillis();
			String day = Util.getFormatedDate(yesterday, "yyyyMMdd");
			String regDate = Util.getFormatedDate(Util.DEFAULT_DATE_FORMAT_MI);
			summaryService.summaryDaily(day , regDate);
			
			logger.info("success summary daily(addDay:-1):");
		} catch (Exception e) {
			logger.info("fail summary daily(addDay:-1):");
			logger.error("", e);
		} finally {
			logger.info("end summary daily(addDay:-1):");
		}
		
		scheduleMonthlySummary(-1);
		
		scheduleAnnuallySummary(-1);
		
		//D-0 : 당일 
		try {
			logger.info("start summary daily addDay:");
			Calendar cal = new GregorianCalendar();
	        cal.add(Calendar.DATE, 0);
	        long yesterday = cal.getTimeInMillis();
			String day = Util.getFormatedDate(yesterday, "yyyyMMdd");
			String regDate = Util.getFormatedDate(Util.DEFAULT_DATE_FORMAT_MI);
			summaryService.summaryDaily(day , regDate);
			
			logger.info("success summary daily:");
		} catch (Exception e) {
			logger.info("fail summary daily:");
			logger.error("", e);
		} finally {
			logger.info("end summary daily:");
		}
		
		scheduleMonthlySummary(0);
		
		scheduleAnnuallySummary(0);
		
	}
	
	 
	public void scheduleMonthlySummary(int addDay) {
		try {
			logger.info("start summary monthly(addDay:" + addDay + "):");
			Calendar cal = new GregorianCalendar();
	        cal.add(Calendar.DATE, addDay);
	        long yesterday = cal.getTimeInMillis();
			String month = Util.getFormatedDate(yesterday, "yyyyMM");
			String regDate = Util.getFormatedDate(Util.DEFAULT_DATE_FORMAT_MI);
			summaryService.summaryMonthly(month , regDate);
			
			logger.info("success summary monthly(addDay:" + addDay + "):");
		} catch (Exception e) {
			logger.info("fail summary monthly(addDay:" + addDay + "):");
			logger.error("", e);
		} finally {
			logger.info("end summary monthly(addDay:" + addDay + "):");
		}
	}
	
	public void scheduleAnnuallySummary(int addDay) {
		try {
			logger.info("start summary annually(addDay:" + addDay + "):");
			Calendar cal = new GregorianCalendar();
	        cal.add(Calendar.DATE, addDay);
	        long yesterday = cal.getTimeInMillis();
			String year = Util.getFormatedDate(yesterday, "yyyy");
			String regDate = Util.getFormatedDate(Util.DEFAULT_DATE_FORMAT_MI);
			summaryService.summaryAnnually(year , regDate);
			
			logger.info("success summary annually(addDay:" + addDay + "):");
		} catch (Exception e) {
			logger.info("fail summary annually(addDay:" + addDay + "):");
			logger.error("", e);
		} finally {
			logger.info("end summary annually(addDay:" + addDay + "):");
		}
	}
	
}
