package experiment.correctness;

import experiment.common.ValidationAdapter;
import experiment.dataset.MoneyTransferTestCase;
import experiment.dataset.MoneyTransferTestCases;
import experiment.generated.explicit.GeneratedExplicitValidationAdapter;
import experiment.generated.jakarta.GeneratedJakartaValidationAdapter;
import experiment.manual.explicit.ManualExplicitValidationAdapter;
import experiment.manual.jakarta.ManualJakartaValidationAdapter;
import experiment.ruleengine.RuleEngineValidationAdapter;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CorrectnessComparisonTest {

    private final List<ValidationAdapter> adapters = List.of(
            new ManualExplicitValidationAdapter(),
            new ManualJakartaValidationAdapter(),
            new GeneratedExplicitValidationAdapter(),
            new GeneratedJakartaValidationAdapter(),
            new RuleEngineValidationAdapter()
    );

    @Test
    void allAvailableApproachesShouldProduceExpectedViolations() {
        for (MoneyTransferTestCase testCase : MoneyTransferTestCases.all()) {
            for (ValidationAdapter adapter : adapters) {
                var actual = adapter.validate(testCase.dto());

                assertEquals(
                        testCase.expectedViolations(),
                        actual,
                        "Mismatch for " + adapter.name()
                                + " / " + testCase.id()
                                + " / " + testCase.description()
                );
            }
        }
    }

    @Test
    void manualApproachesShouldBeSemanticallyEquivalent() {
        ValidationAdapter manualExplicit = new ManualExplicitValidationAdapter();
        ValidationAdapter manualJakarta = new ManualJakartaValidationAdapter();

        for (MoneyTransferTestCase testCase : MoneyTransferTestCases.all()) {
            var explicitResult = manualExplicit.validate(testCase.dto());
            var jakartaResult = manualJakarta.validate(testCase.dto());

            assertEquals(
                    explicitResult,
                    jakartaResult,
                    "Manual explicit and manual Jakarta results differ for "
                            + testCase.id()
                            + " / " + testCase.description()
            );
        }
    }
}