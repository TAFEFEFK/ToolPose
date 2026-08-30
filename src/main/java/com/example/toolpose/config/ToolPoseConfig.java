package com.example.toolpose.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;

public class ToolPoseConfig {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final File CONFIG_FILE = new File("config/toolpose.json");

    // General right-hand settings
    public boolean enabled = true;
    public float offsetX = 0.56f;
    public float offsetY = -0.52f;
    public float offsetZ = -0.72f;
    public float rotationX = 0.0f;
    public float rotationY = 0.0f;
    public float rotationZ = 0.0f;
    public float scale = 1.0f;

    // General left-hand settings
    public float offsetXLeft = -0.56f;
    public float offsetYLeft = -0.52f;
    public float offsetZLeft = -0.72f;
    public float rotationXLeft = 0.0f;
    public float rotationYLeft = 0.0f;
    public float rotationZLeft = 0.0f;
    public float scaleLeft = 1.0f;

    // Swing animation intensity
    public float swingIntensity = 1.0f;
    public float swingIntensityLeft = 1.0f;

    // Master switch for category-specific transforms
    public boolean useCategoryTransforms = false;

    // Right-hand categories
    public Transform sword = new Transform(0.56f, -0.52f, -0.72f, 0f, 0f, 0f);
    public Transform axe = new Transform(0.56f, -0.52f, -0.72f, 0f, 0f, 0f);
    public Transform pickaxe = new Transform(0.56f, -0.52f, -0.72f, 0f, 0f, 0f);
    public Transform shovel = new Transform(0.56f, -0.52f, -0.72f, 0f, 0f, 0f);
    public Transform hoe = new Transform(0.56f, -0.52f, -0.72f, 0f, 0f, 0f);
    public Transform food = new Transform(0.56f, -0.52f, -0.72f, 0f, 0f, 0f);
    public Transform block = new Transform(0.56f, -0.52f, -0.72f, 0f, 0f, 0f);
    public Transform totem = new Transform(0.56f, -0.52f, -0.72f, 0f, 0f, 0f);
    public Transform shield = new Transform(0.56f, -0.52f, -0.72f, 0f, 0f, 0f);
    public Transform bow = new Transform(0.56f, -0.52f, -0.72f, 0f, 0f, 0f);
    public Transform crossbow = new Transform(0.56f, -0.52f, -0.72f, 0f, 0f, 0f);

    // Left-hand categories
    public Transform swordLeft = new Transform(-0.56f, -0.52f, -0.72f, 0f, 0f, 0f);
    public Transform axeLeft = new Transform(-0.56f, -0.52f, -0.72f, 0f, 0f, 0f);
    public Transform pickaxeLeft = new Transform(-0.56f, -0.52f, -0.72f, 0f, 0f, 0f);
    public Transform shovelLeft = new Transform(-0.56f, -0.52f, -0.72f, 0f, 0f, 0f);
    public Transform hoeLeft = new Transform(-0.56f, -0.52f, -0.72f, 0f, 0f, 0f);
    public Transform foodLeft = new Transform(-0.56f, -0.52f, -0.72f, 0f, 0f, 0f);
    public Transform blockLeft = new Transform(-0.56f, -0.52f, -0.72f, 0f, 0f, 0f);
    public Transform totemLeft = new Transform(-0.56f, -0.52f, -0.72f, 0f, 0f, 0f);
    public Transform shieldLeft = new Transform(-0.56f, -0.52f, -0.72f, 0f, 0f, 0f);
    public Transform bowLeft = new Transform(-0.56f, -0.52f, -0.72f, 0f, 0f, 0f);
    public Transform crossbowLeft = new Transform(-0.56f, -0.52f, -0.72f, 0f, 0f, 0f);

    private static ToolPoseConfig instance;

    public static ToolPoseConfig getInstance() {
        if (instance == null) {
            instance = load();
        }
        return instance;
    }

