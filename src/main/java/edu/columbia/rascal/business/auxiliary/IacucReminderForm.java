package edu.columbia.rascal.business.auxiliary;

import org.joda.time.DateTime;
import org.springframework.util.Assert;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class IacucReminderForm extends IacucTaskForm {

    @Override
    public Map<String, Object> getTaskVariables() {
        Map<String, Object> map = new HashMap<String, Object>();
        Date date=getDate();
        Assert.notNull(date);
        DateTime expiration=new DateTime(date);

        if( expiration.minusDays(90).isAfterNow() ) {
            // 90,60,30 reminder
            map.put("m9", true);
            map.put("m6", true);
            map.put("m3", true);
        } else if( expiration.minusDays(60).isAfterNow() ) {
            // 60, 30 reminder
            map.put("m9", false);
            map.put("m6", true);
            map.put("m3", true);
        } else if( expiration.minusDays(30).isAfterNow() ) {
            // 30 reminder
            map.put("m9", false);
            map.put("m6", false);
            map.put("m3", true);
        }
        // inclusive gateway will base on this output
        return map;
    }
}
