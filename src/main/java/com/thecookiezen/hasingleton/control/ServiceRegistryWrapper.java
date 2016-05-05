package com.thecookiezen.hasingleton.control;

import org.jboss.msc.service.ServiceRegistry;

public class ServiceRegistryWrapper {
    private static volatile ServiceRegistry serviceRegistry;

    public static ServiceRegistry getServiceRegistry() {
        return serviceRegistry;
    }

    public static synchronized void setServiceRegistry(final ServiceRegistry serviceRegistry) {
        if (ServiceRegistryWrapper.serviceRegistry == null) {
            ServiceRegistryWrapper.serviceRegistry = serviceRegistry;
        }
    }
}
