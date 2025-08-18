package soon.planhub;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
@TruncateTables
@SpringBootTest
public abstract class IntegrationTestSupport {

}