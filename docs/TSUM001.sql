/* 포트폴리오-수익률(일) */
DROP TABLE TSUM001 
	CASCADE CONSTRAINTS;

/* 포트폴리오-수익률(일) */
CREATE TABLE TSUM001 (
	PORTFOLIO_ID VARCHAR2(50) NOT NULL, /* 포트폴리오ID */
	SYMBOL VARCHAR2(10) NOT NULL, /* 심볼 */
	DAY VARCHAR2(8) NOT NULL, /* 거래일 */
	QTY INTEGER, /* 매입수량 */
	PRICE DECIMAL(13,5), /* 평균매입단가 */
	AMT NUMBER(13), /* 매입금액 */
	CUR_PRICE DECIMAL(13,5), /* 현재가 */
	CUR_AMT NUMBER(13), /* 평가금액 */
	EARNING NUMBER(13), /* 수익 */
	EARNING_RATE DECIMAL(13,5), /* 수익률 */
	DAILY_EARNING NUMBER(13), /* 일수익 */
	DAILY_EARNING_RATE DECIMAL(13,5), /* 일수익률 */
	REG_DATE VARCHAR2(17) /* 등록일 */
);

COMMENT ON TABLE TSUM001 IS '포트폴리오-수익률(일)';

COMMENT ON COLUMN TSUM001.PORTFOLIO_ID IS '포트폴리오ID';

COMMENT ON COLUMN TSUM001.SYMBOL IS '심볼';

COMMENT ON COLUMN TSUM001.DAY IS '거래일';

COMMENT ON COLUMN TSUM001.QTY IS '매입수량';

COMMENT ON COLUMN TSUM001.PRICE IS '평균매입단가';

COMMENT ON COLUMN TSUM001.AMT IS '매입금액';

COMMENT ON COLUMN TSUM001.CUR_PRICE IS '현재가';

COMMENT ON COLUMN TSUM001.CUR_AMT IS '평가금액';

COMMENT ON COLUMN TSUM001.EARNING IS '수익';

COMMENT ON COLUMN TSUM001.EARNING_RATE IS '수익률';

COMMENT ON COLUMN TSUM001.DAILY_EARNING IS '일수익';

COMMENT ON COLUMN TSUM001.DAILY_EARNING_RATE IS '일수익률';

COMMENT ON COLUMN TSUM001.REG_DATE IS '등록일';

CREATE UNIQUE INDEX PK_TSUM001
	ON TSUM001 (
		PORTFOLIO_ID ASC,
		SYMBOL ASC,
		DAY ASC
	);

SELECT * FROM TSUM001;

INSERT INTO TSUM001 (
	 PORTFOLIO_ID
	,SYMBOL
	,"DAY"
	,QTY
	,PRICE
	,AMT
	,CUR_PRICE
	,CUR_AMT
	,EARNING
	,EARNING_RATE
)
select 
     PORTFOLIO_ID  
    ,SYMBOL  
    ,"DAY"  
    ,sum(QTY) AS qty  
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
	      on a.TRADE_DAY <= '20191231'
	     and a.SYMBOL = b.SYMBOL 
	     and b.GET_DATE = (SELECT max(get_date) FROM TYAH002 WHERE GET_DATE <= '20200102') 
)
GROUP BY 
	 PORTFOLIO_ID  
    ,SYMBOL  
    ,DAY  
;  

COMMIT;


SELECT max(DAY) FROM TSUM001 WHERE DAY <= '20200101' AND AMT > 0;

SELECT 
	a.PORTFOLIO_ID, 
	a.SYMBOL, 
	a.DAY, 
	a.EARNING - b.EARNING AS DAILY_EARNING,
	decode(b.AMT, 0 , 0,(100 * (a.EARNING - b.EARNING) / b.AMT)) AS DAILY_EARNING_RATE
  FROM TSUM001 a 
 INNER JOIN TSUM001 b
    ON a.PORTFOLIO_ID = b.PORTFOLIO_ID
   AND a.SYMBOL = b.SYMBOL
   AND a.DAY = '20191231'
   AND b.DAY = (SELECT max(DAY) FROM TSUM001 WHERE DAY <= '20191231' AND AMT > 0)
   AND a.AMT > 0 
   AND b.AMT > 0
  ;

 

/* 포트폴리오-수익율(월) */
DROP TABLE TSUM002 
	CASCADE CONSTRAINTS;

/* 포트폴리오-수익률(년) */
DROP TABLE TSUM003 
	CASCADE CONSTRAINTS;

