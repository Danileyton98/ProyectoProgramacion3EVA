package com.dungeonmvc.models;

import com.dungeonmvc.interfaces.Interactuable;
import com.dungeonmvc.utils.Vector2;

public class Cell {
    private boolean isFloor;
    private Interactuable interactuable;
    

    public Cell(boolean isFloor) {
        this.isFloor = isFloor;
    }

    public boolean isIsFloor() {
        return this.isFloor;
    }

    public boolean getIsFloor() {
        return this.isFloor;
    }

    public void setIsFloor(boolean isFloor) {
        this.isFloor = isFloor;
    }

    public Interactuable getInteractuable(){
        return interactuable;
    }

    public void setInteractuable(Interactuable interactuable){
        this.interactuable = interactuable;
    }

    //Retorna true en el caso que this.interactuable fuese distinto a null, ya que habría un objeto en la casilla, de lo contrario retornaria false
    public boolean ocupada(){
        return this.interactuable != null;
    }

    


}
