package application;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

//Klasse
public class SudokuhelferModel {


    private int [][] felder; //2D Array, sie speichert alle Zahlen zum Sudokuspiel
    private  boolean [][] vorschlaege; //um sich die Vorschläge des Spielers zu merken
    private String [][] moeglicheZahlen;
    private HashSet<Integer>[][] mz; //mögliche Zahlen als Ziffer darsgestellt in einer Liste
    private Set<Integer> ziffer; //enthält Ziffer von 1 bis 9
    private List< List<Integer> > eingetrageneZahlenZeilen;
    private List< List<Integer> > eingetrageneZahlenSpalten;
    private List< List<Integer> > eingetrageneZahlenBlock;



    //Konstruktor
    public SudokuhelferModel() {

        //Initialisiereung aller Attribute
        int [] z = {1,2,3,4,5,6,7,8,9};
        ziffer = new HashSet<Integer>();

        for(int i : z) {
            ziffer.add(i);
        }

        this.felder = new int[9][9];
        this.vorschlaege = new boolean[9][9];
        this.mz = new HashSet[9][9];
        this.moeglicheZahlen = new String[9][9];

        for(int i = 0; i < 9; i++) {

            for(int j = 0; j < 9; j++) {

                moeglicheZahlen[i][j] = "";
                mz[i][j] = new HashSet<Integer>();
            }

        }

        this.felderBefuellen();
        this.sudokuspielGenerieren();

        this.eingetrageneZahlenZeilen = this.eingetrageneZahlenZeilenSammeln();
        this.eingetrageneZahlenSpalten = this.eingetrageneZahlenSpaltenSammeln();
        this.eingetrageneZahlenBlock = this.eingetrageneZahlenBlockSammeln();

    }

    //Alle Felder des Sudokuspiel werden mit Ziffern zwischen 1 und 9 befüllt
    public void felderBefuellen(){

        int [][] feld = { {5, 1, 4, 8, 6, 9, 7, 2, 3},
                            {8, 7, 2, 3, 4, 5, 6, 1, 9},
                            {9, 6, 3, 2, 1, 7, 5, 4, 8},
                            {6, 2, 8, 1, 3, 4, 9, 5, 7},
                            {1, 9, 7, 6, 5, 2, 8, 3, 4},
                             {4, 3, 5, 7, 9, 8, 1, 6, 2},
                            {2, 4, 6, 9, 7, 1, 3, 8, 5},
                            {7, 5, 1, 4, 8, 3, 2, 9, 6},
                            {3, 8, 9, 5, 2, 6, 4, 7, 1} };

        //Copiere alle Zahlen aus dem Array "feld" ins Array "Felder"
        this.felder = feld.clone();
    }


    public void sudokuspielGenerieren() {

        //zufällig felder im Sudokuspiel befreien
        int[] anzFreiFelder = this.anzahlFreierFeldernGenerieren(new int [9]);
        //Bestimmen, welche Felden zu befreien sind (Zeileindex, Spalteindex)
        int [][] spaltenIndex = this.indizesZeileSpalteGenerieren(anzFreiFelder);

        int sp = 0;

        for(int zeile = 0; zeile < 9; zeile++) {

            for(int spalte2 = 0; spalte2 < spaltenIndex[zeile].length; spalte2++) {

                if(spaltenIndex[zeile][spalte2] > 0) {
                    //Index der entsprechenden Spalte aufnehmen
                    sp = spaltenIndex[zeile][spalte2];
                    this.felder[zeile][sp-1] = 0; //das entsprechende Feld befreien (mit 0 besetzen)

                }else {

                    break;
                }

            }
        }
        //zufällig felder im Sudokuspiel befreien end
    }


    /**
     * Die Methode gibt alle Zahlen des Sudokuspiel aus
     */
    public void sudokuspielAusgeben() {

        //Ausgabe
        for(int zeile = 0; zeile < 9; zeile++) {

            for(int spalte = 0; spalte < 9; spalte++) {

                System.out.print(this.felder[zeile][spalte] + " ");
            }

            System.out.println();

        }
    }

