package com.freedom.financial.work.distribution.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.freedom.financial.work.distribution.request.TaskRequest;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(value = { "localDateTime" , "skillSetList"})
public class Task {
    private String taskId = null;
    @NonNull
    private List<SkillSet> skillSetList = null;
    private Agent agentId = null;
    @NonNull
    private Priority priority = null;
    private Status status = null;
    private LocalDateTime localDateTime = null;

    public static Task build(TaskRequest taskRequest) {
        Task task = new Task();
        task.setTaskId(UUID.randomUUID().toString());
        task.setSkillSetList(taskRequest.getSkills());
        task.setPriority(taskRequest.getPriority());
        task.setLocalDateTime(LocalDateTime.now());
        task.setStatus(Status.NEW);
        return task;
    }
}
