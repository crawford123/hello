package fz.spring.ai.tutorial.hello.agent.real.tool;

import com.alibaba.cloud.ai.dashscope.api.DashScopeApi;
import com.alibaba.cloud.ai.dashscope.chat.DashScopeChatModel;
import com.alibaba.cloud.ai.dashscope.chat.DashScopeChatOptions;
import com.alibaba.cloud.ai.graph.RunnableConfig;
import com.alibaba.cloud.ai.graph.agent.ReactAgent;
import com.alibaba.cloud.ai.graph.checkpoint.savers.MemorySaver;
import com.alibaba.cloud.ai.graph.exception.GraphRunnerException;
import fz.spring.ai.tutorial.hello.agent.real.response.ResponseFormat;
import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.tool.ToolCallback;
import org.springframework.ai.tool.function.FunctionToolCallback;

public class RealWeatherAgent {

   private static String SYSTEM_PROMPT = """
    You are an expert weather forecaster, who speaks in puns.

    You have access to two tools:

    - get_weather_for_location: use this to get the weather for a specific location
    - get_user_location: use this to get the user's location

    If a user asks you for the weather, make sure you know the location.
    If you can tell from the question that they mean wherever they are,
    use the get_user_location tool to find their location.
    """;


   private void test() throws GraphRunnerException {
       // 创建工具回调
       ToolCallback getWeatherTool = FunctionToolCallback
               .builder("getWeatherForLocation", new WeatherForLocationTool())
               .description("Get weather for a given city")
               .inputType(String.class)
               .build();

       ToolCallback getUserLocationTool  = FunctionToolCallback
               .builder("getUserLocation", new UserLocationTool())
               .description("Retrieve user location based on user ID")
               .inputType(String.class)
               .build();

       // 配置模型
       DashScopeApi dashScopeApi = DashScopeApi.builder()
               .apiKey(System.getenv("AI_DASHSCOPE_API_KEY"))
               .build();

       ChatModel chatModel = DashScopeChatModel.builder()
               .dashScopeApi(dashScopeApi)
               .defaultOptions(DashScopeChatOptions.builder()
                       // Note: model must be set when use options build.
                       .withModel(DashScopeChatModel.DEFAULT_MODEL_NAME)
                       .withTemperature(0.5)
                       .withMaxToken(1000)
                       .build())
               .build();

       // 添加记忆
       // 在生产环境中，使用持久化的 CheckPointer 将数据保存到数据库。
//       ReactAgent reactAgent = ReactAgent.builder().
//               name("weather_agent")
//               .saver(new MemorySaver())
//               .build();

//       RunnableConfig runnableConfig = RunnableConfig.builder().threadId("1").build();
       // 第一次调用
//       AssistantMessage response1 = reactAgent.call("hat is the weather in San Francisco today.", runnableConfig);
       // 第二次调用
//       AssistantMessage response2 = reactAgent.call("How about the weather tomorrow", runnableConfig);

       // 创建和运行 Agent
       ReactAgent reactAgent = ReactAgent.builder()
               .name("weather_pun_agent")
               .model(chatModel)
               .systemPrompt(SYSTEM_PROMPT)
               .tools(getUserLocationTool, getWeatherTool)
               .outputType(ResponseFormat.class)
               .saver(new MemorySaver())
               .build();

       String threadId = "Thread-123";
       // threadId 是给定对话的唯一标识符
       RunnableConfig runnableConfig = RunnableConfig.builder().threadId(threadId).addMetadata("user_id", "2").build();

       // 第一次调用
       AssistantMessage response = reactAgent.call("what is the weather outside?", runnableConfig);

//       {
//           "punnyResponse": "Looks like San Francisco is shining bright—it’s a sun-derful day!",
//               "weatherConditions": "sunny"
//       }
       System.out.println(response.getText());

       // 注意我们可以使用相同的 threadId 继续对话
       response = reactAgent.call("thank you!", runnableConfig);
//       {
//           "punnyResponse": "You're welcome! Stay sunny on the inside, even if it's raining outside!",
//               "weatherConditions": "clear"
//       }
       System.out.println(response.getText());

   }

    public static void main(String[] args) throws GraphRunnerException {
        RealWeatherAgent realWeatherAgent = new RealWeatherAgent();
        realWeatherAgent.test();
    }



}
