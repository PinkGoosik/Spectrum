package de.dafuqs.spectrum.recipe.anvil_crushing;

import de.dafuqs.spectrum.recipe.SpectrumRecipeTypes;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.RecipeType;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;

public class AnvilCrushingRecipe implements Recipe<Inventory> {

    protected final Identifier id;
    protected final Ingredient inputIngredient;
    protected final ItemStack outputItemStack;
    protected final float crushedItemsPerPointOfDamage;
    protected final float experience;
    protected final Identifier particleEffect;
    protected final Identifier soundEvent;

    public AnvilCrushingRecipe(Identifier id, Ingredient inputIngredient, ItemStack outputItemStack, float crushedItemsPerPointOfDamage, float experience, Identifier particleEffectIdentifier, Identifier soundEventIdentifier) {
        this.id = id;
        this.inputIngredient = inputIngredient;
        this.outputItemStack = outputItemStack;
        this.crushedItemsPerPointOfDamage = crushedItemsPerPointOfDamage;
        this.experience = experience;
        this.particleEffect = particleEffectIdentifier;
        this.soundEvent = soundEventIdentifier;
    }

    public boolean matches(Inventory inv, World world) {
        return this.inputIngredient.test(inv.getStack(0));
    }

    @Override
    public ItemStack craft(Inventory inv) {
        return null;
    }

    @Override
    public boolean fits(int width, int height) {
        return true;
    }

    @Override
    public ItemStack getOutput() {
        return outputItemStack.copy();
    }

    public boolean isIgnoredInRecipeBook() {
        return true;
    }

    public Identifier getId() {
        return this.id;
    }

    public RecipeSerializer<?> getSerializer() {
        return SpectrumRecipeTypes.ANVIL_CRUSHING_RECIPE_SERIALIZER;
    }

    public RecipeType<?> getType() {
        return SpectrumRecipeTypes.ANVIL_CRUSHING;
    }

    public float getCrushedItemsPerPointOfDamage() {
        return crushedItemsPerPointOfDamage;
    }

    public ItemStack getOutputItemStack() {
        return outputItemStack.copy();
    }

    public SoundEvent getSoundEvent() {
        return Registry.SOUND_EVENT.get(soundEvent);
    }

    public ParticleEffect getParticleEffect() {
        return ParticleTypes.EFFECT; //TODO: use and make customizable
        //return Registry.E.get(particleEffect);
    }

    @Override
    public DefaultedList<Ingredient> getIngredients() {
        DefaultedList<Ingredient> defaultedList = DefaultedList.of();
        defaultedList.add(this.inputIngredient);
        return defaultedList;
    }


    public float getExperience() {
        return experience;
    }
}