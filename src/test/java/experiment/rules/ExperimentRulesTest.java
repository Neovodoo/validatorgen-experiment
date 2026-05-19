package experiment.rules;

import experiment.common.ViolationView;
import org.junit.jupiter.api.Test;

import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ExperimentRulesTest {

    @Test
    void shouldContainSixteenRules() {
        assertEquals(16, ExperimentRules.all().size());
    }

    @Test
    void shouldContainEightCompareFieldsRules() {
        assertEquals(8, ExperimentRules.compareFieldsRules().size());
    }

    @Test
    void shouldContainEightRequiredIfRules() {
        assertEquals(8, ExperimentRules.requiredIfRules().size());
    }

    @Test
    void ruleIdsShouldBeUnique() {
        HashSet<String> ids = new HashSet<>();

        for (ExperimentRule rule : ExperimentRules.all()) {
            boolean added = ids.add(rule.ruleId());
            if (!added) {
                throw new AssertionError("Duplicate rule id: " + rule.ruleId());
            }
        }
    }

    @Test
    void shouldCreateViolationByRuleId() {
        ViolationView violation = ExperimentRules.violation("CF-01");

        assertEquals("CF-01", violation.ruleId());
        assertEquals("feeCents", violation.field());
        assertEquals("Fee must be less than transfer amount", violation.message());
    }

    @Test
    void shouldRejectUnknownRuleId() {
        assertThrows(
                IllegalArgumentException.class,
                () -> ExperimentRules.byId("UNKNOWN")
        );
    }
}