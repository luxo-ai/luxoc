Program reltest (input,output);
Var
  h,i,j,k,l:integer;
  m,o : array[1..5] of integer;
  n : array[0..4] of real;
  s,t,u,v,x : real;

begin

  h := -1;
  i := 0;
  j := 1;
  k := 2;
  l := 3;


  h := h * (-1) + 3;

  s := -1.1;
  t := 1.1;
  u := 2.1;
  v := 3.1;
  x := 4.1;

  o[1] := 1;
  o[2] := 2;
  o[3] := 3;
  o[4] := 4;
  o[5] := 5;

  m[i+1] := h;
  m[k] := i;
  m[l] := j;
  m[h] := k;
  m[j+h] := l;

  n[h] := s;
  n[i] := t;
  n[j] := u;
  n[k] := v;
  n[l] := x;


  while j < 6 do
  begin
    j := j +1

  end;


  while (((i <= h) or (m[5] > 8)) and (o[1] < 35)) do
  begin
    i := i + 1
  end

end.
