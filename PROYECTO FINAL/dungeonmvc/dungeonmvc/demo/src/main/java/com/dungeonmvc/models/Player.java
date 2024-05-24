package com.dungeonmvc.models;

import java.util.ArrayList;


import com.dungeonmvc.interfaces.Observer;
import com.dungeonmvc.models.Board.Direction;
import com.dungeonmvc.utils.Vector2;

public class Player extends Personaje{
    ArrayList<Observer> observers;

    private int size;
    private Cell board[][];
    String leftHand;
    String rightHand;
    Inventory inventory;
    boolean eliminado = false;

    public Player(Vector2 position,String image, String name, int puntosVida, int fuerza, int defensa, int velocidad, String portrait, String leftHand, String rightHand) {
        super(position,image,name,puntosVida,fuerza,defensa,velocidad,portrait);
        observers = new ArrayList<>();
        this.leftHand = leftHand;
        this.rightHand = rightHand;
        this.inventory = new Inventory();
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

    public String getLeftHand() {
        return this.leftHand;
    }

    public void setLeftHand(String leftHand) {
        this.leftHand = leftHand;
        notifyObservers();
    }

    public String getRightHand() {
        return this.rightHand;
    }

    public void setRightHand(String rightHand) {
        this.rightHand = rightHand;
        notifyObservers();
    }

    public Inventory getInventory(){
        return this.inventory;
    }

    public void eliminar(){
        eliminado = true;
    }

    public boolean estaEliminado(){
        return eliminado;
    }

    // Comprobamos que el destino del jugador es mayor o igual a 0, esto evita que el personaje se salga del tablero
    // En segundo lugar comprobamos si el destino del jugador es suelo, esto evitara que el jugador se posicione en una casilla de pared
    /*public void move(Player player, Direction direction){
        Vector2 destino = getDestination(player.position, direction);
        if (destino.getX() >= 0 && destino.getX() < size && destino.getY() >= 0 && destino.getY() < size){
            if(board[destino.getX()][destino.getY()].getIsFloor()){
                player.setPosition(destino);
            }
            
        }
        notifyObservers();
    }*/

    /*public Vector2 getDestination(Vector2 position, Direction direction){
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
    }*/
}
