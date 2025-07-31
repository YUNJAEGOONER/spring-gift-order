package gift.member.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record MemberRequestDto(
        @Email(message = "이메일 형식을 지켜야 합니다.")
        @NotBlank(message = "이메일을 입력해 주세요")
        String email,

        @NotBlank(message = "비밀번호를 입력해주세요")
        @Size(min = 8, message = "비밀번호는 8자리 이상이어야 합니다.")
        String password
) {
}
