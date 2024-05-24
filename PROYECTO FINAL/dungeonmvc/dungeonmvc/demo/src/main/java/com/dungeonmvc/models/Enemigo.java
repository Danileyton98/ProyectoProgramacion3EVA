package com.dungeonmvc.models;

import java.util.ArrayList;

import com.dungeonmvc.interfaces.Observer;
import com.dungeonmvc.utils.Vector2;

public class Enemigo extends Personaje{
    
    int percepcion;
    ArrayList<Observer> observers;
    
    public Enemigo(Vector2 position,String image, String name, int puntosVida, int fuerza, int defensa, int velocidad, String portrait, int percepcion){
        super(position,image,name,puntosVida,fuerza,defensa,velocidad,portrait);
        this.percepcion = percepcion;
        observers = new ArrayList<>();
    }

    public int getPercepcion(){
        return percepcion;
    }

    public void setPercepcion(int percepcion){
        this.percepcion = percepcion;
    }

    public void suscribe(Observer observer){
        observers.add(observer);
    }

    public void unsuscribe(Observer observer){
        observers.remove(observer);
    }
}
