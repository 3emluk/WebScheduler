package com.scheduler.service;

import com.scheduler.data.User;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;

/**
 * Repository to perform DB actions with User entities
 */
@Repository
public class UserService {

    /**
     * Link to interact with entities at DB
     */
    @PersistenceContext
    private EntityManager em;

    /**
     * Log4j log variable
     */
    private Logger log = Logger.getLogger(this.getClass().getName());

    public EntityManager getEm() {
        return em;
    }

    public void setEm(EntityManager em) {
        this.em = em;
    }

    /**
     *  Method for adding new User entity
     * @param user User which must be added
     */
    @Transactional
    public void register(User user) {
        this.em.persist(user);
    }

    /**
     * Method for deleting desired User entity from DB
     * @param user User which must be deleted
     */
    @Transactional
    public void delete(User user) {
        User userToRemove = em.getReference(User.class, user.getId());
        this.em.remove(userToRemove);
    }

    /**
     * Method for finding User entity by ID
     * @param id ID of User entity
     * @return Detected User entity
     */
    public User findByPrimaryKey(int id) {
        if (em.find(User.class, id) != null)
            return ((User) em.find(User.class, id));
        else
            return null;

    }

    /**
     * Method for updating desired User entity
     * @param user User which must be updated
     * @return  Status of update operation
     */
    @Transactional
    public String update(User user) {
        User usr = (User) em.find(User.class, user.getId());
        if (usr != null) {
            usr.setEmail(user.getEmail());
            usr.setName(user.getName());
            usr.setPassword(user.getPassword());
            usr.setPhone(user.getPhone());
            return "entity updated successfully";
            //Note managre.merge method is not called. But Product will be synchronised with the DB
        } else
            return "Entity does not exist ";
    }

    /**
     * Method for getting list of User entities available in DB     *
     * @return List of available User entities
     */
    public List<User> getAll() {
        Query query = em.createQuery("select u from User u ");
        return query.getResultList();
    }

    /**
     * Method for getting User entity by it's email field
     * @param user User entity with required fields
     * @return Founded User entity
     */
    public User check(User user) {
        log.info("Check started");
        Query query = em.createQuery("select u from User u  where  u.email=:email");
        query.setParameter("email", user.getEmail());
        List<User> userList = query.getResultList();
        log.info("User list size" + userList.size());
        User usr = userList.get(0);
        return usr;
    }

}
