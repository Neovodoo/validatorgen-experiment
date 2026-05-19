package experiment.dataset;

import experiment.common.ViolationView;
import experiment.dto.MoneyTransferDto;
import experiment.rules.ExperimentRules;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;

public final class MoneyTransferTestCases {

    private MoneyTransferTestCases() {
    }

    public static List<MoneyTransferTestCase> all() {
        return List.of(
                tc01(),
                tc02(),
                tc03(),
                tc04(),
                tc05(),
                tc06(),
                tc07(),
                tc08(),
                tc09(),
                tc10(),
                tc11(),
                tc12(),
                tc13(),
                tc14(),
                tc15(),
                tc16(),
                tc17(),
                tc18(),
                tc19(),
                tc20(),
                tc21(),
                tc22(),
                tc23(),
                tc24(),
                tc25(),
                tc26(),
                tc27(),
                tc28(),
                tc29(),
                tc30(),
                tc31(),
                tc32(),
                tc33(),
                tc34(),
                tc35(),
                tc36(),
                tc37(),
                tc38(),
                tc39(),
                tc40(),
                tc41(),
                tc42(),
                tc43(),
                tc44(),
                tc45(),
                tc46(),
                tc47(),
                tc48(),
                tc49(),
                tc50()
        );
    }

    private static MoneyTransferDto baseValidDto() {
        MoneyTransferDto dto = new MoneyTransferDto();

        dto.setTransferId("TR-001");
        dto.setRequestId("REQ-001");
        dto.setSourceSystem("mobile-app");

        dto.setSenderAccount("ACC-SENDER-001");
        dto.setReceiverAccount("ACC-RECEIVER-001");
        dto.setReceiverPhone(null);
        dto.setReceiverEmail(null);

        dto.setTransferType("INSTANT");
        dto.setStatus("CREATED");
        dto.setChannel("INTERNAL");

        dto.setAmountCents(10_000L);
        dto.setFeeCents(100L);
        dto.setTaxCents(50L);
        dto.setTotalDebitCents(10_150L);
        dto.setAvailableBalanceCents(50_000L);
        dto.setDailyLimitCents(20_000L);
        dto.setMinAllowedAmountCents(100L);
        dto.setMaxAllowedAmountCents(100_000L);

        dto.setRequiresConfirmation(false);
        dto.setConfirmationCode(null);

        dto.setSaveTemplate(false);
        dto.setTemplateName(null);

        dto.setCreatedAt(LocalDateTime.of(2026, 5, 1, 10, 0));
        dto.setScheduledAt(null);
        dto.setExecutedAt(null);
        dto.setCancelledAt(null);

        dto.setEventType(null);
        dto.setEventKey(null);
        dto.setEventVersion(null);

        dto.setComment("base valid transfer");

        return dto;
    }

    private static MoneyTransferTestCase tc(
            String id,
            String description,
            Consumer<MoneyTransferDto> changes,
            String... expectedRuleIds
    ) {
        MoneyTransferDto dto = baseValidDto();
        changes.accept(dto);

        return new MoneyTransferTestCase(
                id,
                description,
                dto,
                expected(expectedRuleIds)
        );
    }

    private static List<ViolationView> expected(String... ruleIds) {
        return Arrays.stream(ruleIds)
                .map(ExperimentRules::violation)
                .sorted()
                .toList();
    }

    // Group A: valid DTOs

    private static MoneyTransferTestCase tc01() {
        return tc(
                "TC-01",
                "Базовый корректный перевод",
                dto -> {
                }
        );
    }

    private static MoneyTransferTestCase tc02() {
        return tc(
                "TC-02",
                "Сумма равна максимуму",
                dto -> {
                    dto.setAmountCents(100_000L);
                    dto.setMaxAllowedAmountCents(100_000L);
                    dto.setTotalDebitCents(100_150L);
                    dto.setAvailableBalanceCents(150_000L);
                    dto.setDailyLimitCents(120_000L);
                }
        );
    }

    private static MoneyTransferTestCase tc03() {
        return tc(
                "TC-03",
                "Сумма равна минимуму",
                dto -> {
                    dto.setAmountCents(100L);
                    dto.setFeeCents(1L);
                    dto.setTaxCents(1L);
                    dto.setTotalDebitCents(102L);
                    dto.setMinAllowedAmountCents(100L);
                }
        );
    }

