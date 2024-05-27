package com.dungeonmvc;

import java.util.ArrayList;

import com.dungeonmvc.models.*;
import com.dungeonmvc.models.Board.Direction;
import com.dungeonmvc.utils.Vector2;


public class GameManager {
        

    ArrayList<Personaje> monigotes = new ArrayList<>();
    private static GameManager instance;
    Enemigo enemigo;
    Player player;
    Board board;
    
    private GameManager(){

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

    public void newTurn(Direction direction){
        if(player != null){
            player.move(direction);
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

        player = new Player(new Vector2(0, 0),"player", "Paladin",34,65,45,47,"portrait", "item7", "item6",board);
        player.getInventory().addItem("item1");
        player.getInventory().addItem("item2");
        player.getInventory().addItem("item3");
        player.getInventory().addItem("item4");
        player.getInventory().addItem("item5");

        

        monigotes.add(player);
        monigotes.add(enemigo = new Enemigo(new Vector2(4, 4), "enemigo","Drako", 35, 12, 29, 34, "portrait" ,45));
        monigotes.add(enemigo = new Enemigo(new Vector2(7, 9), "enemigo","bellatrix", 35, 12, 29, 34, "portrait" ,45));

        for(int i = 0; i < monigotes.size(); i++){
            monigotes.get(i);
        }
    }   
}