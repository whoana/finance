

  SELECT 
		sum(CUR_AMT * decode( b.currency, 'USD', c.currency, 1)) AS AUM
	  FROM tsum003 a
	 inner JOIN tyah001 b 
	    on a.YEAR = '2019'
	   AND a.PORTFOLIO_ID = '1'
	   AND b.SYMBOL = a.SYMBOL 	  
	LEFT OUTER JOIN 
	(SELECT price AS currency FROM tyah007 WHERE GET_DAY = (SELECT max(get_day) FROM tyah007) AND SYMBOL = 'USDKRW=X') c
;



	SELECT 
		sum(a.AMT * a.currency) AS AMT,
		sum(a.EARNING * a.currency) AS EARNING,
		round(100 * sum(a.EARNING * a.currency) / sum(a.AMT * a.currency),2) || '%' AS EARNING_RATE,
		sum(a.DAILY_EARNING * a.currency) AS DAILY_EARNING,
		round(100 * sum(a.DAILY_EARNING * a.currency) / sum(a.AMT * a.currency),2) || '%' AS DAILY_EARNING_RATE,
		sum(a.CUR_AMT * a.currency) AS AUM
	  FROM tsum003 a
	 inner JOIN tyah001 b 
	    on a.YEAR = '2019'
	   AND a.PORTFOLIO_ID = '1'
	   AND b.SYMBOL = a.SYMBOL 	  
	LEFT OUTER JOIN 
	(SELECT price AS currency FROM tyah007 WHERE GET_DAY = (SELECT max(get_day) FROM tyah007) AND SYMBOL = 'USDKRW=X') c
	;

DELETE FROM TSUM001 WHERE "DAY" > '20191231';
	

SELECT * FROM TYAH002 t1 WHERE SYMBOL = 'AAPL';
SELECT * FROM TYAH003 t1 WHERE SYMBOL = 'AAPL';
ALTER TABLE TYAH003 MODIFY (EARNINGS_ANNOUNCEMENT varchar2(8));

---------------------------------------------------------------------------------------------------------------
-- daily earning
---------------------------------------------------------------------------------------------------------------  
SELECT	
	a."DAY",
	sum(a.AMT * a.currency) AS AMT,
	sum(a.EARNING * a.currency) AS EARNING,
	round(100 * sum(a.EARNING * a.currency) / sum(a.AMT * a.currency),2) || '%' AS EARNING_RATE,
	sum(a.DAILY_EARNING * a.currency) AS DAILY_EARNING,
	round(100 * sum(a.DAILY_EARNING * a.currency) / sum(a.AMT * a.currency),2) || '%' AS DAILY_EARNING_RATE,
	sum(a.CUR_AMT * a.currency) AS AUM
  FROM (  
  SELECT 
  		a."DAY",
		a.amt, 
		a.CUR_AMT,
		a.earning,
		a.DAILY_EARNING,
		decode( b.currency, 'USD', c.currency, 1) AS CURRENCY
	  FROM tsum001 a
	 inner JOIN tyah001 b 
	    on a.DAY <= to_char(SYSDATE, 'yyyymmdd')
	   AND a.PORTFOLIO_ID = '1'
	   AND b.SYMBOL = a.SYMBOL 	  
	LEFT OUTER JOIN 
	(SELECT price AS currency FROM tyah007 WHERE GET_DAY = (SELECT max(get_day) FROM tyah007) AND SYMBOL = 'USDKRW=X') c
) a GROUP BY a."DAY"
	;
	
SELECT * FROM TCAS001 t2;

SELECT * FROM TYAH001 t2;


DELETE FROM TCAS001;
COMMIT;
  SELECT 
  	 a.SYMBOL
  	,a.NAME
  	,a.CURRENCY
  	,a.STOCK_EXCHANGE
  	,b.MARKET_CD
  	,b.ASSET_CD
  	,b.PRODUCT_CD
    FROM TYAH001 a
   INNER JOIN TSTC002 b 
      ON a.SYMBOL = b.SYMBOL
     AND b.DEL_YN = 'N';
    LEFT OUTER JOIN TSTC001 MARKET 
      ON MARKET."LEVEL" = '1'
     AND b.MARKET_CD = MARKET.CD
    LEFT OUTER JOIN TSTC001 ASSET 
      ON MARKET."LEVEL" = '2'
     AND b.MARKET_CD = ASSET.CD
    LEFT OUTER JOIN TSTC001 PRODUCT_CD 
      ON MARKET."LEVEL" = '3'
     AND b.MARKET_CD = PRODUCT.CD
