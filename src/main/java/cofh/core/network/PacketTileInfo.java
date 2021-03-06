package cofh.core.network;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;

public class PacketTileInfo extends PacketCoFHBase {

	public static void initialize() {

		PacketHandler.INSTANCE.registerPacket(PacketTileInfo.class);
	}

	public PacketTileInfo() {

	}

	public PacketTileInfo(TileEntity tile) {

		addInt(tile.getPos().getX());
		addInt(tile.getPos().getY());
		addInt(tile.getPos().getZ());

	}

	@Override
	public void handleClientSide(EntityPlayer player) {

		handlePacket(player, false);
	}

	@Override
	public void handleServerSide(EntityPlayer player) {

		handlePacket(player, true);
	}

	@Override
	public void handlePacket(EntityPlayer player, boolean isServer) {

		TileEntity tile = player.world.getTileEntity(new BlockPos(getInt(), getInt(), getInt()));

		if (tile instanceof ITileInfoPacketHandler) {
			((ITileInfoPacketHandler) tile).handleTileInfoPacket(this, isServer, player);
		} else {
			// TODO: Throw error, bad packet
		}
	}

	public static PacketTileInfo newPacket(TileEntity tile) {

		return new PacketTileInfo(tile);
	}

}
