package com.lordbucket.bucketbank.middleware;

import com.lordbucket.bucketbank.middleware.annotation.PublicEndpoint;
import com.lordbucket.bucketbank.middleware.annotation.RoleRequirement;
import com.lordbucket.bucketbank.model.User;
import com.lordbucket.bucketbank.util.Role;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

public class SessionAuthInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (!(handler instanceof HandlerMethod hm)) {
            return true;
        }

        if (hm.hasMethodAnnotation(PublicEndpoint.class) ||
                hm.getBeanType().isAnnotationPresent(PublicEndpoint.class)) {
            return true;
        }

        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Authentication failed");
            return false;
        }

        boolean requiresRole = hm.hasMethodAnnotation(RoleRequirement.class);
        Role requiredRole = null;
        if (!requiresRole) {
            if (hm.getBeanType().getAnnotation(RoleRequirement.class) != null) {
                requiredRole = hm.getMethodAnnotation(RoleRequirement.class).value();
            }
        } else {
            requiredRole = hm.getMethodAnnotation(RoleRequirement.class).value();
        }

        if (requiredRole != null) {
            User user = (User) request.getSession(false).getAttribute("user");
            if (user.getRole() == Role.ADMINISTRATOR || user.getRole() == requiredRole) {
                return true;
            } else {
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "You have no right to perform this action.");
                return false;
            }
        }

        return true;
    }
}
