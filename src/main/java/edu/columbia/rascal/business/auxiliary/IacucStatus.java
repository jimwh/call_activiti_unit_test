package edu.columbia.rascal.business.auxiliary;

import java.util.HashSet;
import java.util.Set;

public enum IacucStatus {

    SUBMIT("Submit") {
        @Override
        public String taskDefKey() {
            return "submit";
        }

        @Override
        public boolean isDefKey(String def) {
            return this.taskDefKey().equals(def);
        }

        @Override
        public boolean isStatus(String status) {
            return statusName().equalsIgnoreCase(status);
        }

        @Override
        public int gatewayValue() {
            return 1;
        }
    },

    DISTRIBUTE("Distribute") {
        @Override
        public String taskDefKey() {
            return "distribution";
        }

        @Override
        public boolean isDefKey(String def) {
            return this.taskDefKey().equals(def);
        }

        @Override
        public boolean isStatus(String status) {
            return statusName().equalsIgnoreCase(status);
        }

        @Override
        public int gatewayValue() {
            return 11;
        }
    },

    SubcommitteeReview("Subcommittee") {
        @Override
        public String taskDefKey() {
            return "subcommittee";
        }

        @Override
        public boolean isDefKey(String def) {
            return this.taskDefKey().equals(def);
        }

        @Override
        public boolean isStatus(String status) {
            return statusName().equalsIgnoreCase(status);
        }

        @Override
        public int gatewayValue() {
            return 1;
        }
    },

    ASSIGNEEREVIEW("Designated Reviewer") {
        @Override
        public String taskDefKey() {
            return "assigneeReview";
        }

        @Override
        public boolean isDefKey(String def) {
            return this.taskDefKey().equals(def);
        }

        @Override
        public boolean isStatus(String status) {
            return statusName().equalsIgnoreCase(status);
        }

        @Override
        public int gatewayValue() {
            return 33;
        }
    },

    RETURNTOPI("Return to PI") {
        @Override
        public String taskDefKey() {
            return "returnToPI";
        }

        @Override
        public boolean isDefKey(String def) {
            return this.taskDefKey().equals(def);
        }

        @Override
        public boolean isStatus(String status) {
            return statusName().equalsIgnoreCase(status);
        }

        @Override
        public int gatewayValue() {
            return 55;
        }
    },

    FINALAPPROVAL("Approve") {
        @Override
        public String taskDefKey() {
            return "finalApproval";
        }

        @Override
        public boolean isDefKey(String def) {
            return this.taskDefKey().equals(def);
        }

        @Override
        public boolean isStatus(String status) {
            return statusName().equalsIgnoreCase(status);
        }

        @Override
        public int gatewayValue() {
            return 66;
        }
    },


    ADVERSEEVENT("AdverseEvent") {
        @Override
        public String taskDefKey() {
            return "adverseEvent";
        }

        @Override
        public boolean isDefKey(String def) {
            return this.taskDefKey().equals(def);
        }

        @Override
        public boolean isStatus(String status) {
            return statusName().equalsIgnoreCase(status);
        }

        @Override
        public int gatewayValue() {
            return 3;
        }
    },

    TERMINATE("Terminate") {
        @Override
        public String taskDefKey() { return "terminateProtocol"; }

        @Override
        public boolean isDefKey(String def) {
            return this.taskDefKey().equals(def);
        }

        @Override
        public boolean isStatus(String status) {
            return statusName().equalsIgnoreCase(status);
        }

        @Override
        public int gatewayValue() {
            return 3;
        }
    },

    SUSPEND("Suspend") {
        @Override
        public String taskDefKey() {
            return "suspendProtocol";
        }

        @Override
        public boolean isDefKey(String def) {
            return this.taskDefKey().equals(def);
        }

        @Override
        public boolean isStatus(String status) {
            return statusName().equalsIgnoreCase(status);
        }

        @Override
        public int gatewayValue() {
            return 4;
        }
    },

