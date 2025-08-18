package soon.planhub;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import soon.planhub.domain.auth.controller.AuthController;
import soon.planhub.domain.auth.service.AuthService;
import soon.planhub.domain.task.controller.TaskTemplateController;
import soon.planhub.domain.task.service.template.TaskTemplateService;
import soon.planhub.domain.team.controller.InvitationController;
import soon.planhub.domain.team.controller.TeamController;
import soon.planhub.domain.team.service.TeamService;
import soon.planhub.domain.team.service.invitation.InvitationService;
import soon.planhub.domain.teammember.controller.TeamMemberController;
import soon.planhub.domain.teammember.service.TeamMemberService;

@AutoConfigureMockMvc(addFilters = false)
@WebMvcTest(
    controllers = {
        AuthController.class,
        TeamController.class,
        TeamMemberController.class,
        InvitationController.class,
        TaskTemplateController.class
    })
public abstract class ControllerTestSupport {

    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    protected ObjectMapper objectMapper;

    @MockitoBean
    protected AuthService authService;

    @MockitoBean
    protected TeamService teamService;

    @MockitoBean
    protected TeamMemberService teamMemberService;

    @MockitoBean
    protected InvitationService invitationService;

    @MockitoBean
    protected TaskTemplateService taskTemplateService;

}