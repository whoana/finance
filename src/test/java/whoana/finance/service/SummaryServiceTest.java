package whoana.finance.service;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDate;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.util.Calendar;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
public class SummaryServiceTest {
    Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    SummaryService summaryService;

    @Test
    public void testDoDailySummary(){

//        LocalDate from = LocalDate.of(2021, Month.DECEMBER, 16);
//        LocalDate to   = LocalDate.of(2021, Month.DECEMBER, 18);
        LocalDate from = LocalDate.now().minusDays(7);
        LocalDate to   = LocalDate.now().plusDays(1);
        for(LocalDate date = from ; date.isBefore(to) ; date = date.plusDays(1)) {
            String portfolioId = "1";
            String tday = date.format(DateTimeFormatter.BASIC_ISO_DATE);
            logger.debug(tday + " summary start...");
            summaryService.doDailySummery(portfolioId, tday);
            logger.debug(tday + " summary end ...");
//            try {
//                Thread.sleep(2000);
//            } catch (InterruptedException e) {
//            }
        }

    }
}