package site.packit.packit.domain.member.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
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

    @GetMapping("/current-members")
    public ResponseEntity<SingleSuccessApiResponse<MemberDto>> getMemberProfile() {
        return ResponseEntity.ok(
                SingleSuccessApiResponse.of(
                        "성공적으로 조회되었습니다.",
                        MemberDto.from(
                                memberService.getAuthorizedMember()
                        )
                )
        );
    }

    @PutMapping("/nicknames")
    public ResponseEntity<SuccessApiResponse> updateMemberNickname(String newNickname) {
        memberService.updateMemberNickname(newNickname);

        return ResponseEntity.ok(
                SuccessApiResponse.of(
                        "성공적으로 닉네임이 변경되었습니다."
                )
        );
    }

    @PutMapping("/profile-images")
    public ResponseEntity<SuccessApiResponse> updateMemberProfileImage(String newProfileImageUrl) {
        memberService.updateMemberNickname(newProfileImageUrl);

        return ResponseEntity.ok(
                SuccessApiResponse.of(
                        "성공적으로 프로필이미지가 변경되었습니다."
                )
        );
    }

    @DeleteMapping("/")
    public ResponseEntity<SuccessApiResponse> deleteMember() {
        memberService.deleteMember();

        return ResponseEntity.ok(
                SuccessApiResponse.of(
                        "성공적으로 회원 정보가 삭제되었습니다."
                )
        );
    }
}