package whoana.finance.service;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import whoana.finance.util.Util;

@Service
public class UploadSheetDataService {

	Logger logger = LoggerFactory.getLogger(getClass());
	
	@Autowired
    private JdbcTemplate jdbcTemplate;
	
	//@Transactional
	public void uploadTSTC001(List<List<Object>> data) {
		jdbcTemplate.update("delete from TSTC001"); 
		for(List<Object> row : data) {
			logger.debug("TSTC001 row:" + Util.toJSONPrettyString(row));
			jdbcTemplate.update("INSERT INTO TSTC001 (LEVEL, CD, LEVEL_NM, NM, DEL_YN, REG_DATE, REG_ID ) values (?,?,?,?,?,?,?)", 
				row.get(0),row.get(1),row.get(2),row.get(3),row.get(4),row.get(5),row.get(6)
			);
		}
		 
	}

	
	public void uploadTSTC002(List<List<Object>> data) {
		
		jdbcTemplate.update("delete from TSTC002"); 
		
		for(List<Object> row : data) {
			try {
				logger.debug("TSTC002 row:" + Util.toJSONPrettyString(row));
				jdbcTemplate.update("INSERT INTO TSTC002 (SYMBOL, MARKET_CD, ASSET_CD, PRODUCT_CD, DEL_YN, REG_DATE, REG_ID ) values (?,?,?,?,?,?,?)", 
					row.get(0),row.get(1),row.get(2),row.get(3),row.get(4),row.get(5),row.get(6)
				);
			}catch(Throwable t) {
				t.printStackTrace();
			}
		}
		
	}
	
	public void uploadTPOR001(List<List<Object>> data) {
		for(List<Object> row : data) {	
			String portfolioId = (String)row.get(0);
			int exists = jdbcTemplate.queryForObject("select count(*) from TPOR001 where SYMBOL = ?", Integer.TYPE, portfolioId);
			if(exists > 0) {
				jdbcTemplate.update("UPDATE TPOR001 SET NAME = ?, DEL_YN = ?, MOD_DATE = ?, MOD_ID = ? WHERE PORTFOLIO_ID = ?", 
					row.get(1),row.get(2),row.get(3),row.get(4),row.get(0)
				);
			}else {
				jdbcTemplate.update("INSERT INTO TPOR001 (PORTFOLIO_ID, NAME, DEL_YN, REG_DATE, REG_ID ) values (?,?,?,?,?)", 
					row.get(0),row.get(1),row.get(2),row.get(3),row.get(4)
				);
			}
		}
	}
	
	public void uploadTPOR002(List<List<Object>> data) {
		for(List<Object> row : data) {	
			String portfolioId = (String)row.get(0);
			String symbol = (String)row.get(1);
			String tradeDay = (String)row.get(2);
			
			int exists = jdbcTemplate.queryForObject(
				"select count(*) from TPOR002 where PORTFOLIO_ID = ? and SYMBOL = ? and TRADE_DAY = ?", 
				Integer.TYPE, 
				portfolioId, symbol, tradeDay);
			
			if(exists > 0) {
				jdbcTemplate.update("UPDATE TPOR002 SET PRICE = ?, QTY = ?, AMT = ? where PORTFOLIO_ID = ? and SYMBOL = ? and TRADE_DAY = ?", 
					row.get(3),row.get(4),row.get(5),row.get(0),row.get(1),row.get(2)
				);
			}else {
				jdbcTemplate.update("INSERT INTO TPOR002 (PORTFOLIO_ID, SYMBOL, TRADE_DAY, PRICE, QTY, AMT ) values (?,?,?,?,?,?)", 
					row.get(0),row.get(1),row.get(2),row.get(3),row.get(4),row.get(5)
				);
			}
		}		
	}


	public void uploadTPOR003(List<List<Object>> data) {
		for(List<Object> row : data) {	
			
			//PORTFOLIO_ID	ASSET_CD	MARKET_CD	PRODUCT_CD    TARGET_PCT_A	TARGET_PCT_B	TARGET_PCT_C	TARGET_PCT	REG_DATE	REG_ID	MOD_DATE	MOD_ID
			
			String portfolioId = (String)row.get(0);
			String assetCd     = (String)row.get(1);
			String marketCd    = (String)row.get(2);
			String productCd   = (String)row.get(3);
			
			int exists = jdbcTemplate.queryForObject(
				"select count(*) from TPOR003 where PORTFOLIO_ID = ? and ASSET_CD = ? and MARKET_CD = ? and PRODUCT_CD = ?", 
				Integer.TYPE, 
				portfolioId, assetCd, marketCd, productCd);
			
			if(exists > 0) {
				jdbcTemplate.update("UPDATE TPOR003 SET TARGET_PCT_A = ?, TARGET_PCT_B = ?, TARGET_PCT_C = ?, TARGET_PCT = ?, MOD_DATE = ?, MOD_ID = ? where PORTFOLIO_ID = ? and ASSET_CD = ? and MARKET_CD = ? and PRODUCT_CD = ?", 
					row.get(4),row.get(5),row.get(6),row.get(7),row.get(10),row.get(11),portfolioId, assetCd, marketCd, productCd
				);
			}else {
				jdbcTemplate.update("INSERT INTO TPOR003 (PORTFOLIO_ID, ASSET_CD, MARKET_CD, PRODUCT_CD, TARGET_PCT_A, TARGET_PCT_B, TARGET_PCT_C, TARGET_PCT, REG_DATE, REG_ID) values (?,?,?,?,?,?,?,?,?,?)", 
					portfolioId, assetCd, marketCd, productCd,row.get(4),row.get(5),row.get(6),row.get(7),row.get(8),row.get(9)
				);
			}
		}
	}

