package site.packit.packit.domain.member.service;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import site.packit.packit.domain.member.dto.CreateMemberDto;
import site.packit.packit.domain.member.dto.MemberDto;
import site.packit.packit.domain.member.entity.Member;
import site.packit.packit.domain.member.repository.MemberRepository;
import site.packit.packit.global.exception.ResourceNotFoundException;

import static site.packit.packit.domain.member.exception.MemberErrorCode.MEMBER_NOT_FOUND;

@Service
public class MemberServiceImpl
        implements MemberService {

    private final MemberRepository memberRepository;

    public MemberServiceImpl(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    @Override
    public void createMember(CreateMemberDto request) {
        memberRepository.save(request.toEntity());
    }

    @Override
    public Member getMemberEntityByPersonalId(String personalId) {
        return memberRepository.findByPersonalId(personalId)
                .orElseThrow(() -> new ResourceNotFoundException(MEMBER_NOT_FOUND));
    }

    @Override
    public Member getAuthorizedMember() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return getMemberEntityByPersonalId(authentication.getName());
    }

    @Override
    public MemberDto getMember(Long memberId) {
        return MemberDto.from(
                memberRepository.findById(memberId)
                        .orElseThrow(() -> new ResourceNotFoundException(MEMBER_NOT_FOUND))
        );
    }

    @Override
    public void updateMemberNickname(
            String newNickname
    ) {
        Member member = getAuthorizedMember();
        member.updateNickname(newNickname);
    }

    @Override
    public void updateMemberProfileImageUrl(String newProfileImageUrl) {
        Member member = getAuthorizedMember();
        member.updateProfileImageUrl(newProfileImageUrl);
    }

    @Override
    public void deleteMember() {
        Member member = getAuthorizedMember();
        memberRepository.delete(member);
    }
}