import java.awt.Font;
import javax.swing.JButton;

/** Фишка с номером, которую перемещают по игровому полю. */
public class Chip extends JButton {

    /** Номер фишки. */
    private int number;

    /**
     * Конструктор.
     *
     * @param number
     */
    public Chip(int number) {
        // Вызов родительского конструктора,
        // с передачей в качестве метки номера фишки:
        super(Integer.toString(number));

        this.number = number;

        // Установка шрифта для числовой метки на фишке:
        Font currentFont = this.getFont();  // получение текущего шрифта.
        this.setFont(new Font(currentFont.getFontName(), currentFont.getStyle(), 16));
    }

    /**
     * Получить номер фишки.
     *
     * @return  номер фишки.
     */
    public int getNumber() {
        return number;
    }

    /**
     * Установить номер фишки.
     *
     * @param number  номер фишки.
     */
    public void setNumber(int number) {
        this.number = number;
    }

}
