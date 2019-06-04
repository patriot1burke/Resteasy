package org.jboss.resteasy.quarkus.deployment;

import io.quarkus.arc.deployment.BeanContainerBuildItem;
import io.quarkus.deployment.annotations.BuildStep;
import io.quarkus.deployment.annotations.ExecutionTime;
import io.quarkus.deployment.annotations.Record;
import io.quarkus.deployment.builditem.FeatureBuildItem;
import org.jboss.logging.Logger;
import org.jboss.resteasy.quarkus.runtime.ResteasySoloTemplate;

public class ResteasySoloProcessor {
    private static final Logger log = Logger.getLogger(ResteasySoloProcessor.class);


    @BuildStep(providesCapabilities = "org.jboss.resteasy.resteasy-solo")
    FeatureBuildItem featureBuildItem() {
        return new FeatureBuildItem("resteasy-solo");
    }

    @BuildStep
    @Record(ExecutionTime.RUNTIME_INIT)
    public void build(final ResteasySoloTemplate solo, final BeanContainerBuildItem beanContainer) {
        log.info("RESTEASY SOLO RUNTIME_INIT");
        solo.start(beanContainer.getValue());

    }
}