/* 포트폴리오-수익율(월) */
CREATE TABLE TSUM002 (
	PORTFOLIO_ID VARCHAR2(50) NOT NULL, /* 포트폴리오ID */
	SYMBOL VARCHAR2(10) NOT NULL, /* 심볼 */
	MONTH VARCHAR2(6) NOT NULL, /* 거래월 */
	QTY INTEGER, /* 매입수량 */
	PRICE DECIMAL(13,5), /* 평균매입단가 */
	AMT NUMBER(13), /* 매입금액 */
	CUR_PRICE DECIMAL(13,5), /* 현재가 */
	CUR_AMT NUMBER(13), /* 평가금액 */
	EARNING NUMBER(13), /* 수익 */
	EARNING_RATE DECIMAL(13,5), /* 수익률 */
	MONTHLY_EARNING NUMBER(13), /* 일수익 */
	MONTHLY_EARNING_RATE DECIMAL(13,5), /* 일수익률 */
	REG_DATE VARCHAR2(17) /* 등록일 */
);

COMMENT ON TABLE TSUM002 IS '포트폴리오-수익율(월)';

COMMENT ON COLUMN TSUM002.PORTFOLIO_ID IS '포트폴리오ID';

COMMENT ON COLUMN TSUM002.SYMBOL IS '심볼';

COMMENT ON COLUMN TSUM002.MONTH IS '거래월';

COMMENT ON COLUMN TSUM002.QTY IS '매입수량';

COMMENT ON COLUMN TSUM002.PRICE IS '평균매입단가';

COMMENT ON COLUMN TSUM002.AMT IS '매입금액';

COMMENT ON COLUMN TSUM002.CUR_PRICE IS '현재가';

COMMENT ON COLUMN TSUM002.CUR_AMT IS '평가금액';

COMMENT ON COLUMN TSUM002.EARNING IS '수익';

COMMENT ON COLUMN TSUM002.EARNING_RATE IS '수익률';

COMMENT ON COLUMN TSUM002.MONTHLY_EARNING IS '일수익';

COMMENT ON COLUMN TSUM002.MONTHLY_EARNING_RATE IS '일수익률';

COMMENT ON COLUMN TSUM002.REG_DATE IS '등록일';

CREATE UNIQUE INDEX PK_TSUM002
	ON TSUM002 (
		PORTFOLIO_ID ASC,
		SYMBOL ASC,
		MONTH ASC
	);

/* 포트폴리오-수익률(년) */
CREATE TABLE TSUM003 (
	PORTFOLIO_ID VARCHAR2(50) NOT NULL, /* 포트폴리오ID */
	SYMBOL VARCHAR2(10) NOT NULL, /* 심볼 */
	YEAR VARCHAR2(4) NOT NULL, /* 거래년 */
	QTY INTEGER, /* 매입수량 */
	PRICE DECIMAL(13,5), /* 평균매입단가 */
	AMT NUMBER(13), /* 매입금액 */
	CUR_PRICE DECIMAL(13,5), /* 현재가 */
	CUR_AMT NUMBER(13), /* 평가금액 */
	EARNING NUMBER(13), /* 수익 */
	EARNING_RATE DECIMAL(13,5), /* 수익률 */
	YEARLY_EARNING NUMBER(13), /* 일수익 */
	YEARLY_EARNING_RATE DECIMAL(13,5), /* 일수익률 */
	REG_DATE VARCHAR2(17) /* 등록일 */
);

COMMENT ON TABLE TSUM003 IS '포트폴리오-수익률(년)';

COMMENT ON COLUMN TSUM003.PORTFOLIO_ID IS '포트폴리오ID';

COMMENT ON COLUMN TSUM003.SYMBOL IS '심볼';

COMMENT ON COLUMN TSUM003.YEAR IS '거래년';

COMMENT ON COLUMN TSUM003.QTY IS '매입수량';

COMMENT ON COLUMN TSUM003.PRICE IS '평균매입단가';

COMMENT ON COLUMN TSUM003.AMT IS '매입금액';

COMMENT ON COLUMN TSUM003.CUR_PRICE IS '현재가';

COMMENT ON COLUMN TSUM003.CUR_AMT IS '평가금액';

COMMENT ON COLUMN TSUM003.EARNING IS '수익';

COMMENT ON COLUMN TSUM003.EARNING_RATE IS '수익률';

COMMENT ON COLUMN TSUM003.YEARLY_EARNING IS '일수익';

COMMENT ON COLUMN TSUM003.YEARLY_EARNING_RATE IS '일수익률';

COMMENT ON COLUMN TSUM003.REG_DATE IS '등록일';

