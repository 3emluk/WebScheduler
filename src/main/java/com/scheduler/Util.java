package com.scheduler;

import org.apache.log4j.Logger;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.SavedRequest;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.URL;

/**
 * Class for performing static actions during all app
 */
public class Util {

    /**
     * Log4j log variable
     */
    private static Logger log = Logger.getLogger(Util.class);

    /**
     * Add a simple Face Message to current page
     *
     * @param msg Value of desired message
     */
    public static void addSimpleMessage(String msg) {
        FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(msg));
    }

    /**
     * Add a complex Face Message to current page
     *
     * @param severity Type of message
     * @param label    Title of message
     * @param message  Text of message
     */
    public static void addParamtrizedMessage(FacesMessage.Severity severity, String label, String message) {
        FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(severity, label, message));
    }

    /**
     * Method or getting attribute value from request
     *
     * @param param Name of attribute
     * @return Value of attribute
     */
    public static String getParameter(String param) {
        HttpServletRequest request = (HttpServletRequest) FacesContext
                .getCurrentInstance().getExternalContext().getRequest();
        if (request != null) {
            return request.getParameter(param);
        }
        return null;
    }

    /**
     * Method or setting attribute value to request
     *
     * @param param Name of attribute
     * @param value Value of attribute
     */
    public static void setParameter(String param, Object value) {
        HttpServletRequest request = (HttpServletRequest) FacesContext
                .getCurrentInstance().getExternalContext().getRequest();
        if (request != null) {
            request.setAttribute(param, value);
        }
    }


    /**
     * Method for saving previously asked url before redirecting
     * @return Previously asked url
     */
    public static String getSavedUrl() {
        HttpServletRequest request = ((HttpServletRequest) FacesContext
                .getCurrentInstance().getExternalContext().getRequest());

        SavedRequest savedRequest = new HttpSessionRequestCache().getRequest(
                request, (HttpServletResponse) FacesContext
                        .getCurrentInstance().getExternalContext()
                        .getResponse());

        if (savedRequest != null) {
            try {
                URL url = new URL(savedRequest.getRedirectUrl());
                return url.getFile().substring(
                        request.getContextPath().length());
            } catch (Exception e) {
                log.error(e.getMessage() + " Using default URL");
            }
        }
        return "/?faces-redirect=true";
    }
}
