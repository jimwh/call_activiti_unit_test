package edu.columbia.rascal.business.auxiliary;

import org.junit.Assert;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class IacucDistributeToDesignatedReviewerForm extends IacucTaskForm {

    @Override
    public Map<String, Object> getTaskVariables() {
        List<String>reviewerList=getReviewerList();
        Assert.assertNotNull(reviewerList);
        Assert.assertNotEquals(true, reviewerList.isEmpty());

        Map<String,Object> map=new HashMap<String, Object>();
        map.put("T1_OUT", IacucStatus.DistributeToDesignatedReviewer.gatewayValue());
        int suffix=0;
        for(String rv: reviewerList) {
            suffix += 1;
            map.put("u"+suffix, rv);
        }
        if(suffix < 2) {
            map.put("u2", null);
            map.put("u3", null);
        }else if(suffix<3) {
            map.put("u3", null);
        }
        // map.put("HAS_APPENDIX", true);
        return map;
    }

}
