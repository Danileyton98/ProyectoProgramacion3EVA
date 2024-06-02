package com.dungeonmvc.models;

import java.util.ArrayList;

import com.dungeonmvc.utils.DiceRoll.Dice;

public class Arma {
    private String imagen;
    private String nombre;
    private int diceQuantity;
    private Dice dice;
    ArrayList<Habilidades> habilidades = new ArrayList<>();

    public Arma (String imagen, String nombre, int diceQuantity, Dice dice, ArrayList<Habilidades> habilidades){
        this.imagen = imagen;
        this.nombre = nombre;
        this.diceQuantity = diceQuantity;
        this.dice = dice;
        this.habilidades = habilidades;
    }

    public String getImagen(){
        return this.imagen;
    }

    public String getNombre(){
        return this.nombre;
    }

    public int getDiceQuantity(){
        return this.diceQuantity;
    }

    public Dice getDice(){
        return this.dice;
    }

    public ArrayList<Habilidades> getHabilidades(){
        return this.habilidades;
    }
}
