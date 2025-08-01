package soon.planhub.domain.auth.controller.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import soon.planhub.domain.auth.service.dto.request.ReissueTokenServiceRequest;

@Builder
public record ReissueTokenRequest(

    @NotBlank(message = "액세스 토큰을 입력해주세요")
    String accessToken,

    @NotBlank(message = "리프레시 토큰을 입력해주세요")
    String refreshToken

) {

    public ReissueTokenServiceRequest toServiceRequest() {
        return ReissueTokenServiceRequest.builder()
            .accessToken(accessToken)
            .refreshToken(refreshToken)
            .build();
    }

}