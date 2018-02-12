package main.java.lexer;

public enum StateType {

    TERMINAL(0),
    DECISION(1),
    NUMERIC(2),
    INTEGER(3),
    REAL(4),
    ALPHABETIC(5),
    IDENTIFIER(6),
    ERROR(7);

    int id;
    StateType(int id){
        this.id = id;
    }

}



