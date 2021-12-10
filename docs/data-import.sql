create table quotes (
    symbol varchar2(10),
    current_price number(15),
    pdate varchar2(10),
    ptime varchar2(10),
    chg number(15),
    oprice number(15),
    hprice number(15),
    lprice number(15),
    vol number(15),
    tdate varchar2(8),
    pprice number(15),
    quantity number(15) ,
    commission number(15),
    hlimit number(15) ,
    llimit number(15),
    comment varchar2(1000)
) as SELECT * FROM CSVREAD('/Users/whoana/Downloads/quotes.csv');
