import java.awt.BorderLayout;
import javax.swing.BorderFactory;
import javax.swing.JPanel;

/** Ячейка на игровом поле. */
public class Cell extends JPanel {

    /** Номер позиции ячейки на игровом поле. */
    private int position;

    /**
     * Конструктор.
     *
     * @param position  номер позиции ячейки на игровом поле.
     */
    public Cell(int position) {
        this.position = position;

        createGUI();  // создать визуальный интерфейс ячейки.
    }

    /** Создать визуальный интерфейс ячейки. */
    private void createGUI() {
        setBorder(BorderFactory.createLoweredBevelBorder());  // рамка для ячейки.
        setLayout(new BorderLayout());  // тип раскладки внутри ячейки.
    }

    /**
     * Метод для проверки заполненности ячейки.
     *
     * @return  true - если в ячейке есть фишка; иначе - false.
     */
    public boolean isEmpty() {
        // Если количество компонентов равно 0, то ячейка пустая:
        return getComponentCount() == 0;
    }

    /**
     * Получить фишку находящуюся в ячейке.
     *
     * @return  null - если ячейка пустая; иначе - фишка.
     */
    public Chip getChip() {
        if (isEmpty()) {  // проверка ячейки на пустоту.
            return null;
        } else {
            // Вовзращаем первый компонент внутри панели, считая, что это фишка:
            return (Chip) getComponent(0);
        }
    }

    /**
     * Получить номер фишки, которая сейчас в этой ячейке.
     *
     * @return  -1 - если ячейка пустая; иначе - номер фишки.
     */
    public int getChipNumber() {
        if (isEmpty()) {  // проверка ячейки на пустоту.
            return -1;
        } else {
            Chip chip = getChip();  // получение фишки находящейся в данной ячейке.
            return chip.getNumber();  // возврат номера фишки.
        }
    }

    /**
     * Получить номер ячейки на игровом поле.
     *
     * @return  номер ячейки.
     */
    public int getPosition() {
        return position;
    }

    /**
     * Установить номер для ячейки.
     *
     * @param position  номер ячейки.
     */
    public void setPosition(int position) {
        this.position = position;
    }

}
