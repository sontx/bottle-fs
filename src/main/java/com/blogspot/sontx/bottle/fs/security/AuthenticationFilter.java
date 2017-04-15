package com.blogspot.sontx.bottle.fs.security;

import com.blogspot.sontx.bottle.fs.bean.VerifyResult;
import com.blogspot.sontx.bottle.fs.utils.ConfigUtils;
import com.blogspot.sontx.bottle.fs.utils.ConnectionUtils;
import lombok.Getter;

import javax.annotation.Priority;
import javax.ws.rs.NotAuthorizedException;
import javax.ws.rs.Priorities;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import javax.ws.rs.ext.Provider;
import java.io.IOException;
import java.security.Principal;

@Secured
@Provider
@Priority(Priorities.AUTHENTICATION)
public class AuthenticationFilter implements ContainerRequestFilter {
    private String authServerUrl;

    public AuthenticationFilter() {
        authServerUrl = ConfigUtils.getValue("default.auth.server");
    }

    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {
        String authorizationHeader = requestContext.getHeaderString(HttpHeaders.AUTHORIZATION);

        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            throw new NotAuthorizedException("Authorization header must be provided");
        }

        try {
            VerifyResult verifyResult = ConnectionUtils.post(authServerUrl, authorizationHeader, VerifyResult.class);
            requestContext.setSecurityContext(new AuthenticationSecurityContext(verifyResult));
        } catch (Exception e) {
            requestContext.abortWith(Response.status(Response.Status.UNAUTHORIZED).build());
        }
    }

    private static class AuthenticationSecurityContext implements SecurityContext {
        private AuthenticationPrincipal authenticationPrincipal;

        private AuthenticationSecurityContext(VerifyResult verifyResult) {
            authenticationPrincipal = new AuthenticationPrincipal(verifyResult);
        }

        @Override
        public String getAuthenticationScheme() {
            return null;
        }

        @Override
        public Principal getUserPrincipal() {
            return authenticationPrincipal;
        }

        @Override
        public boolean isSecure() {
            return false;
        }

        @Override
        public boolean isUserInRole(String arg0) {
            return true;
        }
    }

    public static class AuthenticationPrincipal implements Principal {
        @Getter
        private VerifyResult verifyResult;

        private AuthenticationPrincipal(VerifyResult verifyResult) {
            this.verifyResult = verifyResult;
        }

        @Override
        public String getName() {
            return verifyResult.toString();
        }

    }
}
