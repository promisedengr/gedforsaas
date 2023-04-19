package ma.project.GedforSaas.utils;


import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class ScheduleCalculator {
    public String cronExecExpr = "*/10 * * * * *";


    public String getCron() {
        return this.cronExecExpr;
    }


    public void updateCron(int repeatTime) { // method to update cron used in TaskService.rappel() method
        this.cronExecExpr = ("*/".concat(String.valueOf(repeatTime)).concat(" * * * * *"));
    }

    @Bean("ScheduleCalculator")
    public ScheduleCalculator createScheduleCalculator() {
        return new ScheduleCalculator();
    }

}

