import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.File;

public class ScreenCaptureTool extends JFrame {
    private static Robot robot;
    private boolean alwaysOnTop = false;
    private JButton captureBtn, topBtn;
    private JLabel infoLabel;

    public ScreenCaptureTool() {
        try {
            robot = new Robot();
        } catch (AWTException e) {
            JOptionPane.showMessageDialog(null, "初始化截图引擎失败");
        }

        setTitle("IH 截图工具 v1.0 | Project7-AuroraLink");
        setSize(320, 160);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new FlowLayout(FlowLayout.CENTER, 10, 20));
        setLocationRelativeTo(null);

        infoLabel = new JLabel("快捷键: Ctrl + Shift + S 全屏截图");
        captureBtn = new JButton("手动截图");
        topBtn = new JButton("开启置顶");

        add(infoLabel);
        add(captureBtn);
        add(topBtn);

        // 置顶切换
        topBtn.addActionListener(e -> {
            alwaysOnTop = !alwaysOnTop;
            setAlwaysOnTop(alwaysOnTop);
            topBtn.setText(alwaysOnTop ? "取消置顶" : "开启置顶");
        });
        captureBtn.addActionListener(e -> startCapture());

        // 全局快捷键
        KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher(e -> {
            if (e.isControlDown() && e.isShiftDown() && e.getKeyCode() == KeyEvent.VK_S && e.getID() == KeyEvent.KEY_PRESSED) {
                startCapture();
                return true;
            }
            return false;
        });
    }

    // 修复预览白屏：改用原图自适应，禁止强制缩放
    private void startCapture() {
        setVisible(false);
        try { Thread.sleep(250); } catch (InterruptedException ignored) {}

        try {
            Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
            Rectangle rect = new Rectangle(screenSize);
            BufferedImage snapImg = robot.createScreenCapture(rect);

            // 预览窗口
            JFrame previewFrame = new JFrame("截图预览");
            previewFrame.setAlwaysOnTop(true);
            previewFrame.setDefaultCloseOperation(DISPOSE_ON_CLOSE);

            // 自适应面板，自动适配图片大小
            JPanel picPanel = new JPanel();
            picPanel.setLayout(new BorderLayout());
            JLabel picLabel = new JLabel(new ImageIcon(snapImg));
            JScrollPane scrollPane = new JScrollPane(picLabel); // 滚动防超大图白屏
            picPanel.add(scrollPane);
            previewFrame.add(picPanel);
            previewFrame.setSize(850, 550);
            previewFrame.setLocationRelativeTo(null);
            previewFrame.setVisible(true);

            // 自动保存
            String saveName = "IH_Snap_" + System.currentTimeMillis() + ".png";
            File saveFile = new File(saveName);
            ImageIO.write(snapImg, "png", saveFile);
            JOptionPane.showMessageDialog(this, "已保存至程序目录：" + saveName);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "截图异常：" + ex.getMessage());
        }
        setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new ScreenCaptureTool().setVisible(true));
    }
}