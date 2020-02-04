package com.freedom.financial.work.distribution.repository;

import com.freedom.financial.work.distribution.entity.Agent;
import com.freedom.financial.work.distribution.entity.SkillSet;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.ArrayList;
import java.util.List;

import static com.freedom.financial.work.distribution.entity.SkillSet.*;
import static java.util.Arrays.asList;

@Repository
public interface AgentRepository extends CrudRepository<Agent,Integer> {
    List<Agent> findAgentBySkillSetsContaining(final String skillSets);
}
