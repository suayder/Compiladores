package sample;


import jdk.nashorn.internal.scripts.JO;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;


public class LL1_Gramar {
    private ArrayList<String> nonTerminal; //Alphabet of nonterminal variables
    private ArrayList<String> terminais; //Terminal Variable Alphabet
    private HashMap<String, ArrayList<ArrayList<String>>> P; //Rules of production
    private String S; //Initial symbol
    private HashMap<String, ArrayList<String>> First = new HashMap<String, ArrayList<String>>();
    private HashMap<String, ArrayList<String>> Follow = new HashMap<String, ArrayList<String>>();

    public HashMap<String, ArrayList<String>> getFirst(){
        return First;
    }

    public HashMap<String, ArrayList<String>> getFollow() {
        return Follow;
    }

    void print(String s){
        System.out.println(s);
    }
    void print(Character s){
        System.out.println(s);
    }
    void print(ArrayList<String> s){
        System.out.println(s);
    }

    public LL1_Gramar(ArrayList<String> v, ArrayList<String> t, HashMap<String, ArrayList<ArrayList<String>>> p, String s){

        nonTerminal = v;
        terminais = t;
        P = p;
        S = s;
        calcFirstFollow();
    }

    private void calcFirstFollow(){//Percorrer todas as regras de produções
        print("First: ");
        for(String s: P.keySet()){
            if(!First.containsKey(s)){
                First.put(s, GetFirst(s));
            }
            Follow.put(s,new ArrayList<String>());
        }
        for(String str:First.keySet()){
            print(str+":"+First.get(str));
        }
        print("============================");
        print("FOLLOW: ");
        for(String s: P.keySet()){
            Follow.put(s, GetFollow(s));
            System.out.print(s+": ");
            print(Follow.get(s));
        }
    }

    private ArrayList<String> GetFirst(String s){

        ArrayList<String> firstSet = new ArrayList<String>();
        for (ArrayList<String> prod:this.P.get(s)){
            if(prod.get(0).equals("&")){
                firstSet.add("&");
            }
            else{
                boolean flag = false;
                for (String symbol:prod) {
                    if(terminais.contains(symbol)){
                        firstSet = JoinSet(firstSet, symbol);
                        flag = false;
                        break;
                    }
                    else if(nonTerminal.contains(symbol) && !symbol.equals(s)){
                        ArrayList<String> firstSymbol;
                        if(this.First.containsKey(symbol)){
                            firstSymbol = this.First.get(symbol);
                        }
                        else{
                            firstSymbol = GetFirst(symbol);
                            First.put(symbol, firstSymbol);
                        }
                        firstSet = JoinSet(firstSet, firstSymbol);
                        flag = false;
                        break;
                    }
                }
                if(flag){
                    firstSet = JoinSet(firstSet, "&");
                }
            }
        }

        return firstSet;
    }

    private ArrayList<String> JoinSet(ArrayList<String> f1, ArrayList<String> f2){
        //ArrayList<String> aux = f1;
        for (String str:f2){
            if(!f1.contains(str)){
                f1.add(str);
            }
        }
        return f1;
    }

    private ArrayList<String> JoinSet(ArrayList<String> f1, String f2){
        if(!f1.contains(f2)){
            f1.add(f2);
        }
        return f1;
    }

    private ArrayList<String> JoinSetWithOutVoid(ArrayList<String> f1, ArrayList<String> f2){
        for (String str:f2){
            if(!f1.contains(str) && !str.equals("&")){
                f1.add(str);
            }
        }
        return f1;
    }

    private ArrayList<String> GetFollow(String s){
        ArrayList<String> followSet = new ArrayList<String>();
        //System.out.println("Compare: "+S.equals(s));

        if(S.equals(s)){
            followSet.add("$");
            Follow.get(S).add("$");
        }
        for(String variables:nonTerminal) {
            for (ArrayList<String> str : P.get(variables)){
                for (String symbol : str){
                    if(symbol.equals(s)){
                        boolean flag = false;
                        int k = str.indexOf(symbol);
                        for (k = str.indexOf(symbol) + 1; k < str.size(); k++){

                            if(terminais.contains(str.get(k))){
                                followSet = JoinSet(followSet, str.get(k));
                                flag = true;
                                break;
                            }
                            else if(First.get(str.get(k)).contains("&")){
                                followSet = JoinSet(followSet, First.get(str.get(k)));
                                flag = true;
                                break;
                            }
                            else{
                                followSet = JoinSetWithOutVoid(followSet, First.get(str.get(k)));
                                flag = true;
                                break;
                            }
                        }
                        for (ArrayList<String> w : P.get(S)){
                            if (symbol.equals(w.get(w.size() - 1))) {
                                followSet = JoinSet(followSet, "$");
                                break;
                            }
                        }

                        if (!flag && !variables.equals(s)){
                            if(Follow.get(variables).size()==0) {
                                Follow.put(variables, GetFollow(variables));
                            }
                            followSet = JoinSet(followSet,Follow.get(variables));
                        }
                    }
                }
            }
        }
        return  followSet;
    }

}
