package com.example.demo.profile.service;


import com.example.demo.member.entity.Member;
import com.example.demo.member.repository.MemberRepository;
import com.example.demo.profile.UserRequestException;
import com.example.demo.profile.domain.follow.Follow;
import com.example.demo.profile.domain.follow.FollowRepository;
import com.example.demo.profile.dto.follow.FolloweeListResponseDto;
import com.example.demo.profile.dto.follow.FollowerListResponseDto;
import com.example.demo.profile.dto.follow.MyInfoFolloweeResponseDto;
import com.example.demo.profile.dto.follow.MyInfoFollowerResponseDto;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class FollowService {

    private final MemberRepository memberRepository;
    private final FollowRepository followRepository;

    @Transactional
    public Boolean followUser(Long userId, Member followingMember) {
        //User = follow를 하는 사람 (following)
        //userId = follow를 받는 사람 (followed)
        Member followedMember = memberRepository.findById(userId).orElseThrow(() -> new UserRequestException("팔로우 하려는 회원이 존재하지 않습니다."));
        //Member followedMember = memberRepository.findById(userId).get();
        //Member followingMember = memberRepository.findById(member.getId()).get();

        for (Follow follow : followedMember.getFolloweeList()) { //follow 받는 사람의 팔로워 리스트 중 하나
            if(follow.getFollower().getId().equals(followingMember.getId())){ // 그 리스트에 이미 팔로우 하려는 유저가 있다면
                followedMember.getFolloweeList().remove(follow); // 언팔.
                followingMember.getFollowerList().remove(follow);
                follow.disconnectFollowee();
                follow.disconnectFollower();

                followingMember.hasUnFollowing();
                followedMember.hasUnFollowed();

                followRepository.delete(follow);
                return false;
            }
        }
        followRepository.save(Follow.builder()
                .follower(followingMember)
                .followee(followedMember)
                .build());
        followingMember.hasFollowing();
        followedMember.hasFollowed();
        return true;
    }

    public FollowerListResponseDto getFollowerList(Member member){
        List<MyInfoFollowerResponseDto> myInfoFollowerResponseDtoList = new ArrayList<>();
        for (Follow follow : member.getFolloweeList()){
            Member followee = follow.getFollower();
            myInfoFollowerResponseDtoList.add(MyInfoFollowerResponseDto.of(followee));
        }
        return FollowerListResponseDto.builder()
                .myInfoFollowerResponseDtoList(myInfoFollowerResponseDtoList)
                .build();
    }

    public FolloweeListResponseDto getFolloweeList(Member member){
        List<MyInfoFolloweeResponseDto> myInfoFolloweeResponseDtoList = new ArrayList<>();
        for (Follow follow : member.getFollowerList()){
            Member followee = follow.getFollowee();
            myInfoFolloweeResponseDtoList.add(MyInfoFolloweeResponseDto.of(followee));
        }
        return FolloweeListResponseDto.builder()
                .myInfoFolloweeResponseDtoList(myInfoFolloweeResponseDtoList)
                .build();
    }
}