    public void ausgabe(String arr[][]) {

        //Ausgabe
        for(int zeile = 0; zeile < 9; zeile++) {

            for(int spalte = 0; spalte < 9; spalte++) {

                if(arr[zeile][spalte] == "") {
                    System.out.print(" - ");
                }else {
                    System.out.print(arr[zeile][spalte] + "-");
                }

            }

            System.out.println();

        }
    }

    public void ausgabe(List< List<Integer> > liste) {

        //Ausgabe
        for(List<Integer> l : liste) {

            for(int i : l) {

                System.out.print(i + " ");
            }

            System.out.println();
        }
    }

    /**
     * Die Methode generiert eine zufällige Anzahl(3 - 7) an Feldern, die pro Zeile im Sudoku befreit werden sollen
     * @param anzahlFeldernZuBefreien das zu befreiende Array, mit der Anzahl an Feldern
     * @return das Array mit allen Anzahlen an zu befreienden Feldern
     */
    private int [] anzahlFreierFeldernGenerieren(int [] anzahlFeldernZuBefreien){


        for(int zeile = 0; zeile < 9; zeile++){

            //Generieren einer Anzahl an Feldern zu befreien(zwischen 3 und 7)
            anzahlFeldernZuBefreien[zeile] = (int)Math.round( Math.random() * (7 - 3) ) + 3;

        }

        return anzahlFeldernZuBefreien;

    }

    /**
     * Die Methode gibt ein Array mit den Indizes Paaren (Zeileindex-Spalteindex) (zwischen 0 und 8)
     * für die zu befreienden Felder im Sudokuspiel
     * @return ein Array mit allen Indizes
     */
    private int [][] indizesZeileSpalteGenerieren(int [] anzahlFelderZuBefreien){

        int indexSpalte = 0;
        int spaltenIndex[][] = new int[9][9];
        Set<Integer> indizesSpalten = new HashSet<Integer>();
        Iterator<Integer> it;
        int spalte = 0;

        for(int zeile = 0; zeile < 9; zeile++) {

            //Generiere die Indizes zu Spalten (verschieden voneinander)
            while(indizesSpalten.size() != anzahlFelderZuBefreien[zeile]) {

                //Generieren der Indizes für die Spalten
                indexSpalte = (int)Math.round( Math.random() * (8 - 0) ) + 0;

                indizesSpalten.add(indexSpalte);

            }

            //Speichern der Indizes
            it = indizesSpalten.iterator();

            while( it.hasNext() && spalte < indizesSpalten.size() ) {

                spaltenIndex[zeile][spalte] = (int)it.next();
                spalte++;
            }

            spalte = 0; // für die nächste Runde in der nächsten Zeile
            indizesSpalten = new HashSet<Integer>();

        }

        return spaltenIndex;
    }

    /**
     * Die Methode aktualsiert die Felder mit möglichen Zahlen
     * nach Eintragen oder Entfernen einer Zahl
     */
    public void moeglicheZahlenAktualisieren() {

        //Das String Feld erstmal beleeren sonst alte Werte werden mitgespeichert
        this.moeglicheZahlen = new String[9][9];

        for(int i = 0; i < 9; i++) {

            for(int j = 0; j < 9; j++) {

                moeglicheZahlen[i][j] = "";
                //mz[i][j] = new HashSet<Integer>();
            }

        }

        for(int i = 0; i < 9; i++) {

            for(int j = 0; j < 9; j++) {

                for(Integer hs : this.mz[i][j]) {

                    this.moeglicheZahlen[i][j] = this.moeglicheZahlen[i][j]  + hs; //System.out.print(hs);
                }
            }
        }
    }


