package de.dafuqs.pigment.worldgen;

import net.minecraft.block.sapling.SaplingGenerator;
import net.minecraft.util.DyeColor;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.TreeFeatureConfig;
import org.jetbrains.annotations.Nullable;

import java.util.Random;

public class ColoredSaplingGenerator extends SaplingGenerator {

    DyeColor dyeColor;
    
    public ColoredSaplingGenerator(DyeColor dyeColor) {
        this.dyeColor = dyeColor;
    }

    @Nullable
    @Override
    protected ConfiguredFeature<TreeFeatureConfig, ?> createTreeFeature(Random random, boolean bl) {
        return getConfiguredFeature(this.dyeColor);
    }
    
    private ConfiguredFeature<TreeFeatureConfig, ?> getConfiguredFeature(DyeColor dyeColor) {
        return PigmentFeatures.COLORED_TREE_FEATURES.get(dyeColor);
    }

}