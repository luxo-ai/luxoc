package main.java.lexer;

public class Test {

    int res;

    Test(int res){
        this.res = res;
    }

    public int addTwo(int a, int b){
        int loc = a;
        int loc2 = b;

        return res = mod5(loc + loc2);
    }

    public int mod5(int val){
        return val % 5;
    }

    public int getRes(){
        return this.res;
    }

    public static void main(String[] args){

        Test testy = new Test(0);

        System.out.println(testy.res);

        System.out.println(testy.addTwo(3,4));

        System.out.println(testy.res);

        System.out.println(testy.addTwo(9,0));

        System.out.println(testy.res);

    }
}
