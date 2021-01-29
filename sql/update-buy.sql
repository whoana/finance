SELECT * from  Tyah001;
--- 신규 종목 등록 
INSERT INTO  Tyah001
SELECT DISTINCT 
	b AS symbol, 
	a AS name ,
	'KRW' AS currency, 
	'KSE'AS stock_exchange, 
	to_char(SYSDATE , 'yyyymmddhhmiss') AS reg_date, 
	'' AS mod_date
 FROM investing_tmp WHERE b NOT IN (SELECT symbol FROM Tyah001);
 SELECT * FROM TSTC002 t2 ;
COMMIT;
-- sector 
SELECT DISTINCT 
	b AS symbol, 
	a AS market_cd ,
	'KRW' AS asset_cd, 
	'KSE'AS stock_exchange, 
	to_char(SYSDATE , 'yyyymmddhhmiss') AS reg_date, 
	'' AS mod_date
 FROM investing_tmp
 ;


/*
1	1	시장분류	한국
1	2	시장분류	미국
1	3	시장분류	이머징
2	1	자산분류	주식
2	2	자산분류	채권
2	3	자산분류	원자재
2	4	자산분류	현금
3	1	상품분류	주식
3	2	상품분류	ETF
3	3	상품분류	단기채권
3	4	상품분류	장기채권
3	5	상품분류	금
3	6	상품분류	기타원자재

4	1	섹터	Financials
4	2	섹터	Uitlities
4	3	섹터	Consumer Discretionary 순환 소비제 (자동차 아마존 ...)
4	4	섹터	Consumer Staples 필수 소비제 
4	5	섹터	Energy
4	6	섹터	Healthcare
4	7	섹터	Industrial
4	8	섹터	Technology
4	9	섹터	Telecom
4	10	섹터	Materials
4	11	섹터	Real Estate
4   12  섹터 Consumer Service
4	13  섹터 Total US Market ETF
4	14  섹터 Total World Market ETF
4 	15  섹터 Totoal ULow ETF 
4   16  섹터 US Dividend ETF
4   17  섹터 Long Government Income
4   18	섹터 US Value
4   19  섹터 Innovation1919
4   20  섹터 Game
4   21  섹터 ESG
4   22  섹터 Communication Services
4   22  섹터 Auto Mobility
4   23  섹터 Emerging
4   24  섹터 Semiconductor
 */
SELECT * FROM TSTC001;
ALTER TABLE TSTC002 ADD (SECTOR VARCHAR(5));
ALTER TABLE TSTC002 DROP REG__DATE;
SELECT * FROM Tyah001;
select * FROM TSTC002;


SELECT 
	 a.symbol 
	,a.NAME 
	,'' AS market_cd
	,'' AS asset_cd
	,'' AS product_cd
	,'N' AS DEL_YN
	,to_char(SYSDATE,'yyyymmddhh24miss') AS reg_date
	,'whoana' AS reg_id 
	,to_char(SYSDATE,'yyyymmddhh24miss') AS mod_date
	,'whoana' AS mod_id 
  FROM TYAH001 a;



 SELECT *  FROM investing_tmp;
 --- 매입 데이터 재 업로드 
 DELETE FROM tpor002;
 
 INSERT INTO tpor002
 SELECT 
     '1' AS portfolio_id
 	,d.symbol
 	,d.trade_day
	, ROWNUM() AS seq
 	,d.price
 	,d.qty
 	,d.qty * d.price AS amt
   FROM (
	 SELECT 
	 	  b AS symbol
		, REPLACE(c,'/','') AS trade_day
		, CONVERT(replace(g,',',''),DECIMAL) AS price
		, CONVERT(replace(e,',',''), INT) AS qty
	  FROM INVESTING_TMP
	 ORDER BY TRADE_DAY , SYMBOL 
  ) d
;
COMMIT;

SELECT  * FROM tpor002;

--sector 작업 
