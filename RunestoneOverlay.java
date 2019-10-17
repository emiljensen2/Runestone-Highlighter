package net.runelite.client.plugins.bloodrunes;

import net.runelite.api.Client;
import net.runelite.api.GameObject;
import net.runelite.client.graphics.ModelOutlineRenderer;
import net.runelite.client.plugins.bloodrunes.BloodRunesPlugin;
import net.runelite.client.ui.overlay.*;

import javax.inject.Inject;
import java.awt.*;

public class RunestoneOverlay extends Overlay {

    private final Client client;
    private final BloodRunesPlugin plugin;
    private final ModelOutlineRenderer outlineRenderer;

    @Inject
    private RunestoneOverlay(final Client client, final BloodRunesPlugin plugin, final ModelOutlineRenderer outlineRenderer)
    {
        super(plugin);
        setPosition(OverlayPosition.TOP_RIGHT);
        this.outlineRenderer = outlineRenderer;
        this.client = client;
        this.plugin = plugin;
        setPosition(OverlayPosition.DYNAMIC);
        setPriority(OverlayPriority.LOW);
        setLayer(OverlayLayer.ABOVE_SCENE);
    }

    @Override
    public Dimension render(Graphics2D graphics) {

        GameObject north = plugin.getRunestoneNorth();
        GameObject south = plugin.getRunestoneSouth();

        if(north == null || south == null) {
            return null;
        }

        if(plugin.isChipping()) {
            renderRunestoneOverlay(graphics, north, Color.YELLOW);
            renderRunestoneOverlay(graphics, south, Color.YELLOW);
            return null;
        }

        if(plugin.isMineable(Runestones.NORTH)) {
            renderRunestoneOverlay(graphics, north, Color.GREEN);
        } else {
            renderRunestoneOverlay(graphics, north, Color.RED);
        }

        if(plugin.isMineable(Runestones.SOUTH)) {
            renderRunestoneOverlay(graphics, south, Color.GREEN);
        } else {
            renderRunestoneOverlay(graphics, south, Color.RED);
        }


        return null;
    }

    private void renderRunestoneOverlay(Graphics2D graphics2D, GameObject object, Color color) {
        //outlineRenderer.drawOutline(object, 8, color);
        OverlayUtil.renderPolygon(graphics2D, object.getConvexHull(), color);
    }
}
