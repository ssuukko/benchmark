package kr.co.glab.benchmark.service;

import kr.co.glab.benchmark.repository.ArticleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Order(3)
@RequiredArgsConstructor
@Slf4j
public class DuplicateHackerNewsItemProcessHandler implements HackerNewsItemProcessHandler {

    private static final String SOURCE_HACKER_NEWS = "HACKERNEWS";

    private final ArticleRepository articleRepository;

    @Override
    public boolean supports(HackerNewsItemProcessContext context) {
        String externalId = String.valueOf(context.item().id());
        return articleRepository.findBySourceAndExternalId(SOURCE_HACKER_NEWS, externalId).isPresent();
    }

    @Override
    public HackerNewsItemProcessStatus handle(HackerNewsItemProcessContext context) {
        return HackerNewsItemProcessStatus.DUPLICATE;
    }

    @Override
    public void log(HackerNewsItemProcessContext context) {
        log.debug("Skipping duplicate HN item. storyId={} title={}",
                context.item().id(), context.item().title());
    }
}
