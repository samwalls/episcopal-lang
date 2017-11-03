package ir.translation;

import ir.statement.Statement;

public class Method {

    public String name;

    public Statement statement;

    public EnvNode<String> environment;

    public Method(String name, EnvNode<String> environment, Statement statement) {
        this.name = name;
        this.environment = environment;
        this.statement = statement;
    }
}
