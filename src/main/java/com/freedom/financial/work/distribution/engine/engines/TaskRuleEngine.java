package com.freedom.financial.work.distribution.engine.engines;

import com.freedom.financial.work.distribution.entity.Agent;
import com.freedom.financial.work.distribution.entity.Priority;
import com.freedom.financial.work.distribution.entity.Status;
import com.freedom.financial.work.distribution.entity.Task;
import com.freedom.financial.work.distribution.exception.NoAgentFoundException;
import com.freedom.financial.work.distribution.repository.AgentRepository;
import com.freedom.financial.work.distribution.repository.TaskRepository;
import com.freedom.financial.work.distribution.engine.RuleEngine;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Component("taskValidator")
public class TaskRuleEngine implements RuleEngine<Task, NoAgentFoundException , Integer> {

    private final AgentRepository agentRepository;
    private final TaskRepository taskRepository;

    public TaskRuleEngine(final AgentRepository agentRepository , final TaskRepository taskRepository) {
        this.agentRepository = agentRepository;
        this.taskRepository = taskRepository;
    }


    @Override
    public Integer execute(final Task task) throws NoAgentFoundException {
        final List<Agent> agentList = sortByLocalDateTime(sortByTask(filterBasedOnPriority(task,findAgentBasedSkillSet(task))));
        if (CollectionUtils.isEmpty(agentList)) {
            throw new NoAgentFoundException("Task Id [ " + task.getTaskId() + " ] cannot be assigned");
        }
        return agentList.get(0).getId();
    }

    private List<Agent> findAgentBasedSkillSet(final Task task){
        return agentRepository.findAgentBySkillSetsContaining(task.getSkillSetList());
    }

    private List<Agent> filterBasedOnPriority(final Task task , final List<Agent> agentList) {
       final List<Agent> aList = new ArrayList<>();
        for (Agent agent : agentList) {
            List<Task> tempTaskList = taskRepository.findTasksByAgentIdAndStatusIn(agent.getId(), Status.PENDING.name() , Status.STARTED.name());
            if (tempTaskList == null || tempTaskList.isEmpty()) {
                aList.add(agent);
            } else  {
                tempTaskList.forEach( t -> {
                    if(t.getPriority(t.getPriority()).getValue() < task.getPriority(task.getPriority()).getValue()) {
                        aList.add(agent);
                    }
                });
            }
        }
        return aList;
   }


    private List<Agent> sortByTask(final List<Agent> agentList) {
        if (agentList.size() == 0 || agentList.size() == 1) {
            return agentList;
        }
        Map<String, List<Agent>> assignmentMap = new HashMap<>();
        agentList.forEach(s -> {
            List<Task> taskList = taskRepository.findTasksByAgentIdAndStatusIn(s.getId(), Status.PENDING.name() , Status.STARTED.name());
            if (CollectionUtils.isEmpty(taskList)) {
                List<Agent> tempList =  assignmentMap.get("n");
                if (tempList == null) {
                    tempList = new ArrayList<>();
                }
                tempList.add(s);
                assignmentMap.put("n" , tempList);
            } else {
                taskList.forEach(task -> {
                    List<Agent> tempList;
                    if (task!=null && task.getAgentId()!=null) {
                        tempList = assignmentMap.get("a");
                        if (tempList == null) {
                            tempList = new ArrayList<>();
                        }
                        tempList.add(s);
                        assignmentMap.put("a" , tempList);
                    } else {
                        tempList =  assignmentMap.get("n");
                        if (tempList == null) {
                            tempList = new ArrayList<>();
                        }
                        tempList.add(s);
                        assignmentMap.put("n" , tempList);
                    }
                });
            }
        });
        List<Agent> aList = new ArrayList<>();
        for (String s : assignmentMap.keySet()) {
            aList.addAll(assignmentMap.get(s));
        }
        return aList;
    }

    private List<Agent> sortByLocalDateTime(final List<Agent> agents) {
        if (agents.size() == 0 || agents.size() == 1) {
            return agents;
        }
        final List<Agent> agentList = new ArrayList<>();
        agents.forEach(a -> {
            List<Task> taskList = taskRepository.findTasksByAgentIdAndPriorityAndStatusInOrderByTimestamp(a.getId(),
                    Priority.LOW.name(), Status.PENDING.name() , Status.STARTED.name());
            if (taskList == null || taskList.isEmpty()) {
                agentList.add(a);
            } else {
                taskList.forEach(t -> {
                    if(t.getPriority(t.getPriority()) == Priority.LOW) {
                        agentList.add(a);
                    }
                });
            }
        });
        return agentList;
    }
}
