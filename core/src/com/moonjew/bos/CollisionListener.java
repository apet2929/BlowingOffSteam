package com.moonjew.bos;

import com.badlogic.gdx.physics.box2d.*;
import com.moonjew.bos.entities.Player;

public class CollisionListener implements ContactListener {

    public static final short SEAWEED_MASK = 2;

    private Player player;

    public CollisionListener(Player player){
        this.player = player;
    }

    @Override
    public void beginContact(Contact contact) {
        System.out.println("Contact");
        Fixture fa = contact.getFixtureA();
        Fixture fb = contact.getFixtureB();
        Body a = fa.getBody();
        Body b = fb.getBody();
        System.out.println(fa.getBody().getType() + " has hit " + fb.getBody().getType());

        if(fa.isSensor() || fb.isSensor()){
            String strA = (String) a.getUserData();
            String strB = (String) b.getUserData();
            if(strA != null && strB != null) {
                if (strA.equals("player") || strB.equals("player")) {
                    if (strA.equals("volcano") || strB.equals("volcano")) {
                        player.inVolcano = true;
                    } else if (strA.equals("seaweed") || strB.equals("seaweed")) {
                        player.inSeaweed = true;
                    }

                }
            }
        } else {
            String strA = (String) a.getUserData();
            String strB = (String) b.getUserData();
            if (strA != null && strB != null) {
                if (strA.equals("player") || strB.equals("player")) {
                    if (strA.equals("fish") || strB.equals("fish")) {
                        player.steam -= 15;

                    }
                }
            }
        }
    }


    @Override
    public void endContact(Contact contact) {
        Fixture fa = contact.getFixtureA();
        Fixture fb = contact.getFixtureB();

        if(fa.isSensor() || fb.isSensor()){
//            Player left volcano
            Body a = fa.getBody();
            Body b = fb.getBody();
            String strA = (String) a.getUserData();
            String strB = (String) b.getUserData();
            if(strA != null && strB != null) {
                if(strA.equals("player") || strB.equals("player")) {
                    if (strA.equals("volcano") || strB.equals("volcano")) {
                        player.inVolcano = false;
                    } else if (strA.equals("seaweed") || strB.equals("seaweed")) {
                        player.inSeaweed = false;
                    }
                }
            }
        }
    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {

    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {

    }
}
