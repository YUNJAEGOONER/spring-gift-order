package gift.member.repository;

import gift.member.entity.Member;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findMemberByEmailAndPassword(String email, String password);
    Optional<Member> findMemberByEmail(String email);
    Optional<Member> findMemberById(Long id);
    void removeMemberById(Long memberId);
}
