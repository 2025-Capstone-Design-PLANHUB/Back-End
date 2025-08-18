package soon.planhub.domain.project.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import soon.planhub.domain.project.entity.Project;
import soon.planhub.global.exception.common.EntityNotFoundException;
import soon.planhub.global.exception.dto.ErrorDetail;

@RequiredArgsConstructor
@Repository
public class ProjectRepository {

    private final ProjectJpaRepository projectJpaRepository;

    public void save(Project project) {
        projectJpaRepository.save(project);
    }

    public Project findById(Long projectId) {
        return projectJpaRepository.findById(projectId)
            .orElseThrow(() -> new EntityNotFoundException(ErrorDetail.PROJECT_NOT_FOUND));
    }

}