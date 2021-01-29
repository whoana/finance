
select
    a.NAME
   ,a.SYMBOL
   ,round(100 * a.AMT_CUR_WON/b.TOT_CUR_WON_AMT, 2) as "POSITION(WON %)"
   ,round(100 * a.AMT_CUR_USD/b.TOT_CUR_USD_AMT, 2) as "POSITION(USD %)"
   ,a.EARNING_RATE
   ,a.QTY
   ,a.AVG_PRICE_WON
   ,a.AVG_PRICE_USD
   ,a.AMT_WON
   ,a.AMT_USD
   ,a.CUR_PRICE
   ,a.AMT_CUR_WON
   ,a.AMT_CUR_USD
   ,b.TOT_CUR_WON_AMT
   ,b.TOT_CUR_USD_AMT
from (
    SELECT
        a.NAME
       ,a.SYMBOL
       ,round(100 * (sum(a.USD_CUR_AMT) - sum(a.USD_AMT)) / sum(a.USD_AMT),0) AS EARNING_RATE
       ,sum(a.QTY) AS QTY
       ,round(max(a.WON_AVG_PRICE),0) AS AVG_PRICE_WON
       ,round(max(a.USD_AVG_PRICE),0) AS AVG_PRICE_USD
       ,round(sum(a.WON_AMT),0) AS AMT_WON
       ,round(sum(a.USD_AMT),0) AS AMT_USD
       ,round(max(a.CPRICE),0)  AS CUR_PRICE
       ,round(sum(a.WON_CUR_AMT),0) AS AMT_CUR_WON
       ,round(sum(a.USD_CUR_AMT),0) AS AMT_CUR_USD
    FROM (
        SELECT
             a.PORTFOLIO_ID
            ,a.PORTFOLIO_NM
            ,a.NAME
            ,a.SYMBOL
            ,a.AMT
            ,a.QTY
            ,a.AVG_PRICE
            ,decode(a.CURRENCY, 'USD', a.AVG_PRICE * 1093.44000 , a.AVG_PRICE) 	AS WON_AVG_PRICE
            ,decode(a.CURRENCY, 'USD', a.AMT * 1093.44000 , a.AMT) 				AS WON_AMT
            ,decode(a.CURRENCY, 'KRW', a.AVG_PRICE / 1093.44000 , a.AVG_PRICE) 	AS USD_AVG_PRICE
            ,decode(a.CURRENCY, 'KRW', a.AMT / 1093.44000 , a.AMT) 				AS USD_AMT
            ,a.CURRENCY
            ,a.STOCK_EXCHANGE
            ,a.CPRICE
            ,decode(a.CURRENCY, 'USD', a.QTY * a.CPRICE * 1093.44000 , a.QTY * a.CPRICE) AS WON_CUR_AMT
            ,decode(a.CURRENCY, 'KRW', a.QTY * a.CPRICE / 1093.44000 , a.QTY * a.CPRICE) AS USD_CUR_AMT
          FROM (
            SELECT
                 a.PORTFOLIO_ID
                ,b.NAME AS PORTFOLIO_NM
                ,c.NAME
                ,a.SYMBOL
                ,a.AMT
                ,a.QTY
                ,(a.AMT/a.QTY) AS AVG_PRICE
                ,c.CURRENCY
                ,c.STOCK_EXCHANGE
                ,(SELECT p."CLOSE" FROM TYAH005 p WHERE p.TRADE_DATE = (SELECT max(TRADE_DATE) FROM TYAH005 WHERE SYMBOL = a.SYMBOL ) AND p.SYMBOL = a.SYMBOL ) AS CPRICE
             FROM (
                SELECT
                     a.PORTFOLIO_ID
                    ,a.SYMBOL
                    ,sum(a.QTY) AS qty
                    ,sum(a.QTY * a.PRICE ) AS amt
                FROM TPOR002 a
               GROUP BY a.PORTFOLIO_ID, a.SYMBOL
               ) a
            INNER JOIN TPOR001 b
                    ON a.PORTFOLIO_ID = b.PORTFOLIO_ID
            INNER JOIN TYAH001 c
                    ON a.SYMBOL = c.SYMBOL
        ) a
    ) a
    GROUP BY a.NAME  ,a.SYMBOL
) a
left outer join
(
    select
         sum(decode(a.CURRENCY, 'USD', a.QTY * a.CPRICE * 1093.44000 , a.QTY * a.CPRICE)) AS TOT_CUR_WON_AMT
        ,sum(decode(a.CURRENCY, 'KRW', a.QTY * a.CPRICE / 1093.44000 , a.QTY * a.CPRICE)) AS TOT_CUR_USD_AMT
      FROM (
        SELECT
             a.PORTFOLIO_ID
            ,b.NAME AS PORTFOLIO_NM
            ,c.NAME
            ,a.SYMBOL
            ,a.AMT
            ,a.QTY
            ,(a.AMT/a.QTY) AS AVG_PRICE
            ,c.CURRENCY
            ,c.STOCK_EXCHANGE
            ,(SELECT p."CLOSE" FROM TYAH005 p WHERE p.TRADE_DATE = (SELECT max(TRADE_DATE) FROM TYAH005 WHERE SYMBOL = a.SYMBOL ) AND p.SYMBOL = a.SYMBOL ) AS CPRICE
         FROM (
            SELECT
                 a.PORTFOLIO_ID
                ,a.SYMBOL
                ,sum(a.QTY) AS qty
                ,sum(a.QTY * a.PRICE ) AS amt
            FROM TPOR002 a
           GROUP BY a.PORTFOLIO_ID, a.SYMBOL
           ) a
        INNER JOIN TPOR001 b
                ON a.PORTFOLIO_ID = b.PORTFOLIO_ID
        INNER JOIN TYAH001 c
                ON a.SYMBOL = c.SYMBOL
    ) a
) b