package soon.planhub.domain.task.service.label;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import soon.planhub.domain.task.port.out.TaskLabelPort;
import soon.planhub.domain.task.service.dto.label.request.TaskLabelCreateServiceRequest;
import soon.planhub.global.exception.common.InvalidRequest;
import soon.planhub.global.exception.dto.ErrorDetail;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.willThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class TaskLabelServiceTest {

    @InjectMocks
    private TaskLabelService taskLabelService;

    @Mock
    private TaskLabelCreator taskLabelCreator;

    @Mock
    private TaskLabelValidator taskLabelValidator;

    @Mock
    private TaskLabelPort taskLabelPort;

    @DisplayName("태스크 라벨을 생성한다.")
    @Test
    void createLabel() {
        // given
        var request = TaskLabelCreateServiceRequest.builder()
            .teamId(1L)
            .projectId(1L)
            .title("New Label")
            .description("This is a new label")
            .color("#FFFFFF")
            .build();

        // when
        Long savedLabelId = taskLabelService.createLabel(request, 1L);

        // then
        verify(taskLabelValidator).validateLabelNotExists(request.title(), request.projectId());
        verify(taskLabelPort).createLabel(request.toInfo(), 1L, request.projectId());
        verify(taskLabelCreator).createLabel(request.toInfo(), request.projectId());
    }

    @DisplayName("이미 존재하는 라벨이라면 예외가 발생하고, 라벨을 생성하지 않는다.")
    @Test
    void createLabelWhenLabelAlreadyExists() {
        // given
        var request = TaskLabelCreateServiceRequest.builder()
            .teamId(1L)
            .projectId(1L)
            .title("Existing Label")
            .description("This label already exists")
            .color("#FFFFFF")
            .build();

        willThrow(new InvalidRequest())
            .given(taskLabelValidator)
            .validateLabelNotExists(request.title(), request.projectId());


        // expected
        assertThatThrownBy(() -> taskLabelService.createLabel(request, 1L))
            .isInstanceOf(InvalidRequest.class)
            .hasMessage(ErrorDetail.INVALID_REQUEST.getMessage());

        verify(taskLabelPort, never()).createLabel(request.toInfo(), 1L, request.projectId());
        verify(taskLabelCreator, never()).createLabel(request.toInfo(), request.projectId());
    }

}