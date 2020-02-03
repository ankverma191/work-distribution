package com.freedom.financial.work.distribution.validation.validators;

import com.freedom.financial.work.distribution.entity.Agent;
import com.freedom.financial.work.distribution.entity.Priority;
import com.freedom.financial.work.distribution.entity.Task;
import com.freedom.financial.work.distribution.exception.NoAgentFoundException;
import com.freedom.financial.work.distribution.repository.AgentRepository;
import com.freedom.financial.work.distribution.repository.TaskRepository;
import com.freedom.financial.work.distribution.validation.Validate;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class TaskValidator implements Validate<Task, NoAgentFoundException , Integer> {

    private final AgentRepository agentRepository;
    private final TaskRepository taskRepository;

    public TaskValidator(final AgentRepository agentRepository , final TaskRepository taskRepository) {
        this.agentRepository = agentRepository;
        this.taskRepository = taskRepository;
    }


    @Override
    public Integer validate(final Task task) throws NoAgentFoundException {
        final Agent agent  = sortByLocalDateTime(sortByTask(filterBasedOnPriority(task, findAgentBasedSkillSet(task))));
        if (agent == null ) {
            throw new NoAgentFoundException("Task Id [ " + task.getTaskId() + " ] cannot be assigned");
        }
        return agent.getId();
    }

    private List<Agent> findAgentBasedSkillSet(final Task task){
        return agentRepository.findBySkillSet( task.getSkillSetList());
    }

    private List<Agent> filterBasedOnPriority(final Task task , final List<Agent> agentList) {
        final List<Agent> aList = new ArrayList<>();
        for (Agent agent : agentList) {
            List<Task> tempTaskList = taskRepository.findTaskByAgentId(agent.getId());
            if (tempTaskList == null || tempTaskList.isEmpty()) {
                aList.add(agent);
            } else  {
                tempTaskList.forEach( t -> {
                    if(t.getPriority().getValue() < task.getPriority().getValue()) {
                        aList.add(agent);
                    }
                });
            }
        }
        return aList;
    }


    private List<Agent> sortByTask(final List<Agent> agentList) {
        if (CollectionUtils.isEmpty(agentList)) {
            return null;
        }
        Map<String, List<Agent>> assignmentMap = new HashMap<>();
        agentList.forEach(s -> {
            List<Task> taskList = taskRepository.findTaskByAgentId(s.getId());
            if (taskList == null || taskList.isEmpty()) {
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

    private Agent sortByLocalDateTime(final List<Agent> agents) {
        if (CollectionUtils.isEmpty(agents)) {
            return null;
        }

        final List<Agent> agentList = new ArrayList<>();
        agents.forEach(a -> {
            List<Task> taskList =  taskRepository.findTaskByAgentId(a.getId()) ;
            if (taskList == null || taskList.isEmpty()) {
                agentList.add(a);
            } else {
                taskList.forEach(t -> {
                    if(t.getPriority() == Priority.LOW) {
                        agentList.add(a);
                    }
                });
            }
        });

        if (agentList.size() == 1) {
            return agentList.get(0);
        } else {
            final List<Agent> aList = new ArrayList<>();
            final Map<LocalDateTime , Agent> tempMap = new HashMap<>();
            agentList.forEach(s -> {
                final List<Task> taskList = taskRepository.findTaskByAgentId(s.getId());
                if (taskList == null || taskList.isEmpty()) {
                    tempMap.put(LocalDateTime.now() , s);
                } else {
                    taskList.forEach(task -> {
                        tempMap.put(task.getLocalDateTime() , s);
                    });
                }
            });
            return new ArrayList<>(tempMap.entrySet().stream().sorted(Map.Entry.comparingByKey(Comparator.reverseOrder()))
                    .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue,
                            (oldValue, newValue) -> oldValue, LinkedHashMap::new)).
                            values()).get(0);
        }
    }
}
