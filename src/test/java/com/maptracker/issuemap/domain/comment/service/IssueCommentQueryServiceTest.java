package com.maptracker.issuemap.domain.comment.service;

import com.maptracker.issuemap.domain.comment.dto.IssueCommentResponseDto;
import com.maptracker.issuemap.domain.comment.entity.IssueComment;
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

import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.util.ReflectionTestUtils;

@ExtendWith(MockitoExtension.class)
class IssueCommentQueryServiceTest {
    @InjectMocks
    private IssueCommentQueryService commentQueryService;
    @Mock
    private CommentRepository commentRepository;

    @Mock
    private IssueRepository issueRepository;

    @Mock
    private UserRepository userRepository;


    @Test
    @DisplayName("특정 이슈의 댓글 목록을 조회하면 삭제되지 않은 댓글이 반환된다.")
    void getCommentsByIssue_ShouldReturnCommentList() {
        // ReflectionTestUtils : 클래스의 private 또는 protected 필드에 접근해 값을 읽거나 설정가능함
        // user ,issue에 아직 builder가 사용되지 않아서 직접 객체 생성 방식으로 테스트 진행

        // Given
        Long issueId = 1L;

        // Issue 객체 직접 생성
        Issue issue = new Issue();
        ReflectionTestUtils.setField(issue, "id", issueId); // id 값 설정

        // User 객체 직접 생성
        User user1 = new User();
        ReflectionTestUtils.setField(user1, "id", 1L);

        User user2 = new User();
        ReflectionTestUtils.setField(user2, "id", 2L);

        User user3 = new User();
        ReflectionTestUtils.setField(user3, "id", 3L);

        IssueComment comment1 = IssueComment.builder()
                .content("첫 번째 댓글")
                .issue(issue)
                .user(user1)
                .build();
        ReflectionTestUtils.setField(comment1, "id", 1L); // comment1 id 설정

        IssueComment comment2 = IssueComment.builder()
                .content("두 번째 댓글")
                .issue(issue)
                .user(user2)
                .build();
        ReflectionTestUtils.setField(comment2, "id", 2L); // comment2 id 설정

        // 삭제된 댓글 생성
        IssueComment comment3 = IssueComment.builder()
                .content("삭제된 댓글")
                .issue(issue)
                .user(user3)
                .build();
        ReflectionTestUtils.setField(comment3, "id", 3L); // comment3 id 설정
        ReflectionTestUtils.setField(comment3, "isDeleted", true); // 삭제된 댓글 설정

        // mockComments 리스트에 모든 댓글 포함 (삭제된 댓글 포함)
        List<IssueComment> mockComments = List.of(comment1, comment2, comment3);

        // Repository 호출 시 삭제되지 않은 댓글만 반환하도록 설정
        given(commentRepository.findAllByIssueIdAndIsDeletedFalse(issueId)).willReturn(List.of(comment1, comment2));

        // When
        List<IssueCommentResponseDto> comments = commentQueryService.getCommentsByIssue(issueId);

        // Then
        assertThat(comments).hasSize(2); // 삭제되지 않은 댓글만 2개여야 함

        // 반환된 댓글 목록에 삭제된 댓글이 포함되지 않았음을 검증
        List<Long> returnedIds = comments.stream()
                .map(IssueCommentResponseDto::getId)
                .toList();
        assertThat(returnedIds).doesNotContain(comment3.getId()); // 삭제된 댓글 ID가 포함되지 않았는지 확인

        verify(commentRepository, times(1)).findAllByIssueIdAndIsDeletedFalse(issueId);
    }
}