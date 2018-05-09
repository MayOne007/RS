package core.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource("classpath:application.properties")
public class RedisManagerConfig {

	/*@Autowired
	private RedisConnectionFactory jedisConnectionFactory;
	
	@Value("${redis.host}")  
    private String host; 
	
	@Value("${redis.port}")  
    private Integer port;  */  
    
	/*@SuppressWarnings("serial")
	@Bean(name = "redisCacheManager")
	public RedisCacheManager createCacheManager() {
		Set<String> cacheNames = new HashSet<String>() {
			{
				add("user");
			}
		};
		// return RedisCacheManager.create(connectionFactory); //默认管理器
		RedisCacheManagerBuilder builder = RedisCacheManagerBuilder.fromConnectionFactory(jedisConnectionFactory);
		builder.initialCacheNames(cacheNames); // 设置多个缓存
		return builder.build();
	}*/
	
}
