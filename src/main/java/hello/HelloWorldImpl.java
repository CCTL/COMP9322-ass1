
package hello;

import javax.jws.WebService;

@WebService(endpointInterface = "hello.HelloWorld")
public class HelloWorldImpl implements HelloWorld {

    public String sayHi(String text) {
        return "Hello " + text;
    }
}