;



SELECT	
	a.ASSET_CD,
	a.MARKET_CD,
	a.PRODUCT_CD,
	sum(a.AMT * a.currency) AS AMT,
	sum(a.EARNING * a.currency) AS EARNING,
	round(100 * sum(a.EARNING * a.currency) / sum(a.AMT * a.currency),2) || '%' AS EARNING_RATE,
	sum(a.DAILY_EARNING * a.currency) AS DAILY_EARNING,
	round(100 * sum(a.DAILY_EARNING * a.currency) / sum(a.AMT * a.currency),2) || '%' AS DAILY_EARNING_RATE
  FROM (  
  SELECT   
		a.amt, 
		a.earning,
		a.DAILY_EARNING,
		decode( b.currency, 'USD', c.currency, 1) AS CURRENCY,
		c.ASSET_CD,
		c.MARKET_CD,
		c.PRODUCT_CD
	  FROM tsum001 a
	 inner JOIN tyah001 b 
	    on a.DAY = to_char(SYSDATE - 1, 'yyyymmdd')
	   AND a.PORTFOLIO_ID = '1'
	   AND b.SYMBOL = a.SYMBOL
	   AND a.AMT <> 0
	 INNER JOIN TSTC002 c
	    ON c.SYMBOL = b.SYMBOL
	LEFT OUTER JOIN 
	(SELECT price AS currency FROM tyah007 WHERE GET_DAY = (SELECT max(get_day) FROM tyah007) AND SYMBOL = 'USDKRW=X') c
) a GROUP BY 
	a.ASSET_CD,
	a.MARKET_CD,
	a.PRODUCT_CD
;


SELECT 
--	 TAG.ASSET_CD
	 (SELECT NM||'('||TAG.ASSET_CD||')' FROM TSTC001 WHERE "LEVEL" = '2' AND CD = TAG.ASSET_CD) AS "자산"
--	,TAG.MARKET_CD
	,(SELECT NM||'('||TAG.MARKET_CD||')' FROM TSTC001 WHERE "LEVEL" = '1' AND CD = TAG.MARKET_CD) AS "시장"
--	,TAG.PRODUCT_CD
	,(SELECT NM||'('||TAG.PRODUCT_CD||')' FROM TSTC001 WHERE "LEVEL" = '3' AND CD = TAG.PRODUCT_CD) AS "상품"
	,POS.AMT AS "보유금액"
	,POS.TOTAL_AMT AS "전체금액"
	,POS.PER AS "보유비중(%)"
	,TAG.TARGET_PCT * 100 AS "목표비중(%)"
  FROM TPOR003 TAG LEFT OUTER JOIN (  
	SELECT 
		 s2.ASSET_CD
		,s2.MARKET_CD
		,s2.PRODUCT_CD
		,s2.AMT
		,total.AMT AS TOTAL_AMT
		,round(100 * s2.AMT / total.AMT, 2) AS PER
	  FROM (
		SELECT 
			 s1.ASSET_CD
			,s1.MARKET_CD
			,s1.PRODUCT_CD
			,sum(s1.amt * DECODE(s1.CURRENCY,'USD',(SELECT PRICE FROM TYAH007 WHERE SYMBOL = 'USDKRW=X' AND GET_DAY = (SELECT max(GET_DAY) FROM TYAH007)),1)) AS AMT 
		  FROM ( 
				 SELECT 
					 a.SYMBOL
					,a.QTY
					,a.AMT
					,b.NAME
					,b.CURRENCY
					,c.ASSET_CD
					,c.MARKET_CD
					,c.PRODUCT_CD
		           FROM TPOR002 a
		     INNER JOIN TYAH001 b
		             ON a.SYMBOL = b.SYMBOL
		            AND a.PORTFOLIO_ID = '1'
		     INNER JOIN TSTC002 c 
		             ON c.SYMBOL = b.SYMBOL
			) s1 
		GROUP BY s1.ASSET_CD
				,s1.MARKET_CD
				,s1.PRODUCT_CD
		) s2 
	LEFT OUTER JOIN (
			SELECT 
				sum(a.amt * DECODE(b.CURRENCY,'USD',(SELECT PRICE FROM TYAH007 WHERE SYMBOL = 'USDKRW=X' AND GET_DAY = (SELECT max(GET_DAY) FROM TYAH007)),1)) AS AMT 
		      FROM TPOR002 a
		INNER JOIN TYAH001 b
		        ON a.SYMBOL = b.SYMBOL
		       AND a.PORTFOLIO_ID = '1'
	    ) total
 )  POS 
 ON TAG.ASSET_CD   = POS.ASSET_CD
