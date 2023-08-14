package site.packit.packit.domain.member.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import site.packit.packit.domain.auth.info.CustomUserPrincipal;
import site.packit.packit.domain.member.dto.ChangeNicknameRequest;
import site.packit.packit.domain.member.dto.ChangeProfileImageRequest;
import site.packit.packit.domain.member.dto.MemberDto;
import site.packit.packit.domain.member.service.MemberService;
import site.packit.packit.global.response.success.SingleSuccessApiResponse;
import site.packit.packit.global.response.success.SuccessApiResponse;

@RequestMapping("/api/members")
@RestController
public class MemberController {

    private final MemberService memberService;

    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    @GetMapping
    public ResponseEntity<SingleSuccessApiResponse<MemberDto>> getMember(
            @AuthenticationPrincipal CustomUserPrincipal principal
    ) {
        return ResponseEntity.ok(
                SingleSuccessApiResponse.of(
                        "성공적으로 회원 정보가 조회되었습니다.",
                        memberService.getMember(principal.getMemberId()
                        )
                )
        );
    }

    @PutMapping("/nicknames")
    public ResponseEntity<SuccessApiResponse> updateMemberNickname(
            @AuthenticationPrincipal CustomUserPrincipal principal,
            @RequestBody ChangeNicknameRequest request
            ) {
        memberService.updateMemberNickname(
                principal.getMemberId(),
                request.newNickname()
        );

        return ResponseEntity.ok(
                SuccessApiResponse.of(
                        "성공적으로 닉네임이 변경되었습니다."
                )
        );
    }

    @PutMapping("/profile-images")
    public ResponseEntity<SuccessApiResponse> updateMemberProfileImage(
            @AuthenticationPrincipal CustomUserPrincipal principal,
            @RequestBody ChangeProfileImageRequest request
            ) {
        memberService.updateMemberProfileImageUrl(
                principal.getMemberId(),
                request.newProfileImageUrl()
        );

        return ResponseEntity.ok(
                SuccessApiResponse.of(
                        "성공적으로 프로필이미지가 변경되었습니다."
                )
        );
    }

    @DeleteMapping
    public ResponseEntity<SuccessApiResponse> deleteMember(
            @AuthenticationPrincipal CustomUserPrincipal principal
    ) {
        memberService.deleteMember(principal.getMemberId());

        return ResponseEntity.ok(
                SuccessApiResponse.of(
                        "성공적으로 회원 정보가 삭제되었습니다."
                )
        );
    }
}