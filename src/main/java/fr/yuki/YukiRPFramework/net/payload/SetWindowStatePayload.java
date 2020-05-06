package fr.yuki.YukiRPFramework.net.payload;

public class SetWindowStatePayload {
    private String type;
    private String windowType;
    private boolean windowState;

    public SetWindowStatePayload(String windowType, boolean windowState) {
        this.type = "SET_WINDOW_STATE";
        this.windowType = windowType;
        this.windowState = windowState;
    }

    public String getType() {
        return type;
    }

    public String getWindowType() {
        return windowType;
    }

    public boolean isWindowState() {
        return windowState;
    }
}
