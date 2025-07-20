package soon.planhub.domain.task.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import soon.planhub.domain.task.repository.TaskTemplateRepository;

@RequiredArgsConstructor
@Service
public class TaskTemplateService {

    private final TaskTemplateRepository taskTemplateRepository;

}