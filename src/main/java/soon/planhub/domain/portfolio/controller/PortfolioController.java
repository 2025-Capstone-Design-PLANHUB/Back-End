package soon.planhub.domain.portfolio.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import soon.planhub.domain.portfolio.service.PortfolioService;

@RequiredArgsConstructor
@RequestMapping("/api/v1/portfolios")
@RestController
public class PortfolioController {

    private final PortfolioService portfolioService;

}