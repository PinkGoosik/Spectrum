package de.dafuqs.spectrum.progression;

import de.dafuqs.spectrum.progression.advancement.*;
import net.fabricmc.fabric.mixin.object.builder.CriteriaAccessor;

public class SpectrumAdvancementCriteria {

	public static HasAdvancementCriterion ADVANCEMENT_GOTTEN;
	public static HadRevelationCriterion HAD_REVELATION;
	public static PedestalCraftingCriterion PEDESTAL_CRAFTING;
	public static FusionShrineCraftingCriterion FUSION_SHRINE_CRAFTING;
	public static CompletedMultiblockCriterion COMPLETED_MULTIBLOCK;
	public static BlockBrokenCriterion BLOCK_BROKEN;
	public static TreasureHunterDropCriterion TREASURE_HUNTER_DROP;
	public static NaturesStaffUseCriterion NATURES_STAFF_USE;
	public static EnchanterCraftingCriterion ENCHANTER_CRAFTING;
	public static EnchanterEnchantingCriterion ENCHANTER_ENCHANTING;
	public static EnchantmentUpgradedCriterion ENCHANTER_UPGRADING;
	public static InertiaUsedCriterion INERTIA_USED;

	public static void register() {
		ADVANCEMENT_GOTTEN = CriteriaAccessor.callRegister(new HasAdvancementCriterion());
		HAD_REVELATION = CriteriaAccessor.callRegister(new HadRevelationCriterion());
		PEDESTAL_CRAFTING = CriteriaAccessor.callRegister(new PedestalCraftingCriterion());
		FUSION_SHRINE_CRAFTING = CriteriaAccessor.callRegister(new FusionShrineCraftingCriterion());
		COMPLETED_MULTIBLOCK = CriteriaAccessor.callRegister(new CompletedMultiblockCriterion());
		BLOCK_BROKEN = CriteriaAccessor.callRegister(new BlockBrokenCriterion());
		TREASURE_HUNTER_DROP = CriteriaAccessor.callRegister(new TreasureHunterDropCriterion());
		NATURES_STAFF_USE = CriteriaAccessor.callRegister(new NaturesStaffUseCriterion());
		ENCHANTER_CRAFTING = CriteriaAccessor.callRegister(new EnchanterCraftingCriterion());
		ENCHANTER_ENCHANTING = CriteriaAccessor.callRegister(new EnchanterEnchantingCriterion());
		ENCHANTER_UPGRADING = CriteriaAccessor.callRegister(new EnchantmentUpgradedCriterion());
		INERTIA_USED = CriteriaAccessor.callRegister(new InertiaUsedCriterion());
	}

}
