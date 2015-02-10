package edu.columbia.rascal.business.auxiliary;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.ExecutionListener;
import org.activiti.engine.delegate.TaskListener;
import org.activiti.engine.impl.persistence.entity.ExecutionEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

public class IacucListener implements TaskListener, ExecutionListener {

    private static final Logger log = LoggerFactory.getLogger(IacucListener.class);

    private static final String AllRvs = "allRvs";

    private static final String ApproveA = "ApproveA";
    private static final String ApproveB = "ApproveB";
    private static final String AllAppendicesApproved = "allAppendicesApproved";
    private static final String appendixA = "appendixA";
    private static final String appendixB = "appendixB";

    // task listener
    @Override
    public void notify(DelegateTask delegateTask) {

        DelegateExecution taskExecution = delegateTask.getExecution();
        String bizKey = taskExecution.getProcessBusinessKey();
        String processId = taskExecution.getProcessInstanceId();
        String taskId = delegateTask.getId();
        String taskDefKey = delegateTask.getTaskDefinitionKey();
        String eventName = delegateTask.getEventName();
        StringBuilder sb = new StringBuilder();
        sb.append("bizKey=").append(bizKey)
                .append(",taskId=").append(taskId)
                .append(",taskDefKey=").append(taskDefKey)
                .append(",processId=").append(processId);

        if ("create".equals(eventName)) {
            log.info("create: {}", sb.toString());
        } else if ("complete".equals(eventName)) {
            log.info("complete: {}", sb.toString());


            if( IacucStatus.RETURNTOPI.isDefKey(taskDefKey) ) {
                taskExecution.setVariable("undoApproval", false);
            }
            if( IacucStatus.UndoApproval.isDefKey(taskDefKey) ) {
                taskExecution.setVariable("undoApproval", true);
            }
            if( IacucStatus.FINALAPPROVAL.isDefKey(taskDefKey) ) {
                taskExecution.setVariable("undoApproval", false);
            }



            // for designated reviewers
            if (IacucStatus.Rv1Hold.isDefKey(taskDefKey)) {
                taskExecution.setVariable(AllRvs, false);
            } else if (IacucStatus.Rv1ReqFullReview.isDefKey(taskDefKey)) {
                taskExecution.setVariable(AllRvs, false);
            } else if (IacucStatus.Rv2Hold.isDefKey(taskDefKey)) {
                taskExecution.setVariable(AllRvs, false);
            } else if (IacucStatus.Rv2ReqFullReview.isDefKey(taskDefKey)) {
                taskExecution.setVariable(AllRvs, false);
            } else if (IacucStatus.Rv3Hold.isDefKey(taskDefKey)) {
                taskExecution.setVariable(AllRvs, false);
            } else if (IacucStatus.Rv3ReqFullReview.isDefKey(taskDefKey)) {
                taskExecution.setVariable(AllRvs, false);
            }

            // for sub-process
            if (IacucStatus.SOPreApproveA.isDefKey(taskDefKey)) {
                taskExecution.setVariable(ApproveA, true);
                updateAppendixApproveStatus(delegateTask);
            } else if (IacucStatus.SOHoldA.isDefKey(taskDefKey)) {
                taskExecution.setVariable(AllAppendicesApproved, false);
            } else if (IacucStatus.SOPreApproveB.isDefKey(taskDefKey)) {
                taskExecution.setVariable(ApproveB, true);
                updateAppendixApproveStatus(delegateTask);
            } else if (IacucStatus.SOHoldB.isDefKey(taskDefKey)) {
                taskExecution.setVariable(AllAppendicesApproved, false);
            }

        }

    }

    private void updateAppendixApproveStatus(DelegateTask delegateTask) {
        boolean hasAppendixA = false;
        boolean approvedAppendixA = false;
        if (delegateTask.getVariable(appendixA) != null) {
            if ((Boolean) delegateTask.getVariable(appendixA)) {
                hasAppendixA = true;
            } else {
                approvedAppendixA = true;
            }
        }

        boolean hasAppendixB = false;
        boolean approvedAppendixB = false;
        if (delegateTask.getVariable(appendixB) != null) {
            if ((Boolean) delegateTask.getVariable(appendixB)) {
                hasAppendixB = true;
            } else {
                approvedAppendixB = true;
            }
        }

        if (hasAppendixA) {
            approvedAppendixA = delegateTask.getVariable(ApproveA) != null;
        }
        if (hasAppendixB) {
            approvedAppendixB = delegateTask.getVariable(ApproveB) != null;
        }

        if (approvedAppendixA && approvedAppendixB) {
            delegateTask.setVariable(AllAppendicesApproved, true);
        }
    }

    // execution listener
    @Override
    public void notify(DelegateExecution delegateExecution) throws Exception {


        ExecutionEntity thisEntity = (ExecutionEntity) delegateExecution;
        ExecutionEntity superExecEntity = thisEntity.getSuperExecution();
        String eventName = delegateExecution.getEventName();

        if (superExecEntity == null) {
            // get the business key of the main process
            log.info("main process: eventName={}, bizKey={}, procDefId={}", eventName, thisEntity.getBusinessKey(), thisEntity.getProcessDefinitionId());
            // used by designatedReviews output
            thisEntity.setVariable(AllRvs, true);

        } else {
            // in a sub-process so get the BusinessKey variable set by the caller.
            String key = (String) superExecEntity.getVariable("BusinessKey");
            boolean hasAppendix = (Boolean) superExecEntity.getVariable("hasAppendix");

            log.info("sub-process: eventName={}, bizKey={}, procDefId={}, hasAppendix={}",
                    eventName, key, thisEntity.getProcessDefinitionId(), hasAppendix);

            thisEntity.setVariable("BusinessKey", key);

            // for get task by business key
            thisEntity.setBusinessKey(key);

            //
            Map<String, Object> processMap = new HashMap<String, Object>();
            processMap.put("appendixA", true);   // used in sub-process, has A
            processMap.put("appendixB", true);   // used in sub-process, has B
            thisEntity.setVariables(processMap);

        }
    }
}
