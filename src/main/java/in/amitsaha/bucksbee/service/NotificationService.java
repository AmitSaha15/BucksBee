package in.amitsaha.bucksbee.service;

import in.amitsaha.bucksbee.dto.ExpenseDTO;
import in.amitsaha.bucksbee.entity.ProfileEntity;
import in.amitsaha.bucksbee.repository.ProfileRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationService {

    private final ProfileRepository profileRepository;
    private final EmailService emailService;
    private final ExpenseService expenseService;

    @Value("${bucks.bee.frontend.url}")
    private String frontendUrl;

    @Scheduled(cron = "0 0 22 * * *", zone = "IST")
    public void sendDailyReminder(){
        log.info("Scheduler started at: sendDailyReminder()");

        List<ProfileEntity> profiles = profileRepository.findAll();
        for(ProfileEntity profile : profiles){
            String body = "Hi " + profile.getFullName() + ",<br><br>"
                    + "This is a friendly reminder to add your income and expenses for today in BucksBee.<br><br>"
                    + "<a href="+frontendUrl+" style='display:inline-block;padding:10px 20px;background-color:#4CAF50;color:#fff;text-decoration:none;border-radius:5px;font-weight:bold;'>Go to BucksBee</a>"
                    + "<br><br>Best regards,<br>BucksBee Team";

            emailService.sendEmail(profile.getEmail(), "Daily Reminder - Add Your Incomes and Expenses", body);
        }

        log.info("Job completed: sendDailyReminder()");
    }

    @Scheduled(cron = "0 0 23 * * *", zone = "IST")
    public void sendDailyExpenseSummary(){
        log.info("Scheduler started at: sendDailyExpenseSummary()");

        List<ProfileEntity> profiles = profileRepository.findAll();
        for(ProfileEntity profile : profiles){
            List<ExpenseDTO> expensesForUserOnDate = expenseService.getExpensesForUserOnDate(profile.getId(), LocalDate.now());
            if(!expensesForUserOnDate.isEmpty()){
                StringBuilder table = new StringBuilder();
                table.append("<table style='border-collapse:collapse;width:100%;'>");
                table.append("<tr style='background-color:#f2f2f2;'><th style='border:1px solid #ddd;padding:8px;'>S.No</th><th style='border:1px solid #ddd;padding:8px;'>Name</th><th style='border:1px solid #ddd;padding:8px;'>Amount</th><th style='border:1px solid #ddd;padding:8px;'>Category</th></tr>");

                int i = 1;
                for(ExpenseDTO expense : expensesForUserOnDate) {
                    table.append("<tr>");
                    table.append("<td style='border:1px solid #ddd;padding:8px;'>").append(i++).append("</td>");
                    table.append("<td style='border:1px solid #ddd;padding:8px;'>").append(expense.getName()).append("</td>");
                    table.append("<td style='border:1px solid #ddd;padding:8px;'>").append(expense.getAmount()).append("</td>");
                    table.append("<td style='border:1px solid #ddd;padding:8px;'>").append(expense.getCategoryId() != null ? expense.getCategoryName() : "N/A").append("</td>");
                    table.append("</tr>");
                }

                table.append("</table>");
                String body = "Hi "+profile.getFullName()+",<br/><br/> Here is a summary of your expenses for today:<br/><br/>"+table+"<br/><br/>Best regards,<br/>BucksBee Team";

                emailService.sendEmail(profile.getEmail(), "Your Daily Expense Summary", body);
            }

        }
        log.info("Scheduler completed at: sendDailyExpenseSummary()");
    }
}
