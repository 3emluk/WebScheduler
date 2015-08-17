package com.scheduler.beans;

import com.scheduler.Util;
import com.scheduler.data.User;
import com.scheduler.service.UserService;
import org.apache.log4j.Logger;
import org.primefaces.context.RequestContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.GrantedAuthorityImpl;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.annotation.PostConstruct;
import javax.faces.FacesException;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Class for performing actions with User entity
 */
@ManagedBean
@SessionScoped
public class UserBean implements Serializable, UserDetailsService {


    /**
     * Stores a link to service to work with tasks from DB
     */
    //TODO Find out  why property doesn't injected by ManagedProperty before login...
    @Autowired
    @ManagedProperty("#{userService}")
    private UserService userService;


    /**
     * Stores a link to AuthenticationManager which resolves authentication process
     */
    @ManagedProperty("#{authenticationManager}")
    private AuthenticationManager authenticationManager;

    /**
     * Used for initializing all managed properties before methods executions
     */
    @PostConstruct
    public void initialize() {
    }

    /**
     * Log4j log variable
     */
    private Logger log = Logger.getLogger(this.getClass().getName());

    /**
     * Storing list of available users
     */
    private List<User> users;

    /**
     * Storing currently logged in User Entity
     */
    private User user = new User();

    public UserService getUserService() {
        return userService;
    }

    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }

    public AuthenticationManager getAuthenticationManager() {
        return authenticationManager;
    }

    public void setAuthenticationManager(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    /**
     * Method for deleting user from aplication
     * @return  Redirect url
     */
    public String delete() {
        try {
            userService.delete(this.user);
            Util.addParamtrizedMessage(FacesMessage.SEVERITY_INFO, "Profile deleted", "Logging out...");
            log.info("User:" + user.getId() + " deleted");
            FacesContext ctx = FacesContext.getCurrentInstance();
            ExternalContext extContext = ctx.getExternalContext();

            String url = extContext.encodeActionURL(ctx.getApplication().getViewHandler().getActionURL(ctx, "/j_spring_security_logout?faces-redirect=true"));

            try {
                extContext.redirect(url);
            } catch (IOException ioe) {
                throw new FacesException(ioe);
            }
            return "";
        } catch (Exception e) {
            log.error("User delete error: " + e.getMessage());
            Util.addParamtrizedMessage(FacesMessage.SEVERITY_ERROR, "Delete failed", "");
        }
        return "";
    }

    /**
     * Method for updating existing user profile
     * @return Redirect url
     */
    public String update() {
        RequestContext context = RequestContext.getCurrentInstance();
        try {
            Util.addSimpleMessage("Profile updated ");
            PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
            this.user.setPassword(passwordEncoder.encode(this.user.getPassword()));
            userService.update(user);
            context.addCallbackParam("isRegistered", true);
            log.info("User:" + user.getId() + " updated");
        } catch (Exception e) {
            Util.addSimpleMessage("Update failed " + this.user.getName());
            context.addCallbackParam("isRegistered", false);
            log.error("User update error: " + e.getMessage());
        }
        return "";
    }

    /**
     * Method for updating list of available users
     */
    public void updateUsers() {
        this.setUsers(userService.getAll());
    }

    /**
     * Method for registering new User entity
     * @return Redirect url
     */
    public String register() {
        RequestContext context = RequestContext.getCurrentInstance();
        try {
            PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
            this.user.setPassword(passwordEncoder.encode(this.user.getPassword()));
            userService.register(user);
            context.addCallbackParam("isRegistered", true);
            Util.addSimpleMessage("Registration successful " + this.user.getName());
            this.setUser(new User());
            log.info("User:" + user.getEmail() + " registered");
        } catch (Exception e) {
            Util.addSimpleMessage("Registration failed " + this.user.getName());
            context.addCallbackParam("isRegistered", false);
            log.error("User register error: " + e.getMessage());
        }
        return "";
    }

    /**
     * Method for getting user from DB by it's ID
     * @param id ID of desired user
     * @return Redirect url
     */
    public String getUserById(int id) {
        this.user = userService.findByPrimaryKey(id);
        return "";
    }

    /**
     * Method for performing login action
     * @return redirect url
     */
    public String login() {
        RequestContext context = RequestContext.getCurrentInstance();
        try {
            Authentication authentication = authenticationManager
                    .authenticate(new UsernamePasswordAuthenticationToken(
                            this.user.getEmail(), this.user.getPassword()));

            SecurityContextHolder.getContext()
                    .setAuthentication(authentication);
            this.user = userService.check(this.user);
            log.info("User:" + user.getId() + " logged in");
        } catch (AuthenticationException ex) {
            Util.addParamtrizedMessage(FacesMessage.SEVERITY_WARN, "Login Error", "Invalid credentials!");
            context.addCallbackParam("loggedIn", false);
            log.error("User login error: " + ex.getMessage());
            return "";
        }
        context.addCallbackParam("loggedIn", true);
//TODO CHECK THIS!
        if (Util.getSavedUrl() != "") {
            return Util.getSavedUrl() + "?faces-redirect=true";
        } else {
            return "/index.xhtml?faces-redirect=true";
        }
    }

    /**
     * Method for findinguser by it's email
     * @param email Email of user in DB
     * @return Specified details of detected user
     * @throws UsernameNotFoundException User with email wasn't found
     */
    @Override
    public UserDetails loadUserByUsername(String email)
            throws UsernameNotFoundException {
        this.setUser(new User());
        this.user.setEmail(email);
        UserDetails userDetails = null;
        try {
            com.scheduler.data.User usr = userService.check(this.user);
            if (usr != null) {
                this.setUser(usr);
                userDetails = new org.springframework.security.core.userdetails.User(this.getUser().getEmail(), this.getUser().getPassword(), true, true, true, true, new ArrayList<GrantedAuthority>(Arrays.asList(new GrantedAuthorityImpl("ROLE_ADMIN"))));
            }
        } catch (Exception ex) {
            log.error("Error during getting user details: " + ex);
            return null;
        }
        return userDetails;
    }
}
