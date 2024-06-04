package com.dungeonmvc;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Random;

import com.dungeonmvc.controllers.BoardViewController;
import com.dungeonmvc.models.*;
import com.dungeonmvc.models.Board.Direction;
import com.dungeonmvc.utils.Vector2;
import com.dungeonmvc.utils.DiceRoll.Dice;


public class GameManager {
    ArrayList<boolean[][]> mapas = new ArrayList<>();
    ArrayList<Habilidades> habilidadEnemigo1 = new ArrayList<>();
    ArrayList<Habilidades> habilidadArmaPlayerRight = new ArrayList<>();
    ArrayList<Habilidades> habilidadArmaPlayerLeft = new ArrayList<>();
    HashMap<Habilidades,Resistencias> resistenciaPlayer = new HashMap<>();
    HashMap<Habilidades,Resistencias> resistenciaEnemigo1 = new HashMap<>();
    ArrayList<Personaje> monigotes = new ArrayList<>();
    ArrayList<Objetos> potenciadores = new ArrayList<>();
    private static GameManager instance;
    Enemigo enemigo;
    EnemigoFantasma enemigoFantasma;
    Player player;
    Board board;
    BoardViewController boardViewController;
    Objetos objeto;
    boolean[][] mapa;
    
    
    private GameManager(){
        
    }


    public ArrayList<Personaje> getMonigotes(){
        return monigotes;
    }

    public ArrayList<Objetos> getPotenciadores(){
        return potenciadores;
    }

    public static GameManager getInstance() {
        if (instance == null) {
            instance = new GameManager();
        }
        return instance;
    }

    public BoardViewController getBoardViewController(){
        return this.boardViewController;
    }

    public void setBoardViewController(BoardViewController boardViewController){
        this.boardViewController = boardViewController;
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

    public Objetos getObjetos(){
        return objeto;
    }

    public EnemigoFantasma getEnemigoFantasma(){
        return enemigoFantasma;
    }

    //Llamamos a los metodos move de jugador y move de enemigo recorriendo el arraylist de los personajes y haciendo un casteo de la variable
    //personaje para convertir el objeto tipo personaje en enemigo
    public void newTurn(Direction direction){
            player.move(direction);
            for(Personaje personaje : monigotes){
                if(personaje instanceof Enemigo){
                    ((Enemigo)personaje).moveEnemigo(direction);
                }/*else if(personaje instanceof EnemigoFantasma){
                    ((EnemigoFantasma)personaje).moveEnemigoFantasma(direction);
                }*/
            }
    }
    
        

    public void testGame(){

        //Agregamos mapas a la lista
        mapas.add(mapa = new boolean[][] {
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
        });

        mapas.add(mapa = new boolean[][]{
            {true, true, true, true, true, true, true, true, true, true, true, false, true, true, true},
            {true, false, false, false, false, false, true, false, false, false, false, false, false, false, true},
            {true, false, true, true, true, false, true, false, true, true, true, true, true, true, true},
            {true, false, true, false, false, false, false, false, false, false, false, false, false, false, true},
            {true, false, true, false, true, true, true, true, true, true, false, true, true, true, true},
            {true, false, false, false, false, false, false, false, false, false, false, false, false, false, true},
            {true, true, true, true, true, true, true, true, true, true, true, true, true, true, true},
            {true, false, false, false, false, false, false, false, false, false, false, true, false, false, true},
            {true, false, true, true, true, true, true, true, true, true, true, false, true, false, true},
            {true, false, false, false, false, false, false, false, false, false, false, false, false, false, true},
            {true, true, true, true, true, true, true, true, true, true, true, true, true, true, true},
            {true, false, false, false, false, false, false, false, false, false, false, false, false, false, true},
            {true, false, true, true, true, true, true, true, true, true, false, true, false, true, true},
            {true, false, false, false, false, false, false, false, false, true, false, false, true, false, true},
            {true, true, true, true, true, true, true, true, true, true, true, true, true, true, true}
            });


        Random random = new Random();
        //Guardamos un numero al azar entre el largo de la lista, asi tendramos mapas al azar
        int numMapa = random.nextInt(mapas.size());

        board = new Board(mapas.get(numMapa).length,"floor","wall");
        for (int i = 0; i < mapas.get(numMapa).length; i++) {
            for (int j=0;j < mapas.get(numMapa)[0].length;j++){
                board.newCell(new Vector2(i, j), mapas.get(numMapa)[i][j]);
            }
        }

        Objetos objeto1;
        Objetos objeto2;
        potenciadores.add(objeto1 = new Objetos("cofre","Vida extra", 12, 5, new Vector2(13, 12), 1, 5,board, boardViewController));
        potenciadores.add(objeto2 = new Objetos("cofre","Fuerza extra", 15, 8, new Vector2(0, 10), 1, 8,board, boardViewController));
        //Colocamos los objetos en las celdas correspondientes, para que asi detecte el jugador que hay un objeto interactivo y que no se
        //solape con el objeto
        board.getCell(objeto1.getPosition()).setInteractuable(objeto1);
        board.getCell(objeto2.getPosition()).setInteractuable(objeto2);
        
        

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

        player = new Player(new Vector2(0, 0),"player", "Paladin",60,25,30,54,"portrait", board,resistenciaPlayer, leftHand, rightHand, enemigo);
        player.getInventory().addItem("item1");
        player.getInventory().addItem("item2");
        player.getInventory().addItem("item3");
        player.getInventory().addItem("item4");
        player.getInventory().addItem("item5");

        
        
        monigotes.add(player);
        //monigotes.add(enemigoFantasma = new EnemigoFantasma(new Vector2(6, 4), "enemigo","Voldemort", 35, 30, 29, 34, "portrait", board,resistenciaEnemigo1,1,Dice.d6,3,boardViewController,habilidadEnemigo1));
        monigotes.add(enemigo = new Enemigo(new Vector2(0, 6), "enemigo","Voldemort", 35, 30, 29, 34, "portrait", board,resistenciaEnemigo1,1,Dice.d6,3,boardViewController,habilidadEnemigo1));
        monigotes.add(enemigo = new Enemigo(new Vector2(6, 9), "enemigo","Bellatrix", 35, 30, 29, 45, "portrait", board,resistenciaEnemigo1,1,Dice.d6,3, boardViewController,habilidadEnemigo1));

        //Con esta función nos ahorramos el algoritmo de ordenación, esto hara que se ordene mediante el atributo velocidad ya que
        //en la clase personaje hemos implementado Comparable y sobrecargado el metodo compareTo que define la lógica de comparación
        //basandose en el atributo
        Collections.sort(monigotes);

        for(int i = 0; i < monigotes.size(); i++){
            monigotes.get(i);
        }

        for(int i = 0; i < potenciadores.size(); i++){
            potenciadores.get(i);
        }
        
    }   
}