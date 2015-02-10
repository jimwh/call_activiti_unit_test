package org.activiti;

import java.util.*;

import edu.columbia.rascal.business.auxiliary.*;
import org.activiti.engine.HistoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.history.HistoricTaskInstanceQuery;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Comment;
import org.activiti.engine.task.Task;
import org.activiti.engine.test.ActivitiRule;
import org.activiti.engine.test.Deployment;

import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MyUnitTest {

    static final Logger log = LoggerFactory.getLogger(MyUnitTest.class);
    static final String ProcessDefKey = "my-process";
    static final String START_GATEWAY = "START_GATEWAY";

    @Rule
    public ActivitiRule activitiRule = new ActivitiRule();

    @Test
    @Deployment(resources = {
            "org/activiti/test/my-process.bpmn20.xml",
            "org/activiti/test/appendix-process.bpmn20.xml"
    })

    public void test() {

        String bizKey = "my-bizKey";

        // it has appendix attached
        Map<String, Object> processMap = new HashMap<String, Object>();
        boolean hasAppendix = true;
        boolean allAppendicesApproved = false;
        processMap.put("hasAppendix", hasAppendix);// used in main process
        processMap.put("allAppendicesApproved", allAppendicesApproved);// used in main process
        //processMap.put("appendixA", true);   // used in sub-process, has A
        //processMap.put("appendixB", true);  // used in sub-process, no B

        // Appendix A...I
        startProtocolProcess(bizKey, processMap);
        //
        // distribute it to reviewers
        List<String> rvList = new ArrayList<String>();
        rvList.add("Sam");
        rvList.add("Dave");
        distributeToDS(bizKey, "admin", rvList);
        printOpenTaskList(bizKey);

        // reviewer approval
        u1Approval(bizKey, "Sam");
        approveAppendixA(bizKey, "safetyOfficeDam");
        approveAppendixB(bizKey, "safetyOfficeHolder");
        //holdAppendixB(bizKey, "safetyOfficeHolder");

        // another user approval
        u2Approval(bizKey, "Dave");
        //u2Hold(bizKey, "Dave");


        finalApproval(bizKey, "admin");
        // returnToPI(bizKey, "admin");

        printCurrentApprovalStatus(bizKey);

        //undoApproval(bizKey, "admin");
        /*
        try{
            Thread.sleep(5000);
        }catch(InterruptedException e){}

        printCurrentApprovalStatus(bizKey);

        printHistory(bizKey);
        */
    }


    void submit(String bizKey) {
        IacucTaskForm iacucTaskForm = new IacucTaskForm();
        iacucTaskForm.setBizKey(bizKey);
        iacucTaskForm.setAuthor("bob");
        iacucTaskForm.setTaskName(IacucStatus.SUBMIT.statusName());
        iacucTaskForm.setTaskDefKey(IacucStatus.SUBMIT.taskDefKey());
        completeTaskByTaskService(iacucTaskForm);
    }

    void returnToPI(String bizKey, String user) {
        IacucTaskForm iacucTaskForm = new IacucTaskForm();
        iacucTaskForm.setBizKey(bizKey);
        iacucTaskForm.setAuthor(user);
        iacucTaskForm.setComment("no choice but back to PI...");
        iacucTaskForm.setTaskName(IacucStatus.RETURNTOPI.statusName());
        iacucTaskForm.setTaskDefKey(IacucStatus.RETURNTOPI.taskDefKey());
        //
        IacucCorrespondence corr1 = new IacucCorrespondence();
        corr1.setFrom(user);
        corr1.setRecipient("PI");
        corr1.setCarbonCopy("Co-PI");
        corr1.setSubject("Notification of Return to PI from David");
        corr1.setText("Question about your protocol bla bla ...");
        corr1.apply();
        iacucTaskForm.setIacucCorrespondence(corr1);
        //
        completeTaskByTaskService(iacucTaskForm);
        // completed return-2-pi, there is no task and no instance
        // Assert.assertEquals(0, taskCount(bizKey));
        // Assert.assertNull(getProtocolProcessInstance(bizKey));
    }

    void undoReturnToPI(String bizKey, String user) {
        IacucTaskForm iacucTaskForm = new IacucTaskForm();
        iacucTaskForm.setBizKey(bizKey);
        iacucTaskForm.setAuthor(user);
        iacucTaskForm.setComment("undo return to PI back man1...");
        iacucTaskForm.setTaskName(IacucStatus.UndoReturnToPI.statusName());
        iacucTaskForm.setTaskDefKey(IacucStatus.UndoReturnToPI.taskDefKey());
        //
        completeTaskByTaskService(iacucTaskForm);
    }

    void distributeToDS(String bizKey, String user, List<String> reviewerList) {
        IacucDistributeToDesignatedReviewerForm iacucTaskForm = new IacucDistributeToDesignatedReviewerForm();
        iacucTaskForm.setBizKey(bizKey);
        iacucTaskForm.setAuthor(user);
        iacucTaskForm.setComment("distribute to " + reviewerList);
        iacucTaskForm.setTaskName(IacucStatus.DistributeToDesignatedReviewer.statusName());
        iacucTaskForm.setTaskDefKey(IacucStatus.DistributeToDesignatedReviewer.taskDefKey());
        iacucTaskForm.setReviewerList(reviewerList);
        //
        IacucCorrespondence corr = new IacucCorrespondence();
        corr.setFrom("Freemen");
        corr.setRecipient("sam");
        corr.setCarbonCopy("cameron");
        corr.setSubject("notification of distribution");
        corr.setText("complete review asap ...");
        corr.apply();
        iacucTaskForm.setIacucCorrespondence(corr);
        completeTaskByTaskService(iacucTaskForm);
    }

    void approveAppendixA(String bizKey, String user) {
        IacucTaskForm iacucTaskForm = new IacucTaskForm();
        iacucTaskForm.setBizKey(bizKey);
        iacucTaskForm.setAuthor(user);
        iacucTaskForm.setComment("reviewed and approve appendix-A...");
        iacucTaskForm.setTaskName(IacucStatus.SOPreApproveA.statusName());
        iacucTaskForm.setTaskDefKey(IacucStatus.SOPreApproveA.taskDefKey());
        completeTaskByTaskService(iacucTaskForm);
    }

    void approveAppendixB(String bizKey, String user) {
        IacucTaskForm iacucTaskForm = new IacucTaskForm();
        iacucTaskForm.setBizKey(bizKey);
        iacucTaskForm.setAuthor(user);
        iacucTaskForm.setComment("reviewed and approve appendix-B...");
        iacucTaskForm.setTaskName(IacucStatus.SOPreApproveB.statusName());
        iacucTaskForm.setTaskDefKey(IacucStatus.SOPreApproveB.taskDefKey());
        completeTaskByTaskService(iacucTaskForm);
    }

    void holdAppendixA(String bizKey, String user) {
        IacucTaskForm iacucTaskForm = new IacucTaskForm();
        iacucTaskForm.setBizKey(bizKey);
        iacucTaskForm.setAuthor(user);
        iacucTaskForm.setComment("reviewed but hold appendix-A...");
        iacucTaskForm.setTaskName(IacucStatus.SOHoldA.statusName());
        iacucTaskForm.setTaskDefKey(IacucStatus.SOHoldA.taskDefKey());
        completeTaskByTaskService(iacucTaskForm);
    }

    void holdAppendixB(String bizKey, String user) {
        IacucTaskForm iacucTaskForm = new IacucTaskForm();
        iacucTaskForm.setBizKey(bizKey);
        iacucTaskForm.setAuthor(user);
        iacucTaskForm.setComment("reviewed but hold appendix-B...");
        iacucTaskForm.setTaskName(IacucStatus.SOHoldB.statusName());
        iacucTaskForm.setTaskDefKey(IacucStatus.SOHoldB.taskDefKey());
        completeTaskByTaskService(iacucTaskForm);
    }


    void u1Approval(String bizKey, String u1) {
        IacucTaskForm iacucTaskForm = new IacucTaskForm();
        iacucTaskForm.setBizKey(bizKey);
        iacucTaskForm.setAuthor(u1);
        iacucTaskForm.setComment("approval is given");
        iacucTaskForm.setTaskName(IacucStatus.Rv1Approval.statusName());
        iacucTaskForm.setTaskDefKey(IacucStatus.Rv1Approval.taskDefKey());
        Assert.assertNotNull(getAssigneeTaskByTaskDefKey(bizKey, IacucStatus.Rv1Approval.taskDefKey(), iacucTaskForm.getAuthor()));
        completeTaskByTaskService(iacucTaskForm);
    }

    void u2Approval(String bizKey, String u1) {
        IacucTaskForm iacucTaskForm = new IacucTaskForm();
        iacucTaskForm.setBizKey(bizKey);
        iacucTaskForm.setAuthor(u1);
        iacucTaskForm.setComment("approval is given");
        iacucTaskForm.setTaskName(IacucStatus.Rv2Approval.statusName());
        iacucTaskForm.setTaskDefKey(IacucStatus.Rv2Approval.taskDefKey());
        Assert.assertNotNull(getAssigneeTaskByTaskDefKey(bizKey, IacucStatus.Rv2Approval.taskDefKey(), iacucTaskForm.getAuthor()));
        completeTaskByTaskService(iacucTaskForm);
    }

    void u2Hold(String bizKey, String u1) {
        IacucTaskForm iacucTaskForm = new IacucTaskForm();
        iacucTaskForm.setBizKey(bizKey);
        iacucTaskForm.setAuthor(u1);
        iacucTaskForm.setComment("u1 is hold");
        iacucTaskForm.setTaskName(IacucStatus.Rv2Hold.statusName());
        iacucTaskForm.setTaskDefKey(IacucStatus.Rv2Hold.taskDefKey());
        Assert.assertNotNull(getAssigneeTaskByTaskDefKey(bizKey, IacucStatus.Rv2Hold.taskDefKey(), iacucTaskForm.getAuthor()));
        completeTaskByTaskService(iacucTaskForm);
    }

    void finalApproval(String bizKey, String user) {
        IacucTaskForm iacucTaskForm = new IacucTaskForm();
        iacucTaskForm.setBizKey(bizKey);
        iacucTaskForm.setAuthor(user);
        iacucTaskForm.setComment("final approval");
        iacucTaskForm.setTaskName(IacucStatus.FINALAPPROVAL.statusName());
        iacucTaskForm.setTaskDefKey(IacucStatus.FINALAPPROVAL.taskDefKey());
        //
        IacucCorrespondence corr = new IacucCorrespondence();
        corr.setFrom(user);
        corr.setRecipient("pi");
        corr.setCarbonCopy("co-pi");
        corr.setSubject("notification of approval");
        corr.setText("Your protocol has been approved.");
        corr.apply();
        iacucTaskForm.setIacucCorrespondence(corr);
        completeTaskByTaskService(iacucTaskForm);
    }

    void undoApproval(String bizKey, String user) {
        IacucTaskForm iacucTaskForm = new IacucTaskForm();
        iacucTaskForm.setBizKey(bizKey);
        iacucTaskForm.setAuthor(user);
        iacucTaskForm.setComment("undo approval");
        iacucTaskForm.setTaskName(IacucStatus.UndoApproval.statusName());
        iacucTaskForm.setTaskDefKey(IacucStatus.UndoApproval.taskDefKey());
        //
        completeTaskByTaskService(iacucTaskForm);
    }


    void completeTaskByTaskService(IacucTaskForm iacucTaskForm) {
        Assert.assertNotNull(iacucTaskForm);
        Task task = getTaskByTaskDefKey(iacucTaskForm.getBizKey(), iacucTaskForm.getTaskDefKey());
        Assert.assertNotNull(task);
        TaskService taskService = activitiRule.getTaskService();
        String taskId = task.getId();
        taskService.claim(taskId, iacucTaskForm.getAuthor());

        // if you want to store comment in activity task comment, then... otherwise do nothing
        String content = iacucTaskForm.getComment();
        if (content != null) {
            Comment comment = taskService.addComment(taskId, task.getProcessInstanceId(), iacucTaskForm.getTaskDefKey(), content);
            iacucTaskForm.setCommentId(comment.getId());
        }

        // attach attribute to this task
        Map<String, String> attribute = iacucTaskForm.getProperties();
        if (attribute != null && !attribute.isEmpty())
            taskService.setVariableLocal(taskId, "iacucTaskForm" + taskId, attribute);

        // attach corr to this task
        IacucCorrespondence corr = iacucTaskForm.getIacucCorrespondence();
        if (corr != null) {
            Map<String, String> corrProperties = corr.getProperties();
            if (!corrProperties.isEmpty()) {
                taskService.setVariableLocal(taskId, "IacucCorrespondence" + taskId, corrProperties);
            }
        }

        // determine the direction
        Map<String, Object> map = iacucTaskForm.getTaskVariables();
        if (map != null && !map.isEmpty())
            taskService.complete(taskId, map); // go left/right/middle or go ...
        else
            taskService.complete(taskId); // go straight
    }

    List<IacucTaskForm> getHistoricTaskByBizKey(String bizKey) {
        List<IacucTaskForm> listIacucTaskForm = new ArrayList<IacucTaskForm>();
        // from main process
        List<IacucTaskForm> mainList = getListIacucTaskForm(getFromMainProcess(bizKey));
        listIacucTaskForm.addAll(mainList);
        // from sub-process
        List<IacucTaskForm> subList = getListIacucTaskForm(getFromSubProcess(bizKey));
        listIacucTaskForm.addAll(subList);
        //
        Collections.sort(listIacucTaskForm, Collections.reverseOrder());
        return listIacucTaskForm;
    }


    List<HistoricTaskInstance> getFromMainProcess(String bizKey) {
        HistoryService historyService = activitiRule.getHistoryService();
        HistoricTaskInstanceQuery query = historyService
                .createHistoricTaskInstanceQuery()
                .processDefinitionKey(ProcessDefKey)
                .processInstanceBusinessKey(bizKey)
                .finished()
                .taskDeleteReason("completed")
                .includeTaskLocalVariables()
                .orderByHistoricTaskInstanceEndTime()
                .desc();
        List<HistoricTaskInstance> list = query.list();
        return list;
    }

    List<HistoricTaskInstance> getFromSubProcess(String bizKey) {
        HistoryService historyService = activitiRule.getHistoryService();
        HistoricTaskInstanceQuery query = historyService
                .createHistoricTaskInstanceQuery()
                .processDefinitionKey("appendix-process") // sub-process def key
                        //.processInstanceBusinessKey(bizKey) not for sub process
                .processVariableValueEquals("BusinessKey", bizKey) // use proc var
                .finished()
                .taskDeleteReason("completed")
                .includeTaskLocalVariables()
                .orderByHistoricTaskInstanceEndTime()
                .desc();
        return query.list();
    }


    List<IacucTaskForm> getListIacucTaskForm(List<HistoricTaskInstance> list) {

        List<IacucTaskForm> listIacucTaskForm = new ArrayList<IacucTaskForm>();
        for (HistoricTaskInstance hs : list) {
            IacucTaskForm iacucTaskForm = new IacucTaskForm();
            iacucTaskForm.setTaskId(hs.getId());
            iacucTaskForm.setEndTime(hs.getEndTime());

            //
            // iacucTaskForm.setTaskDefKey(hs.getTaskDefinitionKey());
            // iacucTaskForm.setTaskName(hs.getName());
            //
            Map<String, Object> localMap = hs.getTaskLocalVariables();
            Map<String, String> taskMap = (Map<String, String>) localMap.get("iacucTaskForm" + hs.getId());

            // restore the original attribute
            iacucTaskForm.setProperties(taskMap);

            // two options:
            // if comment is stored in variable, then do nothing
            // if comment is stored in task comment, then as follow
            iacucTaskForm.setComment(getCommentText(iacucTaskForm.getCommentId()));

            // two options:
            // if the snapshot id is retrieved from here, then bla bla ...
            // iacucTaskForm.setSnapshotId(snapshotId);
            // if the snapshot id is pre-stored in properties, then do nothing

            // restore the original correspondence if any
            Map<String, String> corrMap = (Map<String, String>) localMap.get("IacucCorrespondence" + hs.getId());
            if (corrMap != null && !corrMap.isEmpty()) {
                IacucCorrespondence corr = new IacucCorrespondence();
                corr.setProperties(corrMap);
                iacucTaskForm.setIacucCorrespondence(corr);
            }
            listIacucTaskForm.add(iacucTaskForm);
        }
        return listIacucTaskForm;
    }

    String getCommentText(String commentId) {
        if (commentId == null) return null;
        Comment comment = activitiRule.getTaskService().getComment(commentId);
        return comment != null ? comment.getFullMessage() : null;
    }


    Task getTaskByTaskDefKey(String bizKey, String defKey) {
        TaskService taskService = activitiRule.getTaskService();
        return taskService.createTaskQuery()
                .processInstanceBusinessKey(bizKey)
                .taskDefinitionKey(defKey)
                .singleResult();
    }

    Task getAssigneeTaskByTaskDefKey(String bizKey, String defKey, String assignee) {
        TaskService taskService = activitiRule.getTaskService();
        return taskService.createTaskQuery()
                .processInstanceBusinessKey(bizKey)
                .taskDefinitionKey(defKey)
                .taskAssignee(assignee)
                .singleResult();
    }

    Task getAssigneeTaskByTaskName(String bizKey, String taskName, String assignee) {
        TaskService taskService = activitiRule.getTaskService();
        return taskService.createTaskQuery()
                .processInstanceBusinessKey(bizKey)
                .taskName(taskName)
                .taskAssignee(assignee)
                .singleResult();
    }

    long taskCount(String bizKey) {
        return activitiRule.getTaskService()
                .createTaskQuery()
                .processInstanceBusinessKey(bizKey).count();
    }

    void printOpenTaskList(String bizKey) {
        List<Task> taskList = activitiRule.getTaskService()
                .createTaskQuery().processInstanceBusinessKey(bizKey).list();
        for (Task task : taskList) {
            log.info("taskDefKey=" + task.getTaskDefinitionKey());
        }
    }

    void printHistory(String bizKey) {
        List<IacucTaskForm> iacucTaskFormList = getHistoricTaskByBizKey(bizKey);
        for (IacucTaskForm form : iacucTaskFormList) {
            log.info(form.toString());
        }
    }

    // protocol approval process
    void startProtocolProcess(String bizKey, Map<String, Object> processMap) {
        Assert.assertNotNull("dude can't be null", processMap);

        ProcessInstance instance = getProtocolProcessInstance(bizKey);
        Assert.assertNull("dude i am expecting null", instance);

        processMap.put(START_GATEWAY, IacucStatus.SUBMIT.gatewayValue());
        // for call activity
        processMap.put("BusinessKey", bizKey);
        ProcessInstance processInstance = starProcess(bizKey, processMap, IacucStatus.SUBMIT.name());
        Assert.assertNotNull(processInstance);
        submit(bizKey);
    }

    ProcessInstance getProtocolProcessInstance(String bizKey) {
        return getProcessInstanceByName(bizKey, IacucStatus.SUBMIT.name());
    }

    // add correspondence process
    void startCorrProcess(String bizKey) {
        ProcessInstance instance = getCorrProcessInstance(bizKey);
        Assert.assertNull("dude", instance);
        Map<String, Object> map = new HashMap<String, Object>();
        map.put(START_GATEWAY, IacucStatus.AddCorrespondence.gatewayValue());
        starProcess(bizKey, map, IacucStatus.AddCorrespondence.name());
    }

    ProcessInstance getCorrProcessInstance(String bizKey) {
        return getProcessInstanceByName(bizKey, IacucStatus.AddCorrespondence.name());
    }

    // add note process
    void startAddNoteProcess(String bizKey) {
        ProcessInstance instance = getNoteProcessInstance(bizKey);
        Assert.assertNull("dude", instance);
        Map<String, Object> map = new HashMap<String, Object>();
        map.put(START_GATEWAY, IacucStatus.AddNote.gatewayValue());
        starProcess(bizKey, map, IacucStatus.AddNote.name());
    }

    ProcessInstance getNoteProcessInstance(String bizKey) {
        return getProcessInstanceByName(bizKey, IacucStatus.AddNote.name());
    }

    ProcessInstance getProcessInstanceByName(String bizKey, String instanceName) {
        return activitiRule.getRuntimeService()
                .createProcessInstanceQuery()
                .processDefinitionKey(ProcessDefKey)
                .processInstanceBusinessKey(bizKey)
                .processInstanceName(instanceName)
                .singleResult();
    }

    // terminal protocol
    void startTerminateProtocolProcess(String bizKey) {
        ProcessInstance instance = getProtocolProcessInstance(bizKey);
        Assert.assertNull("dude ", instance);
        Map<String, Object> map = new HashMap<String, Object>();
        map.put(START_GATEWAY, IacucStatus.TERMINATE.gatewayValue());
        starProcess(bizKey, map, IacucStatus.TERMINATE.name());
    }

    ProcessInstance starProcess(String bizKey, Map<String, Object> map, String instanceName) {
        RuntimeService runtimeService = activitiRule.getRuntimeService();
        ProcessInstance instance = runtimeService.startProcessInstanceByKey(ProcessDefKey, bizKey, map);
        runtimeService.setProcessInstanceName(instance.getProcessInstanceId(), instanceName);
        return instance;
    }

    String getCurrentProcessInstanceId(String bizKey) {
        ProcessInstance instance = getProtocolProcessInstance(bizKey);
        return instance != null ? instance.getProcessInstanceId() : null;
    }


    List<IacucTaskForm> getCurrentApprovalStatus(String bizKey) {
        List<IacucTaskForm> listIacucTaskForm = new ArrayList<IacucTaskForm>();
        String processId = getCurrentProcessInstanceId(bizKey);
        if (processId == null) return listIacucTaskForm;

        HistoryService historyService = activitiRule.getHistoryService();
        HistoricTaskInstanceQuery query = historyService
                .createHistoricTaskInstanceQuery()
                        // .processDefinitionKey(ProcessDefKey)
                        // .processInstanceBusinessKey(bizKey)
                .processVariableValueEquals("BusinessKey", bizKey)
                        // .processInstanceId(processId)
                .includeTaskLocalVariables()
                .orderByHistoricTaskInstanceEndTime()
                .orderByTaskCreateTime()
                .desc();
        List<HistoricTaskInstance> list = query.list();

        for (HistoricTaskInstance hs : list) {
            if ("deleted".equals(hs.getDeleteReason())) continue;
            IacucTaskForm iacucTaskForm = new IacucTaskForm();
            iacucTaskForm.setTaskId(hs.getId());
            iacucTaskForm.setEndTime(hs.getEndTime());
            iacucTaskForm.setTaskDefKey(hs.getTaskDefinitionKey());
            iacucTaskForm.setTaskName(hs.getName());
            //
            Map<String, Object> localMap = hs.getTaskLocalVariables();
            Map<String, String> taskMap = (Map<String, String>) localMap.get("iacucTaskForm" + hs.getId());

            // restore the original attribute
            iacucTaskForm.setProperties(taskMap);
            iacucTaskForm.setComment(getCommentText(iacucTaskForm.getCommentId()));
            listIacucTaskForm.add(iacucTaskForm);
        }
        return listIacucTaskForm;
    }

    void printCurrentApprovalStatus(String bizKey) {
        log.info("...............................................................\n");
        log.info("get all tasks from current process including open/closed tasks...");
        List<IacucTaskForm> list = getCurrentApprovalStatus(bizKey);
        for (IacucTaskForm form : list) {
            log.info(form.toString());
        }
        log.info("...............................................................\n");
    }

}
