package soon.planhub.domain.portfolio.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import soon.planhub.domain.portfolio.entity.Portfolio;

public interface PortfolioJpaRepository extends JpaRepository<Portfolio, Long> {

}