package com.scheduler.ss;

import org.apache.log4j.Logger;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Class for overriding authentication entry point
 */
public class JsfLoginUrlAuthenticationEntryPoint extends
        LoginUrlAuthenticationEntryPoint {

    private Logger log = Logger.getLogger(this.getClass().getName());

    private RedirectStrategy redirectStrategy;

    public void setRedirectStrategy(RedirectStrategy redirectStrategy) {
        this.redirectStrategy = redirectStrategy;
    }

    @Override
    public void commence(HttpServletRequest request,
                         HttpServletResponse response, AuthenticationException authException)
            throws IOException, ServletException {

        String redirectUrl = null;

        if (isUseForward()) {

            if (isForceHttps() && "http".equals(request.getScheme())) {
                redirectUrl = buildHttpsRedirectUrlForRequest(request);
            }
            if (redirectUrl == null) {
                String loginForm = determineUrlToUseForThisRequest(request,
                        response, authException);
                log.info("Server side forward to: " + loginForm);
                RequestDispatcher dispatcher = request.getRequestDispatcher(loginForm);
                dispatcher.forward(request, response);
                return;
            }
        } else {
            redirectUrl = buildRedirectUrlToLoginPage(request, response,
                    authException);
        }
        redirectStrategy.sendRedirect(request, response, redirectUrl);
    }

}
