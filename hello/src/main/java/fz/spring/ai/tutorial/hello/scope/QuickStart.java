package fz.spring.ai.tutorial.hello.scope;

import fz.spring.ai.tutorial.hello.scope.context.UserContext;
import fz.spring.ai.tutorial.hello.scope.tools.SimpleTools;
import fz.spring.ai.tutorial.hello.scope.tools.SqlTools;
import io.agentscope.core.ReActAgent;
import io.agentscope.core.agent.user.UserAgent;
import io.agentscope.core.message.Msg;
import io.agentscope.core.model.DashScopeChatModel;
import io.agentscope.core.model.ExecutionConfig;
import io.agentscope.core.model.Model;
import io.agentscope.core.plan.PlanNotebook;
import io.agentscope.core.tool.ToolExecutionContext;
import io.agentscope.core.tool.Toolkit;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

public class QuickStart {


    public static void main(String[] args) {
        // 准备工具
        Toolkit toolkit = new Toolkit();
        toolkit.registerTool(new SimpleTools());

        System.out.println("apiKey: " + System.getenv("DASHSCOPE_API_KEY"));
        // 创建智能体
        Model model = DashScopeChatModel.builder().apiKey(System.getenv("DASHSCOPE_API_KEY")).modelName("qwen-max").build();

        ReActAgent jarvis = ReActAgent.builder().name("Jarvis").sysPrompt("你是一个名为Jarvis的助手").model(model).toolkit(toolkit).build();

        // 发送消息
        Msg msg = Msg.builder().textContent("你好！Javis，现在几点了？").build();

        Msg response = jarvis.call(msg).block();
        // 现在北京时间是2026年02月09日 07:31:19。
        System.out.println(response.getTextContent());

        // 更多配置
        // 执行控制
        ReActAgent agent = ReActAgent.builder().name("Assistant").sysPrompt("You are a helpful assistant.").model(model)
                // 最大迭代次数（默认 10）
                .maxIters(10)
                // 阻止并发调用（默认 true）
                .checkRunning(true).build();

        // 超时与重试
        // 模型调用的超时/重试配置
        ExecutionConfig modelConfig = ExecutionConfig.builder().timeout(Duration.ofSeconds(10)).maxAttempts(3).build();

        // 工具调用的超时/重试配置
        ExecutionConfig toolConfig = ExecutionConfig.builder().timeout(Duration.ofSeconds(30)).maxAttempts(1).build();

        ReActAgent agent1 = ReActAgent.builder().name("Assistant").model(model).modelExecutionConfig(modelConfig).toolExecutionConfig(toolConfig).build();

        // 工具执行上下文
        // 向工具传递业务上下文（如用户信息），无需暴露给 LLM：
        ToolExecutionContext toolExecutionContext = ToolExecutionContext.builder().register(new UserContext("user123")).build();
        toolkit.registerTool(new SqlTools());
        ReActAgent reActAgent = ReActAgent.builder().name("Assistant").model(model).toolkit(toolkit).toolExecutionContext(toolExecutionContext).build();
        // 发送消息
        Msg msg1 = Msg.builder().textContent("帮我执行下这个sql：select * from user where id = 1").build();
        Msg response1 = reActAgent.call(msg1).block();
        // 查询结果为：用户 user123 的查询结果。请注意，这里的输出是模拟的，真实的 SQL 查询结果会依赖于数据库中的实际数据。
        System.out.println(response1.getTextContent());

        // 计划管理
        // 启用 PlanNotebook 支持复杂多步骤任务
        // 快速启用
        ReActAgent agent2 = ReActAgent.builder().name("Assistant").model(model).enablePlan().build();
        // 自定义配置
        PlanNotebook planNotebook = new PlanNotebook.Builder().maxSubtasks(15).build();
        ReActAgent agent3 = ReActAgent.builder().name("Assistant").model(model).planNotebook(planNotebook).build();

        // UserAgent
        // 接收外部输入的智能体（如命令行、Web UI）：
        UserAgent userAgent = UserAgent.builder().name("用户").build();
        List<Msg> msgs = new ArrayList<>();
        msgs.add(Msg.builder().textContent("你是谁").build());
        Msg userInput = userAgent.call(msgs).block();
        // userInput:你是谁哇
        System.out.println("userInput:" + userInput.getTextContent());
    }


}
