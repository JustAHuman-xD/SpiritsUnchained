package me.justahuman.spiritsunchained.implementation.machines;

import io.github.thebusybiscuit.slimefun4.api.items.ItemGroup;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;
import io.github.thebusybiscuit.slimefun4.core.attributes.EnergyNetComponent;
import io.github.thebusybiscuit.slimefun4.core.handlers.BlockBreakHandler;
import io.github.thebusybiscuit.slimefun4.core.networks.energy.EnergyNetComponentType;
import io.github.thebusybiscuit.slimefun4.implementation.Slimefun;
import io.github.thebusybiscuit.slimefun4.implementation.handlers.SimpleBlockBreakHandler;
import io.github.thebusybiscuit.slimefun4.libraries.dough.blocks.BlockPosition;
import io.github.thebusybiscuit.slimefun4.libraries.dough.items.CustomItemStack;
import io.github.thebusybiscuit.slimefun4.libraries.dough.protection.Interaction;
import io.github.thebusybiscuit.slimefun4.utils.ChestMenuUtils;

import me.justahuman.spiritsunchained.spirits.SpiritDefinition;
import me.justahuman.spiritsunchained.utils.ParticleUtils;
import me.justahuman.spiritsunchained.utils.PlayerUtils;
import me.justahuman.spiritsunchained.utils.SpiritUtils;

import me.mrCookieSlime.CSCoreLibPlugin.Configuration.Config;
import me.mrCookieSlime.Slimefun.Objects.handlers.BlockTicker;
import me.mrCookieSlime.Slimefun.api.BlockStorage;
import me.mrCookieSlime.Slimefun.api.inventory.BlockMenu;
import me.mrCookieSlime.Slimefun.api.inventory.BlockMenuPreset;
import me.mrCookieSlime.Slimefun.api.item_transport.ItemTransportFlow;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.HashMap;
import java.util.Map;

public class ElectricSpiritWriter extends SlimefunItem implements EnergyNetComponent {

    private static final int[] BACKGROUND_SLOTS = new int[]{0,1,2,3,4,5,6,7,8,13,22,31,36,37,38,39,40,41,42,43,44};
    private static final int[] BORDER_INPUT = new int[]{9,10,11,12,18,21,27,28,29,30};
    private static final int[] BORDER_OUTPUT = new int[]{14,15,16,17,23,26,32,33,34,35};
    private static final int[] INPUT_SLOTS = new int[]{19,20};
    private static final int[] OUTPUT_SLOTS = new int[]{24,25};
    private static final int PROGRESS_SLOT = 22;

    private static final Map<BlockPosition, Integer> progress = new HashMap<>();

    private static final ItemStack PROGRESS_ITEM = new CustomItemStack(Material.BOOK, "&7Progress: 0%");

    @ParametersAreNonnullByDefault
    public ElectricSpiritWriter(ItemGroup itemGroup, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe) {
        super(itemGroup, item, recipeType, recipe);

        buildPreset();
        addItemHandler(onBreak());
    }

    private void buildPreset() {
        new BlockMenuPreset(this.getId(), ChatColor.GREEN + "Electric Spirit Writer") {
            @Override
            public void init() {
                ChestMenuUtils.drawBackground(this, BACKGROUND_SLOTS);
                SpiritUtils.fillSlots(this, BORDER_INPUT, ChestMenuUtils.getInputSlotTexture());
                SpiritUtils.fillSlots(this, BORDER_OUTPUT, ChestMenuUtils.getOutputSlotTexture());
                this.addItem(PROGRESS_SLOT, PROGRESS_ITEM);
            }

            @Override
            public boolean canOpen(@Nonnull Block b, @Nonnull Player p) {
                return (p.hasPermission("slimefun.inventory.bypass")
                        || Slimefun.getProtectionManager().hasPermission(
                        p, b.getLocation(), Interaction.INTERACT_BLOCK));
            }

            @Override
            public int[] getSlotsAccessedByItemTransport(ItemTransportFlow flow) {
                return flow == ItemTransportFlow.INSERT ? getInputSlots() : getOutputSlots();
            }
        };
    }

