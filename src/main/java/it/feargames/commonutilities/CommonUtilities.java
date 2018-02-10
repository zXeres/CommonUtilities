package it.feargames.commonutilities;

import it.feargames.commonutilities.module.ModuleManager;
import it.feargames.commonutilities.service.CommandService;
import it.feargames.commonutilities.service.PluginService;
import it.feargames.commonutilities.service.ProtocolServiceWrapper;
import it.feargames.commonutilities.service.VaultServiceWrapper;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

public final class CommonUtilities extends JavaPlugin implements Listener {

    private PluginService service;
    private CommandService commands;
    private ProtocolServiceWrapper protocol;
    private VaultServiceWrapper vault;
    private ModuleManager moduleManager;

    @Override
    public void onLoad() {
        saveDefaultConfig();

        service = new PluginService(this);
        commands = new CommandService();
        protocol = new ProtocolServiceWrapper(this, service);
        vault = new VaultServiceWrapper(service);
        moduleManager = new ModuleManager(getConfig().getConfigurationSection("modules"), service, commands, protocol);
        moduleManager.loadInternalModules(this::saveConfig);
    }

    @Override
    public void onEnable() {
        protocol.initialize();
        vault.initialize();
        commands.register(this);
        moduleManager.enableModules();
    }

    @Override
    public void onDisable() {
        moduleManager.disableModules();
        protocol.cleanup();
    }

}
