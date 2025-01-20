package com.maptracker.issuemap.domain.comment.service;

import com.maptracker.issuemap.common.jwt.CustomUserDetails;
import com.maptracker.issuemap.domain.comment.dto.IssueCommentCreateDto;
import com.maptracker.issuemap.domain.comment.dto.IssueCommentResponseDto;
import com.maptracker.issuemap.domain.comment.entity.SubIssueComment;
import com.maptracker.issuemap.domain.comment.exception.MyErrorCode;
import com.maptracker.issuemap.domain.comment.exception.MyException;
import com.maptracker.issuemap.domain.comment.repository.SubIssueCommentRepository;
import com.maptracker.issuemap.domain.issue.entity.IssueStatus;
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
import org.springframework.test.util.ReflectionTestUtils;


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
    @DisplayName("하위 이슈 댓글 생성 성공: 이슈에 댓글을 생성하면 댓글 정보가 반환된다")
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
    @DisplayName("하위 이슈 댓글 생성 실패: 존재하지 않는 하위 이슈에 댓글을 생성하려고 하면 예외가 발생한다")
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


    @Test
    @DisplayName("하위 이슈 댓글 업데이트 성공: 해당 이슈와 댓글 작성자가 일치할 경우 댓글 내용이 성공적으로 업데이트된다.")
    void updateCommentSuccess() {
        // Given
        Long issueId = 1L;
        Long commentId = 1L;
        String updatedContent = "수정된 댓글 내용";
        IssueCommentCreateDto requestDto = new IssueCommentCreateDto(null, updatedContent);

        // Mock 데이터 설정
        SubIssueComment comment = mock(SubIssueComment.class);
        SubIssue issue = mock(SubIssue.class);
        User commentOwner = mock(User.class);
        CustomUserDetails userDetails = new CustomUserDetails(commentOwner);

        // Mock 설정
        given(subIssueCommentRepository.findById(commentId)).willReturn(Optional.of(comment)); // 댓글 조회 성공
        given(comment.getSubIssue()).willReturn(issue);
        given(issue.getId()).willReturn(issueId); // 댓글이 해당 이슈에 속함
        given(comment.getUser()).willReturn(commentOwner); // 댓글 작성자와 인증된 사용자 동일

        // invocation: 메서드 호출 정보를 담고 있는 객체(어떤 인자가 전달되었는지, 호출된 메서드 이름이 무엇인지 등을 조회)
        doAnswer(invocation -> { // updateContent(updatedContent) 메서드가 호출될 때의 동작을 정의
            String content = invocation.getArgument(0, String.class);
            // 호출된 메서드의 첫 번째 인자를 String 타입으로 가져옵니다.(첫 번째 인자는 updatedContent)

            given(comment.getContent()).willReturn(content);
            //updateContent("수정된 댓글 내용") 호출 후, comment.getContent()를 호출하면 "수정된 댓글 내용"이 반환됩니다.
            return null;
        }).when(comment).updateContent(updatedContent); //  메서드 호출 시 위에서 정의한 동작(doAnswer)이 실행

        // When
        IssueCommentResponseDto response = commentService.updateComment(issueId, commentId, requestDto, userDetails);

        // Then
        assertAll(
                () -> assertNotNull(response), // 반환 값이 null이 아님을 확인
                () -> assertThat(response.getContent()).isEqualTo(updatedContent), // 업데이트된 댓글 내용 확인
                () -> verify(comment).updateContent(updatedContent),
                () -> verify(subIssueCommentRepository, never()).save(comment) // save가 필요 없는 구조라 호출되지 않음
        );
    }

    @Test
    @DisplayName("하위 이슈 댓글 삭제 성공: 이슈와 작성자가 일치할 경우 Soft Delete 처리된다.")
    void deleteCommentSuccess() {
        // Given
        Long issueId = 1L;
        Long commentId = 1L;

        SubIssueComment comment = mock(SubIssueComment.class);
        SubIssue issue = mock(SubIssue.class);
        User commentOwner = mock(User.class);
        CustomUserDetails userDetails = new CustomUserDetails(commentOwner);

        // Mock 설정
        given(subIssueCommentRepository.findById(commentId)).willReturn(Optional.of(comment)); // 댓글 조회 성공
        given(comment.getSubIssue()).willReturn(issue);
        given(issue.getId()).willReturn(issueId); // 댓글이 해당 이슈에 속함
        given(comment.getUser()).willReturn(commentOwner); // 댓글 작성자와 인증된 사용자 동일

        // When
        commentService.deleteComment(issueId, commentId, userDetails);

        // Then
        verify(comment).markDeleted(); // Soft delete 호출 확인
        verify(subIssueCommentRepository, times(1)).save(comment); // 저장 호출 확인
    }

    @Test
    @DisplayName("하위 이슈 댓글 삭제 실패: 해당 댓글이 존재하지 않을 경우 예외가 발생한다.")
    void deleteCommentWhenCommentNotFound() {
        // Given
        Long issueId = 1L;
        Long commentId = 1L;
        CustomUserDetails userDetails = new CustomUserDetails(mock(User.class));

        // Mock 설정
        given(subIssueCommentRepository.findById(commentId)).willReturn(Optional.empty()); // 댓글 조회 실패

        // When & Then
        MyException exception = assertThrows(MyException.class, () ->
                commentService.deleteComment(issueId, commentId, userDetails)
        );

        assertThat(exception.getErrorCode()).isEqualTo(MyErrorCode.COMMENT_NOT_FOUND);
        verify(subIssueCommentRepository, never()).save(any(SubIssueComment.class)); // 저장 호출되지 않음
    }

    @Test
    @DisplayName("하위이슈 댓글 삭제 실패: 댓글 작성자가 아닌 사용자가 삭제하려고 할 경우 예외가 발생한다.")
    void deleteCommentWhenUnauthorizedUser() {
        // Given
        Long issueId = 1L;
        Long commentId = 1L;

        SubIssueComment comment = mock(SubIssueComment.class);
        SubIssue issue = mock(SubIssue.class);
        User commentOwner = mock(User.class); // 댓글 작성자
        User unauthorizedUser = mock(User.class); // 인증된 사용자 (댓글 작성자가 아님)
        CustomUserDetails userDetails = new CustomUserDetails(unauthorizedUser);

        // Mock 설정
        given(subIssueCommentRepository.findById(commentId)).willReturn(Optional.of(comment)); // 댓글 조회 성공
        given(comment.getSubIssue()).willReturn(issue);
        given(issue.getId()).willReturn(issueId);
        given(comment.getUser()).willReturn(commentOwner); // 댓글 작성자 설정
        given(commentOwner.getId()).willReturn(1L); // 댓글 작성자의 ID
        given(unauthorizedUser.getId()).willReturn(2L); // 인증된 사용자의 ID (이론상 이 유저가 삭제가 가능하면 안됨 )

        // When & Then
        MyException exception = assertThrows(MyException.class, () ->
                commentService.deleteComment(issueId, commentId, userDetails)
        );

        assertThat(exception.getErrorCode()).isEqualTo(MyErrorCode.UNAUTHORIZED_USER);
        verify(comment, never()).markDeleted(); // 삭제 호출되지 않음
        verify(subIssueCommentRepository, never()).save(any(SubIssueComment.class)); // 저장 호출되지 않음
    }


    @Test
    @DisplayName("하위 이슈 대댓글 생성 성공: 부모 댓글과 인증된 사용자 정보가 유효할 때 대댓글 생성 성공")
    void createReplySuccess() {
        // Given
        Long issueId = 1L;
        Long parentCommentId = 1L;
        String content = "대댓글 내용";
        IssueCommentCreateDto requestDto = new IssueCommentCreateDto(parentCommentId, content);

        // SubIssue 객체 생성 및 ID 설정
        SubIssue issue = SubIssue.builder()
                .title("테스트 이슈")
                .user(mock(User.class))
                .issueStatus(IssueStatus.BEFORE)
                .build();
        ReflectionTestUtils.setField(issue, "id", issueId);

        // 부모 댓글 객체 생성 및 ID 설정
        SubIssueComment parentComment = SubIssueComment.builder()
                .content("부모 댓글")
                .user(mock(User.class))
                .subIssue(issue)
                .build();
        ReflectionTestUtils.setField(parentComment, "id", parentCommentId);

        // 인증된 사용자 객체 생성
        User authenticatedUser = User.builder().build();
        ReflectionTestUtils.setField(authenticatedUser, "id", 100L); // 사용자 ID 설정

        // 저장된 대댓글 객체 생성 및 ID 설정
        SubIssueComment savedReply = SubIssueComment.builder()
                .content(content)
                .user(authenticatedUser)
                .subIssue(issue)
                .parentComment(parentComment)
                .build();
        ReflectionTestUtils.setField(savedReply, "id", 1L);

        CustomUserDetails userDetails = new CustomUserDetails(authenticatedUser);

        // Mock 설정
        given(subIssueRepository.findById(issueId)).willReturn(Optional.of(issue)); // 이슈 조회
        given(subIssueCommentRepository.findById(parentCommentId)).willReturn(Optional.of(parentComment)); // 부모 댓글 조회
        given(subIssueCommentRepository.save(any(SubIssueComment.class))).willReturn(savedReply); // 저장된 대댓글 반환

        // When
        IssueCommentResponseDto response = commentService.createReply(issueId, parentCommentId, requestDto, userDetails);

        // Then
        assertAll(
                () -> assertNotNull(response),
                () -> assertThat(response.getId()).isEqualTo(1L),
                () -> assertThat(response.getContent()).isEqualTo(content),
                () -> assertThat(response.getIssueId()).isEqualTo(issueId),
                () -> assertThat(response.getParentCommentId()).isEqualTo(parentCommentId),
                () -> verify(subIssueRepository, times(1)).findById(issueId),
                () -> verify(subIssueCommentRepository, times(1)).findById(parentCommentId),
                () -> verify(subIssueCommentRepository, times(1)).save(any(SubIssueComment.class))
        );
    }

    @Test
    @DisplayName("하위이슈 대댓글 생성 실패: 부모 댓글이 존재하지 않을 경우 예외 발생")
    void createReplyWhenParentCommentNotFound() {
        // Given
        Long issueId = 1L;
        Long parentCommentId = 1L;
        String content = "대댓글 내용";
        IssueCommentCreateDto requestDto = new IssueCommentCreateDto(parentCommentId, content);

        SubIssue issue = mock(SubIssue.class);
        CustomUserDetails userDetails = new CustomUserDetails(mock(User.class));

        given(subIssueRepository.findById(issueId)).willReturn(Optional.of(issue));
        given(subIssueCommentRepository.findById(parentCommentId)).willReturn(Optional.empty());

        // When & Then
        MyException exception = assertThrows(MyException.class, () ->
                commentService.createReply(issueId, parentCommentId, requestDto, userDetails)
        );

        assertThat(exception.getErrorCode()).isEqualTo(MyErrorCode.PARENT_COMMENT_NOT_FOUND);
        verify(subIssueCommentRepository, never()).save(any(SubIssueComment.class));
    }

    @Test
    @DisplayName("하위 이슈 대댓글 생성 실패: 부모 댓글이 해당 이슈에 속하지 않을 경우 예외 발생")
    void createReplyWhenParentCommentNotInIssue() {
        // Given
        Long issueId = 1L;
        Long parentCommentId = 1L;
        String content = "대댓글 내용";
        IssueCommentCreateDto requestDto = new IssueCommentCreateDto(parentCommentId, content);

        SubIssue issue = mock(SubIssue.class);
        SubIssueComment parentComment = mock(SubIssueComment.class);
        CustomUserDetails userDetails = new CustomUserDetails(mock(User.class));

        given(subIssueRepository.findById(issueId)).willReturn(Optional.of(issue));
        given(subIssueCommentRepository.findById(parentCommentId)).willReturn(Optional.of(parentComment));
        given(parentComment.getSubIssue()).willReturn(mock(SubIssue.class)); // 다른 이슈를 반환

        // When & Then
        MyException exception = assertThrows(MyException.class, () ->
                commentService.createReply(issueId, parentCommentId, requestDto, userDetails)
        );

        assertThat(exception.getErrorCode()).isEqualTo(MyErrorCode.ISSUE_MISMATCH);
        verify(subIssueCommentRepository, never()).save(any(SubIssueComment.class));
    }
}