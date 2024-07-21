package com.nhnacademy.orderpaymentrefund.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URI;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

@Slf4j
public class HeaderFilter extends OncePerRequestFilter {
    public static class RouteConfig {
        String path;
        String method;
        List<String> roles;

        public RouteConfig(URI path, String method, List<String> roles) {
            this.path = path.getPath();
            this.method = method;
            this.roles = roles;
        }
    }

    private final List<RouteConfig> routeConfigs;
    private final AntPathMatcher pathMatcher;

    public HeaderFilter(List<RouteConfig> routeConfigs) {
        this.routeConfigs = routeConfigs;
        this.pathMatcher = new AntPathMatcher();
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        for (RouteConfig routeConfig : routeConfigs) {
            if (pathMatcher.match(routeConfig.path, request.getRequestURI()) && request.getMethod().equalsIgnoreCase(routeConfig.method)) {
                log.info("{}:{} header filter start", routeConfig.method, request.getRequestURI());
                try {
                    Long.valueOf(request.getHeader("X-User-Id"));
                } catch ( NumberFormatException e ) {
                    response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "id header is missing or invalid");
                    return;
                }

                if (!isContainRole(getHeaderValues(request, "X-User-Role"), routeConfig.roles)) {
                    response.sendError(HttpServletResponse.SC_FORBIDDEN, "Role header is missing or invalid");
                    return;
                }
            }
        }
        filterChain.doFilter(request, response);
    }

    private Set<String> getHeaderValues(HttpServletRequest request, String headerName) {
        Set<String> headerValues = new HashSet<>();
        Enumeration<String> headers = request.getHeaders(headerName);
        while (headers.hasMoreElements()) {
            headerValues.add(headers.nextElement());
        }
        return headerValues;
    }

    private boolean isContainRole(Set<String> roleSet, List<String> requiredRole) {
        for (String role : requiredRole) {
            if (!roleSet.contains(role)) {
                return false;
            }
        }
        return true;
    }
}
