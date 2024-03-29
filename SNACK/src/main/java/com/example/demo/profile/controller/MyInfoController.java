package com.example.demo.profile.controller;

import com.example.demo.member.entity.Member;
import com.example.demo.member.repository.MemberRepository;
import com.example.demo.profile.UserRequestException;
import com.example.demo.profile.dto.follow.FolloweeListResponseDto;
import com.example.demo.profile.dto.follow.FollowerListResponseDto;
import com.example.demo.profile.dto.myInfo.MyInfoResponseDto;
import com.example.demo.profile.dto.profileUpdate.ProfileReadResponseDto;
import com.example.demo.profile.dto.profileUpdate.ProfileUpdateRequestDto;
import com.example.demo.profile.dto.profileUpdate.ProfileUpdateResponseDto;
import com.example.demo.profile.service.FollowService;
import com.example.demo.profile.service.MyInfoService;
import com.example.demo.profile.service.UserInfoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;


@RequiredArgsConstructor
@RestController
public class MyInfoController {

    private final MyInfoService myInfoService;
    private final FollowService followService;
    private final UserInfoService userInfoService;
    private final MemberRepository memberRepository;

    //내 프로필 보기
    @GetMapping("/myinfo")
    public MyInfoResponseDto readMyInfo(Authentication authentication){
        if(authentication == null)
            throw new UserRequestException("로그인이 필요한 서비스입니다.");
        else
            return myInfoService.readMyInfo(memberRepository.findById(Long.valueOf(authentication.getName())).get());
    }

    //팔로잉
    @PostMapping("/follow/{userId}")
    public Boolean followUser(@PathVariable Long userId, Authentication authentication){
        if(authentication == null)
            throw new UserRequestException("로그인이 필요한 서비스입니다.");
        else
            if (Long.valueOf(authentication.getName()).equals(userId)){
                throw new UserRequestException("본인을 팔로우할 수 없습니다.");
            }
            else
                return followService.followUser(userId, memberRepository.findById(Long.valueOf(authentication.getName())).get());
    }

    //다른 사람 프로필 보기
    @GetMapping("/myinfo/{userId}")
    public MyInfoResponseDto readUserInfo(@PathVariable Long userId){
        Member tempMember = memberRepository.findById(userId).orElseThrow(() -> new UserRequestException("회원이 존재하지 않습니다."));
        return userInfoService.readUserInfo(tempMember);
    }

    //다른 사람 프로필 보기(닉네임)
    @GetMapping("/myinfo/nickname/{nickname}")
    public MyInfoResponseDto readUserInfo(@PathVariable String nickname){
        Member tempMember = memberRepository.findByNickname(nickname).orElseThrow(() -> new UserRequestException("회원이 존재하지 않습니다."));
        return userInfoService.readUserInfo(tempMember);
    }

    //유저 프로필 편집 페이지(닉네임, 프로필사진, 소개글)
    @GetMapping("/user")
    public ProfileReadResponseDto readProfile(Authentication authentication) {
        if(authentication == null)
            throw new UserRequestException("로그인이 필요한 서비스입니다.");
        else
            return myInfoService.readProfile(memberRepository.findById(Long.valueOf(authentication.getName())).get());
    }

    //유저 프로필 편집
    @PutMapping("/user")
    public ProfileUpdateResponseDto updateProfile(@RequestBody @Valid ProfileUpdateRequestDto profileUpdateRequestDto, Authentication authentication){
        if(authentication == null)
            throw new UserRequestException("로그인이 필요한 서비스입니다.");
        else
            return myInfoService.updateProfile(profileUpdateRequestDto, memberRepository.findById(Long.valueOf(authentication.getName())).get());
    }

    //팔로워 목록(나를 팔로우 하는 사람들)
    @GetMapping("/follower/{userId}")
    public FollowerListResponseDto getFollowerList(Authentication authentication, @PathVariable Long userId){
        if(authentication == null)
            throw new UserRequestException("로그인이 필요한 서비스입니다.");
        else
            return followService.getFollowerList(memberRepository.findById(userId).get());
    }

    //팔로우 목록(내가 팔로우 하는 사람들)
    @GetMapping("/followee/{userId}")
    public FolloweeListResponseDto getFolloweeList(Authentication authentication, @PathVariable Long userId){
        if(authentication == null)
            throw new UserRequestException("로그인이 필요한 서비스입니다.");
        else
            return followService.getFolloweeList(memberRepository.findById(userId).get());
    }
}
