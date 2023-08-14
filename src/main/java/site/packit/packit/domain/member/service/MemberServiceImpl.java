package site.packit.packit.domain.member.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import site.packit.packit.domain.member.dto.CreateMemberDto;
import site.packit.packit.domain.member.dto.MemberDto;
import site.packit.packit.domain.member.entity.Member;
import site.packit.packit.domain.member.repository.MemberRepository;
import site.packit.packit.global.exception.ResourceNotFoundException;

import static site.packit.packit.domain.member.exception.MemberErrorCode.MEMBER_NOT_FOUND;

@Transactional
@Service
public class MemberServiceImpl
        implements MemberService {

    private final MemberRepository memberRepository;

    public MemberServiceImpl(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    @Override
    public Long createMember(CreateMemberDto request) {
        return memberRepository.save(request.toEntity()).getId();
    }

    @Transactional(readOnly = true)
    @Override
    public MemberDto getMember(Long memberId) {
        return MemberDto.from(
                getMemberEntity(memberId)
        );
    }

    @Override
    public void updateMemberNickname(
            Long memberId,
            String newNickname
    ) {
        Member savedMember = getMemberEntity(memberId);
        savedMember.updateNickname(newNickname);
    }

    @Override
    public void updateMemberProfileImageUrl(
            Long memberId,
            String newProfileImageUrl
    ) {
        Member savedMember = getMemberEntity(memberId);
        savedMember.updateProfileImageUrl(newProfileImageUrl);
    }

    @Override
    public void deleteMember(Long memberId) {
        Member savedMember = getMemberEntity(memberId);
        memberRepository.delete(savedMember);
    }

    private Member getMemberEntity(Long memberId) {
        return memberRepository.findById(memberId)
                .orElseThrow(() -> new ResourceNotFoundException(MEMBER_NOT_FOUND));
    }
}