package testRunner;
import org.testng.TestNG;
import java.util.Collections;
    
    public class TestRunner {
        public static void main(String[] args) {
            TestNG testng = new TestNG();
            testng.setTestSuites(Collections.singletonList("testng.xml"));
            testng.run();
        }
    }
    