AND TAG.MARKET_CD  = POS.MARKET_CD
AND TAG.PRODUCT_CD = POS.PRODUCT_CD
; 

---------------------------------------------------------------------------------------------------------------------------------
-- 포트폴리오 목표 대비 포지션/ 누적 수익 및 일일 수익률 
---------------------------------------------------------------------------------------------------------------------------------
SELECT max(DAY), sum(a.amt) , (a.CUR_AMT) FROM TSUM001 a WHERE a.PORTFOLIO_ID = '1' AND a.DAY = (SELECT max(DAY) FROM tsum001) ;

SELECT * FROM TSUM001 ORDER BY DAY desc;

SELECT   
	'합'																	AS "자산",
	'+'																	AS "시장",
	'계'																	AS "상품",
	sum(a.amt * decode( b.currency, 'USD', c.currency, 1)) 				AS "보유금액", 
	sum(a.cur_amt * decode( b.currency, 'USD', c.currency, 1)) 			AS "평가금액",
	null																	AS "보유비중(%)",
	null																	AS "목표비중(%)",
	sum(a.earning * decode( b.currency, 'USD', c.currency, 1)) 			AS "누적수익",
	round(100 * sum(a.earning * decode( b.currency, 'USD', c.currency, 1)) / sum(a.amt * decode( b.currency, 'USD', c.currency, 1)), 2) 		AS "누적수익률(%)",
	sum(a.DAILY_EARNING  * decode( b.currency, 'USD', c.currency, 1)) 	AS "일일수익",	
	round(100 * sum(a.DAILY_EARNING  * decode( b.currency, 'USD', c.currency, 1)) / sum(a.amt * decode( b.currency, 'USD', c.currency, 1)), 2) 	AS "일수익률(%)"
  FROM tsum001 a
 inner JOIN tyah001 b 
    on a.DAY = (SELECT max(DAY) FROM tsum001)
   AND a.PORTFOLIO_ID = '1'
   AND b.SYMBOL = a.SYMBOL
   AND a.AMT <> 0 
