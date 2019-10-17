package net.runelite.client.plugins.bloodrunes;

import com.google.common.collect.ImmutableSet;
import lombok.AccessLevel;
import lombok.Getter;
import net.runelite.api.*;
import net.runelite.api.events.GameObjectDespawned;
import net.runelite.api.events.GameObjectSpawned;
import net.runelite.api.events.GameStateChanged;
import net.runelite.client.eventbus.EventBus;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.plugins.PluginType;
import net.runelite.client.ui.overlay.OverlayManager;

import javax.inject.Inject;
import java.util.Set;

@PluginDescriptor(
        name="Blood Runes",
        description = "Helper for crafting blood runes",
        enabledByDefault = true,
        type = PluginType.EXTERNAL
)
public class BloodRunesPlugin extends Plugin {

    private static final Set<Integer> ANIMATIONS = new ImmutableSet.Builder<Integer>()
            .add(
                7201,
                4482
            ).build();

    @Getter(AccessLevel.PACKAGE)
    private GameObject runestoneNorth;
    @Getter(AccessLevel.PACKAGE)
    private GameObject runestoneSouth;

    @Inject
    private OverlayManager overlayManager;

    @Inject
    private Client client;

    @Inject
    private RunestoneOverlay runestoneOverlay;

    @Inject
    private EventBus eventbus;

    @Override
    protected void startUp() {
        addSubscriptions();
        overlayManager.add(runestoneOverlay);

    }

    @Override
    protected void shutDown() {
        eventbus.unregister(this);
        overlayManager.remove(runestoneOverlay);

    }

    private void addSubscriptions() {
        eventbus.subscribe(GameObjectSpawned.class, this, this::onGameObjectSpawned);
        eventbus.subscribe(GameObjectDespawned.class, this, this::onGameObjectDespawned);
        eventbus.subscribe(GameStateChanged.class, this, this::onGameStateChanged);
    }

    private void onGameStateChanged(GameStateChanged event) {
        switch(client.getGameState()) {
            case LOADING:
            case LOGIN_SCREEN:
                runestoneNorth = null;
                runestoneSouth = null;
                break;

        }
    }

    private void onGameObjectSpawned(GameObjectSpawned event) {
        GameObject object = event.getGameObject();
        if(object.getId() == Runestones.NORTH.getId()) {
            runestoneNorth = object;
        } else if(object.getId() == Runestones.SOUTH.getId()) {
            runestoneSouth = object;
        }
    }

    private void onGameObjectDespawned(GameObjectDespawned event) {
        GameObject object = event.getGameObject();
        if(object.getId() == Runestones.NORTH.getId()) {
            runestoneNorth = null;
        } else if(object.getId() == Runestones.SOUTH.getId()) {
            runestoneSouth = null;
        }
    }

    public boolean isMineable(Runestones runestone) {
        return client.getVar(runestone.depletedVarbit) == 0;
    }

    public boolean isChipping() {
        Player localPlayer = client.getLocalPlayer();
        if(localPlayer == null)
            return false;
        return ANIMATIONS.contains(localPlayer.getAnimation());
    }
}
