package de.dafuqs.spectrum.enchantments;

import de.dafuqs.spectrum.SpectrumCommon;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentTarget;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.util.Identifier;

public class FirstStrikeEnchantment extends SpectrumEnchantment {

	public FirstStrikeEnchantment(Rarity weight, Identifier unlockAdvancementIdentifier, EquipmentSlot... slotTypes) {
		super(weight, EnchantmentTarget.WEAPON, slotTypes, unlockAdvancementIdentifier);
	}

	public int getMinPower(int level) {
		return 10;
	}

	public int getMaxPower(int level) {
		return super.getMinPower(level) + 30;
	}

	public int getMaxLevel() {
		return SpectrumCommon.CONFIG.FirstStrikeMaxLevel;
	}

	public boolean canAccept(Enchantment other) {
		return super.canAccept(other);
	}

}
