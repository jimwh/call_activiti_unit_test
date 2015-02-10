package edu.columbia.rascal.business.auxiliary;

import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

public class IacucTaskForm implements IacucTaskFormBase, Comparable<IacucTaskForm> {

    private static final Logger log = LoggerFactory.getLogger(IacucTaskForm.class);

    private String bizKey;
    private String author;
    private String taskId;
    private String taskDefKey;
    private String taskName;
    private String comment;
    private String commentId;
    private String snapshotId;
    private Date endTime;
    private IacucCorrespondence iacucCorrespondence;
    private List<String> reviewerList = new ArrayList<String>();
    private Date date;

    public List<String> getReviewerList() {
        return reviewerList;
    }

    public void setReviewerList(List<String> list) {
        reviewerList = list;
    }

    public String getBizKey() {
        return bizKey;
    }

    public void setBizKey(String bizKey) {
        this.bizKey = bizKey;
    }

    public void setEndTime(Date date) {
        this.endTime = date;
    }
    public Date getEndTime() { return this.endTime; }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public String getTaskDefKey() {
        return taskDefKey;
    }

    public void setTaskDefKey(String taskDefKey) {
        this.taskDefKey = taskDefKey;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getSnapshotId() {
        return snapshotId;
    }

    public void setSnapshotId(String snapshotId) {
        this.snapshotId = snapshotId;
    }

    public void setIacucCorrespondence(IacucCorrespondence corr) {
        this.iacucCorrespondence = corr;
    }

    public IacucCorrespondence getIacucCorrespondence() {
        return this.iacucCorrespondence;
    }


    public Date getDate() {
        return this.date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getCommentId() { return this.commentId; }
    public void setCommentId(String cid) { this.commentId=cid; }

    public String getMeetingDateString() {
        if (date == null) return null;
        DateTime dateTime = new DateTime(date);
        return dateTime.toString("MM/dd/yyyy");
    }

    public String getEndTimeString() {
        if (endTime == null) return null;
        DateTime dateTime = new DateTime(endTime);
        return dateTime.toString("MM/dd/yyyy HH:mm:ss");
    }

    /*
    @Override
    public Map<String, Object> getListCorrMap() {
        Map<String, Map<String, Object>> treeMap = new TreeMap<String, Map<String, Object>>();
        if (iacucCorrespondence.isEmpty()) return treeMap;
        for (IacucCorrespondence corr : this.iacucCorrespondence) {
            Map<String, Object>map=corr.getProperties();
            if(!map.isEmpty())
                treeMap.put(corr.getId(), map);
        }
        return treeMap;
    }
    */

    @Override
    public Map<String, String> getProperties() {
        Map<String, String> map = new HashMap<String, String>();

        // map.put("comment", this.comment);
        map.put("commentId", this.commentId);

        map.put("author", this.author);
        map.put("taskName", this.taskName);
        map.put("taskDefKey", this.taskDefKey);

        if (date != null) {
            DateTime dateTime = new DateTime(this.date);
            map.put("date", dateTime.toString());
        }
        if(!reviewerList.isEmpty()) {
            for(int i=0; i<reviewerList.size(); i++) {
                map.put("u"+i, reviewerList.get(i));
            }
        }
        //...
        return map;
    }

    @Override
    public void setProperties(Map<String, String> map) {
        if (map == null || map.isEmpty()) return;

        this.taskName = map.get("taskName");
        this.taskDefKey = map.get("taskDefKey");
        // this.comment = map.get("comment");
        this.commentId = map.get("commentId");
        this.author = map.get("author");
        this.snapshotId = map.get("snapshotId");
        if (map.get("date") != null) {
            String ms = map.get("date");
            DateTime dateTime = new DateTime(ms);
            this.date = dateTime.toDate();
        }
        if(map.get("u0")!=null) {
            reviewerList.add(map.get("u0"));
        }
        if(map.get("u1")!=null) {
            reviewerList.add(map.get("u1"));
        }
        if(map.get("u2")!=null) {
            reviewerList.add(map.get("u2"));
        }
        //...
    }

    public void setProperty(String id, String value) {
        if ("author".equals(id)) {
            this.author = value;
        } else if("commentId".equals(id)) {
            this.commentId = value;
        } else if ("comment".equals(id)) {
            this.comment = value;
        } else if ("taskName".equals(id)) {
            this.taskName = value;
        } else if ("taskDefKey".equals(id)) {
            this.taskDefKey = value;
        }
    }

    @Override
    public Map<String, Object> getTaskVariables() {
        return null;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("[taskName=").append(this.taskName)
                .append(",taskDefKey=").append(this.taskDefKey)
                .append(",author=").append(this.author);
        if (comment != null) {
            sb.append(",comment=").append(this.comment);
        }
        if (date != null) {
            sb.append(",date=").append(getMeetingDateString());
        }
        if (!reviewerList.isEmpty()) {
            sb.append(",reviewerList").append(reviewerList.toString());
        }

        sb.append(",endTime=").append(getEndTimeString()).append("]");
        if (iacucCorrespondence==null) {
            return sb.toString();
        }
        sb.append("\n").append(iacucCorrespondence.toString()).append("\n");
        return sb.toString();
    }

    @Override
    public int compareTo(IacucTaskForm itf) {
        if( this.endTime==null && itf.getEndTime()!=null) return -1;
        if( this.endTime!=null && itf.getEndTime()==null) return 1;

        return this.endTime.compareTo(itf.getEndTime());
    }
}
