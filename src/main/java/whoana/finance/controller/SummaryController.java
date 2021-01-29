package whoana.finance.controller;
 
 
 
import java.util.HashMap; 
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
import whoana.finance.service.SummaryService; 

@Controller
public class SummaryController {
	
	Logger logger = LoggerFactory.getLogger(getClass());
 
	@Autowired
	SummaryService summaryService;
	 
	
	@RequestMapping(
			value = "/summary/earning",
			method = RequestMethod.GET)
	public @ResponseBody Map summaryEarning(HttpServletRequest request ) throws Exception {

		Map res = new HashMap();

		
		try {
			String day   = request.getParameter("day");	
			String month = day.substring(0, 6);
			String year  = day.substring(0, 4);
			String regDate = Util.getFormatedDate(Util.DEFAULT_DATE_FORMAT_MI);
			
			logger.info("start summary :" + day);
			
			summaryService.summaryDaily(day , regDate);
			 
			summaryService.summaryMonthly(month , regDate);
			
			summaryService.summaryAnnually(year , regDate);
			
			logger.info("success summary :");
			res.put("resultCd", "success");
		} catch (Exception e) {
			res.put("resultCd", "fail");
			res.put("resultMsg", e.getMessage());
			logger.info("fail summary :");
			logger.error("", e);
		} finally {
			logger.info("end summary :");
		}
		
		 
		return res;
	}
	
	 
	
}
