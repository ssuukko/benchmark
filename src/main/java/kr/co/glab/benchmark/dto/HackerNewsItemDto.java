package kr.co.glab.benchmark.dto;

public record HackerNewsItemDto(
        Long id,
        String title,
        String url,
        Integer score,
        Integer descendants,
        Long time,
        String text,
        Boolean dead,
        Boolean deleted,
        String type
) {
}
