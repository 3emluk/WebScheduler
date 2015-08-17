package com.scheduler.beans;

import com.scheduler.Util;
import com.scheduler.data.TaskType;
import com.scheduler.service.TaskTypeService;
import org.apache.log4j.Logger;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import java.io.Serializable;
import java.util.List;

/**
 * Class for performing actions with Task type entity
 */
@ManagedBean
@SessionScoped
public class TaskTypeBean implements Serializable {

    /**
     * Stores a service to work with task types from DB
     */
    @ManagedProperty("#{taskTypeService}")
    private TaskTypeService taskTypeService;
    /**
     * List of currently available task types
     */
    private List<TaskType> taskTypes;

    /**
     * Currently task type to work with
     */
    private TaskType taskType = new TaskType();

    /**
     * Log4j log variable
     */
    private Logger log = Logger.getLogger(this.getClass().getName());

    /**
     * Log4j log variable
     */
    public List<TaskType> getTaskTypes() {
        return taskTypes;
    }

    public void setTaskTypes(List<TaskType> taskTypes) {
        this.taskTypes = taskTypes;
    }

    public TaskType getTaskType() {
        return taskType;
    }

    public void setTaskType(TaskType taskType) {
        this.taskType = taskType;
    }

    public TaskTypeService getTaskTypeService() {
        return taskTypeService;
    }

    public void setTaskTypeService(TaskTypeService taskTypeService) {
        this.taskTypeService = taskTypeService;
    }

    /**
     * Updating list of all available task types
     */
    public void updateTaskTypes() {
        this.setTaskTypes(taskTypeService.getAll());
    }

    /**
     * Method for adding new task type
     * @return Redirect url
     */
    public String add() {
        taskTypeService.add(taskType);
        Util.addSimpleMessage("Task type added");
        log.info("Task type added " + taskType.getType());
        return "";
    }

    /**
     * Method for getting task type by id
     * @param id ID of task type
     * @return  Redirect url
     */
    public String getTaskTypeById(int id) {
        this.taskType = taskTypeService.findByPrimaryKey(id);
        return "";
    }
}
