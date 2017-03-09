package cz.etnetera.testrailwatcher;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Use this to annotate test to specify if its result should be written to TestRail Run (defaults to true). If it should be,
 * you must provide TestRail test case id this test is associated with. Id can be found in URL in TestRail.
 *<br> <br>
 * Works only if test is run as part of Junit Suite in which {@linkplain TRService#createRunForSuite} was called
 * in {@linkplain org.junit.BeforeClass} method.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface TRTest {

    boolean enabled() default true;

    int testCaseId();

}
