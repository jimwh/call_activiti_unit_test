package edu.columbia.rascal.business.auxiliary;

import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class IacucCorrespondence {

    private String id;
    private String from;
    private String recipient;
    private String carbonCopy;
    private String subject;
    private String text;
    private Date creationDate;

    private String fromFirstLastNameUni;

    private static final Logger log = LoggerFactory.getLogger(IacucCorrespondence.class);

    public String getId() {
        return id;
    }

    public String getFrom() {
        return from;
    }

    public boolean isValidFrom() {return !StringUtils.isBlank(this.from);}

    public void setFrom(String fromUni) {
        this.from = fromUni;
    }

    public String getSubject() {
        return subject;
    }

    public boolean isValidSubject() {
        return !StringUtils.isBlank(this.subject);
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void apply() {
        if (creationDate == null) {
            creationDate = new Date();
            id = String.valueOf(creationDate.getTime());
        }
    }

    public void setFromFirstLastNameUni(String flu) {
        fromFirstLastNameUni = flu;
    }

    public String getFromFirstLastNameUni() {
        return fromFirstLastNameUni;
    }

    // it is for activity use, not for you
    public boolean isValid() {
        if (StringUtils.isBlank(this.id)) return false;
        if (!isValidFrom()) return false;
        if (!isValidRecipient()) return false;
        if (!isValidSubject()) return false;
        return true;
    }

    public String getRecipient() {
        return recipient;
    }

    public boolean isValidRecipient() {
        List<String> list = getRecipientAsList();
        return !list.isEmpty();
    }

    public void setRecipient(String recipient) {
        this.recipient = recipient;
    }

    public List<String> getRecipientAsList() {
        List<String> list = new ArrayList<String>();
        if (StringUtils.isBlank(recipient)) return list;
        String noSpaces = removeSpaces(recipient);
        list.addAll(Arrays.asList(noSpaces.split(",")));
        return list;
    }

    private String removeSpaces(String foo) {
        return foo.replaceAll("\\s+", "");
    }

    public List<String> getCarbonCopyAsList() {
        List<String> list = new ArrayList<String>();
        if (StringUtils.isBlank(carbonCopy)) return list;
        String noSpaces = removeSpaces(carbonCopy);
        list.addAll(Arrays.asList(noSpaces.split(",")));
        return list;
    }

    public boolean recipientContains(String uni) {
        return this.recipient == null ? false : this.recipient.contains(uni);
    }

    public String getCarbonCopy() {
        return carbonCopy;
    }

    public void setCarbonCopy(String carbonCopy) {
        this.carbonCopy = carbonCopy;
    }

    public boolean carbonCopyContains(String uni) {
        return this.carbonCopy == null ? false : this.carbonCopy.contains(uni);
    }

    // save data to activity table
    public Map<String, String> getProperties() {
        Map<String, String> map = new HashMap<String, String>();
        if (StringUtils.isBlank(id)) {
            return map;
        } else if (StringUtils.isBlank(from)) {
            return map;
        } else if (StringUtils.isBlank(recipient)) {
            return map;
        } else if (StringUtils.isBlank(subject)) {
            return map;
        } else if (StringUtils.isBlank(text)) {
            return map;
        } else if (creationDate==null) {
            return map;
        }

        map.put("id", id);
        map.put("from", from);
        map.put("recipient", recipient);
        map.put("subject", subject);
        map.put("text", text);
        map.put("carbonCopy", carbonCopy);
        DateTime dateTime=new DateTime(creationDate);
        // attention: this is joda DateTime string
        map.put("creationDate", dateTime.toString());
        return map;
    }

    public boolean setProperties(Map<String, String> map) {
        if (map == null || map.isEmpty()) {
            log.error("empty map");
            return false;
        }
        boolean bool = true;
        this.id = map.get("id");
        if(id==null) {
            log.error("no id");
            bool = false;
        }
        from = map.get("from");
        if (from==null) {
            log.error("no from");
            bool = false;
        }
        recipient = map.get("recipient");
        if( recipient==null) {
            log.error("no recipient");
            bool = false;
        }

        carbonCopy = map.get("carbonCopy");

        subject = map.get("subject");
        if( subject==null) {
            log.error("no subject");
            bool = false;
        }
        text = map.get("text");
        if( text==null) {
            log.error("no body");
            bool = false;
        }
        String jodaDateTimeString=map.get("creationDate");
        if (jodaDateTimeString != null) {
            creationDate = new DateTime(jodaDateTimeString).toDate();
        } else {
            log.error("no date");
            bool = false;
        }

        return bool;
    }

    public String getDateString() {
        if (creationDate == null) return "";
        DateTime dateTime = new DateTime(creationDate);
        return dateTime.toString("MM/dd/yyyy HH:mm:ss");
    }

    // just for front show purpose
    private boolean showCorrToUser = false;

    public boolean getShowCorrToUser() {
        return showCorrToUser;
    }

    public void setShowCorrToUser(String userId) {
        if (!StringUtils.isBlank(from)) {
            if (from.contains(userId)) {
                showCorrToUser = true;
            }
        }
        if (!StringUtils.isBlank(recipient)) {
            if (recipient.contains(userId)) {
                showCorrToUser = true;
            }
        }
        if (!StringUtils.isBlank(carbonCopy)) {
            if (carbonCopy.contains(userId)) {
                showCorrToUser = true;
            }
        }
    }

    
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("[id=").append(id)
                .append(",from=").append(from)
                .append(",to=").append(recipient)
                .append(",cc=").append(carbonCopy)
                .append(",subject=").append(subject)
                .append(",body=").append(text)
                .append(",date=").append(creationDate).append("]");
        return sb.toString();
    }

}
