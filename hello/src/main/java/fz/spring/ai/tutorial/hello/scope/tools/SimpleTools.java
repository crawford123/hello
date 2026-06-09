package fz.spring.ai.tutorial.hello.scope.tools;

import io.agentscope.core.tool.Tool;
import io.agentscope.core.tool.ToolParam;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class SimpleTools {


    @Tool(name = "get_time", description = "获取当前时间")
    public String getTime(@ToolParam(name = "zone", description = "时区：北京") String zone) {
        return LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyy-MM-dd HH:mm:ss"));

    }


}
