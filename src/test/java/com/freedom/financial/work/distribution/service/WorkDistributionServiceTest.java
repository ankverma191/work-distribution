package com.freedom.financial.work.distribution.service;

import com.freedom.financial.work.distribution.engine.engines.TaskRuleEngine;
import com.freedom.financial.work.distribution.entity.Priority;
import com.freedom.financial.work.distribution.entity.SkillSet;
import com.freedom.financial.work.distribution.entity.Status;
import com.freedom.financial.work.distribution.entity.Task;
import com.freedom.financial.work.distribution.repository.TaskRepository;
import com.freedom.financial.work.distribution.request.TaskRequest;
import com.freedom.financial.work.distribution.service.impl.WorkDistributionServiceImpl;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.*;
import org.mockito.junit.MockitoJUnitRunner;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.*;

@RunWith(MockitoJUnitRunner.class)
public class WorkDistributionServiceTest {

    private WorkDistributionService workDistributionService;

    @Mock
    private TaskRepository taskRepository;

    @Mock
    private TaskRuleEngine taskRuleEngine;

    @Before
    public void setUp() {
        Task inputTask  = new Task();
        inputTask.setTaskId(UUID.randomUUID().toString());
        inputTask.setAgentId(1);
        inputTask.setPriority(Priority.LOW.name());
        inputTask.setSkillSetList("SKILL_1");
        inputTask.setStatus(Status.PENDING.name());
        inputTask.setTimestamp(Timestamp.valueOf(LocalDateTime.now()));

        workDistributionService = new WorkDistributionServiceImpl(taskRuleEngine , taskRepository);

        Mockito.when(taskRepository.findById("1")).thenReturn(Optional.of(inputTask));
        Mockito.when(taskRepository.findTasksByStatusIn(Status.PENDING.name(), Status.STARTED.name())).thenReturn(Collections.singletonList(inputTask));




        Task outputTask  = new Task();
        outputTask.setTaskId(UUID.randomUUID().toString());
        outputTask.setAgentId(1);
        outputTask.setPriority(Priority.LOW.name());
        outputTask.setSkillSetList("SKILL_1");
        outputTask.setStatus(Status.COMPLETED.name());
        outputTask.setTimestamp(Timestamp.valueOf(LocalDateTime.now()));

        Mockito.when(taskRepository.save(ArgumentMatchers.any(Task.class))).thenReturn(outputTask);
        Mockito.when(taskRuleEngine.execute(inputTask)).thenReturn(1);
    }

    @Test
    public void updateTask() {
       Task task =  workDistributionService.updateTaskStatus("1");
       Assert.assertEquals(task.getStatus() , Status.COMPLETED.name());
    }

    @Test
    public void createTask() {
        TaskRequest taskRequest = new TaskRequest();
        taskRequest.setPriority(Priority.LOW);
        taskRequest.setSkills(Collections.singletonList(SkillSet.SKILL_2));
        Task task = workDistributionService.createTask(taskRequest);
        Assert.assertEquals(task.getAgentId() , Integer.valueOf("1"));
    }

    @Test
    public void getAllAgents() {
        final Map<Integer, List<Task>> taskMap = workDistributionService.getAllAgents();
        Assert.assertEquals(taskMap.size() , 1);
        final List<Task> taskList = taskMap.get(1);
        Assert.assertEquals(taskList.size() , 1);

        final Task task = taskList.get(0);
        Assert.assertNotNull(task.getTaskId());
        Assert.assertEquals(task.getPriority() , "LOW");
        Assert.assertEquals(task.getStatus(), "PENDING");
    }
}
