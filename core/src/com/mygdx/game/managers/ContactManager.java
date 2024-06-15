package com.mygdx.game.managers;

import static com.mygdx.game.GameSettings.BULLET_BIT;
import static com.mygdx.game.GameSettings.ENEMY_BULLET_BIT;
import static com.mygdx.game.GameSettings.PIRATES_BIT;
import static com.mygdx.game.GameSettings.SHIP_BIT;
import static com.mygdx.game.GameSettings.TRASH_BIT;

import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.physics.box2d.World;
import com.mygdx.game.objects.GameObject;

public class ContactManager {
    World world;

    public ContactManager(World world){
        this.world = world;

        world.setContactListener(new ContactListener() {
            @Override
            public void beginContact(Contact contact) {
                Fixture fixA = contact.getFixtureA();
                Fixture fixB = contact.getFixtureB();

                int cDef1 = fixA.getFilterData().categoryBits;
                int cDef2 = fixB.getFilterData().categoryBits;

                if ((cDef1 == SHIP_BIT && cDef2 == TRASH_BIT) ||
                        (cDef2 == SHIP_BIT && cDef1 == TRASH_BIT) ||
                        (cDef1 == PIRATES_BIT && cDef2 == TRASH_BIT) ||
                        (cDef2 == PIRATES_BIT && cDef1 == TRASH_BIT) ||
                        (cDef1 == BULLET_BIT && cDef2 == TRASH_BIT) ||
                        (cDef2 == BULLET_BIT && cDef1 == TRASH_BIT) ||
                        (cDef1 == SHIP_BIT && cDef2 == ENEMY_BULLET_BIT) ||
                        (cDef2 == SHIP_BIT && cDef1 == ENEMY_BULLET_BIT) ||
                        (cDef1 == PIRATES_BIT && cDef2 == ENEMY_BULLET_BIT) ||
                        (cDef2 == PIRATES_BIT && cDef1 == ENEMY_BULLET_BIT) ||
                        (cDef1 == PIRATES_BIT && cDef2 == SHIP_BIT) ||
                        (cDef2 == PIRATES_BIT && cDef1 == SHIP_BIT) ||
                        (cDef1 == BULLET_BIT && cDef2 == PIRATES_BIT) ||
                        (cDef2 == BULLET_BIT && cDef1 == PIRATES_BIT)){
                    ((GameObject) fixA.getUserData()).hit();
                    ((GameObject) fixB.getUserData()).hit();
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
        });
    }
}
