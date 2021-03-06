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
package org.jboss.seam.social.twitter.jackson;

import static org.jboss.seam.social.TwitterLiteral.INSTANCE;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import org.codehaus.jackson.annotate.JsonCreator;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;
import org.jboss.seam.social.URLUtils;
import org.jboss.seam.social.oauth.OAuthServiceBase;
import org.jboss.seam.social.rest.RestResponse;
import org.jboss.seam.social.rest.RestVerb;
import org.jboss.seam.social.twitter.RateLimitStatus;
import org.jboss.seam.social.twitter.TwitterService;
import org.jboss.seam.social.twitter.model.SuggestionCategory;
import org.jboss.seam.social.twitter.model.Tweet;
import org.jboss.seam.social.twitter.model.TwitterProfile;
import org.jboss.solder.logging.Logger;

/**
 * @author Antoine Sabot-Durand
 * 
 */
public class TwitterServiceJackson extends OAuthServiceBase implements TwitterService {

    /**
     * Typed list of TwitterProfile. This helps Jackson know which type to deserialize list contents into.
     * 
     * @author Craig Walls
     * @author Antoine Sabot-Durand
     */

    @SuppressWarnings("serial")
    static class TwitterProfileList extends ArrayList<TwitterProfile> {

    }

    @SuppressWarnings("serial")
    static class SuggestionCategoryList extends ArrayList<SuggestionCategory> {

    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    static class TwitterProfileUsersList {

        private final List<TwitterProfile> list;

        @JsonCreator
        public TwitterProfileUsersList(@JsonProperty("users") List<TwitterProfile> list) {
            this.list = list;
        }

        public List<TwitterProfile> getList() {
            return list;
        }
    }

    private static final long serialVersionUID = 6806035986656777834L;
    static final String VERIFY_CREDENTIALS_URL = "https://api.twitter.com/1/account/verify_credentials.json";
    static final String GET_USER_PROFILE_URL = "https://api.twitter.com/1/users/show.json";
    static final String SEARCH_USER_URL = "https://api.twitter.com/1/users/search.json";
    static final String SUGGESTION_CATEGORIES = "https://api.twitter.com/1/users/suggestions.json";

    static final String FRIENDS_STATUSES_URL = "https://api.twitter.com/1/statuses/friends.json?screen_name={screen_name}";

    // static final String SEARCH_URL = "https://search.twitter.com/search.json?q={query}&rpp={rpp}&page={page}";
    static final String TWEET_URL = "https://api.twitter.com/1/statuses/update.json";
    static final String RETWEET_URL = "https://api.twitter.com/1/statuses/retweet/{tweet_id}.json";
    static final String LOGO_URL = "https://d2l6uygi1pgnys.cloudfront.net/2-2-08/images/buttons/twitter_connect.png";

    @Inject
    Logger log;

    @Override
    public Tweet updateStatus(String message) {
        RestResponse resp = sendSignedRequest(RestVerb.POST, TWEET_URL, "status", message);
        log.infof("update status is %s", message);
        return jsonService.requestObject(resp, Tweet.class);

    }

    /*
     * (non-Javadoc)
     * 
     * @see org.jboss.seam.social.oauth.OAuthService#getServiceLogo()
     */
    @Override
    public String getServiceLogo() {
        return LOGO_URL;
    }

    public String getProfileId() {
        requireAuthorization();
        return getMyProfile().getId();
    }

    public String getScreenName() {
        requireAuthorization();
        return getMyProfile().getScreenName();
    }

    @Override
    public TwitterProfile getMyProfile() {
        return (TwitterProfile) getSession().getUserProfile();
    }

    public TwitterProfile getUserProfile(String screenName) {
        return jsonService.requestObject(
                sendSignedRequest(RestVerb.GET,
                        URLUtils.appendParametersToQueryString(GET_USER_PROFILE_URL, "screen_name", screenName)),
                TwitterProfile.class);
    }

    public TwitterProfile getUserProfile(long userId) {
        return jsonService.requestObject(
                sendSignedRequest(RestVerb.GET,
                        URLUtils.appendParametersToQueryString(GET_USER_PROFILE_URL, "user_id", String.valueOf(userId))),
                TwitterProfile.class);
    }

    @Override
    public List<TwitterProfile> getUsers(Long... userIds) {
        String joinedIds = URLUtils.commaJoiner.join(userIds);
        return jsonService.requestObject(
                sendSignedRequest(RestVerb.GET,
                        URLUtils.appendParametersToQueryString(GET_USER_PROFILE_URL, "user_id", joinedIds)),
                TwitterProfileList.class);
    }

    @Override
    public List<TwitterProfile> getUsers(String... screenNames) {
        String joinedScreenNames = URLUtils.commaJoiner.join(screenNames);
        return jsonService.requestObject(
                sendSignedRequest(RestVerb.GET,
                        URLUtils.appendParametersToQueryString(GET_USER_PROFILE_URL, "screen_name", joinedScreenNames)),
                TwitterProfileList.class);
    }

    public List<TwitterProfile> searchForUsers(String query) {
        requireAuthorization();
        return jsonService.requestObject(
                sendSignedRequest(RestVerb.GET, URLUtils.appendParametersToQueryString(SEARCH_USER_URL, "q", query)),
                TwitterProfileList.class);
    }

    @Override
    public List<SuggestionCategory> getSuggestionCategories() {
        requireAuthorization();
        return jsonService.requestObject(sendSignedRequest(RestVerb.GET, SUGGESTION_CATEGORIES), SuggestionCategoryList.class);
    }

    public List<TwitterProfile> getSuggestions(String slug) {
        return jsonService.requestObject(
                sendSignedRequest(RestVerb.GET, "https://api.twitter.com/1/users/suggestions/" + slug + ".json"),
                TwitterProfileUsersList.class).getList();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.jboss.seam.social.OAuthServiceBase#initMyProfile()
     */
    @Override
    protected void initMyProfile() {
        getSession().setUserProfile(
                jsonService.requestObject(sendSignedRequest(RestVerb.GET, VERIFY_CREDENTIALS_URL), TwitterProfile.class));
    }

    @Override
    public RateLimitStatus getRateLimitStatus() {
        return jsonService.requestObject(
                sendSignedRequest(RestVerb.GET, "https://api.twitter.com/1/account/rate_limit_status.json"),
                RateLimitStatus.class);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.jboss.seam.social.rest.RestService#getQualifier()
     */
    @Override
    public Annotation getQualifier() {
        return INSTANCE;
    }
}
