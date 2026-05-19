package experiment.common;

import java.util.Objects;

public record ViolationView(
        String ruleId,
        String field,
        String message
) implements Comparable<ViolationView> {

    public ViolationView {
        Objects.requireNonNull(ruleId, "ruleId must not be null");
        Objects.requireNonNull(field, "field must not be null");
        Objects.requireNonNull(message, "message must not be null");
    }

    @Override
    public int compareTo(ViolationView other) {
        int byRuleId = this.ruleId.compareTo(other.ruleId);
        if (byRuleId != 0) {
            return byRuleId;
        }

        int byField = this.field.compareTo(other.field);
        if (byField != 0) {
            return byField;
        }

        return this.message.compareTo(other.message);
    }
}