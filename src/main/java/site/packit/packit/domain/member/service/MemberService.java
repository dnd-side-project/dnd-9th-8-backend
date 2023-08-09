package site.packit.packit.domain.member.service;

import site.packit.packit.domain.member.dto.CreateMemberDto;
import site.packit.packit.domain.member.entity.Member;

public interface MemberService {

    void createMember(CreateMemberDto request);

    Member getMemberEntityByPersonalId(String personalId);

}