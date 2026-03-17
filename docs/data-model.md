# Data Model

현재 구현과 다음 단계 기능을 기준으로 데이터 모델을 아래 5개 축으로 고정한다.

## Core Tables

### `articles`
- 외부 소스에서 수집한 원본 게시글
- 중복 기준은 `source + external_id`
- 공통 메타데이터만 둔다
- Hacker News와 GitHub를 모두 담을 수 있게 유지한다

### `article_models`
- 게시글과 AI 모델의 매핑
- 한 게시글이 여러 모델을 동시에 언급할 수 있으므로 별도 테이블로 분리한다
- `mention_count`를 둬서 단순 포함 여부가 아니라 언급 강도까지 저장한다

### `trend_stats`
- 주간 집계 결과
- 홈 대시보드와 랭킹 조회를 빠르게 하기 위한 집계 테이블이다
- `week + model_name` 단위로 유일해야 한다

### `keyword_stats`
- 모델별 주간 키워드 집계
- 키워드 분석과 모델 상세 화면의 Top N 조회에 사용한다
- `week + model_name + keyword` 단위로 유일해야 한다

### `weekly_reports`
- 한 주에 하나 생성되는 요약 리포트
- 리포트 재생성 정책이 없다면 `week`를 유니크로 유지한다

## Why This Split

- `Article` 하나만으로는 한 게시글이 여러 모델을 언급하는 구조를 표현하기 어렵다
- `TrendStat`만으로는 어떤 게시글이 어떤 모델을 얼마나 언급했는지 추적할 수 없다
- 키워드와 리포트는 원본 데이터와 성격이 달라 별도 집계 테이블로 유지하는 편이 맞다

## Deferred

아래는 실제 기능 구현 시점에 필요하면 추가한다.

- `users`: 로그인, 구독이 확정될 때 추가
- `source-specific metadata`: GitHub 전용 별/포크 수 등은 공통 모델로 부족해질 때 분리
- `daily trend table`: 날짜별 차트가 꼭 필요해지면 `trend_daily_stats` 같은 일 단위 집계 테이블 추가
