/*
 * JBoss, Home of Professional Open Source
 * Copyright 2011, Red Hat Middleware LLC, and individual contributors
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

import java.lang.annotation.Annotation;

import org.jboss.seam.social.SeamSocialExtension;
import org.jboss.seam.social.UserProfile;

/**
 * @author antoine
 */
public class OAuthSessionImpl implements OAuthSession {

    /**
     * 
     */
    private static final long serialVersionUID = -2526192334215289830L;

    private OAuthToken requestToken;

    private OAuthToken accessToken;

    private String verifier;

    private UserProfile userProfile;

    private Annotation serviceQualifier;

    private String serviceName;

    public OAuthSessionImpl(Annotation qualifier) {
        serviceQualifier = qualifier;
        serviceName = SeamSocialExtension.getServicesToQualifier().get(qualifier);
    }

    @Override
    public OAuthToken getRequestToken() {
        return requestToken;
    }

    @Override
    public void setRequestToken(OAuthToken requestToken) {
        this.requestToken = requestToken;
    }

    @Override
    public OAuthToken getAccessToken() {
        return accessToken;
    }

    @Override
    public void setAccessToken(OAuthToken accessToken) {
        this.accessToken = accessToken;
    }

    @Override
    public String getVerifier() {
        return verifier;
    }

    @Override
    public void setVerifier(String verifier) {
        this.verifier = verifier;
    }

    @Override
    public UserProfile getUserProfile() {
        return userProfile;
    }

    @Override
    public void setUserProfile(UserProfile userProfile) {
        this.userProfile = userProfile;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((userProfile == null) ? 0 : userProfile.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        OAuthSessionImpl other = (OAuthSessionImpl) obj;
        if (userProfile == null) {
            if (other.userProfile != null)
                return false;
        } else if (!userProfile.equals(other.userProfile))
            return false;
        return true;
    }

    @Override
    public Annotation getServiceQualifier() {
        return serviceQualifier;
    }

    @Override
    public String toString() {
        return serviceName + " - " + (isConnected() ? getUserProfile().getFullName() : "not connected");
    }

    @Override
    public boolean isConnected() {
        return accessToken != null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.jboss.seam.social.oauth.OAuthSession#getName()
     */
    @Override
    public String getName() {
        return toString();
    }

}