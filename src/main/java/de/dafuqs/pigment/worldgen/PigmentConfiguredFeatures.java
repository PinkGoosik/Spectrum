package de.dafuqs.pigment.worldgen;

import com.google.common.collect.ImmutableList;
import de.dafuqs.pigment.registries.PigmentBlocks;
import de.dafuqs.pigment.PigmentCommon;
import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.structure.rule.BlockMatchRuleTest;
import net.minecraft.structure.rule.RuleTest;
import net.minecraft.util.DyeColor;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.BuiltinRegistries;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.gen.GenerationStep;
import net.minecraft.world.gen.UniformIntDistribution;
import net.minecraft.world.gen.YOffset;
import net.minecraft.world.gen.decorator.Decorator;
import net.minecraft.world.gen.decorator.DecoratorConfig;
import net.minecraft.world.gen.feature.*;
import net.minecraft.world.gen.feature.size.TwoLayersFeatureSize;
import net.minecraft.world.gen.foliage.BlobFoliagePlacer;
import net.minecraft.world.gen.stateprovider.SimpleBlockStateProvider;
import net.minecraft.world.gen.trunk.StraightTrunkPlacer;

import java.util.*;

import static net.minecraft.world.gen.feature.ConfiguredFeatures.OAK;

public class PigmentConfiguredFeatures {

    public static ConfiguredFeature<?, ?> CITRINE_GEODE;
    public static ConfiguredFeature<?, ?> TOPAZ_GEODE;
    public static ConfiguredFeature<?, ?> MOONSTONE_GEODE;

    // COLORED TREES
    public static HashMap<DyeColor, ConfiguredFeature<TreeFeatureConfig, ?>> COLORED_TREE_FEATURES = new HashMap<>(); // FOR SAPLINGS
    public static ConfiguredFeature<?, ?> DECORATED_TREES; // FOR WORLD GEN

    private static ConfiguredFeature<?, ?> SPARKLESTONE_ORE;
    private static ConfiguredFeature<?, ?> KOENIGSBLAU_ORE;
    private static ConfiguredFeature<?, ?> PALETUR_ORE;
    private static ConfiguredFeature<?, ?> CRIMSON_ORE;

    private static ConfiguredFeature<?, ?> QUITOXIC_REEDS;
    private static ConfiguredFeature<?, ?> MERMAIDS_BRUSH;

    public static void register() {
        registerGeodes();
        registerOres();
        registerColoredTrees();
        registerPlants();
    }

    private static <FC extends FeatureConfig> ConfiguredFeature<FC, ?> registerConfiguredFeature(Identifier identifier, ConfiguredFeature<FC, ?> configuredFeature) {
        return Registry.register(BuiltinRegistries.CONFIGURED_FEATURE, identifier, configuredFeature);
    }

