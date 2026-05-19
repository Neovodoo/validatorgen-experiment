package experiment.manual.explicit;

import experiment.common.ViolationView;
import experiment.dto.MoneyTransferDto;
import experiment.rules.ExperimentRules;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public final class ManualMoneyTransferValidator {

    public List<ViolationView> validate(MoneyTransferDto dto) {
        Objects.requireNonNull(dto, "dto must not be null");

        List<ViolationView> violations = new ArrayList<>();

        validateCompareFieldsRules(dto, violations);
        validateRequiredIfRules(dto, violations);

        return violations.stream()
                .sorted()
                .toList();
    }

    private void validateCompareFieldsRules(
            MoneyTransferDto dto,
            List<ViolationView> violations
    ) {
        // CF-01: amountCents > feeCents
        if (!greaterThan(dto.getAmountCents(), dto.getFeeCents())) {
            violations.add(ExperimentRules.violation("CF-01"));
        }

        // CF-02: amountCents > taxCents
        if (!greaterThan(dto.getAmountCents(), dto.getTaxCents())) {
            violations.add(ExperimentRules.violation("CF-02"));
        }

        // CF-03: amountCents >= minAllowedAmountCents
        if (!greaterThanOrEqual(dto.getAmountCents(), dto.getMinAllowedAmountCents())) {
            violations.add(ExperimentRules.violation("CF-03"));
        }

        // CF-04: amountCents <= maxAllowedAmountCents
        if (!lessThanOrEqual(dto.getAmountCents(), dto.getMaxAllowedAmountCents())) {
            violations.add(ExperimentRules.violation("CF-04"));
        }

        // CF-05: amountCents <= availableBalanceCents
        if (!lessThanOrEqual(dto.getAmountCents(), dto.getAvailableBalanceCents())) {
            violations.add(ExperimentRules.violation("CF-05"));
        }

        // CF-06: totalDebitCents <= availableBalanceCents
        if (!lessThanOrEqual(dto.getTotalDebitCents(), dto.getAvailableBalanceCents())) {
            violations.add(ExperimentRules.violation("CF-06"));
        }

        // CF-07: totalDebitCents <= dailyLimitCents
        if (!lessThanOrEqual(dto.getTotalDebitCents(), dto.getDailyLimitCents())) {
            violations.add(ExperimentRules.violation("CF-07"));
        }

        // CF-08: senderAccount != receiverAccount
        if (!notEqual(dto.getSenderAccount(), dto.getReceiverAccount())) {
            violations.add(ExperimentRules.violation("CF-08"));
        }
    }

    private void validateRequiredIfRules(
            MoneyTransferDto dto,
            List<ViolationView> violations
    ) {
        // RI-01: scheduledAt required if transferType == SCHEDULED
        if (Objects.equals(dto.getTransferType(), "SCHEDULED")
                && !isPresent(dto.getScheduledAt())) {
            violations.add(ExperimentRules.violation("RI-01"));
        }

        // RI-02: executedAt required if status == EXECUTED
        if (Objects.equals(dto.getStatus(), "EXECUTED")
                && !isPresent(dto.getExecutedAt())) {
            violations.add(ExperimentRules.violation("RI-02"));
        }

        // RI-03: cancelledAt required if status == CANCELLED
        if (Objects.equals(dto.getStatus(), "CANCELLED")
                && !isPresent(dto.getCancelledAt())) {
            violations.add(ExperimentRules.violation("RI-03"));
        }

        // RI-04: confirmationCode required if requiresConfirmation == true
        if (Boolean.TRUE.equals(dto.getRequiresConfirmation())
                && !isPresent(dto.getConfirmationCode())) {
            violations.add(ExperimentRules.violation("RI-04"));
        }

        // RI-05: templateName required if saveTemplate == true
        if (Boolean.TRUE.equals(dto.getSaveTemplate())
                && !isPresent(dto.getTemplateName())) {
            violations.add(ExperimentRules.violation("RI-05"));
        }

        // RI-06: eventType required if channel == KAFKA
        if (Objects.equals(dto.getChannel(), "KAFKA")
                && !isPresent(dto.getEventType())) {
            violations.add(ExperimentRules.violation("RI-06"));
        }

        // RI-07: eventKey required if channel == KAFKA
        if (Objects.equals(dto.getChannel(), "KAFKA")
                && !isPresent(dto.getEventKey())) {
            violations.add(ExperimentRules.violation("RI-07"));
        }

        // RI-08: requestId required if channel == API
        if (Objects.equals(dto.getChannel(), "API")
                && !isPresent(dto.getRequestId())) {
            violations.add(ExperimentRules.violation("RI-08"));
        }
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