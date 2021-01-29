package whoana.finance.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface FooMapper {
	
	public List<Map> getFoos();
	
}
