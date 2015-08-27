package com.legendzero.lzlib.command;

import com.google.common.base.Joiner;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Maps;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.Plugin;

import java.util.List;
import java.util.Map;

@FieldDefaults(level = AccessLevel.PRIVATE)
@RequiredArgsConstructor
public class CommandContext {

    @Getter final Plugin plugin;
    @Getter final CommandSender sender;
    @Getter final String cmdLabel;
    @Getter final List<String> arguments;
    int argumentIndex = 0;
    int markIndex = 0;
    final Map<String, Object> contextMap = Maps.newHashMap();

    public String getCommandLabel() {
        return this.cmdLabel;
    }

    public String getExecutedCommand(boolean context) {
        if (context) {
            return "/" + this.cmdLabel + " " + Joiner.on(' ')
                    .join(this.getArguments().subList(0, this.argumentIndex));
        } else {
            return "/" + this.cmdLabel + " " + Joiner.on(' ')
                    .join(this.getArguments());
        }
    }

    public boolean hasNextArgument() {
        return this.argumentIndex < this.arguments.size();
    }

    public boolean hasPreviousArgument() {
        return this.argumentIndex > 0;
    }

    public String peekArgument() {
        return this.arguments.get(this.argumentIndex);
    }

    public String nextArgument() {
        String argument = this.arguments.get(this.argumentIndex);
        this.argumentIndex++;
        return argument;
    }

    public String previousArgument() {
        this.argumentIndex--;
        return this.arguments.get(this.argumentIndex);
    }

    public int markArgument() {
        return this.markIndex = this.argumentIndex;
    }

    public void resetToMarkArgument() {
        this.argumentIndex = this.markIndex;
    }

    public void resetToIndex(int index) {
        this.argumentIndex = index;
    }

    public void resetIndex() {
        this.resetToIndex(0);
    }

    public List<String> getRemainingArguments() {
        return this.arguments.subList(this.argumentIndex, this.arguments.size());
    }

    public Map<String, Object> getContextMap() {
        return this.contextMap;
    }

    public void putContext(String key, Object value) {
        this.contextMap.put(key, value);
    }

    public Object getContext(String key) {
        return this.contextMap.get(key);
    }

    public Object getContext(String key, Object def) {
        return this.contextMap.getOrDefault(key, def);
    }

    public <T> T getContext(String key, Class<T> type) {
        Object context = this.getContext(key);
        try {
            return type.cast(context);
        } catch (ClassCastException e) {
            throw new IllegalArgumentException("Context value did not match assumed type");
        }
    }

    public <T> T getContext(String key, Class<T> type, T def) {
        Object context = this.getContext(key, def);
        try {
            return type.cast(context);
        } catch (ClassCastException e) {
            throw new IllegalArgumentException("Context value did not match assumed type");
        }
    }

    public void removeContext(String key) {
        this.contextMap.remove(key);
    }
}
