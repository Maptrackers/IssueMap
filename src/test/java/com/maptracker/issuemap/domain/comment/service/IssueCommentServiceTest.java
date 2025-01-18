package com.maptracker.issuemap.domain.comment.service;
import com.maptracker.issuemap.domain.comment.dto.IssueCommentCreateDto;
import com.maptracker.issuemap.domain.comment.dto.IssueCommentResponseDto;
import com.maptracker.issuemap.domain.comment.entity.IssueComment;
import com.maptracker.issuemap.domain.comment.exception.MyErrorCode;
import com.maptracker.issuemap.domain.comment.exception.MyException;
import com.maptracker.issuemap.domain.comment.repository.CommentRepository;
import com.maptracker.issuemap.domain.issue.entity.Issue;
import com.maptracker.issuemap.domain.issue.repository.IssueRepository;
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
class IssueCommentServiceTest {
    @InjectMocks
    private IssueCommentService commentService; // 테스트 대상 클래스

    @Mock
    private CommentRepository commentRepository;

    @Mock
    private IssueRepository issueRepository;

    @Mock
    private UserRepository userRepository;

    @Test
    @DisplayName("댓글을 생성하면 생성된 댓글의 정보를 반환한다")
    void createComment() {
        // Given
        Long issueId = 1L;
        Long userId = 1L;
        IssueCommentCreateDto requestDto = new IssueCommentCreateDto(1L, null, "테스트 댓글 내용");

        Issue issue = mock(Issue.class);
        User user = mock(User.class);
        IssueComment savedComment = mock(IssueComment.class);

        given(issueRepository.findById(issueId)).willReturn(Optional.of(issue));
        given(userRepository.findById(userId)).willReturn(Optional.of(user));
        given(commentRepository.save(any(IssueComment.class))).willReturn(savedComment);

        given(savedComment.getId()).willReturn(1L);
        given(savedComment.getContent()).willReturn("테스트 댓글 내용");
        given(savedComment.getIssue()).willReturn(issue);
        given(savedComment.getUser()).willReturn(user);

        // When
        IssueCommentResponseDto responseDto = commentService.createComment(issueId, requestDto, userId);

        // Then
        assertAll(
                () -> assertNotNull(responseDto), // 정상적으로 생성했는지 확인
                () -> assertThat(responseDto.getContent()).isEqualTo("테스트 댓글 내용"), //요청한 댓글 내용이 정상적으로 저장되었는지 확인
                () -> verify(commentRepository, times(1)).save(any(IssueComment.class)) //한번만 호출되는지 확인
        );
    }

    @Test
    @DisplayName("존재하지 않는 이슈에 댓글을 생성하려고 하면 예외를 던진다")
    void createCommentWhenIssueNotFound() {
        // Given
        Long issueId = 1L;
        Long userId = 1L;
        IssueCommentCreateDto requestDto = new IssueCommentCreateDto(1L, null, "테스트 댓글 내용");

        given(issueRepository.findById(1L)).willReturn(Optional.empty());

        // When & Then
        MyException exception = assertThrows(MyException.class, () -> commentService.createComment(issueId, requestDto, userId));

        assertAll(
                () -> assertThat(exception.getErrorCode()).isEqualTo(MyErrorCode.ISSUE_NOT_FOUND),
                () -> verify(commentRepository, never()).save(any(IssueComment.class))
        );
    }

    @Test
    @DisplayName("다른 사용자가 댓글을 수정하려고 하면 예외를 던진다")
    void updateCommentWhenUnauthorizedUser() {
        // Given
        Long issueId = 1L;
        Long commentId = 1L;
        Long userId = 2L; // 다른 사용자 ID
        IssueCommentCreateDto requestDto = new IssueCommentCreateDto(null, null, "수정된 댓글 내용");

        IssueComment comment = mock(IssueComment.class);
        Issue issue = mock(Issue.class);
        User anotherUser = mock(User.class);

        given(commentRepository.findById(commentId)).willReturn(Optional.of(comment));
        given(comment.getIssue()).willReturn(issue);
        given(issue.getId()).willReturn(issueId);
        given(comment.getUser()).willReturn(anotherUser);
        given(anotherUser.getId()).willReturn(1L); // 댓글 작성자는 다른 사용자

        // When & Then
        MyException exception = assertThrows(MyException.class, () -> commentService.updateComment(issueId, commentId, requestDto, userId));
        assertThat(exception.getErrorCode()).isEqualTo(MyErrorCode.UNAUTHORIZED_USER);
    }

    @Test
    @DisplayName("댓글을 삭제하면 삭제된 댓글이 표시되지 않는다")
    void deleteComment() {
        // Given
        Long issueId = 1L;
        Long commentId = 1L;
        Long userId = 1L;

        IssueComment comment = mock(IssueComment.class);
        Issue issue = mock(Issue.class);
        User user = mock(User.class);

        given(commentRepository.findById(commentId)).willReturn(Optional.of(comment));
        given(comment.getIssue()).willReturn(issue);
        given(issue.getId()).willReturn(issueId);
        given(comment.getUser()).willReturn(user);
        given(user.getId()).willReturn(userId);

        // When
        commentService.deleteComment(issueId, commentId, userId);

        // Then
        verify(comment).markDeleted(); // Soft delete 호출 확인
        verify(commentRepository, times(1)).save(comment);
    }

    @Test
    @DisplayName("다른 사용자가 댓글을 삭제하려고 하면 예외를 던진다")
    void deleteCommentWhenUnauthorizedUser() {
        // Given
        Long issueId = 1L;
        Long commentId = 1L;
        Long userId = 2L; // 다른 사용자 ID

        IssueComment comment = mock(IssueComment.class);
        Issue issue = mock(Issue.class);
        User anotherUser = mock(User.class);

        given(commentRepository.findById(commentId)).willReturn(Optional.of(comment));
        given(comment.getIssue()).willReturn(issue);
        given(issue.getId()).willReturn(issueId);
        given(comment.getUser()).willReturn(anotherUser);
        given(anotherUser.getId()).willReturn(1L); // 댓글 작성자는 다른 사용자

        // When & Then
        MyException exception = assertThrows(MyException.class, () -> commentService.deleteComment(issueId, commentId, userId));
        assertThat(exception.getErrorCode()).isEqualTo(MyErrorCode.UNAUTHORIZED_USER);
    }
}