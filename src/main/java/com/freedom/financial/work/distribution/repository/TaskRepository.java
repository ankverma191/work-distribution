package com.freedom.financial.work.distribution.repository;

import com.freedom.financial.work.distribution.entity.Agent;
import com.freedom.financial.work.distribution.entity.Priority;
import com.freedom.financial.work.distribution.entity.Status;
import com.freedom.financial.work.distribution.entity.Task;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.freedom.financial.work.distribution.entity.Priority.HIGH;
import static com.freedom.financial.work.distribution.entity.Priority.LOW;
import static com.freedom.financial.work.distribution.entity.SkillSet.*;
import static com.freedom.financial.work.distribution.entity.Status.COMPLETED;
import static com.freedom.financial.work.distribution.entity.Status.PENDING;

@Repository
public interface TaskRepository extends CrudRepository<Task, String> {
    List<Task> findTasksByStatusIn(final String ...status);
    List<Task> findTasksByAgentIdAndStatusIn(final Integer agentId, final String ...status);
    List<Task> findTasksByAgentIdAndPriorityAndStatusInOrderByTimestamp(final Integer agentId, final String priority, final String ...status);
}
