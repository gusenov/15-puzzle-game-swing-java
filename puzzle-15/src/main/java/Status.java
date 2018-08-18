import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;

/** Панель для информации (о результатах игры и т. п.). */
public class Status extends JPanel {

    /** Ссылка на управляющую панель. */
    private Control control;

    /** Ссылка на игровое поле. */
    private Board board;

    /** Текущее количество ходов (перемещений фишек). */
    private int moves;

    /** Время выделенное на игру (используется, если игра на время). */
    private long time;

    /** Метка показывающая надпись "Счётчик ходов: ". */
    private final JLabel lMovesTitle = new JLabel("Счётчик ходов: ");

    /** Метка для отображения количества ходов. */
    private final JLabel lMoves = new JLabel("0");

    /** Метка показывающая надпись "Время (сек.): ". */
    private final JLabel lTimeTitle = new JLabel("Время (сек.): ");

    /** Метка для отображения оставшего времени при игре на время. */
    private final JLabel lTime = new JLabel("0");

    /** Метка для отображения результатов игры. */
    private final JLabel lResult = new JLabel("Время вышло");

    /** Результат игры. */
    private Result result;

    /** Конструктор. */
    public Status() {
        createGUI();  // создать графический интерфейс.
    }

    /** Метод для создания графического интерфейса панели для информации о ходе игры. */
    private void createGUI() {
        setLayout(new BoxLayout(this, BoxLayout.LINE_AXIS));  // установка раскладки для компонентов.

        add(Box.createHorizontalGlue());  // отступ.

        // Количество ходов:
        add(lMovesTitle);
        add(Box.createRigidArea(new Dimension(5,0)));  // фиксированный отступ.
        add(lMoves);

        add(Box.createHorizontalGlue());  // отступ.

        // Оставшееся время игры:
        showTime(false);  // в самом начале не показываем время.
        add(lTimeTitle);
        add(Box.createRigidArea(new Dimension(5,0)));  // фиксированный отступ.
        add(lTime);

        add(Box.createHorizontalGlue());  // отступ.

        // Результаты игры:
        lResult.setVisible(false);  // в самом начале не показываем результаты игры.
        Font currentFont = lResult.getFont();  // текущий шрифт.
        lResult.setFont(new Font(currentFont.getFontName(), currentFont.getStyle(), 16));  // увеличиваем шрифт.
        add(lResult);

        add(Box.createHorizontalGlue());  // отступ.
    }

    /**
     * Показать или скрыть таймер.
     *
     * @param isVisible  true - показать; false - скрыть.
     */
    public void showTime(boolean isVisible) {
        lTime.setVisible(isVisible);
        lTimeTitle.setVisible(isVisible);
    }


    /** Получить количество ходов. */
    public int getMoves() {
        return moves;
    }

    /**
     * Установить количество ходов.
     *
     * @param moves  количество ходов.
     */
    public void setMoves(int moves) {
        this.moves = moves;
        lMoves.setText(Integer.toString(moves));
    }

    /** Увеличить счётчик ходов на единицу. */
    public void incMoves() {
        ++moves;
        lMoves.setText(Integer.toString(moves));
    }

    /** Уменьшить счётчик ходов на единицу. */
    public void decMoves() {
        --moves;
        lMoves.setText(Integer.toString(moves));
    }


    /**
     * Получить оставшееся время игры.
     *
     * @return  оставшееся время.
     */
    public long getTime() {
        return time;
    }

    /**
     * Установить оставшееся время игры.
     *
     * @param time  оставшееся время.
     */
    public void setTime(long time) {
        this.time = time;
        lTime.setText(Long.toString(time));  // обновить значение в метке для отображения времени.
    }


    /**
     * Показать или скрыть метку с результатами игры.
     *
     * @param isVisible  true - показать; false - скрыть.
     */
    private void showResult(boolean isVisible) {
        lResult.setVisible(isVisible);
    }

    /**
     * Получить результат игры.
     *
     * @return  результат игры: NONE, VICTORY, BAD_LUCK или TIME_IS_OVER.
     */
    public Result getResult() {
        return result;
    }

    /**
     * Установить результат игры.
     *
     * @param result  результат: NONE, VICTORY, BAD_LUCK или TIME_IS_OVER.
     */
    public void setResult(Result result) {
        this.result = result;
        switch (result) {
            case VICTORY:
                lResult.setForeground(Color.BLUE);  // цвет текста синий.
                lResult.setText("Победа");
                showResult(true);
                break;
            case BAD_LUCK:
                lResult.setForeground(Color.MAGENTA);  // цвет текста пурпурный.
                lResult.setText("Не повезло");
                showResult(true);
                break;
            case TIME_IS_OVER:
                lResult.setForeground(Color.RED);  // цвет текста красный.
                lResult.setText("Время вышло");
                showResult(true);
                break;
            case NONE:
                showResult(false);  // скрыть метку с результатами игры.
                return;
        }

        board.enableChips(false);  // выключить фишки, т.к. игра окончена.
        control.enableUndoMove(false);  // выключить кнопку отмены хода.
    }


    /**
     * Получить ссылку на панель управления.
     *
     * @return  ссылка на панель управления.
     */
    public Control getControl() {
        return control;
    }

    /**
     * Установить ссылку на панель управления.
     *
     * @param control  ссылка на панель управления.
     */
    public void setControl(Control control) {
        this.control = control;
    }


    /**
     * Получить ссылку на игровое поле.
     *
     * @return  ссылка на игровое поле.
     */
    public Board getBoard() {
        return board;
    }

    /**
     * Установить ссылку на игровое поле.
     *
     * @param board  ссылка на игровое поле.
     */
    public void setBoard(Board board) {
        this.board = board;
    }

}
