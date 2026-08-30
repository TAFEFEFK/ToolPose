package com.example.toolpose.gui;

import com.example.toolpose.config.ToolPoseConfig;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

public class WeaponConfigScreen extends Screen {
    private final Screen parent;
    private final ToolPoseConfig config = ToolPoseConfig.getInstance();
    private boolean isLeftHand = false; // false = right, true = left

    private static final String[] CATEGORIES = {
        "sword", "axe", "pickaxe", "shovel", "hoe",
        "food", "block", "totem", "shield", "bow", "crossbow"
    };

    public WeaponConfigScreen(Screen parent) {
        super(Component.translatable("screen.toolpose.weaponConfig"));
        this.parent = parent;
    }

    @Override
    protected void init() {
        super.init();
        int centerX = this.width / 2;
        int startY = 40;
        int rowHeight = 25;
        int colWidth = 160;

        int leftX = centerX - colWidth - 10;
        int rightX = centerX + 10;

        // Hand switch button
        this.addRenderableWidget(Button.builder(
                Component.translatable(isLeftHand ? "button.toolpose.leftHand" : "button.toolpose.rightHand"),
                btn -> {
                    isLeftHand = !isLeftHand;
                    this.rebuildWidgets();
                })
                .bounds(centerX - 50, 15, 100, 20)
                .build());

        // Randomize button for current hand
        this.addRenderableWidget(Button.builder(
                Component.translatable("button.toolpose.randomize"),
                btn -> {
                    config.randomizeHand(isLeftHand);
                    this.rebuildWidgets();
                })
                .bounds(centerX + 60, 15, 90, 20)
                .build());

        for (int i = 0; i < CATEGORIES.length; i++) {
            String cat = CATEGORIES[i];
            int col = i < 6 ? 0 : 1;
            int x = col == 0 ? leftX : rightX;
            int y = startY + (i % 6) * rowHeight;

            ToolPoseConfig.Transform transform = getTransform(cat);
            boolean enabled = transform != null && transform.enabled;

            Button categoryButton = Button.builder(
                    Component.translatable("category.toolpose." + cat),
                    btn -> this.minecraft.gui.setScreen(new CategoryConfigScreen(this, cat, isLeftHand)))
                    .bounds(x, y, colWidth - 25, 20)
                    .build();
            categoryButton.active = enabled;
            this.addRenderableWidget(categoryButton);

            this.addRenderableWidget(Button.builder(
                    Component.literal(enabled ? "✔" : "❌"),
                    btn -> {
                        if (transform != null) {
                            transform.enabled = !transform.enabled;
                            config.save();
                            this.rebuildWidgets();
                        }
                    })
                    .bounds(x + colWidth - 20, y, 20, 20)
                    .tooltip(Tooltip.create(Component.translatable(enabled ? "tooltip.toolpose.disableCategory" : "tooltip.toolpose.enableCategory")))
                    .build());
        }

        this.addRenderableWidget(Button.builder(
                Component.translatable("button.toolpose.back"),
                btn -> this.onClose())
                .bounds(centerX - 50, this.height - 30, 100, 20)
                .build());
    }

    private ToolPoseConfig.Transform getTransform(String category) {
        boolean left = isLeftHand;
        switch (category) {
            case "sword": return left ? config.swordLeft : config.sword;
            case "axe": return left ? config.axeLeft : config.axe;
            case "pickaxe": return left ? config.pickaxeLeft : config.pickaxe;
            case "shovel": return left ? config.shovelLeft : config.shovel;
            case "hoe": return left ? config.hoeLeft : config.hoe;
            case "food": return left ? config.foodLeft : config.food;
            case "block": return left ? config.blockLeft : config.block;
            case "totem": return left ? config.totemLeft : config.totem;
            case "shield": return left ? config.shieldLeft : config.shield;
            case "bow": return left ? config.bowLeft : config.bow;
            case "crossbow": return left ? config.crossbowLeft : config.crossbow;
            default: return null;
        }
    }

    @Override
    public void onClose() {
        this.minecraft.gui.setScreen(parent);
    }
}