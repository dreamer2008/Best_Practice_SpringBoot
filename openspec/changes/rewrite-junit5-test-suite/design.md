## Context

The project is a single-module Employee CRUD API on Spring Boot 3.5.12 and Java 21. Its current source test tree only contains a `@SpringBootTest` smoke test, while repository memory and historical reports show earlier tests that mixed full-context loading, Mockito-only tests, and database-coupled repository checks. The build also sets `maven-surefire-plugin.testFailureIgnore=true`, which means `mvn test` is not a reliable release gate.

## Goals / Non-Goals

**Goals:**
- Rebuild the test suite around JUnit 5 with explicit separation between unit, MVC slice, JPA slice, and smoke tests.
- Keep tests deterministic by replacing implicit wiring and external database dependence with mocks or embedded slice infrastructure.
- Make `mvn test` fail on both test failures and insufficient coverage, with JaCoCo enforcing at least 80% line coverage on the intended codebase.

**Non-Goals:**
- No production feature changes to controller, service, repository, or DTO contracts.
- No attempt to raise coverage through low-value assertions against generated code or framework internals.
- No expansion to integration environments outside Maven local test execution.

## Decisions

### Decision: Split tests by responsibility instead of using full context everywhere
Use Mockito for pure service and utility tests, `@WebMvcTest(EmployeeController.class)` for HTTP contract checks, `@DataJpaTest` for repository behavior, and keep a minimal `@SpringBootTest` smoke test only for application bootstrap.

Rationale: this makes failures local, shortens execution time, and removes the mapper/mock wiring problem noted in prior service tests.

Alternative considered: keep all tests under `@SpringBootTest` for convenience. Rejected because it increases startup cost, hides dependency boundaries, and makes failure diagnosis slower.

```java
@ExtendWith(MockitoExtension.class)
class EmployeeServiceImplTest {

    @Mock
    private EmployeeDao employeeDao;

    @Mock
    private EmployeeMapper employeeMapper;

    @InjectMocks
    private EmployeeServiceImpl employeeService;
}
```

### Decision: Enforce verification through Maven instead of relying on reports under target/
Remove failure masking from Surefire and add a `jacoco:check` rule bound to the test lifecycle so the coverage threshold is part of the same `mvn test` contract.

Rationale: the user requirement is explicit that `mvn test` must validate the rewrite, so the command must return a failing status whenever tests fail or coverage is below target.

Alternative considered: keep `testFailureIgnore=true` and inspect HTML/XML reports manually. Rejected because it makes CI and local verification inconsistent.

```xml
<plugin>
  <groupId>org.jacoco</groupId>
  <artifactId>jacoco-maven-plugin</artifactId>
  <executions>
    <execution>
      <id>check</id>
      <phase>test</phase>
      <goals><goal>check</goal></goals>
    </execution>
  </executions>
</plugin>
```

### Decision: Cover behavior-rich classes first and exclude bootstrap-only noise deliberately
The rewrite will prioritize `EmployeeController`, `EmployeeServiceImpl`, `GlobalExceptionHandler`, `Result`, and repository behavior that matters to CRUD flows. The application bootstrap class may stay excluded if it only hosts `main` and framework startup.

Rationale: this yields meaningful coverage and avoids inflating the metric with trivial framework entry points.

Alternative considered: chase 80% by testing every class mechanically. Rejected because it adds maintenance cost without increasing confidence.

## Risks / Trade-offs

- Mock-heavy service tests can diverge from Spring wiring expectations -> keep one smoke test and focused MVC/JPA slices to cover wiring boundaries.
- A strict coverage gate can block early refactors if the threshold is measured against too much boilerplate -> document explicit exclusions and target behavior-centric classes.
- Legacy expectations inferred from `target/` may differ from current source behavior -> treat current production code plus source tests as the baseline and rewrite assertions accordingly.

## Migration Plan

1. Inventory target classes and remove obsolete or duplicate tests from `src/test/java`.
2. Recreate unit, MVC, JPA, and smoke tests with JUnit 5 annotations and deterministic fixtures.
3. Update Maven Surefire and JaCoCo configuration so `mvn test` becomes authoritative.
4. Run `mvn test`, inspect failures or coverage gaps, and iterate until the command passes with coverage at or above 80%.

Rollback: revert the rewritten test sources and Maven plugin changes if the new gate blocks development unexpectedly.

## Open Questions

- Should repository coverage use H2 via `@DataJpaTest` or a dedicated test container if production SQL behavior becomes important later?
- Should the 80% threshold apply bundle-wide or only to a curated package set that excludes bootstrap/config classes?