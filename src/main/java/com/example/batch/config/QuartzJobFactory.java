package com.example.batch.config;

import lombok.Setter;
import org.quartz.spi.TriggerFiredBundle;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.quartz.SpringBeanJobFactory;
import org.springframework.stereotype.Component;

@Component
public class QuartzJobFactory extends SpringBeanJobFactory {

    private final AutowireCapableBeanJobFactory beanJobFactory;

    public QuartzJobFactory(ApplicationContext applicationContext) {

        this.beanJobFactory = new AutowireCapableBeanJobFactory();
        this.beanJobFactory.setApplicationContext(applicationContext);
        super.setApplicationContext(applicationContext);
    }

    @Override
    protected Object createJobInstance(TriggerFiredBundle bundle) throws Exception {

        Object job = super.createJobInstance(bundle);
        beanJobFactory.autowireBean(job);
        return job;
    }

    // Quartz Job에 의존성 주입을 위한 Helper 클래스
    @Setter
    public static class AutowireCapableBeanJobFactory {
        private ApplicationContext applicationContext;

        public void autowireBean(Object bean) {
            applicationContext.getAutowireCapableBeanFactory().autowireBean(bean);
        }
    }
} 