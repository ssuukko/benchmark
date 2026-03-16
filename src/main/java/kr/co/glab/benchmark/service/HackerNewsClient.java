package kr.co.glab.benchmark.service;

import java.util.List;
import kr.co.glab.benchmark.dto.HackerNewsItemDto;

public interface HackerNewsClient {

    List<Long> fetchTopStoryIds(int limit);

    HackerNewsItemDto fetchItem(Long id);
}
