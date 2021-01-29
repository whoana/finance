package whoana.finance.controller;

import java.lang.reflect.Method;
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

import whoana.finance.batch.LoadSheetData; 

@Controller
public class SheetDataController {

	Logger logger = LoggerFactory.getLogger(getClass());
	
	@Autowired
	LoadSheetData loadSheetDataService;
	
	@RequestMapping(
			value = "/sheets",
			method = RequestMethod.GET)
	public @ResponseBody Map load(HttpServletRequest request ) throws Exception {
		Map res = new HashMap();
		try {
			logger.info("start load:");
			String table = request.getParameter("table");			
			String spreadsheetId = request.getParameter("spreadsheetId");
			String range = request.getParameter("range");
			logger.debug("spreadsheetId:"+spreadsheetId);
			logger.debug("range:"+range);
			
			Class clazz = loadSheetDataService.getClass();
			Class [] parameterTypes= {String.class, String.class};
			Method method = clazz.getDeclaredMethod(table, parameterTypes);
			Object args[] = {spreadsheetId, range};
			method.invoke(loadSheetDataService, args);
			//loadSheetDataService.tcas001(spreadsheetId, range);
			
			
			logger.info("success load :");
			res.put("resultCd", "success");
		} catch (Exception e) {
			res.put("resultCd", "fail");
			res.put("resultMsg", e.getMessage());
			logger.info("fail load :");
			logger.error("", e);
		} finally {
			logger.info("end load :");
		}
		return res; 
	}
	
}
