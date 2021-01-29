package whoana.finance.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import whoana.finance.mapper.ReportMapper;
import whoana.finance.util.Util;

@Service
public class ReportService {

	Logger logger = LoggerFactory.getLogger(getClass());
	
	@Autowired
    ReportMapper reportMapper;
	
	
	public List<List<Object>> reportDailyPortfolio(String portfolioId){
		List<LinkedHashMap<String, Object>> list = reportMapper.reportDailyPortfolio(portfolioId);
		
		List<List<Object>> res = new ArrayList<List<Object>>();
		
		if(list != null && list.size() > 0) {
			for (LinkedHashMap<String, Object> linkedHashMap : list) {
				res.add(Arrays.asList(linkedHashMap.values().toArray()));
			}
		}
		return res;
	}
	
}
