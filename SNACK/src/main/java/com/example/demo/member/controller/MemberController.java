package com.example.demo.member.controller;

import com.example.demo.board.dto.BoardResponseDTO;
import com.example.demo.board.enrollment.EnrollmentService;
import com.example.demo.board.entity.Board;
import com.example.demo.member.dto.MemberPublicResponse;
import com.example.demo.member.dto.MemberRequestDTO;
import com.example.demo.member.dto.MemberResponseDTO;
import com.example.demo.member.entity.Member;
import com.example.demo.member.service.MemberService;
import com.example.demo.profile.domain.userinfo.UserInfo;
import com.example.demo.profile.domain.userinfo.UserInfoRepository;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/member/")
public class MemberController {

    private final MemberService memberService;
    private final EnrollmentService enrollmentService;
    private final UserInfoRepository userInfoRepository;

    // 회원가입
    @PostMapping("save")
    public ResponseEntity<?> save(@RequestBody @Validated MemberRequestDTO memberRequestDTO, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            List<String> errors = bindingResult.getAllErrors().stream().map(e -> e.getDefaultMessage()).collect(Collectors.toList());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
        }
        UserInfo emptyUserInfo = new UserInfo(0L, 0L, 0L);

        memberService.save(memberRequestDTO, emptyUserInfo);

        return ResponseEntity.status(HttpStatus.ACCEPTED).body(true);
        // 반납할때는 true값보다 생성된 회원 id를 보내주는게 맞지않을까요?

    }

    @AllArgsConstructor
    @Getter
    @Setter
    class Token {
        private String token;
    }

    @PostMapping("login")
    public ResponseEntity<Token> login(@RequestBody Map<String, String> member) {
        String loginResult = memberService.login(member);

        if (loginResult != null) {
            Token token = new Token(loginResult);
            return ResponseEntity.ok().body(token);
        } else {
            Token tokenfalse = new Token(null);

            return ResponseEntity.ok().body(tokenfalse);

        }

    }

    //아래 만든게 회원가입할때 id 복수 체크 부분
    @GetMapping("/save/{user_id}")
    public ResponseEntity<Boolean> checkIdExist(@PathVariable String user_id) {
        Member member = memberService.findMemberByLoginId(user_id);

        return member != null ? ResponseEntity.ok().body(true) : ResponseEntity.ok().body(false);
    }


    // TODO 테스트 필요
    @GetMapping("attending")
    public ResponseEntity<List> searchAttendingBoards(Authentication authentication) {
        // authentication  멤버 식별 번호
        Long memberId = Long.parseLong(authentication.getName());

        List<Board> boards = memberService.findBoardsByMemberId(memberId);
        List<BoardResponseDTO> responseDTOList = boards.stream()
                .map(BoardResponseDTO::getBuild)
                .collect(Collectors.toList());

        return ResponseEntity.ok(responseDTOList);

    }

    @GetMapping("{memberLoginId}")
    public ResponseEntity<?> searchMemberId(@PathVariable String memberLoginId, Authentication authentication) {
        Member member = memberService.findMemberByLoginId(memberLoginId);
        Member authMember = memberService.findMemberById(authentication.getName());

        if (member.equals(authMember)) {
            MemberResponseDTO memberResponseDTO = MemberResponseDTO.toResponseEntity(member);
            return ResponseEntity.ok().body(memberResponseDTO);
        } else {
            MemberPublicResponse memberPublicResponse = MemberPublicResponse.toResponseEntity(member);
            return ResponseEntity.ok().body(memberPublicResponse);
        }
    }

    @GetMapping("like/{socialBoardId}")
    public ResponseEntity<Result> readMemberLikedBoard(@PathVariable Long socialBoardId, Authentication authentication){
        // 관리자 확인 메서드 추가?
        List<MemberDTO> collect = memberService.findMembersLikedBoardByBoardId(socialBoardId).stream()
                .map(m -> new MemberDTO(m.getId()))
                .collect(Collectors.toList());
        return ResponseEntity.ok().body(new Result(collect));

    }

    @Data
    @AllArgsConstructor
    static class Result<T>{
        private T data;
    }

    @Data
    @AllArgsConstructor
    static class MemberDTO{
        private Long id;
    }
}






