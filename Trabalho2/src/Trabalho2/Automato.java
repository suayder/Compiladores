package Trabalho2;

import org.omg.PortableInterceptor.INACTIVE;

import java.awt.*;
import java.util.*;


public class Automato {

    private ArrayList<Character> alfabeto;
    private ArrayList<Integer> estados;
    private ArrayList<Integer> matrizDeTransicao[][];
    private int estadoInicial;
    private ArrayList<Integer> estadosFinais;
    private Hashtable<Integer, ArrayList<Integer>> fechoE;

    private Integer matrizDeTransicaoAFD[][];
    private ArrayList<Integer> estadosAFD;
    private ArrayList<Character> alfabetoAFD;
    private int estadoInicialAFD;
    private ArrayList<Integer> estadosFinaisAFD;

    private Integer matrizTransicaoAFDMinimo[][];
    private ArrayList<Integer> EstadosFinaisAFDMinimo;

    Automato() {
        this.alfabeto = new ArrayList<Character>();
        this.estadosFinais = new ArrayList<Integer>();
        this.estados = new ArrayList<Integer>();
        this.estadoInicial = 0;
        this.fechoE = new Hashtable<Integer, ArrayList<Integer>>();
    }

    Automato(int qtEstados, int tamAlfabeto) {
        this.alfabeto = new ArrayList<Character>();
        this.estadosFinais = new ArrayList<Integer>();
        this.estados = new ArrayList<Integer>();
        this.estadoInicial = 0;
        this.matrizDeTransicao = new ArrayList[qtEstados][tamAlfabeto];
        for(int i = 0; i < qtEstados; i++)
            for(int j = 0; j < tamAlfabeto; j++)
                this.matrizDeTransicao[i][j] = new ArrayList<Integer>();

        this.fechoE = new Hashtable<Integer, ArrayList<Integer>>();

    }

    public ArrayList<Character> getAlfabeto() {
        return alfabeto;
    }

    public ArrayList<Integer> getEstados() {
        return estados;
    }

    public ArrayList<Integer>[][] getMatrizDeTransicao() {
        return matrizDeTransicao;
    }

    public Integer[][] getMatrizDeTransicaoAFD(){
        return matrizDeTransicaoAFD;
    }

    public int getEstadoInicial() {
        return estadoInicial;
    }

    public ArrayList<Integer> getEstadosFinais() {
        return estadosFinais;
    }

    private void setAlfabetoAFD(){
        if(this.alfabetoAFD == null)
            this.alfabetoAFD = new ArrayList<Character>();

        for(Character i:this.alfabeto){
            if(i!='&') {
                this.alfabetoAFD.add(i);
            }
        }
    }

    public Integer[][] getMatrizTransicaoAFDMinimo(){
        return matrizTransicaoAFDMinimo;
    }

    private int ehSimbolo(char c) {
        for (char simbolo : alfabeto) {
            if (c == simbolo) return alfabeto.indexOf(simbolo);
        }
        /*for(int i = 0; i < alfabeto.length; i++)
        {
            if(c == alfabeto[i]) return i;
        }*/
        return -1;
    }

    private int ehEstado(int q) {
        for (int estado : estados) {
            if (q == estado) return estados.indexOf(estado);
        }
        /*for(int i = 0; i < estados.length; i++)
        {
            if(q == estados[i]) return i;
        }*/
        return -1;
    }

    private boolean ehEstadoFinal(int q) {
        /*for(int i = 0; i < estados.length; i++)
        {
            if(q == estadosFinais[i]) return true;
        }*/
        for (int estado : estadosFinais) {
            if (q == estado) return true;
        }
        return false;
    }

    public ArrayList<Integer> funcaoDeTransicao(int q, char a) {
        int posEstado = ehEstado(q);
        int posSimbolo = ehSimbolo(a);
        if (posEstado != -1 && (posSimbolo != -1)) {
            return matrizDeTransicao[posEstado][posSimbolo];
        }
        else {
            return null;
        }
    }

    private int ehEstadoAFD(int estado){
        for (int q : this.estadosAFD) {
            if (q == estado) return this.estadosAFD.indexOf(estado);
        }
        return -1;
    }

