package com.scheduler.beans;

import com.scheduler.data.TaskType;
import com.scheduler.service.TaskTypeService;
import org.apache.log4j.Logger;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import javax.faces.convert.FacesConverter;

/**
 * Class for performing String to TaskType conversion and vice versa
 */
@ManagedBean(name = "taskTypeConverterBean")
@FacesConverter(value = "taskTypeConverter")
public class TaskTypeConverter implements Converter {

    /**
     * Stores a service to work with task types from DB
     */
    @ManagedProperty("#{taskTypeService}")
    private TaskTypeService taskTypeService;

    /**
     * Log4j log variable
     */
    private Logger log = Logger.getLogger(this.getClass().getName());

    public TaskTypeService getTaskTypeService() {
        return taskTypeService;
    }

    public void setTaskTypeService(TaskTypeService taskTypeService) {
        this.taskTypeService = taskTypeService;
    }

    /**
     * Method for getting object from String value of TaskType
     * @param context Current context
     * @param component Component which sends value
     * @param submittedValue Submitted value form component
     * @return Converted object
     */
    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String submittedValue) {
        if (submittedValue == null || submittedValue.isEmpty()) {
            return null;
        }
        try {
            return taskTypeService.findByPrimaryKey(Integer.valueOf(submittedValue));
        } catch (NumberFormatException e) {
            log.error("is not a valid TaskType ID: " + submittedValue);
            throw new ConverterException(new FacesMessage(String.format("%s is not a valid TaskType ID", submittedValue)), e);
        }
    }

    /**
     * Method for getting String representation of TaskType
     * @param context Current context
     * @param component Component which sends value
     * @param modelValue Received object
     * @return String representation of TaskType
     */
    @Override
    public String getAsString(FacesContext context, UIComponent component, Object modelValue) {
        if (modelValue == null) {
            return "";
        }

        if (modelValue instanceof TaskType) {
            return String.valueOf(((TaskType) modelValue).getId());
        } else {
            log.error("is not a valid TaskType: " + modelValue);
            throw new ConverterException(new FacesMessage(String.format("%s is not a valid TaskType", modelValue)));
        }
    }

}