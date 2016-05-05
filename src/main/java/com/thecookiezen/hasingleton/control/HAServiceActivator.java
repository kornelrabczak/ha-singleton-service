package com.thecookiezen.hasingleton.control;

import org.jboss.as.server.ServerEnvironment;
import org.jboss.as.server.ServerEnvironmentService;
import org.jboss.msc.service.*;
import org.jboss.msc.value.InjectedValue;
import org.wildfly.clustering.singleton.SingletonServiceBuilderFactory;
import org.wildfly.clustering.singleton.election.NamePreference;
import org.wildfly.clustering.singleton.election.PreferredSingletonElectionPolicy;
import org.wildfly.clustering.singleton.election.SimpleSingletonElectionPolicy;

public class HAServiceActivator implements ServiceActivator {

    private static final String CONTAINER_NAME = "server";
    private static final String CACHE_NAME = "default";
    private static final ServiceName FACTORY_NAME = SingletonServiceBuilderFactory.SERVICE_NAME.append(CONTAINER_NAME, CACHE_NAME);
    public static final String PREFERRED_NODE = "node-1";
    public static final ServiceName SINGLETON_SERVICE_NAME = ServiceName.JBOSS.append("ha", "singleton", "example");

    @Override
    public void activate(ServiceActivatorContext context) {
        InjectedValue<ServerEnvironment> env = new InjectedValue<>();
        HASingletonService service = new HASingletonService(env);
        final ServiceRegistry serviceRegistry = context.getServiceRegistry();
        ServiceRegistryWrapper.setServiceRegistry(serviceRegistry);
        ServiceController<?> factoryService = serviceRegistry.getRequiredService(FACTORY_NAME);
        SingletonServiceBuilderFactory factory = (SingletonServiceBuilderFactory) factoryService.getValue();
        factory.createSingletonServiceBuilder(SINGLETON_SERVICE_NAME, service)
                .electionPolicy(new PreferredSingletonElectionPolicy(new SimpleSingletonElectionPolicy(), new NamePreference(PREFERRED_NODE)))
                .build(context.getServiceTarget())
                .addDependency(ServerEnvironmentService.SERVICE_NAME, ServerEnvironment.class, env)
                .setInitialMode(ServiceController.Mode.ACTIVE)
                .install();
    }
}
