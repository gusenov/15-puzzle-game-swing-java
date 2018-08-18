import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

/**
 * Главный класс являющийся точкой сборки всего воедино.
 */
public class Main {

    /** Главный фрейм. */
    private final JFrame frame = new JFrame("15");

    /** Главная панель. */
    private final JPanel panel = new JPanel();

    /** Заголовок. */
    private final JLabel title = new JLabel("Игра \"Пятнадцать\"");

    /** Ссылка на управляющую панель. */
    private final Control control = new Control();

    /** Ссылка на панель для информации (о результатах игры и т. п.). */
    private final Status status = new Status();

    /** Ссылка на игровое поле. */
    private final Board board = new Board(4, 4);

    /** Конструктор. */
    public Main() {

        // При закрытии окна нужно завершать программу:
        frame.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                System.exit(0);  // выход.
            }
        });

        // Внедрение зависимостей:

        // Панели управления нужен доступ к:
        control.setStatus(status);  // панели информации,
        control.setBoard(board);  // игровому полю.

        // Панели информации нужен доступ к:
        status.setControl(control);  // панели управления,
        status.setBoard(board);  // игровому полю.

        // Игровому полю нужен доступ к:
        board.setControl(control);  // панели управления,
        board.setStatus(status);  // панели информации.
    }

    /** Метод для создания графического интерфейса игры. */
    public void createAndShowGUI() {
        frame.setSize(640,640);  // размеры окна.
        frame.setResizable(false);  // окно не доступно для изменения размеров.

        panel.setLayout(new BoxLayout(panel, BoxLayout.PAGE_AXIS));  // раскладка для главной панели.
        panel.setBorder(new EmptyBorder(10, 10, 10, 10));  // отступ от краев.

        // Заголовок:
        Font currentFont = title.getFont();  // текущиий шрифт.
        title.setFont(new Font(currentFont.getFontName(), currentFont.getStyle(), 24));  // увеличиваем шрифт.
        title.setAlignmentX(Component.LEFT_ALIGNMENT);  // выравнивание.
        panel.add(title);  // добавление заголовка на форму.

        panel.add(Box.createRigidArea(new Dimension(0,16)));  // отступ после заголовка.

        // Панель управления игрой:
        control.setAlignmentX(Component.LEFT_ALIGNMENT);  // выравнивание.
        panel.add(control);  // добавление панели управления игрой на форму.

        panel.add(Box.createRigidArea(new Dimension(0,16)));  // отступ после панели управления.

        // Панель информации для отображения сведений о ходе игры:
        status.setAlignmentX(Component.LEFT_ALIGNMENT);  // выравнивание.
        panel.add(status);  // добавление панели информации о ходе игры на форму.

        panel.add(Box.createRigidArea(new Dimension(0,16)));  // отступ после панели информации.

        // Игровое поле для перемещения фишек:
        board.setAlignmentX(Component.LEFT_ALIGNMENT);  // выравнивание.
        panel.add(board);  // добавление игрового поля на форму.

        frame.add(panel);  // добавление главной панели в главный фрейм.
        frame.setVisible(true);  // делаем фрейм видимым.
    }

    /**
     * Главный метод, который является точкой входа в программу.
     *
     * @param args  аргументы.
     */
    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                Main main = new Main();  // создать экземпляр главного класса.
                main.createAndShowGUI();  // создать и показать пользователю графический интерфейс.
            }
        });
    }

}