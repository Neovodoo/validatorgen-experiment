package experiment.ruleengine;

public record RuleEngineViolation(
        String ruleId,
        String field,
        String message
) {
}