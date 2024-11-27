package fr.cyu.jee.service;

import java.util.HashMap;
import java.util.Map;

public class ServiceManager {

    private static final ServiceManager INSTANCE = new ServiceManager();

    public static ServiceManager getInstance() {
        return INSTANCE;
    }

    private final Map<ServiceKey<?>, Object> services;

    public ServiceManager(Map<ServiceKey<?>, Object> services) {
        this.services = services;
    }

    public ServiceManager() {
        this(new HashMap<>());
    }

    @SuppressWarnings("unchecked")
    public <T> T getService(ServiceKey<T> key) {
        return (T) services.get(key);
    }

    public <T> T registerService(ServiceKey<T> key, T service) {
        services.put(key, service);
        return service;
    }

    public void unregisterService(ServiceKey<?> key) {
        services.remove(key);
    }

    public void unregisterAll() {
        services.clear();
    }
}
