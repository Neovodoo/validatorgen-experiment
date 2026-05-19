package experiment.generated.jakarta;

import experiment.common.ValidationAdapter;
import experiment.common.ViolationView;
import experiment.dto.MoneyTransferDto;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

import java.util.List;
import java.util.Map;
import java.util.Set;

public final class GeneratedJakartaValidationAdapter implements ValidationAdapter {

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

    private final Validator validator;

    public GeneratedJakartaValidationAdapter() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        this.validator = factory.getValidator();
    }

    @Override
    public String name() {
        return "generated-jakarta";
    }

    @Override
    public List<ViolationView> validate(MoneyTransferDto dto) {
        MoneyTransferDtoGeneratedJakarta jakartaDto = toGeneratedJakartaDto(dto);

        Set<ConstraintViolation<MoneyTransferDtoGeneratedJakarta>> violations =
                validator.validate(jakartaDto, GeneratedJakartaGroup.class);

        return violations.stream()
                .map(this::toViolationView)
                .sorted()
                .toList();
    }

    private ViolationView toViolationView(
            ConstraintViolation<MoneyTransferDtoGeneratedJakarta> violation
    ) {
        String rawMessage = violation.getMessage();
        String field = violation.getPropertyPath().toString();

        int delimiterIndex = rawMessage.indexOf('|');
        if (delimiterIndex < 0) {
            return new ViolationView(
                    "UNKNOWN",
                    field,
                    rawMessage
            );
        }

        String generatedRuleId = rawMessage.substring(0, delimiterIndex);
        String message = rawMessage.substring(delimiterIndex + 1);

        String canonicalRuleId = GENERATED_TO_CANONICAL_RULE_ID.get(generatedRuleId);
        if (canonicalRuleId == null) {
            throw new IllegalStateException("Unknown generated rule id: " + generatedRuleId);
        }

        return new ViolationView(
                canonicalRuleId,
                field,
                message
        );
    }

    private static MoneyTransferDtoGeneratedJakarta toGeneratedJakartaDto(MoneyTransferDto source) {
        MoneyTransferDtoGeneratedJakarta target = new MoneyTransferDtoGeneratedJakarta();

        target.setTransferId(source.getTransferId());
        target.setRequestId(source.getRequestId());
        target.setSourceSystem(source.getSourceSystem());

        target.setSenderAccount(source.getSenderAccount());
        target.setReceiverAccount(source.getReceiverAccount());
        target.setReceiverPhone(source.getReceiverPhone());
        target.setReceiverEmail(source.getReceiverEmail());

        target.setTransferType(source.getTransferType());
        target.setStatus(source.getStatus());
        target.setChannel(source.getChannel());

        target.setAmountCents(source.getAmountCents());
        target.setFeeCents(source.getFeeCents());
        target.setTaxCents(source.getTaxCents());
        target.setTotalDebitCents(source.getTotalDebitCents());
        target.setAvailableBalanceCents(source.getAvailableBalanceCents());
        target.setDailyLimitCents(source.getDailyLimitCents());
        target.setMinAllowedAmountCents(source.getMinAllowedAmountCents());
        target.setMaxAllowedAmountCents(source.getMaxAllowedAmountCents());

        target.setRequiresConfirmation(source.getRequiresConfirmation());
        target.setConfirmationCode(source.getConfirmationCode());

        target.setSaveTemplate(source.getSaveTemplate());
        target.setTemplateName(source.getTemplateName());

        target.setCreatedAt(source.getCreatedAt());
        target.setScheduledAt(source.getScheduledAt());
        target.setExecutedAt(source.getExecutedAt());
        target.setCancelledAt(source.getCancelledAt());

        target.setEventType(source.getEventType());
        target.setEventKey(source.getEventKey());
        target.setEventVersion(source.getEventVersion());

        target.setComment(source.getComment());

        return target;
    }
}