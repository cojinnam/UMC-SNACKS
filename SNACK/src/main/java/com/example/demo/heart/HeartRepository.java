package com.example.demo.heart;

import com.example.demo.member.entity.Member;
import com.example.demo.socialboard.entity.SocialBoard;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface HeartRepository extends JpaRepository<Heart, Long> {

    Optional<Heart> findByMemberAndSocialBoard(Member member, SocialBoard socialBoard);

}
