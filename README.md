
````markdown
# ValidatorGen Experiment

Экспериментальный проект для сравнения пяти подходов к реализации кросс-полевой валидации DTO.

Проект используется как отдельный стенд для магистерской работы. Его задача — проверить один и тот же набор правил валидации на одном и том же DTO и сравнить подходы по корректности, объему ручного кода, объему сгенерированного кода, количеству файлов, зависимостям и трудоемкости сопровождения.

## 1. Цель эксперимента

Эксперимент отвечает на вопрос:

> Насколько прототип снижает объем ручной реализации и упрощает создание кросс-полевой валидации DTO для двух базовых категорий правил: сравнения значений и условной обязательности?

В эксперименте рассматриваются две категории правил:

1. `COMPARE_FIELDS` — сравнение значений двух полей DTO.
2. `REQUIRED_IF` — условная обязательность одного поля в зависимости от значения другого поля.

Категории взаимного исключения, временного порядка и арифметической согласованности в текущий эксперимент не включены. Они рассматриваются как направления дальнейшего расширения прототипа и экспериментального набора.

## 2. Общая идея стенда

Во всех подходах используется один и тот же DTO:

```text
MoneyTransferDto
````

Один и тот же набор правил:

```text
8 правил COMPARE_FIELDS
8 правил REQUIRED_IF
16 правил всего
```

Один и тот же набор тестовых данных:

```text
50 тестовых экземпляров MoneyTransferDto
```

Каждый подход приводит результат валидации к единому формату:

```java
ViolationView(String ruleId, String field, String message);
```

Это позволяет сравнивать подходы по результату, а не по внутреннему формату конкретной технологии.

## 3. Сравниваемые подходы

В проекте реализованы пять подходов.

| Подход                             | Название адаптера    | Смысл                                                                   |
| ---------------------------------- | -------------------- | ----------------------------------------------------------------------- |
| Ручной Java-валидатор              | `manual-explicit`    | Разработчик вручную пишет обычный Java-класс с `if`-проверками          |
| Ручная Jakarta Validation          | `manual-jakarta`     | Разработчик вручную пишет class-level аннотацию и `ConstraintValidator` |
| Сгенерированный explicit validator | `generated-explicit` | Прототип генерирует отдельный Java-класс валидатора                     |
| Сгенерированная Jakarta Validation | `generated-jakarta`  | Прототип генерирует Jakarta Validation-артефакты                        |
| Rule engine                        | `rule-engine-drools` | Правила выносятся во внешний `.drl`-файл и исполняются Drools           |

Все пять подходов проверяются на одном наборе тестовых случаев.

## 4. Структура проекта

```text
validatorgen-experiment/
  pom.xml
  README.md

  src/main/java/experiment/dto/
    MoneyTransferDto.java

  src/main/java/experiment/common/
    ViolationView.java
    ValidationAdapter.java
    ValidationRunResult.java
    ValidationApproach.java

  src/main/java/experiment/rules/
    RuleKind.java
    ExperimentRule.java
    ExperimentRules.java

  src/main/java/experiment/manual/explicit/
    ManualMoneyTransferValidator.java
    ManualExplicitValidationAdapter.java

  src/main/java/experiment/manual/jakarta/
    ManualJakartaGroup.java
    ManualMoneyTransferValid.java
    ManualMoneyTransferConstraintValidator.java
    ManualJakartaValidationAdapter.java
    MoneyTransferDtoJakarta.java

  src/main/java/experiment/dto/generated/
    MoneyTransferDtoGeneratedValidator.java
    MoneyTransferDtoGeneratedValidation.java
    MoneyTransferDtoGeneratedConstraintValidator.java

  src/main/java/experiment/generated/explicit/
    GeneratedExplicitValidationAdapter.java

  src/main/java/experiment/generated/jakarta/
    GeneratedJakartaGroup.java
    MoneyTransferDtoGeneratedJakarta.java
    GeneratedJakartaValidationAdapter.java

  src/main/java/experiment/ruleengine/
    RuleEngineViolation.java
    RuleEngineValidationAdapter.java

  src/main/resources/rules/
    money-transfer-validation.drl

  src/test/java/experiment/
    SmokeTest.java

  src/test/java/experiment/rules/
    ExperimentRulesTest.java

  src/test/java/experiment/dataset/
    MoneyTransferTestCase.java
    MoneyTransferTestCases.java
    MoneyTransferTestCasesTest.java

  src/test/java/experiment/manual/explicit/
    ManualMoneyTransferValidatorTest.java

  src/test/java/experiment/manual/jakarta/
    ManualJakartaValidationAdapterTest.java

  src/test/java/experiment/generated/explicit/
    GeneratedExplicitValidationAdapterTest.java

  src/test/java/experiment/generated/jakarta/
    GeneratedJakartaValidationAdapterTest.java

  src/test/java/experiment/ruleengine/
    RuleEngineValidationAdapterTest.java

  src/test/java/experiment/correctness/
    CorrectnessComparisonTest.java
