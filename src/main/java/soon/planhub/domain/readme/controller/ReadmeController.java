package soon.planhub.domain.readme.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import soon.planhub.domain.readme.service.ReadmeService;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/teams/{teamId}")
public class ReadmeController {

    private final ReadmeService readmeService;

}