package kr.co.glab.benchmark.service;

import java.util.Arrays;
import java.util.List;
import kr.co.glab.benchmark.dto.HackerNewsItemDto;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
public class HackerNewsClientImpl implements HackerNewsClient {

    private static final String BASE_URL = "https://hacker-news.firebaseio.com/v0";

    private final RestClient restClient = RestClient.builder()
            .baseUrl(BASE_URL)
            .build();

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
}
