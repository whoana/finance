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

import whoana.finance.service.EmailService;
import whoana.finance.util.Util;

@Controller
public class EmailController {

	Logger logger = LoggerFactory.getLogger(getClass());
	
	@Autowired
	EmailService emailService;
	
	
	@RequestMapping(
			value = "/email",
			method = RequestMethod.GET)
	public @ResponseBody Map email(HttpServletRequest request ) throws Exception {

		Map res = new HashMap();

		try {
			String to   = request.getParameter("to");
			String subject   = request.getParameter("subject");
			String text   = request.getParameter("text");
		 
			
			logger.info("start email :");
			
			emailService.sendSimpleMessage(to, subject, text);
			
		 
			logger.info("success email :");
			res.put("resultCd", "success");
		} catch (Exception e) {
			res.put("resultCd", "fail");
			res.put("resultMsg", e.getMessage());
			logger.info("fail email :");
			logger.error("", e);
		} finally {
			logger.info("end email :");
		}
		
		 
		return res;
	}
	
	
}
