package com.freedom.financial.work.distribution.service.impl;

import com.freedom.financial.work.distribution.engine.engines.TaskRuleEngine;
import com.freedom.financial.work.distribution.entity.Status;
import com.freedom.financial.work.distribution.entity.Task;
import com.freedom.financial.work.distribution.exception.NoAgentFoundException;
import com.freedom.financial.work.distribution.exception.NoDataFoundException;
import com.freedom.financial.work.distribution.repository.TaskRepository;
import com.freedom.financial.work.distribution.request.TaskRequest;
import com.freedom.financial.work.distribution.service.WorkDistributionService;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class WorkDistributionServiceImpl implements WorkDistributionService {

    private final TaskRuleEngine taskRuleEngine;
    private final TaskRepository taskRepository;

    public WorkDistributionServiceImpl(final TaskRuleEngine taskRuleEngine , final TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
        this.taskRuleEngine = taskRuleEngine;
    }

    @Override
    public Task createTask(final TaskRequest taskRequest) throws NoAgentFoundException {
        final Task task = Task.build(taskRequest);
        task.setAgentId(taskRuleEngine.execute(task));
        return taskRepository.save(task);
    }

    @Override
    public Task updateTaskStatus(final String taskId) throws NoDataFoundException {
        final Task task = taskRepository.findById(taskId).
                orElseThrow(() -> new NoDataFoundException("No Data Found for Task Id [ " + taskId  + " ] "));
        task.setStatus(Status.COMPLETED.name());
        return taskRepository.save(task);
    }

    @Override
    public Map<Integer, List<Task>> getAllAgents() {
        final Map<Integer, List<Task>> taskMap = new HashMap<>();
        final List<Task> tasks = taskRepository.findTasksByStatusIn(Status.PENDING.name(), Status.STARTED.name());
         tasks.forEach(task -> {
            final int agent =  task.getAgentId();
                List<Task> tasksList = taskMap.get(agent);
                 if (CollectionUtils.isEmpty(tasksList)) {
                      tasksList = new ArrayList<>();
               }
                  tasksList.add(task);
                  taskMap.put(agent, tasksList);
        });
         return taskMap;
    }
}
