package org.jboss.seam.social.examples.security.model;

import javax.enterprise.event.Observes;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.jboss.solder.servlet.WebApplication;
import org.jboss.solder.servlet.event.Initialized;
import org.jboss.seam.transaction.Transactional;

/**
 * Loads initial data in a platform-independent way
 *
 * @author Marek Schmidt
 */
public class PopulateDatabase {
    
    @PersistenceContext
    EntityManager entityManager;
    
    @Transactional
    public void loadData(@Observes @Initialized WebApplication webapp) {
        
        // Object types
        IdentityObjectType USER = new IdentityObjectType();
        USER.setName("USER");
        entityManager.persist(USER);
        
        IdentityObjectType GROUP = new IdentityObjectType();
        GROUP.setName("GROUP");
        entityManager.persist(GROUP);

        // Credential types
        IdentityObjectCredentialType PASSWORD = new IdentityObjectCredentialType();
        PASSWORD.setName("PASSWORD");
        entityManager.persist(PASSWORD);
        
        // Object relationship types
        IdentityObjectRelationshipType jbossIdentityMembership = new IdentityObjectRelationshipType();
        jbossIdentityMembership.setName("JBOSS_IDENTITY_MEMBERSHIP");
        entityManager.persist(jbossIdentityMembership);
        
        IdentityObjectRelationshipType jbossIdentityRole = new IdentityObjectRelationshipType();
        jbossIdentityRole.setName("JBOSS_IDENTITY_ROLE");
        entityManager.persist(jbossIdentityRole);
    }
}
