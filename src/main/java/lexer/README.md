# Lexical Analyzer 

Author: Luis F. Serazo

## Purpose

The goal of lexical analysis is to classify program substrings (lexemes) according 
to their role. These substrings are represented as Token pairs. 

**Example**: 
Substring :== Lexer :== <class, string>. Where <class, string> 
is the token.

Consider: foo := 42. You'd get 3 tokens: <identifier, "foo">,  <assignOP, ":=">, 
<integer, "42">. The parser uses these sequences of Tokens by calling the: ```getNextToken()``` method. 


### FileStream

The FileStream class acts as a wrapper for the BufferedReader class in Java. It
handles the work of passing characters in the source file to the Tokenizer. It
also handles the work of ignoring comments and throwing illegal character errors when
a unrecognized character appears.  

### Testing

LexDriver acts a simple run-of-the-mill way of testing the lexer. I've also provided a 
number of test files in */examples/lex*. These have been designed to test the limits of the 
lexer.
