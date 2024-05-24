package com.dungeonmvc.models;

import java.util.ArrayList;

import com.dungeonmvc.interfaces.Observer;
import com.dungeonmvc.utils.Vector2;

public class Personaje {
    
    Vector2 position;
    String name;
    String image;
    int puntosVida;
    int fuerza;
    int defensa;
    int velocidad;
    String portrait;
    ArrayList<Observer> observers;

    public Personaje(){

    }

    public Personaje(Vector2 position,String image, String name, int puntosVida, int fuerza, int defensa, int velocidad, String portrait){
        this.position = position;
        this.name = name;
        this.image = image;
        this.puntosVida = puntosVida;
        this.fuerza = fuerza;
        this.defensa = defensa;
        this.velocidad = velocidad;
        this.portrait = portrait;
        observers = new ArrayList<>();
    }

    public String getImage() {
        return this.image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getName(){
        return name;
    }

    public void setName(String name){
        this.name = name;
        notifyObservers();
    }

    public void suscribe(Observer observer){
        observers.add(observer);
    }

    public void unsuscribe(Observer observer){
        observers.remove(observer);
    }

    private void notifyObservers() {
        observers.forEach(x -> x.onChange());
    }

    public int getPuntosVida(){
        return puntosVida;
    }

    public void setPuntosVida(int puntosVida){
        this.puntosVida = puntosVida;
    }

    public int getFuerza(){
        return fuerza;
    }

    public void setFuerza(int fuerza){
        this.fuerza = fuerza;
    }

    public int getDefensa(){
        return defensa;
    }

    public void setDefensa(int defensa){
        this.defensa = defensa;
    }

    public int getVelocidad(){
        return velocidad;
    }

    public void setVelocidad(int velocidad){
        this.velocidad = velocidad;
    }

    public Vector2 getPosition() {
        return this.position;
    }

    public int getX(){
        return this.position.getX();
    }

    public int getY(){
        return this.position.getY();
    }

    public void setPosition(Vector2 position) {
        this.position = position;
        //notifyObservers();
    }

    public String getPortrait() {
        return this.portrait;
    }

    public void setPortrait(String portrait) {
        this.portrait = portrait;
        notifyObservers();
    }
}