    /**
     * Die Methode durchlauft jede Zeile des Sudokus
     * und speichert alle möglichen Zahlen pro Spaltz
     * als Zeichenkette
     * @return gibt eine Liste aller Listen mit den eingetragenen Zahlen pro Zeile
     */
    public void moeglicheZahlenSammeln(){

        //String[][] mz = new String[9][9];
        Set<Integer> zeilenSpaltenBlockZahlen = new HashSet<Integer>();
        Set<Integer> moeglicheZahlen = new HashSet<Integer>();

        for(int zeile = 0; zeile < 9; zeile++) {

            for(int spalte = 0; spalte < 9; spalte++) {

                //Für leere Felder
                if(this.felder[zeile][spalte] == 0) {

                    //eingetragene Zahlen in Zeile, Spalte und Block zusammen speichern
                    zeilenSpaltenBlockZahlen = eingetrageneZahlenZeilenSpaltenBlockSammeln(zeile, spalte);

                    //mögliche Zahlen pro Zele in jeder Spalte bestimmen
                    moeglicheZahlen = this.ziffer;

                    for(int z : zeilenSpaltenBlockZahlen) {

                        moeglicheZahlen.remove(z);
                    }

                    for(int z : moeglicheZahlen) {

                        this.mz[zeile][spalte].add(z);
                        this.moeglicheZahlen[zeile][spalte] = this.moeglicheZahlen[zeile][spalte] + String.valueOf(z);
                    }

                    //Die Zahlen 1 bis 9 wieder befüllen
                    for(int i = 1; i <= 9; i++){
                        ziffer.add(i);
                    }
                }

            }
        }

    }

    /**
     * Die Methode sammeln alle eingetragenen Zahlen in
     * der entsprechenden Zeile , Spalte und im Block
     * @return eine Liste aller eingetragene Zahlen
     */
    public Set<Integer> eingetrageneZahlenZeilenSpaltenBlockSammeln(int zeile, int spalte){

        //List<Integer> eingetrageneZahlen = new ArrayList<Integer>();
        Set<Integer> zeilenSpaltenBlockZahlen = new HashSet<Integer>();
        int block = -1;

        for(int z : this.eingetrageneZahlenZeilen.get(zeile)) {

            zeilenSpaltenBlockZahlen.add(z);
        }

        for(int z : this.eingetrageneZahlenSpalten.get(spalte)) {

            zeilenSpaltenBlockZahlen.add(z);
        }

        //Auswahl des entsprechenden Blocks
        if(zeile >= 0 && zeile <= 2) {

            if(spalte >= 0 && spalte <= 2) {
                block = 0;
            }else if(spalte >= 3 && spalte <= 5) {
                block = 1;
            }else if(spalte >= 6 && spalte <= 8) {
                block = 2;
            }

        }else if(zeile >= 3 && zeile <= 5){

            if(spalte >= 0 && spalte <= 2) {
                block = 3;
            }else if(spalte >= 3 && spalte <= 5) {
                block = 4;
            }else if(spalte >= 6 && spalte <= 8) {
                block = 5;
            }

        }else if(zeile >= 6 && zeile <= 8) {

            if(spalte >= 0 && spalte <= 2) {
                block = 6;
            }else if(spalte >= 3 && spalte <= 5) {
                block = 7;
            }else if(spalte >= 6 && spalte <= 8) {
                block = 8;
            }
        }

        for(int z : this.eingetrageneZahlenBlock.get(block)) {

            zeilenSpaltenBlockZahlen.add(z);
        }

        return zeilenSpaltenBlockZahlen;
    }


    /**
     * Die Methode durchlauft jede Zeile des Sudokus
     * und speichert alle eingetragenen Zahlen pro Zeile
     * in einer entsprechenden Liste
     * @return gibt eine Liste aller Listen mit den eingetragenen Zahlen pro Zeile
     */
    public List< List<Integer> > eingetrageneZahlenZeilenSammeln(){

        List< List<Integer> > ezz = new ArrayList< List<Integer> >();
        List<Integer> temp = new ArrayList<Integer>();

        for(int zeile = 0; zeile < 9; zeile++) {

            for(int spalte = 0; spalte < 9; spalte++) {

                if(this.felder[zeile][spalte] != 0) {
                    temp.add( this.felder[zeile][spalte] );
                }

            }

            ezz.add(temp);
            temp = new ArrayList<Integer>();
        }


        return ezz;
    }

    /**
     * Die Methode durchlauft jede Spalte des Sudokus
     * und speichert alle eingetragenen Zahlen pro Spalte
     * in einer entsprechenden Liste
     * @return gibt eine Liste aller Listen mit den eingetragenen Zahlen pro Spalte
     */
    public List< List<Integer> > eingetrageneZahlenSpaltenSammeln(){

        List< List<Integer> > ezs = new ArrayList< List<Integer> >();
        List<Integer> temp = new ArrayList<Integer>();

        for(int spalte = 0; spalte < 9; spalte++) {

            for(int zeile = 0; zeile < 9; zeile++) {

                if(this.felder[zeile][spalte] != 0) {
                    temp.add( this.felder[zeile][spalte] );
                }

            }

            ezs.add(temp);
            temp = new ArrayList<Integer>();
        }


        return ezs;
    }

