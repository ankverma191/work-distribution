package com.freedom.financial.work.distribution.entity;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class  ErrorModel {
    private String message;
    private String traceId;
    private String method;
}
