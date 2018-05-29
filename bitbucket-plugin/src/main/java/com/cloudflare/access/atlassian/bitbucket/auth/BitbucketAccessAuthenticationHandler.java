package com.cloudflare.access.atlassian.bitbucket.auth;

import static com.cloudflare.access.atlassian.bitbucket.auth.BitbucketPluginDetails.AUTHENTICATED_USER_NAME_ATTRIBUTE;

import java.io.IOException;

import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.atlassian.bitbucket.auth.HttpAuthenticationContext;
import com.atlassian.bitbucket.auth.HttpAuthenticationHandler;
import com.atlassian.bitbucket.auth.HttpAuthenticationSuccessContext;
import com.atlassian.bitbucket.auth.HttpAuthenticationSuccessHandler;
import com.atlassian.bitbucket.user.ApplicationUser;
import com.atlassian.bitbucket.user.UserService;
import com.atlassian.plugin.spring.scanner.annotation.imports.ComponentImport;

@Named("cloudflareAccessAuthenticationHandler")
public class BitbucketAccessAuthenticationHandler implements HttpAuthenticationHandler, HttpAuthenticationSuccessHandler {

	private static final Logger log = LoggerFactory.getLogger(BitbucketAccessAuthenticationHandler.class);

	private UserService userService;

	@Inject
	public BitbucketAccessAuthenticationHandler(@ComponentImport UserService userService) {
		this.userService = userService;
	}

	@Override
	public ApplicationUser authenticate(HttpAuthenticationContext httpAuthenticationContext) {
        HttpServletRequest httpRequest = httpAuthenticationContext.getRequest();

        String userName = (String) httpRequest.getAttribute(AUTHENTICATED_USER_NAME_ATTRIBUTE);
        log.debug("Attempting authentication, username: {}", userName);
        if(userName == null) {
        	log.debug("No username in the request, opt-out auth");
        	return null;
        }
        log.debug("Loading user from user sevice...");
		return userService.getUserByName(userName);
	}

	@Override
	public void validateAuthentication(HttpAuthenticationContext httpAuthenticationContext) {
		log.debug("Passing auth validation");
		HttpSession session = httpAuthenticationContext.getRequest().getSession(false);
        if (session == null) {
            // nothing to validate - the user wasn't authenticated by this authentication handler
            return;
        }
	}

	@Override
	public boolean onAuthenticationSuccess(HttpAuthenticationSuccessContext context) throws ServletException, IOException {
        String authenticatedUserName = (String) context.getRequest().getAttribute(AUTHENTICATED_USER_NAME_ATTRIBUTE);
        if (authenticatedUserName != null) {
        	log.debug("Found value for attribute '{}', successful auth!", AUTHENTICATED_USER_NAME_ATTRIBUTE);
            context.getRequest().getSession().setAttribute(AUTHENTICATED_USER_NAME_ATTRIBUTE, authenticatedUserName);
            return true;
        }
        log.debug("No '{}' attribute in the request, unsucessful auth!", AUTHENTICATED_USER_NAME_ATTRIBUTE);
        return false;
	}




}
