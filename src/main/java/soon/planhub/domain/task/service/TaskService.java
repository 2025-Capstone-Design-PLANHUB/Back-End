package soon.planhub.domain.task.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import soon.planhub.domain.task.repository.TaskRepository;

@RequiredArgsConstructor
@Service
public class TaskService {

    private final TaskRepository taskRepository;

}