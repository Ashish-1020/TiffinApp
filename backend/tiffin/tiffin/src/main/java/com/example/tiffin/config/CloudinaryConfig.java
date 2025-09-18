package com.example.tiffin.config;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CloudinaryConfig {

    @Bean
    public Cloudinary cloudinary() {
        return new Cloudinary(ObjectUtils.asMap(
                "cloud_name", "dkflxoyda",
                "api_key", "898924273719847",
                "api_secret", "IPPwmK7niBWAd1FJCVidJQECr6w"
        ));
    }
}
