import info.leochoi.creditservice.CreditserviceIntegrationTestsTestBase;
import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;

@io.cucumber.junit.platform.engine.Cucumber
@RunWith(Cucumber.class)
@CucumberOptions(
    features = {"src/test/resources"},
    publish = true)
public class CreditserviceIntegrationTests extends CreditserviceIntegrationTestsTestBase {}
