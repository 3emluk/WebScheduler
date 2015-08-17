package com.scheduler.ss;

import org.apache.log4j.Logger;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.util.UrlUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Class for implementing RedirectStrategy
 */
public class JsfRedirectStrategy implements RedirectStrategy {

    private static final String FACES_REQUEST_HEADER = "faces-request";

    private Logger log = Logger.getLogger(this.getClass().getName());

    private boolean contextRelative;

    /**
     * Method for redirecting the response to the supplied URL.
     */
    public void sendRedirect(HttpServletRequest request,
                             HttpServletResponse response, String url) throws IOException {
        String redirectUrl = calculateRedirectUrl(request.getContextPath(), url);
        redirectUrl = response.encodeRedirectURL(redirectUrl);

        boolean ajaxRedirect = "partial/ajax".equals(request
                .getHeader(FACES_REQUEST_HEADER));
        if (ajaxRedirect) {

            String ajaxRedirectXml = createAjaxRedirectXml(redirectUrl);
            log.info("Ajax partial response to redirect: "
                    + ajaxRedirectXml);

            response.setContentType("text/xml");
            response.getWriter().write(ajaxRedirectXml);
        } else {
            log.info("Non-ajax redirecting to '" + redirectUrl + "'");
            response.sendRedirect(redirectUrl);
        }
    }

    /**
     * Method for calculating correct url to perform redirect
     *
     * @param contextPath
     * @param url
     * @return
     */
    private String calculateRedirectUrl(String contextPath, String url) {
        if (!UrlUtils.isAbsoluteUrl(url)) {
            if (contextRelative) {
                return url;
            } else {
                return contextPath + url;
            }
        }

        if (!contextRelative) {
            return url;
        }
        url = url.substring(url.lastIndexOf("://") + 3);
        url = url.substring(url.indexOf(contextPath) + contextPath.length());
        if (url.length() > 1 && url.charAt(0) == '/') {
            url = url.substring(1);
        }

        return url;
    }

    /**
     * Method for turning on relative context
     *
     * @param useRelativeContext Enable relative context?
     */
    public void setContextRelative(boolean useRelativeContext) {
        this.contextRelative = useRelativeContext;
    }

    private String createAjaxRedirectXml(String redirectUrl) {
        return new StringBuilder()
                .append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>")
                .append("<partial-response><redirect url=\"")
                .append(redirectUrl)
                .append("\"></redirect></partial-response>").toString();
    }

}
