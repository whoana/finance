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



	/**
	 * <pre>
	 *     tday 누적 수익 집계
	 * </pre>
	 * @param tday
	 */
	void doDailySummary(@Param("portfolioId") String portfolioId, @Param("tday") String tday);

	/**
	 * tday 누적 수익 집계 삭제
	 * @param portfolioId
	 * @param tday
	 */
	void deleteDailySummary(@Param("portfolioId") String portfolioId, @Param("tday") String tday);
	List<Map> selectDailySummary(@Param("portfolioId") String portfolioId, @Param("tday") String tday);


}
