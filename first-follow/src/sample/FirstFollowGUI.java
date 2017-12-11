package sample;

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.*;

public class FirstFollowGUI {
    private JTextField FilePathTextField;
    public JPanel panelMain;
    private JButton setDataButton;
    private JTextField v;
    private JTextField t;
    private JTextArea p;
    private JTextField s;
    private JButton first_FollowButton;
    private JTextArea result;

    void print(String s){
        System.out.println(s);
    }
    void print(Character s){
        System.out.println(s);
    }

    public FirstFollowGUI(){

        setDataButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                Scanner in = null;
                try {
                    in = new Scanner(new FileReader(FilePathTextField.getText()));
                } catch (FileNotFoundException e1) {
                    e1.printStackTrace();
                }
                v.setText(in.nextLine());
                t.setText(in.nextLine());
                String aux = null;
                p.setText("");
                while (in.hasNextLine()){
                    aux = in.nextLine();
                    if(!in.hasNextLine()){
                        break;
                    }
                    p.setText(p.getText()+aux+"\n");
                }
                s.setText(aux);

            }
        });

        first_FollowButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                if (v.getText().length() != 0 && t.getText().length() != 0 && p.getText().length() != 0 && s.getText().length() != 0) {

                    ArrayList<String> V = new ArrayList<String>();
                    ArrayList<String> T = new ArrayList<String>();
                    HashMap<String, ArrayList<ArrayList<String>>> P = new HashMap<String, ArrayList<ArrayList<String>>>();
                    String S;

                    String aux = v.getText().replaceAll(" ", "");
                    String aux2 = new String("");
                    for(int i = 0; i < aux.length();i++){
                        if(aux.charAt(i)!=',') {
                            aux2 = aux2 + aux.charAt(i);
                            if(i+1==aux.length())
                                V.add(V.size(),aux2);
                        }
                        else{
                            V.add(V.size(),aux2);
                            aux2 = new String("");
                        }
                    }

                    aux = t.getText().replaceAll(" ", "");
                    aux2 = new String("");
                    for(int i = 0; i < aux.length();i++){
                        if(aux.charAt(i)!=',') {
                            aux2 = aux2 + aux.charAt(i);
                            if(i+1==aux.length())
                                T.add(T.size(),aux2);
                        }
                        else{
                            T.add(T.size(),aux2);
                            aux2 = new String("");
                        }
                    }

                    aux = p.getText().replaceAll(" ", "");
                    String rules = new String("");

                    for(int i = 0; i < aux.length();i++){
                        //First part of the rule
                        ArrayList<ArrayList<String>> L = new ArrayList<ArrayList<String>>();
                        aux2 = new String("");

                        for(i = i; i < aux.length();i++){
                            if (aux.charAt(i) == '-' && aux.charAt(i + 1) == '>') {
                                i += 2;
                                break;
                            }
                            aux2 = aux2 + aux.charAt(i);
                        }

                        //Second part get all gramar
                        for(i = i; i < aux.length();i++){
                            ArrayList<String> elements = new ArrayList<String>();

                            for(i = i; i < aux.length();i++){
                                if(aux.charAt(i)=='|' || aux.charAt(i)=='\n'){
                                    break;
                                }
                                rules = rules + aux.charAt(i);

                                if(V.contains(rules) || T.contains(rules)) {
                                    elements.add(rules);
                                    rules = new String("");
                                }

                            }
                            L.add(elements);

                            if(i>=aux.length() || aux.charAt(i)=='\n'){
                                break;
                            }

                        }
                        if(V.contains(aux2))
                            P.put(aux2, L);
                        else
                            print("Something wrong");
                    }

                    for (String s:P.keySet()) {
                        for (List<String> o : P.get(s)) {
                            print("Key: " + s + " Value: " + o);
                        }
                    }

                    S = s.getText();

                    LL1_Gramar gramar = new LL1_Gramar(V,T,P,S);
                    HashMap<String, ArrayList<String>> f = gramar.getFirst();
                    String r = "FIRST:\n";
                    for (String str:f.keySet()) {
                        r = r+str+":";
                        String au = "{";
                        for (String va:f.get(str)){
                            au = au+va+",";
                        }
                        au = au.substring(0, au.length() - 1);
                        au = au+"}";
                        r = r+au+"\n";
                    }
                    HashMap<String, ArrayList<String>> fo = gramar.getFollow();
                    r = r+"============\nFOLLOW:\n";
                    for (String str:fo.keySet()) {
                        r = r+str+":";
                        String au = "{";
                        for (String va:fo.get(str)){
                            au = au+va+",";
                        }
                        au = au.substring(0, au.length() - 1);
                        au = au+"}";
                        r = r+au+"\n";
                    }

                    result.setText(r);

                }
            }
        });
    }



    private void createUIComponents() {
        // TODO: place custom component creation code here
    }
}
