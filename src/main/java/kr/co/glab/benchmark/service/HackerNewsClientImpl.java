package kr.co.glab.benchmark.service;

import jakarta.annotation.PreDestroy;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import kr.co.glab.benchmark.dto.HackerNewsItemDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
@Slf4j
public class HackerNewsClientImpl implements HackerNewsClient {

    private static final String BASE_URL = "https://hacker-news.firebaseio.com/v0";
    private static final int FETCH_THREADS = 8;

    private final RestClient restClient = RestClient.builder()
            .baseUrl(BASE_URL)
            .build();
    private final ExecutorService executorService = Executors.newFixedThreadPool(FETCH_THREADS);

    @Override
    public List<Long> fetchTopStoryIds(int limit) {
        Long[] response = restClient.get()
                .uri("/topstories.json")
                .retrieve()
                .body(Long[].class);

        if (response == null) {
            return List.of();
        }

        return Arrays.stream(response)
                .limit(limit)
                .toList();
    }

    @Override
    public HackerNewsItemDto fetchItem(Long id) {
        return restClient.get()
                .uri("/item/{id}.json", id)
                .retrieve()
                .body(HackerNewsItemDto.class);
    }

    @Override
    public List<HackerNewsItemDto> fetchItems(List<Long> ids) {
        return ids.stream()
                .map(id -> CompletableFuture.supplyAsync(() -> fetchItemSafely(id), executorService))
                .toList()
                .stream()
                .map(CompletableFuture::join)
                .filter(item -> item != null)
                .toList();
    }

    private HackerNewsItemDto fetchItemSafely(Long id) {
        try {
            return fetchItem(id);
        } catch (Exception exception) {
            log.warn("Failed to fetch HN item. storyId={}", id, exception);
            return null;
        }
    }

    @PreDestroy
    void shutdownExecutor() {
        executorService.shutdown();
    }
}