    private static void registerOres() {
        BlockState sparklestoneOre = PigmentBlocks.SPARKLESTONE_ORE.getDefaultState();
        BlockState koenigsblauOre = PigmentBlocks.KOENIGSBLAU_ORE.getDefaultState();
        BlockState crimsonOre = PigmentBlocks.CRIMSON_ORE.getDefaultState();
        BlockState paleturOre = PigmentBlocks.PALETUR_ORE.getDefaultState();

        Identifier sparklestoneOreIdentifier = new Identifier(PigmentCommon.MOD_ID, "sparklestone_ore");
        Identifier koenigsblauOreIdentifier = new Identifier(PigmentCommon.MOD_ID, "koenigsblau_ore");
        Identifier crimsonOreIdentifier = new Identifier(PigmentCommon.MOD_ID, "crimson_ore");
        Identifier paleturOreIdentifier = new Identifier(PigmentCommon.MOD_ID, "paletur_ore");

        SPARKLESTONE_ORE = registerConfiguredFeature(sparklestoneOreIdentifier,
        Feature.ORE.configure(new OreFeatureConfig(OreFeatureConfig.Rules.BASE_STONE_OVERWORLD, sparklestoneOre, 17)) // vein size
                .rangeOf(YOffset.fixed(92), YOffset.fixed(192)) // min and max height
                .spreadHorizontally()
                .repeat(6)); // number of veins per chunk

        KOENIGSBLAU_ORE = registerConfiguredFeature(koenigsblauOreIdentifier,
                Feature.ORE.configure(new OreFeatureConfig(OreFeatureConfig.Rules.BASE_STONE_OVERWORLD, koenigsblauOre, 5)) // vein size
                        .rangeOf(YOffset.getBottom(), YOffset.aboveBottom(64)) // min and max height
                        .spreadHorizontally()
                        .repeat(4)); // number of veins per chunk

        CRIMSON_ORE = registerConfiguredFeature(crimsonOreIdentifier,
                Feature.ORE.configure(new OreFeatureConfig(OreFeatureConfig.Rules.BASE_STONE_NETHER, crimsonOre, 6)) // vein size
                        .range(ConfiguredFeatures.Decorators.BOTTOM_TO_TOP_OFFSET_10) // min and max height
                        .spreadHorizontally()
                        .repeat(8)); // number of veins per chunk

        PALETUR_ORE = registerConfiguredFeature(paleturOreIdentifier,
                Feature.ORE.configure(new OreFeatureConfig(Rules.END_STONE, paleturOre, 4, 0.3F)) // vein size + discard on air exposure
                        .rangeOf(YOffset.getBottom(), YOffset.aboveBottom(80)) // min and max height
                        .spreadHorizontally()
                        .repeat(6)); // number of veins per chunk

        BiomeModifications.addFeature(BiomeSelectors.foundInOverworld(), GenerationStep.Feature.UNDERGROUND_ORES, RegistryKey.of(Registry.CONFIGURED_FEATURE_WORLDGEN, sparklestoneOreIdentifier));
        BiomeModifications.addFeature(BiomeSelectors.foundInOverworld(), GenerationStep.Feature.UNDERGROUND_ORES, RegistryKey.of(Registry.CONFIGURED_FEATURE_WORLDGEN, koenigsblauOreIdentifier));
        BiomeModifications.addFeature(BiomeSelectors.foundInTheNether(), GenerationStep.Feature.UNDERGROUND_ORES, RegistryKey.of(Registry.CONFIGURED_FEATURE_WORLDGEN, crimsonOreIdentifier));
        BiomeModifications.addFeature(BiomeSelectors.foundInTheEnd(), GenerationStep.Feature.UNDERGROUND_ORES, RegistryKey.of(Registry.CONFIGURED_FEATURE_WORLDGEN, paleturOreIdentifier));
    }

    private static void registerColoredTree(DyeColor dyeColor) {
        String identifierString = dyeColor.toString() + "_tree";
        RegistryKey<ConfiguredFeature<?, ?>> configuredFeatureRegistryKey = RegistryKey.of(Registry.CONFIGURED_FEATURE_WORLDGEN, new Identifier(PigmentCommon.MOD_ID, identifierString));
        //RegistryKey<ConfiguredFeature<?, ?>> decoratedFeatureRegistryKey = RegistryKey.of(Registry.CONFIGURED_FEATURE_WORLDGEN, new Identifier(PigmentCommon.MOD_ID, identifierString + "_decorated"));

        // TODO: add custom tree feature config (currently generates exactly like oak)
        // how the colored tree will look when generated
        ConfiguredFeature<TreeFeatureConfig, ?> configuredFeature = Feature.TREE.configure(
                (new TreeFeatureConfig.Builder(
                        new SimpleBlockStateProvider(PigmentBlocks.getColoredLogBlock(dyeColor).getDefaultState()),
                        new SimpleBlockStateProvider(PigmentBlocks.getColoredLeavesBlock(dyeColor).getDefaultState()),
                        new BlobFoliagePlacer(UniformIntDistribution.of(2), UniformIntDistribution.of(0), 3),
                        new StraightTrunkPlacer(4, 2, 2), // 4-8 height
                        new TwoLayersFeatureSize(1, 0, 1)))
                        .ignoreVines().build());

        COLORED_TREE_FEATURES.put(dyeColor, Registry.register(BuiltinRegistries.CONFIGURED_FEATURE, configuredFeatureRegistryKey.getValue(), configuredFeature));
    }

