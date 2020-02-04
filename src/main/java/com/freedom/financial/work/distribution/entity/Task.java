package com.freedom.financial.work.distribution.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.freedom.financial.work.distribution.request.TaskRequest;
import com.google.common.base.Joiner;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(value = { "localDateTime" , "skillSetList"})
@Entity
@Table(name = "TASK")
public class Task {
    @Id
    private String taskId = null;
    @NonNull
    @Column(name = "skill_Set")
    private String skillSetList = null;
    @Column(name = "agent_Id")
    private Integer agentId = null;
    @NonNull
    @Column(name = "priority")
    private String priority = null;
    @Column(name = "status")
    private String status = null;
    @Column(name = "task_Date")
    private Timestamp timestamp = null;

    public Priority getPriority(final String priority) {
        return Priority.valueOf(priority);
    }

    public static Task build(TaskRequest taskRequest) {
        Task task = new Task();
        task.setTaskId(UUID.randomUUID().toString());
        task.setSkillSetList(Joiner.on(",").join(taskRequest.getSkills()));
        task.setPriority(taskRequest.getPriority().name());
        task.setTimestamp(Timestamp.valueOf(LocalDateTime.now()));
        task.setStatus(Status.STARTED.name());
        return task;
    }
}