    private static ToolPoseConfig load() {
        if (CONFIG_FILE.exists()) {
            try (FileReader reader = new FileReader(CONFIG_FILE)) {
                return GSON.fromJson(reader, ToolPoseConfig.class);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        ToolPoseConfig config = new ToolPoseConfig();
        config.save();
        return config;
    }

    public void save() {
        try (FileWriter writer = new FileWriter(CONFIG_FILE)) {
            GSON.toJson(this, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Randomize both hands (used by main screen)
    public void randomize() {
        Random random = new Random();

        // Right hand
        offsetX = -0.06f + random.nextFloat() * (0.8f - (-0.06f));
        offsetY = -0.7f + random.nextFloat() * (0.5f - (-0.7f));
        offsetZ = -2.0f + random.nextFloat() * (-0.7f - (-2.0f));
        rotationX = -180f + random.nextFloat() * 360f;
        rotationY = -180f + random.nextFloat() * 360f;
        rotationZ = -180f + random.nextFloat() * 360f;
        scale = 0.5f + random.nextFloat() * (2.0f - 0.5f);
        swingIntensity = 0.1f + random.nextFloat() * (2.0f - 0.1f);

        // Left hand
        offsetXLeft = -(0.8f - random.nextFloat() * (0.8f - (-0.06f)));
        offsetYLeft = -0.7f + random.nextFloat() * (0.5f - (-0.7f));
        offsetZLeft = -2.0f + random.nextFloat() * (-0.7f - (-2.0f));
        rotationXLeft = -180f + random.nextFloat() * 360f;
        rotationYLeft = -180f + random.nextFloat() * 360f;
        rotationZLeft = -180f + random.nextFloat() * 360f;
        scaleLeft = 0.5f + random.nextFloat() * (2.0f - 0.5f);
        swingIntensityLeft = 0.1f + random.nextFloat() * (2.0f - 0.1f);

        save();
    }

    // Randomize only one hand (used by weapon settings)
    public void randomizeHand(boolean left) {
        Random random = new Random();

        if (left) {
            offsetXLeft = -0.06f + random.nextFloat() * (0.8f - (-0.06f));
            offsetYLeft = -0.7f + random.nextFloat() * (0.5f - (-0.7f));
            offsetZLeft = -2.0f + random.nextFloat() * (-0.7f - (-2.0f));
            rotationXLeft = -180f + random.nextFloat() * 360f;
            rotationYLeft = -180f + random.nextFloat() * 360f;
            rotationZLeft = -180f + random.nextFloat() * 360f;
            scaleLeft = 0.5f + random.nextFloat() * (2.0f - 0.5f);
            swingIntensityLeft = 0.1f + random.nextFloat() * (2.0f - 0.1f);
        } else {
            offsetX = -0.06f + random.nextFloat() * (0.8f - (-0.06f));
            offsetY = -0.7f + random.nextFloat() * (0.5f - (-0.7f));
            offsetZ = -2.0f + random.nextFloat() * (-0.7f - (-2.0f));
            rotationX = -180f + random.nextFloat() * 360f;
            rotationY = -180f + random.nextFloat() * 360f;
            rotationZ = -180f + random.nextFloat() * 360f;
            scale = 0.5f + random.nextFloat() * (2.0f - 0.5f);
            swingIntensity = 0.1f + random.nextFloat() * (2.0f - 0.1f);
        }

        save();
    }

    public void resetToDefaults() {
        this.enabled = true;
        this.offsetX = 0.56f;
        this.offsetY = -0.52f;
        this.offsetZ = -0.72f;
        this.rotationX = 0.0f;
        this.rotationY = 0.0f;
        this.rotationZ = 0.0f;
        this.scale = 1.0f;

        this.offsetXLeft = -0.56f;
        this.offsetYLeft = -0.52f;
        this.offsetZLeft = -0.72f;
        this.rotationXLeft = 0.0f;
        this.rotationYLeft = 0.0f;
        this.rotationZLeft = 0.0f;
        this.scaleLeft = 1.0f;

        this.swingIntensity = 1.0f;
        this.swingIntensityLeft = 1.0f;
        this.useCategoryTransforms = false;

        resetTransform(sword, 0.56f, -0.52f, -0.72f, 0f, 0f, 0f);
        resetTransform(axe, 0.56f, -0.52f, -0.72f, 0f, 0f, 0f);
        resetTransform(pickaxe, 0.56f, -0.52f, -0.72f, 0f, 0f, 0f);
        resetTransform(shovel, 0.56f, -0.52f, -0.72f, 0f, 0f, 0f);
        resetTransform(hoe, 0.56f, -0.52f, -0.72f, 0f, 0f, 0f);
        resetTransform(food, 0.56f, -0.52f, -0.72f, 0f, 0f, 0f);
        resetTransform(block, 0.56f, -0.52f, -0.72f, 0f, 0f, 0f);
        resetTransform(totem, 0.56f, -0.52f, -0.72f, 0f, 0f, 0f);
        resetTransform(shield, 0.56f, -0.52f, -0.72f, 0f, 0f, 0f);
        resetTransform(bow, 0.56f, -0.52f, -0.72f, 0f, 0f, 0f);
        resetTransform(crossbow, 0.56f, -0.52f, -0.72f, 0f, 0f, 0f);

        resetTransform(swordLeft, -0.56f, -0.52f, -0.72f, 0f, 0f, 0f);
        resetTransform(axeLeft, -0.56f, -0.52f, -0.72f, 0f, 0f, 0f);
        resetTransform(pickaxeLeft, -0.56f, -0.52f, -0.72f, 0f, 0f, 0f);
        resetTransform(shovelLeft, -0.56f, -0.52f, -0.72f, 0f, 0f, 0f);
        resetTransform(hoeLeft, -0.56f, -0.52f, -0.72f, 0f, 0f, 0f);
        resetTransform(foodLeft, -0.56f, -0.52f, -0.72f, 0f, 0f, 0f);
        resetTransform(blockLeft, -0.56f, -0.52f, -0.72f, 0f, 0f, 0f);
        resetTransform(totemLeft, -0.56f, -0.52f, -0.72f, 0f, 0f, 0f);
        resetTransform(shieldLeft, -0.56f, -0.52f, -0.72f, 0f, 0f, 0f);
        resetTransform(bowLeft, -0.56f, -0.52f, -0.72f, 0f, 0f, 0f);
        resetTransform(crossbowLeft, -0.56f, -0.52f, -0.72f, 0f, 0f, 0f);

        save();
    }

    private void resetTransform(Transform t, float ox, float oy, float oz, float rx, float ry, float rz) {
        t.offsetX = ox;
        t.offsetY = oy;
        t.offsetZ = oz;
        t.rotationX = rx;
        t.rotationY = ry;
        t.rotationZ = rz;
        t.scale = 1.0f;
        t.enabled = true;
        t.swingIntensity = 1.0f;
    }

    public static class Transform {
        public boolean enabled = true;
        public float offsetX;
        public float offsetY;
        public float offsetZ;
        public float rotationX;
        public float rotationY;
        public float rotationZ;
        public float scale = 1.0f;
        public float swingIntensity = 1.0f;

        public Transform(float offsetX, float offsetY, float offsetZ,
                         float rotationX, float rotationY, float rotationZ) {
            this.offsetX = offsetX;
            this.offsetY = offsetY;
            this.offsetZ = offsetZ;
            this.rotationX = rotationX;
            this.rotationY = rotationY;
            this.rotationZ = rotationZ;
            this.scale = 1.0f;
            this.swingIntensity = 1.0f;
        }
    }
}