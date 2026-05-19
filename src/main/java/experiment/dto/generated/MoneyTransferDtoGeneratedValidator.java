package experiment.dto.generated;

import java.util.ArrayList;
import java.util.List;
import experiment.dto.MoneyTransferDto;


//Это сгенерированный валидатор в явном формате, оставшиеся файлы в папке это аннотации
public final class MoneyTransferDtoGeneratedValidator {
    private MoneyTransferDtoGeneratedValidator() {}

    public static List<Violation> validate(MoneyTransferDto dto) {
        List<Violation> violations = new ArrayList<>();

        // Rule GT_amountCents_feeCents_1: amountCents > feeCents
        if (!(dto.getAmountCents() > dto.getFeeCents())) {
            violations.add(new Violation("feeCents", "Fee must be less than transfer amount", "GT_amountCents_feeCents_1"));
        }

        // Rule GT_amountCents_taxCents_2: amountCents > taxCents
        if (!(dto.getAmountCents() > dto.getTaxCents())) {
            violations.add(new Violation("taxCents", "Tax must be less than transfer amount", "GT_amountCents_taxCents_2"));
        }

        // Rule GE_amountCents_minAllowedAmountCents_3: amountCents >= minAllowedAmountCents
        if (!(dto.getAmountCents() >= dto.getMinAllowedAmountCents())) {
            violations.add(new Violation("amountCents", "Transfer amount must not be below minimum allowed amount", "GE_amountCents_minAllowedAmountCents_3"));
        }

        // Rule LE_amountCents_maxAllowedAmountCents_4: amountCents <= maxAllowedAmountCents
        if (!(dto.getAmountCents() <= dto.getMaxAllowedAmountCents())) {
            violations.add(new Violation("amountCents", "Transfer amount must not exceed maximum allowed amount", "LE_amountCents_maxAllowedAmountCents_4"));
        }

        // Rule LE_amountCents_availableBalanceCents_5: amountCents <= availableBalanceCents
        if (!(dto.getAmountCents() <= dto.getAvailableBalanceCents())) {
            violations.add(new Violation("amountCents", "Transfer amount must not exceed available balance", "LE_amountCents_availableBalanceCents_5"));
        }

        // Rule LE_totalDebitCents_availableBalanceCents_6: totalDebitCents <= availableBalanceCents
        if (!(dto.getTotalDebitCents() <= dto.getAvailableBalanceCents())) {
            violations.add(new Violation("totalDebitCents", "Total debit must not exceed available balance", "LE_totalDebitCents_availableBalanceCents_6"));
        }

        // Rule LE_totalDebitCents_dailyLimitCents_7: totalDebitCents <= dailyLimitCents
        if (!(dto.getTotalDebitCents() <= dto.getDailyLimitCents())) {
            violations.add(new Violation("totalDebitCents", "Total debit must not exceed daily limit", "LE_totalDebitCents_dailyLimitCents_7"));
        }

        // Rule NE_senderAccount_receiverAccount_8: senderAccount != receiverAccount
        if (!(!(java.util.Objects.equals(dto.getSenderAccount(), dto.getReceiverAccount())))) {
            violations.add(new Violation("receiverAccount", "Sender and receiver accounts must be different", "NE_senderAccount_receiverAccount_8"));
        }

        // Rule REQUIRED_IF_scheduledAt_transferType_9: scheduledAt required if transferType == SCHEDULED
        if (!(!(java.util.Objects.equals(dto.getTransferType(), "SCHEDULED")) || (dto.getScheduledAt() != null))) {
            violations.add(new Violation("scheduledAt", "Scheduled transfer must have scheduled date", "REQUIRED_IF_scheduledAt_transferType_9"));
        }

        // Rule REQUIRED_IF_executedAt_status_10: executedAt required if status == EXECUTED
        if (!(!(java.util.Objects.equals(dto.getStatus(), "EXECUTED")) || (dto.getExecutedAt() != null))) {
            violations.add(new Violation("executedAt", "Executed transfer must have execution date", "REQUIRED_IF_executedAt_status_10"));
        }

        // Rule REQUIRED_IF_cancelledAt_status_11: cancelledAt required if status == CANCELLED
        if (!(!(java.util.Objects.equals(dto.getStatus(), "CANCELLED")) || (dto.getCancelledAt() != null))) {
            violations.add(new Violation("cancelledAt", "Cancelled transfer must have cancellation date", "REQUIRED_IF_cancelledAt_status_11"));
        }

        // Rule REQUIRED_IF_confirmationCode_requiresConfirmation_12: confirmationCode required if requiresConfirmation == true
        if (!(!(java.lang.Boolean.TRUE.equals(dto.getRequiresConfirmation())) || (dto.getConfirmationCode() != null && !dto.getConfirmationCode().isBlank()))) {
            violations.add(new Violation("confirmationCode", "Confirmation code is required when confirmation is enabled", "REQUIRED_IF_confirmationCode_requiresConfirmation_12"));
        }

        // Rule REQUIRED_IF_templateName_saveTemplate_13: templateName required if saveTemplate == true
        if (!(!(java.lang.Boolean.TRUE.equals(dto.getSaveTemplate())) || (dto.getTemplateName() != null && !dto.getTemplateName().isBlank()))) {
            violations.add(new Violation("templateName", "Template name is required when saving transfer as template", "REQUIRED_IF_templateName_saveTemplate_13"));
        }

        // Rule REQUIRED_IF_eventType_channel_14: eventType required if channel == KAFKA
        if (!(!(java.util.Objects.equals(dto.getChannel(), "KAFKA")) || (dto.getEventType() != null && !dto.getEventType().isBlank()))) {
            violations.add(new Violation("eventType", "Kafka transfer event must have event type", "REQUIRED_IF_eventType_channel_14"));
        }

        // Rule REQUIRED_IF_eventKey_channel_15: eventKey required if channel == KAFKA
        if (!(!(java.util.Objects.equals(dto.getChannel(), "KAFKA")) || (dto.getEventKey() != null && !dto.getEventKey().isBlank()))) {
            violations.add(new Violation("eventKey", "Kafka transfer event must have event key", "REQUIRED_IF_eventKey_channel_15"));
        }

        // Rule REQUIRED_IF_requestId_channel_16: requestId required if channel == API
        if (!(!(java.util.Objects.equals(dto.getChannel(), "API")) || (dto.getRequestId() != null && !dto.getRequestId().isBlank()))) {
            violations.add(new Violation("requestId", "API transfer request must have request id", "REQUIRED_IF_requestId_channel_16"));
        }

        return violations;
    }

    public record Violation(String path, String message, String ruleId) {}
}