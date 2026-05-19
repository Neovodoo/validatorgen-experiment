package experiment.dataset;

import experiment.common.ViolationView;
import experiment.dto.MoneyTransferDto;

import java.util.List;
import java.util.Objects;

public record MoneyTransferTestCase(
        String id,
        String description,
        MoneyTransferDto dto,
        List<ViolationView> expectedViolations
) {

    public MoneyTransferTestCase {
        Objects.requireNonNull(id, "id must not be null");
        Objects.requireNonNull(description, "description must not be null");
        Objects.requireNonNull(dto, "dto must not be null");
        Objects.requireNonNull(expectedViolations, "expectedViolations must not be null");
    }
}