package com.polytech.tp;

public class CoursEnLigne extends CoursDecorator {

    public CoursEnLigne(ICours cours) {
        //TODO: Implémenter le reste ...
        super(cours);
    }

    @Override
    public String getDescription() {
        // TODO Auto-generated method stub
        return super.getDescription() + " (En ligne)";
    }

    @Override
    public double getDuree() {
        // TODO Auto-generated method stub
    
        return super.getDuree();
    };
}