    private static void registerColoredTrees() {
        for(DyeColor dyeColor : DyeColor.values()) {
            registerColoredTree(dyeColor);
        }

        ConfiguredFeature<?, ?> configuredFeature = Feature.RANDOM_SELECTOR.configure(
                new RandomFeatureConfig(ImmutableList.of(
                        COLORED_TREE_FEATURES.get(DyeColor.BLACK).withChance(0.025F),
                        COLORED_TREE_FEATURES.get(DyeColor.BLUE).withChance(0.025F),
                        COLORED_TREE_FEATURES.get(DyeColor.BROWN).withChance(0.025F),
                        COLORED_TREE_FEATURES.get(DyeColor.CYAN).withChance(0.025F),
                        COLORED_TREE_FEATURES.get(DyeColor.GRAY).withChance(0.025F),
                        COLORED_TREE_FEATURES.get(DyeColor.GREEN).withChance(0.025F),
                        COLORED_TREE_FEATURES.get(DyeColor.LIGHT_BLUE).withChance(0.025F),
                        COLORED_TREE_FEATURES.get(DyeColor.LIGHT_GRAY).withChance(0.025F),
                        COLORED_TREE_FEATURES.get(DyeColor.LIME).withChance(0.025F),
                        COLORED_TREE_FEATURES.get(DyeColor.MAGENTA).withChance(0.025F),
                        COLORED_TREE_FEATURES.get(DyeColor.ORANGE).withChance(0.025F),
                        COLORED_TREE_FEATURES.get(DyeColor.PINK).withChance(0.025F),
                        COLORED_TREE_FEATURES.get(DyeColor.PURPLE).withChance(0.025F),
                        COLORED_TREE_FEATURES.get(DyeColor.RED).withChance(0.025F),
                        COLORED_TREE_FEATURES.get(DyeColor.WHITE).withChance(0.025F),
                        COLORED_TREE_FEATURES.get(DyeColor.YELLOW).withChance(0.025F)
                        ), OAK)).decorate(Decorator.DARK_OAK_TREE.configure(DecoratorConfig.DEFAULT).applyChance(20));

        RegistryKey<ConfiguredFeature<?, ?>> decoratedFeatureRegistryKey = RegistryKey.of(Registry.CONFIGURED_FEATURE_WORLDGEN, new Identifier(PigmentCommon.MOD_ID, "random_colored_trees"));
        Registry.register(BuiltinRegistries.CONFIGURED_FEATURE, decoratedFeatureRegistryKey.getValue(), configuredFeature);

        // Add generation to world
        BiomeModifications.addFeature(BiomeSelectors.foundInOverworld(), GenerationStep.Feature.VEGETAL_DECORATION, decoratedFeatureRegistryKey);
    }

