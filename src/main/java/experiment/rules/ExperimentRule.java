package experiment.rules;

import java.util.Objects;

public record ExperimentRule(
        String ruleId,
        RuleKind kind,
        String expression,
        String targetField,
        String message
) {

    public ExperimentRule {
        Objects.requireNonNull(ruleId, "ruleId must not be null");
        Objects.requireNonNull(kind, "kind must not be null");
        Objects.requireNonNull(expression, "expression must not be null");
        Objects.requireNonNull(targetField, "targetField must not be null");
        Objects.requireNonNull(message, "message must not be null");
    }
}