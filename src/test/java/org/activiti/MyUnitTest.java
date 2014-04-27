package org.activiti;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.activiti.engine.HistoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.activiti.engine.test.ActivitiRule;
import org.activiti.engine.test.Deployment;
import org.junit.Rule;
import org.junit.Test;

public class MyUnitTest {
	
	@Rule
	public ActivitiRule activitiRule = new ActivitiRule();
	
	@Test
	@Deployment(resources = {"org/activiti/test/MyProcess2.bpmn"})
	public void test() {
		StringBuilder strb = new StringBuilder();
		Map<String, Object> variables = new HashMap<String, Object>();
		variables.put("employeeName", "Kermit");
		variables.put("numberOfDays", new Integer(4));
		variables.put("vacationMotivation", "I'm really tired!");
		RuntimeService runtimeService = activitiRule.getRuntimeService();
		ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("myProcess", variables);
		System.err.println("activityId :" + processInstance.getActivityId());
		TaskService taskService = activitiRule.getTaskService();
		List<Task> tasks = taskService.createTaskQuery().taskCandidateGroup("management").list();
				
		Task task = tasks.get(0);
	      
		Map<String, Object> taskVariables = new HashMap<String, Object>();
		taskVariables.put("vacationApproved", "false");
		taskVariables.put("managerMotivation", "We have a tight deadline!");
		strb.append(task.getDescription()).append("\r\n");
		taskService.complete(task.getId(), taskVariables); 

		task = taskService.createTaskQuery().singleResult();
		taskVariables = new HashMap<String, Object>();
		taskVariables.put("resendRequest", "true");
		taskService.complete(task.getId(), taskVariables);
		strb.append(task.getDescription()).append("\r\n");
//		
//		task = taskService.createTaskQuery().singleResult();
//		taskVariables = new HashMap<String, Object>();
//		taskVariables.put("numberOfDays", new Integer(3));
//		taskVariables.put("vacationMotivation", "I'm really really tired!");
//		taskService.complete(task.getId(), taskVariables);
//		strb.append(task.getDescription()).append("\r\n");
		
		task = taskService.createTaskQuery().singleResult();
		taskVariables = new HashMap<String, Object>();
		taskVariables.put("vacationApproved", "true");
		taskService.complete(task.getId(), taskVariables);
		strb.append(task.getDescription()).append("\r\n"); 
		HistoryService historyService = activitiRule.getHistoryService();
		HistoricProcessInstance historys = historyService.createHistoricProcessInstanceQuery().processInstanceId(processInstance.getId()).singleResult();
		System.err.println(taskService.createTaskQuery().count());
		System.err.println(strb.toString());

	}
}
