package com.freedom.financial.work.distribution.repository;

import com.freedom.financial.work.distribution.entity.Agent;
import com.freedom.financial.work.distribution.entity.SkillSet;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.ArrayList;
import java.util.List;

import static com.freedom.financial.work.distribution.entity.SkillSet.*;
import static java.util.Arrays.asList;

@Repository
public class AgentRepository {
    private final List<Agent> agentList = new ArrayList<>();

    @PostConstruct
    public void setUpAgents() {
        agentList.add(new Agent(1 , asList(SKILL_1, SKILL_2)));
        agentList.add(new Agent(2 , asList(SKILL_2, SKILL_3)));
        agentList.add(new Agent(3 , asList(SKILL_1, SKILL_3)));
    }

    public Agent findAgentById(final int id) {
        return agentList.stream().filter(s -> s.getId() == id).findFirst().orElse(null);
    }

    public List<Agent> findAll() {
        return this.agentList;
    }

    public List<Agent> findBySkillSet(final List<SkillSet> skillSetList) {
        final List<Agent> filterAgentList = new ArrayList<>();
        agentList.forEach(s -> {
           List<SkillSet> agentSkillSetList =  s.getSkillSetList();
           agentSkillSetList.forEach(skillSet -> {
               if (skillSetList.contains(skillSet)) {
                   filterAgentList.add(s);
               }
           });
         });
        return filterAgentList;
    }


    @PreDestroy
    public void destroy() {
        agentList.clear();
    }
}
