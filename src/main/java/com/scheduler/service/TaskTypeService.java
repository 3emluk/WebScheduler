package com.scheduler.service;

import com.scheduler.data.TaskType;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;

/**
 * Repository to perform DB actions with TaskType entities
 */
@Repository
public class TaskTypeService {
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
     * Method for adding new TaskType entity
     *
     * @param taskType TaskType which must be added
     */
    @Transactional
    public void add(TaskType taskType) {
        this.em.persist(taskType);
    }

    /**
     * Method for deleting desired TaskType entity from DB
     *
     * @param taskType TaskType which must be deleted
     */
    @Transactional
    public void delete(TaskType taskType) {
        this.em.remove(taskType);
    }

    /**
     * Method for finding TaskType entity by ID
     *
     * @param id ID of TaskType entity
     * @return Detected TaskType entity
     */
    public TaskType findByPrimaryKey(int id) {
        if (em.find(TaskType.class, id) != null)
            return ((TaskType) em.find(TaskType.class, id));
        else
            return null;
    }

    /**
     * Method for getting list of TaskType entities available in DB
     * @return List of TaskType entities
     */
    public List<TaskType> getAll() {
        Query query = em.createQuery("select a from TaskType a");
        return query.getResultList();
    }
}
