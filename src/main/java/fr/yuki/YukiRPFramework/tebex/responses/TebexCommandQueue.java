package fr.yuki.YukiRPFramework.tebex.responses;

public class TebexCommandQueue {
    private TebexCommandQueueMeta meta;
    private Command[] commands;

    public TebexCommandQueueMeta getMeta() { return meta; }
    public void setMeta(TebexCommandQueueMeta value) { this.meta = value; }

    public Command[] getCommands() { return commands; }
    public void setCommands(Command[] value) { this.commands = value; }
}
