package pl.mwiski.dieticianfrontend.clients.user;

import org.springframework.http.MediaType;
import org.springframework.http.converter.json.GsonHttpMessageConverter;
import org.springframework.stereotype.Component;
import java.util.Arrays;
import java.util.List;

@Component
public class MyGsonHttpMessageConverter extends GsonHttpMessageConverter {
    public MyGsonHttpMessageConverter() {
        List<MediaType> types = Arrays.asList(
                new MediaType("text", "html", DEFAULT_CHARSET),
                new MediaType("application", "json", DEFAULT_CHARSET),
                new MediaType("application", "*+json", DEFAULT_CHARSET)
        );
        super.setSupportedMediaTypes(types);
    }
}