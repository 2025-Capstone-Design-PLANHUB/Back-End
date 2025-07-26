package soon.planhub.global.security.jwt.filter;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class JwtFilterTestController {

    @GetMapping("api/test/secure")
    public String secureApi() {
        return "secure api";
    }

    @GetMapping("api/test/public")
    public String publicApi() {
        return "public api";
    }

}