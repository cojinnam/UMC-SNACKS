package com.example.demo.comment.entity;

import static jakarta.persistence.FetchType.LAZY;

import com.example.demo.BaseTimeEntity;
import com.example.demo.member.entity.Member;
import com.example.demo.socialboard.entity.SocialBoard;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

@Entity
@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Comment extends BaseTimeEntity {

    @GeneratedValue(strategy = GenerationType.AUTO)
    @Id
    @Column(name = "COMMENT_ID")
    private Long id;

    @Column(name = "CONTENT")
    private String content;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "MEMBER_ID")
    private Member writer;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "BOARD_ID")
    private SocialBoard board;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "comment")
    private List<ReplyComment> replies = new ArrayList<>();

}
