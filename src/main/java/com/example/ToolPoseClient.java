package com.example;

import com.example.toolpose.config.ToolPoseConfig;
import com.example.toolpose.gui.ToolPoseConfigScreen;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keymapping.v1.KeyMappingHelper;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.Identifier;
import com.mojang.blaze3d.platform.InputConstants;
import org.lwjgl.glfw.GLFW;

public class ToolPoseClient implements ClientModInitializer {
    private static KeyMapping configKey;
    private static final KeyMapping.Category TOOLPOSE_CATEGORY = new KeyMapping.Category(
        Identifier.fromNamespaceAndPath("toolpose", "category")
    );

    @Override
    public void onInitializeClient() {
        ToolPoseConfig.getInstance();

        configKey = KeyMappingHelper.registerKeyMapping(new KeyMapping(
                "key.toolpose.config",
                InputConstants.Type.KEYSYM,
                GLFW.GLFW_KEY_K,
                TOOLPOSE_CATEGORY
        ));

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (configKey.consumeClick()) {
                if (client.gui.screen() == null) {
                    client.gui.setScreen(new ToolPoseConfigScreen());
                }
            }
        });
    }
}