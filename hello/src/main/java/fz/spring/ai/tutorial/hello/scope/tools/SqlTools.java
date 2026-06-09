package fz.spring.ai.tutorial.hello.scope.tools;

import fz.spring.ai.tutorial.hello.scope.context.UserContext;
import io.agentscope.core.tool.Tool;
import io.agentscope.core.tool.ToolParam;

public class SqlTools {

    // 工具中自动注入
    @Tool(name = "query", description = "查询数据")
    public String query(@ToolParam(name = "sql") String sql, UserContext ctx) {
        return "用户 " + ctx.getUserId() + " 的查询结果";
    }
}
