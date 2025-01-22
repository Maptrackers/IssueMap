package com.maptracker.issuemap.domain.comment.service;
import com.maptracker.issuemap.common.jwt.CustomUserDetails;
import com.maptracker.issuemap.domain.comment.dto.IssueCommentCreateDto;
import com.maptracker.issuemap.domain.comment.dto.IssueCommentResponseDto;
import com.maptracker.issuemap.domain.comment.entity.IssueComment;
import com.maptracker.issuemap.domain.comment.exception.MyErrorCode;
import com.maptracker.issuemap.domain.comment.exception.MyException;
import com.maptracker.issuemap.domain.comment.repository.IssueCommentRepository;
import com.maptracker.issuemap.domain.issue.entity.Issue;
import com.maptracker.issuemap.domain.issue.entity.IssueStatus;
import com.maptracker.issuemap.domain.issue.entity.IssueType;
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
import org.springframework.test.util.ReflectionTestUtils;

@ExtendWith(MockitoExtension.class)
class IssueCommentServiceTest {
    @InjectMocks
    private IssueCommentService commentService; // 테스트 대상 클래스

    @Mock
    private IssueCommentRepository commentRepository;

    @Mock
    private IssueRepository issueRepository;

    @Mock
    private UserRepository userRepository;

    @Test
    @DisplayName("이슈에 댓글을 생성하면 생성된 댓글의 정보를 반환한다")
    void createComment() {
        // Given
        Long issueId = 1L;
//        Long userId = 1L;
        IssueCommentCreateDto requestDto = new IssueCommentCreateDto( null, "테스트 댓글 내용");

        Issue issue = mock(Issue.class);
        User user = mock(User.class);
        IssueComment savedComment = mock(IssueComment.class);
        CustomUserDetails userDetails = new CustomUserDetails(user);

        given(issueRepository.findById(issueId)).willReturn(Optional.of(issue));
        given(commentRepository.save(any(IssueComment.class))).willReturn(savedComment);

        given(savedComment.getId()).willReturn(1L);
        given(savedComment.getContent()).willReturn("테스트 댓글 내용");
        given(savedComment.getIssue()).willReturn(issue);
        given(savedComment.getUser()).willReturn(user);

        // When
        IssueCommentResponseDto responseDto = commentService.createComment(issueId, requestDto, userDetails);

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
//        Long userId = 1L;
        IssueCommentCreateDto requestDto = new IssueCommentCreateDto( null, "테스트 댓글 내용");

        User user = mock(User.class);
        CustomUserDetails userDetails = new CustomUserDetails(user);

        given(issueRepository.findById(1L)).willReturn(Optional.empty());

        // When & Then
        MyException exception = assertThrows(MyException.class, () -> commentService.createComment(issueId, requestDto, userDetails));

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
//        Long userId = 2L; // 다른 사용자 ID
        IssueCommentCreateDto requestDto = new IssueCommentCreateDto(null, "수정된 댓글 내용");

        IssueComment comment = mock(IssueComment.class);
        Issue issue = mock(Issue.class);
        User anotherUser = mock(User.class);
        User unauthorizedUser = mock(User.class); // 인증된 사용자

        CustomUserDetails userDetails = new CustomUserDetails(unauthorizedUser);

        given(commentRepository.findById(commentId)).willReturn(Optional.of(comment));
        given(comment.getIssue()).willReturn(issue);
        given(issue.getId()).willReturn(issueId);
        given(comment.getUser()).willReturn(anotherUser);
        given(anotherUser.getId()).willReturn(1L); // 댓글 작성자는 다른 사용자
        given(unauthorizedUser.getId()).willReturn(2L); // 인증된 사용자는 ID 2인 사용자

        // When & Then
        MyException exception = assertThrows(MyException.class, () -> commentService.updateComment(issueId, commentId, requestDto, userDetails));
        assertThat(exception.getErrorCode()).isEqualTo(MyErrorCode.UNAUTHORIZED_USER);
    }

