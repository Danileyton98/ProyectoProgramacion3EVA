package com.dungeonmvc.models;

import java.util.ArrayList;

import com.dungeonmvc.GameManager;
import com.dungeonmvc.interfaces.Observer;
import com.dungeonmvc.utils.Vector2;


public class Board{
    private ArrayList<Observer> observers;

    public enum Direction {UP, RIGHT, DOWN, LEFT}

    private int size;
    private Cell board[][];
    private String floorImage;
    private String wallImage;
    private String cofre;
    private Player player;  //PROVISIONAL, tener las referencias de todos los elementos interactivos
    private Enemigo enemigo;

    public Board(int size, String floorImage, String wallImage) {
        this.size = size;
        this.board = new Cell[size][size];
        this.floorImage = floorImage;
        this.wallImage = wallImage;

        this.player = GameManager.getInstance().getPlayer();    //PROVISIONAL
        this.enemigo = GameManager.getInstance().getEnemigo();
        observers=new ArrayList<>();

    }

    public void suscribe(Observer observer){
        observers.add(observer);
    }

    public void unsuscribe(Observer observer){
        observers.remove(observer);
    }

    public void notifyObservers(){
        observers.forEach(x -> x.onChange());
    }

    public int getSize() {
        return this.size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public String getFloorImage() {
        return this.floorImage;
    }

    public void setFloorImage(String floorImage) {
        this.floorImage = floorImage;
    }

    public String getWallImage() {
        return this.wallImage;
    }

    public void setWallImage(String wallImage) {
        this.wallImage = wallImage;
    }

    public String getCofre(){
        return cofre;
    }

    public void setCofre(String cofre){
        this.cofre = cofre;
    }

    public boolean isFloor(Vector2 position){
        return board[position.getX()][position.getY()].getIsFloor();
    }

    public boolean isFloor(int x, int y){
        return board[x][y].getIsFloor();
    }

    public ArrayList<Observer> getObservers() {
        return this.observers;
    }

    public void setObservers(ArrayList<Observer> observers) {
        this.observers = observers;
    }

    public Player getPlayer() {
        return this.player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public Enemigo getEnemigo(){
        return this.enemigo;
    }

    public void setEnemigo(Enemigo enemigo){
        this.enemigo = enemigo;
    }

    public void newCell(Vector2 position, boolean isFloor){
        Cell cell = new Cell(isFloor);
        board[position.getX()][position.getY()] = cell;
    }

    
    public void move(Player player, Direction direction){
        Vector2 destino = getDestination(player.position, direction);
        if (destino.getX() >= 0 && destino.getX() < size && destino.getY() >= 0 && destino.getY() < size){
            if(board[destino.getX()][destino.getY()].getIsFloor()){
                player.setPosition(destino);
            }
            
        }
        notifyObservers();
    }

    public Vector2 getDestination(Vector2 position, Direction direction){
        int destinoX = position.getX();
        int destinoY = position.getY();
        switch (direction) {
            case UP:
                destinoY--;
            break;

            case RIGHT:
                destinoX++;
            break;

            case DOWN:
                destinoY++;
                break;

            case LEFT:
                destinoX--;
                break;

            default:
                break;
        }
        return new Vector2(destinoX, destinoY);
    }
}