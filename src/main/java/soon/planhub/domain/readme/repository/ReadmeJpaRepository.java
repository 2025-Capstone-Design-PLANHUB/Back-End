package soon.planhub.domain.readme.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import soon.planhub.domain.readme.entity.Readme;

public interface ReadmeJpaRepository extends JpaRepository<Readme, Long> {

}