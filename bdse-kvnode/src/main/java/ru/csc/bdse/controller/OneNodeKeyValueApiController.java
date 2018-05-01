package ru.csc.bdse.controller;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.web.bind.annotation.RestController;
import ru.csc.bdse.kv.KeyValueApi;

@RestController
@ConditionalOnProperty("one.node.controller.enabled")
public class OneNodeKeyValueApiController extends AbstractKeyValueApiController{
    public OneNodeKeyValueApiController(@Qualifier("node") KeyValueApi keyValueApi) {
        super(keyValueApi);
    }
}
