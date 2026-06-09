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
        }
    }

//    public static String switchPractice(String str) {
//        switch (str) {
//            case null -> System.out.println();
//
//        }
//    }

    public static void main(String[] args) {
        PRNG();
//        switchPractice("hello");
    }

}
