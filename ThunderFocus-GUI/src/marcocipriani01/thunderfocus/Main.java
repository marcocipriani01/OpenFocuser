package marcocipriani01.thunderfocus;

import com.formdev.flatlaf.FlatDarkLaf;
import com.formdev.flatlaf.FlatLightLaf;
import marcocipriani01.simplesocket.ConnectionException;
import marcocipriani01.thunderfocus.board.ThunderFocuser;
import marcocipriani01.thunderfocus.indi.INDIServerCreator;

import javax.swing.*;
import javax.swing.event.HyperlinkEvent;
import java.awt.*;
import java.io.IOException;
import java.net.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Enumeration;
import java.util.Locale;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.jar.Attributes;
import java.util.jar.Manifest;

public class Main {

    public static final ResourceBundle RES_BUNDLE = ResourceBundle.getBundle("marcocipriani01.thunderfocus.lang");
    public static final String APP_NAME = i18n("app.name");
    public static final Image APP_LOGO = Toolkit.getDefaultToolkit().getImage(
            Main.class.getResource("/marcocipriani01/thunderfocus/res/ThunderFocus.png"));
    public static final ThunderFocuser focuser = new ThunderFocuser();
    public static final INDIServerCreator indiServerCreator = new INDIServerCreator();
    public static final OperatingSystem OPERATING_SYSTEM = getOperatingSystem();
    public static final Settings settings = Settings.load();
    public static ASCOMFocuserBridge ascomFocuserBridge;
    private static Path pidLock;

    public static void main(String[] args) {
        try {
            switch (settings.getTheme()) {
                case LIGHT -> FlatLightLaf.install();
                case DARK -> FlatDarkLaf.install();
                case SYSTEM -> UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            pidLock = Paths.get(Settings.getSettingsFolder() + "PID.lock");
            String pid = String.valueOf(ProcessHandle.current().pid());
            if (pidLock.toFile().exists()) {
                try {
                    Optional<ProcessHandle> processHandle = ProcessHandle.of(Long.parseLong(Files.readString(pidLock).replace("\n", "").trim()));
                    if (processHandle.isPresent()) {
                        ProcessHandle presentHandle = processHandle.get();
                        if (presentHandle.isAlive()) {
                            Optional<String> command = presentHandle.info().command();
                            if (command.isPresent() && command.get().contains("java") &&
                                    (JOptionPane.showConfirmDialog(null, APP_NAME + i18n("is.already.running"),
                                            APP_NAME, JOptionPane.OK_CANCEL_OPTION, JOptionPane.ERROR_MESSAGE) == JOptionPane.CANCEL_OPTION)) {
                                try {
                                    Thread.sleep(100);
                                } catch (InterruptedException ignored) {
                                }
                                return;
                            }
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            Files.write(pidLock, pid.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
        SwingUtilities.invokeLater(MainWindow::new);
    }

    public static String i18n(String id) {
        return RES_BUNDLE.getString(id);
    }

    private static OperatingSystem getOperatingSystem() {
        String os = System.getProperty("os.name", "generic").toLowerCase(Locale.ENGLISH);
        if (os.contains("win")) {
            return OperatingSystem.WINDOWS;
        } else if (os.contains("nux")) {
            return OperatingSystem.LINUX;
        } else if ((os.contains("mac")) || (os.contains("darwin"))) {
            return OperatingSystem.MACOS;
        }
        return OperatingSystem.OTHER;
    }

    public static boolean isAscomRunning() {
        return (ascomFocuserBridge != null) && (ascomFocuserBridge.isConnected());
    }

    public static void exit(int code) {
        try {
            Files.deleteIfExists(pidLock);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (isAscomRunning()) {
            try {
                ascomFocuserBridge.close();
            } catch (ConnectionException e) {
                e.printStackTrace();
            }
        }
        //indiServerCreator.stop();
        if (focuser.isConnected()) {
            focuser.disconnect();
        }
        System.exit(code);
    }

    public static String getIP(boolean localhost) throws SocketException, IllegalStateException {
        if (localhost) return "localhost";
        Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
        while (interfaces.hasMoreElements()) {
            NetworkInterface networkInterface = interfaces.nextElement();
            if (networkInterface.isLoopback() || !networkInterface.isUp()) continue;
            Enumeration<InetAddress> addresses = networkInterface.getInetAddresses();
            while (addresses.hasMoreElements()) {
                InetAddress address = addresses.nextElement();
                if (address instanceof Inet6Address) continue;
                return address.getHostAddress();
            }
        }
        throw new IllegalStateException("No network interface found.");
    }

    public static void openBrowser(String url, JFrame frame) {
        try {
            Desktop desktop;
            if (Desktop.isDesktopSupported() && (desktop = Desktop.getDesktop()).isSupported(Desktop.Action.BROWSE)) {
                desktop.browse(new URI(url));
            } else if (Main.OPERATING_SYSTEM == OperatingSystem.LINUX) {
                Runtime.getRuntime().exec("xdg-open " + url);
            } else {
                throw new UnsupportedOperationException("Browser support not found.");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(frame,
                    i18n("browser.error"), APP_NAME, JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void openBrowser(HyperlinkEvent uri, JFrame frame) {
        openBrowser(uri.getDescription().replace("\\", ""), frame);
    }

    public static String getAppVersion() {
        try {
            Enumeration<URL> resources = Main.class.getClassLoader().getResources("META-INF/MANIFEST.MF");
            while (resources.hasMoreElements()) {
                Attributes attributes = new Manifest(resources.nextElement().openStream()).getMainAttributes();
                if (attributes.getValue("Specification-Title").equals(APP_NAME)) {
                    String version = attributes.getValue("Specification-Version");
                    if (version != null) return version;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public enum OperatingSystem {
        WINDOWS, MACOS, LINUX, OTHER
    }
}