```

## 5. Основные сущности проекта

### `MoneyTransferDto`

Файл:

```text
src/main/java/experiment/dto/MoneyTransferDto.java
```

Это общий DTO для эксперимента. Он моделирует объект передачи данных денежного перевода.

DTO содержит:

* идентификаторы;
* данные отправителя и получателя;
* тип, статус и канал перевода;
* денежные поля в `Long ...Cents`;
* флаги подтверждения и сохранения шаблона;
* даты;
* интеграционные поля;
* комментарий.

Этот класс является общим входом для всех подходов, кроме Jakarta-реализаций, где дополнительно используются аннотированные DTO-классы или обертки.

### `ViolationView`

Файл:

```text
src/main/java/experiment/common/ViolationView.java
```

Единый формат результата валидации:

```java
public record ViolationView(
    String ruleId,
    String field,
    String message
) {}
```

Он нужен для сравнения результатов разных подходов.

### `ValidationAdapter`

Файл:

```text
src/main/java/experiment/common/ValidationAdapter.java
```

Общий интерфейс для всех подходов:

```java
public interface ValidationAdapter {
    String name();
    List<ViolationView> validate(MoneyTransferDto dto);
}
```

Каждый подход реализует этот интерфейс и возвращает результат в едином формате.

### `ExperimentRules`

Файл:

```text
src/main/java/experiment/rules/ExperimentRules.java
```

Единый каталог правил эксперимента. Он хранит:

* `ruleId`;
* тип правила;
* человекочитаемое выражение;
* целевое поле;
* диагностическое сообщение.

Этот класс является единым источником `ruleId`, `targetField` и `message` для тестовых данных и ручных реализаций.

## 6. Набор правил

В эксперименте используется 16 правил.

### 6.1 COMPARE_FIELDS

| ID      | Правило                                    | Target field      |
| ------- | ------------------------------------------ | ----------------- |
| `CF-01` | `amountCents > feeCents`                   | `feeCents`        |
| `CF-02` | `amountCents > taxCents`                   | `taxCents`        |
| `CF-03` | `amountCents >= minAllowedAmountCents`     | `amountCents`     |
| `CF-04` | `amountCents <= maxAllowedAmountCents`     | `amountCents`     |
| `CF-05` | `amountCents <= availableBalanceCents`     | `amountCents`     |
| `CF-06` | `totalDebitCents <= availableBalanceCents` | `totalDebitCents` |
| `CF-07` | `totalDebitCents <= dailyLimitCents`       | `totalDebitCents` |
| `CF-08` | `senderAccount != receiverAccount`         | `receiverAccount` |

### 6.2 REQUIRED_IF

| ID      | Условие                                                     | Target field       |
| ------- | ----------------------------------------------------------- | ------------------ |
| `RI-01` | `scheduledAt required if transferType == SCHEDULED`         | `scheduledAt`      |
| `RI-02` | `executedAt required if status == EXECUTED`                 | `executedAt`       |
| `RI-03` | `cancelledAt required if status == CANCELLED`               | `cancelledAt`      |
| `RI-04` | `confirmationCode required if requiresConfirmation == true` | `confirmationCode` |
| `RI-05` | `templateName required if saveTemplate == true`             | `templateName`     |
| `RI-06` | `eventType required if channel == KAFKA`                    | `eventType`        |
| `RI-07` | `eventKey required if channel == KAFKA`                     | `eventKey`         |
| `RI-08` | `requestId required if channel == API`                      | `requestId`        |

## 7. Тестовые данные

Тестовые данные находятся в пакете:

```text
src/test/java/experiment/dataset/
```

Основной файл:

```text
MoneyTransferTestCases.java
```

Он содержит 50 тестовых экземпляров DTO.

Тестовые случаи разделены на четыре группы:

| Группа                        | Количество | Назначение                                                 |
| ----------------------------- | ---------: | ---------------------------------------------------------- |
| Корректные DTO                |         10 | Проверка отсутствия ложных срабатываний                    |
| DTO с одним нарушением        |         20 | Проверка каждого правила отдельно                          |
| DTO с несколькими нарушениями |         10 | Проверка накопления нескольких ошибок                      |
| Граничные и corner-case DTO   |         10 | Проверка поведения на граничных значениях и пустых строках |

Каждый тестовый случай содержит:

* `id`;
* описание;
* DTO;
* ожидаемый список `ViolationView`.

Ожидаемые нарушения используются как oracle для всех подходов.

## 8. Ручной Java-валидатор

Файлы:

```text
src/main/java/experiment/manual/explicit/
  ManualMoneyTransferValidator.java
  ManualExplicitValidationAdapter.java
