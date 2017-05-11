package com.blogspot.sontx.bottle.fs.security;

import com.blogspot.sontx.bottle.fs.bean.VerifyResult;

import javax.annotation.Priority;
import javax.ws.rs.Priorities;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.ResourceInfo;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;
import java.io.IOException;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Secured
@Provider
@Priority(Priorities.AUTHORIZATION)
public class AuthorizationFilter implements ContainerRequestFilter {

    @Context
    private ResourceInfo resourceInfo;

    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {

        // Get the resource class which matches with the requested URL
        // Extract the roles declared by it
        Class<?> resourceClass = resourceInfo.getResourceClass();
        List<Role> classRoles = extractRoles(resourceClass);

        // Get the resource method which matches with the requested URL
        // Extract the roles declared by it
        Method resourceMethod = resourceInfo.getResourceMethod();
        List<Role> methodRoles = extractRoles(resourceMethod);

        AuthenticationFilter.AuthenticationPrincipal authenticationPrincipal = (AuthenticationFilter.AuthenticationPrincipal) requestContext
                .getSecurityContext().getUserPrincipal();
        int role = authenticationPrincipal.getVerifyResult().getRole();

        try {

            // Check if the user is allowed to execute the method
            // The method annotations override the class annotations
            if (methodRoles.isEmpty()) {
                checkPermissions(classRoles, role);
            } else {
                checkPermissions(methodRoles, role);
            }

        } catch (Exception e) {
            e.printStackTrace();
            requestContext.abortWith(Response.status(Response.Status.FORBIDDEN).build());
        }
    }

    // Extract the roles from the annotated element
    private List<Role> extractRoles(AnnotatedElement annotatedElement) {
        if (annotatedElement == null) {
            return new ArrayList<>();
        } else {
            Secured secured = annotatedElement.getAnnotation(Secured.class);
            if (secured == null) {
                return new ArrayList<>();
            } else {
                Role[] allowedRoles = secured.value();
                return Arrays.asList(allowedRoles);
            }
        }
    }

    private void checkPermissions(List<Role> allowedRoles, int role) throws Exception {
        // Check if the user contains one of the allowed roles
        // Throw an Exception if the user has not permission to execute the
        // method
        if (!allowedRoles.isEmpty()) {
            if (role == VerifyResult.ROLE_ADMIN && !hasRole(allowedRoles, Role.ROLE_ADMIN))
                throw new Exception("Do not have user permission");
            if (role == VerifyResult.ROLE_USER && !hasRole(allowedRoles, Role.ROLE_USER))
                throw new Exception("Do not have admin permission");
        }

    }

    private boolean hasRole(List<Role> allowedRoles, Role role) {
        for (Role irole : allowedRoles) {
            if (irole == role)
                return true;
        }
        return false;
    }
}