package whoana.finance.batch;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import whoana.finance.service.StockService;
import whoana.finance.service.SummaryService;


import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.List;

@Service
public class SummaryBatch {

    Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    StockService stockService;

    @Autowired
    SummaryService summaryService;


    /**
     * <pre>
     *     the method to run daily batch
     *     	       *           *　　　　　　*　　　　　　*　　　　　　*　　　　　　*
     * 		초(0-59)   분(0-59)　　시간(0-23)　　일(1-31)　　월(1-12)　　요일(0-7)
     * 	    	* : 모든 값
     * ? : 특정 값 없음
     * - : 범위 지정에 사용
     * , : 여러 값 지정 구분에 사용
     * / : 초기값과 증가치 설정에 사용
     * L : 지정할 수 있는 범위의 마지막 값
     * W : 월~금요일 또는 가장 가까운 월/금요일
     * # : 몇 번째 무슨 요일 2#1 => 첫 번째 월요일
     * </pre>
     */
    @Scheduled(cron = "0 0 * * * ?")
    public void runDaily(){
        logger.info("start daily batch....");
        try{
            getDollarWonFx();
        }catch (Exception e){
            logger.error("getDollarWonFx error:", e);
        }

        try{
            updateStockInfo();
        }catch (Exception e){
            logger.error("updateStockInfo error", e);
        }

        try{
            dailyEarningSummary();
        }catch (Exception e){
            logger.error("dailyEarningSummary error", e);
        }

        logger.info("end daily batch....");
    }

    public void getDollarWonFx() throws Exception {
        try {
            stockService.getDollarWonFx();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            throw e;
        }
    }


    public void updateStockInfo() {

        Calendar from = Calendar.getInstance();
        from.add(Calendar.DAY_OF_MONTH, -7);
        Calendar to = Calendar.getInstance();

        List<String> symbols = stockService.getMySymbols();
        symbols.stream().forEach(symbol ->{
            try {
                logger.debug("upsert symbol:" + symbol);
                stockService.upsertStockInfo(symbol, from, to);
            } catch (Exception e) {
                logger.error("upsert error symbol:" + symbol, e);
            }
        });
    }


    /**
     * <pre>
     *
     * </pre>
     */
    public void dailyEarningSummary(){
        LocalDate from = LocalDate.now().minusDays(7);
        LocalDate to   = LocalDate.now().plusDays(1);
        for(LocalDate date = from ; date.isBefore(to) ; date = date.plusDays(1)) {
            String portfolioId = "1";
            String tday = date.format(DateTimeFormatter.BASIC_ISO_DATE);
            logger.debug(tday + " summary start...");
            summaryService.doDailySummery(portfolioId, tday);
            logger.debug(tday + " summary end ...");
        }

    }


}
