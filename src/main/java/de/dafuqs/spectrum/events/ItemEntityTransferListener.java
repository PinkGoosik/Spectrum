package de.dafuqs.spectrum.events;

import blue.endless.jankson.annotation.Nullable;
import de.dafuqs.spectrum.networking.SpectrumS2CPackets;
import de.dafuqs.spectrum.particle.effect.ItemTransfer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;
import net.minecraft.world.event.PositionSource;
import net.minecraft.world.event.listener.GameEventListener;

import java.util.Optional;

public class ItemEntityTransferListener implements GameEventListener {

	protected final PositionSource positionSource;
	protected final int range;
	protected final ItemEntityTransferListener.Callback callback;
	protected Optional<GameEvent> event = Optional.empty();
	protected int distance;
	protected int delay = 0;
	protected ItemEntity itemEntity;

	public ItemEntityTransferListener(PositionSource positionSource, int range, ItemEntityTransferListener.Callback listener) {
		this.positionSource = positionSource;
		this.range = range;
		this.callback = listener;
	}

	public void tick(World world) {
		if (this.event.isPresent()) {
			--this.delay;
			if (this.delay <= 0) {
				this.delay = 0;
				this.callback.accept(world, this, this.event.get(), this.distance);
				this.event = Optional.empty();
			}
		}
	}

	public PositionSource getPositionSource() {
		return this.positionSource;
	}

	public int getRange() {
		return this.range;
	}

	public boolean listen(World world, GameEvent event, @Nullable Entity entity, BlockPos pos) {
		if (!this.shouldActivate(event, entity)) {
			return false;
		} else {
			Optional<BlockPos> optional = this.positionSource.getPos(world);
			if (!optional.isPresent()) {
				return false;
			} else {
				itemEntity = (ItemEntity) entity;
				BlockPos blockPos = optional.get();
				if (!this.callback.accepts(world, this, pos, event, entity)) {
					return false;
				} else {
					this.listen(world, event, pos, blockPos);
					return true;
				}
			}
		}
	}

	boolean shouldActivate(GameEvent event, @Nullable Entity entity) {
		if (this.event.isEmpty()) {
			if(entity instanceof ItemEntity itemEntity) {
				return itemEntity.isAlive() && !itemEntity.getStack().isEmpty();
			}
		}
		return false;
	}

	private void listen(World world, GameEvent event, BlockPos pos, BlockPos sourcePos) {
		this.event = Optional.of(event);
		if (world instanceof ServerWorld) {
			this.distance = MathHelper.floor(Math.sqrt(pos.getSquaredDistance(sourcePos, false)));
			this.delay = this.distance;
			SpectrumS2CPackets.sendItemTransferPacket((ServerWorld) world, new ItemTransfer(pos, this.positionSource, this.delay));
		}
	}

	public ItemEntity getItemEntity() {
		return this.itemEntity;
	}

	public interface Callback {
		/**
		 * Returns whether the callback wants to accept this event.
		 */
		boolean accepts(World world, GameEventListener listener, BlockPos pos, GameEvent event, @Nullable Entity entity);

		/**
		 * Accepts a game event after delay.
		 */
		void accept(World world, GameEventListener listener, GameEvent event, int distance);
	}

}