package experiment.common;

import java.util.List;

public record ValidationRunResult(
        String approach,
        String testCaseId,
        List<ViolationView> violations,
        long durationNanos
) {
}