package com.freedom.financial.work.distribution.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Agent {
    private int id = 0;
    private List<SkillSet> skillSetList = null;
    public Agent(final int id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "Agent (" + this.getId() + ")";
    }
}
