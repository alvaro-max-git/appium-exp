package tests;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.nttdata.drivers.AndroidDriverManager;


public class AndroidTestBase {

    @BeforeEach
    void setUp () {
        AndroidDriverManager.create();
    }

    @AfterEach
    void tearDown() {
        AndroidDriverManager.quit();
    }

}
