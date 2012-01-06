package org.jboss.seam.social.examples.security;

import javax.enterprise.context.SessionScoped;
import javax.enterprise.inject.Produces;
import javax.inject.Named;

import org.jboss.seam.social.MultiServicesManager;
import org.jboss.solder.core.Client;

// just a hack to make the MultiServicesManager @Named
public class MultiServicesManagerProducer {
    
    @Named
    @Produces
    @SessionScoped
    @Client
    MultiServicesManager getMultiServicesManager(MultiServicesManager manager) {
        return manager;
    }
}
