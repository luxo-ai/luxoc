# Lexical Analysis

Author: Luis F. Serazo

## Goals

The goal of lexical analysis is to classify program substrings according 
to their role (tokens) and communicate the tokens to the parser. 

STRING ===> LA == <class, string> ==> parser. Where <class, string> 
is the token.

Consider: foo = 42. You get 3 tokens: <Id, "foo">,  <Operator, "=">, 
<Int, "42">. Parser takes in these sequences of pairs.

### Info and challenges

Substrings corresponding to tokens are called lexemes. Important when we tokenize 
to figure out how to handle strings like: x == 4. Is this, a seq. of lexemes: 
"x | = | = | 4" or "x | == | 4"? Need some kind of look-ahead. Need to figure out 
where one token ends and another begins. 

### Considerations

Read from left to right, generating each token one at a time.
