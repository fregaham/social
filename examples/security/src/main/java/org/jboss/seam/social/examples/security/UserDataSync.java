package org.jboss.seam.social.examples.security;

import javax.enterprise.event.Observes;
import javax.inject.Inject;
import javax.persistence.EntityManager;

import org.jboss.seam.security.Identity;
import org.jboss.seam.security.events.DeferredAuthenticationEvent;
import org.jboss.seam.social.examples.security.model.IdentityObject;
import org.jboss.seam.social.oauth.OAuthUser;
import org.jboss.solder.logging.Logger;

public class UserDataSync {
    
    @Inject
    EntityManager em;
    
    @Inject
    Logger log;
    
    public void onDeferredEvent(@Observes DeferredAuthenticationEvent event, Identity identity) {
        if (event.isSuccess()) {
            
            log.info("updating user identity");
            
            OAuthUser oauthUser = (OAuthUser)identity.getUser();
            IdentityObject io = em.find(IdentityObject.class, oauthUser.getKey());
            io.setFullName(oauthUser.getUserProfile().getFullName());           
            io.setProfileImageUrl(oauthUser.getUserProfile().getProfileImageUrl());           
            
            log.info("updating user identity end");
        }
    }
}
