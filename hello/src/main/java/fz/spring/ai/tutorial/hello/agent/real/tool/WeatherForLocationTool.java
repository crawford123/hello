package fz.spring.ai.tutorial.hello.agent.real.tool;

import org.jetbrains.annotations.NotNull;
import org.springframework.ai.chat.model.ToolContext;
import org.springframework.ai.tool.annotation.ToolParam;

import java.util.function.BiFunction;
import java.util.function.Function;

public class WeatherForLocationTool implements BiFunction<String, ToolContext, String> {


    @Override
    public String apply(@ToolParam(description = "The city name") String city, ToolContext toolContext) {
        return "It's always sunny in " + city + "!";
    }

}