    private static MoneyTransferTestCase tc04() {
        return tc(
                "TC-04",
                "Отложенный перевод с датой",
                dto -> {
                    dto.setTransferType("SCHEDULED");
                    dto.setScheduledAt(LocalDateTime.of(2026, 5, 2, 10, 0));
                }
        );
    }

    private static MoneyTransferTestCase tc05() {
        return tc(
                "TC-05",
                "Исполненный перевод с датой исполнения",
                dto -> {
                    dto.setStatus("EXECUTED");
                    dto.setExecutedAt(LocalDateTime.of(2026, 5, 1, 10, 5));
                }
        );
    }

    private static MoneyTransferTestCase tc06() {
        return tc(
                "TC-06",
                "Отмененный перевод с датой отмены",
                dto -> {
                    dto.setStatus("CANCELLED");
                    dto.setCancelledAt(LocalDateTime.of(2026, 5, 1, 10, 5));
                }
        );
    }

    private static MoneyTransferTestCase tc07() {
        return tc(
                "TC-07",
                "Перевод с подтверждением",
                dto -> {
                    dto.setRequiresConfirmation(true);
                    dto.setConfirmationCode("123456");
                }
        );
    }

    private static MoneyTransferTestCase tc08() {
        return tc(
                "TC-08",
                "Сохранение шаблона",
                dto -> {
                    dto.setSaveTemplate(true);
                    dto.setTemplateName("Rent payment");
                }
        );
    }

    private static MoneyTransferTestCase tc09() {
        return tc(
                "TC-09",
                "Kafka-событие с обязательными полями",
                dto -> {
                    dto.setChannel("KAFKA");
                    dto.setEventType("TRANSFER_CREATED");
                    dto.setEventKey("TR-001");
                }
        );
    }

    private static MoneyTransferTestCase tc10() {
        return tc(
                "TC-10",
                "API-запрос с requestId",
                dto -> {
                    dto.setChannel("API");
                    dto.setRequestId("REQ-API-001");
                }
        );
    }

    // Group B: DTOs with one violation

    private static MoneyTransferTestCase tc11() {
        return tc(
                "TC-11",
                "Нарушение CF-01 через равенство комиссии сумме",
                dto -> {
                    dto.setAmountCents(100L);
                    dto.setFeeCents(100L);
                    dto.setTaxCents(10L);
                    dto.setTotalDebitCents(110L);
                },
                "CF-01"
        );
    }

    private static MoneyTransferTestCase tc12() {
        return tc(
                "TC-12",
                "Нарушение CF-02 через равенство налога сумме",
                dto -> {
                    dto.setAmountCents(100L);
                    dto.setFeeCents(10L);
                    dto.setTaxCents(100L);
                    dto.setTotalDebitCents(210L);
                },
                "CF-02"
        );
    }

    private static MoneyTransferTestCase tc13() {
        return tc(
                "TC-13",
                "Сумма меньше минимальной",
                dto -> {
                    dto.setAmountCents(50L);
                    dto.setFeeCents(1L);
                    dto.setTaxCents(1L);
                    dto.setTotalDebitCents(52L);
                    dto.setMinAllowedAmountCents(100L);
                },
                "CF-03"
        );
    }

    private static MoneyTransferTestCase tc14() {
        return tc(
                "TC-14",
                "Сумма больше максимальной",
                dto -> {
                    dto.setAmountCents(200_000L);
                    dto.setTotalDebitCents(200_150L);
                    dto.setMaxAllowedAmountCents(100_000L);
                    dto.setAvailableBalanceCents(300_000L);
                    dto.setDailyLimitCents(300_000L);
                },
                "CF-04"
        );
    }

    private static MoneyTransferTestCase tc15() {
        return tc(
                "TC-15",
                "Сумма больше доступного баланса",
                dto -> {
                    dto.setAmountCents(10_000L);
                    dto.setTotalDebitCents(4_000L);
                    dto.setAvailableBalanceCents(5_000L);
                },
                "CF-05"
        );
    }

