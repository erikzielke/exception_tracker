package dk.responsfabrikken.exception_tracker.core.api;

import dk.responsfabrikken.exception_tracker.core.model.client.SettingsDto;
import dk.responsfabrikken.exception_tracker.core.model.server.Settings;
import dk.responsfabrikken.exception_tracker.core.model.server.SettingsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/settings")
public class SettingsResource {
    @Autowired SettingsRepository settingsRepository;

    @RequestMapping(method = RequestMethod.POST)
    public void save(@RequestBody SettingsDto settingsDto) {
        List<Settings> settingsList = settingsRepository.findAll();
        Settings settings;
        if (!settingsList.isEmpty()) {
            settings = settingsList.get(0);
        } else {
            settings = new Settings();
        }
        settings.setHost(settingsDto.getHost());
        settings.setUsername(settingsDto.getUsername());
        settings.setPassword(settingsDto.getPassword());
        settings.setPort(settingsDto.getPort());
        settingsRepository.save(settings);
    }

    @RequestMapping
    public SettingsDto get() {
        List<Settings> all = settingsRepository.findAll();
        if (!all.isEmpty()) {
            Settings settings = all.get(0);
            return SettingsDto.fromSettings(settings);
        } else {
            return new SettingsDto();
        }
    }

}
