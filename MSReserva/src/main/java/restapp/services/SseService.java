package restapp.services;

import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.HashMap;
import java.util.Map;

public class SseService {

    private final Map<String, SseEmitter> sseEmitterManager = new HashMap<>();

    public SseEmitter getSseEmitter(String username){
        if(!sseEmitterManager.containsKey(username)){
            sseEmitterManager.put(username, new SseEmitter(0L));

            sseEmitterManager.get(username).onCompletion(() -> sseEmitterManager.remove(username));
            sseEmitterManager.get(username).onTimeout(() -> sseEmitterManager.remove(username));
            sseEmitterManager.get(username).onError((e) -> sseEmitterManager.remove(username));
        }

        return sseEmitterManager.get(username);
    }
}
