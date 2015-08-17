package com.scheduler.service;

import com.scheduler.data.Task;
import com.scheduler.data.User;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;

/**
 * Repository to perform DB actions with Task entities
 */
@Repository
public class TaskService {
    /**
     * Link to interact with entities at DB
     */
    @PersistenceContext
    private EntityManager em;

    public EntityManager getEm() {
        return em;
    }

    public void setEm(EntityManager em) {
        this.em = em;
    }

    /**
     * Method for adding new Task entity
     * @param task Task which must be added
     */
    @Transactional
    public void add(Task task) {
        this.em.persist(task);
    }

    /**
     * Method for deleting desired Task entity from DB
     * @param task Task which must be deleted
     */
    @Transactional
    public void delete(Task task) {
        Task taskToRemove = em.getReference(Task.class, task.getId());
        this.em.remove(taskToRemove);
    }

    /**
     * Method for finding Task entity by ID
     * @param id ID of Task entity
     * @return Detected Task entity
     */
    public Task findByPrimaryKey(int id) {
        if (em.find(Task.class, id) != null)
            return ((Task) em.find(Task.class, id));
        else
            return null;

    }

    /**
     * Method for updating desired entity
     * @param task Task which must be updated
     * @return Status of update operation
     */
    @Transactional
    public String update(Task task) {
        Task tsk = (Task) em.find(Task.class, task.getId());
        if (task != null) {
            tsk.setDate(task.getDate());
            tsk.setFrom(task.getFrom());
            tsk.setTo(task.getTo());
            tsk.setLocation(task.getLocation());
            tsk.setIsDone(task.isDone());
            tsk.setTaskType(task.getTaskType());
            return "entity updated successfully";
        } else
            return "Entity does not exist ";
    }

    /**
     * Method for getting list of Task entities available to particular user
     * @param user Chosen user
     * @return List of wired Task entities
     */
    public List<Task> getAll(User user) {
        int id = user.getId();
        Query query = em.createQuery("select a from Task a  where a.user=:user ");
        query.setParameter("user", user);
        return query.getResultList();
    }
}