    private int ehSimboloAFD(int simbolo){
        for (char s : this.alfabetoAFD) {
            if (s == simbolo) return this.alfabetoAFD.indexOf(s);
        }

        return -1;
    }

    private boolean ehEstadoFinalAFD(int estado) {
        return this.estadosFinaisAFD.contains(estado);
    }

    public Integer funcaoDeTransicaoAFD(int estado, char simbolo){
        int posEstado = this.ehEstadoAFD(estado);
        int posAlfabeto = this.ehSimboloAFD(simbolo);
        //System.out.println("pos Estado: "+ posEstado + " f "+posAlfabeto);
        if(posEstado!=-1 && posAlfabeto!=-1){
            return matrizDeTransicaoAFD[posEstado][posAlfabeto];
        }
        else
            return -1;
    }

    public static ArrayList<Character> uneAlfabetos(ArrayList<Character> alfabeto1, ArrayList<Character> alfabeto2) {
        ArrayList<Character> resultado = new ArrayList<Character>();
        for (char c : alfabeto1) {
            if (!resultado.contains(c) && c != '&') resultado.add(c);
        }
        for (char c : alfabeto2) {
            if (!resultado.contains(c) && c != '&') resultado.add(c);
        }
        resultado.add('&');

        //this.alfabeto = resultado;
        return resultado;

    }

    Automato base(char simbolo) {
        Automato base = new Automato(2, 1);
        base.alfabeto.add(simbolo);
        base.estados.add(0);
        base.estados.add(1);
        base.matrizDeTransicao[0][0].add(1);
        base.estadosFinais.add(1);
        return base;
    }

    Automato(char simbolo) {
        this.alfabeto = new ArrayList<Character>();
        this.estadosFinais = new ArrayList<Integer>();
        this.estados = new ArrayList<Integer>();
        this.estadoInicial = 0;
        this.matrizDeTransicao = new ArrayList[2][1];
        for(int i = 0; i < 2; i++)
            for(int j = 0; j < 1; j++)
                this.matrizDeTransicao[i][j] = new ArrayList<Integer>();
        this.alfabeto.add(simbolo);
        this.estados.add(0);
        this.estados.add(1);
        this.matrizDeTransicao[0][0].add(1);
        this.estadosFinais.add(1);
    }

    public static ArrayList<Integer> uneEstados(ArrayList<Integer> e1, ArrayList<Integer> e2) {

        ArrayList<Integer> resultado = new ArrayList<Integer>();


        for (int estado : e1) {
            resultado.add(estado);
        }
        for (int estado : e2) {
            resultado.add(estado + e1.size());
        }

        return resultado;

    }

    public static Automato concatenacao(Automato a1, Automato a2) {
        int novaQtEstados = a1.estados.size() + a2.estados.size();
        ArrayList<Character> novoAlfabeto = Automato.uneAlfabetos(a1.alfabeto, a2.alfabeto);
        int tamAlfabeto = novoAlfabeto.size();

        Automato resultado = new Automato(novaQtEstados, tamAlfabeto);
        resultado.alfabeto = novoAlfabeto;

        ArrayList<Integer> estadosA2 = a2.getEstados();
        for (int estado : a1.estados) {
            resultado.estados.add(estado);
        }
        for (int estado : a2.estados) {
            resultado.estados.add(estado + a1.estados.size());
        }

        for (char c : resultado.alfabeto) {
            int coluna = resultado.alfabeto.indexOf(c);
            int colunaA1 = a1.alfabeto.indexOf(c);
            int colunaA2 = a2.alfabeto.indexOf(c);

            if (colunaA1 != -1) {
                for (int estado : a1.estados) {
                    for (int i : a1.matrizDeTransicao[estado][colunaA1])
                        resultado.matrizDeTransicao[estado][coluna].add(i);
                }
            }

            int offsetEstados = a1.estados.size();
            if (colunaA2 != -1) {
                for (int estado : a2.estados) {
                    for (int i : a2.matrizDeTransicao[estado][colunaA2])
                        resultado.matrizDeTransicao[estado + offsetEstados][coluna].add(i + offsetEstados);
                }
            }


        }

        resultado.estadoInicial = 0;
        resultado.estadosFinais.add(novaQtEstados - 1);
        for (int i : a1.estadosFinais) {
            resultado.matrizDeTransicao[i][tamAlfabeto - 1].add(a1.estados.size());
        }


        return resultado;


    }

