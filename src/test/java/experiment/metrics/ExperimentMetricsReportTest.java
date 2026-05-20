package experiment.metrics;

import experiment.common.ValidationAdapter;
import experiment.common.ViolationView;
import experiment.dataset.MoneyTransferTestCase;
import experiment.dataset.MoneyTransferTestCases;
import experiment.generated.explicit.GeneratedExplicitValidationAdapter;
import experiment.generated.jakarta.GeneratedJakartaValidationAdapter;
import experiment.manual.explicit.ManualExplicitValidationAdapter;
import experiment.manual.jakarta.ManualJakartaValidationAdapter;
import experiment.ruleengine.RuleEngineValidationAdapter;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ExperimentMetricsReportTest {

    private static final Path PROJECT_ROOT = Path.of("").toAbsolutePath();
    private static final Path RESULTS_DIR = PROJECT_ROOT.resolve("target/experiment-results");

    private static final int WARMUP_ROUNDS = 5;
    private static final int DATASET_SIZE = MoneyTransferTestCases.all().size();
    private static final int LARGE_RUN_REPEATS = 200; // 50 * 200 = 10 000 DTO validations

    private final List<ValidationAdapter> adapters = List.of(
            new ManualExplicitValidationAdapter(),
            new ManualJakartaValidationAdapter(),
            new GeneratedExplicitValidationAdapter(),
            new GeneratedJakartaValidationAdapter(),
            new RuleEngineValidationAdapter()
    );

    @Test
    void shouldGenerateExperimentMetricsReport() throws IOException {
        Files.createDirectories(RESULTS_DIR);

        List<CorrectnessMetric> correctnessMetrics = collectCorrectnessMetrics();
        List<LocMetric> locMetrics = collectLocMetrics();
        List<RuntimeMetric> runtimeMetrics = collectRuntimeMetrics();
        List<DependencyMetric> dependencyMetrics = collectDependencyMetrics();

        writeCorrectnessCsv(correctnessMetrics);
        writeLocCsv(locMetrics);
        writeRuntimeCsv(runtimeMetrics);
        writeDependenciesCsv(dependencyMetrics);
        writeMarkdownReport(correctnessMetrics, locMetrics, runtimeMetrics, dependencyMetrics);

        assertAllApproachesAreCorrect(correctnessMetrics);
    }

    private List<CorrectnessMetric> collectCorrectnessMetrics() {
        List<CorrectnessMetric> rows = new ArrayList<>();

        for (ValidationAdapter adapter : adapters) {
            int matchedCases = 0;
            int falsePositive = 0;
            int falseNegative = 0;

            for (MoneyTransferTestCase testCase : MoneyTransferTestCases.all()) {
                Set<ViolationView> expected = new TreeSet<>(testCase.expectedViolations());
                Set<ViolationView> actual = new TreeSet<>(adapter.validate(testCase.dto()));

                if (actual.equals(expected)) {
                    matchedCases++;
                }

                Set<ViolationView> unexpected = new TreeSet<>(actual);
                unexpected.removeAll(expected);
                falsePositive += unexpected.size();

                Set<ViolationView> missed = new TreeSet<>(expected);
                missed.removeAll(actual);
                falseNegative += missed.size();
            }

            rows.add(new CorrectnessMetric(
                    adapter.name(),
                    DATASET_SIZE,
                    matchedCases,
                    falsePositive,
                    falseNegative,
                    matchedCases == DATASET_SIZE && falsePositive == 0 && falseNegative == 0
            ));
        }

        return rows;
    }

    private List<LocMetric> collectLocMetrics() throws IOException {
        List<FileGroup> groups = List.of(
                new FileGroup(
                        "manual-explicit",
                        "rules-implementation",
                        List.of("src/main/java/experiment/manual/explicit/ManualMoneyTransferValidator.java")
                ),
                new FileGroup(
                        "manual-explicit",
                        "integration-wrapper",
                        List.of("src/main/java/experiment/manual/explicit/ManualExplicitValidationAdapter.java")
                ),

                new FileGroup(
                        "manual-jakarta",
                        "rules-implementation",
                        List.of(
                                "src/main/java/experiment/manual/jakarta/ManualMoneyTransferValid.java",
                                "src/main/java/experiment/manual/jakarta/ManualMoneyTransferConstraintValidator.java",
                                "src/main/java/experiment/manual/jakarta/MoneyTransferDtoJakarta.java"
                        )
                ),
                new FileGroup(
                        "manual-jakarta",
                        "integration-wrapper",
                        List.of(
                                "src/main/java/experiment/manual/jakarta/ManualJakartaGroup.java",
                                "src/main/java/experiment/manual/jakarta/ManualJakartaValidationAdapter.java"
                        )
                ),

                new FileGroup(
                        "generated-explicit",
                        "generated-artifact",
                        List.of("src/main/java/experiment/dto/generated/MoneyTransferDtoGeneratedValidator.java")
                ),
                new FileGroup(
                        "generated-explicit",
                        "integration-wrapper",
                        List.of("src/main/java/experiment/generated/explicit/GeneratedExplicitValidationAdapter.java")
                ),

                new FileGroup(
                        "generated-jakarta",
                        "generated-artifact",
                        List.of(
                                "src/main/java/experiment/dto/generated/MoneyTransferDtoGeneratedValidation.java",
                                "src/main/java/experiment/dto/generated/MoneyTransferDtoGeneratedConstraintValidator.java"
                        )
                ),
                new FileGroup(
                        "generated-jakarta",
                        "integration-wrapper",
                        List.of(
                                "src/main/java/experiment/generated/jakarta/GeneratedJakartaGroup.java",
                                "src/main/java/experiment/generated/jakarta/MoneyTransferDtoGeneratedJakarta.java",
                                "src/main/java/experiment/generated/jakarta/GeneratedJakartaValidationAdapter.java"
                        )
                ),

                new FileGroup(
                        "rule-engine-drools",
                        "rules-implementation",
                        List.of("src/main/resources/rules/money-transfer-validation.drl")
                ),
                new FileGroup(
                        "rule-engine-drools",
                        "integration-wrapper",
                        List.of(
                                "src/main/java/experiment/ruleengine/RuleEngineViolation.java",
                                "src/main/java/experiment/ruleengine/RuleEngineValidationAdapter.java"
                        )
                ),

                new FileGroup(
                        "experiment-common",
                        "shared-infrastructure",
                        List.of(
                                "src/main/java/experiment/dto/MoneyTransferDto.java",
                                "src/main/java/experiment/common/ViolationView.java",
                                "src/main/java/experiment/common/ValidationAdapter.java",
                                "src/main/java/experiment/common/ValidationRunResult.java",
                                "src/main/java/experiment/common/ValidationApproach.java",
                                "src/main/java/experiment/rules/RuleKind.java",
                                "src/main/java/experiment/rules/ExperimentRule.java",
                                "src/main/java/experiment/rules/ExperimentRules.java"
                        )
                ),
                new FileGroup(
                        "experiment-tests",
                        "shared-test-infrastructure",
                        List.of(
                                "src/test/java/experiment/dataset/MoneyTransferTestCase.java",
                                "src/test/java/experiment/dataset/MoneyTransferTestCases.java",
                                "src/test/java/experiment/correctness/CorrectnessComparisonTest.java",
                                "src/test/java/experiment/metrics/ExperimentMetricsReportTest.java"
                        )
                )
        );

        List<LocMetric> metrics = new ArrayList<>();

        for (FileGroup group : groups) {
            int fileCount = 0;
            int loc = 0;
            List<String> missingFiles = new ArrayList<>();

            for (String relativePath : group.relativePaths()) {
                Path path = PROJECT_ROOT.resolve(relativePath);

                if (!Files.exists(path)) {
                    missingFiles.add(relativePath);
                    continue;
                }

                fileCount++;
                loc += countLogicalLoc(path);
            }

            metrics.add(new LocMetric(
                    group.approach(),
                    group.category(),
                    fileCount,
                    loc,
                    missingFiles
            ));
        }

        return metrics;
    }

    private int countLogicalLoc(Path path) throws IOException {
        int loc = 0;
        boolean inBlockComment = false;

        for (String line : Files.readAllLines(path, StandardCharsets.UTF_8)) {
            String trimmed = line.trim();

            if (trimmed.isEmpty()) {
                continue;
            }

            if (inBlockComment) {
                if (trimmed.contains("*/")) {
                    inBlockComment = false;
                }
                continue;
            }

            if (trimmed.startsWith("/*")) {
                if (!trimmed.contains("*/")) {
                    inBlockComment = true;
                }
                continue;
            }

            if (trimmed.startsWith("*") || trimmed.startsWith("//")) {
                continue;
            }

            loc++;
        }

        return loc;
    }

    private List<RuntimeMetric> collectRuntimeMetrics() {
        List<RuntimeMetric> rows = new ArrayList<>();

        for (ValidationAdapter adapter : adapters) {
            warmUp(adapter);

            long smallRunNanos = measureRuntime(adapter, 1);
            long largeRunNanos = measureRuntime(adapter, LARGE_RUN_REPEATS);

            long smallDtoCount = DATASET_SIZE;
            long largeDtoCount = (long) DATASET_SIZE * LARGE_RUN_REPEATS;

            rows.add(new RuntimeMetric(
                    adapter.name(),
                    smallDtoCount,
                    smallRunNanos,
                    nanosToMillis(smallRunNanos),
                    averageMicrosPerDto(smallRunNanos, smallDtoCount)
            ));

            rows.add(new RuntimeMetric(
                    adapter.name(),
                    largeDtoCount,
                    largeRunNanos,
                    nanosToMillis(largeRunNanos),
                    averageMicrosPerDto(largeRunNanos, largeDtoCount)
            ));
        }

        return rows;
    }

    private void warmUp(ValidationAdapter adapter) {
        for (int i = 0; i < WARMUP_ROUNDS; i++) {
            runAdapterOnDataset(adapter, 1);
        }
    }

    private long measureRuntime(ValidationAdapter adapter, int repeats) {
        long start = System.nanoTime();
        runAdapterOnDataset(adapter, repeats);
        return System.nanoTime() - start;
    }

    private void runAdapterOnDataset(ValidationAdapter adapter, int repeats) {
        for (int i = 0; i < repeats; i++) {
            for (MoneyTransferTestCase testCase : MoneyTransferTestCases.all()) {
                adapter.validate(testCase.dto());
            }
        }
    }

    private double nanosToMillis(long nanos) {
        return nanos / 1_000_000.0;
    }

    private double averageMicrosPerDto(long nanos, long dtoCount) {
        return nanos / 1_000.0 / dtoCount;
    }

    private List<DependencyMetric> collectDependencyMetrics() {
        return List.of(
                new DependencyMetric(
                        "manual-explicit",
                        "JDK only",
                        "Нет внешних runtime-зависимостей для самой логики валидации"
                ),
                new DependencyMetric(
                        "manual-jakarta",
                        "jakarta.validation-api; hibernate-validator; expressly",
                        "Требует Jakarta Validation API, реализации валидатора и EL-реализации"
                ),
                new DependencyMetric(
                        "generated-explicit",
                        "JDK only",
                        "Сгенерированный explicit validator не требует внешнего runtime-фреймворка"
                ),
                new DependencyMetric(
                        "generated-jakarta",
                        "jakarta.validation-api; hibernate-validator; expressly",
                        "Сгенерированные Jakarta-артефакты требуют Jakarta Validation runtime"
                ),
                new DependencyMetric(
                        "rule-engine-drools",
                        "kie-api; kie-ci; drools-core; drools-compiler; drools-mvel",
                        "Требует подключения rule engine и его инфраструктуры выполнения"
                )
        );
    }

    private void assertAllApproachesAreCorrect(List<CorrectnessMetric> rows) {
        for (CorrectnessMetric row : rows) {
            assertEquals(
                    true,
                    row.passed(),
                    "Correctness failed for " + row.approach()
                            + ": matchedCases=" + row.matchedCases()
                            + ", falsePositive=" + row.falsePositive()
                            + ", falseNegative=" + row.falseNegative()
            );
        }
    }

    private void writeCorrectnessCsv(List<CorrectnessMetric> rows) throws IOException {
        List<String> lines = new ArrayList<>();
        lines.add("approach,totalCases,matchedCases,falsePositive,falseNegative,passed");

        for (CorrectnessMetric row : rows) {
            lines.add(String.join(",",
                    row.approach(),
                    String.valueOf(row.totalCases()),
                    String.valueOf(row.matchedCases()),
                    String.valueOf(row.falsePositive()),
                    String.valueOf(row.falseNegative()),
                    String.valueOf(row.passed())
            ));
        }

        writeFile("correctness.csv", lines);
    }

    private void writeLocCsv(List<LocMetric> rows) throws IOException {
        List<String> lines = new ArrayList<>();
        lines.add("approach,category,fileCount,logicalLoc,missingFiles");

        for (LocMetric row : rows) {
            lines.add(String.join(",",
                    row.approach(),
                    row.category(),
                    String.valueOf(row.fileCount()),
                    String.valueOf(row.logicalLoc()),
                    quote(String.join(";", row.missingFiles()))
            ));
        }

        writeFile("loc.csv", lines);
    }

    private void writeRuntimeCsv(List<RuntimeMetric> rows) throws IOException {
        List<String> lines = new ArrayList<>();
        lines.add("approach,dtoCount,totalNanos,totalMillis,avgMicrosPerDto");

        for (RuntimeMetric row : rows) {
            lines.add(String.join(",",
                    row.approach(),
                    String.valueOf(row.dtoCount()),
                    String.valueOf(row.totalNanos()),
                    format(row.totalMillis()),
                    format(row.avgMicrosPerDto())
            ));
        }

        writeFile("runtime.csv", lines);
    }

    private void writeDependenciesCsv(List<DependencyMetric> rows) throws IOException {
        List<String> lines = new ArrayList<>();
        lines.add("approach,runtimeDependencies,comment");

        for (DependencyMetric row : rows) {
            lines.add(String.join(",",
                    row.approach(),
                    quote(row.runtimeDependencies()),
                    quote(row.comment())
            ));
        }

        writeFile("dependencies.csv", lines);
    }

    private void writeMarkdownReport(
            List<CorrectnessMetric> correctnessMetrics,
            List<LocMetric> locMetrics,
            List<RuntimeMetric> runtimeMetrics,
            List<DependencyMetric> dependencyMetrics
    ) throws IOException {
        StringBuilder md = new StringBuilder();

        md.append("# Experiment Metrics Report\n\n");
        md.append("Generated by `ExperimentMetricsReportTest`.\n\n");

        md.append("## Correctness\n\n");
        md.append("| Approach | DTO cases | Matched cases | False positive | False negative | Passed |\n");
        md.append("|---|---:|---:|---:|---:|---|\n");
        for (CorrectnessMetric row : correctnessMetrics) {
            md.append("| ")
                    .append(row.approach()).append(" | ")
                    .append(row.totalCases()).append(" | ")
                    .append(row.matchedCases()).append(" | ")
                    .append(row.falsePositive()).append(" | ")
                    .append(row.falseNegative()).append(" | ")
                    .append(row.passed()).append(" |\n");
        }

        md.append("\n## LOC and files\n\n");
        md.append("| Approach | Category | Files | Logical LOC | Missing files |\n");
        md.append("|---|---|---:|---:|---|\n");
        for (LocMetric row : locMetrics) {
            md.append("| ")
                    .append(row.approach()).append(" | ")
                    .append(row.category()).append(" | ")
                    .append(row.fileCount()).append(" | ")
                    .append(row.logicalLoc()).append(" | ")
                    .append(row.missingFiles().isEmpty() ? "-" : String.join("<br>", row.missingFiles()))
                    .append(" |\n");
        }

        md.append("\n## Runtime\n\n");
        md.append("| Approach | DTO count | Total, ms | Avg, μs / DTO |\n");
        md.append("|---|---:|---:|---:|\n");
        for (RuntimeMetric row : runtimeMetrics) {
            md.append("| ")
                    .append(row.approach()).append(" | ")
                    .append(row.dtoCount()).append(" | ")
                    .append(format(row.totalMillis())).append(" | ")
                    .append(format(row.avgMicrosPerDto())).append(" |\n");
        }

        md.append("\n## Runtime dependencies\n\n");
        md.append("| Approach | Runtime dependencies | Comment |\n");
        md.append("|---|---|---|\n");
        for (DependencyMetric row : dependencyMetrics) {
            md.append("| ")
                    .append(row.approach()).append(" | ")
                    .append(row.runtimeDependencies()).append(" | ")
                    .append(row.comment()).append(" |\n");
        }

        md.append("\n## Notes\n\n");
        md.append("- Logical LOC is calculated by excluding blank lines and full-line comments.\n");
        md.append("- Dataset contains ").append(DATASET_SIZE).append(" DTO cases.\n");
        md.append("- Large runtime run contains ")
                .append((long) DATASET_SIZE * LARGE_RUN_REPEATS)
                .append(" DTO validations.\n");
        md.append("- Runtime measurements are simple application-level measurements based on `System.nanoTime()`, not JMH microbenchmarks.\n");
        md.append("- Generated artifacts are treated as generated code and are not counted as handwritten implementation LOC.\n");
        md.append("- Integration wrappers are counted separately from rule implementation LOC.\n");

        Files.writeString(
                RESULTS_DIR.resolve("experiment-metrics-report.md"),
                md.toString(),
                StandardCharsets.UTF_8
        );
    }

    private void writeFile(String fileName, List<String> lines) throws IOException {
        Files.write(
                RESULTS_DIR.resolve(fileName),
                lines,
                StandardCharsets.UTF_8
        );
    }

    private String quote(String value) {
        return "\"" + value.replace("\"", "\"\"") + "\"";
    }

    private String format(double value) {
        return String.format(java.util.Locale.US, "%.3f", value);
    }

    private record FileGroup(
            String approach,
            String category,
            List<String> relativePaths
    ) {
    }

    private record CorrectnessMetric(
            String approach,
            int totalCases,
            int matchedCases,
            int falsePositive,
            int falseNegative,
            boolean passed
    ) {
    }

    private record LocMetric(
            String approach,
            String category,
            int fileCount,
            int logicalLoc,
            List<String> missingFiles
    ) {
    }

    private record RuntimeMetric(
            String approach,
            long dtoCount,
            long totalNanos,
            double totalMillis,
            double avgMicrosPerDto
    ) {
    }

    private record DependencyMetric(
            String approach,
            String runtimeDependencies,
            String comment
    ) {
    }
}