LEFT OUTER JOIN 
(SELECT price AS currency FROM tyah007 WHERE GET_DAY = (SELECT max(get_day) FROM tyah007) AND SYMBOL = 'USDKRW=X') c
UNION ALL
SELECT * FROM (
SELECT
 	 (SELECT NM||'('||a.ASSET_CD||')'   FROM TSTC001 WHERE "LEVEL" = '2' AND CD = a.ASSET_CD) 	AS "자산"
	,(SELECT NM||'('||a.MARKET_CD||')'  FROM TSTC001 WHERE "LEVEL" = '1' AND CD = a.MARKET_CD) 	AS "시장"
	,(SELECT NM||'('||a.PRODUCT_CD||')' FROM TSTC001 WHERE "LEVEL" = '3' AND CD = a.PRODUCT_CD) AS "상품"
	,a.AMT 																						AS "보유금액"	
	,b.CUR_AMT  																				AS "평가금액"	
	,a.PER 																						AS "보유비중(%)"
	,a.TARGET_PCT * 100 																		AS "목표비중(%)"
	,b.EARNING          																		AS "누적수익"
	,b.EARNING_RATE     																		AS "누적수익률(%)"
	,b.DAILY_EARNING    																		AS "일일수익"
	,b.DAILY_EARNING_RATE 																		AS "일일수익률(%)"
  FROM ( 
	SELECT 
		 TAG.ASSET_CD 
		,TAG.MARKET_CD 
		,TAG.PRODUCT_CD 
		,POS.AMT 
		,POS.TOTAL_AMT 
		,POS.PER 
		,TAG.TARGET_PCT 
	  FROM TPOR003 TAG LEFT OUTER JOIN (  
		SELECT 
			 s2.ASSET_CD
			,s2.MARKET_CD
			,s2.PRODUCT_CD
			,s2.AMT
			,total.AMT AS TOTAL_AMT
			,round(100 * s2.AMT / total.AMT, 2) AS PER
		  FROM (
			SELECT 
				 s1.ASSET_CD
				,s1.MARKET_CD
				,s1.PRODUCT_CD
				,sum(s1.amt * DECODE(s1.CURRENCY,'USD',(SELECT PRICE FROM TYAH007 WHERE SYMBOL = 'USDKRW=X' AND GET_DAY = (SELECT max(GET_DAY) FROM TYAH007)),1)) AS AMT 
			  FROM ( 
					 SELECT 
						 a.SYMBOL
						,a.QTY
						,a.AMT
						,b.NAME
						,b.CURRENCY
						,c.ASSET_CD
						,c.MARKET_CD
						,c.PRODUCT_CD
			           FROM TPOR002 a
			     INNER JOIN TYAH001 b
			             ON a.SYMBOL = b.SYMBOL
			            AND a.PORTFOLIO_ID = '1'
			     INNER JOIN TSTC002 c 
			             ON c.SYMBOL = b.SYMBOL
				) s1 
			GROUP BY s1.ASSET_CD
					,s1.MARKET_CD
					,s1.PRODUCT_CD
			) s2 
		LEFT OUTER JOIN (
				SELECT 
					sum(a.amt * DECODE(b.CURRENCY,'USD',(SELECT PRICE FROM TYAH007 WHERE SYMBOL = 'USDKRW=X' AND GET_DAY = (SELECT max(GET_DAY) FROM TYAH007)),1)) AS AMT 
			      FROM TPOR002 a
			INNER JOIN TYAH001 b
			        ON a.SYMBOL = b.SYMBOL
			       AND a.PORTFOLIO_ID = '1'
		    ) total
	 )  POS 
	 ON TAG.ASSET_CD   = POS.ASSET_CD
	AND TAG.MARKET_CD  = POS.MARKET_CD
	AND TAG.PRODUCT_CD = POS.PRODUCT_CD
) a 
LEFT OUTER JOIN (
	SELECT	
		a.ASSET_CD,
		a.MARKET_CD,
		a.PRODUCT_CD,
		sum(a.AMT * a.currency) AS AMT,
		sum(a.CUR_AMT * a.currency) AS CUR_AMT,
		sum(a.EARNING * a.currency) AS EARNING,
		round(100 * sum(a.EARNING * a.currency) / sum(a.AMT * a.currency),2) AS EARNING_RATE,
		sum(a.DAILY_EARNING * a.currency) AS DAILY_EARNING,
		round(100 * sum(a.DAILY_EARNING * a.currency) / sum(a.AMT * a.currency),2) AS DAILY_EARNING_RATE
	  FROM (  
	  SELECT   
			a.amt, 
			a.cur_amt,
			a.earning,
			a.DAILY_EARNING,
			decode( b.currency, 'USD', c.currency, 1) AS CURRENCY,
			c.ASSET_CD,
			c.MARKET_CD,
			c.PRODUCT_CD
		  FROM tsum001 a
		 inner JOIN tyah001 b 
		    on a.DAY = (SELECT max(DAY) FROM tsum001)
		   AND a.PORTFOLIO_ID = '1'
		   AND b.SYMBOL = a.SYMBOL
		   AND a.AMT <> 0
		 INNER JOIN TSTC002 c
		    ON c.SYMBOL = b.SYMBOL
		LEFT OUTER JOIN 
		(SELECT price AS currency FROM tyah007 WHERE GET_DAY = (SELECT max(get_day) FROM tyah007) AND SYMBOL = 'USDKRW=X') c
	) a GROUP BY 
		a.ASSET_CD,
		a.MARKET_CD,
		a.PRODUCT_CD
) b
 ON a.ASSET_CD   = b.ASSET_CD
AND a.MARKET_CD  = b.MARKET_CD
AND a.PRODUCT_CD = b.PRODUCT_CD
ORDER BY b.EARNING desc
) a
;