    public static Automato uniao(Automato a1, Automato a2) {
        int novaQtEstados = a1.estados.size() + a2.estados.size() + 2;
        ArrayList<Character> novoAlfabeto = Automato.uneAlfabetos(a1.alfabeto, a2.alfabeto);
        int tamAlfabeto = novoAlfabeto.size();

        Automato resultado = new Automato(novaQtEstados, tamAlfabeto);
        resultado.alfabeto = novoAlfabeto;

        resultado.estados.add(0);
        for (int estado : a1.estados) {
            resultado.estadosFinais.add(novaQtEstados - 1);
            resultado.estados.add(estado + 1);
        }
        for (int estado : a2.estados) {
            resultado.estados.add(estado + a1.estados.size() + 1);
        }

        for (char c : resultado.alfabeto) {
            int coluna = resultado.alfabeto.indexOf(c);
            int colunaA1 = a1.alfabeto.indexOf(c);
            int colunaA2 = a2.alfabeto.indexOf(c);
            int offsetEstados = 1;

            if (colunaA1 != -1) {
                for (int estado : a1.estados) {
                    for (int i : a1.matrizDeTransicao[estado][colunaA1])
                        resultado.matrizDeTransicao[estado + offsetEstados][coluna].add(i + offsetEstados);
                }
            }

            offsetEstados += a1.estados.size();
            if (colunaA2 != -1) {
                for (int estado : a2.estados) {
                    for (int i : a2.matrizDeTransicao[estado][colunaA2])
                        resultado.matrizDeTransicao[estado + offsetEstados][coluna].add(i + offsetEstados);
                }
            }


        }

        resultado.estadoInicial = 0;
        resultado.matrizDeTransicao[0][tamAlfabeto - 1].add(1);//Add transicao vazia pro inicio do automato1
        resultado.matrizDeTransicao[0][tamAlfabeto - 1].add(1 + a1.estados.size());//Add transicao vazia pro inicio do automato2
        resultado.estadosFinais.add(novaQtEstados - 1);

        for (int i : a1.estadosFinais) {
            resultado.matrizDeTransicao[i+1][tamAlfabeto - 1].add(novaQtEstados - 1);
        }
        for (int i : a2.estadosFinais) {
            System.out.println(i);
            resultado.matrizDeTransicao[i + a1.estados.size() + 1][tamAlfabeto - 1].add(novaQtEstados - 1);
        }
        resultado.estados.add(novaQtEstados -1);


        return resultado;


    }

    public static Automato fechoDeKleene(Automato a) {
        int novaQtEstados = a.estados.size() + 2;
        ArrayList<Character> novoAlfabeto = Automato.uneAlfabetos(a.alfabeto, a.alfabeto);
        int tamAlfabeto = novoAlfabeto.size();

        Automato resultado = new Automato(novaQtEstados, tamAlfabeto);
        resultado.alfabeto = novoAlfabeto;

        resultado.estados.add(0);
        for (int estado : a.estados) {
            resultado.estados.add(estado + 1);
        }
        resultado.estados.add(novaQtEstados - 1);

        for (char c : resultado.alfabeto) {
            int coluna = resultado.alfabeto.indexOf(c);
            int colunaA1 = a.alfabeto.indexOf(c);

            if (colunaA1 != -1) {
                for (int estado : a.estados) {
                    for (int i : a.matrizDeTransicao[estado][colunaA1])
                        resultado.matrizDeTransicao[estado + 1][coluna].add(i + 1);
                }
            }
        }

        //resultado.matrizDeTransicao[novaQtEstados-2][tamAlfabeto-1].add(novaQtEstados-1);
        for (int i : a.estadosFinais) {
            resultado.matrizDeTransicao[i + 1][tamAlfabeto - 1].add(1);
            resultado.matrizDeTransicao[i + 1][tamAlfabeto - 1].add(novaQtEstados - 1);
        }
        //resultado.matrizDeTransicao[novaQtEstados - 1][tamAlfabeto - 1].add(0);
        resultado.matrizDeTransicao[0][tamAlfabeto - 1].add(1);
        resultado.matrizDeTransicao[0][tamAlfabeto - 1].add(novaQtEstados - 1);


        return resultado;


    }

