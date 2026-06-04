import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ScreenCaptureApp extends JFrame {
    private JCheckBox alwaysOnTopBox;
    private JButton captureBtn;
    private JButton chooseDirBtn;
    private JLabel statusLabel;
    private File saveDir;

    public ScreenCaptureApp() {
        super("Java 截图工具");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(360, 140);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(8, 8));

        JPanel top = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 8));
        alwaysOnTopBox = new JCheckBox("置顶");
        alwaysOnTopBox.addActionListener(e -> setAlwaysOnTop(alwaysOnTopBox.isSelected()));
        captureBtn = new JButton("截图 (Ctrl+Shift+S)");
        chooseDirBtn = new JButton("选择保存目录");

        top.add(alwaysOnTopBox);
        top.add(captureBtn);
        top.add(chooseDirBtn);

        add(top, BorderLayout.NORTH);

        statusLabel = new JLabel("就绪");
        add(statusLabel, BorderLayout.SOUTH);

        // 默认保存到桌面/截图文件夹
        String userHome = System.getProperty("user.home");
        File pictures = new File(userHome, "Pictures");
        saveDir = new File(pictures, "java-screenshots");
        if (!saveDir.exists()) saveDir.mkdirs();

        captureBtn.addActionListener(e -> doCapture());
        chooseDirBtn.addActionListener(e -> chooseDirectory());

        // 当窗口有焦点或在其窗口范围内，按 Ctrl+Shift+S 触发截图
        JRootPane root = getRootPane();
        KeyStroke ks = KeyStroke.getKeyStroke(KeyEvent.VK_S, KeyEvent.CTRL_DOWN_MASK | KeyEvent.SHIFT_DOWN_MASK);
        root.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(ks, "capture");
        root.getActionMap().put("capture", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                doCapture();
            }
        });

        setVisible(true);
    }

    private void chooseDirectory() {
        JFileChooser chooser = new JFileChooser(saveDir);
        chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        int r = chooser.showOpenDialog(this);
        if (r == JFileChooser.APPROVE_OPTION) {
            saveDir = chooser.getSelectedFile();
            statusLabel.setText("保存目录: " + saveDir.getAbsolutePath());
        }
    }

    private void doCapture() {
        try {
            setVisible(true); // ensure window exists
            // small delay to allow UI to update / hide if needed
            Thread.sleep(100);
            Robot robot = new Robot();
            Rectangle screenRect = new Rectangle(Toolkit.getDefaultToolkit().getScreenSize());
            BufferedImage image = robot.createScreenCapture(screenRect);

            String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
            File out = new File(saveDir, "screenshot_" + timestamp + ".png");
            ImageIO.write(image, "png", out);

            statusLabel.setText("已保存: " + out.getAbsolutePath());
            // 显示缩略预览
            showPreview(image, out.getName());
        } catch (Exception ex) {
            statusLabel.setText("截图失败: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    private void showPreview(BufferedImage image, String title) {
        ImageIcon icon = new ImageIcon(image.getScaledInstance(600, -1, Image.SCALE_SMOOTH));
        JLabel pic = new JLabel(icon);
        JScrollPane scroll = new JScrollPane(pic);
        JFrame preview = new JFrame("预览: " + title);
        preview.getContentPane().add(scroll);
        preview.setSize(800, 600);
        preview.setLocationRelativeTo(this);
        preview.setVisible(true);
    }

    public static void main(String[] args) {
        // 在事件分发线程中启动
        SwingUtilities.invokeLater(() -> new ScreenCaptureApp());
    }
}
