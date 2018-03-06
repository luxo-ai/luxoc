# Parser

Author: Luis F. Serazo

### A Parser Package

This package contains all the parsing material. It
uses the tokenizer to get the next Token in the file and 
parses the file through and LL(1) method. 

#### Parsing

To parse is to analyze a sentence into its parts and describe their syntactic roles.

### How to Test

The parser can be tested against any of the test files in */examples/parse*.
The ParseDriver will be used to load and test a file. You can use the debug option
(```-d``` or ```--debug```) to print the stack during parsing. 

### Pseudo Code

```Parser Pseudo Code:


Current token = GET Next Token

SET Parse Stack TO Empty stack;
PUSH ENDOFFILE and Start symbol ON Parse stack;

WHILE Current token not equal to ENDOFFILE:

   SET Predicted TO POP(Parse Stack)
   IF Predicted is a Token:
        // Try a match move:
         IF Predicted = Current token
             Current token = GET Next Token
             ELSE IF Predicted not equal to Current token:
                ERROR "Expecting x, y found"

   ELSE IF Predicted is a Non-terminal:
        IF [Parse_table [Predicted, Current token]] = ERROR
           ERROR  "Uexpected :" Current token
       ELSE
           PUSH Symbols in RHS [Parse_table [Predicted, Current token]]
                ON Parse Stack
```