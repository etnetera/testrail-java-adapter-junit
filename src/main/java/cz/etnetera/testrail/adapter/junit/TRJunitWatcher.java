package cz.etnetera.testrail.adapter.junit;

import com.codepine.api.testrail.TestRail;
import com.codepine.api.testrail.TestRailException;
import com.codepine.api.testrail.model.Result;
import com.codepine.api.testrail.model.ResultField;
import org.junit.rules.TestWatcher;
import org.junit.runner.Description;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

/**
 * To make it work you have to:
 * <br> <br>
 * 1) Provide necessary values as System properties or in testrail.properties <br>
 * <ul>
 * <li>testrail.url</li>
 * <li>testrail.username</li>
 * <li>testrail.password</li>
 * <li>testrail.projectid</li>
 * </ul>
 *
 * 2) Run {@link TRService#createRunForSuite(int)} in {@link org.junit.BeforeClass} method of Junit Suite
 * <br> <br>
 * 3) Provide this class as junit @Rule. Best attitude is to create superclass which all test inherit from.
 * <br> <br>
 * 4) Annotate tests wih {@link TRTest}
 */
public class TRJunitWatcher extends TestWatcher {

    private TRService trService = TRService.getInstance();

    private TestRail testRail = trService.getTestRail();

    private Result result;

    private LocalDateTime started;

    @Override
    protected void starting(Description description) {
        started = LocalDateTime.now();
    }

    @Override
    protected void succeeded(Description description) {
        result = new Result().setStatusId(1);
    }

    @Override
    protected void failed(Throwable e, Description description) {
        result = new Result().setStatusId(5)
                .setComment(e.toString());
    }

    @Override
    protected void finished(Description description) {
        TRTest info = getInfo(description);
        if (shouldWrite() && info != null && info.enabled()) {
            try {
                result.setCreatedOn(new Date()).setElapsed(Duration.between(started, LocalDateTime.now()).getSeconds() + "s");
                List<ResultField> customResultFields = testRail.resultFields().list().execute();
                testRail.results()
                        .addForCase(trService.getRun().getId(), info.testCaseId(), result, customResultFields)
                        .execute();
            } catch (TestRailException e) {
                System.err.println("Error while writting test result to Testrail: " + e);
            }
        } else {
            System.err.println("Result was not written to Testrail. Possible reasons: \n" +
                    "1) TRService#createRunForSuite was not called in @BeforeClass method of Junit Suite \n" +
                    "2) TRTest annotation was not provided to the test \n" +
                    "3) Writing disabled in annotation");
        }
    }

    private boolean shouldWrite() {
        return trService.isEnabled();
    }

    private TRTest getInfo(Description description) {
        return description.getAnnotation(TRTest.class);
    }
}
