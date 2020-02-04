package com.freedom.financial.work.distribution.service;


import com.freedom.financial.work.distribution.entity.Task;
import com.freedom.financial.work.distribution.exception.NoAgentFoundException;
import com.freedom.financial.work.distribution.exception.NoDataFoundException;
import com.freedom.financial.work.distribution.request.TaskRequest;

import java.util.List;
import java.util.Map;

public interface WorkDistributionService {
    Task createTask(TaskRequest taskRequest) throws NoAgentFoundException;
    Task updateTaskStatus(final String taskId) throws NoDataFoundException;
    Map<Integer, List<Task>>  getAllAgents();
}
