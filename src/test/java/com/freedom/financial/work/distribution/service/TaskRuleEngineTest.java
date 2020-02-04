package com.freedom.financial.work.distribution.service;

import com.freedom.financial.work.distribution.engine.engines.TaskRuleEngine;
import com.freedom.financial.work.distribution.entity.Agent;
import com.freedom.financial.work.distribution.entity.Priority;
import com.freedom.financial.work.distribution.entity.SkillSet;
import com.freedom.financial.work.distribution.entity.Task;
import com.freedom.financial.work.distribution.repository.AgentRepository;
import com.freedom.financial.work.distribution.repository.TaskRepository;
import com.freedom.financial.work.distribution.request.TaskRequest;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@RunWith(MockitoJUnitRunner.class)
public class TaskRuleEngineTest {

    private TaskRuleEngine taskRuleEngine;

    @Mock
    private TaskRepository taskRepository;

    @Mock
    private AgentRepository agentRepository;

    @Before
    public void setUp() {
        final Agent a1 = new Agent();
        a1.setId(1);
        a1.setSkillSets("SKILL_1, SKILL_2");

        final Agent a2 = new Agent();
        a2.setId(2);
        a2.setSkillSets("SKILL_1");

        final List<Agent> agentList = new ArrayList<>();
        agentList.add(a1);
        agentList.add(a2);

        taskRuleEngine = new TaskRuleEngine(agentRepository, taskRepository);
        Mockito.when(agentRepository.findAgentBySkillSetsContaining("SKILL_1")).thenReturn(agentList);
    }

    @Test
    public void execute() {
        TaskRequest taskRequest = new TaskRequest();
        taskRequest.setPriority(Priority.LOW);
        taskRequest.setSkills(Collections.singletonList(SkillSet.SKILL_1));

        Task task = Task.build(taskRequest);
        taskRuleEngine.execute(task);
    }
}