    @Test
    @DisplayName("댓글을 삭제하면 삭제된 댓글이 표시되지 않는다")
    void deleteComment() {
        // Given
        Long issueId = 1L;
        Long commentId = 1L;
//        Long userId = 1L;

        IssueComment comment = mock(IssueComment.class);
        Issue issue = mock(Issue.class);
        User user = mock(User.class);

        CustomUserDetails userDetails = new CustomUserDetails(user);

        given(commentRepository.findById(commentId)).willReturn(Optional.of(comment));
        given(comment.getIssue()).willReturn(issue);
        given(issue.getId()).willReturn(issueId);
        given(comment.getUser()).willReturn(user);
        given(user.getId()).willReturn(1L);

        // When
        commentService.deleteComment(issueId, commentId, userDetails);

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
//        Long userId = 2L; // 다른 사용자 ID

        IssueComment comment = mock(IssueComment.class);
        Issue issue = mock(Issue.class);
        User commentOwner = mock(User.class);
        User unauthorizedUser = mock(User.class);

        CustomUserDetails userDetails = new CustomUserDetails(unauthorizedUser);

        given(commentRepository.findById(commentId)).willReturn(Optional.of(comment));
        given(comment.getIssue()).willReturn(issue);
        given(issue.getId()).willReturn(issueId);
        given(comment.getUser()).willReturn(commentOwner);
        given(commentOwner.getId()).willReturn(1L); // 댓글 작성자는 ID 1인 사용자
        given(unauthorizedUser.getId()).willReturn(2L); // 인증된 사용자는 ID 2인 사용자

        // When & Then
        MyException exception = assertThrows(MyException.class, () -> commentService.deleteComment(issueId, commentId, userDetails));
        assertThat(exception.getErrorCode()).isEqualTo(MyErrorCode.UNAUTHORIZED_USER);
    }

    // 대댓글 생성 관련 테스트

    @Test
    @DisplayName("대댓글 생성 성공")
    void createReplySuccess() {
        // Given
        Long issueId = 1L;
        Long parentCommentId = 1L;
        String replyContent = "대댓글 내용";

        // 대댓글 요청 데이터 객체 생성
        IssueCommentCreateDto requestDto = new IssueCommentCreateDto(null, replyContent);

        // Issue 객체를 생성
        Issue issue = Issue.builder()
                .user(null) // 필요한 필드만 초기화
                .project(null)
                .title("Test Issue")
                .issueType(IssueType.BUG)
                .issueStatus(IssueStatus.BEFORE)
                .build();
        ReflectionTestUtils.setField(issue, "id", issueId); // id 값 설정

        // 유저 객체 생성(작성자)
        User user = User.builder()
                .email("test@test.com")
                .password("password")
                .build();
        ReflectionTestUtils.setField(user, "id", 2L); // id 값 설정

        // 부모 댓글 객체 생성
        IssueComment parentComment = IssueComment.builder()
                .content("부모 댓글")
                .user(user)
                .issue(issue)
                .build();
        ReflectionTestUtils.setField(parentComment, "id", parentCommentId); // id 값 설정

        // 대댓글 객체 생성
        IssueComment savedReply = IssueComment.builder()
                .content(replyContent)
                .user(user)
                .issue(issue)
                .parentComment(parentComment)
                .build();
        ReflectionTestUtils.setField(savedReply, "id", 3L); // id 값 설정

        CustomUserDetails userDetails = new CustomUserDetails(user);

        // findById,save 호출시 미리 정의 된 객체를 반환
        given(issueRepository.findById(issueId)).willReturn(Optional.of(issue));
        given(commentRepository.findById(parentCommentId)).willReturn(Optional.of(parentComment));
        given(commentRepository.save(any(IssueComment.class))).willReturn(savedReply);

        // When
        IssueCommentResponseDto response = commentService.createReply(issueId, parentCommentId, requestDto, userDetails);

        // Then
        assertAll(
                () -> assertNotNull(response), // 응답이 null이 아닌지 확인
                () -> assertThat(response.getContent()).isEqualTo(replyContent), // 응답 내용 확인
                () -> assertThat(response.getUserId()).isEqualTo(user.getId()), // 유저 ID 확인
                () -> assertThat(response.getIssueId()).isEqualTo(issue.getId()), // 이슈 ID 확인
                () -> assertThat(response.getParentCommentId()).isEqualTo(parentComment.getId()), // 부모 댓글 ID 확인
                () -> verify(commentRepository, times(1)).save(any(IssueComment.class)) // 저장 호출 확인
        );
    }

