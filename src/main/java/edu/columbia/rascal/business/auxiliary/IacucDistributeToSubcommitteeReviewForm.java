package edu.columbia.rascal.business.auxiliary;

import org.junit.Assert;

import java.util.HashMap;
import java.util.Map;

public class IacucDistributeToSubcommitteeReviewForm extends IacucTaskForm {

    @Override
    public Map<String,Object>getTaskVariables() {
        Assert.assertNotNull(getDate());
        Map<String,Object> map=new HashMap<String, Object>();
        map.put("T1_OUT", IacucStatus.DistributeToSubcommittee.gatewayValue());
        return map;
    }

}
