package ru.otus.library.authentication;

import org.aopalliance.intercept.MethodInvocation;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.expression.method.DefaultMethodSecurityExpressionHandler;
import org.springframework.security.access.expression.method.MethodSecurityExpressionHandler;
import org.springframework.security.access.expression.method.MethodSecurityExpressionOperations;
import org.springframework.security.authentication.AuthenticationTrustResolver;
import org.springframework.security.authentication.AuthenticationTrustResolverImpl;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.method.configuration.GlobalMethodSecurityConfiguration;
import org.springframework.security.core.Authentication;

@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class MethodSecurityConfiguration extends GlobalMethodSecurityConfiguration {

    @Override
    protected MethodSecurityExpressionHandler createExpressionHandler() {
        return new CustomMethodSecurityExpressionHandler();
    }

    private class CustomMethodSecurityExpressionHandler extends DefaultMethodSecurityExpressionHandler {
        private AuthenticationTrustResolver trustResolver = new AuthenticationTrustResolverImpl();

        @Override
        protected MethodSecurityExpressionOperations createSecurityExpressionRoot(Authentication authentication,
                                                                                  MethodInvocation invocation) {
            MethodSecurityExpressionRoot securityExpressionRoot = new MethodSecurityExpressionRoot(authentication);
            securityExpressionRoot.setPermissionEvaluator(getPermissionEvaluator());
            securityExpressionRoot.setTrustResolver(this.trustResolver);
            securityExpressionRoot.setRoleHierarchy(getRoleHierarchy());
            return securityExpressionRoot;
        }
    }
}
