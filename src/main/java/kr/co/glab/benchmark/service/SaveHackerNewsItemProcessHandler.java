package kr.co.glab.benchmark.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Order(4)
@RequiredArgsConstructor
@Slf4j
public class SaveHackerNewsItemProcessHandler implements HackerNewsItemProcessHandler {

    private final ArticleIngestionService articleIngestionService;

    @Override
    public boolean supports(HackerNewsItemProcessContext context) {
        return true;
    }

    @Override
    public HackerNewsItemProcessStatus handle(HackerNewsItemProcessContext context) {
        articleIngestionService.saveHackerNewsArticle(
                context.item(),
                context.evaluation().content(),
                context.evaluation().mentions()
        );
        return HackerNewsItemProcessStatus.SAVED;
    }

    @Override
    public void log(HackerNewsItemProcessContext context) {
        log.info("Saved HN article. storyId={} score={} comments={} title={}",
                context.item().id(),
                context.item().score(),
                context.item().descendants(),
                context.item().title());
    }
}
