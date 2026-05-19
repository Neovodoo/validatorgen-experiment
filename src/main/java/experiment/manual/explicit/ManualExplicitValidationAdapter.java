package experiment.manual.explicit;

import experiment.common.ValidationAdapter;
import experiment.common.ViolationView;
import experiment.dto.MoneyTransferDto;

import java.util.List;

public final class ManualExplicitValidationAdapter implements ValidationAdapter {

    private final ManualMoneyTransferValidator validator = new ManualMoneyTransferValidator();

    @Override
    public String name() {
        return "manual-explicit";
    }

    @Override
    public List<ViolationView> validate(MoneyTransferDto dto) {
        return validator.validate(dto);
    }
}