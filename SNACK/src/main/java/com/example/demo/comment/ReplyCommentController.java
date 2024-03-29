package com.example.demo.comment;

import com.example.demo.comment.dto.ReplyCommentResponseDTO;
import com.example.demo.comment.dto.ReplyRequestCommentDTO;
import com.example.demo.comment.entity.ReplyComment;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/comment/reply/")
public class ReplyCommentController {
    private final ReplyCommentService replyCommentService;

    @Autowired
    public ReplyCommentController(ReplyCommentService replyCommentService) {
        this.replyCommentService = replyCommentService;
    }

    @PostMapping("{commentId}")
    public ResponseEntity insert(@PathVariable Long commentId, @RequestBody ReplyRequestCommentDTO replyRequestCommentDTO) {
        ReplyComment replyComment = replyCommentService.insert(commentId, replyRequestCommentDTO);

        ReplyCommentResponseDTO responseDTO = ReplyCommentResponseDTO.getResponseDTO(replyComment);
        return ResponseEntity.ok().body(responseDTO);
    }

    @GetMapping("{commentId}")
    public ResponseEntity update(@PathVariable Long commentId, @Valid @RequestBody ReplyRequestCommentDTO replyCommentRequestDTO) {

        ReplyComment replyComment = replyCommentService.update(commentId, replyCommentRequestDTO);
        ReplyCommentResponseDTO responseDTO = ReplyCommentResponseDTO.getResponseDTO(replyComment);

        return ResponseEntity.ok().body(responseDTO);

    }
    // TODO delete test 필요

    @DeleteMapping("{commentId}")
    public ResponseEntity delete(@PathVariable Long commentId) {
        replyCommentService.delete(commentId);
        return ResponseEntity.ok().build();
    }

}
