package com.thecookiezen.hasingleton.control;

import lombok.extern.log4j.Log4j;
import org.jboss.as.server.ServerEnvironment;
import org.jboss.msc.service.Service;
import org.jboss.msc.service.StartContext;
import org.jboss.msc.service.StartException;
import org.jboss.msc.service.StopContext;
import org.jboss.msc.value.Value;

import java.util.concurrent.atomic.AtomicBoolean;

@Log4j
public class HASingletonService implements Service<String> {

    private final Value<ServerEnvironment> env;
    private final AtomicBoolean started = new AtomicBoolean(false);

    public HASingletonService(Value<ServerEnvironment> env) {
        this.env = env;
    }

    @Override
    public void start(StartContext startContext) throws StartException {
        if (!started.compareAndSet(false, true)) {
            log.warn("The service " + this.getClass().getName() + " is already active!");
        }
    }

    @Override
    public void stop(StopContext stopContext) {
        if (!started.compareAndSet(true, false)) {
            log.warn("The service " + this.getClass().getName() + " is not active!");
        }
    }

    @Override
    public String getValue() throws IllegalStateException, IllegalArgumentException {
        if (!this.started.get()) {
            throw new IllegalStateException();
        }
        return this.env.getValue().getNodeName();
    }
}
