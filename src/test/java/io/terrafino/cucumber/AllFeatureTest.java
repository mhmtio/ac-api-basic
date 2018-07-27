package io.terrafino.cucumber;

import org.junit.runner.RunWith;
import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;

@RunWith(Cucumber.class)
@CucumberOptions(
        features = "src/test/features/"
        ,glue={"io.terrafino.cucumber.steps"}
)

public class AllFeatureTest {

}