    private static MoneyTransferTestCase tc16() {
        return tc(
                "TC-16",
                "Итоговое списание больше доступного баланса",
                dto -> {
                    dto.setAmountCents(1_000L);
                    dto.setFeeCents(10L);
                    dto.setTaxCents(10L);
                    dto.setTotalDebitCents(60_000L);
                    dto.setAvailableBalanceCents(50_000L);
                    dto.setDailyLimitCents(70_000L);
                },
                "CF-06"
        );
    }

    private static MoneyTransferTestCase tc17() {
        return tc(
                "TC-17",
                "Итоговое списание больше дневного лимита",
                dto -> {
                    dto.setAmountCents(1_000L);
                    dto.setFeeCents(10L);
                    dto.setTaxCents(10L);
                    dto.setTotalDebitCents(25_000L);
                    dto.setDailyLimitCents(20_000L);
                },
                "CF-07"
        );
    }

    private static MoneyTransferTestCase tc18() {
        return tc(
                "TC-18",
                "Счет отправителя совпадает со счетом получателя",
                dto -> dto.setReceiverAccount("ACC-SENDER-001"),
                "CF-08"
        );
    }

    private static MoneyTransferTestCase tc19() {
        return tc(
                "TC-19",
                "Отложенный перевод без даты",
                dto -> {
                    dto.setTransferType("SCHEDULED");
                    dto.setScheduledAt(null);
                },
                "RI-01"
        );
    }

    private static MoneyTransferTestCase tc20() {
        return tc(
                "TC-20",
                "Исполненный перевод без даты исполнения",
                dto -> {
                    dto.setStatus("EXECUTED");
                    dto.setExecutedAt(null);
                },
                "RI-02"
        );
    }

    private static MoneyTransferTestCase tc21() {
        return tc(
                "TC-21",
                "Отмененный перевод без даты отмены",
                dto -> {
                    dto.setStatus("CANCELLED");
                    dto.setCancelledAt(null);
                },
                "RI-03"
        );
    }

    private static MoneyTransferTestCase tc22() {
        return tc(
                "TC-22",
                "Требуется подтверждение, но код отсутствует",
                dto -> {
                    dto.setRequiresConfirmation(true);
                    dto.setConfirmationCode(null);
                },
                "RI-04"
        );
    }

    private static MoneyTransferTestCase tc23() {
        return tc(
                "TC-23",
                "Сохранение шаблона без имени шаблона",
                dto -> {
                    dto.setSaveTemplate(true);
                    dto.setTemplateName(null);
                },
                "RI-05"
        );
    }

    private static MoneyTransferTestCase tc24() {
        return tc(
                "TC-24",
                "Kafka-событие без типа события",
                dto -> {
                    dto.setChannel("KAFKA");
                    dto.setEventType(null);
                    dto.setEventKey("TR-001");
                },
                "RI-06"
        );
    }

    private static MoneyTransferTestCase tc25() {
        return tc(
                "TC-25",
                "Kafka-событие без ключа события",
                dto -> {
                    dto.setChannel("KAFKA");
                    dto.setEventType("TRANSFER_CREATED");
                    dto.setEventKey(null);
                },
                "RI-07"
        );
    }

    private static MoneyTransferTestCase tc26() {
        return tc(
                "TC-26",
                "API-запрос без requestId",
                dto -> {
                    dto.setChannel("API");
                    dto.setRequestId(null);
                },
                "RI-08"
        );
    }

    private static MoneyTransferTestCase tc27() {
        return tc(
                "TC-27",
                "Комиссия больше суммы",
                dto -> {
                    dto.setAmountCents(100L);
                    dto.setFeeCents(101L);
                    dto.setTaxCents(1L);
                    dto.setTotalDebitCents(102L);
                },
                "CF-01"
        );
    }

    private static MoneyTransferTestCase tc28() {
        return tc(
                "TC-28",
                "Сумма на единицу больше максимума",
                dto -> {
                    dto.setAmountCents(100_001L);
                    dto.setTotalDebitCents(100_101L);
                    dto.setMaxAllowedAmountCents(100_000L);
                    dto.setAvailableBalanceCents(150_000L);
                    dto.setDailyLimitCents(150_000L);
                },
                "CF-04"
        );
    }

