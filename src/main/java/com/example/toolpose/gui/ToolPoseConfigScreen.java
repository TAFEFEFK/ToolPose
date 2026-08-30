package com.example.toolpose.gui;

import com.example.toolpose.config.ToolPoseConfig;
import net.minecraft.client.gui.components.AbstractSliderButton;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

public class ToolPoseConfigScreen extends Screen {
    private final ToolPoseConfig config = ToolPoseConfig.getInstance();

    public ToolPoseConfigScreen() {
        super(Component.translatable("screen.toolpose.config"));
    }

    @Override
    protected void init() {
        super.init();
        int centerX = this.width / 2;
        int startY = 40;
        int rowHeight = 22;
        int sliderWidth = 180;

        int leftX = centerX - sliderWidth - 10;
        int rightX = centerX + 10;

        // Right hand sliders
        int y = startY;
        addSlider(rightX + sliderWidth/2, y, "slider.toolpose.offsetX", config.offsetX, -1.0f, 1.0f, v -> { config.offsetX = v; config.save(); });
        y += rowHeight;
        addSlider(rightX + sliderWidth/2, y, "slider.toolpose.offsetY", config.offsetY, -1.0f, 1.0f, v -> { config.offsetY = v; config.save(); });
        y += rowHeight;
        addSlider(rightX + sliderWidth/2, y, "slider.toolpose.offsetZ", config.offsetZ, -2.0f, 2.0f, v -> { config.offsetZ = v; config.save(); });
        y += rowHeight;
        addSlider(rightX + sliderWidth/2, y, "slider.toolpose.rotationX", config.rotationX, -180.0f, 180.0f, v -> { config.rotationX = v; config.save(); });
        y += rowHeight;
        addSlider(rightX + sliderWidth/2, y, "slider.toolpose.rotationY", config.rotationY, -180.0f, 180.0f, v -> { config.rotationY = v; config.save(); });
        y += rowHeight;
        addSlider(rightX + sliderWidth/2, y, "slider.toolpose.rotationZ", config.rotationZ, -180.0f, 180.0f, v -> { config.rotationZ = v; config.save(); });
        y += rowHeight;
        addSlider(rightX + sliderWidth/2, y, "slider.toolpose.swingIntensity", config.swingIntensity, 0.0f, 2.0f, v -> { config.swingIntensity = v; config.save(); });
        y += rowHeight;
        addSlider(rightX + sliderWidth/2, y, "slider.toolpose.scale", config.scale, 0.5f, 2.0f, v -> { config.scale = v; config.save(); });
        y += rowHeight;

        // Left hand sliders
        int yLeft = startY;
        addSlider(leftX + sliderWidth/2, yLeft, "slider.toolpose.offsetX", config.offsetXLeft, -1.0f, 1.0f, v -> { config.offsetXLeft = v; config.save(); });
        yLeft += rowHeight;
        addSlider(leftX + sliderWidth/2, yLeft, "slider.toolpose.offsetY", config.offsetYLeft, -1.0f, 1.0f, v -> { config.offsetYLeft = v; config.save(); });
        yLeft += rowHeight;
        addSlider(leftX + sliderWidth/2, yLeft, "slider.toolpose.offsetZ", config.offsetZLeft, -2.0f, 2.0f, v -> { config.offsetZLeft = v; config.save(); });
        yLeft += rowHeight;
        addSlider(leftX + sliderWidth/2, yLeft, "slider.toolpose.rotationX", config.rotationXLeft, -180.0f, 180.0f, v -> { config.rotationXLeft = v; config.save(); });
        yLeft += rowHeight;
        addSlider(leftX + sliderWidth/2, yLeft, "slider.toolpose.rotationY", config.rotationYLeft, -180.0f, 180.0f, v -> { config.rotationYLeft = v; config.save(); });
        yLeft += rowHeight;
        addSlider(leftX + sliderWidth/2, yLeft, "slider.toolpose.rotationZ", config.rotationZLeft, -180.0f, 180.0f, v -> { config.rotationZLeft = v; config.save(); });
        yLeft += rowHeight;
        addSlider(leftX + sliderWidth/2, yLeft, "slider.toolpose.swingIntensity", config.swingIntensityLeft, 0.0f, 2.0f, v -> { config.swingIntensityLeft = v; config.save(); });
        yLeft += rowHeight;
        addSlider(leftX + sliderWidth/2, yLeft, "slider.toolpose.scale", config.scaleLeft, 0.5f, 2.0f, v -> { config.scaleLeft = v; config.save(); });
        yLeft += rowHeight;

        // Reset button
        this.addRenderableWidget(Button.builder(
                Component.translatable("button.toolpose.reset"),
                btn -> {
                    config.resetToDefaults();
                    this.rebuildWidgets();
                })
                .bounds(this.width - 100, 10, 90, 20)
                .build());

        // Bottom buttons
        int bottomY = this.height - 30;
        int buttonWidth = 130;
        int gap = 10;
        int leftStart = centerX - (buttonWidth + 20 + gap) / 2;

        Button weaponConfigButton = Button.builder(
                Component.translatable("button.toolpose.weaponConfig"),
                btn -> this.minecraft.gui.setScreen(new WeaponConfigScreen(this)))
                .bounds(leftStart, bottomY - 50, buttonWidth, 20)
                .build();
        weaponConfigButton.active = config.useCategoryTransforms;
        this.addRenderableWidget(weaponConfigButton);

        String toggleText = config.useCategoryTransforms ? "✔" : "❌";
        Button toggle = Button.builder(
                Component.literal(toggleText),
                btn -> {
                    config.useCategoryTransforms = !config.useCategoryTransforms;
                    config.save();
                    this.rebuildWidgets();
                })
                .bounds(leftStart + buttonWidth + gap, bottomY - 50, 20, 20)
                .tooltip(Tooltip.create(Component.translatable(config.useCategoryTransforms ? "tooltip.toolpose.disableCategories" : "tooltip.toolpose.enableCategories")))
                .build();
        this.addRenderableWidget(toggle);

        // Randomize button for both hands
        this.addRenderableWidget(Button.builder(
                Component.translatable("button.toolpose.randomize"),
                btn -> {
                    config.randomize();
                    this.rebuildWidgets();
                })
                .bounds(centerX - 75, bottomY - 25, 150, 20)
                .build());

        this.addRenderableWidget(Button.builder(
                Component.translatable("button.toolpose.done"),
                btn -> this.onClose())
                .bounds(centerX - 50, bottomY, 100, 20)
                .build());
    }

    private void addSlider(int centerX, int y, String translationKey, float currentValue, float min, float max, java.util.function.Consumer<Float> setter) {
        double normalized = (currentValue - min) / (max - min);
        AbstractSliderButton slider = new AbstractSliderButton(
            centerX - 90, y, 180, 20,
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
        super.onClose();
    }
}