package site.packit.packit.domain.member.service;

import site.packit.packit.domain.member.dto.CreateMemberDto;
import site.packit.packit.domain.member.dto.MemberDto;

public interface MemberService {

    Long createMember(CreateMemberDto dto);

    MemberDto getMember(Long memberId);

    void updateMemberNickname(Long memberId, String newNickname);

    void updateMemberProfileImageUrl(Long memberId, String newProfileImageUrl);

    void deleteMember(Long memberId);

}