    private static MoneyTransferTestCase tc29() {
        return tc(
                "TC-29",
                "Пустой код подтверждения",
                dto -> {
                    dto.setRequiresConfirmation(true);
                    dto.setConfirmationCode(" ");
                },
                "RI-04"
        );
    }

    private static MoneyTransferTestCase tc30() {
        return tc(
                "TC-30",
                "Пустое имя шаблона",
                dto -> {
                    dto.setSaveTemplate(true);
                    dto.setTemplateName("");
                },
                "RI-05"
        );
    }

    // Group C: DTOs with multiple violations

    private static MoneyTransferTestCase tc31() {
        return tc(
                "TC-31",
                "Комиссия и налог равны сумме",
                dto -> {
                    dto.setAmountCents(100L);
                    dto.setFeeCents(100L);
                    dto.setTaxCents(100L);
                    dto.setTotalDebitCents(300L);
                },
                "CF-01",
                "CF-02"
        );
    }

    private static MoneyTransferTestCase tc32() {
        return tc(
                "TC-32",
                "Сумма и итоговое списание превышают баланс и лимит",
                dto -> {
                    dto.setAmountCents(10_000L);
                    dto.setTotalDebitCents(60_000L);
                    dto.setAvailableBalanceCents(5_000L);
                    dto.setDailyLimitCents(5_000L);
                },
                "CF-05",
                "CF-06",
                "CF-07"
        );
    }

    private static MoneyTransferTestCase tc33() {
        return tc(
                "TC-33",
                "Один и тот же счет и отсутствует requestId для API",
                dto -> {
                    dto.setReceiverAccount("ACC-SENDER-001");
                    dto.setChannel("API");
                    dto.setRequestId(null);
                },
                "CF-08",
                "RI-08"
        );
    }

    private static MoneyTransferTestCase tc34() {
        return tc(
                "TC-34",
                "Отложенный перевод без даты и без кода подтверждения",
                dto -> {
                    dto.setTransferType("SCHEDULED");
                    dto.setScheduledAt(null);
                    dto.setRequiresConfirmation(true);
                    dto.setConfirmationCode(null);
                },
                "RI-01",
                "RI-04"
        );
    }

    private static MoneyTransferTestCase tc35() {
        return tc(
                "TC-35",
                "Исполненный Kafka-перевод без даты и event-полей",
                dto -> {
                    dto.setStatus("EXECUTED");
                    dto.setExecutedAt(null);
                    dto.setChannel("KAFKA");
                    dto.setEventType(null);
                    dto.setEventKey(null);
                },
                "RI-02",
                "RI-06",
                "RI-07"
        );
    }

    private static MoneyTransferTestCase tc36() {
        return tc(
                "TC-36",
                "Отмененный перевод и шаблон без имени",
                dto -> {
                    dto.setStatus("CANCELLED");
                    dto.setCancelledAt(null);
                    dto.setSaveTemplate(true);
                    dto.setTemplateName(null);
                },
                "RI-03",
                "RI-05"
        );
    }

    private static MoneyTransferTestCase tc37() {
        return tc(
                "TC-37",
                "Несогласованные минимальная и максимальная границы",
                dto -> {
                    dto.setAmountCents(3_000L);
                    dto.setMinAllowedAmountCents(5_000L);
                    dto.setMaxAllowedAmountCents(1_000L);
                    dto.setFeeCents(100L);
                    dto.setTaxCents(50L);
                    dto.setTotalDebitCents(3_150L);
                },
                "CF-03",
                "CF-04"
        );
    }

    private static MoneyTransferTestCase tc38() {
        return tc(
                "TC-38",
                "Несколько ошибок сравнения и одинаковые счета",
                dto -> {
                    dto.setAmountCents(100L);
                    dto.setFeeCents(100L);
                    dto.setTaxCents(100L);
                    dto.setReceiverAccount("ACC-SENDER-001");
                    dto.setTotalDebitCents(300L);
                },
                "CF-01",
                "CF-02",
                "CF-08"
        );
    }

    private static MoneyTransferTestCase tc39() {
        return tc(
                "TC-39",
                "Kafka-событие без event-полей и шаблон без имени",
                dto -> {
                    dto.setChannel("KAFKA");
                    dto.setEventType(null);
                    dto.setEventKey(null);
                    dto.setSaveTemplate(true);
                    dto.setTemplateName(null);
                },
                "RI-05",
                "RI-06",
                "RI-07"
        );
    }

