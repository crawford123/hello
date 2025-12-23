package fz.spring.ai.tutorial.hello.agent.base;

import com.alibaba.cloud.ai.dashscope.api.DashScopeApi;
import com.alibaba.cloud.ai.dashscope.chat.DashScopeChatModel;
import com.alibaba.cloud.ai.graph.agent.ReactAgent;
import com.alibaba.cloud.ai.graph.checkpoint.savers.MemorySaver;
import com.alibaba.cloud.ai.graph.exception.GraphRunnerException;
import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.tool.ToolCallback;
import org.springframework.ai.tool.function.FunctionToolCallback;

public class BaseWeatherAgent {

    public static void main(String[] args) throws GraphRunnerException {
        // 初始化 ChatModel
        String AI_DASHSCOPE_API_KEY = System.getenv("AI_DASHSCOPE_API_KEY");
        System.out.println("AI_DASHSCOPE_API_KEY: " + AI_DASHSCOPE_API_KEY);
        DashScopeApi dashScopeApi = DashScopeApi.builder().apiKey(AI_DASHSCOPE_API_KEY).build();
        ChatModel chatModel = DashScopeChatModel.builder().dashScopeApi(dashScopeApi).build();
        System.out.println(chatModel);

        ToolCallback weatherTool = FunctionToolCallback.builder("get_weather", new WeatherTool()).description("Get weather for a given city").inputType(String.class).build();

        // 创建agent
        ReactAgent agent = ReactAgent.builder().name("weather_agent").model(chatModel).tools(weatherTool).systemPrompt("You are a helpful assistant").saver(new MemorySaver()).build();

        // 运行agent
        // What's the weather like in Shanghai?
        AssistantMessage response = agent.call("what is the weather in San Francisco");
        // It seems there might be some confusion—while San Francisco is known for its pleasant climate, it's not always sunny. The city often experiences fog, especially during the summer months, and has a more moderate temperature range compared to other parts of California. If you'd like current weather details for San Francisco, I can help check that for you.
        System.out.println(response.getText());
    }
}
