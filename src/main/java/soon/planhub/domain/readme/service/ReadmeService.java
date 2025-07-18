package soon.planhub.domain.readme.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import soon.planhub.domain.readme.repository.ReadmeRepository;

@RequiredArgsConstructor
@Service
public class ReadmeService {

    private final ReadmeRepository readmeRepository;

}