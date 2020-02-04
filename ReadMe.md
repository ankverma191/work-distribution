# Work Distribution APIs

You will build a work distribution service that can distribute tasks to agents

An agent is defined by a unique identifier, and a set of skills they possess.A task is defined by a unique identifier, a priority, a set of skills an agent needs to possess to handle that task and the agent currently assigned to that task, if any.

The system will assign the tasks to the agents using the following rules:

- The agent must possess at least all the skills required by the task
- An agent cannot be assigned a task if they’re already working on a task of equal or higher priority.
- The system will always prefer an agent that is not assigned any task to an agent already assigned to a task.
- If all agents are currently working on a lower priority task, the system will pick the agent that started working on his/her current task the most recently.
- If no agent is able to take the task, the service should return an error

**API Documentation**

| API                          | TYPE | REQUEST           | RESPONSE                 |
| ---------------------------- | ---- | ----------------- | ------------------------ |
| /work/distribution           | POST | TaskRequest       | Task                     |
| /work/distribution           | PUT  | TaskUpdateRequest | Task                     |
| /work/distribution/agentList | GET  |                   | Map<AgentId, List<Task>> |

**Error Responses**

| Error                 | Message                                  |
| --------------------- | ---------------------------------------- |
| NoDataFoundException  | No Data is found for the input task type |
| NoAgentFoundException | No Agent is found for the follwing Task. |

**Input Models** 

1. TaskRequest

    

   ```json
   {
     "priority": "LOW",
     "skill": [
       "SKILL_1"
     ]
   }
   ```

   

2. TaskUpdateRequest

   ```json
   {
     "taskId": "string"
   }
   ```

   

Swagger Documention can be seen at:

> http://localhost:9091/swagger-ui.html

Database : H2 in memory DB 

> Console : http://localhost:9091/h2-console
> username: sa
> password: password

Build commands 

```
mvn clean install 
java -jar <app-name>.jar
```

> We are using lombok so enable lombok plugin in intellij or if using eclipse https://www.baeldung.com/lombok-ide



​	
