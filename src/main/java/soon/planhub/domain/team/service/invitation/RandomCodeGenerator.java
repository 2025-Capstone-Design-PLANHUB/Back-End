package soon.planhub.domain.team.service.invitation;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Component;

@Component
public class RandomCodeGenerator implements CodeGenerator {

    private static final int INVITATION_CODE_LENGTH = 8;

    @Override
    public String generate() {
        return RandomStringUtils.randomAlphanumeric(INVITATION_CODE_LENGTH);
    }
}