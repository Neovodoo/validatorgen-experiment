package experiment.generated.jakarta;

import experiment.common.ValidationAdapter;
import experiment.dataset.MoneyTransferTestCase;
import experiment.dataset.MoneyTransferTestCases;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class GeneratedJakartaValidationAdapterTest {

    private final ValidationAdapter adapter = new GeneratedJakartaValidationAdapter();

    @Test
    void shouldMatchExpectedViolationsForAllTestCases() {
        for (MoneyTransferTestCase testCase : MoneyTransferTestCases.all()) {
            var actual = adapter.validate(testCase.dto());

            assertEquals(
                    testCase.expectedViolations(),
                    actual,
                    "Mismatch for " + adapter.name() + " / " + testCase.id()
                            + " / " + testCase.description()
            );
        }
    }
}