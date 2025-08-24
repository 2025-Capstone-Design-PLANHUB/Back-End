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
import soon.planhub.infra.github.dto.GithubIssueLabelDeleteRequest;
import soon.planhub.infra.github.dto.GithubIssueLabelUpdateRequest;
import soon.planhub.infra.github.task.label.GithubIssueLabelCreator;
import soon.planhub.infra.github.task.label.GithubIssueLabelModifier;
import soon.planhub.infra.github.task.label.GithubIssueLabelRemover;

@RequiredArgsConstructor
@Component
public class GithubTaskLabelAdapter implements TaskLabelPort {

    private final MemberRepository memberRepository;
    private final ProjectRepository projectRepository;
    private final GithubIssueLabelCreator githubIssueLabelCreator;
    private final GithubIssueLabelModifier githubIssueLabelModifier;
    private final GithubIssueLabelRemover githubIssueLabelRemover;

    @Override
    public void createLabel(TaskLabelInformation info, Long memberId, Long projectId) {
        Context context = getContext(memberId, projectId);
        githubIssueLabelCreator.createGithubIssueLabelAsync(GithubIssueLabelCreateRequest.from(context.oauthToken, info, context.project));
    }

    @Override
    public void updateLabel(TaskLabelUpdateServiceRequest request, Long memberId) {
        Context context = getContext(memberId, request.projectId());
        githubIssueLabelModifier.updateGithubIssueLabelAsync(GithubIssueLabelUpdateRequest.from(context.oauthToken, context.project, request));
    }

    @Override
    public void deleteLabel(Long memberId, Long projectId, String title) {
        Context context = getContext(memberId, projectId);
        githubIssueLabelRemover.deleteGithubIssueLabelAsync(GithubIssueLabelDeleteRequest.from(context.oauthToken, context.project, title));
    }

    private Context getContext(Long memberId, Long projectId) {
        String oauthToken = memberRepository.findById(memberId).getOauthToken();
        Project project = projectRepository.findById(projectId);
        return new Context(oauthToken, project);
    }

    private record Context(String oauthToken, Project project) {
    }

}