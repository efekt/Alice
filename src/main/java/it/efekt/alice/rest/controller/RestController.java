package it.efekt.alice.rest.controller;

import it.efekt.alice.core.AliceBootstrap;
import it.efekt.alice.rest.model.Status;
import net.dv8tion.jda.core.JDA;
import org.springframework.web.bind.annotation.RequestMapping;

@org.springframework.web.bind.annotation.RestController
@RequestMapping("/api/v1")
public class RestController {
    private JDA jda = AliceBootstrap.alice.getJDA();

    @RequestMapping("/status")
    public Status status(){
        Status status = new Status();
        status.setPing(this.jda.getPing());
        return status;
    }

}
