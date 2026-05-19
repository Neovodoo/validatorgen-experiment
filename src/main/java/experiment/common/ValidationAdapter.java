package experiment.common;

import experiment.dto.MoneyTransferDto;

import java.util.List;

public interface ValidationAdapter {

    String name();

    List<ViolationView> validate(MoneyTransferDto dto);
}