CREATE UNIQUE INDEX PK_TSUM003
	ON TSUM003 (
		PORTFOLIO_ID ASC,
		SYMBOL ASC,
		YEAR ASC
	);
	









 
		select 
		     PORTFOLIO_ID  
		    ,SYMBOL   
		    ,sum(QTY) AS qty  
		    ,avg(BUY_PRICE) AS price
		    ,sum(AMT) AS amt
		    ,avg(CUR_PRICE) AS cur_price
		    ,sum(CUR_AMT)   AS cur_amt
		    ,sum(CUR_AMT - AMT) as EARNING
		    ,round(decode(sum(AMT), 0 , 0, 100 * (sum(CUR_AMT - AMT)) / sum(AMT)),2)  as EARNING_RATE 
		from(
				   	SELECT		   	
			     a.PORTFOLIO_ID  
			    ,a.SYMBOL  
			    ,substr(a.DAY,1, 6) as MONTH  
			    ,sum(a.QTY) AS QTY  
			    ,sum(a.AMT) AS AMT
			    ,avg(a.PRICE) as BUY_PRICE
			    ,max(b.PRICE) as CUR_PRICE
			    ,max(a.CUR_AMT)  as CUR_AMT 
			  from TSUM001 a
			 inner join TYAH002 b
			      on substr(a.DAY,1,6) <= '202001'
			     and a.SYMBOL = b.SYMBOL 
			     and b.GET_DATE = (SELECT max(get_date) FROM TYAH002 WHERE GET_DATE <= '20200102') 
			     GROUP BY 
			 a.PORTFOLIO_ID  
		    ,a.SYMBOL  
		    ,substr(a.DAY,1, 6)
		)
		  
		    ;
		    
		SELECT 
			 a.PORTFOLIO_ID  
			,a.SYMBOL  
			,a.MONTH
			,sum(a.QTY)	  		 AS QTY  
			,sum(a.AMT)   		 AS AMT
			,sum(a.PRICE) 		 AS PRICE
			,sum(a.CUR_PRICE)  	 as CUR_PRICE
			,sum(a.CUR_AMT) 	 as CUR_AMT 
			,sum(a.EARNING) 	 as EARNING
			,sum(a.EARNING_RATE) as EARNING_RATE 
		FROM (    
		   	SELECT		   	
			     a.PORTFOLIO_ID  
			    ,a.SYMBOL  
			    ,substr(a.DAY,1, 6) as MONTH
			    ,0	AS QTY  
			    ,0  AS AMT
			    ,0  AS PRICE
			    ,0  as CUR_PRICE
			    ,0  as CUR_AMT 
			    ,sum(CUR_AMT - AMT) as EARNING
			    ,round(decode(sum(AMT), 0 , 0, 100 * (sum(CUR_AMT - AMT)) / sum(AMT)),2)  as EARNING_RATE 
			  from TSUM001 a
             WHERE a.DAY >= '20200101' 
               AND a.DAY <= '20200131'			     
		  GROUP BY 
			 a.PORTFOLIO_ID  
		    ,a.SYMBOL  
		    ,substr(a.DAY,1, 6)
		  UNION ALL
			SELECT		   	
			     a.PORTFOLIO_ID  
			    ,a.SYMBOL  
			    ,substr(a.DAY,1, 6) as MONTH
			    ,a.QTY  
			    ,a.AMT
			    ,a.PRICE
			    ,a.CUR_PRICE
			    ,a.CUR_AMT 
			    ,0  as EARNING
			    ,0  as EARNING_RATE 
			  from TSUM001 a
             WHERE a.DAY = (SELECT max(DAY) FROM TSUM001 WHERE DAY >= '20200101' AND DAY <= '20200131') 
              	 	  
		 ) a 
		 GROUP BY
		 a.PORTFOLIO_ID  
			,a.SYMBOL  
			,a.MONTH
	;
	



		SELECT  
		 	 a.PORTFOLIO_ID  
			,a.SYMBOL  
			,a.MONTH
			,sum(a.QTY)	  		 AS QTY  
			,sum(a.AMT)   		 AS AMT
			,sum(a.PRICE) 		 AS PRICE
			,sum(a.CUR_PRICE)  	 as CUR_PRICE
			,sum(a.CUR_AMT) 	 as CUR_AMT 
			,sum(a.EARNING) 	 as EARNING
			,sum(a.EARNING_RATE) as EARNING_RATE
		FROM (
			SELECT		   	
			     a.PORTFOLIO_ID  
			    ,a.SYMBOL  
			    ,substr(a.DAY,1, 6) as MONTH
			    ,0	AS QTY  
			    ,0  AS AMT
			    ,0  AS PRICE
			    ,0  as CUR_PRICE
			    ,0  as CUR_AMT 
			    ,sum(CUR_AMT - AMT) as EARNING
			    ,round(decode(sum(AMT), 0 , 0, 100 * (sum(CUR_AMT - AMT)) / sum(AMT)),2)  as EARNING_RATE 
			  from TSUM001 a
             WHERE a.DAY >= '20200101' 
               AND a.DAY <= '20200131'			     
		  GROUP BY 
			 a.PORTFOLIO_ID  
		    ,a.SYMBOL  
		    ,substr(a.DAY,1, 6)
		  UNION ALL
			SELECT		   	
			     a.PORTFOLIO_ID  
			    ,a.SYMBOL  
			    ,substr(a.DAY,1, 6) as MONTH
			    ,a.QTY  
			    ,a.AMT
			    ,a.PRICE
			    ,a.CUR_PRICE
			    ,a.CUR_AMT 
			    ,0  as EARNING
			    ,0  as EARNING_RATE 
			  from TSUM001 a
             WHERE a.DAY = (SELECT max(DAY) FROM TSUM001 WHERE DAY >= '20200101' AND DAY <= '20200131') 
		) a GROUP BY
		 a.PORTFOLIO_ID  
			,a.SYMBOL  
			,a.MONTH
             ;

            
            SELECT * FROM TSUM002 t2;
            SELECT * FROM TSUM003 t3;
            
           
           
           
           
		SELECT  
		 	 a.PORTFOLIO_ID  
			,a.SYMBOL  
			,a.MONTH
			,sum(a.QTY)	  		 AS QTY  
			,sum(a.PRICE) 		 AS PRICE
			,sum(a.AMT)   		 AS AMT
			,sum(a.CUR_PRICE)  	 as CUR_PRICE
			,sum(a.CUR_AMT) 	 as CUR_AMT 
			,sum(a.EARNING) 	 as EARNING
			,sum(a.EARNING_RATE) as EARNING_RATE
		FROM (
			SELECT		   	
			     a.PORTFOLIO_ID  
			    ,a.SYMBOL  
			    ,substr(a.DAY,1, 6) as MONTH
			    ,0	AS QTY  
			    ,0  AS AMT
			    ,0  AS PRICE
			    ,0  as CUR_PRICE
			    ,0  as CUR_AMT 
			    ,sum(CUR_AMT - AMT) as EARNING
			    ,round(decode(sum(AMT), 0 , 0, 100 * (sum(CUR_AMT - AMT)) / sum(AMT)),2)  as EARNING_RATE 
			  from TSUM001 a
             WHERE a.DAY >= '20200101'
               AND a.DAY <= '20200131'			     
		  GROUP BY 
			 a.PORTFOLIO_ID  
		    ,a.SYMBOL  
		    ,substr(a.DAY,1, 6)
		  UNION ALL
			SELECT		   	
			     a.PORTFOLIO_ID  
			    ,a.SYMBOL  
			    ,substr(a.DAY,1, 6) as MONTH
			    ,a.QTY  
			    ,a.AMT
			    ,a.PRICE
			    ,a.CUR_PRICE
			    ,a.CUR_AMT 
			    ,0  as EARNING
			    ,0  as EARNING_RATE 
			  from TSUM001 a
             WHERE a.DAY = (SELECT max(DAY) FROM TSUM001 WHERE DAY >= '20200101' AND DAY <= '20200131') 
		) a 
		GROUP BY
			 a.PORTFOLIO_ID  
			,a.SYMBOL  
			,a.MONTH
			;
		
		
		
