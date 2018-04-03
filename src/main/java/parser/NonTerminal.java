/*
 * File: NonTerminal.java
 *
 * Desc: contains the NonTerminal enumerable.
 *
 */
package main.java.parser;
import main.java.grammar.GrammarSymbol;

/**
 * NonTerminal enum
 */
public enum NonTerminal implements GrammarSymbol{
    program(0),
    identifier_list(1),
    declarations(2),
    sub_declarations(3),
    compound_statement(4),
    identifier_list_tail(5),
    declaration_list(6),
    type(7),
    declaration_list_tail(8),
    standard_type(9),
    array_type(10),
    subprogram_declaration(11),
    subprogram_head(12), arguments(13),
    parameter_list(14),
    parameter_list_tail(15),
    statement_list(16),
    statement(17),
    statement_list_tail(18),
    elementary_statement(19),
    expression(20),
    else_clause(21),
    es_tail(22),
    subscript(23),
    parameters(24),
    expression_list(25),
    expression_list_tail(26),
    simple_expression(27),
    expression_tail(28),
    term(29),
    simple_expression_tail(30),
    sign(31),
    factor(32),
    term_tail(33),
    factor_tail(34),
    actual_parameters(35),
    Goal(36),
    constant(37);

    /* enum index */
    private int index;

    /**
     * NonTerminal: enum constructor.
     * @param index, the NT index.
     */
    NonTerminal(int index){ this.index = index; }

    /**
     * getIndex: returns the index of this GrammarSymbol
     * @return the GS index
     */
    public int getIndex(){ return this.index; }

    /**
     * isToken: determines if this GrammarSymbol is a Token
     * @return True if the GS is a Token, False otherwise
     */
    public boolean isToken(){ return false; }

    /**
     * isNonTerminal: determines if this GrammarSymbol is a NonTerminal
     * @return True if the GS is a NonTerminal, False otherwise
     */
    public boolean isNonTerminal(){ return true; }

    /**
     * isSemAction: determines if the GrammarSymbol is a SemanticAction
     * @return True if the GS is a SemanticAction, False otherwise
     */
    public boolean isSemAction(){ return false; }


} /* end of NonTerminal class */
