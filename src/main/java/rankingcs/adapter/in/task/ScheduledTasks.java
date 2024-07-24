package rankingcs.adapter.in.task;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.RestController;
import rankingcs.port.in.SaveReadmePortIn;

@RestController
public class ScheduledTasks {

    private final SaveReadmePortIn saveReadmePortIn;

    public ScheduledTasks(SaveReadmePortIn saveReadmePortIn) {
        this.saveReadmePortIn = saveReadmePortIn;
    }

    @Scheduled(cron = "* * * * * *")
    public void saveReadme() {
        this.saveReadmePortIn.processReadmeFiles();
        System.out.println("Readme files processed at " + new java.util.Date());
    }
}