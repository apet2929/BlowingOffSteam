package com.moonjew.bos;

import com.badlogic.gdx.physics.box2d.*;

public class CollisionListener implements ContactListener {
    @Override
    public void beginContact(Contact contact) {
        System.out.println("Contact");
        Fixture fa = contact.getFixtureA();
        Fixture fb = contact.getFixtureB();
        System.out.println(fa.getBody().getType() + " has hit " + fb.getBody().getType());
        Body a = fa.getBody();
        Body b = fb.getBody();
        if(a.getUserData() != null && b.getType().equals(BodyDef.BodyType.StaticBody)){
            if(a.getUserData().equals("fish")) {
                System.out.println("True");
//                a.setLinearVelocity(a.getLinearVelocity().scl(-1, 1));
            }
        } else if(b.getUserData() != null && a.getType().equals(BodyDef.BodyType.StaticBody)) {
            if(b.getUserData().equals("fish")) {
                System.out.println("True");
//                b.setLinearVelocity(b.getLinearVelocity().scl(-1, 1));
            }
        }
    }

    @Override
    public void endContact(Contact contact) {

    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {

    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {

    }
}
