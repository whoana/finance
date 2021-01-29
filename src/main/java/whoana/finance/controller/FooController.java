package whoana.finance.controller;

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
import whoana.finance.service.FooService; 

@Controller
public class FooController {

	Logger logger = LoggerFactory.getLogger(getClass());
	
	@Autowired
	FooService fooService;
	
	@RequestMapping(
			value = "/foos",
			method = RequestMethod.GET)
	public @ResponseBody List<Map> getFoos(
		HttpServletRequest request
	) throws Exception { 
		
		List<Map> foos = fooService.getFoos();
		logger.debug("foos :" + Util.toJSONPrettyString(foos));
		 
		return foos;
	}
	
}