SELECT DAY
FROM tsum001 GROUP BY "DAY";		

SELECT MONTH
FROM tsum002 GROUP BY month;

SELECT * FROM tsum002;

SELECT *
FROM tsum003 ;
		



		SELECT 
			a.PORTFOLIO_ID  as "portfolioId", 
			a.SYMBOL		as "symbol", 
			a.MONTH			as "month", 
			a.EARNING - b.EARNING AS "monthlyEarning",
			decode(b.AMT, 0 , 0,(100 * (a.EARNING - b.EARNING) / b.AMT)) AS "monthlyEarningRate"
		  FROM TSUM002 a 
		 INNER JOIN TSUM002 b
		    ON a.PORTFOLIO_ID = b.PORTFOLIO_ID
		   AND a.SYMBOL = b.SYMBOL
		   AND a.MONTH = '202001'
		   AND b.MONTH = (SELECT max(MONTH) FROM TSUM002 WHERE MONTH <  '202001' AND AMT > 0)
		   AND a.AMT > 0 
		   AND b.AMT > 0
		   ;
		   
		
		
		/* 포트폴리오-수익률(년) */
DROP TABLE TSUM003 
	CASCADE CONSTRAINTS;

/* 포트폴리오-수익률(년) */
CREATE TABLE TSUM003 (
	PORTFOLIO_ID VARCHAR2(50) NOT NULL, /* 포트폴리오ID */
	SYMBOL VARCHAR2(10) NOT NULL, /* 심볼 */
	YEAR VARCHAR2(4) NOT NULL, /* 거래년 */
	QTY INTEGER, /* 매입수량 */
	PRICE DECIMAL(13,5), /* 평균매입단가 */
	AMT NUMBER(13), /* 매입금액 */
	CUR_PRICE DECIMAL(13,5), /* 현재가 */
	CUR_AMT NUMBER(13), /* 평가금액 */
	EARNING NUMBER(13), /* 수익 */
	EARNING_RATE DECIMAL(13,5), /* 수익률 */
	ANNUALLY_EARNING NUMBER(13), /* 일수익 */
	ANNUALLY_EARNING_RATE DECIMAL(13,5), /* 일수익률 */
	REG_DATE VARCHAR2(17) /* 등록일 */
);

