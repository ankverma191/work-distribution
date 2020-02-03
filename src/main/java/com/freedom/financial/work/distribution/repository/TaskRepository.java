package com.freedom.financial.work.distribution.repository;

import com.freedom.financial.work.distribution.entity.Agent;
import com.freedom.financial.work.distribution.entity.Priority;
import com.freedom.financial.work.distribution.entity.Status;
import com.freedom.financial.work.distribution.entity.Task;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.freedom.financial.work.distribution.entity.Priority.HIGH;
import static com.freedom.financial.work.distribution.entity.Priority.LOW;
import static com.freedom.financial.work.distribution.entity.SkillSet.*;
import static com.freedom.financial.work.distribution.entity.Status.COMPLETED;
import static com.freedom.financial.work.distribution.entity.Status.PENDING;

@Repository
public class TaskRepository {
    private final List<Task> taskList = new ArrayList<>();

    @PostConstruct
    public void setUpTasks() {
        taskList.add(new Task("1", Collections.singletonList(SKILL_1) , new Agent(1) , HIGH,COMPLETED , LocalDateTime.now()));
        taskList.add(new Task("2", Collections.singletonList(SKILL_2) , new Agent(2) , LOW, COMPLETED, LocalDateTime.now()));
        taskList.add(new Task("3", Collections.singletonList(SKILL_3) , new Agent(3) , LOW, COMPLETED, LocalDateTime.now()));
        taskList.add(new Task("4", Collections.singletonList(SKILL_3) , new Agent(3) , HIGH, PENDING, LocalDateTime.now()));
    }

    public Task addTask(final Task task) {
        taskList.add(task);
        return task;
    }

    public List<Task> findTaskListByStatus(final Status status) {
        return taskList.stream().filter(s -> s.getStatus() == status).collect(Collectors.toList());
    }

    public List<Task> findTaskListByPriority(final Priority priority) {
        return taskList.stream().filter(s -> s.getPriority() == priority).collect(Collectors.toList());
    }

    public Optional<Task> findTaskById(final String taskId) {
        return taskList.stream().filter(s -> taskId.equalsIgnoreCase(s.getTaskId())).findFirst();
    }

    public Task updateTask(Task task) {
        taskList.removeIf(s -> s.getTaskId().equalsIgnoreCase(task.getTaskId()));
        taskList.add(task);
        return task;
    }

    public List<Task> findAll() {
        return this.taskList;
    }

    public List<Task> findTaskByAgentId(int id) {
        return taskList.parallelStream().filter(s -> s.getAgentId().getId() == id).filter(s -> s.getStatus() != COMPLETED).collect(Collectors.toList());
    }

    @PreDestroy
    public void destroy() {
        taskList.clear();
    }

}
