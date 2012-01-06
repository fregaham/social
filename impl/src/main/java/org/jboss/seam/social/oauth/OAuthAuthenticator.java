/*
 * JBoss, Home of Professional Open Source
 * Copyright 2012, Red Hat Middleware LLC, and individual contributors
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.jboss.seam.social.oauth;

import java.io.IOException;
import java.io.Serializable;

import javax.enterprise.context.SessionScoped;
import javax.enterprise.inject.Instance;
import javax.enterprise.inject.spi.BeanManager;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

import org.jboss.seam.security.Authenticator;
import org.jboss.seam.security.BaseAuthenticator;
import org.jboss.seam.security.events.DeferredAuthenticationEvent;
import org.jboss.seam.social.MultiServicesManager;
import org.jboss.solder.logging.Logger;

/**
 * An Authenticator implementation that uses OAuth to authenticate the user.
 * 
 * @author maschmid
 *
 */
@Named("oauthAuthenticator")
@SessionScoped
public class OAuthAuthenticator extends BaseAuthenticator implements Authenticator, Serializable {

    private static final long serialVersionUID = -5615811225606141834L;

    private Instance<OAuthService> oauthService;
    
    private String serviceName = null;
    
    @Inject
    MultiServicesManager multiServicesManager;
    
    @Inject
    Logger log;
    
    @Inject BeanManager beanManager;
    
    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }
    
    public String getServiceName() {
        return serviceName;
    }
    
    @Override
    public void authenticate() {
        
        // If there is only one
        if (serviceName == null && !oauthService.isUnsatisfied() && !oauthService.isAmbiguous()) {
            log.debug("Authenticating unambiguous OAuthService");
            
            OAuthService unambiguousOAuthService = oauthService.get();
            
            if (unambiguousOAuthService.isConnected()) {
                setUser(new OAuthUser(unambiguousOAuthService.getType(), unambiguousOAuthService.getMyProfile()));
                setStatus(AuthenticationStatus.SUCCESS);                
            }
            else {
                
                try {
                    FacesContext.getCurrentInstance().getExternalContext().redirect(unambiguousOAuthService.getAuthorizationUrl());
                } catch (IOException e) {
                    log.error("Failed to redirect ", e);
                    setStatus(AuthenticationStatus.FAILURE);
                }
            
                setStatus(AuthenticationStatus.DEFERRED);
            }
        }
        else {
            // use MultiServicesManager
            log.debug("Useing oauthService by name");
            
            String serviceUrl = multiServicesManager.initNewSession(serviceName);
            
            try {
                FacesContext.getCurrentInstance().getExternalContext().redirect(serviceUrl);
            } catch (IOException e) {
                log.error("Failed to redirect ", e);
                setStatus(AuthenticationStatus.FAILURE);
            }
        }
        
        setStatus(AuthenticationStatus.FAILURE);
    }
    
    public String getVerifierParamName() {
        if (serviceName != null) {
            return multiServicesManager.getCurrentService().getVerifierParamName();
        }
        else {
            throw new UnsupportedOperationException("verifier with unambiguous service not implemented yet");
        }
    }
    
    public String getVerifier() {
        if (serviceName != null) {
            return multiServicesManager.getCurrentSession().getVerifier();
        }
        else {
            throw new UnsupportedOperationException("verifier with unambiguous service not implemented yet");
        }
    }
    
    public void setVerifier(String verifier) {
        if (serviceName != null) {
            multiServicesManager.getCurrentSession().setVerifier(verifier);
        }
        else {        
            throw new UnsupportedOperationException("verifier with unambiguous service not implemented yet");
        }
    }

    public void connect() {
        if (serviceName != null) {
            multiServicesManager.connectCurrentService();
           
            OAuthService currentService = multiServicesManager.getCurrentService();
            OAuthSession currentSession = multiServicesManager.getCurrentSession();
            
            setUser(new OAuthUser(currentService.getType(), currentSession.getUserProfile()));
            setStatus(AuthenticationStatus.SUCCESS);
            
            beanManager.fireEvent(new DeferredAuthenticationEvent(true));
        }
        else {
            throw new UnsupportedOperationException("verifier with unambiguous service not implemented yet");
        }
    }
}
