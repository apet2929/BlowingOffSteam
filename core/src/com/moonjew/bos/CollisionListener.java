package com.moonjew.bos;

import com.badlogic.gdx.physics.box2d.*;
import com.moonjew.bos.entities.Player;

public class CollisionListener implements ContactListener {

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
            System.out.println("True???");
            String strA = (String) a.getUserData();
            String strB = (String) b.getUserData();
            if(strA != null && strB != null) {
                if (strA.equals("player") || strB.equals("player")) {
                    System.out.println("PLAYER HIT VOLCANO");
                    player.inVolcano = true;
                }
            }
        }

    }

    @Override
    public void endContact(Contact contact) {
        Fixture fa = contact.getFixtureA();
        Fixture fb = contact.getFixtureB();

        if(fa.isSensor() || fb.isSensor()){
            System.out.println("True???");
//            Player left volcano
            Body a = fa.getBody();
            Body b = fb.getBody();
            String strA = (String) a.getUserData();
            String strB = (String) b.getUserData();
            if(strA != null && strB != null) {
                if (strA.equals("player") || strB.equals("player")) {
                    System.out.println("PLAYER LEFT VOLCANO");
                    player.inVolcano = false;
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
