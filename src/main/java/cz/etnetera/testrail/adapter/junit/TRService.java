package cz.etnetera.testrail.adapter.junit;

import com.codepine.api.testrail.TestRail;
import com.codepine.api.testrail.model.Run;

import java.util.Date;

/**
 * Singleton providing access to Testrail API.
 */
public class TRService {

    private static TRService trService;
    
    public static TRService getInstance() {
        if (trService == null) trService = new TRService();
        return trService;
    }
    
    private TRService() {
    }

    private TRPropertyLoader loader = TRPropertyLoader.getInstance();

    private TestRail testRail = TestRail.builder(loader.getUrl(), loader.getUsername(), loader.getPassword()).applicationName("seb").build();

    private Run run;

    private boolean enabled = false;

    /**
     * 
     * @param suiteId Id can be found in URL in TestRail.
     */
    public void createRunForSuite(int suiteId){
        Run run = new Run().setCreatedOn(new Date())
                .setSuiteId(suiteId);
        this.run = testRail.runs().add(loader.getProjectid(), run).execute();
        enabled = true;
    }

    public void createRunForSuite(Run run){
        this.run = testRail.runs().add(loader.getProjectid(), run).execute();
        enabled = true;
    }


    public TestRail getTestRail() {
        return testRail;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public Run getRun() {
        return run;
    }
}
