package louisdnb;

import net.botwithus.api.game.hud.inventories.Backpack;
import net.botwithus.internal.scripts.ScriptDefinition;
import net.botwithus.rs3.events.impl.SkillUpdateEvent;
import net.botwithus.rs3.game.Client;
import net.botwithus.rs3.game.cs2.ScriptBuilder;
import net.botwithus.rs3.game.cs2.layouts.Layout;
import net.botwithus.rs3.game.hud.interfaces.Interfaces;
import net.botwithus.rs3.game.queries.builders.objects.SceneObjectQuery;
import net.botwithus.rs3.game.scene.entities.characters.player.LocalPlayer;
import net.botwithus.rs3.game.scene.entities.object.SceneObject;
import net.botwithus.rs3.script.Execution;
import net.botwithus.rs3.script.LoopingScript;
import net.botwithus.rs3.script.config.ScriptConfig;

import java.util.Random;

public class Herblore extends LoopingScript {
    private Random random = new Random();

    public enum BotState {
        IDLE,
        INITIALIZING,
        BANKING,
        INTERACT_WELL,
        INTERACT_HERBLORE_INTERFACE,
        MIXING,
        DEBUG,
    }

    public static BotState botState = BotState.IDLE;

    public Herblore(String s, ScriptConfig scriptConfig, ScriptDefinition scriptDefinition) {
        super(s, scriptConfig, scriptDefinition);
    }

    @Override
    public boolean initialize() {
        this.sgc = new Menu(getConsole(), this);
        this.loopDelay = 500;

        return super.initialize();
    }

    @Override
    public void onLoop() {

        LocalPlayer localPlayer = Client.getLocalPlayer();
        if (localPlayer == null || Client.getGameState() != Client.GameState.LOGGED_IN || botState == BotState.IDLE) {
            Execution.delay(random.nextLong(3000,7000));
            return;
        }

        switch (botState) {
            case INITIALIZING -> {
                botState = BotState.BANKING;
            }
            case BANKING -> {
                SceneObject chest = SceneObjectQuery.newQuery().name("Bank chest").results().nearest();
                if (chest == null) {
                    println("No bank chest found");
                    return;
                }

                if (!chest.interact("Load Last Preset from")) {
                    println("Failed to interact with bank chest");
                    return;
                }

                Execution.delay(random.nextLong(1000,3000));

                botState = BotState.INTERACT_WELL;
            }
            case INTERACT_WELL -> {
                SceneObject well = SceneObjectQuery.newQuery().name("Portable well").results().nearest();
                if (well == null) {
                    println("No portable well found");
                    return;
                }

                if (!well.interact("Mix Potions")) {
                    println("Failed to interact with portable well");
                    return;
                }

                Execution.delayUntil(5000, () -> Interfaces.isOpen(1370));

                if (!Interfaces.isOpen(1370)) {
                    println("Failed to open herblore interface");
                    return;
                }

                botState = BotState.INTERACT_HERBLORE_INTERFACE;
            }
            case INTERACT_HERBLORE_INTERFACE -> {
                if (!Interfaces.isOpen(1370)) {
                    println("Failed to open herblore interface");
                    return;
                }

                //interact with mix button
                ScriptBuilder.of(6970).args(Layout.INT).invokeExact(83);

                Execution.delayUntil(5000, () -> Interfaces.isOpen(321));

                if (!Interfaces.isOpen(1251)) {
                    println("Failed to open skilling interface");
                    return;
                }

                botState = BotState.MIXING;
            }
            case MIXING -> {
                if (Interfaces.isOpen(1251)) {
                    return;
                }

                botState = BotState.BANKING;
            }
        }

    }

    public BotState getBotState() {
        return botState;
    }

}
