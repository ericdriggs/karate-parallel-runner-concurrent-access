package examples;

import com.intuit.karate.Results;
import com.intuit.karate.Runner;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ParallelRunnerTest {

    @Test
    public void testParallel() {
        System.out.println("### ParallelTestRunner:testParallel");
        Results results = Runner.path("classpath:examples")
                .tags("~@ignore")
                .outputCucumberJson(true)
                .parallel(5);
        assertEquals(0, results.getFailCount(), results.getErrorMessages());
    }

}
