package net.darkhax.gamestages.packet;

import net.darkhax.bookshelf.network.SerializableMessage;
import net.darkhax.bookshelf.util.PlayerUtils;
import net.darkhax.gamestages.capabilities.PlayerDataHandler;
import net.darkhax.gamestages.capabilities.PlayerDataHandler.IStageData;
import net.darkhax.gamestages.event.GameStageEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * A packet for syncing a stage add/remove action with the client. It uses
 * {@code net.darkhax.bookshelf.network.SerializableMessage} to handle the IO automagically.
 */
public class PacketStage extends SerializableMessage {

    /**
     * The serial ID. Please update this when fields are added/removed, or other big changes
     * happen. Not that important though.
     */
    private static final long serialVersionUID = 333083516838356520L;

    /**
     * The name of the stage to modify.
     */
    public String stageName;

    /**
     * Whether or not the stage is being added or removed.
     */
    public boolean unlock;

    /**
     * Empty constructor, required by forge's system.
     */
    public PacketStage () {

    }

    /**
     * Constructor for the packet.
     *
     * @param stageName The name of the stage to modify.
     * @param unlock Whether or not the stage is being added or removed.
     */
    public PacketStage (String stageName, boolean unlock) {

        this.stageName = stageName.toLowerCase();
        this.unlock = unlock;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IMessage handleMessage (MessageContext context) {

        Minecraft.getMinecraft().addScheduledTask(() -> {
            
            final EntityPlayer player = PlayerUtils.getClientPlayer();
            final IStageData info = PlayerDataHandler.getStageData(player);

            if (this.unlock) {
                info.unlockStage(this.stageName);
            }
            else {
                info.lockStage(this.stageName);
            }

            MinecraftForge.EVENT_BUS.post(new GameStageEvent.ClientSync(player, this.stageName, this.unlock));
        });

        return null;
    }
}
