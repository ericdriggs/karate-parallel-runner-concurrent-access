package examples;

import java.util.function.Function;

public class Hello {

    public static String sayHello (final String message) {
        return "hello " + message;
    }
    
    public static Function<String,String> sayHelloFactory() {
        return s-> sayHello(s);
    }

}
