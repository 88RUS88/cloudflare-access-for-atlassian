package com.cloudflare.access.atlassian.base.config;

import java.util.Optional;

import com.cloudflare.access.atlassian.common.config.PluginConfiguration;

public interface ConfigurationService {

	void save(ConfigurationVariables configurationVariables);

	Optional<ConfigurationVariables> loadConfigurationVariables();

	Optional<PluginConfiguration> getPluginConfiguration();

	boolean emailDomainRequiresAtlassianAuthentication(String email);
}
