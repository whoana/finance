/**
 * Copyright 2018 mocomsys Inc. All Rights Reserved.
 */
package whoana.finance;




import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;
 
/**
 * <pre>
 * spring java bean config file
 * rose.mary.trace
 * TraceConfig.java
 * </pre>
 * @author whoana
 * @date Aug 1, 2019
 */
@Configuration
public class ApplicationConfig {
 
	 
	int schedulerPoolSize = 100;
	String schedulerPrefix = "scheduler";
	@Bean(name = "taskScheduler")
	public ThreadPoolTaskScheduler taskScheduler() {
		ThreadPoolTaskScheduler scheduler = new ThreadPoolTaskScheduler();
        scheduler.setPoolSize(schedulerPoolSize);
        scheduler.setThreadNamePrefix(schedulerPrefix);
        scheduler.initialize();
		return scheduler;
	}

	
	@Bean
    public LocaleResolver localeResolver() {
        SessionLocaleResolver localeResolver = new SessionLocaleResolver();
        //localeResolver.setDefaultLocale(Locale.KOREA);
        return localeResolver;
    }
	
 
}
