package com.freedom.financial.work.distribution.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@Entity
@Table(name = "AGENTS")
public class Agent {
    @Id
    private Integer id = null;
    @Column(name = "skill_sets")
    private String skillSets = null;

    public List<SkillSet> getSkillSet() {
       return Arrays.asList(this.skillSets.split(",")).
               stream().map(m -> SkillSet.valueOf(m)).collect(Collectors.toList());
    }

    public Agent(final int id) {
        this.id = id;
    }
    @Override
    public String toString() {
        return "Agent (" + this.getId() + ")";
    }
}
