## 1. Rebuild the test baseline

- [x] 1.1 Audit the current `src/test/java` tree and remove or replace obsolete tests so the source tree matches the intended suite boundaries.
- [x] 1.2 Keep a minimal `ApplicationTests` smoke test and create the missing JUnit 5 class skeletons for controller, service, repository, exception, and result coverage.

## 2. Implement deterministic test slices

- [x] 2.1 Reimplement `EmployeeServiceImplTest` with Mockito-managed `EmployeeDao` and `EmployeeMapper` collaborators covering save, lookup, update, and delete flows.
- [x] 2.2 Reimplement `EmployeeControllerWebMvcTest` and `GlobalExceptionHandlerTest` with `@WebMvcTest`, mocked service behavior, and assertions for success, validation failure, and not-found responses.
- [x] 2.3 Reimplement `EmployeeDaoTest` and `ResultTest` with deterministic fixtures that verify repository persistence semantics and response-wrapper factory behavior.

## 3. Enforce Maven quality gates

- [x] 3.1 Update `pom.xml` so Surefire does not ignore test failures and JaCoCo checks line coverage during `mvn test`.
- [x] 3.2 Tune JaCoCo includes or excludes so the 80% threshold measures behavior-rich application code rather than trivial bootstrap noise.

## 4. Validate the rewrite

- [x] 4.1 Run `mvn test` and fix any failing assertions, slice configuration errors, or flaky fixtures until the command exits successfully.
- [x] 4.2 Confirm the JaCoCo result produced by `mvn test` is at or above 80% and document any intentional exclusions needed to keep the gate stable.