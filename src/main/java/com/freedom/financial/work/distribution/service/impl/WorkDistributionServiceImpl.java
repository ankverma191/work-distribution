package com.freedom.financial.work.distribution.service.impl;

import com.freedom.financial.work.distribution.entity.Agent;
import com.freedom.financial.work.distribution.entity.Status;
import com.freedom.financial.work.distribution.entity.Task;
import com.freedom.financial.work.distribution.exception.NoAgentFoundException;
import com.freedom.financial.work.distribution.exception.NoDataFoundException;
import com.freedom.financial.work.distribution.repository.TaskRepository;
import com.freedom.financial.work.distribution.request.TaskRequest;
import com.freedom.financial.work.distribution.service.WorkDistributionService;
import com.freedom.financial.work.distribution.validation.validators.TaskValidator;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class WorkDistributionServiceImpl implements WorkDistributionService {

    private final TaskValidator taskValidator;
    private final TaskRepository taskRepository;

    public WorkDistributionServiceImpl(final TaskValidator taskValidator , final TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
        this.taskValidator = taskValidator;
    }

    @Override
    public Task createTask(final TaskRequest taskRequest) throws NoAgentFoundException {
        final Task task = Task.build(taskRequest);
        task.setAgentId(new Agent(taskValidator.validate(task)));
        return taskRepository.addTask(task);
    }

    @Override
    public Task updateTaskStatus(final String taskId) throws NoDataFoundException {
        final Task task = taskRepository.findTaskById(taskId).
                orElseThrow(() -> new NoDataFoundException("No Data Found for Task Id [ " + taskId  + " ] "));
        task.setStatus(Status.COMPLETED);
        return taskRepository.updateTask(task);
    }

    @Override
    public Map<Agent, List<Task>> getAllAgents() {
        final Map<Agent, List<Task>> taskMap = new HashMap<>();
        final List<Task> tasks = taskRepository.findAll();
        tasks.forEach(task -> {
           final Agent agent =  task.getAgentId();
           List<Task> tasksList = taskMap.get(agent);
           if (CollectionUtils.isEmpty(tasksList)) {
                tasksList = new ArrayList<>();
           }
           if (task.getStatus() != Status.COMPLETED) {
               tasksList.add(task);
           }
            taskMap.put(agent, tasksList);
        });
        return taskMap;
    }
}
