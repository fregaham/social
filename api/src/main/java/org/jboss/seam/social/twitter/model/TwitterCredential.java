/**
 * 
 */
package org.jboss.seam.social.twitter.model;

import org.jboss.seam.social.oauth.User;
import org.jboss.seam.social.twitter.TwitterHandler;

/**
*
* Implementation of this interface contains information about Twitter Credential of the current user
* It is returned by {@link TwitterHandler#verifyCrendentials()}
* 
* @author Antoine Sabot-Durand
*
*/
public interface TwitterCredential extends User
{


   public String getName();

   public String getScreenName();

   
}
