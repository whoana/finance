package whoana.finance.mapper;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface ReportMapper {

	List<LinkedHashMap<String, Object>> reportDailyPortfolio(@Param("portfolioId") String portfolioId);

}