    private BlockBreakHandler onBreak() {
        return new SimpleBlockBreakHandler() {

            @Override
            public void onBlockBreak(@Nonnull Block b) {
                final BlockMenu inv = BlockStorage.getInventory(b);

                if (inv != null) {
                    inv.dropItems(b.getLocation(), getInputSlots());
                    inv.dropItems(b.getLocation(), getOutputSlots());
                }

                progress.remove(new BlockPosition(b.getWorld(), b.getX(), b.getY(), b.getZ()));
            }
        };
    }

    @Override
    public void preRegister() {
        addItemHandler(new BlockTicker() {

            @Override
            public void tick(Block b, SlimefunItem sf, Config data) {
                ElectricSpiritWriter.this.tick(b);
            }

            @Override
            public boolean isSynchronized() {
                return true;
            }
        });
    }

    protected void tick(Block b) {

        if (getCharge(b.getLocation()) < getEnergyConsumption()) return;

        final BlockMenu inv = BlockStorage.getInventory(b);
        final BlockPosition pos = new BlockPosition(b.getWorld(), b.getX(), b.getY(), b.getZ());
        int currentProgress = progress.getOrDefault(pos, 0);

        final ItemStack inputSlot1 = inv.getItemInSlot(19);
        final ItemStack inputSlot2 = inv.getItemInSlot(20);

        if (inputSlot1 == null || inputSlot2 == null) {
            return;
        }

        if (!(SpiritUtils.isSpiritItem(inputSlot1) && SlimefunItem.getByItem(inputSlot2) != null && SlimefunItem.getByItem(inputSlot2).getId().equals("SU_SPIRIT_BOOK")) && !(SpiritUtils.isSpiritItem(inputSlot2) && SlimefunItem.getByItem(inputSlot1) != null && SlimefunItem.getByItem(inputSlot1).getId().equals("SU_SPIRIT_BOOK")) ) {
            return;
        }

        if (inv.getItemInSlot(24) != null || inv.getItemInSlot(25) != null) {
            return;
        }

        final ItemStack spiritItem = SpiritUtils.isSpiritItem(inputSlot1) ? inputSlot1 : inputSlot2;
        final ItemStack bookItem = SpiritUtils.isSpiritItem(inputSlot1) ? inputSlot2 : inputSlot1;
        final SpiritDefinition definition = SpiritUtils.getSpiritDefinition(spiritItem);
        final EntityType type = definition.getType();
        final int maxTime = 3 * definition.getTier();

        ParticleUtils.spawnParticleRadius(b.getLocation(), Particle.ENCHANTMENT_TABLE, 1.5, 10, "");

        if (currentProgress < maxTime) {

            progress.put(pos, ++currentProgress);

            ChestMenuUtils.updateProgressbar(inv, PROGRESS_SLOT, maxTime - currentProgress, maxTime, PROGRESS_ITEM);

            removeCharge(b.getLocation(), getEnergyConsumption());
            return;
        }

        inv.pushItem(spiritItem.clone(), getOutputSlots());
        inv.pushItem(SpiritUtils.getFilledBook(definition), getOutputSlots());

        spiritItem.subtract();
        bookItem.subtract();

        for (Player player : b.getLocation().getNearbyPlayers(10)) {
            PlayerUtils.learnKnowledgePiece(player, type, 2);
        }

        progress.put(pos, 0);
        ChestMenuUtils.updateProgressbar(inv, PROGRESS_SLOT, 1, 1, PROGRESS_ITEM);
        b.getWorld().playSound(b.getLocation(), Sound.BLOCK_ENCHANTMENT_TABLE_USE, 1, 1);
    }

    @Nonnull
    @Override
    public EnergyNetComponentType getEnergyComponentType() {
        return EnergyNetComponentType.CONSUMER;
    }

    @Override
    public int getCapacity() {
        return 1000;
    }

    public static int getEnergyConsumption() {
        return 125;
    }

    public int[] getInputSlots() {
        return INPUT_SLOTS;
    }

    public int[] getOutputSlots() {
        return OUTPUT_SLOTS;
    }

}
