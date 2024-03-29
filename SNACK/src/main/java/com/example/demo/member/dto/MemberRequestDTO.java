package com.example.demo.member.dto;

import com.example.demo.member.entity.Member;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class MemberRequestDTO {

    @NotNull(message = "아이디를 입력해주세요")
    private String loginId;

    @NotNull(message = "비밀번호를 입력해주세요")
    private String pw;

    @NotNull(message = "이름을 입력해주세요")
    private String name;

    @NotNull(message = "사용할 닉네임을 입력해주세요")
    private String nickname;

    // LocalDate가 날짜만 저장!
    @Past(message = "과거의 날자만 가능합니다.")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Seoul") //날짜 포멧 바꾸기
    private LocalDate birth;

    public static MemberRequestDTO toMemberDTOWithLongId(Member member) {
        return MemberRequestDTO.builder()
                .loginId(member.getLoginId())
                .pw(member.getPw())
                .name(member.getName())
                .nickname(member.getNickname())
                .birth(member.getBirth())
                .build();
    }



}
