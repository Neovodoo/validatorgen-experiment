package experiment.generated.explicit;

import experiment.common.ValidationAdapter;
import experiment.common.ViolationView;
import experiment.dto.MoneyTransferDto;
import experiment.dto.generated.MoneyTransferDtoGeneratedValidator;

import java.util.List;
import java.util.Map;

public final class GeneratedExplicitValidationAdapter implements ValidationAdapter {

    private static final Map<String, String> GENERATED_TO_CANONICAL_RULE_ID = Map.ofEntries(
            Map.entry("GT_amountCents_feeCents_1", "CF-01"),
            Map.entry("GT_amountCents_taxCents_2", "CF-02"),
            Map.entry("GE_amountCents_minAllowedAmountCents_3", "CF-03"),
            Map.entry("LE_amountCents_maxAllowedAmountCents_4", "CF-04"),
            Map.entry("LE_amountCents_availableBalanceCents_5", "CF-05"),
            Map.entry("LE_totalDebitCents_availableBalanceCents_6", "CF-06"),
            Map.entry("LE_totalDebitCents_dailyLimitCents_7", "CF-07"),
            Map.entry("NE_senderAccount_receiverAccount_8", "CF-08"),
            Map.entry("REQUIRED_IF_scheduledAt_transferType_9", "RI-01"),
            Map.entry("REQUIRED_IF_executedAt_status_10", "RI-02"),
            Map.entry("REQUIRED_IF_cancelledAt_status_11", "RI-03"),
            Map.entry("REQUIRED_IF_confirmationCode_requiresConfirmation_12", "RI-04"),
            Map.entry("REQUIRED_IF_templateName_saveTemplate_13", "RI-05"),
            Map.entry("REQUIRED_IF_eventType_channel_14", "RI-06"),
            Map.entry("REQUIRED_IF_eventKey_channel_15", "RI-07"),
            Map.entry("REQUIRED_IF_requestId_channel_16", "RI-08")
    );

    @Override
    public String name() {
        return "generated-explicit";
    }

    @Override
    public List<ViolationView> validate(MoneyTransferDto dto) {
        return MoneyTransferDtoGeneratedValidator.validate(dto)
                .stream()
                .map(this::toViolationView)
                .sorted()
                .toList();
    }

    private ViolationView toViolationView(
            MoneyTransferDtoGeneratedValidator.Violation violation
    ) {
        String canonicalRuleId = GENERATED_TO_CANONICAL_RULE_ID.get(violation.ruleId());

        if (canonicalRuleId == null) {
            throw new IllegalStateException(
                    "Unknown generated rule id: " + violation.ruleId()
            );
        }

        return new ViolationView(
                canonicalRuleId,
                violation.path(),
                violation.message()
        );
    }
}