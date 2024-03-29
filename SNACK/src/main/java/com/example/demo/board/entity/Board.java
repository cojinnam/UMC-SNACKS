package com.example.demo.board.entity;

import com.example.demo.BaseTimeEntity;
import com.example.demo.Games;
import com.example.demo.chat.Entity.ChatRoom;
import com.example.demo.member.entity.Member;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Transient;
import java.time.LocalDateTime;
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
public class Board extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "BOARD_ID")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "WRITER_ID")
    private Member writer;

    @Column(name = "TITLE")
    private String title;

    @Column(name = "GAME_TITLE")
    @Enumerated(EnumType.STRING)
    private Games gameTitle;

    @Column(name = "ETC_TITLE")
    private String etcTitle;

    @OneToMany(mappedBy = "board")
    @JsonIgnoreProperties({"board"})
    private List<BoardMember> boardMembers = new ArrayList<>();

    @Column(name = "DATE")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime date;

    @OneToOne(mappedBy = "board", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnoreProperties({"board"})
    private ChatRoom chatRoom;

    @Column(name = "NOTICE")
    private String notice;

    @Column(name = "MAX_COUNT")
    private Integer maxCount;

    @Column(name = "AUTO_CHECK")
    private boolean autoCheckIn;

    @Transient
    private Integer memberCount;


}
