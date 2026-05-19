package experiment.manual.jakarta;

import experiment.dto.MoneyTransferDtoJakarta;
import experiment.rules.ExperimentRule;
import experiment.rules.ExperimentRules;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.time.LocalDateTime;
import java.util.Objects;

public final class ManualMoneyTransferConstraintValidator
        implements ConstraintValidator<ManualMoneyTransferValid, MoneyTransferDtoJakarta> {

    @Override
    public boolean isValid(
            MoneyTransferDtoJakarta dto,
            ConstraintValidatorContext context
    ) {
        if (dto == null) {
            return true;
        }

        context.disableDefaultConstraintViolation();

        boolean valid = true;

        // CF-01: amountCents > feeCents
        if (!greaterThan(dto.getAmountCents(), dto.getFeeCents())) {
            addViolation(context, "CF-01");
            valid = false;
        }

        // CF-02: amountCents > taxCents
        if (!greaterThan(dto.getAmountCents(), dto.getTaxCents())) {
            addViolation(context, "CF-02");
            valid = false;
        }

        // CF-03: amountCents >= minAllowedAmountCents
        if (!greaterThanOrEqual(dto.getAmountCents(), dto.getMinAllowedAmountCents())) {
            addViolation(context, "CF-03");
            valid = false;
        }

        // CF-04: amountCents <= maxAllowedAmountCents
        if (!lessThanOrEqual(dto.getAmountCents(), dto.getMaxAllowedAmountCents())) {
            addViolation(context, "CF-04");
            valid = false;
        }

        // CF-05: amountCents <= availableBalanceCents
        if (!lessThanOrEqual(dto.getAmountCents(), dto.getAvailableBalanceCents())) {
            addViolation(context, "CF-05");
            valid = false;
        }

        // CF-06: totalDebitCents <= availableBalanceCents
        if (!lessThanOrEqual(dto.getTotalDebitCents(), dto.getAvailableBalanceCents())) {
            addViolation(context, "CF-06");
            valid = false;
        }

        // CF-07: totalDebitCents <= dailyLimitCents
        if (!lessThanOrEqual(dto.getTotalDebitCents(), dto.getDailyLimitCents())) {
            addViolation(context, "CF-07");
            valid = false;
        }

        // CF-08: senderAccount != receiverAccount
        if (!notEqual(dto.getSenderAccount(), dto.getReceiverAccount())) {
            addViolation(context, "CF-08");
            valid = false;
        }

        // RI-01: scheduledAt required if transferType == SCHEDULED
        if (Objects.equals(dto.getTransferType(), "SCHEDULED")
                && !isPresent(dto.getScheduledAt())) {
            addViolation(context, "RI-01");
            valid = false;
        }

        // RI-02: executedAt required if status == EXECUTED
        if (Objects.equals(dto.getStatus(), "EXECUTED")
                && !isPresent(dto.getExecutedAt())) {
            addViolation(context, "RI-02");
            valid = false;
        }

        // RI-03: cancelledAt required if status == CANCELLED
        if (Objects.equals(dto.getStatus(), "CANCELLED")
                && !isPresent(dto.getCancelledAt())) {
            addViolation(context, "RI-03");
            valid = false;
        }

        // RI-04: confirmationCode required if requiresConfirmation == true
        if (Boolean.TRUE.equals(dto.getRequiresConfirmation())
                && !isPresent(dto.getConfirmationCode())) {
            addViolation(context, "RI-04");
            valid = false;
        }

        // RI-05: templateName required if saveTemplate == true
        if (Boolean.TRUE.equals(dto.getSaveTemplate())
                && !isPresent(dto.getTemplateName())) {
            addViolation(context, "RI-05");
            valid = false;
        }

        // RI-06: eventType required if channel == KAFKA
        if (Objects.equals(dto.getChannel(), "KAFKA")
                && !isPresent(dto.getEventType())) {
            addViolation(context, "RI-06");
            valid = false;
        }

        // RI-07: eventKey required if channel == KAFKA
        if (Objects.equals(dto.getChannel(), "KAFKA")
                && !isPresent(dto.getEventKey())) {
            addViolation(context, "RI-07");
            valid = false;
        }

        // RI-08: requestId required if channel == API
        if (Objects.equals(dto.getChannel(), "API")
                && !isPresent(dto.getRequestId())) {
            addViolation(context, "RI-08");
            valid = false;
        }

        return valid;
    }

    private static void addViolation(
            ConstraintValidatorContext context,
            String ruleId
    ) {
        ExperimentRule rule = ExperimentRules.byId(ruleId);

        context.buildConstraintViolationWithTemplate(
                        rule.ruleId() + "|" + rule.message()
                )
                .addPropertyNode(rule.targetField())
                .addConstraintViolation();
    }

    private static boolean greaterThan(Long left, Long right) {
        return left != null && right != null && left > right;
    }

    private static boolean greaterThanOrEqual(Long left, Long right) {
        return left != null && right != null && left >= right;
    }

    private static boolean lessThanOrEqual(Long left, Long right) {
        return left != null && right != null && left <= right;
    }

    private static boolean notEqual(String left, String right) {
        return isPresent(left) && isPresent(right) && !Objects.equals(left, right);
    }

    private static boolean isPresent(String value) {
        return value != null && !value.isBlank();
    }

    private static boolean isPresent(LocalDateTime value) {
        return value != null;
    }
}