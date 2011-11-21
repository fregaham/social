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

import java.util.Map;

import org.jboss.seam.social.rest.RestRequest;

/**
 * Implementation of this interface Represent an OAuth Request
 * 
 * @author Antoine Sabot-Durand
 * 
 */
public interface OAuthRequest extends RestRequest {

    /**
     * Adds an OAuth parameter.
     * 
     * @param key name of the parameter
     * @param value value of the parameter
     * 
     */
    public void addOAuthParameter(String name, String value);

    /**
     * Returns the {@link Map} containing the key-value pair of parameters.
     * 
     * @return parameters as map
     */
    public Map<String, String> getOauthParameters();

}