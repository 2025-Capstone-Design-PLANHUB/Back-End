package soon.planhub.domain.task.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class TaskService {

    private final TaskRepository taskRepository;

}