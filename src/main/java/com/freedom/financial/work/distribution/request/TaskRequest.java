package com.freedom.financial.work.distribution.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.freedom.financial.work.distribution.entity.Priority;
import com.freedom.financial.work.distribution.entity.SkillSet;
import lombok.Data;
import lombok.NonNull;

import javax.validation.constraints.NotEmpty;
import java.util.List;

@Data
public class TaskRequest {
    @NonNull
    @NotEmpty(message = "priority cannot be empty")
    @JsonProperty("priority")
    private Priority priority = null;

    @NonNull
    @NotEmpty(message = "skill set cannot be empty")
    @JsonProperty("skill")
    private List<SkillSet> skills = null;
}
