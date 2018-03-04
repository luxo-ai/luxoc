{
FILE: Complex.pas
Author: Luis Serazo
}
PROGRAM myProgram(foo);

VAR arry1, arry2, arry3: ARRAY[1..2] of Integer;
    real1, real2, real3: Real;
    int1, int2, int3: Integer;
    myArry: ARRAY[7..8] of Real; {:= (1.0, 2.0); cannot do this}
    scientific: Real; {:= 1.0e+10; cannot do this}

FUNCTION routine: result Integer;
  VAR local: Integer; { cannot just assign here }
  BEGIN
     local := 0;
     { start routine }
     IF local = 0 THEN local := 1;
     WHILE (local <> 6) OR (local = 2) DO local := local + 1;
     { setting the return value }
     IF local = 6 THEN resulth := 0 ELSE resulth := -1 { given the grammar rules, there's a problem setting result }
  END

FUNCTION modOne(m,n: Integer): result Integer;
  BEGIN
    { mod one !}
    { must have something before setting result }
    m := m;
    res := m mod n
  END

FUNCTION addTwo(x,y: Real): result Real;
  BEGIN
    { must have something before setting result }
    x := x;
    result := x + y
  END

FUNCTION multTwo(x,y: Real): result Real;
  BEGIN
    { must have something before setting result }
    x := x;
    res := x * y
  END

{ program logic }
BEGIN
  int1 := routine;
  IF int1 = 0 THEN
    BEGIN
      int2 := modOne(3,2);
      int3 := addTwo(int2, 3);
      real1 := multTwo(myArry[7], myArry[8])
    END
  ELSE
    int2 := 2 div 3

{end of the program }
END.





