import javax.swing.*;
import javax.mail.*;
import javax.mail.internet.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Properties;
import java.util.Timer;
import java.util.TimerTask;

public class ScheduledMessageApp {

    private JFrame frame;
    private JTextField dayField, monthField, yearField, hourField, minuteField;
    private DefaultListModel<String> whatsappListModel, emailListModel;

    public ScheduledMessageApp() {
        frame = new JFrame("Envio de Mensagens Agendado");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 500);
        frame.setLayout(new BorderLayout());

        // Toolbar
        JToolBar toolbar = new JToolBar();
        JButton addWhatsAppButton = new JButton("Adicionar Número WhatsApp");
        JButton addEmailButton = new JButton("Adicionar E-mail");
        JButton scheduleWhatsAppButton = new JButton("Agendar WhatsApp");
        JButton scheduleEmailButton = new JButton("Agendar E-mails");

        addWhatsAppButton.addActionListener(e -> addWhatsAppFields());
        addEmailButton.addActionListener(e -> addEmailFields());
        scheduleWhatsAppButton.addActionListener(e -> sendWhatsAppScheduled());
        scheduleEmailButton.addActionListener(e -> sendEmailScheduled());

        toolbar.add(addWhatsAppButton);
        toolbar.add(addEmailButton);
        toolbar.add(scheduleWhatsAppButton);
        toolbar.add(scheduleEmailButton);
        frame.add(toolbar, BorderLayout.NORTH);

        // Schedule input
        JPanel schedulePanel = new JPanel();
        dayField = new JTextField(5);
        monthField = new JTextField(5);
        yearField = new JTextField(8);
        hourField = new JTextField(5);
        minuteField = new JTextField(5);

        schedulePanel.add(new JLabel("Dia:"));
        schedulePanel.add(dayField);
        schedulePanel.add(new JLabel("Mês:"));
        schedulePanel.add(monthField);
        schedulePanel.add(new JLabel("Ano:"));
        schedulePanel.add(yearField);
        schedulePanel.add(new JLabel("Hora:"));
        schedulePanel.add(hourField);
        schedulePanel.add(new JLabel("Minuto:"));
        schedulePanel.add(minuteField);

        frame.add(schedulePanel, BorderLayout.SOUTH);

        // List models
        whatsappListModel = new DefaultListModel<>();
        emailListModel = new DefaultListModel<>();

        // List for WhatsApp numbers
        JList<String> whatsappList = new JList<>(whatsappListModel);
        frame.add(new JScrollPane(whatsappList), BorderLayout.WEST);

        // List for Emails
        JList<String> emailList = new JList<>(emailListModel);
        frame.add(new JScrollPane(emailList), BorderLayout.EAST);

        frame.setVisible(true);
    }

    private void addWhatsAppFields() {
        String number = JOptionPane.showInputDialog(frame, "Número do WhatsApp (com DDI):");
        String message = JOptionPane.showInputDialog(frame, "Mensagem:");
        if (number != null && message != null) {
            whatsappListModel.addElement("Número: " + number + ", Mensagem: " + message);
        }
    }

    private void addEmailFields() {
        String email = JOptionPane.showInputDialog(frame, "E-mail de destino:");
        String subject = JOptionPane.showInputDialog(frame, "Assunto:");
        String message = JOptionPane.showInputDialog(frame, "Mensagem:");
        if (email != null && subject != null && message != null) {
            emailListModel.addElement("E-mail: " + email + ", Assunto: " + subject + ", Mensagem: " + message);
        }
    }

    private void sendWhatsAppScheduled() {
        // Implementar a lógica para agendar mensagens no WhatsApp
        JOptionPane.showMessageDialog(frame, "Mensagens agendadas no WhatsApp!");
    }

    private void sendEmailScheduled() {
        String day = dayField.getText();
        String month = monthField.getText();
        String year = yearField.getText();
        String hour = hourField.getText();
        String minute = minuteField.getText();

        // Lógica de agendamento
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                for (int i = 0; i < emailListModel.size(); i++) {
                    String emailInfo = emailListModel.get(i);
                    String[] parts = emailInfo.split(", ");
                    String email = parts[0].split(": ")[1];
                    String subject = parts[1].split(": ")[1];
                    String message = parts[2].split(": ")[1];
                    sendEmail(email, subject, message);
                }
            }
        }, 10000); // Exemplo: 10 segundos para agendar

        JOptionPane.showMessageDialog(frame, "E-mails agendados com sucesso!");
    }

    private void sendEmail(String recipient, String subject, String message) {
        String senderEmail = "seuemail@gmail.com";
        String senderPassword = "suasenha";

        Properties properties = new Properties();
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.host", "smtp.gmail.com");
        properties.put("mail.smtp.port", "587");

        Session session = Session.getInstance(properties, new javax.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(senderEmail, senderPassword);
            }
        });

        try {
            Message msg = new MimeMessage(session);
            msg.setFrom(new InternetAddress(senderEmail));
            msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipient));
            msg.setSubject(subject);
            msg.setText(message);
            Transport.send(msg);
            System.out.println("E-mail enviado para: " + recipient);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(ScheduledMessageApp::new);
    }
}
