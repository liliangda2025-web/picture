package org.example.picturebackend.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import org.springframework.boot.jackson.JsonComponent;
import org.springframework.context.annotation.Bean;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

// 1. @JsonComponent 是 Spring Boot 提供的专用于 Jackson 配置的注解
// 它会让 Spring 自动将该类中定义的 Bean 注入到全局 ObjectMapper 中
@JsonComponent
public class JsonConfig {

    // 2. 定义一个 Bean，返回自定义的 ObjectMapper
    // 注意：方法名 jacksonObjectMapper 会被 Spring 识别，用于覆盖默认的映射器
    @Bean
    public ObjectMapper jacksonObjectMapper(Jackson2ObjectMapperBuilder builder) {

        // 3. 利用 Builder 构建一个标准的 JSON 映射器（禁用 XML 功能）
        //    createXmlMapper(false) 确保只处理 JSON
        ObjectMapper objectMapper = builder.createXmlMapper(false).build();

        // 4. 创建一个新的模块（Module），用于注册自定义序列化器
        SimpleModule module = new SimpleModule();

        // 5. 为 Long 类型注册序列化器
        //    ToStringSerializer.instance 是 Jackson 内置的工具类
        //    它在序列化时会调用对象的 toString() 方法，对于 Long 就是输出数字字符串
        module.addSerializer(Long.class, ToStringSerializer.instance);
        //    同时注册基本类型 long（因为基本类型和包装类在反射中通常被视为不同）
        module.addSerializer(Long.TYPE, ToStringSerializer.instance);

        // 6. 将自定义模块注册到 ObjectMapper 中
        //    这一步生效后，所有 Long 类型的序列化都会走上面的规则
        objectMapper.registerModule(module);

        // 7. 返回配置好的 ObjectMapper，替换 Spring 容器中的默认实例
        return objectMapper;
    }
}
