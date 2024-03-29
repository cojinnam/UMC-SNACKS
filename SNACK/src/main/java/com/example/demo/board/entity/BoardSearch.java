package com.example.demo.board.entity;


import com.example.demo.Games;
import com.example.demo.member.entity.Member;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BoardSearch {
    private String title;
    private Games gameTitle;
    private String etcTitle;
    private List<Member> members = new ArrayList<>();
    private LocalDateTime date;
    private String notice;
    private Integer maxCount;
    private int memberCount;


}
