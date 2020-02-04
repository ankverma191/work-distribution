package com.freedom.financial.work.distribution.engine.engines;

import com.freedom.financial.work.distribution.engine.RuleEngine;
import com.freedom.financial.work.distribution.entity.Agent;
import com.freedom.financial.work.distribution.entity.Priority;
import com.freedom.financial.work.distribution.entity.Status;
import com.freedom.financial.work.distribution.entity.Task;
import com.freedom.financial.work.distribution.exception.NoAgentFoundException;
import com.freedom.financial.work.distribution.repository.AgentRepository;
import com.freedom.financial.work.distribution.repository.TaskRepository;
import com.google.common.collect.ImmutableList;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component("taskValidator")
public class TaskRuleEngine implements RuleEngine<Task, NoAgentFoundException, Integer> {

    private final AgentRepository agentRepository;
    private final TaskRepository taskRepository;

    public TaskRuleEngine(final AgentRepository agentRepository, final TaskRepository taskRepository) {
        this.agentRepository = agentRepository;
        this.taskRepository = taskRepository;
    }

    @Override
    public Integer execute(final Task task) throws NoAgentFoundException {
        final List<Agent> agentList = sortByLocalDateTime(sortByTask(filterBasedOnPriority(task, findAgentBasedSkillSet(task))));
        if (CollectionUtils.isEmpty(agentList)) {
            throw new NoAgentFoundException("Task Id [ " + task.getTaskId() + " ] cannot be assigned");
        }
        return agentList.get(0).getId();
    }

    /**
     * This will find the agents based on the skill set of the task
     * @param task
     * @return List<Agent>
     */
    private List<Agent> findAgentBasedSkillSet(final Task task) {
        return agentRepository.findAgentBySkillSetsContaining(task.getSkillSetList());
    }

    /**
     * Filter agents based on the priority
     * @param task
     * @param agentList
     * @return List<Agent>
     */
    private List<Agent> filterBasedOnPriority(final Task task, final List<Agent> agentList) {
        final List<Agent> aList = new ArrayList<>();
        for (Agent agent : agentList) {
            List<Task> tempTaskList = taskRepository.findTasksByAgentIdAndStatusIn(agent.getId(), Status.PENDING.name(), Status.STARTED.name());
            if (CollectionUtils.isEmpty(tempTaskList)) {
                aList.add(agent);
            } else {
                tempTaskList.forEach(t -> {
                    if (t.getPriority(t.getPriority()).getValue() < task.getPriority(task.getPriority()).getValue()) {
                        aList.add(agent);
                    }
                });
            }
        }
        return aList;
    }


    private List<Agent> sortByTask(final List<Agent> agentList) {
        if (validateSize(agentList.size())) {
            return agentList;
        }
        Map<String, List<Agent>> agentMap = new HashMap<>();
        agentList.forEach(a -> {
            List<Task> taskList = taskRepository.findTasksByAgentIdAndStatusIn(a.getId(), Status.PENDING.name(), Status.STARTED.name());
            if (CollectionUtils.isEmpty(taskList)) {
                buildAgentMap(agentMap,"n" , a );
            } else {
                taskList.forEach(task -> buildAgentMap(agentMap , task != null && task.getAgentId() != null ? "a" : "n" , a));
            }
        });
        return ImmutableList.of(agentMap.get("n") , agentMap.get("a")).asList().get(0);
    }

    private List<Agent> sortByLocalDateTime(final List<Agent> agents) {
        if (validateSize(agents.size())) {
            return agents;
        }
        final List<Agent> agentList = new ArrayList<>();
        agents.forEach(a -> {
            List<Task> taskList = taskRepository.findTasksByAgentIdAndPriorityAndStatusInOrderByTimestamp(a.getId(),
                    Priority.LOW.name(), Status.PENDING.name(), Status.STARTED.name());
            if (CollectionUtils.isEmpty(taskList)) {
                agentList.add(a);
            } else {
                taskList.forEach(t -> {
                    if (t.getPriority(t.getPriority()) == Priority.LOW) {
                        agentList.add(a);
                    }
                });
            }
        });
        return agentList;
    }


    private void buildAgentMap(final Map<String, List<Agent>> agentMap ,final String code  ,final Agent agent) {
        List<Agent> tempList = agentMap.get(code);
        if (tempList == null) {
            tempList = new ArrayList<>();
        }
        tempList.add(agent);
        agentMap.put(code, tempList);
    }

    private boolean validateSize(int size) {
        return size == 0 || size == 1;
    }
}