    WITHDRAW("Withdraw") {
        @Override
        public String taskDefKey() {
            return "withdrawal";
        }

        @Override
        public boolean isDefKey(String def) {
            return this.taskDefKey().equals(def);
        }

        @Override
        public boolean isStatus(String status) {
            return statusName().equalsIgnoreCase(status);
        }

        @Override
        public int gatewayValue() {
            return 5;
        }
    },

    AddCorrespondence("Add Correspondence") {
        @Override
        public String taskDefKey() {
            return "addCorrespondence";
        }

        @Override
        public boolean isDefKey(String def) {
            return this.taskDefKey().equals(def);
        }

        @Override
        public boolean isStatus(String status) {
            return statusName().equalsIgnoreCase(status);
        }

        @Override
        public int gatewayValue() {
            return 6;
        }
    },

    REINSTATE("Reinstate") {
        @Override
        public String taskDefKey() {
            return "reinstatement";
        }

        @Override
        public boolean isDefKey(String def) {
            return this.taskDefKey().equals(def);
        }

        @Override
        public boolean isStatus(String status) {
            return statusName().equalsIgnoreCase(status);
        }

        @Override
        public int gatewayValue() {
            return 7;
        }
    },

    KAPUT("Kaput") {
        @Override
        public String taskDefKey() {
            return "kaput";
        }

        @Override
        public boolean isDefKey(String def) {
            return this.taskDefKey().equals(def);
        }

        @Override
        public boolean isStatus(String status) {
            return  statusName().equals(status) ? true : isKaput(status);
        }

        @Override
        public int gatewayValue() {
            return 8;
        }
    },
    
    UndoReturnToPI("Undo Return to PI") {
        @Override
        public String taskDefKey() {
            return "undoReturnToPI";
        }

        @Override
        public boolean isDefKey(String def) {
            return this.taskDefKey().equals(def);
        }

        @Override
        public boolean isStatus(String status) {
            return  statusName().equals(status);
        }

        @Override
        public int gatewayValue() {
            // unused
        	return 9;
        }
    },
    
    UndoApproval("Undo Approval") {
        @Override
        public String taskDefKey() {
            return "undoApproval";
        }

        @Override
        public boolean isDefKey(String def) {
            return this.taskDefKey().equals(def);
        }

        @Override
        public boolean isStatus(String status) {
            return  statusName().equals(status);
        }

        @Override
        public int gatewayValue() {
        	// unused
            return 10;
        }
    },

    DistributeToSubcommittee("Distribute: Subcommittee") {
        @Override
        public String taskDefKey() {
            return "distributeToSub";
        }

        @Override
        public boolean isDefKey(String def) {
            return this.taskDefKey().equals(def);
        }

        @Override
        public boolean isStatus(String status) {
            return  statusName().equals(status);
        }

        @Override
        public int gatewayValue() { return 1; }
    },

    DistributeToDesignatedReviewer("Distribute: Designated Reviewers") {
        @Override
        public String taskDefKey() {  return "distributeToDS"; }

        @Override
        public boolean isDefKey(String def) {
            return this.taskDefKey().equals(def);
        }

        @Override
        public boolean isStatus(String status) {
            return  statusName().equals(status);
        }

        @Override
        public int gatewayValue() { return 2; }
    },
    
    Rv1Approval("Designated Reviewer Approval") {
        @Override
        public String taskDefKey() { return "rv1Approval"; }

        @Override
        public boolean isDefKey(String def) {
            return this.taskDefKey().equals(def);
        }

        @Override
        public boolean isStatus(String status) {
            return  statusName().equals(status);
        }

        @Override
        public int gatewayValue() {
        	// unused
            return 10;
        }
    },
    Rv1Hold("Designated Reviewer Hold") {
        @Override
        public String taskDefKey() { return "rv1Hold"; }

        @Override
        public boolean isDefKey(String def) {
            return this.taskDefKey().equals(def);
        }

        @Override
        public boolean isStatus(String status) {
            return  statusName().equals(status);
        }

        @Override
        public int gatewayValue() {
            // unused
            return 10;
        }
    },