COMMENT ON TABLE TSUM003 IS '포트폴리오-수익률(년)';

COMMENT ON COLUMN TSUM003.PORTFOLIO_ID IS '포트폴리오ID';

COMMENT ON COLUMN TSUM003.SYMBOL IS '심볼';

COMMENT ON COLUMN TSUM003.YEAR IS '거래년';

COMMENT ON COLUMN TSUM003.QTY IS '매입수량';

COMMENT ON COLUMN TSUM003.PRICE IS '평균매입단가';

COMMENT ON COLUMN TSUM003.AMT IS '매입금액';

COMMENT ON COLUMN TSUM003.CUR_PRICE IS '현재가';

COMMENT ON COLUMN TSUM003.CUR_AMT IS '평가금액';

COMMENT ON COLUMN TSUM003.EARNING IS '수익';

COMMENT ON COLUMN TSUM003.EARNING_RATE IS '수익률';

COMMENT ON COLUMN TSUM003.ANNUALLY_EARNING IS '일수익';

COMMENT ON COLUMN TSUM003.ANNUALLY_EARNING_RATE IS '일수익률';

COMMENT ON COLUMN TSUM003.REG_DATE IS '등록일';

CREATE UNIQUE INDEX PK_TSUM003
	ON TSUM003 (
		PORTFOLIO_ID ASC,
		SYMBOL ASC,
		YEAR ASC
	);
	

SELECT price FROM tyah007 WHERE GET_DAY = (SELECT max(get_day) FROM tyah007) AND SYMBOL = 'USDKRW=X';


SELECT
	'2019 누적수익률' AS YEAR,
	sum(a.AMT * a.currency) AS AMT,
	sum(a.EARNING * a.currency) AS EARNING,
	round(100 * sum(a.EARNING * a.currency) / sum(a.AMT * a.currency),2) || '%' AS EARNING_RATE,
  FROM (
	SELECT 
		a.amt, 
		a.earning,
		decode( b.currency, 'USD', c.currency, 1) AS CURRENCY
	  FROM tsum003 a
	 inner JOIN tyah001 b 
	    on a.YEAR = '2019'
	   AND a.PORTFOLIO_ID = '1'
	   AND b.SYMBOL = a.SYMBOL 
	LEFT OUTER JOIN 
	(SELECT price AS currency FROM tyah007 WHERE GET_DAY = (SELECT max(get_day) FROM tyah007) AND SYMBOL = 'USDKRW=X') c
) a WHERE a.AMT > 0;

   ;
  
---------------------------------------------------------------------------------------------------------------
-- daily earning
---------------------------------------------------------------------------------------------------------------  
SELECT	
	a."DAY",
	sum(a.AMT * a.currency) AS AMT,
	sum(a.EARNING * a.currency) AS EARNING,
	round(100 * sum(a.EARNING * a.currency) / sum(a.AMT * a.currency),2) || '%' AS EARNING_RATE,
	sum(a.DAILY_EARNING * a.currency) AS DAILY_EARNING,
	round(100 * sum(a.DAILY_EARNING * a.currency) / sum(a.AMT * a.currency),2) || '%' AS DAILY_EARNING_RATE
  FROM (  
  SELECT 
  		a."DAY",
		a.amt, 
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
	
	
  
  
  SELECT * FROM top0904;
  
  
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
     
  SELECT * FROM TSTC001;
  SELECT * FROM TSTC002 ;
 
 
  
 
 
 ALTER TABLE tpor001 add(REG_ID varchar2(50), MOD_ID varchar2(50));