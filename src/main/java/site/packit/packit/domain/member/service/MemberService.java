package site.packit.packit.domain.member.service;

import site.packit.packit.domain.member.dto.CreateMemberDto;
import site.packit.packit.domain.member.dto.MemberDto;
import site.packit.packit.domain.member.entity.Member;

public interface MemberService {

    void createMember(CreateMemberDto request);

    Member getMemberEntityByPersonalId(String personalId);

    MemberDto getMember(Long memberId);

    void updateMemberNickname(String newNickname);

    void updateMemberProfileImageUrl(String newProfileImageUrl);

    void deleteMember();

    Member getAuthorizedMember();

}