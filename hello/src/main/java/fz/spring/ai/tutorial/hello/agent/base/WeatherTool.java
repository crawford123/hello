package fz.spring.ai.tutorial.hello.agent.base;

import com.alibaba.cloud.ai.dashscope.api.DashScopeApi;
import com.alibaba.cloud.ai.dashscope.chat.DashScopeChatModel;
import com.alibaba.cloud.ai.graph.agent.ReactAgent;
import com.alibaba.cloud.ai.graph.checkpoint.savers.MemorySaver;
import com.alibaba.cloud.ai.graph.exception.GraphRunnerException;
import org.jetbrains.annotations.NotNull;
import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.model.ToolContext;
import org.springframework.ai.tool.ToolCallback;
import org.springframework.ai.tool.function.FunctionToolCallback;

import java.util.function.BiFunction;
import java.util.function.Function;

public class WeatherTool implements BiFunction<String, ToolContext, String> {

    @Override
    public String apply(String city, ToolContext toolContext) {
        return "It's always sunny in " + city + "!";
    }

    @NotNull
    @Override
    public <V> BiFunction<String, ToolContext, V> andThen(@NotNull Function<? super String, ? extends V> after) {
        return BiFunction.super.andThen(after);
    }

    public static void main(String[] args) throws GraphRunnerException {
        // 初始化 ChatModel
        String AI_DASHSCOPE_API_KEY = System.getenv("AI_DASHSCOPE_API_KEY");
     System.out.println("AI_DASHSCOPE_API_KEY: " + AI_DASHSCOPE_API_KEY);
        DashScopeApi dashScopeApi = DashScopeApi.builder().apiKey("sk-y3RYpGHh4YOWSoEk63YmpqxYTZyoJ40bURss0S8u2hMC6CWE").build();
        ChatModel chatModel = DashScopeChatModel.builder().dashScopeApi(dashScopeApi).build();
        System.out.println(chatModel);

        ToolCallback weatherTool  = FunctionToolCallback.builder("get_weather", new WeatherTool())
                .description("Get weather for a given city")
                .inputType(String.class)
                .build();

        // 创建agent
        ReactAgent agent = ReactAgent.builder()
                .name("weather_agent")
                .model(chatModel)
                .tools(weatherTool)
                .systemPrompt("You are a helpful assistant")
                .saver(new MemorySaver())
                .build();

        // 运行agent
        // What's the weather like in Shanghai?
        AssistantMessage response = agent.call("what is the weather in San Francisco");
        System.out.println(response.getText());
    }
}
