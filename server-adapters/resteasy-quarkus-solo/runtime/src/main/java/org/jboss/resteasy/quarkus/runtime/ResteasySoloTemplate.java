package org.jboss.resteasy.quarkus.runtime;

import io.netty.channel.EventLoopGroup;
import io.quarkus.arc.runtime.BeanContainer;
import io.quarkus.netty.BossGroup;
import io.quarkus.runtime.annotations.Template;
import org.jboss.logging.Logger;

import javax.inject.Inject;
import java.lang.annotation.Annotation;

@Template
public class ResteasySoloTemplate {
    private static final Logger log = Logger.getLogger(ResteasySoloTemplate.class);

    /*
    @Inject
    EventLoopGroup work;

    @Inject
    @BossGroup
    EventLoopGroup boss;
     */

    public void start(BeanContainer beanContainer) {
        log.info("STARTING RESTEASY SOLO");
        EventLoopGroup work = beanContainer.instance(EventLoopGroup.class);
        BossGroup bossAnnotation = new BossGroup() {
            @Override
            public Class<? extends Annotation> annotationType() {
                return BossGroup.class;
            }
        };
        EventLoopGroup boss = beanContainer.instance(EventLoopGroup.class, bossAnnotation);
        log.info("work: " + (work == null ? "null" : "set"));
        log.info("boss: " + (boss == null ? "null" : "set"));

    }
}
