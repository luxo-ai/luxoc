package main.java.lexer;

public enum StateType {

    TERMINAL(0),
    DECISION(1),
    NUMERIC(2),
    INTEGER(3),
    REAL(4),
    EXPON(5),
    ALPHABETIC(6),
    IDENTIFIER(7),
    ERROR(8);

    int id;
    StateType(int id){
        this.id = id;
    }

}



