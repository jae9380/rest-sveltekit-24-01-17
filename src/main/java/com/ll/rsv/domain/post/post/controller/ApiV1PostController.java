package com.ll.rsv.domain.post.post.controller;

import com.ll.rsv.domain.post.post.dto.PostDto;
import com.ll.rsv.domain.post.post.entity.Post;
import com.ll.rsv.domain.post.post.service.PostService;
import com.ll.rsv.global.exceptions.GlobalException;
import com.ll.rsv.global.rq.Rq;
import com.ll.rsv.global.rsData.RsData;
import com.ll.rsv.standard.base.Empty;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/posts")
@RequiredArgsConstructor
public class ApiV1PostController {
    private final PostService postService;
    private final Rq rq;


    public record GetPostsResponseBody(@NonNull List<PostDto> items) {
    }

    @GetMapping("")
    public RsData<GetPostsResponseBody> getPosts() {
        List<Post> items = postService.findByPublished(true);
        List<PostDto> _items = items.stream()
                .map(post -> {
                    PostDto postDto = new PostDto(post);
                    postDto.setActorCanRead(postService.canRead(rq.getMember(), post));
                    postDto.setActorCanEdit(postService.canEdit(rq.getMember(), post));
                    postDto.setActorCanDelete(postService.canDelete(rq.getMember(), post));
                    return postDto;
                })
                .collect(Collectors.toList());

        return RsData.of(
                new GetPostsResponseBody(
                        _items
                )
        );
    }


    public record GetPostResponseBody(@NonNull PostDto item) {
    }

    @GetMapping("/{id}")
    public RsData<GetPostResponseBody> getPost(
            @PathVariable long id
    ) {
        Post post = postService.findById(id).orElseThrow(GlobalException.E404::new);

        if (!postService.canRead(rq.getMember(), post))
            throw new GlobalException("403-1", "권한이 없습니다.");

        PostDto postDto = new PostDto(post);
        postDto.setActorCanRead(postService.canRead(rq.getMember(), post));
        postDto.setActorCanEdit(postService.canEdit(rq.getMember(), post));
        postDto.setActorCanDelete(postService.canDelete(rq.getMember(), post));

        return RsData.of(
                new GetPostResponseBody(postDto)
        );
    }


    public record EditRequestBody(@NotBlank String title, @NotBlank String body, @NotNull boolean published) {
    }

    public record EditResponseBody(@NonNull PostDto item) {
    }

    @PutMapping(value = "/{id}")
    public RsData<EditResponseBody> edit(
            @PathVariable long id,
            @Valid @RequestBody EditRequestBody requestBody
    ) {
        Post post = postService.findById(id).orElseThrow(GlobalException.E404::new);

        if (!postService.canEdit(rq.getMember(), post))
            throw new GlobalException("403-1", "권한이 없습니다.");

        postService.edit(post, requestBody.title, requestBody.body, requestBody.published);

        return RsData.of(
                "%d번 글이 수정되었습니다.".formatted(id),
                new EditResponseBody(new PostDto(post))
        );
    }


    @DeleteMapping(value = "/{id}")
    public RsData<Empty> delete(
            @PathVariable long id
    ) {
        Post post = postService.findById(id).orElseThrow(GlobalException.E404::new);

        if (!postService.canDelete(rq.getMember(), post))
            throw new GlobalException("403-1", "권한이 없습니다.");

        postService.delete(post);

        return RsData.of(
                "%d번 글이 삭제되었습니다.".formatted(id)
        );
    }
}