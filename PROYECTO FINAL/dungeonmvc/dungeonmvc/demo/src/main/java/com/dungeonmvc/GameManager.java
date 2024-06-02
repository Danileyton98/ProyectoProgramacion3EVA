package com.dungeonmvc;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;

import com.dungeonmvc.controllers.BoardViewController;
import com.dungeonmvc.models.*;
import com.dungeonmvc.models.Board.Direction;
import com.dungeonmvc.utils.Vector2;
import com.dungeonmvc.utils.DiceRoll.Dice;

import javafx.scene.layout.Pane;


public class GameManager {
    
    ArrayList<Habilidades> habilidadEnemigo1 = new ArrayList<>();
    ArrayList<Habilidades> habilidadArmaPlayerRight = new ArrayList<>();
    ArrayList<Habilidades> habilidadArmaPlayerLeft = new ArrayList<>();
    HashMap<Habilidades,Resistencias> resistenciaPlayer = new HashMap<>();
    HashMap<Habilidades,Resistencias> resistenciaEnemigo1 = new HashMap<>();
    ArrayList<Personaje> monigotes = new ArrayList<>();
    private static GameManager instance;
    Enemigo enemigo;
    Player player;
    Board board;
    BoardViewController boardViewController;
    
    private GameManager(){
        this.boardViewController = new BoardViewController();
    }

    public ArrayList<Personaje> getMonigotes(){
        return monigotes;
    }

    public static GameManager getInstance() {
        if (instance == null) {
            instance = new GameManager();
        }
        return instance;
    }

    public Enemigo getEnemigo(){
        return this.enemigo;
    }

    public Player getPlayer() {
        return this.player;
    }

    public Board getBoard() {
        return this.board;
    }

    //Llamamos a los metodos move de jugador y move de enemigo recorriendo el arraylist de los personajes y haciendo un casteo de la variable
    //personaje para convertir el objeto tipo personaje en enemigo
    public void newTurn(Direction direction){
            player.move(direction);
            for(Personaje personaje : monigotes){
                if(personaje instanceof Enemigo){
                    ((Enemigo)personaje).moveEnemigo(direction);
                }
                
            }
    }
    
        

    public void testGame(){

        

        boolean boardMatrix[][] = {
            {true, true, true, true, true, true, true, true, true, true, true, true, true, true, true},
            {true, false, false, false, true, false, false, false, false, false, false, false, false, false, true},
            {true, false, true, false, true, false, true, true, true, true, true, true, false, true, true},
            {true, false, true, false, true, false, false, false, false, false, false, false, false, false, true},
            {true, false, true, false, true, false, true, true, true, true, true, true, false, true, true},
            {true, false, false, false, true, false, false, false, false, false, false, false, false, false, true},
            {true, true, true, true, true, true, true, true, true, true, true, true, true, true, true},
            {true, true, true, true, true, true, true, true, true, true, true, true, true, true, true},
            {true, false, false, false, true, false, false, false, false, false, false, false, false, false, true},
            {true, false, true, false, true, false, true, true, true, true, true, true, false, true, true},
            {true, false, true, false, true, false, false, false, false, false, false, false, false, false, true},
            {true, false, true, false, true, false, true, true, true, true, true, true, false, true, true},
            {true, false, false, false, true, false, false, false, false, false, false, false, false, false, true},
            {true, true, true, true, true, true, true, true, true, true, true, true, true, true, true},
            {true, true, true, true, true, true, true, true, true, true, true, true, true, true, true}
        };

        board = new Board(boardMatrix.length,"floor","wall");
        for (int i = 0; i < boardMatrix.length; i++) {
            for (int j=0;j < boardMatrix[0].length;j++){
                board.newCell(new Vector2(i, j), boardMatrix[i][j]);
            }
        }

        habilidadEnemigo1.add(Habilidades.PERFORANTE);
        habilidadEnemigo1.add(Habilidades.MAGIA);
        resistenciaEnemigo1.put(Habilidades.CORTANTE,Resistencias.VULNERABLE);
        resistenciaEnemigo1.put(Habilidades.CONTUNDENTE,Resistencias.RESISTENTE);


        resistenciaPlayer.put(Habilidades.CORTANTE, Resistencias.INMUNE);
        resistenciaPlayer.put(Habilidades.FUEGO, Resistencias.ABSORVENTE);
        resistenciaPlayer.put(Habilidades.MAGIA, Resistencias.RESISTENTE);

        habilidadArmaPlayerLeft.add(Habilidades.CONTUNDENTE);
        habilidadArmaPlayerLeft.add(Habilidades.MAGIA);
        habilidadArmaPlayerRight.add(Habilidades.PERFORANTE);
        habilidadArmaPlayerRight.add(Habilidades.FUEGO);

        Arma leftHand = new Arma("item7", "Espadon del caballero Lobo", 2, Dice.d6, habilidadArmaPlayerLeft);
        Arma rightHand = new Arma("item6", "Estoque llameante", 1, Dice.d4, habilidadArmaPlayerRight);

        player = new Player(new Vector2(0, 0),"player", "Paladin",60,25,30,47,"portrait", board,resistenciaPlayer, leftHand, rightHand, enemigo);
        player.getInventory().addItem("item1");
        player.getInventory().addItem("item2");
        player.getInventory().addItem("item3");
        player.getInventory().addItem("item4");
        player.getInventory().addItem("item5");

        

        monigotes.add(player);
        monigotes.add(enemigo = new Enemigo(new Vector2(4, 4), "enemigo","Voldemort", 35, 30, 29, 34, "portrait", board,resistenciaEnemigo1,1,Dice.d6,3,boardViewController,habilidadEnemigo1));
        monigotes.add(enemigo = new Enemigo(new Vector2(7, 9), "enemigo","Bellatrix", 35, 30, 29, 34, "portrait", board,resistenciaEnemigo1,1,Dice.d6,3, boardViewController,habilidadEnemigo1));

        //Con esta función nos ahorramos el algoritmo de ordenación, esto hara que se ordene mediante el atributo velocidad ya que
        //en la clase personaje hemos implementado Comparable y sobrecargado el metodo compareTo que define la lógica de comparación
        //basandose en el atributo
        Collections.sort(monigotes);

        for(int i = 0; i < monigotes.size(); i++){
            monigotes.get(i);
        }
        
    }   
}