package whoana.finance.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface SummaryMapper {

	public void deleteSummaryDaily(@Param("day") String day);

	public void summaryDaily(@Param("day") String day, @Param("regDate") String regDate);
	
	public List<Map> selectDailyEarning(@Param("day") String day);
	
	public void updateDailyEarning(Map earning);

	public void deleteSummaryMonthly(@Param("month")String month);

	public void summaryMonthly(@Param("from") String from, @Param("to") String to, @Param("regDate")String regDate);

	public List<Map> selectMonthlyEarning(@Param("month") String month);
	
	public void updateMonthlyEarning(Map earning);
	
	public void deleteSummaryAnnually(@Param("year")String year);

	public void summaryAnnually(@Param("from") String from, @Param("to") String to, @Param("regDate")String regDate);
 
	public List<Map> selectAnnuallyEarning(@Param("year") String year);
	
	public void updateAnnuallyEarning(Map earning);
}
