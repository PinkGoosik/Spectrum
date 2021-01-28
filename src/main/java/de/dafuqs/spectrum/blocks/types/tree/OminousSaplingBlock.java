package de.dafuqs.spectrum.blocks.types.tree;

import de.dafuqs.spectrum.SpectrumCommon;
import de.dafuqs.spectrum.Support;
import de.dafuqs.spectrum.blocks.SpectrumBlockEntityType;
import net.minecraft.block.Block;
import net.minecraft.block.BlockEntityProvider;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.Random;
import java.util.UUID;

public class OminousSaplingBlock extends Block implements BlockEntityProvider {

    public OminousSaplingBlock(Settings settings) {
        super(settings);
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        if(!world.isClient()) {
            OminousSaplingBlockEntity ominousSaplingBlockEntity = getBlockEntity(world, pos);
            if (ominousSaplingBlockEntity != null) {
                player.sendMessage(Text.of("Sapling UUID: " + ominousSaplingBlockEntity.getPlayerUUID()), false);
            } else {
                player.sendMessage(Text.of("Sapling block entity putt :("), false);
            }
        }

        return ActionResult.SUCCESS;
    }

    public void randomTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
        if (world.getLightLevel(pos.up()) >= 9 && random.nextInt(2) == 0) {
            this.generateOminousTree(world, pos, state, random);
        }
    }

    private void generateOminousTree(ServerWorld world, BlockPos pos, BlockState state, Random random) {
        OminousSaplingBlockEntity ominousSaplingBlockEntity = getBlockEntity(world, pos);
        if(ominousSaplingBlockEntity != null) {
            UUID ownerUUID = ominousSaplingBlockEntity.getPlayerUUID();
            ServerPlayerEntity serverPlayerEntity = SpectrumCommon.minecraftServer.getPlayerManager().getPlayer(ownerUUID);
            if(serverPlayerEntity != null) { // offline?
                Support.grantAdvancementCriterion(serverPlayerEntity, "grow_ominous_sapling","grow");
            }
        }

        // TODO: grow!
    }

    @Override
    public @Nullable BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new OminousSaplingBlockEntity(SpectrumBlockEntityType.OMINOUS_SAPLING_BLOCK_ENTITY_TYPE, pos, state);
    }

    @Override
    public @Nullable <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
        return null;
    }

    /*
    @Override
    public @Nullable <T extends BlockEntity> class_5714 method_32896(World world, T blockEntity) {
        return null;
    }*/

    private OminousSaplingBlockEntity getBlockEntity(World world, BlockPos blockPos) {
        BlockEntity saplingBlockEntity = world.getBlockEntity(blockPos);
        if(saplingBlockEntity instanceof OminousSaplingBlockEntity) {
            return (OminousSaplingBlockEntity) saplingBlockEntity;
        } else {
            return null;
        }
    }

}