    public ArrayList<Integer> Uestado(ArrayList<Integer> e1, ArrayList<Integer> e2){

        if(e1.isEmpty()) return e2;

        if(e2.isEmpty()) return e1;

        ArrayList<Integer> resultado = new ArrayList<Integer>();
        resultado.addAll(e1);

        for(Integer i: e2){
            if(!e1.contains(i)){
                resultado.add((resultado.size()-1),i);
            }
        }
        return resultado;
    }

    //Calcula fechoE para todos os estados
    public void FechoE(){
        for(Integer i: this.estados){
            ArrayList<Integer> transicoes = this.CalcFechoE(i, new ArrayList<Integer>());
            transicoes.add(0, i);
            Collections.sort(transicoes);
            fechoE.put(i, transicoes);
        }
        System.out.println("\nFechoE do automato:\n");
        Enumeration names = fechoE.keys();
        while(names.hasMoreElements()) {
            Integer key = (Integer) names.nextElement();
            System.out.println("Q"+key + ":" + fechoE.get(key));
        }

        this.ConstrucaoSubConjuntos();
    }

    //Para dado um estado calcula o fechoE dele
    public ArrayList<Integer> CalcFechoE(Integer estado, ArrayList<Integer> passou){

        ArrayList<Integer> transicaovazia = this.funcaoDeTransicao(estado,'&');
        if(!transicaovazia.isEmpty()){
            for(Integer i:transicaovazia){
                if(!passou.contains(i)) {
                    passou.add(0,i);
                    transicaovazia = this.Uestado(transicaovazia, CalcFechoE(i, passou));
                }
            }
        }
        return transicaovazia;
    }

    public void ConstrucaoSubConjuntos(){

        ArrayList<ArrayList<Integer>> estados = new ArrayList<ArrayList<Integer>>();
        estados.add(0, this.fechoE.get(this.estadoInicial));

        ArrayList<ArrayList<ArrayList<Integer>>> mattransicoes= new ArrayList<ArrayList<ArrayList<Integer>>>();

        for(int i=0;i<estados.size();i++){
            ArrayList<ArrayList<Integer>> linha = new ArrayList<ArrayList<Integer>>();

            for(int k=0;k<this.alfabeto.size()-1;k++){
                ArrayList<Integer> auxiliar = new ArrayList<Integer>();
                ArrayList<Integer> celula = new ArrayList<Integer>();

                for(Integer j: estados.get(i)){
                    auxiliar = Uestado(auxiliar, this.funcaoDeTransicao(j,this.alfabeto.get(k)));
                }

                for(Integer aux:auxiliar)
                    celula = this.Uestado(celula, this.fechoE.get(aux));


                Collections.sort(celula);
                if(!estados.contains(celula)){
                    estados.add(estados.size(), celula);

                }

                linha.add(k,celula);

            }
            mattransicoes.add(i,linha);
        }
        this.RenomearAFD(mattransicoes, estados);
    }

