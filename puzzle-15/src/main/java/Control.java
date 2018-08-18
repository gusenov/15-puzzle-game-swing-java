import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import java.util.Timer;
import java.util.TimerTask;

/** Управляющая панель. */
public class Control extends JPanel {

    /** Ссылка на панель для информации (о результатах игры и т. п.). */
    private Status status;

    /** Ссылка на игровое поле. */
    private Board board;

    /** Кнопка для начала новой игры. */
    private final JButton bNewGame = new JButton("Новая игра");

    /** Кнопка для начала новой игры на время. */
    private final JButton bNewGameUltimatum = new JButton("Новая игра на время");

    /** Таймер. */
    private Timer timer;

    /** Переменная для хранения времени начала игры на время. */
    long startTime = 0;

    /** Кнопка для отмены хода. */
    private final JButton bUndoMove = new JButton("Отменить ход");

    /** Кнопка для выхода из игры. */
    private final JButton bExit = new JButton("Выход");

    /** Конструктор. */
    public Control() {
        createGUI();
    }

    /** Метод для создания графического интерфейса управляющей панели. */
    private void createGUI() {
        setLayout(new BoxLayout(this, BoxLayout.LINE_AXIS));  // установка раскладки.

        bUndoMove.setEnabled(false);  // в самом начале кнопка отмены хода выключена.

        add(Box.createHorizontalGlue());  // отступ.
        add(bNewGame);  // добавление на форму кнопки "Новая игра".
        add(Box.createHorizontalGlue());  // отступ.
        add(bNewGameUltimatum);  // добавление на форму кнопки "Новая игра на время".
        add(Box.createHorizontalGlue());  // отступ.
        add(bUndoMove);  // добавление на форму кнопки "Отменить ход".
        add(Box.createHorizontalGlue());  // отступ.
        add(bExit);  // добавление на форму кнопки "Выход".
        add(Box.createHorizontalGlue());  // отступ.

        // Код выполняющийся при нажатии на кнопку "Новая игра":
        bNewGame.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {

                board.enableChips(true);  // включить фишки, чтобы их можно было перемещать.
                board.fill();  // заполнить игровое поле фишками в случайном порядке.
                enableUndoMove(false);  // в самом начале игры отключить кнопку отмены хода.

                status.setMoves(0);  // в самом начале игры счётчик количества ходов равен 0.

                status.showTime(false);  // выключить индикатор времени, т.к. игра не на время.
                status.setResult(Result.NONE);  // в самом начале игры результат не определён.

                // Если работает таймер от предыдущей игры на время, то выключить его:
                if (startTime != 0) {
                    timer.cancel();
                    timer.purge();
                    startTime = 0;
                }
            }
        });

        // Код выполняющийся при нажатии на кнопку "Новая игра на время":
        bNewGameUltimatum.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                int sec = inputTime();  // ввод времени отпущенного на игру.

                if (sec > 0) {

                    board.enableChips(true);  // включить фишки, чтобы их можно было перемещать.
                    board.fill();  // заполнить игровое поле фишками в случайном порядке.
                    enableUndoMove(false);  // в самом начале игры отключить кнопку отмены хода.

                    status.setMoves(0);  // в самом начале игры счётчик количества ходов равен 0.
                    status.setResult(Result.NONE);  // в самом начале игры результат не определён.

                    // Если работает таймер от предыдущей игры на время, то выключить его:
                    if (startTime != 0) {
                        timer.cancel();
                        timer.purge();
                        startTime = 0;
                    }

                    startTime = System.currentTimeMillis();  // запомнить время начала игры.
                    timer = new Timer();  // создать новый таймер
                    timer.schedule(new TimerTask() {  // и запустить его:
                        @Override
                        public void run() {
                            long stopTime = System.currentTimeMillis();  // время текущего момента.

                            // Если появился результат игры, то:
                            if (status.getResult() != Result.NONE) {
                                // Выключить таймер:
                                timer.cancel();
                                timer.purge();
                                startTime = 0;
                                return;  // выйти из кода таймера.
                            }

                            // Если время вышло:
                            if ((stopTime - startTime) / 1000 > sec) {
                                // Выключить таймер:
                                timer.cancel();
                                timer.purge();
                                startTime = 0;

                                status.setResult(Result.TIME_IS_OVER);  // показать сообщение о том, что время вышло.

                            } else {  // иначе, если время еще есть, то:
                                status.setTime(sec - ((stopTime - startTime) / 1000));  // показать сколько осталось.
                            }
                        }

                    }, 0, 1000);  // повторять код таймера каждую секунду.

                    status.showTime(true);  // включить индикатор времени, т.к. игра на время.
                }

            }
        });

        // Код выполняющийся при нажатии на кнопку "Отменить ход":
        bUndoMove.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                board.undoMove();  // вызов метода отмены хода у объекта представляющего собой игровое поле.
            }
        });

        // Код выполняющийся при нажатии на кнопку "Выход":
        bExit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                System.exit(0);  // завершить выполнение программы.
            }
        });
    }

    /**
     * Метод для ввода количества времени в секундах перед игрой на время.
     *
     * @return  число представляющее время в секундах или -1, если пользователь отменил ввод.
     */
    private int inputTime() {
        // Показать окно для ввода информации:
        String sec = (String)JOptionPane.showInputDialog(this,
                "Введите время в секундах: ",
                "Игра на время",
                JOptionPane.QUESTION_MESSAGE,
                null,
                null,
                "60");

        if (sec != null) {
            return Integer.valueOf(sec);  // преобразование введенной строки в целое число.
        } else {
            return -1;
        }
    }


    /**
     * Получить ссылку на панель информации.
     *
     * @return  ссылка на панель информации.
     */
    public Status getStatus() {
        return status;
    }

    /**
     * Установить ссылку на панель информации.
     *
     * @param status  ссылка на панель информации.
     */
    public void setStatus(Status status) {
        this.status = status;
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


    /**
     * Включить или выключить кнопку отмены хода.
     *
     * @param isEnabled  true - включить кнопку; false - выключить.
     */
    public void enableUndoMove(boolean isEnabled) {
        bUndoMove.setEnabled(isEnabled);
    }
}
