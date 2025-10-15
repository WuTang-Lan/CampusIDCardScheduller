import com.riara.dao.StudentDAO;
import com.riara.dao.AppointmentDAO;
import com.riara.model.Student;
import com.riara.model.Appointment;
import com.riara.util.EmailService;

import static spark.Spark.*;

import java.time.LocalDate;
import java.time.LocalTime;

public class Main {
    public static void main(String[] args) {
        // Serve static files (HTML, CSS, JS) from /public
        staticFiles.location("/public");
        staticFiles.expireTime(0); // Disable caching during dev
        port(8080);

        // Redirect root to login
        get("/", (req, res) -> {
            res.redirect("/login.html");
            return null;
        });

        // ========== LOGIN ==========
        post("/login", (req, res) -> {
            String email = req.queryParams("email");
            String password = req.queryParams("password");

            StudentDAO studentDAO = new StudentDAO();
            Student student = studentDAO.findByEmail(email);

            if (student != null && com.riara.util.PasswordUtil.verifyPassword(password, student.getPassword())) {
                req.session().attribute("student", student);
                res.redirect("/student_dashboard.html");
            } else {
                res.redirect("/login.html?error=1");
            }
            return null;
        });

        // ========== BOOK APPOINTMENT ==========
        post("/book", (req, res) -> {
            Object sessionObj = req.session().attribute("student");
            if (!(sessionObj instanceof Student)) {
                res.redirect("/login.html");
                return null;
            }
            Student student = (Student) sessionObj;

            String dateStr = req.queryParams("date");
            String timeStr = req.queryParams("time");

            if (dateStr == null || timeStr == null || dateStr.isEmpty() || timeStr.isEmpty()) {
                res.redirect("/appointment_form.html?error=missing");
                return null;
            }

            Appointment appointment = new Appointment();
            appointment.setStudentId(student.getStudentId());
            appointment.setAppointmentDate(LocalDate.parse(dateStr));
            appointment.setTimeSlot(LocalTime.parse(timeStr));
            appointment.setStatus("Scheduled");

            boolean saved = new AppointmentDAO().save(appointment);
            if (saved) {
                res.redirect("/student_dashboard.html?booked=1");
            } else {
                res.redirect("/appointment_form.html?error=save");
            }
            return null;
        });

        // ========== VIEW MY APPOINTMENTS ==========
        get("/my-appointments", (req, res) -> {
            Object sessionObj = req.session().attribute("student");
            if (!(sessionObj instanceof Student)) {
                res.redirect("/login.html");
                return null;
            }
            Student student = (Student) sessionObj;

            StringBuilder html = new StringBuilder();
            html.append("<!DOCTYPE html><html><head><meta charset='UTF-8'><title>My Appointments</title>");
            html.append("<link rel='stylesheet' href='/css/style.css'>");
            html.append("</head><body>");
            html.append("<div class='container'><div class='card'>");
            html.append("<h2>📋 My Appointments</h2>");

            java.util.List<Appointment> apps = new AppointmentDAO().getByStudentId(student.getStudentId());
            if (apps.isEmpty()) {
                html.append("<p>No appointments found.</p>");
            } else {
                html.append("<ul class='appointments-list'>");
                for (Appointment app : apps) {
                    html.append("<li>")
                            .append("📅 Date: ").append(app.getAppointmentDate())
                            .append(" | ⏰ Time: ").append(app.getTimeSlot())
                            .append(" | 🟢 Status: ").append(app.getStatus())
                            .append("</li>");
                }
                html.append("</ul>");
            }

            html.append("<div class='back-link'><a href='/student_dashboard.html'>← Back to Dashboard</a></div>");
            html.append("</div></div></body></html>");
            return html.toString();
        });

        // ========== ADMIN DASHBOARD ==========
        get("/admin", (req, res) -> {
            Object sessionObj = req.session().attribute("student");
            if (!(sessionObj instanceof Student)) {
                res.redirect("/login.html");
                return null;
            }

            java.util.List<Appointment> apps = new AppointmentDAO().getAll();
            StringBuilder html = new StringBuilder();

            html.append("<!DOCTYPE html><html><head><meta charset='UTF-8'><title>Admin Dashboard</title>");
            html.append("<link rel='stylesheet' href='/css/style.css'>");
            html.append("</head><body>");
            html.append("<div class='container'><div class='card'>");
            html.append("<h2>👁️ Admin Dashboard – Appointments</h2>");

            html.append("<table class='appointments-table'>");
            html.append("<thead><tr><th>ID</th><th>Student ID</th><th>Date</th><th>Time</th><th>Status</th><th>Action</th></tr></thead>");
            html.append("<tbody>");

            for (Appointment app : apps) {
                html.append("<tr>")
                        .append("<td>").append(app.getAppointmentId()).append("</td>")
                        .append("<td>").append(app.getStudentId()).append("</td>")
                        .append("<td>").append(app.getAppointmentDate()).append("</td>")
                        .append("<td>").append(app.getTimeSlot()).append("</td>")
                        .append("<td>").append(app.getStatus()).append("</td>")
                        .append("<td>")
                        .append("<form method='post' action='/admin/update' style='display:inline;'>")
                        .append("<input type='hidden' name='id' value='").append(app.getAppointmentId()).append("'>")
                        .append("<select name='status'>")
                        .append("<option value='Scheduled'" + ("Scheduled".equals(app.getStatus()) ? " selected" : "") + ">Scheduled</option>")
                        .append("<option value='Completed'" + ("Completed".equals(app.getStatus()) ? " selected" : "") + ">Completed</option>")
                        .append("<option value='Cancelled'" + ("Cancelled".equals(app.getStatus()) ? " selected" : "") + ">Cancelled</option>")
                        .append("</select>")
                        .append("<button type='submit' class='btn-small'>Update</button>")
                        .append("</form>")
                        .append("</td>")
                        .append("</tr>");
            }

            html.append("</tbody></table>");
            html.append("<div class='back-link'><a href='/login.html'>Logout</a></div>");
            html.append("</div></div></body></html>");
            return html.toString();
        });

        // ========== ADMIN: UPDATE STATUS + SEND EMAIL ==========
        post("/admin/update", (req, res) -> {
            int appId = Integer.parseInt(req.queryParams("id"));
            String newStatus = req.queryParams("status");

            AppointmentDAO appDao = new AppointmentDAO();
            StudentDAO studentDao = new StudentDAO();

            // Fetch appointment to get studentId
            Appointment targetApp = null;
            for (Appointment a : appDao.getAll()) {
                if (a.getAppointmentId() == appId) {
                    targetApp = a;
                    break;
                }
            }

            // If status is "Completed", send email
            if (targetApp != null && "Completed".equals(newStatus)) {
                Student student = studentDao.findById(targetApp.getStudentId());
                if (student != null && student.getEmail() != null) {
                    EmailService.sendIdReadyEmail(student.getEmail(), student.getFullName());
                }
            }

            appDao.updateStatus(appId, newStatus);
            res.redirect("/admin");
            return null;
        });

        // ========== LOGOUT ==========
        get("/logout", (req, res) -> {
            req.session().invalidate();
            res.redirect("/login.html");
            return null;
        });

        System.out.println("\n✅ Campus ID Scheduler is RUNNING!");
        System.out.println("   Open: http://localhost:8080");
        System.out.println("   Test login: test@student.riara.ac.ke / password123\n");
    }
}