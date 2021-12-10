        select
             y1.symbol
            ,y1.name
            ,y1.currency
            ,d.tdate     as trade_date
            ,d.pprice    as price
            ,d.quantity  as qty
           from quotes d
left outer join tyah001 y1
             on d.symbol = y1.symbol
            and d.TDATE <= '20171231';

with portfolio as (select * from QUOTES a where a.TDATE <= '20181231')
select * from portfolio;

select * from tyah005;

select * from (select * from QUOTES a where a.TDATE <= '20191231') a
left outer join TYAh005 b on a.SYMBOL = b.SYMBOL
and a.TDATE = b.TRADE_DATE;

        select
              a.symbol
             ,a.trade_date
             ,a.qty
             ,a.price
             ,a.cprice
        from (
                 select a.symbol
                      , a.tdate    as trade_date
                      , a.pprice   as price
                      , a.quantity as qty
                      , (
                     select b.CLOSE
                     from TYAH005 b
                     where b.TRADE_DATE =
                           (select min(TRADE_DATE) from TYAH005 where SYMBOL = a.SYMBOL and TRADE_DATE >= a.TDATE)
                       and b.SYMBOL = a.SYMBOL
                     )  as cprice
                 from quotes a
                 where a.tdate <= '20181231'
             ) a
            ;

select (141 * 23 + 136 * 11)/34.0 from dual;
        select
              a.symbol
             ,'20181231'      as tdate
             ,avg(a.pprice)   as price
             ,sum(a.quantity) as qty
             ,(
             select b.CLOSE
             from TYAH005 b
             where b.TRADE_DATE =
                   (select min(TRADE_DATE) from TYAH005 where SYMBOL = a.SYMBOL and TRADE_DATE >= '20181231')
               and b.SYMBOL = a.SYMBOL
             )  as cprice
        from quotes a
        where a.tdate <= '20181231'
     group by a.symbol
        union all
        select
              a.symbol
             ,'20190101'      as tdate
             ,avg(a.pprice)   as price
             ,sum(a.quantity) as qty
             ,(
            select b.CLOSE
            from TYAH005 b
            where b.TRADE_DATE =
                  (select min(TRADE_DATE) from TYAH005 where SYMBOL = a.SYMBOL and TRADE_DATE >= '20190101')
              and b.SYMBOL = a.SYMBOL
            )  as cprice
        from quotes a
        where a.tdate <= '20190101'
        group by a.symbol
        union all
        select
            a.symbol
             ,'20190102'      as tdate
             ,avg(a.pprice)   as price
             ,sum(a.quantity) as qty
             ,(
            select b.CLOSE
            from TYAH005 b
            where b.TRADE_DATE =
                  (select min(TRADE_DATE) from TYAH005 where SYMBOL = a.SYMBOL and TRADE_DATE >= '20190102')
              and b.SYMBOL = a.SYMBOL
            )  as cprice
        from quotes a
        where a.tdate <= '20190102'
        group by a.symbol
;


        select * from TYAH005 where SYMBOL = 'VTI' and TRADE_DATE >= '20181231' ;

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