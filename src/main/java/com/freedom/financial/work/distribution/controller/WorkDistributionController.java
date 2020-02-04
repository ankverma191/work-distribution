package com.freedom.financial.work.distribution.controller;

import com.freedom.financial.work.distribution.aspect.LogExecutionTime;
import com.freedom.financial.work.distribution.entity.Agent;
import com.freedom.financial.work.distribution.entity.Task;
import com.freedom.financial.work.distribution.exception.NoAgentFoundException;
import com.freedom.financial.work.distribution.exception.NoDataFoundException;
import com.freedom.financial.work.distribution.request.TaskRequest;
import com.freedom.financial.work.distribution.request.TaskUpdateRequest;
import com.freedom.financial.work.distribution.service.WorkDistributionService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping("/work/distribution")
@ApiOperation("Api pertaining to work distribution")
public class WorkDistributionController {

    private final WorkDistributionService workDistributionService;

    public WorkDistributionController(final WorkDistributionService workDistributionService) {
        this.workDistributionService = workDistributionService;
    }

    @ApiOperation("Create the task based on the task request and will return task with associated agent")
    @ApiResponses( {
            @ApiResponse(code = 201 , message = "Agent has been assigned successfully"),
            @ApiResponse(code = 404 , message = "No suitable agent has been found")
        }
    )
    @LogExecutionTime()
    @PostMapping(consumes = APPLICATION_JSON_VALUE , produces = APPLICATION_JSON_VALUE)
    public
    @ResponseBody
    ResponseEntity<Task> createTask(@RequestBody final TaskRequest taskRequest) throws NoAgentFoundException {
        return new ResponseEntity<>(workDistributionService.createTask(taskRequest), HttpStatus.CREATED);
    }

    @ApiOperation("Update the task based on the task id")
    @ApiResponses( {
            @ApiResponse(code = 200 , message = "Task has been assigned successfully updated"),
            @ApiResponse(code = 404 , message = "No task is found for the ID provided")
    }
    )
    @LogExecutionTime()
    @PutMapping(consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    public
    @ResponseBody
    ResponseEntity<Task> updateTaskStatus(@RequestBody final TaskUpdateRequest taskUpdateRequest) throws NoDataFoundException {
        return new ResponseEntity<>(workDistributionService.updateTaskStatus(taskUpdateRequest.getTaskId()), HttpStatus.OK);
    }

    @ApiOperation("Get all the agents with there assigned task")
    @ApiResponses( {
            @ApiResponse(code = 200 , message = "details of the agents"),
    }
    )
    @LogExecutionTime()
    @GetMapping(value = "/agentList" , produces = APPLICATION_JSON_VALUE)
    public @ResponseBody ResponseEntity<Map<Integer, List<Task>>> getAllAgents() {
        return new ResponseEntity<>(workDistributionService.getAllAgents() , HttpStatus.OK);
    }
}
