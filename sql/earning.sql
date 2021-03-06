-- 2018 종목별 수익률
SELECT 
	 BL.PORTFOLIO_ID 
	,BL.SYMBOL 
	,BL.TRADE_DAY 
	,BL.AVG_PRICE
	,BL.QTY
	,BL.AMT
	,PC."CLOSE" 
	,BL.QTY  * pc."CLOSE" AS CUR_AMT
	,100 * (BL.QTY  * pc."CLOSE" - BL.AMT)/BL.AMT AS EARNING_RT 
  FROM (
	SELECT 
		 a.PORTFOLIO_ID 
		,a.SYMBOL 
		,a.TRADE_DAY 
		,avg(a.PRICE ) 			AS AVG_PRICE
		,sum(a.QTY)				AS QTY
		,sum(a.QTY * a.PRICE ) 	AS AMT
	  FROM TPOR002 a
	 WHERE a.TRADE_DAY <= '20181231'
	GROUP BY a.PORTFOLIO_ID , a.SYMBOL , a.TRADE_DAY 
) BL
INNER JOIN tyah005 PC 
ON BL.SYMBOL = PC.SYMBOL 
AND PC.TRADE_DATE = '20181231';

-- 2018 총 수익률
SELECT 
	 sum(BL.AMT)
	,sum(BL.QTY  * pc."CLOSE") AS CUR_AMT
	,100 * sum(BL.QTY  * pc."CLOSE" - BL.AMT)/sum(BL.AMT) AS EARNING_RT 
  FROM (
	SELECT 
		 a.PORTFOLIO_ID 
		,a.SYMBOL 
		,a.TRADE_DAY 
		,avg(a.PRICE ) 			AS AVG_PRICE
		,sum(a.QTY)				AS QTY
		,sum(a.QTY * a.PRICE ) 	AS AMT
	  FROM TPOR002 a
	 WHERE a.TRADE_DAY <= '20181231'
	GROUP BY a.PORTFOLIO_ID , a.SYMBOL , a.TRADE_DAY 
) BL
INNER JOIN tyah005 PC 
ON BL.SYMBOL = PC.SYMBOL 
AND PC.TRADE_DATE = '20181231';


SELECT * FROM tyah002 t ;

SELECT * FROM TSTC001 t ;


SELECT * FROM TPOR002;


