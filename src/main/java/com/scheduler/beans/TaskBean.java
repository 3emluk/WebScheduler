package com.scheduler.beans;

import com.scheduler.Util;
import com.scheduler.data.Task;
import com.scheduler.service.TaskService;
import org.apache.log4j.Logger;
import org.primefaces.context.RequestContext;
import org.primefaces.event.RowEditEvent;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import java.io.Serializable;
import java.util.List;

/**
 * Class for performing actions with task to particular user
 */
@ManagedBean
@SessionScoped
public class TaskBean implements Serializable {

    /**
     * Stores a link to service to work with tasks from DB
     */
    @ManagedProperty("#{taskService}")
    private TaskService taskService;


    /**
     * Stores a link to service to work with tasks
     */
    @ManagedProperty("#{userBean}")
    private UserBean userBean;

    /**
     * Storing list of wired tasks
     */
    private List<Task> tasks;

    /**
     * Storing current active task
     */
    private Task task = new Task();

    /**
     * Log4j log variable
     */
    private Logger log = Logger.getLogger(this.getClass().getName());

    public List<Task> getTasks() {
        return tasks;
    }

    public void setTasks(List<Task> tasks) {
        this.tasks = tasks;
    }

    public Task getTask() {
        return task;
    }

    public void setTask(Task task) {
        this.task = task;
    }

    public TaskService getTaskService() {
        return taskService;
    }

    public void setTaskService(TaskService taskService) {
        this.taskService = taskService;
    }

    public UserBean getUserBean() {
        return userBean;
    }

    public void setUserBean(UserBean userBean) {
        this.userBean = userBean;
    }

    /**
     * Method for updating existing tasks
     */
    public void updateTasks() {
        this.setTasks(taskService.getAll(userBean.getUser()));
    }

    /**
     * Adding new task to defined user
     *
     * @return Redirect location
     */
    public String add() {
        RequestContext context = RequestContext.getCurrentInstance();
        try {
            task.setUser(userBean.getUser());
            taskService.add(task);
            context.addCallbackParam("isTaskCreated", true);
            Util.addParamtrizedMessage(FacesMessage.SEVERITY_INFO, "Task created", "");
            this.setNull();
            log.info("Task for user " + userBean.getUser().getId() + " added.");
        } catch (Exception e) {
            Util.addParamtrizedMessage(FacesMessage.SEVERITY_ERROR, "Task doesn't created", "Shit happens:D!");
            context.addCallbackParam("isTaskCreated", false);
            log.error("Error creating task for user " + userBean.getUser().getId() + ". " + e.getMessage());
        }
        return "";
    }

    /**
     * Event triggered on row modification at task table
     *
     * @param event triggered event information
     */
    public void onRowEdit(RowEditEvent event) {
        taskService.update((Task) event.getObject());
        Util.addSimpleMessage("Task Updated");
    }

    /**
     * Event triggered on row deleting from task table
     *
     * @param event triggered event information
     */
    public void onRowCancel(RowEditEvent event) {
        Util.addSimpleMessage("Task Edit canceled");
    }

    /**
     * Clearing current task
     */
    public void setNull() {
        task = new Task();
    }

    /**
     * Method for deleting task from user
     * @param t Task which will be deleted
     * @return  Redirect url
     */
    public String delete(Task t) {
        this.taskService.delete(t);
        Util.addSimpleMessage("Task Deleted");
        log.info("Task:" + t.getId() + " deleted.");
        return "";
    }

    /**
     * Method for updating chosen task
     * @return Redirect url
     */
    public String update() {
        taskService.update(task);
        Util.addSimpleMessage("Task Updated!");
        log.info("Task:" + task.getId() + " updated.");
        return "";
    }
}
