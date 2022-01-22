package de.dafuqs.spectrum.registries;

import de.dafuqs.spectrum.sound.SpectrumSoundEvents;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.ArmorMaterial;
import net.minecraft.item.Items;
import net.minecraft.recipe.Ingredient;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Lazy;

import java.util.function.Supplier;

public enum SpectrumArmorMaterials implements ArmorMaterial {

   BEDROCK("bedrock", 5, new int[]{6, 10, 14, 6}, 5, SoundEvents.ITEM_ARMOR_EQUIP_NETHERITE, 4.0F, 0.4F, Ingredient::empty),
   GLOW_VISION("glow_vision", 15, new int[]{1, 2, 3, 1}, 7, SpectrumSoundEvents.ITEM_ARMOR_EQUIP_GLOW_VISION, 0.0F, 0.0F, () -> {
	  return Ingredient.ofItems(Items.GLOW_INK_SAC);
   }),
   EMERGENCY("gemstone", 9, new int[]{3, 5, 7, 3}, 15, SoundEvents.BLOCK_AMETHYST_BLOCK_CHIME, 0.0F, 0.0F, () -> {
	  return Ingredient.fromTag(SpectrumItemTags.GEMSTONE_SHARDS);
   });

   private static final int[] BASE_DURABILITY = new int[]{13, 15, 16, 11};
   private final String name;
   private final int durabilityMultiplier;
   private final int[] protectionAmounts;
   private final int enchantability;
   private final SoundEvent equipSound;
   private final float toughness;
   private final float knockbackResistance;
   private final Lazy<Ingredient> repairIngredientSupplier;

   SpectrumArmorMaterials(String name, int durabilityMultiplier, int[] protectionAmounts, int enchantability, SoundEvent equipSound, float toughness, float knockbackResistance, Supplier<Ingredient> repairIngredientSupplier) {
	  this.name = name;
	  this.durabilityMultiplier = durabilityMultiplier;
	  this.protectionAmounts = protectionAmounts;
	  this.enchantability = enchantability;
	  this.equipSound = equipSound;
	  this.toughness = toughness;
	  this.knockbackResistance = knockbackResistance;
	  this.repairIngredientSupplier = new Lazy(repairIngredientSupplier);
   }

   public int getDurability(EquipmentSlot slot) {
	  return BASE_DURABILITY[slot.getEntitySlotId()] * this.durabilityMultiplier;
   }

   public int getProtectionAmount(EquipmentSlot slot) {
	  return this.protectionAmounts[slot.getEntitySlotId()];
   }

   public int getEnchantability() {
	  return this.enchantability;
   }

   public SoundEvent getEquipSound() {
	  return this.equipSound;
   }

   public Ingredient getRepairIngredient() {
	  return this.repairIngredientSupplier.get();
   }

   public String getName() {
	  return this.name;
   }

   public float getToughness() {
	  return this.toughness;
   }

   public float getKnockbackResistance() {
	  return this.knockbackResistance;
   }
}
