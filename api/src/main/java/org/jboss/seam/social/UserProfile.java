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
package org.jboss.seam.social;

import java.io.Serializable;

/**
 * Generic abstract class to define a basic user information
 * 
 * @author Antoine Sabot-Durand
 */
@SuppressWarnings("serial")
public abstract class UserProfile implements Serializable {

    protected final String id;

    protected UserProfile(String id) {
        super();
        this.id = id;
    }

    /**
     * Permanent identifier against the social relationship for the life-time of the network account
     * 
     * @return the user's social network key
     */
    public String getId() {
        return id;
    }

    /**
     * @return the user's full name
     */
    public abstract String getFullName();

    /**
     * @return the user's picture url
     */
    public abstract String getProfileImageUrl();

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
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
        UserProfile other = (UserProfile) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        return true;
    }

}
