package application;

import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;

public class SudokuhelferController{

    @FXML
    private GridPane sFelder; // Layout worauf die Zahl angezeigt werden

    private SudokuhelferModel sm;

    public void initialize() {
        // hier  wird ein Feld 9x9 erzueugt und
        //mit zufälig generierten Zahlen befüllt
        sm = new SudokuhelferModel();
        sm.moeglicheZahlenSammeln();
    }

    //Wenn eine Zahl vom Spieler eintragen wird
    public boolean zahlEintragen(int vorschlag,int zeile, int spalte){

       boolean result =  sm.vorschlagEintragen(vorschlag, zeile, spalte);

       if(result){
           sm.felderAktualisieren();
           spielAusgeben();

           Node n = new TextField();

           for(Node nd : sFelder.getChildren()){

               if(GridPane.getColumnIndex(nd) == spalte && GridPane.getRowIndex(nd) == zeile){
                   n = nd;
                   n.setStyle("-fx-font-size: 16;" +
                           "-fx-text-fill: green;" +
                           "-fx-font-weight: bold;" +
                           "-fx-background-color: white;" +
                           "-fx-border-color: black;" +
                           "-fx-border-width: 1;");
               }
           }

       }

        return result;
    }

    //Wenn eine vom Spieler eingetragene Zahl entfernt ist
    public void zahlenEntfernen(int vorschlag, int zeile,int spalte){

        sm.vorschlagEntfernen(vorschlag, zeile, spalte);
        sm.felderAktualisieren();
        spielAusgeben();
    }

    //
    public void behandleTextField(TextField tf){

        int zahl = 0;
        TextField neuTf = new TextField();
        neuTf.setAlignment(Pos.CENTER);
        neuTf.setPrefHeight(40);
        neuTf.setPrefWidth(73);
        if(tf.getText().isEmpty()){

            zahlenEntfernen(0, GridPane.getRowIndex(tf), GridPane.getColumnIndex(tf));
            /*int row = GridPane.getRowIndex(tf);
            int col = GridPane.getColumnIndex(tf);
            neuTf.setText(sm.getMoeglicheZahlen()[row][col]);
            neuTf.setStyle("-fx-font-size: 13;" +
                    "-fx-text-fill: grey;" +
                    "-fx-font-weight: bold;" +
                    "-fx-background-color: white;" +
                    "-fx-border-color: black;" +
                    "-fx-border-width: 1;");
            sFelder.getChildren().remove(tf);
            sFelder.add(neuTf, col, row);*/
        }else{
            zahl = Integer.parseInt(tf.getText());
            //if(zahl < 1 || zahl > 9){
                int row = GridPane.getRowIndex(tf);
                int col = GridPane.getColumnIndex(tf);
                neuTf.setText(sm.getMoeglicheZahlen()[row][col]);
                neuTf.setStyle("-fx-font-size: 13;" +
                        "-fx-text-fill: grey;" +
                        "-fx-font-weight: bold;" +
                        "-fx-background-color: white;" +
                        "-fx-border-color: black;" +
                        "-fx-border-width: 1;");
                sFelder.getChildren().remove(tf);
                sFelder.add(neuTf, col, row);
            //}
        }
    }


    public void neuesSpielAnzeigen(){
        initialize(); //neues Sudokuspiel  mit möglichen Zahlen generiert
        spielAusgeben(); //Alle Daten zum Susokuspiel werden auf die View abgebildet
    }

    public void spielAusgeben(){

        int[][] felder = sm.getFelder();
        String [][] moeglicheZahlen= sm.getMoeglicheZahlen();

        for(int i = 0; i < 9; i++){
            for(int j = 0; j < 9; j++){

                Label l = new Label();
                TextField t = new TextField();

                if(felder[i][j] == 0){
                    t.setText(moeglicheZahlen[i][j]);
                    t.setAlignment(Pos.CENTER);
                    t.setPrefHeight(40);
                    t.setPrefWidth(73);
                    t.setStyle("-fx-font-size: 13;" +
                            "-fx-text-fill: grey;" +
                            "-fx-font-weight: bold;" +
                            "-fx-background-color: white;" +
                            "-fx-border-color: black;" +
                            "-fx-border-width: 1;");

                    //Nach Drücken der Taste 'Enter', den Eintrag grün färben
                    int zeile = i;
                    int spalte = j;
                    t.setOnKeyPressed((final KeyEvent keyEvent) -> {
                        if ( keyEvent.getCode() == KeyCode.ENTER && !t.getText().isEmpty() ) {
                            int v = Integer.parseInt(t.getText()); //Wandle den Text in Ziffer

                            //Falls die eingetragene Zahl erlaubt ist
                            if(!zahlEintragen(v, zeile, spalte)){
                                behandleTextField(t);
                            }

                        }else if( keyEvent.getCode() == KeyCode.ENTER && t.getText().isEmpty() ){
                            behandleTextField(t);
                        }
                    });

                    sFelder.add(t, j, i);
                    sFelder.setAlignment(Pos.CENTER);
                }else{
                    //Falls die Zahl vom Spieler eintragen wurde
                    if(sm.getVorschlaege()[i][j]){
                        t.setText(moeglicheZahlen[i][j]);
                        t.setAlignment(Pos.CENTER);
                        t.setPrefHeight(40);
                        t.setPrefWidth(73);
                        t.setStyle("-fx-font-size: 13;" +
                                "-fx-text-fill: grey;" +
                                "-fx-font-weight: bold;" +
                                "-fx-background-color: white;" +
                                "-fx-border-color: black;" +
                                "-fx-border-width: 1;");
                    }else{

                        l.setText(String.valueOf(felder[i][j]));
                        l.setTextFill(Color.rgb(0, 0, 0));
                        l.setAlignment(Pos.CENTER);
                        l.setPrefHeight(40);
                        l.setPrefWidth(73);
                        l.setStyle("-fx-font-size: 20;" +
                                "-fx-font-weight: bold;" +
                                "-fx-background-color: white;" +
                                "-fx-border-color: black;" +
                                "-fx-border-width: 1;");
                        sFelder.add(l, j, i);
                        sFelder.setAlignment(Pos.CENTER);

                    }

                }



            }
        }
    }

}
