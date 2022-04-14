package com.jjrepos.atom.api.security;

import com.jjrepos.atom.api.security.usercontext.UserContext;
import com.jjrepos.atom.api.security.usercontext.UserContextManager;
import com.jjrepos.atom.api.security.usercontext.UserContextProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.util.Optional;
import java.util.Set;

@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class AuthorizationInterceptor extends HandlerInterceptorAdapter {

    private static final Logger LOG = LoggerFactory.getLogger(AuthorizationInterceptor.class);
    @Autowired
    private Set<UserContextProvider> providers;

    @Autowired
    private Optional<ResourceOwnerAuthorizationProvider> authProvider;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        Optional<ProtectedResource> annotation = getProtectedResourceAnnotation(handler);
        if (annotation == null) {
            return false;
        }
        annotation.ifPresent(protectedResource -> authorize(protectedResource.type(), request));
        return true;
    }

    /**
     * Gets the annotation present on a resource method or the resource class.
     * <p>
     * Note:
     * </p>
     * First preference is given to the annotation at the method level, as it could
     * override the class level annotation.
     *
     * @param resourceInfo {@link ResourceInfo}
     * @return {@link ProtectedResource} annotation if present, null otherwise.
     */
    private Optional<ProtectedResource> getProtectedResourceAnnotation(Object handler) {
        LOG.debug("AuthorizationInterceptor handler {}: ", handler.toString());
        if (!(handler instanceof HandlerMethod)) {
            return Optional.empty();
        }
        Method method = ((HandlerMethod) handler).getMethod();
        LOG.debug("AuthorizationInterceptor method {}: ", method.toString());
        ProtectedResource annotation = method.getAnnotation(ProtectedResource.class);
        return Optional.ofNullable(
                (annotation == null) ? method.getDeclaringClass().getAnnotation(ProtectedResource.class) : annotation);
    }

    private void authorize(ResourceAccessType accessType, HttpServletRequest request) {
        UserContext ctx = providers.stream().map(provider -> provider.getUserContext(request))
                .filter(Optional::isPresent).map(Optional::get).findFirst().orElseThrow(NotAuthorizedException::new);
        UserContextManager.setUserContext(ctx);
        LOG.trace("ResourceAccessType= {}", accessType.toString());
        if (accessType == ResourceAccessType.OWNER) {
            authProvider.ifPresent(provider -> provider.isResourceOwner(request));
            if (authProvider.isPresent()) {
                if (!authProvider.get().isResourceOwner(request)) {
                    throw new NotAuthorizedException("User is not resource owner");
                }
            }
        }
    }

}
