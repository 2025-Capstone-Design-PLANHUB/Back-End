package soon.planhub.infra.github.adapter.task.label;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import soon.planhub.domain.member.repository.MemberRepository;
import soon.planhub.domain.project.entity.Project;
import soon.planhub.domain.project.repository.ProjectRepository;
import soon.planhub.domain.task.port.out.TaskLabelPort;
import soon.planhub.domain.task.service.dto.label.TaskLabelInformation;
import soon.planhub.domain.task.service.dto.label.request.TaskLabelUpdateServiceRequest;
import soon.planhub.infra.github.dto.GithubIssueLabelCreateRequest;
import soon.planhub.infra.github.dto.GithubIssueLabelUpdateRequest;
import soon.planhub.infra.github.task.label.GithubIssueLabelCreator;
import soon.planhub.infra.github.task.label.GithubIssueLabelModifier;

@RequiredArgsConstructor
@Component
public class GithubTaskLabelAdapter implements TaskLabelPort {

    private final MemberRepository memberRepository;
    private final ProjectRepository projectRepository;
    private final GithubIssueLabelCreator githubIssueLabelCreator;
    private final GithubIssueLabelModifier githubIssueLabelModifier;

    @Override
    public void createLabel(TaskLabelInformation info, Long memberId, Long projectId) {
        String oauthToken = memberRepository.findById(memberId).getOauthToken();
        Project project = projectRepository.findById(projectId);

        githubIssueLabelCreator.createGithubIssueLabelAsync(GithubIssueLabelCreateRequest.from(oauthToken, info, project));
    }

    @Override
    public void updateLabel(TaskLabelUpdateServiceRequest request, Long memberId) {
        String oauthToken = memberRepository.findById(memberId).getOauthToken();
        Project project = projectRepository.findById(request.projectId());

        githubIssueLabelModifier.updateGithubIssueLabelAsync(GithubIssueLabelUpdateRequest.from(oauthToken, project, request));
    }

}