package assignment2;

import assignment2.model.CSVFileParser;
import assignment2.model.CancellationReason;
import assignment2.model.CancelledTransaction;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class CSVFileParserTest {

    private CSVFileParser csvFileParser;

    @Test
    public void updateAndGetCancelledTransaction(){

        csvFileParser = new CSVFileParser("", "", "", "src/test/resources/cancelledTransactions.csv");

        CancelledTransaction cancelledTransaction1 = new CancelledTransaction("test", CancellationReason.TIMEOUT, LocalDateTime.of(2022, 1, 12, 23, 11));
        CancelledTransaction cancelledTransaction2 = new CancelledTransaction("test2", CancellationReason.USER_CANCELLATION, LocalDateTime.of(2022, 12, 12, 23, 11));
        CancelledTransaction cancelledTransaction3 = new CancelledTransaction("test3", CancellationReason.CHANGE_NOT_AVAILABLE, LocalDateTime.of(2023, 12, 12, 23, 11));

        List<CancelledTransaction> cancelledTransactionList = Arrays.asList(new CancelledTransaction[] {cancelledTransaction1, cancelledTransaction2, cancelledTransaction3});

        csvFileParser.updateCancelledTransactions(cancelledTransactionList);

        List<CancelledTransaction> out = csvFileParser.getCancelledTransactions();

        assertEquals(out.size(), 3);

        CancelledTransaction out1 = out.get(0);
        assertEquals(out1.getUsername(), cancelledTransaction1.getUsername());
        assertEquals(out1.getCancellationReason(), cancelledTransaction1.getCancellationReason());
        assertEquals(out1.getTimeCancelled(), cancelledTransaction1.getTimeCancelled());

        CancelledTransaction out2 = out.get(1);
        assertEquals(out2.getUsername(), cancelledTransaction2.getUsername());
        assertEquals(out2.getCancellationReason(), cancelledTransaction2.getCancellationReason());
        assertEquals(out2.getTimeCancelled(), cancelledTransaction2.getTimeCancelled());

        CancelledTransaction out3 = out.get(2);
        assertEquals(out3.getUsername(), cancelledTransaction3.getUsername());
        assertEquals(out3.getCancellationReason(), cancelledTransaction3.getCancellationReason());
        assertEquals(out3.getTimeCancelled(), cancelledTransaction3.getTimeCancelled());

    }

    @Test
    public void getCancelledTransactionsEmpty(){

        csvFileParser = new CSVFileParser("", "", "", "");

        assertNotNull(csvFileParser.getCancelledTransactions());
        assertEquals(csvFileParser.getCancelledTransactions().size(), 0);

    }



}
