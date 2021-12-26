package whoana.finance.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import whoana.finance.mapper.SummaryMapper;

@Service
public class SummaryService {
	
	@Autowired
	SummaryMapper summaryMapper;
	
	@Transactional
	public void summaryDaily(String day, String regDate) {
		
		summaryMapper.deleteSummaryDaily(day);
		summaryMapper.summaryDaily(day, regDate);
		List<Map> dailyEarnings = summaryMapper.selectDailyEarning(day);
		if(dailyEarnings != null && dailyEarnings.size() > 0)
		for (Map earning : dailyEarnings) {
			summaryMapper.updateDailyEarning(earning);
		}
		
	}

	
	@Transactional
	public void summaryMonthly(String month, String regDate) {
		summaryMapper.deleteSummaryMonthly(month);
		summaryMapper.summaryMonthly(month + "01", month + "31", regDate);
		 
		List<Map> monthlyEarnings = summaryMapper.selectMonthlyEarning(month);
		if(monthlyEarnings != null && monthlyEarnings.size() > 0)
		for (Map earning : monthlyEarnings) {
			summaryMapper.updateMonthlyEarning(earning);
		}
	}
	
	@Transactional
	public void summaryAnnually(String year, String regDate) {
		summaryMapper.deleteSummaryAnnually(year);
		summaryMapper.summaryAnnually(year + "01", year + "12" , regDate);
		List<Map> annuallyEarnings = summaryMapper.selectAnnuallyEarning(year);
		if(annuallyEarnings != null && annuallyEarnings.size() > 0)
		for (Map earning : annuallyEarnings) {
			summaryMapper.updateAnnuallyEarning(earning);
		}
	}


	@Transactional
	public void doDailySummery(String portfolioId, String tday) {
		summaryMapper.deleteDailySummary(portfolioId, tday);
		summaryMapper.doDailySummary(portfolioId, tday);
		//summaryMapper.selectDailySummary(portfolioId, tday);
	}

}