SELECT   
			'S'																	AS c1,
			'U'																	AS c2,
			'M'																	AS c3,
			sum(a.amt * decode( b.currency, 'USD', c.currency, 1)) 				AS c4, 
			sum(a.cur_amt * decode( b.currency, 'USD', c.currency, 1)) 			AS c5,
			0   																AS c6,
			0   																AS c7,
			sum(a.earning * decode( b.currency, 'USD', c.currency, 1)) 			AS c8,
			round(100 * sum(a.earning * decode( b.currency, 'USD', c.currency, 1)) / sum(a.amt * decode( b.currency, 'USD', c.currency, 1)), 2) 		AS c9,
			sum(a.DAILY_EARNING  * decode( b.currency, 'USD', c.currency, 1)) 	AS c10,	
			round(100 * sum(a.DAILY_EARNING  * decode( b.currency, 'USD', c.currency, 1)) / sum(a.amt * decode( b.currency, 'USD', c.currency, 1)), 2) 	AS c11
		  FROM tsum001 a
		 inner JOIN tyah001 b 
		    on a.DAY = (SELECT max(DAY) FROM tsum001)
		   AND a.PORTFOLIO_ID = '1'
		   AND b.SYMBOL = a.SYMBOL
		   AND a.AMT <> 0 
		LEFT OUTER JOIN 
		(SELECT price AS currency FROM tyah007 WHERE GET_DAY = (SELECT max(get_day) FROM tyah007) AND SYMBOL = 'USDKRW=X') c
		UNION ALL
		SELECT * FROM (
		SELECT
		 	 (SELECT NM||'('||a.ASSET_CD||')'   FROM TSTC001 WHERE "LEVEL" = '2' AND CD = a.ASSET_CD) 	AS c1
			,(SELECT NM||'('||a.MARKET_CD||')'  FROM TSTC001 WHERE "LEVEL" = '1' AND CD = a.MARKET_CD) 	AS c2
			,(SELECT NM||'('||a.PRODUCT_CD||')' FROM TSTC001 WHERE "LEVEL" = '3' AND CD = a.PRODUCT_CD) AS c3
			,a.AMT 																						AS c4	
			,b.CUR_AMT  																				AS c5	
			,a.PER 																						AS c6
			,a.TARGET_PCT * 100 																		AS c7
			,b.EARNING          																		AS c8
			,b.EARNING_RATE     																		AS c9
			,b.DAILY_EARNING    																		AS c10
			,b.DAILY_EARNING_RATE 																		AS c11
		  FROM ( 
			SELECT 
				 TAG.ASSET_CD 
				,TAG.MARKET_CD 
				,TAG.PRODUCT_CD 
				,POS.AMT 
				,POS.TOTAL_AMT 
				,POS.PER 
				,TAG.TARGET_PCT 
			  FROM TPOR003 TAG LEFT OUTER JOIN (  
				SELECT 
					 s2.ASSET_CD
					,s2.MARKET_CD
					,s2.PRODUCT_CD
					,s2.AMT
					,total.AMT AS TOTAL_AMT
					,round(100 * s2.AMT / total.AMT, 2) AS PER
				  FROM (
					SELECT 
						 s1.ASSET_CD
						,s1.MARKET_CD
						,s1.PRODUCT_CD
						,sum(s1.amt * DECODE(s1.CURRENCY,'USD',(SELECT PRICE FROM TYAH007 WHERE SYMBOL = 'USDKRW=X' AND GET_DAY = (SELECT max(GET_DAY) FROM TYAH007)),1)) AS AMT 
					  FROM ( 
							 SELECT 
								 a.SYMBOL
								,a.QTY
								,a.AMT
								,b.NAME
								,b.CURRENCY
								,c.ASSET_CD
								,c.MARKET_CD
								,c.PRODUCT_CD
					           FROM TPOR002 a
					     INNER JOIN TYAH001 b
					             ON a.SYMBOL = b.SYMBOL
					            AND a.PORTFOLIO_ID = '1'
					     INNER JOIN TSTC002 c 
					             ON c.SYMBOL = b.SYMBOL
						) s1 
					GROUP BY s1.ASSET_CD
							,s1.MARKET_CD
							,s1.PRODUCT_CD
					) s2 
				LEFT OUTER JOIN (
						SELECT 
							sum(a.amt * DECODE(b.CURRENCY,'USD',(SELECT PRICE FROM TYAH007 WHERE SYMBOL = 'USDKRW=X' AND GET_DAY = (SELECT max(GET_DAY) FROM TYAH007)),1)) AS AMT 
					      FROM TPOR002 a
					INNER JOIN TYAH001 b
					        ON a.SYMBOL = b.SYMBOL
					       AND a.PORTFOLIO_ID = '1'
				    ) total
			 )  POS 
			 ON TAG.ASSET_CD   = POS.ASSET_CD
			AND TAG.MARKET_CD  = POS.MARKET_CD
			AND TAG.PRODUCT_CD = POS.PRODUCT_CD
		) a 
		LEFT OUTER JOIN (
			SELECT	
				a.ASSET_CD,
				a.MARKET_CD,
				a.PRODUCT_CD,
				sum(a.AMT * a.currency) AS AMT,
				sum(a.CUR_AMT * a.currency) AS CUR_AMT,
				sum(a.EARNING * a.currency) AS EARNING,
				round(100 * sum(a.EARNING * a.currency) / sum(a.AMT * a.currency),2) AS EARNING_RATE,
				sum(a.DAILY_EARNING * a.currency) AS DAILY_EARNING,
				round(100 * sum(a.DAILY_EARNING * a.currency) / sum(a.AMT * a.currency),2) AS DAILY_EARNING_RATE
			  FROM (  
			  SELECT   
					a.amt, 
					a.cur_amt,
					a.earning,
					a.DAILY_EARNING,
					decode( b.currency, 'USD', c.currency, 1) AS CURRENCY,
					c.ASSET_CD,
					c.MARKET_CD,
					c.PRODUCT_CD
				  FROM tsum001 a
				 inner JOIN tyah001 b 
				    on a.DAY = (SELECT max(DAY) FROM tsum001)
				   AND a.PORTFOLIO_ID = '1'
				   AND b.SYMBOL = a.SYMBOL
				   AND a.AMT <> 0
				 INNER JOIN TSTC002 c
				    ON c.SYMBOL = b.SYMBOL
				LEFT OUTER JOIN 
				(SELECT price AS currency FROM tyah007 WHERE GET_DAY = (SELECT max(get_day) FROM tyah007) AND SYMBOL = 'USDKRW=X') c
			) a GROUP BY 
				a.ASSET_CD,
				a.MARKET_CD,
				a.PRODUCT_CD
		) b
		 ON a.ASSET_CD   = b.ASSET_CD
		AND a.MARKET_CD  = b.MARKET_CD
		AND a.PRODUCT_CD = b.PRODUCT_CD
		ORDER BY b.EARNING desc
		) a
		;
	
	SELECT * FROM tyah007;