    Rv1ReqFullReview("Designated Reviewer Request Full Review") {
        @Override
        public String taskDefKey() { return "rv1ReqFullReview"; }

        @Override
        public boolean isDefKey(String def) {
            return this.taskDefKey().equals(def);
        }

        @Override
        public boolean isStatus(String status) {
            return  statusName().equals(status);
        }

        @Override
        public int gatewayValue() {
            // unused
            return 10;
        }
    },

    Rv2Approval("Designated Reviewer Approval") {
        @Override
        public String taskDefKey() { return "rv2Approval"; }

        @Override
        public boolean isDefKey(String def) {
            return this.taskDefKey().equals(def);
        }

        @Override
        public boolean isStatus(String status) {
            return  statusName().equals(status);
        }

        @Override
        public int gatewayValue() {
            // unused
            return 10;
        }
    },

    Rv2Hold("Designated Reviewer Hold") {
        @Override
        public String taskDefKey() { return "rv2Hold"; }

        @Override
        public boolean isDefKey(String def) {
            return this.taskDefKey().equals(def);
        }

        @Override
        public boolean isStatus(String status) {
            return  statusName().equals(status);
        }

        @Override
        public int gatewayValue() {
            // unused
            return 10;
        }
    },

    Rv2ReqFullReview("Designated Reviewer Request Full Review") {
        @Override
        public String taskDefKey() { return "rv2ReqFullReview"; }

        @Override
        public boolean isDefKey(String def) {
            return this.taskDefKey().equals(def);
        }

        @Override
        public boolean isStatus(String status) {
            return  statusName().equals(status);
        }

        @Override
        public int gatewayValue() {
            // unused
            return 10;
        }
    },

    Rv3Approval("Designated Reviewer Approval") {
        @Override
        public String taskDefKey() { return "rv3Approval"; }

        @Override
        public boolean isDefKey(String def) {
            return this.taskDefKey().equals(def);
        }

        @Override
        public boolean isStatus(String status) {
            return  statusName().equals(status);
        }

        @Override
        public int gatewayValue() {
            // unused
            return 10;
        }
    },
    Rv3Hold("Designated Reviewer Hold") {
        @Override
        public String taskDefKey() { return "rv3Hold"; }

        @Override
        public boolean isDefKey(String def) {
            return this.taskDefKey().equals(def);
        }

        @Override
        public boolean isStatus(String status) {
            return  statusName().equals(status);
        }

        @Override
        public int gatewayValue() {
            // unused
            return 10;
        }
    },
    Rv3ReqFullReview("Designated Reviewer Request Full Review") {
        @Override
        public String taskDefKey() { return "rv3ReqFullReview"; }

        @Override
        public boolean isDefKey(String def) {
            return this.taskDefKey().equals(def);
        }

        @Override
        public boolean isStatus(String status) {
            return  statusName().equals(status);
        }

        @Override
        public int gatewayValue() {
            // unused
            return 10;
        }
    },

    AddNote("Add Note") {
        @Override
        public String taskDefKey() { return "addNote"; }

        @Override
        public boolean isDefKey(String def) {
            return this.taskDefKey().equals(def);
        }

        @Override
        public boolean isStatus(String status) {
            return  statusName().equals(status);
        }

        @Override
        public int gatewayValue() { return 10; }
    },

    M9Reminder("9 Month Reminder") {
        @Override
        public String taskDefKey() { return "m9Reminder"; }

        @Override
        public boolean isDefKey(String def) {
            return this.taskDefKey().equals(def);
        }

        @Override
        public boolean isStatus(String status) {
            return  statusName().equals(status);
        }

        @Override
        public int gatewayValue() { return 10; }
    },

    M6Reminder("6 Month Reminder") {
        @Override
        public String taskDefKey() { return "m6Reminder"; }

        @Override
        public boolean isDefKey(String def) {
            return this.taskDefKey().equals(def);
        }

        @Override
        public boolean isStatus(String status) {
            return  statusName().equals(status);
        }

        @Override
        public int gatewayValue() { return 10; }
    },

