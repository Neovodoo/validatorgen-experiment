package experiment.dataset;

import experiment.rules.ExperimentRules;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class MoneyTransferTestCasesTest {

    @Test
    void shouldContainFiftyTestCases() {
        assertEquals(50, MoneyTransferTestCases.all().size());
    }

    @Test
    void testCaseIdsShouldBeUnique() {
        HashSet<String> ids = new HashSet<>();

        for (MoneyTransferTestCase testCase : MoneyTransferTestCases.all()) {
            assertTrue(ids.add(testCase.id()), "Duplicate test case id: " + testCase.id());
        }
    }

    @Test
    void tc01ShouldBeValid() {
        MoneyTransferTestCase tc01 = MoneyTransferTestCases.all().get(0);

        assertEquals("TC-01", tc01.id());
        assertTrue(tc01.expectedViolations().isEmpty());
    }

    @Test
    void tc11ShouldExpectCf01() {
        MoneyTransferTestCase tc11 = MoneyTransferTestCases.all().stream()
                .filter(testCase -> testCase.id().equals("TC-11"))
                .findFirst()
                .orElseThrow();

        assertEquals(
                List.of(ExperimentRules.violation("CF-01")),
                tc11.expectedViolations()
        );
    }

    @Test
    void tc35ShouldExpectThreeViolations() {
        MoneyTransferTestCase tc35 = MoneyTransferTestCases.all().stream()
                .filter(testCase -> testCase.id().equals("TC-35"))
                .findFirst()
                .orElseThrow();

        assertEquals(3, tc35.expectedViolations().size());
        assertEquals(
                List.of(
                        ExperimentRules.violation("RI-02"),
                        ExperimentRules.violation("RI-06"),
                        ExperimentRules.violation("RI-07")
                ),
                tc35.expectedViolations()
        );
    }
}