package com.lot.smartcossiol.cucumber

import com.lot.smartcossiol.BackendApp
import io.cucumber.java.Before
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.web.WebAppConfiguration

@SpringBootTest
@WebAppConfiguration
@ContextConfiguration(classes = [BackendApp::class])
class CucumberContextConfiguration {

    @Before
    fun setup_cucumber_spring_context() {
        // Dummy method so cucumber will recognize this class as glue
        // and use its context configuration.
    }
}