	@Transactional
	public void uploadTDIV001(List<List<Object>> data) {
		
		for(List<Object> row : data) {
		
			logger.debug("TDIV001 row:" + Util.toJSONPrettyString(row));
			 
			String symbol 	= (String)row.get(0);
			String day    	= (String)row.get(1);
			int    seq    	= Integer.parseInt((String)row.get(2));
			String exDivDay = (String)row.get(3);
			double amt 		= Double.parseDouble(((String)row.get(4)).replace(",", ""));
			String memo 	= (String)row.get(5);			
			String delYn  	= (String)row.get(6);
			String regDate 	= (String)row.get(7);			
			String regId  	= (String)row.get(8);
			String modDate 	= (String)row.get(9);			
			String modId  	= (String)row.get(10);


			
			
			int exists = jdbcTemplate.queryForObject(
					"select count(*) from TDIV001 where symbol = ? and day = ? and seq = ?", 
					Integer.TYPE, 
					symbol, day, seq); 
				
			if(exists > 0) {
				jdbcTemplate.update(
					"UPDATE TDIV001 SET EX_DIV_DAY = ?, AMT = ?, MEMO = ?, DEL_YN = ?, MOD_DATE = ?, MOD_ID = ? where symbol = ? and day = ? and seq = ?", 
					exDivDay, amt, memo, delYn, modDate, modId, symbol, day, seq
				);
			}else {
				jdbcTemplate.update("INSERT INTO TDIV001 (SYMBOL, DAY, SEQ, EX_DIV_DAY, AMT, MEMO, DEL_YN, REG_DATE, REG_ID ) values (?,?,?,?,?,?,?,?,?)", 
					symbol, day, seq, exDivDay, amt, memo, delYn, regDate, regId
				);
			}
		}
 				
	}
	
	
	@Transactional
	public void uploadTCAS001(List<List<Object>> data) {
		
		for(List<Object> row : data) {
			
			
			logger.debug("TCAS001 row:" + Util.toJSONPrettyString(row));
			if(row == null || row.size() == 0) continue;
			 
			String currency = (String)row.get(0);
			if(currency == null || currency.length() == 0) continue;
			
			String day    	= (String)row.get(1);
			int    seq    	= Integer.parseInt((String)row.get(2));
			String type 	= (String)row.get(3);
			double amt 		= Double.parseDouble(((String)row.get(4)).replace(",", ""));
			double exchange = Double.parseDouble(((String)row.get(6)).replace(",", ""));
			String memo 	= (String)row.get(7);			
			String delYn  	= (String)row.get(8);
			String regDate 	= (String)row.get(9);			
			String regId  	= (String)row.get(10);
			String modDate 	= (String)row.get(11);			
			String modId  	= (String)row.get(12);
			
			
			int exists = jdbcTemplate.queryForObject(
					"select count(*) from TCAS001 where currency = ? and day = ? and seq = ?", 
					Integer.TYPE, 
					currency, day, seq); 
				
			if(exists > 0) {
				jdbcTemplate.update(
					"UPDATE TCAS001 SET TYPE = ?, AMT = ?, EXCHANGE = ?, MEMO = ?, DEL_YN = ?, MOD_DATE = ?, MOD_ID = ? where CURRENCY = ? and day = ? and seq = ?", 
					type, amt, exchange, memo, delYn, modDate, modId, currency, day, seq
				);
			}else {
				jdbcTemplate.update("INSERT INTO TCAS001 (CURRENCY, DAY, SEQ, TYPE, AMT, EXCHANGE, MEMO, DEL_YN, REG_DATE, REG_ID ) values (?,?,?,?,?,?,?,?,?,?)", 
						currency, day, seq, type, amt, exchange, memo, delYn, regDate, regId
				);
			}
		}
 				
	}
	
	
}
