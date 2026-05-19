package experiment.common;

import experiment.dto.MoneyTransferDto;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class SmokeTest {

    @Test
    void projectShouldCompileAndCreateDto() {
        MoneyTransferDto dto = new MoneyTransferDto();

        dto.setTransferId("TR-001");
        dto.setAmountCents(10_000L);

        assertEquals("TR-001", dto.getTransferId());
        assertEquals(10_000L, dto.getAmountCents());
    }

    @Test
    void violationViewShouldBeCreated() {
        ViolationView violation = new ViolationView(
                "CF-01",
                "feeCents",
                "Fee must be less than transfer amount"
        );

        assertEquals("CF-01", violation.ruleId());
        assertEquals("feeCents", violation.field());
        assertNotNull(violation.message());
    }
}