package com.freedom.financial.work.distribution.entity;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class  ErrorModel {
    private String message;
    private String traceId;
    private String method;
    @JsonProperty("class")
    private String className;
    private Integer lineNumber;
    private String fileName;
}
