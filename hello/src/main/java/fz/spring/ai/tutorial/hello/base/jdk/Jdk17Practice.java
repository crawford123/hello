package fz.spring.ai.tutorial.hello.base.jdk;

import java.util.Objects;
import java.util.random.RandomGenerator;
import java.util.random.RandomGeneratorFactory;

public class Jdk17Practice {


    public static void PRNG() {
        RandomGeneratorFactory<RandomGenerator> factory = RandomGeneratorFactory.of("L128X256MixRandom");
        // 使用时间戳作为随机数种子
        RandomGenerator randomGenerator = factory.create(System.currentTimeMillis());
        // 生成随机数
        int random = randomGenerator.nextInt(10);
        System.out.println(random);
    }

    public static void instancePractice(Object object) {
        if (object instanceof String str) {
            str = str.concat(" test");
            // str:hello test
            System.out.println("str:" + str);
        } else if (object instanceof Integer integer) {
            integer = integer + 3;
            System.out.println("integer:" + integer);
        }
    }

    public static String oldFormatter(Object o) {
        String formatted = "unknown";
        if (o instanceof Integer i) {
            formatted = String.format("int %d", i);
        } else if (o instanceof Long l) {
            formatted = String.format("long %d", l);
        } else if (o instanceof Double d) {
            formatted = String.format("double %f", d);
        } else if (o instanceof String s) {
            formatted = String.format("String %s", s);
        }
        return formatted;
    }

    public static String formatterPatternSwitch(Object o) {
        return switch (o) {
            case Integer i -> String.format("int %d", i);
            case Long l -> String.format("long %d", l);
            case Double d -> String.format("double %f", d);
            case String s -> String.format("String %s", s);
            default -> o.toString();
        };
    }


    public static void main(String[] args) {
        PRNG();
        instancePractice("hello");
//        switchPractice("hello");
    }

}
