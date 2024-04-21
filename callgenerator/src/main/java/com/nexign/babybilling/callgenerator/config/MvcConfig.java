package com.nexign.babybilling.callgenerator.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Класс по созданию необходимых бинов
 */
@Configuration
public class MvcConfig {

    @Value("${spring.data.redis.host}")
    private String redisHost;
    @Value("${spring.data.redis.port}")
    private int redisPort;


    @Bean
    public Random random() {
        return new Random();
    }

    @Bean
    JedisConnectionFactory jedisConnectionFactory() {
        RedisStandaloneConfiguration redisStandaloneConfiguration = new RedisStandaloneConfiguration();
        redisStandaloneConfiguration.setHostName(redisHost);
        redisStandaloneConfiguration.setPort(redisPort);

        return new JedisConnectionFactory(redisStandaloneConfiguration);
    }

    @Bean
    public <F, S> RedisTemplate<F, S> redisTemplate(JedisConnectionFactory factory) {
        RedisTemplate<F, S> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(factory);

        return redisTemplate;
    }

    /**
     * создание абонентов разных операторов
     * @return список абонентов, 955 - код оператора ромашка
     */
    @Bean
    public List<String> allUsers() {
        return List.of("79554014636", "79315545944", "79553488401", "79052951842", "79015350940",
                "79153862957", "79310390473", "79156339875", "74326371627", "79550398772", "79158865832",
                "79311331751", "79150489745", "79151949300", "79011682503",
                "79154526175", "79014665450", "79151050978", "79552658665", "79734595671",
                "79552168075", "79013733715", "74320253225", "79018955329", "79157500150",
                "79734444495", "79059681329", "79315165192", "79058274997", "79058594043", "79735180117",
                "79015003688", "79734115659", "74324468109", "79019066494", "79159278079", "79734225027",
                "79738485439", "79553669810", "79734919052", "79056243184", "79555651417", "79550471859",
                "79316404934", "79154603945", "79019328858", "79157812081", "79559779972", "74320375034",
                "79051844004", "79016495129", "79318760197", "79159608579", "79312080597", "79012894313", "79310768952",
                "79734345167", "79555857898", "79316921493", "79058533273", "79313184283", "79156343670", "79312465948",
                "79314959568", "79056426794", "79318522958", "79317614896", "79016051544", "79011729345", "79010823106",
                "79315321116", "79550410514", "74329498109", "79551006150", "79154319680", "79551975058", "79734556695",
                "79011610212", "79053683058", "79016045120", "79052931293", "79015108734", "79731481049", "79156555709", "79057947023",
                "79019782314", "79735098519", "79551302983", "79737210201", "79555478980", "79056234675", "79154578938", "79012787779",
                "79010256180", "79016695300", "79150481373", "79156946290", "79318257955", "79557581785", "79056731921", "74320821936",
                "79552919091", "74322966233", "74324967679", "79050186233", "79011556029", "79012751069", "79737328589", "79553513426",
                "79050727150", "74327496562", "79059805065", "79551529009", "79056724811", "79550790113", "79311778131", "79734978674",
                "79019107267", "79550289408", "79011571820", "79555678757", "79053880379", "79151688600", "79012126000", "79010334631",
                "79553208309", "79311068953", "79016530707", "74320599941", "79055826141", "79735781344", "74323699134", "79013468476",
                "79057681664", "79059688981", "79053747031", "79551967402", "79155284898", "74321886165", "79152441428", "79550144220",
                "74320358596", "79736868970", "79011543461", "79730988891", "79553648563", "79732424479", "74328255592",
                "79738828431", "79554831095");
    }
}