```

### Что это

Это рукописный Java baseline. В нем разработчик вручную реализует все 16 правил через обычные `if`-проверки.

### Что учитывать в LOC

При подсчете ручного Java LOC для подхода `manual-explicit` учитывать:

```text
src/main/java/experiment/manual/explicit/ManualMoneyTransferValidator.java
```

Допустимо также отдельно учитывать адаптер:

```text
src/main/java/experiment/manual/explicit/ManualExplicitValidationAdapter.java
```

Но в основной метрике трудоемкости реализации правил лучше выделять именно `ManualMoneyTransferValidator.java`, потому что адаптер является вспомогательной частью экспериментального стенда.

### Что не учитывать как реализацию правил

Не учитывать в LOC ручной реализации:

* DTO;
* dataset;
* tests;
* `ViolationView`;
* `ValidationAdapter`;
* `ExperimentRules`;
* correctness-тесты.

Они являются общей инфраструктурой эксперимента.

## 9. Ручная Jakarta Validation

Файлы:

```text
src/main/java/experiment/manual/jakarta/
  ManualJakartaGroup.java
  ManualMoneyTransferValid.java
  ManualMoneyTransferConstraintValidator.java
  ManualJakartaValidationAdapter.java
  MoneyTransferDtoJakarta.java
```

### Что это

Это baseline на стандартном подходе Jakarta Validation. Разработчик вручную создает:

* class-level аннотацию;
* группу валидации;
* `ConstraintValidator`;
* аннотированную DTO-копию или DTO-обертку;
* адаптер для приведения `ConstraintViolation` к `ViolationView`.

### Что учитывать в LOC

При подсчете LOC для ручной Jakarta Validation учитывать:

```text
src/main/java/experiment/manual/jakarta/ManualMoneyTransferValid.java
src/main/java/experiment/manual/jakarta/ManualMoneyTransferConstraintValidator.java
src/main/java/experiment/manual/jakarta/MoneyTransferDtoJakarta.java
```

Дополнительно можно отдельно учитывать:

```text
src/main/java/experiment/manual/jakarta/ManualJakartaGroup.java
src/main/java/experiment/manual/jakarta/ManualJakartaValidationAdapter.java
```

Но в таблице результатов лучше разделить:

* LOC реализации правил;
* LOC интеграционной обвязки.

### Почему используется отдельный DTO

Jakarta Validation запускает class-level constraint только тогда, когда соответствующая аннотация присутствует на проверяемом классе. Чтобы не изменять общий `MoneyTransferDto`, в эксперименте используется отдельная аннотированная версия DTO для ручного Jakarta-подхода.

Это решение нужно учитывать при сравнении: ручной Jakarta-подход требует дополнительной точки интеграции в виде аннотации на DTO.

## 10. Сгенерированный explicit validator

Файлы:

```text
src/main/java/experiment/dto/generated/
  MoneyTransferDtoGeneratedValidator.java

src/main/java/experiment/generated/explicit/
  GeneratedExplicitValidationAdapter.java
```

### Что это

`MoneyTransferDtoGeneratedValidator.java` — это сгенерированный артефакт прототипом.

Он имеет вид:

```java
public final class MoneyTransferDtoGeneratedValidator {
  public static List<Violation> validate(MoneyTransferDto dto) { ... }
  public record Violation(String path, String message, String ruleId) {}
}
```

Каждое правило оформлено как сгенерированный `if`-блок с комментарием:

```java
// Rule GT_amountCents_feeCents_1: amountCents > feeCents
```

### Что учитывать в LOC

Для метрики generated LOC учитывать:

```text
src/main/java/experiment/dto/generated/MoneyTransferDtoGeneratedValidator.java
```

Для метрики ручного LOC прототипа этот файл не учитывать, потому что он не пишется разработчиком вручную.

Адаптер:

```text
src/main/java/experiment/generated/explicit/GeneratedExplicitValidationAdapter.java
```

является экспериментальной обвязкой. Его можно считать отдельно как infrastructure LOC, но не как generated LOC и не как ручную реализацию правил.

### Важное правило

Сгенерированный валидатор не должен редактироваться вручную после генерации. Если generated-код не компилируется или возвращает неверный результат, это фиксируется как результат эксперимента.

## 11. Сгенерированная Jakarta Validation

Файлы:

```text
src/main/java/experiment/dto/generated/
  MoneyTransferDtoGeneratedValidation.java
  MoneyTransferDtoGeneratedConstraintValidator.java