    @Test
    @DisplayName("이슈가 존재하지 않을 때 예외 발생")
    void createReplyWhenIssueNotFound() {
        // Given
        Long issueId = 1L;
        Long parentCommentId = 1L;
        IssueCommentCreateDto requestDto = new IssueCommentCreateDto( null, "대댓글 내용");

        CustomUserDetails userDetails = new CustomUserDetails(mock(User.class));

        given(issueRepository.findById(issueId)).willReturn(Optional.empty());

        // When & Then
        MyException exception = assertThrows(MyException.class, () ->
                commentService.createReply(issueId, parentCommentId, requestDto, userDetails)
        );

        assertThat(exception.getErrorCode()).isEqualTo(MyErrorCode.ISSUE_NOT_FOUND);
        verify(commentRepository, never()).save(any(IssueComment.class)); // 저장이 호출되지 않아야 함
    }

    @Test
    @DisplayName("부모 댓글이 존재하지 않을 때 예외 발생")
    void createReplyWhenParentCommentNotFound() {
        // Given
        Long issueId = 1L;
        Long parentCommentId = 1L;
        IssueCommentCreateDto requestDto = new IssueCommentCreateDto( null, "대댓글 내용");

        CustomUserDetails userDetails = new CustomUserDetails(mock(User.class));
        Issue issue = mock(Issue.class);

        given(issueRepository.findById(issueId)).willReturn(Optional.of(issue));
        given(commentRepository.findById(parentCommentId)).willReturn(Optional.empty());

        // When & Then
        MyException exception = assertThrows(MyException.class, () ->
                commentService.createReply(issueId, parentCommentId, requestDto, userDetails)
        );

        assertThat(exception.getErrorCode()).isEqualTo(MyErrorCode.PARENT_COMMENT_NOT_FOUND);
        verify(commentRepository, never()).save(any(IssueComment.class)); // 저장이 호출되지 않아야 함
    }

    @Test
    @DisplayName("부모 댓글이 해당 이슈에 속하지 않을 때 예외 발생")
    void createReplyWhenParentCommentNotInIssue() {
        // Given
        Long issueId = 1L;
        Long parentCommentId = 1L;
        IssueCommentCreateDto requestDto = new IssueCommentCreateDto( null, "대댓글 내용");

        CustomUserDetails userDetails = new CustomUserDetails(mock(User.class));
        Issue issue = mock(Issue.class);
        IssueComment parentComment = mock(IssueComment.class);

        given(issueRepository.findById(issueId)).willReturn(Optional.of(issue));
        given(commentRepository.findById(parentCommentId)).willReturn(Optional.of(parentComment));
        given(parentComment.getIssue()).willReturn(mock(Issue.class)); // 다른 이슈를 반환

        // When & Then
        MyException exception = assertThrows(MyException.class, () ->
                commentService.createReply(issueId, parentCommentId, requestDto, userDetails)
        );

        assertThat(exception.getErrorCode()).isEqualTo(MyErrorCode.ISSUE_MISMATCH);
        verify(commentRepository, never()).save(any(IssueComment.class)); // 저장이 호출되지 않아야 함
    }
}