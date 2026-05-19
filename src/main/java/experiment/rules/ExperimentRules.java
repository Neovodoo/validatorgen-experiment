package experiment.rules;

import experiment.common.ViolationView;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public final class ExperimentRules {

    private ExperimentRules() {
    }

    public static final ExperimentRule CF_01 = new ExperimentRule(
            "CF-01",
            RuleKind.COMPARE_FIELDS,
            "amountCents > feeCents",
            "feeCents",
            "Fee must be less than transfer amount"
    );

    public static final ExperimentRule CF_02 = new ExperimentRule(
            "CF-02",
            RuleKind.COMPARE_FIELDS,
            "amountCents > taxCents",
            "taxCents",
            "Tax must be less than transfer amount"
    );

    public static final ExperimentRule CF_03 = new ExperimentRule(
            "CF-03",
            RuleKind.COMPARE_FIELDS,
            "amountCents >= minAllowedAmountCents",
            "amountCents",
            "Transfer amount must not be below minimum allowed amount"
    );

    public static final ExperimentRule CF_04 = new ExperimentRule(
            "CF-04",
            RuleKind.COMPARE_FIELDS,
            "amountCents <= maxAllowedAmountCents",
            "amountCents",
            "Transfer amount must not exceed maximum allowed amount"
    );

    public static final ExperimentRule CF_05 = new ExperimentRule(
            "CF-05",
            RuleKind.COMPARE_FIELDS,
            "amountCents <= availableBalanceCents",
            "amountCents",
            "Transfer amount must not exceed available balance"
    );

    public static final ExperimentRule CF_06 = new ExperimentRule(
            "CF-06",
            RuleKind.COMPARE_FIELDS,
            "totalDebitCents <= availableBalanceCents",
            "totalDebitCents",
            "Total debit must not exceed available balance"
    );

    public static final ExperimentRule CF_07 = new ExperimentRule(
            "CF-07",
            RuleKind.COMPARE_FIELDS,
            "totalDebitCents <= dailyLimitCents",
            "totalDebitCents",
            "Total debit must not exceed daily limit"
    );

    public static final ExperimentRule CF_08 = new ExperimentRule(
            "CF-08",
            RuleKind.COMPARE_FIELDS,
            "senderAccount != receiverAccount",
            "receiverAccount",
            "Sender and receiver accounts must be different"
    );

    public static final ExperimentRule RI_01 = new ExperimentRule(
            "RI-01",
            RuleKind.REQUIRED_IF,
            "scheduledAt required if transferType == SCHEDULED",
            "scheduledAt",
            "Scheduled transfer must have scheduled date"
    );

    public static final ExperimentRule RI_02 = new ExperimentRule(
            "RI-02",
            RuleKind.REQUIRED_IF,
            "executedAt required if status == EXECUTED",
            "executedAt",
            "Executed transfer must have execution date"
    );

    public static final ExperimentRule RI_03 = new ExperimentRule(
            "RI-03",
            RuleKind.REQUIRED_IF,
            "cancelledAt required if status == CANCELLED",
            "cancelledAt",
            "Cancelled transfer must have cancellation date"
    );

    public static final ExperimentRule RI_04 = new ExperimentRule(
            "RI-04",
            RuleKind.REQUIRED_IF,
            "confirmationCode required if requiresConfirmation == true",
            "confirmationCode",
            "Confirmation code is required when confirmation is enabled"
    );

    public static final ExperimentRule RI_05 = new ExperimentRule(
            "RI-05",
            RuleKind.REQUIRED_IF,
            "templateName required if saveTemplate == true",
            "templateName",
            "Template name is required when saving transfer as template"
    );

    public static final ExperimentRule RI_06 = new ExperimentRule(
            "RI-06",
            RuleKind.REQUIRED_IF,
            "eventType required if channel == KAFKA",
            "eventType",
            "Kafka transfer event must have event type"
    );

    public static final ExperimentRule RI_07 = new ExperimentRule(
            "RI-07",
            RuleKind.REQUIRED_IF,
            "eventKey required if channel == KAFKA",
            "eventKey",
            "Kafka transfer event must have event key"
    );

    public static final ExperimentRule RI_08 = new ExperimentRule(
            "RI-08",
            RuleKind.REQUIRED_IF,
            "requestId required if channel == API",
            "requestId",
            "API transfer request must have request id"
    );

    private static final List<ExperimentRule> ALL = List.of(
            CF_01,
            CF_02,
            CF_03,
            CF_04,
            CF_05,
            CF_06,
            CF_07,
            CF_08,
            RI_01,
            RI_02,
            RI_03,
            RI_04,
            RI_05,
            RI_06,
            RI_07,
            RI_08
    );

    private static final Map<String, ExperimentRule> BY_ID = ALL.stream()
            .collect(Collectors.toUnmodifiableMap(
                    ExperimentRule::ruleId,
                    Function.identity()
            ));

    public static List<ExperimentRule> all() {
        return ALL;
    }

    public static List<ExperimentRule> compareFieldsRules() {
        return ALL.stream()
                .filter(rule -> rule.kind() == RuleKind.COMPARE_FIELDS)
                .toList();
    }

    public static List<ExperimentRule> requiredIfRules() {
        return ALL.stream()
                .filter(rule -> rule.kind() == RuleKind.REQUIRED_IF)
                .toList();
    }

    public static ExperimentRule byId(String ruleId) {
        ExperimentRule rule = BY_ID.get(ruleId);
        if (rule == null) {
            throw new IllegalArgumentException("Unknown experiment rule id: " + ruleId);
        }
        return rule;
    }

    public static ViolationView violation(String ruleId) {
        ExperimentRule rule = byId(ruleId);

        return new ViolationView(
                rule.ruleId(),
                rule.targetField(),
                rule.message()
        );
    }
}