    private static void registerGeodes() {
        BlockState AIR = Blocks.AIR.getDefaultState();
        BlockState CALCITE = Blocks.CALCITE.getDefaultState();
        BlockState TUFF = Blocks.TUFF.getDefaultState();

        BlockState CITRINE_BLOCK = PigmentBlocks.CITRINE_BLOCK.getDefaultState();
        BlockState BUDDING_CITRINE = PigmentBlocks.BUDDING_CITRINE.getDefaultState();
        BlockState SMALL_CITRINE_BUD = PigmentBlocks.SMALL_CITRINE_BUD.getDefaultState();
        BlockState MEDIUM_CITRINE_BUD = PigmentBlocks.MEDIUM_CITRINE_BUD.getDefaultState();
        BlockState LARGE_CITRINE_BUD = PigmentBlocks.LARGE_CITRINE_BUD.getDefaultState();
        BlockState CITRINE_CLUSTER = PigmentBlocks.CITRINE_CLUSTER.getDefaultState();

        BlockState TOPAZ_BLOCK = PigmentBlocks.TOPAZ_BLOCK.getDefaultState();
        BlockState BUDDING_TOPAZ = PigmentBlocks.BUDDING_TOPAZ.getDefaultState();
        BlockState SMALL_TOPAZ_BUD = PigmentBlocks.SMALL_TOPAZ_BUD.getDefaultState();
        BlockState MEDIUM_TOPAZ_BUD = PigmentBlocks.MEDIUM_TOPAZ_BUD.getDefaultState();
        BlockState LARGE_TOPAZ_BUD = PigmentBlocks.LARGE_TOPAZ_BUD.getDefaultState();
        BlockState TOPAZ_CLUSTER = PigmentBlocks.TOPAZ_CLUSTER.getDefaultState();

        BlockState MOONSTONE_BLOCK = PigmentBlocks.MOONSTONE_BLOCK.getDefaultState();
        BlockState BUDDING_MOONSTONE = PigmentBlocks.BUDDING_MOONSTONE.getDefaultState();
        BlockState SMALL_MOONSTONE_BUD = PigmentBlocks.SMALL_MOONSTONE_BUD.getDefaultState();
        BlockState MEDIUM_MOONSTONE_BUD = PigmentBlocks.MEDIUM_MOONSTONE_BUD.getDefaultState();
        BlockState LARGE_MOONSTONE_BUD = PigmentBlocks.LARGE_MOONSTONE_BUD.getDefaultState();
        BlockState MOONSTONE_CLUSTER = PigmentBlocks.MOONSTONE_CLUSTER.getDefaultState();

        CITRINE_GEODE = (PigmentFeatures.SOLID_BLOCKS_GEODE.configure(new GeodeFeatureConfig(
                new GeodeLayerConfig(
                        new SimpleBlockStateProvider(AIR),
                        new SimpleBlockStateProvider(CITRINE_BLOCK),
                        new SimpleBlockStateProvider(BUDDING_CITRINE),
                        new SimpleBlockStateProvider(CALCITE),
                        new SimpleBlockStateProvider(TUFF),
                        ImmutableList.of(SMALL_CITRINE_BUD, MEDIUM_CITRINE_BUD, LARGE_CITRINE_BUD, CITRINE_CLUSTER)),
                new GeodeLayerThicknessConfig(1.7D, 2.2D, 3.2D, 4.2D),
                new GeodeCrackConfig(0.95D, 2.0D, 2), 0.35D, 0.083D, true, 4, 7, 3, 5, 1, 3, -16, 16, 0.05D, 1))
                .rangeOf(YOffset.aboveBottom(32), YOffset.aboveBottom(96))
                .spreadHorizontally()
                .applyChance(30));

        TOPAZ_GEODE = (PigmentFeatures.SOLID_BLOCKS_GEODE.configure(new GeodeFeatureConfig(
                new GeodeLayerConfig(
                        new SimpleBlockStateProvider(AIR),
                        new SimpleBlockStateProvider(TOPAZ_BLOCK),
                        new SimpleBlockStateProvider(BUDDING_TOPAZ),
                        new SimpleBlockStateProvider(CALCITE),
                        new SimpleBlockStateProvider(TUFF),
                        ImmutableList.of(SMALL_TOPAZ_BUD, MEDIUM_TOPAZ_BUD, LARGE_TOPAZ_BUD, TOPAZ_CLUSTER)),
                new GeodeLayerThicknessConfig(1.7D, 2.2D, 3.2D, 4.2D),
                new GeodeCrackConfig(0.95D, 2.0D, 2), 0.35D, 0.083D, true, 4, 7, 3, 5, 1, 3, -16, 16, 0.05D, 1))
                .rangeOf(YOffset.fixed(64), YOffset.fixed(128))
                .spreadHorizontally()
                .applyChance(30));

        MOONSTONE_GEODE = (Feature.GEODE.configure(new GeodeFeatureConfig(
                new GeodeLayerConfig(
                        new SimpleBlockStateProvider(AIR),
                        new SimpleBlockStateProvider(MOONSTONE_BLOCK),
                        new SimpleBlockStateProvider(BUDDING_MOONSTONE),
                        new SimpleBlockStateProvider(CALCITE),
                        new SimpleBlockStateProvider(TUFF),
                        ImmutableList.of(SMALL_MOONSTONE_BUD, MEDIUM_MOONSTONE_BUD, LARGE_MOONSTONE_BUD, MOONSTONE_CLUSTER)),
                new GeodeLayerThicknessConfig(1.7D, 2.2D, 3.2D, 4.2D),
                new GeodeCrackConfig(0.95D, 2.0D, 2), 0.35D, 0.083D, true, 4, 7, 3, 5, 1, 3, -16, 16, 0.05D, 1))
                .rangeOf(YOffset.aboveBottom(10), YOffset.belowTop(10))
                .spreadHorizontally()
                .applyChance(30));

        RegistryKey<ConfiguredFeature<?, ?>> CITRINE_GEODE_KEY = RegistryKey.of(Registry.CONFIGURED_FEATURE_WORLDGEN, new Identifier(PigmentCommon.MOD_ID, "citrine_geode"));
        Registry.register(BuiltinRegistries.CONFIGURED_FEATURE, CITRINE_GEODE_KEY.getValue(), CITRINE_GEODE);
        BiomeModifications.addFeature(BiomeSelectors.foundInOverworld(), GenerationStep.Feature.UNDERGROUND_STRUCTURES, CITRINE_GEODE_KEY);

        RegistryKey<ConfiguredFeature<?, ?>> TOPAZ_GEODE_KEY = RegistryKey.of(Registry.CONFIGURED_FEATURE_WORLDGEN, new Identifier(PigmentCommon.MOD_ID, "topaz_geode"));
        Registry.register(BuiltinRegistries.CONFIGURED_FEATURE, TOPAZ_GEODE_KEY.getValue(), TOPAZ_GEODE);
        BiomeModifications.addFeature(BiomeSelectors.foundInOverworld(), GenerationStep.Feature.UNDERGROUND_STRUCTURES, TOPAZ_GEODE_KEY);

        RegistryKey<ConfiguredFeature<?, ?>> MOONSTONE_GEODE_KEY = RegistryKey.of(Registry.CONFIGURED_FEATURE_WORLDGEN, new Identifier(PigmentCommon.MOD_ID, "moonstone_geode"));
        Registry.register(BuiltinRegistries.CONFIGURED_FEATURE, MOONSTONE_GEODE_KEY.getValue(), MOONSTONE_GEODE);
    }