    /**
     * Die Methode durchlauft jeden Block(3x3) des Sudokus
     * und speichert alle eingetragenen Zahlen pro Block
     * in einer entsprechenden Liste
     * @return gibt eine Liste aller Listen mit den eingetragenen Zahlen pro Block
     */
    public List< List<Integer> > eingetrageneZahlenBlockSammeln(){

        List< List<Integer> > ezb = new ArrayList< List<Integer> >();
        List<Integer> temp = new ArrayList<Integer>();

        for(int zeile = 0; zeile < 3; zeile++) {

            for(int spalte = 0; spalte < 3; spalte++) {

                if(this.felder[zeile][spalte] != 0) {
                    temp.add( this.felder[zeile][spalte] );
                }
            }
        }

        ezb.add(temp);
        temp = new ArrayList<Integer>();

        for(int zeile = 0; zeile < 3; zeile++) {

            for(int spalte = 3; spalte < 6; spalte++) {

                if(this.felder[zeile][spalte] != 0) {
                    temp.add( this.felder[zeile][spalte] );
                }
            }
        }

        ezb.add(temp);
        temp = new ArrayList<Integer>();

        for(int zeile = 0; zeile < 3; zeile++) {

            for(int spalte = 6; spalte < 9; spalte++) {

                if(this.felder[zeile][spalte] != 0) {
                    temp.add( this.felder[zeile][spalte] );
                }
            }
        }

        ezb.add(temp);
        temp = new ArrayList<Integer>();


        for(int zeile = 3; zeile < 6; zeile++) {

            for(int spalte = 0; spalte < 3; spalte++) {

                if(this.felder[zeile][spalte] != 0) {
                    temp.add( this.felder[zeile][spalte] );
                }
            }
        }

        ezb.add(temp);
        temp = new ArrayList<Integer>();

        for(int zeile = 3; zeile < 6; zeile++) {

            for(int spalte = 3; spalte < 6; spalte++) {

                if(this.felder[zeile][spalte] != 0) {
                    temp.add( this.felder[zeile][spalte] );
                }
            }
        }

        ezb.add(temp);
        temp = new ArrayList<Integer>();

        for(int zeile = 3; zeile < 6; zeile++) {

            for(int spalte = 6; spalte < 9; spalte++) {

                if(this.felder[zeile][spalte] != 0) {
                    temp.add( this.felder[zeile][spalte] );
                }
            }
        }

        ezb.add(temp);
        temp = new ArrayList<Integer>();

        for(int zeile = 6; zeile < 9; zeile++) {

            for(int spalte = 0; spalte < 3; spalte++) {

                if(this.felder[zeile][spalte] != 0) {
                    temp.add( this.felder[zeile][spalte] );
                }
            }
        }

        ezb.add(temp);
        temp = new ArrayList<Integer>();

        for(int zeile = 6; zeile < 9; zeile++) {

            for(int spalte = 3; spalte < 6; spalte++) {

                if(this.felder[zeile][spalte] != 0) {
                    temp.add( this.felder[zeile][spalte] );
                }
            }
        }

        ezb.add(temp);
        temp = new ArrayList<Integer>();

        for(int zeile = 6; zeile < 9; zeile++) {

            for(int spalte = 6; spalte < 9; spalte++) {

                if(this.felder[zeile][spalte] != 0) {
                    temp.add( this.felder[zeile][spalte] );
                }
            }
        }

        ezb.add(temp);
        temp = new ArrayList<Integer>();


        return ezb;
    }

