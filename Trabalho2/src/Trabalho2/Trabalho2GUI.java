package Trabalho2;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.StringJoiner;


public class Trabalho2GUI {
    public JPanel panelMain;
    private JTable tabela;
    private JTextField textFieldEntrada;
    private JTextField textFieldSaida;
    private JTable table1;
    private JScrollPane scrollPane;

    public Trabalho2GUI() {
        Object [][] dados = {
                {"Ana Monteiro", "48 9923-7898", "ana.monteiro@gmail.com"},
                {"João da Silva", "48 8890-3345", "joaosilva@hotmail.com"},
                {"Pedro Cascaes", "48 9870-5634", "pedrinho@gmail.com"}
        };

        String [] colunas = {"Nome", "Telefone", "Email"};

        textFieldEntrada.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                super.keyPressed(e);
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    RegularExpression regex = new RegularExpression(textFieldEntrada.getText());
                    textFieldSaida.setText(regex.getPosfixa());
                    Automato automato = regex.getAutomato();
                    ArrayList<Integer> matriz[][] = automato.getMatrizDeTransicao();
                    ArrayList<Character> alfabeto = automato.getAlfabeto();
                    ArrayList<Integer> estados = automato.getEstados();

                    ArrayList<String> cabecalhoJTable = new ArrayList<String>();
                    cabecalhoJTable.add("");
                    for(char c : alfabeto)
                    {
                        cabecalhoJTable.add(c+"");
                    }
                    String[][] dados = new String[estados.size()][alfabeto.size()+1];
                    for(int i : estados)
                    {

                        if(automato.getEstadosFinais().contains(i)) dados[i][0] = "*q"+i;
                        else if(automato.getEstadoInicial() == i) dados[i][0] = "->q"+i;
                        else dados[i][0] = "q"+i;
                        for(int j = 0; j < alfabeto.size(); j++)
                        {
                            if(dados[i][j+1] == null) dados[i][j+1] = "{";
                            for(int k : matriz[i][j])
                            {

                                dados[i][j+1] += "q"+ k + ",";
                            }
                            if(dados[i][j+1].lastIndexOf(',') > 0) dados[i][j+1] = dados[i][j+1].substring(0,dados[i][j+1].length()-1);

                            dados[i][j+1] += "}";

                        }
                    }

                    //Try Table Model
                    DefaultTableModel dtm = new DefaultTableModel(estados.size(),alfabeto.size()+1);
                    //dtm.setColumnIdentifiers(cabecalhoJTable.toArray());
                    dtm.setDataVector(dados,cabecalhoJTable.toArray());
                    table1.setModel(dtm);


                    //tabela = new JTable(dados, cabecalhoJTable.toArray());


                    //scrollPane.setViewportView(tabela);
                    //scrollPane.repaint();
                    automato.FechoE();

                }
            }
        });
    }
}