    private void RenomearAFD(ArrayList<ArrayList<ArrayList<Integer>>> mat, ArrayList<ArrayList<Integer>> estados){

        HashMap<ArrayList<Integer>, Integer> mapeamentoDeEstados = new HashMap<ArrayList<Integer>, Integer>();
        Integer cont = 0;
        this.estadosAFD = new ArrayList<Integer>();
        this.estadosFinaisAFD = new ArrayList<Integer>();
        this.estadoInicialAFD = 0;

        for(ArrayList<Integer> i:estados){
            for(Integer p:this.estadosFinais){
                if(i.contains(p)){
                    this.estadosFinaisAFD.add(this.estadosFinaisAFD.size(), cont);
                }
            }
            this.estadosAFD.add(this.estadosAFD.size(),cont);
            mapeamentoDeEstados.put(i,cont);
            cont++;
        }
        this.matrizDeTransicaoAFD = new Integer[estados.size()][this.alfabeto.size()-1];
        this.setAlfabetoAFD();

        for(int i = 0;i<mat.size();i++){
            for(int j = 0; j < mat.get(i).size();j++){
                this.matrizDeTransicaoAFD[i][j] = mapeamentoDeEstados.get(mat.get(i).get(j));
            }
        }

        System.out.println("\n\nAFD Não Minimizado:");
        System.out.print("       ");
        for (int k = 0;k<this.alfabeto.size()-1;k++)
            System.out.print(this.alfabeto.get(k)+"  ");

        System.out.println("");

        for(int i = 0; i<this.matrizDeTransicaoAFD.length;i++){
            if(this.estadosAFD.get(i) == this.estadoInicialAFD)
                System.out.print("->Q"+this.estadosAFD.get(i)+"| ");
            else if (this.estadosFinaisAFD.contains(this.estadosAFD.get(i)))
                System.out.print(" *Q"+this.estadosAFD.get(i)+"| ");
            else
                System.out.print("  Q"+this.estadosAFD.get(i)+"| ");

            for (int j = 0; j<this.matrizDeTransicaoAFD[i].length;j++){
                System.out.print("Q"+this.matrizDeTransicaoAFD[i][j]+" ");
            }
            System.out.println("");
        }
        minimizaAFD();
    }

