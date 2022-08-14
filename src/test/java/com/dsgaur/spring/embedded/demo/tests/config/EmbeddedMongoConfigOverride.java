package com.dsgaur.spring.embedded.demo.tests.config;

import de.flapdoodle.embed.mongo.config.*;
import de.flapdoodle.embed.mongo.distribution.Feature;
import de.flapdoodle.embed.mongo.distribution.IFeatureAwareVersion;
import de.flapdoodle.embed.mongo.distribution.Version;
import de.flapdoodle.embed.mongo.distribution.Versions;
import de.flapdoodle.embed.process.runtime.Network;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.mongo.MongoProperties;
import org.springframework.boot.autoconfigure.mongo.embedded.EmbeddedMongoAutoConfiguration;
import org.springframework.boot.autoconfigure.mongo.embedded.EmbeddedMongoProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.util.Assert;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * Overrides the default configuration to enable journaling.
 * Most of the code is copied from {@link EmbeddedMongoAutoConfiguration}
 *
 * @see EmbeddedMongoAutoConfiguration
 */
@Configuration
@AutoConfigureBefore(EmbeddedMongoAutoConfiguration.class)
@EnableConfigurationProperties({MongoProperties.class, EmbeddedMongoProperties.class})
public class EmbeddedMongoConfigOverride {


    @Autowired
    EmbeddedMongoAutoConfiguration embeddedMongoAutoConfiguration;


    /**
     * Overrides the default embedded mongo configuration to enable journaling.
     * Actual magic happens here.
     *
     * @return Mongod configuration which is used to set up the embedded mongo server as well as the mongo clients
     * ({@link MongoRepository}, {@link MongoTemplate}, etc.)
     */
    @Bean
    public MongodConfig embeddedMongoConfiguration(EmbeddedMongoProperties embeddedProperties) throws IOException {
        ImmutableMongodConfig mongodConfig = (ImmutableMongodConfig) embeddedMongoAutoConfiguration.embeddedMongoConfiguration(embeddedProperties);
        return mongodConfig.withCmdOptions(MongoCmdOptions.builder().useNoJournal(false).build());
    }
}
