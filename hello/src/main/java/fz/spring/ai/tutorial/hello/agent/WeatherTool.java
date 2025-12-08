package fz.spring.ai.tutorial.hello.agent;

import org.jetbrains.annotations.NotNull;
import org.springframework.ai.chat.model.ToolContext;

import java.util.function.BiFunction;
import java.util.function.Function;

public class WeatherTool implements BiFunction<String, ToolContext, String> {


    @Override
    public String apply(String city, ToolContext toolContext) {
        return "";
    }

    @NotNull
    @Override
    public <V> BiFunction<String, ToolContext, V> andThen(@NotNull Function<? super String, ? extends V> after) {
        return BiFunction.super.andThen(after);
    }
}
