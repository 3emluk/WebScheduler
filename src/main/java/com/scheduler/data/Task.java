package com.scheduler.data;

import javax.persistence.*;
import java.util.Date;

/**
 * Class for representation persistence of Task entity
 */
@Entity
@Table(name = "task")
public class Task {
    @Id
    @Column(name = "id")
    private int id;
    @Column(name = "start")
    private Date from;
    @Column(name = "end")
    private Date to;
    @Column(name = "date")
    private Date date;
    @Column(name = "location")
    private String location;
    @Column(name = "isDone")
    private boolean isDone;
    @ManyToOne(optional = false)
    @JoinColumn(name = "idType", referencedColumnName = "id")
    private TaskType taskType;
    @ManyToOne(optional = false)
    @JoinColumn(name = "idUser", referencedColumnName = "id")
    private User user;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getFrom() {
        return from;
    }

    public void setFrom(Date from) {
        this.from = from;
    }

    public Date getTo() {
        return to;
    }

    public void setTo(Date to) {
        this.to = to;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public boolean isDone() {
        return isDone;
    }

    public boolean getIsDone() {
        return isDone;
    }

    public void setIsDone(boolean isDone) {
        this.isDone = isDone;
    }

    public TaskType getTaskType() {
        return taskType;
    }

    public void setTaskType(TaskType taskType) {
        this.taskType = taskType;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
