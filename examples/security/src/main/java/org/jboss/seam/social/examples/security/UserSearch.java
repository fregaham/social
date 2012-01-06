package org.jboss.seam.social.examples.security;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;

import javax.enterprise.event.Observes;
import javax.enterprise.inject.Model;
import javax.inject.Inject;

import org.jboss.seam.security.events.DeferredAuthenticationEvent;
import org.jboss.seam.security.events.LoggedInEvent;
import org.jboss.seam.security.events.LoginFailedEvent;
import org.jboss.solder.logging.Logger;
import org.picketlink.idm.api.Attribute;
import org.picketlink.idm.api.IdentitySearchCriteria;
import org.picketlink.idm.api.User;
import org.picketlink.idm.api.IdentitySession;
import org.picketlink.idm.common.exception.IdentityException;
import org.picketlink.idm.impl.api.IdentitySearchCriteriaImpl;

@Model
public class UserSearch {
    @Inject
    IdentitySession identitySession;
    
    @Inject Logger log;

    public List<User> getUsers() throws IdentityException {
        List<User> list = new ArrayList<User>();
        
        IdentitySearchCriteria criteria = new IdentitySearchCriteriaImpl();
        for (User user : identitySession.getPersistenceManager().findUser(criteria)) {   
            list.add(user);
        }
        
        return list;
    }
    
    public void onLoggedIn(@Observes LoggedInEvent event) {
        log.info("Logged in: " + event.getUser().getId());
    }
    
    public void onLoginFailed(@Observes LoginFailedEvent event) {
        log.info("Login failed ", event.getLoginException());
    }
    
    public void onDeferredEvent(@Observes DeferredAuthenticationEvent event) {
        log.info("Deferred login " + event.isSuccess());
    }
}