src/main/java/experiment/generated/jakarta/
  GeneratedJakartaGroup.java
  MoneyTransferDtoGeneratedJakarta.java
  GeneratedJakartaValidationAdapter.java
```

### Что это

`MoneyTransferDtoGeneratedValidation.java` и `MoneyTransferDtoGeneratedConstraintValidator.java` — это сгенерированные прототипом Jakarta Validation-артефакты.


* находятся в package `experiment.dto.generated`;
* используют generated ruleId вида `GT_amountCents_feeCents_1`;
* генерируют прямолинейный Java-код проверок;
* добавляют нарушения через `ConstraintValidatorContext`.

### Что учитывать в generated LOC

Для generated Jakarta LOC учитывать:

```text
src/main/java/experiment/dto/generated/MoneyTransferDtoGeneratedValidation.java
src/main/java/experiment/dto/generated/MoneyTransferDtoGeneratedConstraintValidator.java
```

### Что учитывать как интеграционную обвязку

Файлы:

```text
src/main/java/experiment/generated/jakarta/GeneratedJakartaGroup.java
src/main/java/experiment/generated/jakarta/MoneyTransferDtoGeneratedJakarta.java
src/main/java/experiment/generated/jakarta/GeneratedJakartaValidationAdapter.java
```

являются инфраструктурой экспериментального стенда.

Они нужны для запуска generated Jakarta Validation в тестовом проекте и для преобразования результата в `ViolationView`. Их нужно учитывать отдельно как integration/infrastructure LOC, но не как generated LOC.

### Почему нужна `MoneyTransferDtoGeneratedJakarta`

Jakarta Validation требует, чтобы class-level constraint был привязан к проверяемому классу. Чтобы не изменять базовый `MoneyTransferDto`, в эксперименте используется отдельная интеграционная обертка:

```java
@MoneyTransferDtoGeneratedValidation(groups = GeneratedJakartaGroup.class)
public class MoneyTransferDtoGeneratedJakarta extends MoneyTransferDto {
}
```

Эта обертка не является логикой правил. Она отражает интеграционный шаг, необходимый для запуска Jakarta Validation без изменения исходного DTO.

## 12. Rule engine baseline

Файлы:

```text
src/main/java/experiment/ruleengine/
  RuleEngineViolation.java
  RuleEngineValidationAdapter.java

src/main/resources/rules/
  money-transfer-validation.drl
```

### Что это

Это baseline на основе Drools. Все 16 правил вынесены в `.drl`-файл.

### Что учитывать в LOC

Для rule engine LOC учитывать:

```text
src/main/resources/rules/money-transfer-validation.drl
```

Также отдельно можно учитывать Java-интеграцию движка:

```text
src/main/java/experiment/ruleengine/RuleEngineValidationAdapter.java
src/main/java/experiment/ruleengine/RuleEngineViolation.java
```

В итоговой таблице лучше разделить:

* LOC правила/DSL;
* LOC интеграции rule engine.

### Что не учитывать

Не учитывать rule engine Java-адаптер как “код правил”, потому что сами правила находятся в `.drl`.

Однако адаптер важно учитывать в метриках интеграционной сложности, потому что без него Drools не запускается внутри Java-приложения.

## 13. Что является общей инфраструктурой эксперимента

Следующие файлы являются общей инфраструктурой и не должны относиться к одному из пяти подходов как реализация правил:

```text
src/main/java/experiment/dto/MoneyTransferDto.java

src/main/java/experiment/common/ViolationView.java
src/main/java/experiment/common/ValidationAdapter.java
src/main/java/experiment/common/ValidationRunResult.java
src/main/java/experiment/common/ValidationApproach.java

src/main/java/experiment/rules/RuleKind.java
src/main/java/experiment/rules/ExperimentRule.java
src/main/java/experiment/rules/ExperimentRules.java

src/test/java/experiment/dataset/MoneyTransferTestCase.java
src/test/java/experiment/dataset/MoneyTransferTestCases.java
src/test/java/experiment/dataset/MoneyTransferTestCasesTest.java

