/*
 * Copyright 2011 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.jboss.seam.social.twitter;

import java.util.Date;

import org.codehaus.jackson.annotate.JsonCreator;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;

/**
 * Carries data concerning the rate limit status.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class RateLimitStatus {

	private final int hourlyLimit;
	
	private final int remainingHits;
	
	private final long resetTimeInSeconds;
	
	private final Date resetTime;
	
	@JsonCreator	
	public RateLimitStatus(@JsonProperty("hourly_limit") int hourlyLimit, 
			@JsonProperty("remaining_hits") int remainingHits, @JsonProperty("reset_time_in_seconds") long resetTimeInSeconds) {
		this.hourlyLimit = hourlyLimit;
		this.remainingHits = remainingHits;
		this.resetTimeInSeconds = resetTimeInSeconds;
		this.resetTime = new Date(resetTimeInSeconds * 1000);
	}
	
	public int getHourlyLimit() {
		return hourlyLimit;
	}

	public int getRemainingHits() {
		return remainingHits;
	}

	public long getResetTimeInSeconds() {
		return resetTimeInSeconds;
	}
	
	public Date getResetTime() {
		return resetTime;
	}

	/**
	 * The approximate number of seconds until the rate limits are reset.
	 * Note that this method recalculates the seconds until reset on each call.
	 */
	public int getSecondsUntilReset() {
		return (int) (resetTimeInSeconds - System.currentTimeMillis() / 1000);
	}
}