    private static void registerPlants() {
        // TODO: Does not work (always tries to place stuff in air or under y-64)??
        /*// MERMAIDS BRUSH
        Identifier mermaidsBrushIdentifier = new Identifier(PigmentCommon.MOD_ID, "mermaids_brush");

        ConfiguredFeature mermaidsBrushFeature = Feature.RANDOM_PATCH.configure((
                new RandomPatchFeatureConfig.Builder(
                        new SimpleBlockStateProvider(PigmentBlocks.MERMAIDS_BRUSH.getDefaultState()),
                        SimpleBlockPlacer.INSTANCE))
                .tries(64)
                .cannotProject().build());

        MERMAIDS_BRUSH = registerConfiguredFeature(mermaidsBrushIdentifier, mermaidsBrushFeature)
                .decorate(Decorator.CARVING_MASK.configure(new CarvingMaskDecoratorConfig(GenerationStep.Carver.LIQUID)));

        Collection<RegistryKey<Biome>> deepOceans = new ArrayList<>();
        deepOceans.add(BiomeKeys.DEEP_OCEAN);
        deepOceans.add(BiomeKeys.DEEP_COLD_OCEAN);
        deepOceans.add(BiomeKeys.DEEP_FROZEN_OCEAN);
        deepOceans.add(BiomeKeys.DEEP_WARM_OCEAN);
        deepOceans.add(BiomeKeys.DEEP_LUKEWARM_OCEAN);
        BiomeModifications.addFeature(
                BiomeSelectors.includeByKey(deepOceans),
                GenerationStep.Feature.VEGETAL_DECORATION,
                RegistryKey.of(Registry.CONFIGURED_FEATURE_WORLDGEN, mermaidsBrushIdentifier)
        );

        // QUITOXIC REEDS
        Identifier quitoxicReedsIdentifier = new Identifier(PigmentCommon.MOD_ID, "quitoxic_reeds");
        HashSet<Block> quitoxicReedsWhiteList = new HashSet();
        quitoxicReedsWhiteList.add(Blocks.WATER);
        quitoxicReedsWhiteList.add(Blocks.CLAY);
        RandomPatchFeatureConfig QUITOXIC_REEDS_CONFIG = (
                new RandomPatchFeatureConfig.Builder(
                        new SimpleBlockStateProvider(PigmentBlocks.QUITOXIC_REEDS.getDefaultState()),
                        new ColumnPlacer(2, 4)))
                .tries(20)
                .whitelist(quitoxicReedsWhiteList)
                .spreadX(4)
                .spreadY(0)
                .spreadZ(4)
                .build();

        QUITOXIC_REEDS = registerConfiguredFeature(quitoxicReedsIdentifier,
                (ConfiguredFeature)Feature.RANDOM_PATCH
                        .configure(QUITOXIC_REEDS_CONFIG)
                        .decorate(ConfiguredFeatures.Decorators.SQUARE_HEIGHTMAP_SPREAD_DOUBLE)
                        .repeat(20));

        Collection<RegistryKey<Biome>> swamps = new ArrayList<>();
        swamps.add(BiomeKeys.SWAMP);
        swamps.add(BiomeKeys.SWAMP_HILLS);
        BiomeModifications.addFeature(
                BiomeSelectors.includeByKey(swamps),
                GenerationStep.Feature.VEGETAL_DECORATION,
                RegistryKey.of(Registry.CONFIGURED_FEATURE_WORLDGEN, quitoxicReedsIdentifier)
        );*/
    }

    public static final class Rules {
        public static final RuleTest END_STONE;

        static {
            END_STONE = new BlockMatchRuleTest(Blocks.END_STONE);
        }
    }

}