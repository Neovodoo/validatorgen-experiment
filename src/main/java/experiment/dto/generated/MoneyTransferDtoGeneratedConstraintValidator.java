package experiment.dto.generated;

import experiment.dto.MoneyTransferDto;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public final class MoneyTransferDtoGeneratedConstraintValidator
        implements ConstraintValidator<MoneyTransferDtoGeneratedValidation, MoneyTransferDto> {

    @Override
    public boolean isValid(MoneyTransferDto dto, ConstraintValidatorContext context) {
        if (dto == null) {
            return true;
        }

        context.disableDefaultConstraintViolation();

        boolean valid = true;

        // Rule GT_amountCents_feeCents_1: amountCents > feeCents
        if (!(dto.getAmountCents() > dto.getFeeCents())) {
            addViolation(context, "feeCents", "Fee must be less than transfer amount", "GT_amountCents_feeCents_1");
            valid = false;
        }

        // Rule GT_amountCents_taxCents_2: amountCents > taxCents
        if (!(dto.getAmountCents() > dto.getTaxCents())) {
            addViolation(context, "taxCents", "Tax must be less than transfer amount", "GT_amountCents_taxCents_2");
            valid = false;
        }

        // Rule GE_amountCents_minAllowedAmountCents_3: amountCents >= minAllowedAmountCents
        if (!(dto.getAmountCents() >= dto.getMinAllowedAmountCents())) {
            addViolation(context, "amountCents", "Transfer amount must not be below minimum allowed amount", "GE_amountCents_minAllowedAmountCents_3");
            valid = false;
        }

        // Rule LE_amountCents_maxAllowedAmountCents_4: amountCents <= maxAllowedAmountCents
        if (!(dto.getAmountCents() <= dto.getMaxAllowedAmountCents())) {
            addViolation(context, "amountCents", "Transfer amount must not exceed maximum allowed amount", "LE_amountCents_maxAllowedAmountCents_4");
            valid = false;
        }

        // Rule LE_amountCents_availableBalanceCents_5: amountCents <= availableBalanceCents
        if (!(dto.getAmountCents() <= dto.getAvailableBalanceCents())) {
            addViolation(context, "amountCents", "Transfer amount must not exceed available balance", "LE_amountCents_availableBalanceCents_5");
            valid = false;
        }

        // Rule LE_totalDebitCents_availableBalanceCents_6: totalDebitCents <= availableBalanceCents
        if (!(dto.getTotalDebitCents() <= dto.getAvailableBalanceCents())) {
            addViolation(context, "totalDebitCents", "Total debit must not exceed available balance", "LE_totalDebitCents_availableBalanceCents_6");
            valid = false;
        }

        // Rule LE_totalDebitCents_dailyLimitCents_7: totalDebitCents <= dailyLimitCents
        if (!(dto.getTotalDebitCents() <= dto.getDailyLimitCents())) {
            addViolation(context, "totalDebitCents", "Total debit must not exceed daily limit", "LE_totalDebitCents_dailyLimitCents_7");
            valid = false;
        }

        // Rule NE_senderAccount_receiverAccount_8: senderAccount != receiverAccount
        if (!(!(java.util.Objects.equals(dto.getSenderAccount(), dto.getReceiverAccount())))) {
            addViolation(context, "receiverAccount", "Sender and receiver accounts must be different", "NE_senderAccount_receiverAccount_8");
            valid = false;
        }

        // Rule REQUIRED_IF_scheduledAt_transferType_9: scheduledAt required if transferType == SCHEDULED
        if (!(!(java.util.Objects.equals(dto.getTransferType(), "SCHEDULED")) || (dto.getScheduledAt() != null))) {
            addViolation(context, "scheduledAt", "Scheduled transfer must have scheduled date", "REQUIRED_IF_scheduledAt_transferType_9");
            valid = false;
        }

        // Rule REQUIRED_IF_executedAt_status_10: executedAt required if status == EXECUTED
        if (!(!(java.util.Objects.equals(dto.getStatus(), "EXECUTED")) || (dto.getExecutedAt() != null))) {
            addViolation(context, "executedAt", "Executed transfer must have execution date", "REQUIRED_IF_executedAt_status_10");
            valid = false;
        }

        // Rule REQUIRED_IF_cancelledAt_status_11: cancelledAt required if status == CANCELLED
        if (!(!(java.util.Objects.equals(dto.getStatus(), "CANCELLED")) || (dto.getCancelledAt() != null))) {
            addViolation(context, "cancelledAt", "Cancelled transfer must have cancellation date", "REQUIRED_IF_cancelledAt_status_11");
            valid = false;
        }

        // Rule REQUIRED_IF_confirmationCode_requiresConfirmation_12: confirmationCode required if requiresConfirmation == true
        if (!(!(java.lang.Boolean.TRUE.equals(dto.getRequiresConfirmation())) || (dto.getConfirmationCode() != null && !dto.getConfirmationCode().isBlank()))) {
            addViolation(context, "confirmationCode", "Confirmation code is required when confirmation is enabled", "REQUIRED_IF_confirmationCode_requiresConfirmation_12");
            valid = false;
        }

        // Rule REQUIRED_IF_templateName_saveTemplate_13: templateName required if saveTemplate == true
        if (!(!(java.lang.Boolean.TRUE.equals(dto.getSaveTemplate())) || (dto.getTemplateName() != null && !dto.getTemplateName().isBlank()))) {
            addViolation(context, "templateName", "Template name is required when saving transfer as template", "REQUIRED_IF_templateName_saveTemplate_13");
            valid = false;
        }

        // Rule REQUIRED_IF_eventType_channel_14: eventType required if channel == KAFKA
        if (!(!(java.util.Objects.equals(dto.getChannel(), "KAFKA")) || (dto.getEventType() != null && !dto.getEventType().isBlank()))) {
            addViolation(context, "eventType", "Kafka transfer event must have event type", "REQUIRED_IF_eventType_channel_14");
            valid = false;
        }

        // Rule REQUIRED_IF_eventKey_channel_15: eventKey required if channel == KAFKA
        if (!(!(java.util.Objects.equals(dto.getChannel(), "KAFKA")) || (dto.getEventKey() != null && !dto.getEventKey().isBlank()))) {
            addViolation(context, "eventKey", "Kafka transfer event must have event key", "REQUIRED_IF_eventKey_channel_15");
            valid = false;
        }

        // Rule REQUIRED_IF_requestId_channel_16: requestId required if channel == API
        if (!(!(java.util.Objects.equals(dto.getChannel(), "API")) || (dto.getRequestId() != null && !dto.getRequestId().isBlank()))) {
            addViolation(context, "requestId", "API transfer request must have request id", "REQUIRED_IF_requestId_channel_16");
            valid = false;
        }

        return valid;
    }

    private static void addViolation(
            ConstraintValidatorContext context,
            String path,
            String message,
            String ruleId
    ) {
        context.buildConstraintViolationWithTemplate(ruleId + "|" + message)
                .addPropertyNode(path)
                .addConstraintViolation();
    }
}