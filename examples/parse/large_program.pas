{In pascal, we define all of the variables, functions and such before the main program - like macros in C}
PROGRAM longProgram (foo, bar, baz);

VAR x,y,z : REAL;
    s,m,t: INTEGER;
    arry : array[0..3] of Integer;

FUNCTION func (a,b,c: Integer; val,val2: Real): result INTEGER;  {result must be a standard type, int or real}
{declarations can be null - which is what we’re doing in this case}
{now lets start the compound statement - which always start with begin and end with end}
BEGIN
  {statement-list -> statement statement-list-tail}
  {statement -> elementary-statement | if expression then statement else clause | while expression do statment}
  {elementary-statement -> identifier es-tail}
  {es-tail -> subscript assignop expression}
  {subscript -> [ expression ] | null}
  {expression -> simple-expression expression-tail}
  {simple-expression -> term simple-expression-tail | sign term simple-expression-tail}
  {…..}
  val [ val1 * val2 / val3 ] := 4;
  IF (val <> 3) THEN x := 3 ELSE y := 3;
  WHILE val <> 3 do x := 3 + y
END


FUNCTION empty: result REAL;
VAR a,b,c: INTEGER;

BEGIN
  i := 3
END

BEGIN
  { call a function }
  func;
  empty(3,2)

END. {end marker}