-- 배당금 
SELECT	
	round(sum(a.amt * DECODE(b.CURRENCY,'USD',(SELECT PRICE FROM TYAH007 WHERE SYMBOL = 'USDKRW=X' AND GET_DAY = (SELECT max(GET_DAY) FROM TYAH007)),1)),2) AS AMT  
  FROM TDIV001 a 
INNER JOIN TYAH001 b ON a.SYMBOL = b.SYMBOL;

-- 월별 배당  
SELECT	
	substr(DAY,1,6),
	round(sum(a.amt * DECODE(b.CURRENCY,'USD',(SELECT PRICE FROM TYAH007 WHERE SYMBOL = 'USDKRW=X' AND GET_DAY = (SELECT max(GET_DAY) FROM TYAH007)),1)),2) AS AMT  
  FROM TDIV001 a 
INNER JOIN TYAH001 b ON a.SYMBOL = b.SYMBOL
;
GROUP BY substr(DAY,1,6);





SELECT * FROM TSUM001 ;





SELECT * 
  FROM TYAH001 t1, TYAH002 t2 
 WHERE t1.SYMBOL = t2.SYMBOL 
   AND t1.SYMBOL = 'SMH';
   
  
  SELECT * 
    FROM TIND001;
  
  
  INSERT INTO TIND001 values('1', 68909028.10, '20191231', 100,'N',to_char(SYSDATE, 'yyyymmddHH24missSSS'),'whoana', NULL, NULL);
  COMMIT;
/* 지수 */
CREATE TABLE TIND001 (
	PORTFOLIO_ID VARCHAR2(50) NOT NULL, /* 포트폴리오ID */
	BASIC_ASSET DECIMAL(13,5) NOT NULL, /* 기초자산 */
	BASIC_DAY VARCHAR2(8) NOT NULL, /* 기준일자 */
	BASIC_POINT DECIMAL(13,5) NOT NULL, /* 기준지수 */
	DEL_YN CHAR(1) DEFAULT 'N' NOT NULL, /* 삭제구분 */
	REG_DATE VARCHAR2(17) NOT NULL, /* 등록일 */
	REG_ID VARCHAR2(50) NOT NULL, /* 등록자 */
	MOD_DATE VARCHAR2(17), /* 수정일 */
	MOD_ID VARCHAR2(50) /* 수정자 */
);

COMMENT ON TABLE TIND001 IS '지수';

COMMENT ON COLUMN TIND001.PORTFOLIO_ID IS '포트폴리오ID';

