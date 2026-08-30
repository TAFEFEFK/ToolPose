package com.example.toolpose.gui;

import com.example.toolpose.config.ToolPoseConfig;
import net.minecraft.client.gui.components.AbstractSliderButton;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

public class CategoryConfigScreen extends Screen {
    private final Screen parent;
    private final String category;
    private final boolean isLeftHand;
    private final ToolPoseConfig config = ToolPoseConfig.getInstance();

    public CategoryConfigScreen(Screen parent, String category, boolean isLeftHand) {
        super(Component.translatable("screen.toolpose.categoryConfig", category));
        this.parent = parent;
        this.category = category;
        this.isLeftHand = isLeftHand;
    }

    private ToolPoseConfig.Transform getTransform() {
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
    protected void init() {
        super.init();
        ToolPoseConfig.Transform t = getTransform();
        if (t == null) return;

        int centerX = this.width / 2;
        int startY = 40;
        int rowHeight = 25;
        int sliderWidth = 200;

        addSlider(centerX, startY, "slider.toolpose.offsetX", t.offsetX, -1.0f, 1.0f, v -> { t.offsetX = v; config.save(); });
        addSlider(centerX, startY + rowHeight, "slider.toolpose.offsetY", t.offsetY, -1.0f, 1.0f, v -> { t.offsetY = v; config.save(); });
        addSlider(centerX, startY + rowHeight*2, "slider.toolpose.offsetZ", t.offsetZ, -2.0f, 2.0f, v -> { t.offsetZ = v; config.save(); });
        addSlider(centerX, startY + rowHeight*3, "slider.toolpose.rotationX", t.rotationX, -180.0f, 180.0f, v -> { t.rotationX = v; config.save(); });
        addSlider(centerX, startY + rowHeight*4, "slider.toolpose.rotationY", t.rotationY, -180.0f, 180.0f, v -> { t.rotationY = v; config.save(); });
        addSlider(centerX, startY + rowHeight*5, "slider.toolpose.rotationZ", t.rotationZ, -180.0f, 180.0f, v -> { t.rotationZ = v; config.save(); });
        addSlider(centerX, startY + rowHeight*6, "slider.toolpose.swingIntensity", t.swingIntensity, 0.0f, 2.0f, v -> { t.swingIntensity = v; config.save(); });
        addSlider(centerX, startY + rowHeight*7, "slider.toolpose.scale", t.scale, 0.5f, 2.0f, v -> { t.scale = v; config.save(); });

        this.addRenderableWidget(Button.builder(
                Component.translatable("button.toolpose.done"),
                btn -> this.onClose())
                .bounds(centerX - 50, this.height - 30, 100, 20)
                .build());
    }

    private void addSlider(int centerX, int y, String translationKey, float currentValue, float min, float max, java.util.function.Consumer<Float> setter) {
        double normalized = (currentValue - min) / (max - min);
        AbstractSliderButton slider = new AbstractSliderButton(
            centerX - 100, y, 200, 20,
            Component.translatable(translationKey, String.format("%.2f", currentValue)),
            normalized
        ) {
            @Override
            protected void updateMessage() {
                float val = (float)(min + (max - min) * this.value);
                setMessage(Component.translatable(translationKey, String.format("%.2f", val)));
            }

            @Override
            protected void applyValue() {
                float val = (float)(min + (max - min) * this.value);
                setter.accept(val);
            }
        };
        this.addRenderableWidget(slider);
    }

    @Override
    public void onClose() {
        config.save();
        this.minecraft.gui.setScreen(parent);
    }
}