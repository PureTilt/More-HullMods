package data.hullmods;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.combat.ShipAPI.HullSize;
import com.fs.starfarer.api.loading.HullModSpecAPI;
import com.fs.starfarer.api.ui.Alignment;
import com.fs.starfarer.api.ui.LabelAPI;
import com.fs.starfarer.api.ui.TooltipMakerAPI;
import com.fs.starfarer.api.util.Misc;
import org.lwjgl.input.Keyboard;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;

import static Utilities.mhmods_eneableSmod.getEnable;

public class MHMods_IntegratedArmor extends mhmods_baseSHmod {

    final float
            MinArmor = 0.05f,
            addForSMod = 500;

    private final Map<HullSize, Integer> maxArmour = new HashMap<>();

    {
        maxArmour.put(HullSize.FIGHTER, 500);
        maxArmour.put(HullSize.FRIGATE, 500);
        maxArmour.put(HullSize.DESTROYER, 1000);
        maxArmour.put(HullSize.CRUISER, 1500);
        maxArmour.put(HullSize.CAPITAL_SHIP, 2000);

        id = "mhmods_integratedarmor";
    }

    @Override
    public void applyEffectsAfterShipCreation(ShipAPI ship, String id) {
        float maxArmor = maxArmour.get(ship.getHullSize());
        //Smod part
        if (ship.getVariant().getSMods().contains(this.id) && getEnable()) maxArmor += addForSMod;
        if (ship.getArmorGrid().getArmorRating() > maxArmor) {
            ship.getMutableStats().getMinArmorFraction().modifyFlat(id, MinArmor * (maxArmor / ship.getArmorGrid().getArmorRating()));
        } else {
            ship.getMutableStats().getMinArmorFraction().modifyFlat(id, MinArmor);
        }
    }

    public String getDescriptionParam(int index, HullSize hullSize) {
        if (index == 0) return (Math.round(100 * MinArmor) + "%");
        if (index == 1) return "" + Math.round(maxArmour.get(HullSize.FRIGATE) * MinArmor);
        if (index == 2) return "" + Math.round(maxArmour.get(HullSize.DESTROYER) * MinArmor);
        if (index == 3) return "" + Math.round(maxArmour.get(HullSize.CRUISER) * MinArmor);
        if (index == 4) return "" + Math.round(maxArmour.get(HullSize.CAPITAL_SHIP) * MinArmor);
        return null;
    }

    @Override
    public void addPostDescriptionSection(TooltipMakerAPI tooltip, HullSize hullSize, ShipAPI ship, float width, boolean isForModSpec) {
        if (!getEnable()) return;
        if (!Keyboard.isKeyDown(Keyboard.getKeyIndex("F1")) && (ship == null || !ship.getVariant().getSMods().contains(id))){
            tooltip.addPara("Hold F1 to show S-mod effect info", Misc.getGrayColor(), 10);
            return;
        }
        Color lableColor = Misc.getTextColor();
        Color h = Misc.getHighlightColor();
        if (ship == null || !ship.getVariant().getSMods().contains(id)) {
            tooltip.addSectionHeading("Effect if S-modded", Alignment.MID, pad);
            lableColor = Misc.getGrayColor();
            h = Misc.getGrayColor();
        }
        HullModSpecAPI hullmod = Global.getSettings().getHullModSpec(id);
        LabelAPI label = tooltip.addPara(hullmod.getDescriptionFormat(), pad, lableColor, h,
                (Math.round(100 * MinArmor) + "%"),
                Math.round((maxArmour.get(HullSize.FRIGATE) + addForSMod) * MinArmor) + "",
                Math.round((maxArmour.get(HullSize.DESTROYER) + addForSMod) * MinArmor) + "",
                Math.round((maxArmour.get(HullSize.CRUISER) + addForSMod) * MinArmor) + "",
                Math.round((maxArmour.get(HullSize.CAPITAL_SHIP) + addForSMod) * MinArmor) + "");

        label.setHighlight(Math.round((maxArmour.get(HullSize.FRIGATE) + addForSMod) * MinArmor) + "",
                Math.round((maxArmour.get(HullSize.DESTROYER) + addForSMod) * MinArmor) + "",
                Math.round((maxArmour.get(HullSize.CRUISER) + addForSMod) * MinArmor) + "",
                Math.round((maxArmour.get(HullSize.CAPITAL_SHIP) + addForSMod) * MinArmor) + "");

        label.setHighlightColors(s, s, s, s);
    }

}