package Trabalho2;

import java.util.LinkedList;
import java.util.Stack;



public class RegularExpression {

    private String infixa;
    private String posfixa;
    private Stack<Character> pilha;
    private char operadores[] = {'+', '.', '*'};
    private Automato automato;

    RegularExpression(String entrada)
    {
        this.infixa = formatEntrada(entrada);
        this.pilha = new Stack<Character>();
        this.posfixa = this.converter(this.infixa);
        this.automato = this.gerarAutomato();

    }

    public Automato getAutomato()
    {
        return this.automato;
    }

    private Automato gerarAutomato()
    {
        boolean literal = false;
        char entrada[] = this.posfixa.toCharArray();
        Stack<Automato> pilha = new Stack<Automato>();
        for(char simbolo : entrada)
        {
            switch (simbolo)
            {
                case '*':
                    if (!pilha.isEmpty()) {
                        pilha.push(Automato.fechoDeKleene(pilha.pop()));
                    }
                    break;
                case '+':
                    if(!pilha.isEmpty())
                    {
                        Automato a2 = pilha.pop();
                        if(!pilha.isEmpty())
                        {
                            Automato a1 = pilha.pop();
                            pilha.push(Automato.uniao(a1,a2));
                        }
                    }
                    break;
                case '.':
                    if(!pilha.isEmpty())
                    {
                        Automato a2 = pilha.pop();
                        if(!pilha.isEmpty())
                        {
                            Automato a1 = pilha.pop();
                            pilha.push(Automato.concatenacao(a1,a2));
                        }
                    }
                    break;
                default:
                    if(isOperando(simbolo))
                    {
                        pilha.push(new Automato(simbolo));
                    }


            }


        }
        Automato resultado = pilha.pop();
        if(pilha.isEmpty()) System.out.println("Sucesso Geração Automato!");
        return resultado;
    }

    private String formatEntrada(String entrada)
    {

        char entradaArray[] = entrada.toCharArray();
        //Implementação LITERAL
        boolean literal = false;
        StringBuilder newEntrada = new StringBuilder();
        LinkedList<Character> replaceList = new LinkedList<Character>();
        for(char c : entradaArray)
        {
            if(literal)//Se o ultimo caractere foi o literal
            {
                newEntrada.append('\\');
                literal = false;
                replaceList.add(c);
            }
            else {
                if (c == '\\') {
                    literal = true;
                } else {
                    newEntrada.append(c);
                }
            }
        }
        //FIM implementação LITERAL
        entrada = newEntrada.toString();
        entradaArray = entrada.toCharArray();
        StringBuilder saida = new StringBuilder();
        for(int i = 0; i < entrada.length(); i++)
        {
            char c1 = entradaArray[i];
            saida.append(c1);
            if(i+1 < entradaArray.length)
            {
                char c2 = entradaArray[i+1];
                if((c1 != '(') && (c2 != ')') && !isOperator(c2) && (c1 != '+')) saida.append('.');
            }
        }

        //Restaurar literais
        StringBuilder saidacomLiteral = new StringBuilder();
        for(char c : saida.toString().toCharArray())
        {
            if(c == '\\'){
                saidacomLiteral.append(c).append(replaceList.removeFirst());
            }
            else saidacomLiteral.append(c);
        }

        System.out.println("Saida com literal: " + saidacomLiteral.toString());
        System.out.println("Saida sem literal: " + saida.toString());


        //return saida.toString();
        return saidacomLiteral.toString();
    }

    private String converter(String entrada)
    {
        StringBuilder saida = new StringBuilder();
        char entradaArray[] = entrada.toCharArray();
        boolean literal = false;
        for(char simbolo : entradaArray)
        {
            if(literal)
            {
                saida.append(simbolo);
                literal = false;
            }
            else {
                switch (simbolo) {
                    case '\\':
                        saida.append(simbolo);
                        literal = true;
                        break;
                    case '(':
                        pilha.push(simbolo);
                        break;
                    case ')':
                        while (!pilha.isEmpty() && pilha.peek() != '(') {
                            saida.append(pilha.pop());
                        }
                        if (!pilha.isEmpty()) pilha.pop();
                        break;
                    default:
                        if (isOperando(simbolo)) saida.append(simbolo);
                        else {
                            while (!pilha.isEmpty()) {
                                if (getPrecedence(pilha.peek()) >= getPrecedence(simbolo)) saida.append(pilha.pop());
                                else break;
                            }
                            pilha.push(simbolo);
                        }

                }
            }
        }
        while(!pilha.isEmpty() && isOperator(pilha.peek()))
        {
            saida.append(pilha.pop());
        }

        if(pilha.isEmpty()) System.out.println("Sucesso!");
        else System.out.println("Pilha não vazia!");


        return saida.toString();
    }

    private int getPrecedence(char simbolo)
    {
        if(simbolo == '+') return 0;
        if(simbolo == '.') return 1;
        if(simbolo == '*') return 2;

        return -1;
    }

    private boolean isOperando(char simbolo)
    {
        if(isOperator(simbolo)) return false;
        if(simbolo == '(' || simbolo == ')') return false;
        return true;
    }

    private boolean isOperator(char simbolo)
    {
        for (char operador : this.operadores) {
            if (simbolo == operador) return true;
        }
        return false;
    }


    public String getInfixa()
    {
        return this.infixa;

    }
    public String getPosfixa()
    {

        return this.posfixa;
    }




}
