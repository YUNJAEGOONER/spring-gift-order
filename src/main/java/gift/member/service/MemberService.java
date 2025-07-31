package gift.member.service;

import gift.exception.ErrorCode;
import gift.jwt.exception.LoginError;
import gift.member.dto.MemberRequestDto;
import gift.member.entity.Member;
import gift.member.exception.MemberNotFoundException;
import gift.member.exception.UnavailableEmailException;
import gift.member.repository.MemberRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class MemberService {

    private final MemberRepository memberRepository;

    public MemberService(MemberRepository memberRepository){
        this.memberRepository = memberRepository;
    }

    //멤버 회원 가입 -> 리포지토리에 저장
    public Member register(MemberRequestDto memberRequestDto){
        //중복을 확인
        memberRepository.findMemberByEmail(memberRequestDto.email()).ifPresent(member -> {
            throw new UnavailableEmailException(ErrorCode.UNAVAILABLE_EMAIL);
        });
        //중복된 이메일이 아니라면 회원가입을 진행
        Member createdMember = new Member(memberRequestDto.email(), memberRequestDto.password());
        return memberRepository.save(createdMember);
    }


    //로그인 기능 -> 이메일과 비밀번호가 일치하는지 확인하는 로직
    @Transactional(readOnly = true)
    public Member checkMember(String email, String password){
        return memberRepository.findMemberByEmailAndPassword(email, password).orElseThrow(() -> new LoginError(ErrorCode.LOGIN_UNAVAILABLE));
    }

    //특정 멤버를 조회하는 기능
    @Transactional(readOnly = true)
    public Member findMember(Long id){
        return memberRepository.findMemberById(id).orElseThrow(() -> new MemberNotFoundException(ErrorCode.MEMBER_NOT_FOUND));
    }

    @Transactional(readOnly = true)
    public Optional<Member> getMemberByEmail(String email){
        return memberRepository.findMemberByEmail(email);
    }


    //멤버의 정보 수정이 가능한지 확인하는 기능
    @Transactional(readOnly = true)
    public boolean checkAvailableModify(Long id, MemberRequestDto memberRequestDto){
        Optional<Member> member = memberRepository.findMemberByEmail(memberRequestDto.email());
        //이메일을 변경하는 경우 (이메일 + 비밀번호 모두 변경)
        if(member.isEmpty()){
            return true; //변경 가능
        }
        //비밀번호만 변경하는 경우(이메일은 변경하지 않음)
        String email = memberRepository.findMemberById(id).get().getEmail();
        return email.equals(memberRequestDto.email()); //변경 가능
    }

    //멤버의 정보를 수정하는 기능
    public Member modifyMember(Long id, MemberRequestDto memberRequestDto){
        Member member = findMember(id);
        member.changeInfo(memberRequestDto.email(), memberRequestDto.password());
        memberRepository.save(member);
        return member;
    }

    //멤버를 삭제하는 기능
    public void removeMember(Long id){
        memberRepository.removeMemberById(id);
    }

    @Transactional(readOnly = true)
    public List<Member> getAllMembers(){
        return memberRepository.findAll();
    }

}