    M3Reminder("3 Month Reminder") {
        @Override
        public String taskDefKey() { return "m3Reminder"; }

        @Override
        public boolean isDefKey(String def) {
            return this.taskDefKey().equals(def);
        }

        @Override
        public boolean isStatus(String status) {
            return  statusName().equals(status);
        }

        @Override
        public int gatewayValue() { return 10; }
    },

    SOPreApproveA("Safety Office Pre-approve Appendix-A") {
        @Override
        public String taskDefKey() { return "soPreApproveA"; }

        @Override
        public boolean isDefKey(String def) {
            return this.taskDefKey().equals(def);
        }

        @Override
        public boolean isStatus(String status) {
            return  statusName().equals(status);
        }

        @Override
        public int gatewayValue() { return 10; }
    },

    SOHoldA("Safety Office Hold Appendix-A") {
        @Override
        public String taskDefKey() { return "soHoldA"; }

        @Override
        public boolean isDefKey(String def) {
            return this.taskDefKey().equals(def);
        }

        @Override
        public boolean isStatus(String status) {
            return  statusName().equals(status);
        }

        @Override
        public int gatewayValue() { return 10; }
    },

    SOPreApproveB("Safety Office Pre-approve Appendix-B") {
        @Override
        public String taskDefKey() { return "soPreApproveB"; }

        @Override
        public boolean isDefKey(String def) {
            return this.taskDefKey().equals(def);
        }

        @Override
        public boolean isStatus(String status) {
            return  statusName().equals(status);
        }

        @Override
        public int gatewayValue() { return 10; }
    },

    SOHoldB("Safety Office Hold Appendix-B") {
        @Override
        public String taskDefKey() { return "soHoldB"; }

        @Override
        public boolean isDefKey(String def) {
            return this.taskDefKey().equals(def);
        }

        @Override
        public boolean isStatus(String status) {
            return  statusName().equals(status);
        }

        @Override
        public int gatewayValue() { return 10; }
    };


    private String codeName;

    private IacucStatus(String s) {
        this.codeName = s;
    }

    private static final Set<String> KaputSet = new HashSet<String>();
    static {
        KaputSet.add("Release");
        KaputSet.add("UnRelease");
        KaputSet.add("Reject");
        KaputSet.add("Notify");
        //
        KaputSet.add("PreApprove");
        //
        KaputSet.add("ChgApprovalDate");
        KaputSet.add("ChgEffectivDate");
        KaputSet.add("ChgEndDate");
        KaputSet.add("ChgMeetingDate");
        //
        KaputSet.add("FullReviewReq");
        //
        KaputSet.add("HazardsApprove");
        //
        KaputSet.add("SOPreApproveA");
        KaputSet.add("SOPreApproveB");
        KaputSet.add("SOPreApproveC");
        KaputSet.add("SOPreApproveD");
        KaputSet.add("SOPreApproveE");
        KaputSet.add("SOPreApproveF");
        KaputSet.add("SOPreApproveG");
        KaputSet.add("SOPreApproveI");
        KaputSet.add("SOHoldA");
        KaputSet.add("SOHoldB");
        KaputSet.add("SOHoldC");
        KaputSet.add("SOHoldD");
        KaputSet.add("SOHoldE");
        KaputSet.add("SOHoldF");
        KaputSet.add("SOHoldG");
        KaputSet.add("SOHoldI");
        //
        KaputSet.add("VetPreApproveB");
        KaputSet.add("VetPreApproveC");
        KaputSet.add("VetPreApproveE");
        KaputSet.add("VetPreApproveF");
        KaputSet.add("VetHoldB");
        KaputSet.add("VetHoldC");
        KaputSet.add("VetHoldE");
        KaputSet.add("VetHoldF");
    }

    public String statusName() {
        return codeName;
    }

    public boolean isKaput(String name) {
        return KaputSet.contains(name);
    }

    public abstract String taskDefKey();

    public abstract boolean isDefKey(String def);

    public abstract boolean isStatus(String status);

    public abstract int gatewayValue();
}

