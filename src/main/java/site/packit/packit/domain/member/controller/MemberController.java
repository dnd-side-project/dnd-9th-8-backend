package site.packit.packit.domain.member.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import site.packit.packit.domain.auth.info.CustomUserPrincipal;
import site.packit.packit.domain.image.service.ImageService;
import site.packit.packit.domain.member.dto.ChangeNicknameRequest;
import site.packit.packit.domain.member.dto.MemberWithTravelCountDto;
import site.packit.packit.domain.member.service.MemberService;
import site.packit.packit.domain.travel.service.TravelService;
import site.packit.packit.global.response.success.SingleSuccessApiResponse;
import site.packit.packit.global.response.success.SuccessApiResponse;

@RequestMapping("/api/members")
@RestController
public class MemberController {

    private final TravelService travelService;
    private final MemberService memberService;
    private final ImageService imageService;

    public MemberController(
            TravelService travelService,
            MemberService memberService,
            ImageService imageService
    ) {
        this.travelService = travelService;
        this.memberService = memberService;
        this.imageService = imageService;
    }

    @GetMapping
    public ResponseEntity<SingleSuccessApiResponse<MemberWithTravelCountDto>> getMember(
            @AuthenticationPrincipal CustomUserPrincipal principal
    ) {
        return ResponseEntity.ok(
                SingleSuccessApiResponse.of(
                        "성공적으로 회원 정보가 조회되었습니다.",
                        MemberWithTravelCountDto.from(
                                memberService.getMember(principal.getMemberId()),
                                travelService.getTravelCount(principal)
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
            @RequestBody String profileImageUrl
    ) {
        memberService.updateMemberProfileImageUrl(
                principal.getMemberId(),
                profileImageUrl
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