package com.maptracker.issuemap.domain.comment.service;

import com.maptracker.issuemap.common.jwt.CustomUserDetails;
import com.maptracker.issuemap.domain.comment.dto.IssueCommentCreateDto;
import com.maptracker.issuemap.domain.comment.dto.IssueCommentResponseDto;
import com.maptracker.issuemap.domain.comment.entity.SubIssueComment;
import com.maptracker.issuemap.domain.comment.exception.MyErrorCode;
import com.maptracker.issuemap.domain.comment.exception.MyException;
import com.maptracker.issuemap.domain.comment.repository.SubIssueCommentRepository;
import com.maptracker.issuemap.domain.issue.entity.SubIssue;
import com.maptracker.issuemap.domain.issue.repository.SubIssueRepository;
import com.maptracker.issuemap.domain.user.entity.User;
import com.maptracker.issuemap.domain.user.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.junit.jupiter.api.Assertions.*;
import static org.assertj.core.api.Assertions.assertThat;
import java.util.Optional;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import org.junit.jupiter.api.extension.ExtendWith;


@ExtendWith(MockitoExtension.class)
class SubIssueCommentServiceTest {

    @InjectMocks
    private SubIssueCommentService commentService;

    @Mock
    private SubIssueRepository subIssueRepository;

    @Mock
    private SubIssueCommentRepository subIssueCommentRepository;

    @Mock
    private UserRepository userRepository;

    @Test
    @DisplayName("댓글 생성 성공: 이슈에 댓글을 생성하면 댓글 정보가 반환된다")
    void createCommentSuccess() {
        // Given
        Long issueId = 1L;
        String content = "테스트 댓글 내용";

        IssueCommentCreateDto requestDto = new IssueCommentCreateDto(null, content);

        SubIssue issue = mock(SubIssue.class);
        User user = mock(User.class);
        SubIssueComment savedComment = mock(SubIssueComment.class);

        CustomUserDetails userDetails = new CustomUserDetails(user);

        given(subIssueRepository.findById(issueId)).willReturn(Optional.of(issue));
        given(subIssueCommentRepository.save(any(SubIssueComment.class))).willReturn(savedComment);

        given(savedComment.getId()).willReturn(1L);
        given(savedComment.getContent()).willReturn(content);
        given(savedComment.getSubIssue()).willReturn(issue);
        given(savedComment.getUser()).willReturn(user);

        // When
        IssueCommentResponseDto response = commentService.createComment(issueId, requestDto, userDetails);

        // Then
        assertAll(
                () -> assertNotNull(response),
                () -> assertThat(response.getContent()).isEqualTo(content),
                () -> assertThat(response.getIssueId()).isEqualTo(issue.getId()),
                () -> assertThat(response.getParentCommentId()).isNull(),
                () -> verify(subIssueRepository, times(1)).findById(issueId),
                () -> verify(subIssueCommentRepository, times(1)).save(any(SubIssueComment.class))
        );
    }

    @Test
    @DisplayName("댓글 생성 실패: 존재하지 않는 하위 이슈에 댓글을 생성하려고 하면 예외가 발생한다")
    void createCommentWhenIssueNotFound() {
        // Given
        Long issueId = 1L;
        String content = "테스트 댓글 내용";
        IssueCommentCreateDto requestDto = new IssueCommentCreateDto(null, content);

        CustomUserDetails userDetails = new CustomUserDetails(mock(User.class));

        given(subIssueRepository.findById(issueId)).willReturn(Optional.empty());

        // When & Then
        MyException exception = assertThrows(MyException.class, () ->
                commentService.createComment(issueId, requestDto, userDetails)
        );

        assertThat(exception.getErrorCode()).isEqualTo(MyErrorCode.ISSUE_NOT_FOUND);
        verify(subIssueRepository, times(1)).findById(issueId);
        verify(subIssueCommentRepository, never()).save(any(SubIssueComment.class));
    }
}