src/test/java/experiment/correctness/CorrectnessComparisonTest.java
src/test/java/experiment/SmokeTest.java
```

Эти файлы нужны для проведения эксперимента, но не являются частью сравниваемых реализаций валидации.

## 14. Какие файлы учитывать при подсчете LOC

### 14.1 LOC реализации правил

| Подход                       | Учитываемые файлы                                                                                              |
| ---------------------------- | -------------------------------------------------------------------------------------------------------------- |
| Ручной Java-валидатор        | `src/main/java/experiment/manual/explicit/ManualMoneyTransferValidator.java`                                   |
| Ручная Jakarta Validation    | `ManualMoneyTransferValid.java`, `ManualMoneyTransferConstraintValidator.java`, `MoneyTransferDtoJakarta.java` |
| Generated explicit validator | `src/main/java/experiment/dto/generated/MoneyTransferDtoGeneratedValidator.java`                               |
| Generated Jakarta Validation | `MoneyTransferDtoGeneratedValidation.java`, `MoneyTransferDtoGeneratedConstraintValidator.java`                |
| Rule engine                  | `src/main/resources/rules/money-transfer-validation.drl`                                                       |

### 14.2 LOC интеграционной обвязки

| Подход                       | Учитываемые файлы                                                                                               |
| ---------------------------- | --------------------------------------------------------------------------------------------------------------- |
| Ручной Java-валидатор        | `ManualExplicitValidationAdapter.java`                                                                          |
| Ручная Jakarta Validation    | `ManualJakartaGroup.java`, `ManualJakartaValidationAdapter.java`                                                |
| Generated explicit validator | `GeneratedExplicitValidationAdapter.java`                                                                       |
| Generated Jakarta Validation | `GeneratedJakartaGroup.java`, `MoneyTransferDtoGeneratedJakarta.java`, `GeneratedJakartaValidationAdapter.java` |
| Rule engine                  | `RuleEngineViolation.java`, `RuleEngineValidationAdapter.java`                                                  |

### 14.3 LOC общей инфраструктуры

Считать отдельно, но не включать в сравнение подходов:

```text
DTO
common classes
rules catalog
dataset
tests
README
pom.xml
```

## 15. Результаты корректности на текущем этапе

На текущем этапе реализованы и проходят correctness-проверку пять подходов:

```text
manual-explicit
manual-jakarta
generated-explicit
generated-jakarta
rule-engine-drools
```

Для каждого подхода выполняется проверка на 50 тестовых экземплярах DTO.

Основной тест:

```text
src/test/java/experiment/correctness/CorrectnessComparisonTest.java
```

Проверка считается успешной, если каждый подход возвращает точно тот же набор `ViolationView`, который задан в `MoneyTransferTestCases`.

## 16. Команды запуска

Запуск всех тестов:

```bash
mvn test
```

Запуск общего correctness-теста:

```bash
mvn test -Dtest=CorrectnessComparisonTest
```

Запуск ручного Java baseline:

```bash
mvn test -Dtest=ManualMoneyTransferValidatorTest
```

Запуск ручного Jakarta baseline:

```bash
mvn test -Dtest=ManualJakartaValidationAdapterTest
```

Запуск generated explicit baseline:

```bash
mvn test -Dtest=GeneratedExplicitValidationAdapterTest
```

Запуск generated Jakarta baseline:

```bash
mvn test -Dtest=GeneratedJakartaValidationAdapterTest
```

Запуск Drools baseline:

```bash
mvn test -Dtest=RuleEngineValidationAdapterTest
```

## 17. Правила интерпретации LOC

При интерпретации LOC важно разделять:

1. Код, который разработчик пишет вручную.
2. Код, который создается генератором.
3. Декларативную спецификацию правил.
4. Интеграционную обвязку.
5. Общую инфраструктуру эксперимента.

Сгенерированный код может быть длиннее ручного, но это не является недостатком само по себе. В эксперименте основное внимание уделяется объему кода, который разработчик должен написать и сопровождать вручную.


## 18. Текущие ограничения стенда

1. Generated explicit и generated Jakarta artifacts в текущем проекте являются имитацией ожидаемого вывода прототипа, выполненной по фактическому стилю generated explicit validator.
2. После фактической генерации артефактов прототипом эти файлы должны быть заменены реальным выводом.
3. Если фактический вывод прототипа будет отличаться, адаптеры нужно будет скорректировать.
4. Rule engine baseline использует Drools и требует дополнительных runtime-зависимостей.
5. Jakarta-подходы требуют отдельной аннотированной версии DTO или интеграционной обертки.
6. Dataset является синтетическим и предназначен для оценки кросс-полевых правил, а не для моделирования полного промышленного банковского процесса.


```
