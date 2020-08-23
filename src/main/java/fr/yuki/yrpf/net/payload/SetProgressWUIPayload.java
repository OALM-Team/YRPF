package fr.yuki.yrpf.net.payload;

public class SetProgressWUIPayload {
    private int progress;
    private String type;

    public SetProgressWUIPayload(int progress) {
        this.progress = progress;
        this.type = "SET_PROGRESS_WUI";
    }

    public int getProgress() {
        return progress;
    }

    public String getType() {
        return type;
    }
}