    private static MoneyTransferTestCase tc40() {
        return tc(
                "TC-40",
                "Отложенный исполненный перевод без даты и подтверждения",
                dto -> {
                    dto.setTransferType("SCHEDULED");
                    dto.setScheduledAt(null);
                    dto.setStatus("EXECUTED");
                    dto.setExecutedAt(null);
                    dto.setRequiresConfirmation(true);
                    dto.setConfirmationCode(null);
                },
                "RI-01",
                "RI-02",
                "RI-04"
        );
    }

    // Group D: boundary and corner cases

    private static MoneyTransferTestCase tc41() {
        return tc(
                "TC-41",
                "Сумма равна минимально допустимой",
                dto -> {
                    dto.setAmountCents(100L);
                    dto.setFeeCents(1L);
                    dto.setTaxCents(1L);
                    dto.setTotalDebitCents(102L);
                    dto.setMinAllowedAmountCents(100L);
                }
        );
    }

    private static MoneyTransferTestCase tc42() {
        return tc(
                "TC-42",
                "Сумма равна максимально допустимой",
                dto -> {
                    dto.setAmountCents(100_000L);
                    dto.setMaxAllowedAmountCents(100_000L);
                    dto.setTotalDebitCents(100_150L);
                    dto.setAvailableBalanceCents(150_000L);
                    dto.setDailyLimitCents(150_000L);
                }
        );
    }

    private static MoneyTransferTestCase tc43() {
        return tc(
                "TC-43",
                "Сумма равна доступному балансу",
                dto -> {
                    dto.setAmountCents(10_000L);
                    dto.setAvailableBalanceCents(10_000L);
                    dto.setTotalDebitCents(10_000L);
                    dto.setDailyLimitCents(20_000L);
                }
        );
    }

    private static MoneyTransferTestCase tc44() {
        return tc(
                "TC-44",
                "Итоговое списание равно доступному балансу",
                dto -> {
                    dto.setTotalDebitCents(50_000L);
                    dto.setAvailableBalanceCents(50_000L);
                    dto.setDailyLimitCents(60_000L);
                }
        );
    }

    private static MoneyTransferTestCase tc45() {
        return tc(
                "TC-45",
                "Итоговое списание равно дневному лимиту",
                dto -> {
                    dto.setTotalDebitCents(20_000L);
                    dto.setDailyLimitCents(20_000L);
                    dto.setAvailableBalanceCents(50_000L);
                }
        );
    }

    private static MoneyTransferTestCase tc46() {
        return tc(
                "TC-46",
                "Комиссия равна сумме перевода",
                dto -> {
                    dto.setAmountCents(100L);
                    dto.setFeeCents(100L);
                    dto.setTaxCents(1L);
                    dto.setTotalDebitCents(101L);
                },
                "CF-01"
        );
    }

    private static MoneyTransferTestCase tc47() {
        return tc(
                "TC-47",
                "Налог равен сумме перевода",
                dto -> {
                    dto.setAmountCents(100L);
                    dto.setFeeCents(1L);
                    dto.setTaxCents(100L);
                    dto.setTotalDebitCents(101L);
                },
                "CF-02"
        );
    }

    private static MoneyTransferTestCase tc48() {
        return tc(
                "TC-48",
                "Blank-код подтверждения считается отсутствующим",
                dto -> {
                    dto.setRequiresConfirmation(true);
                    dto.setConfirmationCode(" ");
                },
                "RI-04"
        );
    }

    private static MoneyTransferTestCase tc49() {
        return tc(
                "TC-49",
                "Отложенный перевод с отсутствующей датой",
                dto -> {
                    dto.setTransferType("SCHEDULED");
                    dto.setScheduledAt(null);
                },
                "RI-01"
        );
    }

    private static MoneyTransferTestCase tc50() {
        return tc(
                "TC-50",
                "Target пустой, но условие не выполнено",
                dto -> {
                    dto.setRequiresConfirmation(false);
                    dto.setConfirmationCode(" ");
                }
        );
    }
}