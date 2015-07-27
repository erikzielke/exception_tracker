package dk.responsfabrikken.exception_tracker.core.model.client;

import dk.responsfabrikken.exception_tracker.core.model.server.Settings;

public class SettingsDto {
    private String host;
    private String username;
    private String password;
    private int port;

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public static SettingsDto fromSettings(Settings settings) {
        SettingsDto settingsDto = new SettingsDto();
        settingsDto.setHost(settings.getHost());
        settingsDto.setUsername(settings.getUsername());
        settingsDto.setPassword(settings.getPassword());
        settingsDto.setPort(settings.getPort());
        return settingsDto;
    }
}
