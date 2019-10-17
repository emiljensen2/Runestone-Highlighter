package net.runelite.client.plugins.bloodrunes;

import lombok.AccessLevel;
import lombok.Getter;
import net.runelite.api.Varbits;

public enum Runestones {

    NORTH(8981, Varbits.RUNESTONE_NORTH_DEPLETED),
    SOUTH(10796, Varbits.RUNESTONE_SOUTH_DEPLETED);

    @Getter(AccessLevel.PACKAGE)
    public final int id;
    @Getter(AccessLevel.PACKAGE)
    public final Varbits depletedVarbit;

    Runestones(final int id, final Varbits depletedVarbit) {
        this.id = id;
        this.depletedVarbit = depletedVarbit;
    }
}
