package org.springframework.boot.firehose.annotations;

import org.springframework.boot.firehose.config.MetricConfig;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * Created by vcarvalho on 4/28/15.
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import(MetricConfig.class)
public @interface EnableFirehoseMetrics {
}
