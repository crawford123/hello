# AGENTS.md

This file provides guidelines for agentic coding assistants working on this Spring Boot project.

## Project Overview

This is a Spring Boot application integrating Spring AI with Alibaba Cloud AI (DashScope) and OpenAI. The project demonstrates AI agent patterns, tool calling, and reactive streaming responses.

- **Build System**: Maven
- **Java Version**: 17 (but prefer Java 8 syntax per CLAUDE.md)
- **Framework**: Spring Boot 3.5.3, Spring AI 1.0.0
- **Testing**: JUnit 5 (Jupiter)

## Build, Lint, and Test Commands

All Maven commands should be run from the `hello/` directory (or root with `-f hello/pom.xml`).

```bash
# Build the project
mvn clean compile

# Run tests
mvn test

# Run a specific test class
mvn test -Dtest=HelloApplicationTests

# Run a specific test method
mvn test -Dtest=HelloApplicationTests#contextLoads

# Build package
mvn clean package

# Run the application
mvn spring-boot:run
```

## Code Style Guidelines

### Imports
- Avoid wildcard imports (e.g., use `import java.util.List;` not `import java.util.*;`)
- Group imports: standard library, third-party, project-specific
- Remove unused imports

### Formatting
- Use 4 spaces for indentation (no tabs)
- Maximum line length: 120 characters (soft limit)
- Place opening brace on same line for class/method declarations
- One blank line between methods

### Types and Generics
- Always specify generic types (avoid raw types)
- Use diamond operator `<>` when type is inferred
- Prefer `List<T>` over `ArrayList<T>` for method parameters/returns

### Naming Conventions
- **Classes**: PascalCase (e.g., `HelloController`, `WeatherTool`)
- **Methods**: camelCase (e.g., `helloStream`, `getUserLocation`)
- **Variables**: camelCase (e.g., `chatClient`, `executor`)
- **Constants**: UPPER_SNAKE_CASE (e.g., `SYSTEM_PROMPT`, `AI_DASHSCOPE_API_KEY`)
- **Packages**: all lowercase (e.g., `fz.spring.ai.tutorial.hello`)

### Error Handling
- Use specific exceptions, not generic `Exception` or `RuntimeException`
- Log errors appropriately (use SLF4J/Logback via `@Slf4j`)
- Handle checked exceptions with `throws` or try-catch as appropriate
- When wrapping exceptions, preserve the original cause

### Spring Boot Specifics
- Use constructor injection over field injection
- Use `@Autowired` implicitly via constructor parameters
- Controllers should return responses with proper content types
- For streaming responses, use `Flux<String>` with `produces = "text/html;charset=UTF-8"`

### AI Agent Patterns
- Agents use `ReactAgent` from Alibaba Cloud AI Graph
- Tools implement `BiFunction<String, ToolContext, String>` or similar interfaces
- Use `FunctionToolCallback.builder()` to register tools
- Configure agents with `.builder()` pattern
- Use `MemorySaver` for conversation state (production should use persistent storage)
- Use `RunnableConfig` with threadId for multi-turn conversations

### Dependencies
- Spring AI for AI chat model abstractions
- Alibaba Cloud AI (DashScope) for Chinese LLM support
- Project Reactor for reactive programming
- Spring Actuator for monitoring

## Testing
- Use JUnit 5 (`@Test`, `@SpringBootTest`)
- Test classes should end with `Tests` (e.g., `HelloApplicationTests`)
- Mock external dependencies using Mockito when appropriate
- Write descriptive test method names

## Environment Variables
Required environment variables (set in system or `.env` file):
- `AI_DASHSCOPE_API_KEY`: Alibaba Cloud API key for DashScope

## Key Conventions from CLAUDE.md
- **IMPORTANT**: Prefer Java 8 syntax even though project uses Java 17
- Avoid using streams, lambdas, and other Java 9+ features unless necessary
- Use traditional for-loops and explicit type declarations