    private void minimizaAFD(){

        Integer[][] tabela = new Integer[this.estadosAFD.size()][this.estadosAFD.size()];
        for(int i = 0; i<this.estadosAFD.size();i++){
            for(int j = 0; j<this.estadosAFD.size();j++){
                if(j<i){
                    if((this.ehEstadoFinalAFD(this.estadosAFD.get(i)) && !this.ehEstadoFinalAFD(this.estadosAFD.get(j))) ||
                        (!this.ehEstadoFinalAFD(this.estadosAFD.get(i)) && this.ehEstadoFinalAFD(this.estadosAFD.get(j))))

                        tabela[i][j] = 1;
                    else
                        tabela[i][j] = 0;
                }
            }
        }

        int contMarcacao = 1;
        int contaux = 1;
        while(contMarcacao!=0){

            contMarcacao = 0;
            contaux++;

            for (int i = 0; i < this.estadosAFD.size(); i++){
                for (int j = 0; j < this.estadosAFD.size(); j++){
                    if (j < i) {
                        if (tabela[i][j] == 0) {
                            for(Character c:this.alfabetoAFD){
                                Integer transicaoafd1 = this.funcaoDeTransicaoAFD(this.estadosAFD.get(i), c);
                                Integer transicaoafd2 = this.funcaoDeTransicaoAFD(this.estadosAFD.get(j), c);

                                if((this.ehEstadoFinalAFD(transicaoafd1) && !this.ehEstadoFinalAFD(transicaoafd2)) ||
                                    (!this.ehEstadoFinalAFD(transicaoafd1) && this.ehEstadoFinalAFD(transicaoafd2))){
                                    tabela[i][j] = contaux;
                                    contMarcacao++;
                                    break;
                                }
                                else{
                                    if(transicaoafd1!=transicaoafd2){
                                        if(tabela[this.estadosAFD.indexOf(transicaoafd1)][this.estadosAFD.lastIndexOf(transicaoafd2)]!=null) {
                                            if (tabela[this.estadosAFD.indexOf(transicaoafd1)][this.estadosAFD.lastIndexOf(transicaoafd2)] != 0) {
                                                tabela[i][j] = contaux;
                                                contMarcacao++;
                                                break;
                                            }
                                        }
                                        else{
                                            if (tabela[this.estadosAFD.indexOf(transicaoafd2)][this.estadosAFD.lastIndexOf(transicaoafd1)] != 0) {
                                                tabela[i][j] = contaux;
                                                contMarcacao++;
                                                break;
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                    else
                        break;
                }
            }
        }

        ArrayList<ArrayList<Integer>> novosEstados = new ArrayList<ArrayList<Integer>>();

        for(int i = 0; i<this.estadosAFD.size();i++){

            ArrayList<Integer> estadoeq = new ArrayList<Integer>();
            estadoeq.add(this.estadosAFD.get(i));

            for(int j = i+1; j<this.estadosAFD.size();j++){
                if(tabela[i][j] != null){
                    if(tabela[i][j]==0){
                        estadoeq.add(this.estadosAFD.get(j));
                    }
                }
                else{
                    if(tabela[j][i]==0){
                        estadoeq.add(this.estadosAFD.get(j));
                    }
                }
            }
            Collections.sort(estadoeq);
            if(!novosEstados.contains(estadoeq)) {
                boolean aux = false;
                for (ArrayList<Integer> e : novosEstados) {
                    if(e.size()>estadoeq.size()) {
                        for (Integer t : estadoeq) {
                            if (e.contains(t)) {
                                aux = true;
                            }
                        }
                    }
                }
                if(aux == false){
                    novosEstados.add(estadoeq);
                }
            }
        }
        ArrayList<Integer> novasTransicoes[][] = new ArrayList[novosEstados.size()][this.alfabetoAFD.size()];
        for(int i = 0; i <novosEstados.size();i++){
            for(int j = 0;j<this.alfabetoAFD.size();j++){
                novasTransicoes[i][j] = new ArrayList<Integer>();
                for(Integer k:novosEstados.get(i)){
                    int t = funcaoDeTransicaoAFD(k,this.alfabeto.get(j));

                    for(ArrayList<Integer> l: novosEstados)
                        if(l.contains(t))
                            novasTransicoes[i][j] = l;
                }
            }
        }

        HashMap<ArrayList<Integer>, Integer> mapeamentoDeEstados = new HashMap<ArrayList<Integer>, Integer>();
        Integer cont = 0;
        this.estadosAFD.clear();
        this.EstadosFinaisAFDMinimo = new ArrayList<Integer>();
        this.estadoInicialAFD = 0;

        for(ArrayList<Integer> i: novosEstados){
            for(Integer p:this.estadosFinaisAFD){
                if(i.contains(p)){
                    this.EstadosFinaisAFDMinimo.add(cont);
                }
            }
            this.estadosAFD.add(this.estadosAFD.size(),cont);
            mapeamentoDeEstados.put(i,cont);
            cont++;
        }
        this.matrizTransicaoAFDMinimo = new Integer[novosEstados.size()][this.alfabetoAFD.size()];

        for(int i = 0;i<novasTransicoes.length;i++){
            for(int j = 0; j < novasTransicoes[i].length;j++){
                this.matrizTransicaoAFDMinimo[i][j] = mapeamentoDeEstados.get(novasTransicoes[i][j]);
            }
        }

        System.out.println("\n\nAFD Minimo:");
        System.out.print("       ");
        for (int k = 0;k<this.alfabetoAFD.size();k++)
            System.out.print(this.alfabetoAFD.get(k)+"  ");

        System.out.println("");

        for(int i = 0; i<this.matrizTransicaoAFDMinimo.length;i++){
            if(this.estadosAFD.get(i) == this.estadoInicialAFD)
                System.out.print("->Q"+this.estadosAFD.get(i)+"| ");
            else if (this.estadosFinaisAFD.contains(this.estadosAFD.get(i)))
                System.out.print(" *Q"+this.estadosAFD.get(i)+"| ");
            else
                System.out.print("  Q"+this.estadosAFD.get(i)+"| ");

            for (int j = 0; j<this.matrizTransicaoAFDMinimo[i].length;j++){
                System.out.print("Q"+this.matrizTransicaoAFDMinimo[i][j]+" ");
            }
            System.out.println("");
        }

        /*System.out.println("Novas Transicoes");
        for(int i = 0; i <novosEstados.size();i++) {
            for (int j = 0; j < this.alfabetoAFD.size(); j++) {
                System.out.print(this.matrizTransicaoAFDMinimo[i][j]);
            }
            System.out.println("");
        }

        for (int i = 0; i<tabela.length;i++){
            for(int j=0;j<tabela[i].length;j++)
                if(tabela[i][j]!=null) System.out.print(tabela[i][j]);;
            System.out.println("");
        }*/
    }
    /*TODO public int[] funcaoDeTransicaoEstendida(int q, char w[])
    {


    }*/


}
