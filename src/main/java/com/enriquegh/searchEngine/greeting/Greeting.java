package com.enriquegh.searchEngine.greeting;

public class Greeting {

    private String message;


    public Greeting(String name) {
        this.message = getGreeting(name);
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String name) {
        this.message = name;
    }

    private String getGreeting(String name) {
        return "Hello " + name;
    }
}
