package experiment.ruleengine;

import experiment.common.ValidationAdapter;
import experiment.common.ViolationView;
import experiment.dto.MoneyTransferDto;
import org.kie.api.KieServices;
import org.kie.api.builder.KieBuilder;
import org.kie.api.builder.KieFileSystem;
import org.kie.api.builder.Message;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.kie.internal.io.ResourceFactory;

import java.util.ArrayList;
import java.util.List;

public final class RuleEngineValidationAdapter implements ValidationAdapter {

    private static final String RULES_PATH = "rules/money-transfer-validation.drl";

    private final KieContainer kieContainer;

    public RuleEngineValidationAdapter() {
        this.kieContainer = createKieContainer();
    }

    @Override
    public String name() {
        return "rule-engine-drools";
    }

    @Override
    public List<ViolationView> validate(MoneyTransferDto dto) {
        List<RuleEngineViolation> rawViolations = new ArrayList<>();

        KieSession session = kieContainer.newKieSession();
        try {
            session.setGlobal("violations", rawViolations);
            session.insert(dto);
            session.fireAllRules();
        } finally {
            session.dispose();
        }

        return rawViolations.stream()
                .map(v -> new ViolationView(v.ruleId(), v.field(), v.message()))
                .sorted()
                .toList();
    }

    private static KieContainer createKieContainer() {
        KieServices kieServices = KieServices.Factory.get();

        KieFileSystem kieFileSystem = kieServices.newKieFileSystem();
        kieFileSystem.write(
                ResourceFactory.newClassPathResource(RULES_PATH)
        );

        KieBuilder kieBuilder = kieServices.newKieBuilder(kieFileSystem);
        kieBuilder.buildAll();

        if (kieBuilder.getResults().hasMessages(Message.Level.ERROR)) {
            throw new IllegalStateException(
                    "Drools rule compilation failed: " + kieBuilder.getResults()
            );
        }

        return kieServices.newKieContainer(
                kieServices.getRepository().getDefaultReleaseId()
        );
    }
}