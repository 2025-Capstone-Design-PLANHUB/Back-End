package soon.planhub.domain.project.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import soon.planhub.domain.project.entity.Project;

public interface ProjectJpaRepository extends JpaRepository<Project, Long> {

}