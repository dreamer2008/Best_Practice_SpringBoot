## Why

The current test baseline is not trustworthy enough to support refactoring on Spring Boot 3.5.x. The source tree only retains a minimal context-load test, while historical reports show older tests with mixed styles, mapper wiring issues, and database-coupled behavior; in addition, `mvn test` can report success even when tests fail because Surefire ignores failures.

## What Changes

- Rebuild the test suite around Spring Boot 3.5.12 and JUnit 5.12.2 using clear test slices for unit, MVC, JPA, and application smoke coverage.
- Replace brittle or stateful tests with deterministic fixtures, explicit mocking, and assertions focused on observable behavior.
- Align Maven test verification with JaCoCo so `mvn test` becomes a meaningful validation step and enforces at least 80% coverage for the target code.
- Remove dependence on stale reports as evidence of quality and make the test source tree the only supported baseline.

## Capabilities

### New Capabilities
- `deterministic-test-suite`: Defines the required structure, execution rules, and coverage gate for the Spring Boot 3.5 / JUnit 5 test suite.

### Modified Capabilities
- None.

## Impact

- Affected code: `src/test/java`, Maven test and JaCoCo plugin configuration in `pom.xml`, and any test-only fixtures needed around controller, service, repository, exception, and DTO behavior.
- Validation path: `mvn test` must fail on broken tests or insufficient coverage instead of masking failures.
- Existing tests: the current `ApplicationTests` smoke test remains in scope but will no longer be the primary quality signal; legacy expectations inferred only from `target/surefire-reports` are not treated as source of truth.