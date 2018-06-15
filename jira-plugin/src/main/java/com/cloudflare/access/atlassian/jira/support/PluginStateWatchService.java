package com.cloudflare.access.atlassian.jira.support;

import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;

public interface PluginStateWatchService extends InitializingBean, DisposableBean{

	void onConfigChange();

}
