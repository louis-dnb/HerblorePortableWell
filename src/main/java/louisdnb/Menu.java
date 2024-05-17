package louisdnb;

import net.botwithus.rs3.imgui.ImGui;
import net.botwithus.rs3.imgui.ImGuiWindowFlag;
import net.botwithus.rs3.script.ScriptConsole;
import net.botwithus.rs3.script.ScriptGraphicsContext;

public class Menu extends ScriptGraphicsContext {

    private Herblore script;

    public Menu(ScriptConsole scriptConsole, Herblore script) {
        super(scriptConsole);
        this.script = script;
    }

    @Override
    public void drawSettings() {
        ImGui.SetWindowSize((float) 250, (float) 250);
        if (ImGui.Begin("Herblore Portable Well", ImGuiWindowFlag.None.getValue())) {
            ImGui.SetWindowSize((float) 250, (float) 250);

            if (ImGui.Button("Start")) {
                Herblore.botState = Herblore.BotState.INITIALIZING;
            }

            ImGui.SameLine();

            if (ImGui.Button("Stop")) {
                Herblore.botState = Herblore.BotState.IDLE;
            }

            ImGui.Text("Bot state: " + Herblore.botState);


            ImGui.End();
        }

    }

    @Override
    public void drawOverlay() {
        super.drawOverlay();
    }
}
