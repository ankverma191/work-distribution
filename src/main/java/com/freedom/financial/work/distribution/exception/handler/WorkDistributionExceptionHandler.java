package com.freedom.financial.work.distribution.exception.handler;

import brave.Tracer;
import com.freedom.financial.work.distribution.entity.ErrorModel;
import com.freedom.financial.work.distribution.exception.NoAgentFoundException;
import com.freedom.financial.work.distribution.exception.NoDataFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import static org.springframework.http.HttpStatus.NOT_FOUND;

@RestControllerAdvice
public class WorkDistributionExceptionHandler extends ResponseEntityExceptionHandler {

    private final Tracer tracer;

    public WorkDistributionExceptionHandler(final Tracer tracer) {
        this.tracer = tracer;
    }

    @ExceptionHandler(value = { NoDataFoundException.class , NoAgentFoundException.class })
    public @ResponseBody
    ResponseEntity<ErrorModel> handleDistributionException(RuntimeException ex, WebRequest request) {
        return new ResponseEntity<>(buildErrorModel(ex), NOT_FOUND);
    }

    private ErrorModel buildErrorModel(final Exception ex) {
        return ErrorModel.builder().
                traceId(tracer.currentSpan().context().traceIdString()).
                message(ex.getMessage()).
                method(ex.getStackTrace()[0].getMethodName()).
                className(ex.getStackTrace()[0].getClassName()).
                fileName(ex.getStackTrace()[0].getFileName()).
                lineNumber(ex.getStackTrace()[0].getLineNumber()).
                build();
    }
}