COMMENT ON COLUMN TIND001.BASIC_ASSET IS '기초자산';

COMMENT ON COLUMN TIND001.BASIC_DAY IS '기준일자';

COMMENT ON COLUMN TIND001.BASIC_POINT IS '기준지수';

COMMENT ON COLUMN TIND001.DEL_YN IS '삭제구분';

COMMENT ON COLUMN TIND001.REG_DATE IS '등록일';

COMMENT ON COLUMN TIND001.REG_ID IS '등록자';

COMMENT ON COLUMN TIND001.MOD_DATE IS '수정일';

COMMENT ON COLUMN TIND001.MOD_ID IS '수정자';

CREATE UNIQUE INDEX PK_TIND001
	ON TIND001 (
		PORTFOLIO_ID ASC
	);

/* 지수-히스토리 */
CREATE TABLE TIND002 (
	MOD_DATE VARCHAR2(17) NOT NULL, /* 수정일 */
	PORTFOLIO_ID VARCHAR2(50) NOT NULL, /* 포트폴리오ID */
	BASIC_ASSET DECIMAL(13,5) NOT NULL, /* 기초자산 */
	BASIC_DAY VARCHAR2(8) NOT NULL, /* 기준일자 */
	BASIC_POINT DECIMAL(13,5) NOT NULL, /* 기준지수 */
	MOD_ID VARCHAR2(50) /* 수정자 */
);

COMMENT ON TABLE TIND002 IS '지수-히스토리';

COMMENT ON COLUMN TIND002.MOD_DATE IS '수정일';

COMMENT ON COLUMN TIND002.PORTFOLIO_ID IS '포트폴리오ID';

COMMENT ON COLUMN TIND002.BASIC_ASSET IS '기초자산';

COMMENT ON COLUMN TIND002.BASIC_DAY IS '기준일자';

COMMENT ON COLUMN TIND002.BASIC_POINT IS '기준지수';

COMMENT ON COLUMN TIND002.MOD_ID IS '수정자';

CREATE UNIQUE INDEX PK_TIND002
	ON TIND002 (
		MOD_DATE ASC,
		PORTFOLIO_ID ASC
	);




select 
		   --  PORTFOLIO_ID  
		   -- ,SYMBOL  
		    --,#{day} as DAY 
		    sum(QTY) AS qty  
		    ,avg(BUY_PRICE) AS price
		    ,sum(AMT) AS amt
		    ,avg(CUR_PRICE) AS cur_price
		    ,sum(CUR_AMT)   AS cur_amt
		    ,sum(CUR_AMT - AMT) as EARNING
		    ,round(decode(sum(AMT), 0 , 0, 100 * (sum(CUR_AMT - AMT)) / sum(AMT)),2)  as EARNING_RATE 
		from(
			select 
			     a.PORTFOLIO_ID  
			    ,a.SYMBOL  
			    ,a.TRADE_DAY as DAY  
			    ,a.QTY  
			    ,a.AMT
			    ,a.PRICE as BUY_PRICE
			    ,b.PRICE as CUR_PRICE
			    ,(a.QTY * b.PRICE)  as CUR_AMT 
			  from TPOR002 a
			 inner join TYAH002 b
			      on a.TRADE_DAY <= '20200115'
			     and a.SYMBOL = b.SYMBOL 
			     and b.GET_DATE = (SELECT max(get_date) FROM TYAH002 WHERE GET_DATE <= '20200115')  
		)
		--GROUP BY 
		---	 PORTFOLIO_ID  
		--    ,SYMBOL  
		--    ,DAY 
		    ;
		   
		   SELECT * FROM TYAH007;
		  
		  
		  SELECT GET_DATE, SYMBOL, MARKET_CAP, SHARES, SHARES, EPS, EPS_EST_CURR_YEAR, PRICE_BOOK, BOOK_VALUE_PER_PRICE, EARNINGS_ANNOUNCEMENT FROM TYAH003 WHERE SYMBOL = 'V';
		 SELECT * FROM TYAH002 WHERE SYMBOL = 'V';
		
		
		SELECT * FROM TDIV001 t2;
	    delete FROM TCAS001 WHERE REG_DATE >= '20200114135900000';
	   COMMIT;
	  
	  
	  SELECT  *FROM TCAS001;
	 
	 
	 
	 
	 
	 
	 
	 SELECT * FROM TYAH002 WHERE 
		  