package ru.csc.bdse.controller;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.web.bind.annotation.RestController;
import ru.csc.bdse.kv.KeyValueApi;

@RestController
@ConditionalOnProperty("coordinator.http.controller.enabled")
public class CoordinatorKeyValueApiController extends AbstractKeyValueApiController {
    public CoordinatorKeyValueApiController(@Qualifier("coordinator") KeyValueApi keyValueApi) {
        super(keyValueApi);
    }
}
