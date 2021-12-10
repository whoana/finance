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
             ) a;