    /**
     * Die Methode speichert die vom Spieler vorgeschlagene Zahl
     * in entsprechenden Feld mit gegebener Zeile und Spalte
     * @param vorschlag
     * @param zeile
     * @param spalte
     * @return gibt true zurück, wenn eine Zahl im Parameter 'vorschlag' eingetragen wurde und false sonst
     */
    public boolean vorschlagEintragen(int vorschlag, int zeile, int spalte) {

        //prüfe estmal ob die Zahl erlaubt ist (unter den angezeiten möglichen Zahlen)
        //und zwichen 1 und 9
        if (this.mz[zeile][spalte].contains(vorschlag)) {

            //System.out.println("Zahl ist erlaubt!!");
            this.felder[zeile][spalte] = vorschlag;
            this.vorschlaege[zeile][spalte] = true;
            this.moeglicheZahlen = new String[9][9];

            for (int i = 0; i < 9; i++) {

                for (int j = 0; j < 9; j++) {

                    moeglicheZahlen[i][j] = "";
                    mz[i][j] = new HashSet<Integer>();
                }

            }

            this.eingetrageneZahlenZeilen = this.eingetrageneZahlenZeilenSammeln();
            this.eingetrageneZahlenSpalten = this.eingetrageneZahlenSpaltenSammeln();
            this.eingetrageneZahlenBlock = this.eingetrageneZahlenBlockSammeln();

            return true;

        }

        return false;
    }

        /**
         * Die Methode entfernt die vom Spieler vorgeschlagene Zahl
         * aus einem entsprechenden Feld mit gegebener Zeile und Spalte
         * @param vorschlag
         * @param zeile
         * @param spalte
         */
    public void vorschlagEntfernen(int vorschlag, int zeile, int spalte) {

        this.felder[zeile][spalte] = 0;
        this.vorschlaege[zeile][spalte] = false;
        this.moeglicheZahlen = new String[9][9];

        for(int i = 0; i < 9; i++) {

            for(int j = 0; j < 9; j++) {

                moeglicheZahlen[i][j] = "";
                mz[i][j] = new HashSet<Integer>();
            }

        }

        this.eingetrageneZahlenZeilen = this.eingetrageneZahlenZeilenSammeln();
        this.eingetrageneZahlenSpalten = this.eingetrageneZahlenSpaltenSammeln();
        this.eingetrageneZahlenBlock = this.eingetrageneZahlenBlockSammeln();

    }


    /**
     * Die Methode aktualisiert alle Felder in einer Zeile, Spalte un einem Block
     * nach Eintragen oder Entfernen einer neuen Zahl
     */
    public void felderAktualisieren() {

        this.moeglicheZahlenSammeln();
        this.moeglicheZahlenAktualisieren();

    }

    //Getters and Setters
    public int[][] getFelder() {
        return felder;
    }


    public void setFelder(int[][] felder) {
        this.felder = felder;
    }


    public String[][] getMoeglicheZahlen() {
        return moeglicheZahlen;
    }


    public void setMoeglicheZahlen(String[][] moeglicheZahlen) {
        this.moeglicheZahlen = moeglicheZahlen;
    }


    public Set<Integer> getZiffer() {
        return ziffer;
    }


    public void setZiffer(Set<Integer> ziffer) {
        this.ziffer = ziffer;
    }


    public List<List<Integer>> getEingetrageneZahlenZeilen() {
        return eingetrageneZahlenZeilen;
    }


    public void setEingetrageneZahlenZeilen(List<List<Integer>> eingetrageneZahlenZeilen) {
        this.eingetrageneZahlenZeilen = eingetrageneZahlenZeilen;
    }


    public List<List<Integer>> getEingetrageneZahlenSpalten() {
        return eingetrageneZahlenSpalten;
    }


    public void setEingetrageneZahlenSpalten(List<List<Integer>> eingetrageneZahlenSpalten) {
        this.eingetrageneZahlenSpalten = eingetrageneZahlenSpalten;
    }


    public List<List<Integer>> getEingetrageneZahlenBlock() {
        return eingetrageneZahlenBlock;
    }


    public void setEingetrageneZahlenBlock(List<List<Integer>> eingetrageneZahlenBlock) {
        this.eingetrageneZahlenBlock = eingetrageneZahlenBlock;
    }


    public HashSet<Integer>[][] getMz() {
        return mz;
    }


    public void setMz(HashSet<Integer>[][] mz) {
        this.mz = mz;
    }

    public boolean[][] getVorschlaege() {
        return vorschlaege;
    }

    public void setVorschlaege(boolean[][] vorschlaege) {
        this.vorschlaege = vorschlaege;
    }

}
