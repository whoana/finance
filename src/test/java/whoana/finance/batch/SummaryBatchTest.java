package whoana.finance.batch;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import whoana.finance.batch.SummaryBatch;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
public class SummaryBatchTest {

    @Autowired
    SummaryBatch summaryBatch;

    @Test
    public void updateStockInfoTest(){
        summaryBatch.updateStockInfo();
    }


}
