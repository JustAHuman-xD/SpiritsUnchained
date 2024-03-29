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
import io.github.thebusybiscuit.slimefun4.libraries.dough.data.persistent.PersistentDataAPI;
import io.github.thebusybiscuit.slimefun4.libraries.dough.protection.Interaction;
import io.github.thebusybiscuit.slimefun4.utils.ChestMenuUtils;

import me.justahuman.spiritsunchained.SpiritsUnchained;
import me.justahuman.spiritsunchained.slimefun.ItemStacks;
import me.justahuman.spiritsunchained.utils.Keys;
import me.justahuman.spiritsunchained.utils.ParticleUtils;
import me.justahuman.spiritsunchained.utils.SpiritUtils;

import me.mrCookieSlime.CSCoreLibPlugin.Configuration.Config;
import me.mrCookieSlime.Slimefun.Objects.handlers.BlockTicker;
import me.mrCookieSlime.Slimefun.api.BlockStorage;
import me.mrCookieSlime.Slimefun.api.inventory.BlockMenu;
import me.mrCookieSlime.Slimefun.api.inventory.BlockMenuPreset;
import me.mrCookieSlime.Slimefun.api.item_transport.ItemTransportFlow;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.Vibration;
import org.bukkit.block.Block;
import org.bukkit.entity.Allay;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.HashMap;
import java.util.Map;

public class ElectricSpiritBottler extends SlimefunItem implements EnergyNetComponent {

    private static final int[] BACKGROUND_SLOTS = new int[]{0,1,2,3,4,5,6,7,8,13, 22, 31,36,37,38,39,40,41,42,43,44};
    private static final int[] BORDER_INPUT = new int[]{9,10,11,12,18,21,27,28,29,30};
    private static final int[] BORDER_OUTPUT = new int[]{14,15,16,17,23,26,32,33,34,35};
    private static final int[] INPUT_SLOTS = new int[]{19,20};
    private static final int[] OUTPUT_SLOTS = new int[]{24,25};
    private static final Map<BlockPosition, Boolean> catching = new HashMap<>();

    @ParametersAreNonnullByDefault
    public ElectricSpiritBottler(ItemGroup itemGroup, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe) {
        super(itemGroup, item, recipeType, recipe);

        buildPreset();
        addItemHandler(onBreak());
    }

    private void buildPreset() {
        new BlockMenuPreset(this.getId(), SpiritUtils.getTranslation("names.items.electric_spirit_bottler.name")) {
            @Override
            public void init() {
                ChestMenuUtils.drawBackground(this, BACKGROUND_SLOTS);
                SpiritUtils.fillSlots(this, BORDER_INPUT, ChestMenuUtils.getInputSlotTexture());
                SpiritUtils.fillSlots(this, BORDER_OUTPUT, ChestMenuUtils.getOutputSlotTexture());
            }

            @Override
            public boolean canOpen(@Nonnull Block b, @Nonnull Player p) {
                return (p.hasPermission("slimefun.inventory.bypass")
                        || Slimefun.getProtectionManager().hasPermission(
                        p, b.getLocation(), Interaction.INTERACT_BLOCK));
            }

            @Override
            public int[] getSlotsAccessedByItemTransport(ItemTransportFlow flow) {
                int[] toReturn = new int[]{};
                if (flow == ItemTransportFlow.INSERT) {
                    toReturn = getInputSlots();
                }
                if (flow == ItemTransportFlow.WITHDRAW) {
                    toReturn = getOutputSlots();
                }
                return toReturn;
            }
        };
    }

    private BlockBreakHandler onBreak() {
        return new SimpleBlockBreakHandler() {

            @Override
            public void onBlockBreak(@Nonnull Block b) {
                BlockMenu inv = BlockStorage.getInventory(b);

                if (inv != null) {
                    inv.dropItems(b.getLocation(), getInputSlots());
                    inv.dropItems(b.getLocation(), getOutputSlots());
                }
            }
        };
    }

    @Override
    public void preRegister() {
        addItemHandler(new BlockTicker() {

            @Override
            public void tick(Block b, SlimefunItem sf, Config data) {
                ElectricSpiritBottler.this.tick(b);
            }

            @Override
            public boolean isSynchronized() {
                return true;
            }
        });
    }

    protected void tick(Block b) {
        final Location location = b.getLocation();
        final BlockMenu inv = BlockStorage.getInventory(b);
        final BlockPosition pos = new BlockPosition(b.getWorld(), b.getX(), b.getY(), b.getZ());
        boolean isCatching = catching.getOrDefault(pos, false);

        if (isCatching || getCharge(location) < getCapacity()) {
            return;
        }

        final ItemStack input1 = inv.getItemInSlot(19);
        final ItemStack input2 = inv.getItemInSlot(20);
        if ((input1 == null && input2 == null) || (input1 != null && SlimefunItem.getByItem(input1) != null && input2 != null && SlimefunItem.getByItem(input2) != null)) {
            return;
        }

        if (inv.getItemInSlot(24) != null && inv.getItemInSlot(25) != null) {
            return;
        }

        final ItemStack glassBottle = input1 != null && SlimefunItem.getByItem(input1) == null ? inv.getItemInSlot(19) : inv.getItemInSlot(20);
        final int outputSlot = inv.getItemInSlot(24) == null ? 24 : 25;

        LivingEntity target = null;
        for (LivingEntity livingEntity : SpiritUtils.getNearbySpirits(location, 16)) {
            if (!PersistentDataAPI.hasBoolean(livingEntity, Keys.spiritLocked)) {
                target = livingEntity;
                break;
            }
        }

        if (target == null) {
            return;
        }

        PersistentDataAPI.setBoolean(target, Keys.spiritLocked, true);
        ((Allay) target).setAware(false);
        final Vibration vibration = new Vibration(target.getLocation(), new Vibration.Destination.BlockDestination(location), 20);
        location.getWorld().spawnParticle(Particle.VIBRATION, target.getLocation(), 1, vibration);
        final Vector direction = location.clone().subtract(target.getLocation().clone()).toVector();
        target.setVelocity(direction.normalize().multiply(0.5));

        final ItemStack toReturn = ItemStacks.SU_SPIRIT_BOTTLE.clone();

        removeCharge(location, getEnergyConsumption());
        catching.put(pos, true);

        final LivingEntity finalTarget = target;
        Bukkit.getScheduler().runTaskLater(SpiritsUnchained.getInstance(), () -> {
            ParticleUtils.spawnParticleRadius(location, Particle.SPELL_INSTANT, 1.5, 10, "");
            finalTarget.getWorld().playSound(finalTarget.getLocation(), Sound.BLOCK_AMETHYST_BLOCK_BREAK, 1, 1);
            finalTarget.remove();
            glassBottle.subtract();
            catching.put(pos, false);
            inv.replaceExistingItem(outputSlot, toReturn);
        }, 20L);
    }

    @Nonnull
    @Override
    public EnergyNetComponentType getEnergyComponentType() {
        return EnergyNetComponentType.CONSUMER;
    }

    @Override
    public int getCapacity() {
        return 2000;
    }
    
    public static int getEnergyConsumption() {
        return 100;
    }

    public int[] getInputSlots() {
        return INPUT_SLOTS;
    }

    public int[] getOutputSlots() {
        return OUTPUT_SLOTS;
    }

}
