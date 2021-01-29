package whoana.finance.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import whoana.finance.mapper.FooMapper;

@Service
public class FooService {

	@Autowired
	FooMapper fooMapper;
	
	public List<Map> getFoos() {
		return fooMapper.getFoos();
	}
}
