# Lexical Analysis

Author: Luis F. Serazo

## Goals

The goal of lexical analysis is to classify program substrings according to their role (tokens) 
and communicate the tokens to the parser. 

STRING ===> LA == <class, string> ==> parser. Where <class, string> is the token.

Consider: foo = 42. You get 3 tokens: <Id, "foo">,  <Operator, "=">, <Int, "42">. Parser takes in these sequences of pairs.