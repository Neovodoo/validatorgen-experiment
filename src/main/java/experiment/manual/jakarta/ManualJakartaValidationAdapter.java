package experiment.manual.jakarta;

import experiment.common.ValidationAdapter;
import experiment.common.ViolationView;
import experiment.dto.MoneyTransferDto;
import experiment.dto.MoneyTransferDtoJakarta;
import experiment.rules.ExperimentRules;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

import java.util.List;
import java.util.Set;

public final class ManualJakartaValidationAdapter implements ValidationAdapter {

    private final Validator validator;

    public ManualJakartaValidationAdapter() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        this.validator = factory.getValidator();
    }

    @Override
    public String name() {
        return "manual-jakarta";
    }

    @Override
    public List<ViolationView> validate(MoneyTransferDto dto) {
        MoneyTransferDtoJakarta jakartaDto = toJakartaDto(dto);

        Set<ConstraintViolation<MoneyTransferDtoJakarta>> violations =
                validator.validate(jakartaDto, ManualJakartaGroup.class);

        return violations.stream()
                .map(this::toViolationView)
                .sorted()
                .toList();
    }

    private ViolationView toViolationView(
            ConstraintViolation<MoneyTransferDtoJakarta> violation
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

        String ruleId = rawMessage.substring(0, delimiterIndex);
        String message = rawMessage.substring(delimiterIndex + 1);

        if (field == null || field.isBlank()) {
            field = ExperimentRules.byId(ruleId).targetField();
        }

        return new ViolationView(
                ruleId,
                field,
                message
        );
    }

    private static MoneyTransferDtoJakarta toJakartaDto(MoneyTransferDto source) {
        MoneyTransferDtoJakarta target = new MoneyTransferDtoJakarta();

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